package com.orango.electronic.orangetxusb.mainActivity

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.orango.electronic.orangetxusb.UsbPad.PadSelect
import com.orango.electronic.orangetxusb.ManualActivity.Users_Manual
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.SettingPagr.Setting
import kotlinx.android.synthetic.main.fragment_home.view.*
import android.net.Uri
import java.lang.Exception


class HomeFragment : Fragment() {
    companion object {
        val TAG = "HomeFragment"
    }



    lateinit var navActivity: NavigationActivity
    lateinit var rootView: View
    lateinit var programSensorBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        navActivity = activity as NavigationActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home,container,false)
        val profilePreferences = navActivity.getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val a= profilePreferences.getString("Language","English")
        if(a=="Italiano"){
            rootView.online_shopping_btn.setBackgroundResource(R.mipmap.setting)
            rootView.Setim.visibility=View.GONE
            rootView.textView68.visibility=View.GONE
        }
        navActivity.back.visibility=View.GONE
        programSensorBtn = rootView.findViewById(R.id.program_sensor_btn)
        programSensorBtn.setOnClickListener {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, AreaFragment(), "Select Area")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Select Area")
                // 提交事務
                .commit()
        }
        rootView.program_pad_btn.setOnClickListener {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, PadSelect(), "PadSelect")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("PadSelect")
                // 提交事務
                .commit()
        }
        rootView.user_manual_btn.setOnClickListener {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                Users_Manual(), "UsersManual")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("UsersManual")
                // 提交事務
                .commit()
        }
        rootView.Setim.setOnClickListener {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                Setting(), "Setting")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Setting")
                // 提交事務
                .commit()
        }
        rootView.online_shopping_btn.setOnClickListener {
            val profilePreferences = navActivity.getSharedPreferences("Setting", Context.MODE_PRIVATE)
            val a= profilePreferences.getString("Language","English")
            var uti="http://simple-sensor.com"
            when(a){
                "繁體中文"->{ uti="http://simple-sensor.com"}
                "简体中文"->{ uti="http://simple-sensor.com"}
                "Deutsche"->{ uti="http://orange-rdks.de"}
                "English"->{ uti="http://simple-sensor.com"}
                "Italiano"->{
//                    uti="http://orange-like.it"
                    val transaction = fragmentManager!!.beginTransaction()
                    transaction.replace(R.id.nav_host_fragment,
                        Setting(), "Setting")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        .addToBackStack("Setting")
                        // 提交事務
                        .commit()
                    return@setOnClickListener
                }
            }
            try {
                val uri = Uri.parse(uti)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                navActivity.startActivity(intent)
            }catch ( e:Exception){}

        }

        checkmmy()
        return rootView
    }
    var handler= Handler()
fun checkmmy(){
    Thread{
        handler.post {
            if(navActivity.HaveMMy){
                rootView.program_sensor_btn.isEnabled=true
                rootView.program_pad_btn.isEnabled=true
                rootView.user_manual_btn.isEnabled=true
                rootView.Setim.isEnabled=true
            }else{checkmmy()}
        }
    }.start()

}
    override fun onResume() {
        super.onResume()
        (activity as NavigationActivity).setActionBarTitle("Orange TPMS")
navActivity.RightTop.setBackgroundResource(R.mipmap.exit)
        navActivity.RightTop.visibility=View.VISIBLE
        navActivity.RightTop.setOnClickListener {
            var intent=Intent(navActivity,Logout::class.java)
            startActivity(intent)
        }
    }

}
