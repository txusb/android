package com.orango.electronic.orangetxusb.SettingPagr


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.HomeFragment
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.tool.FileDowload
import com.orango.electronic.orangetxusb.tool.FileDowload.DonloadMuc
import com.orango.electronic.orangetxusb.tool.FileDowload.DonloadMucCabel
import kotlinx.android.synthetic.main.fragment_update.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 *
 */
class Update : Fragment() {
lateinit var rootview:View
    lateinit var act:NavigationActivity
    var progress=false
    var mcpass="0"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       rootview=inflater.inflate(R.layout.fragment_update, container, false)
        act=activity!! as NavigationActivity
        rootview.button13.setOnClickListener {
            if(act.command.AppverInspire.equals(act.command.Appver)){
                Toast.makeText(act,resources.getString(R.string.is_latest),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(progress){
                return@setOnClickListener
            }
            mcpass="0"
            UpdateMuc()

        }
        act.loading(0)
        donloadmcu()
        return rootview
    }
    fun donloadmcu(){
        rootview.button13.isEnabled=false
        Thread{
            val a=DonloadMuc(act)
            val b=DonloadMucCabel(act)
            if(a&&b){
                handler.post {
                    rootview.button13.isEnabled=true
                    act.LoadingSuccess()
                }
            }else{
                donloadmcu()
            }
        }.start()

    }
fun UpdateMuc(){
    act.command.HandShake(mcpass)
    progress=true
    act.update(0)
    act.back.isEnabled=false
    Thread{
        when(act.command.HandShake(mcpass)){
            0->{
                if(act.command.GoProgram(mcpass)){
                    val a=act.command.WriteFlash(act,132,"CABLE",mcpass)
                    handler.post {
                        act.LoadingSuccess()
                        if(a){
                            Toast.makeText(act,resources.getString(R.string.update_success),Toast.LENGTH_SHORT).show()
                            val transaction = fragmentManager!!.beginTransaction()
                            transaction.add(R.id.nav_host_fragment, HomeFragment(), "Home")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                // 提交事務
                                .commit()
                        }else{
                            Toast.makeText(act,resources.getString(R.string.error),Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    handler.post {
                        act.LoadingSuccess()
                        Toast.makeText(act,resources.getString(R.string.error),Toast.LENGTH_SHORT).show()
                    }
                }
                progress=false

            }
            1->{
                if(act.command.GoProgram(mcpass)){
                    val a=act.command.WriteFlash(act,132,"PAD",mcpass)
                    handler.post {
                        act.LoadingSuccess()
                        if(a){
                            if(mcpass.equals("1")){    Toast.makeText(act,resources.getString(R.string.update_success),Toast.LENGTH_SHORT).show()
                                val transaction = fragmentManager!!.beginTransaction()
                                transaction.add(R.id.nav_host_fragment, HomeFragment(), "Home")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                    // 提交事務
                                    .commit()  ;progress=false}else{
                                Thread.sleep(2000)
                                mcpass="1";UpdateMuc()}
                        }else{
                            Toast.makeText(act,resources.getString(R.string.error),Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    handler.post {
                        act.LoadingSuccess()
                        Toast.makeText(act,resources.getString(R.string.error),Toast.LENGTH_SHORT).show()
                    }
                }
            }
            2->{
                if(act.command.A0xEB()){
                    handler.post { UpdateMuc() }
                }else{
                    handler.post {
                        act.LoadingSuccess()
                        Toast.makeText(act,resources.getString(R.string.error),Toast.LENGTH_SHORT).show()}}
                progress=false
            }
            -1->{
                handler.post {
                    Toast.makeText(act,resources.getString(R.string.error),Toast.LENGTH_SHORT).show()
                }
                progress=false
            }
        }
        handler.post {    act.back.isEnabled=true }

    }.start()
}
var handler=Handler()
    override fun onResume() {
        super.onResume()
        (activity as NavigationActivity).back.visibility=View.VISIBLE
    }

}
