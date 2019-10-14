package com.orango.electronic.orangetxusb.UsbPad


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.ScanBle
import com.orango.electronic.orangetxusb.mainActivity.MakeFragment
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.mainActivity.QrcodeScanner
import kotlinx.android.synthetic.main.fragment_area.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PadSelect : Fragment() {
    lateinit var rootView: View
    lateinit var Idcopy: Button
    lateinit var Program: Button
    lateinit var act: NavigationActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_pad_select, container, false)
        act=activity as NavigationActivity
        NavigationActivity.PAD_OR_USB="PAD"
        act.back.visibility=View.VISIBLE
        rootView.scan.setOnClickListener {
            act.scanorselect=0
            RequestPermission()
        }
        rootView.serch.setOnClickListener {
            act.scanorselect=1
            if(act.connected==NavigationActivity.Connected.True){
                act.WaitBle()
                Thread{
                    val a=act.command.Command03()
                    if(a&&act.command.IC>0){
                        Log.d("IC數量","${act.command.IC}")
                        val transaction = fragmentManager!!.beginTransaction()
                        transaction.replace(R.id.nav_host_fragment, MakeFragment(), "Vehicle Selection")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                            .addToBackStack("Vehicle Selection")
                            .commit()
                    }else{
                        if(act.bleServiceControl.isconnect){
                            val a=act.command.Command03()
                            handle.post {
                                act.LoadingSuccess()
                                if(a){
                                    val transaction = fragmentManager!!.beginTransaction()
                                    transaction.replace(R.id.nav_host_fragment, MakeFragment(), "Vehicle Selection")
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                        .addToBackStack("Vehicle Selection")
                                        .commit()
                                }else{

                                    var intent= Intent(act, ScanBle::class.java)
                                    act.startActivity(intent)
                                }
                            }
                        }else{
                            handle.post {
                                var intent= Intent(act, ScanBle::class.java)
                                act.startActivity(intent)}
                        }
                    }
                    handle.post {
                        act.LoadingSuccess()}
                }.start()
            }else{
                if(act.bleServiceControl.isconnect){
                    act.WaitBle()
                    Thread{
                        val a=act.command.Command03()
                        handle.post {
                            act.LoadingSuccess()
                            if(a){
                                val transaction = fragmentManager!!.beginTransaction()
                                transaction.replace(R.id.nav_host_fragment, MakeFragment(), "Vehicle Selection")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                    .addToBackStack("Vehicle Selection")
                                    .commit()
                            }else{
                                var intent= Intent(act, ScanBle::class.java)
                                act.startActivity(intent)
                            }
                        }
                    }.start()
                }else{
                    var intent= Intent(act, ScanBle::class.java)
                    act.startActivity(intent)
                }
            }
        }
        Idcopy=rootView.findViewById(R.id.Idcopy)
        Program =rootView.findViewById(R.id.Program)
        Idcopy.setOnClickListener{
            idcopy()
        }
        Program.setOnClickListener{
           program()
        }
        when(NavigationActivity.Action){
            "IDCOPY"->{idcopy()}
            "PROGRAM"->{program()}
        }
        return rootView
    }
    var handle= Handler()
    fun idcopy(){
        NavigationActivity.Action="IDCOPY"

        Idcopy.background=resources.getDrawable(R.drawable.solid,null)
        Program.background=resources.getDrawable(R.drawable.stroke,null)
        Idcopy.setTextColor(resources.getColor(R.color.white))
        Program.setTextColor(resources.getColor(R.color.buttoncolor))
    }
    fun program(){ NavigationActivity.Action="PROGRAM"
        Program.background=resources.getDrawable(R.drawable.solid,null)
        Idcopy.background=resources.getDrawable(R.drawable.stroke,null)
        Idcopy.setTextColor(resources.getColor(R.color.buttoncolor))
        Program.setTextColor(resources.getColor(R.color.white))}
    override fun onResume() {
        super.onResume()
        (activity as NavigationActivity).setActionBarTitle(activity!!.resources.getString(R.string.Program_USB_PAD))
        (activity as NavigationActivity).RightTop.setBackgroundResource(R.mipmap.icon_link_pad)
        (activity as NavigationActivity).RightTop.setOnClickListener { }
            if((activity as NavigationActivity).bleServiceControl.isconnect){
                (activity as NavigationActivity).RightTop.visibility=View.VISIBLE
                (activity as NavigationActivity).RightTop.setBackgroundResource(R.mipmap.icon_link_pad)
                (activity as NavigationActivity).RightTop.setOnClickListener { }
            }else{  (activity as NavigationActivity).RightTop.visibility=View.GONE}
    }
    fun RequestPermission() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(activity!!)
                    .setCancelable(false)
                    .setTitle("需要相機權限")
                    .setMessage("需要相機權限才能掃描BARCODE!")
                    .setPositiveButton(
                        "確認"
                    ) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.CAMERA),
                            1
                        )
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), 1)
            }
        }else{
            if(act.connected==NavigationActivity.Connected.True){
                act.WaitBle()
                Thread{
                    val a=act.command.Command03()
                    if(a&&act.command.IC>0){
                        Log.d("IC數量","${act.command.IC}")
                        val transaction = fragmentManager!!.beginTransaction()
                        transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                            .addToBackStack("Scanner")
                            // 提交事務
                            .commit()
                    }else{
                        if(act.bleServiceControl.isconnect){
                            val a=act.command.Command03()
                            handle.post {
                                act.LoadingSuccess()
                                if(a){
                                    val transaction = fragmentManager!!.beginTransaction()
                                    transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                        .addToBackStack("Scanner")
                                        // 提交事務
                                        .commit()
                                }else{

                                    var intent= Intent(act, ScanBle::class.java)
                                    act.startActivity(intent)
                                }
                            }
                        }else{
                            handle.post {
                                var intent= Intent(act, ScanBle::class.java)
                                act.startActivity(intent)}
                        }
                    }
                    handle.post {
                        act.LoadingSuccess()}
                }.start()
            }else{
                if(act.bleServiceControl.isconnect){
                    act.WaitBle()
                    Thread{
                        val a=act.command.Command03()
                        handle.post {
                            act.LoadingSuccess()
                            if(a){
                                val transaction = fragmentManager!!.beginTransaction()
                                transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                    .addToBackStack("Scanner")
                                    // 提交事務
                                    .commit()
                            }else{
                                var intent= Intent(act, ScanBle::class.java)
                                act.startActivity(intent)
                            }
                        }
                    }.start()
                }else{
                    var intent= Intent(act, ScanBle::class.java)
                    act.startActivity(intent)
                }
            }
          }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 ->
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults.indices) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            if(act.connected==NavigationActivity.Connected.True){
                                act.WaitBle()
                                Thread{
                                    val a=act.command.Command03()
                                    if(a&&act.command.IC>0){
                                        Log.d("IC數量","${act.command.IC}")
                                        val transaction = fragmentManager!!.beginTransaction()
                                        transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                            .addToBackStack("Scanner")
                                            // 提交事務
                                            .commit()
                                    }else{
                                        if(act.bleServiceControl.isconnect){
                                            val a=act.command.Command03()
                                            handle.post {
                                                act.LoadingSuccess()
                                                if(a){
                                                    val transaction = fragmentManager!!.beginTransaction()
                                                    transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                                        .addToBackStack("Scanner")
                                                        // 提交事務
                                                        .commit()
                                                }else{

                                                    var intent= Intent(act, ScanBle::class.java)
                                                    act.startActivity(intent)
                                                }
                                            }
                                        }else{
                                            handle.post {
                                                var intent= Intent(act, ScanBle::class.java)
                                                act.startActivity(intent)}
                                        }
                                    }
                                    handle.post {
                                        act.LoadingSuccess()}
                                }.start()
                            }else{
                                if(act.bleServiceControl.isconnect){
                                    act.WaitBle()
                                    Thread{
                                        val a=act.command.Command03()
                                        handle.post {
                                            act.LoadingSuccess()
                                            if(a){
                                                val transaction = fragmentManager!!.beginTransaction()
                                                transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                                    .addToBackStack("Scanner")
                                                    // 提交事務
                                                    .commit()
                                            }else{
                                                var intent= Intent(act, ScanBle::class.java)
                                                act.startActivity(intent)
                                            }
                                        }
                                    }.start()
                                }else{
                                    var intent= Intent(act, ScanBle::class.java)
                                    act.startActivity(intent)
                                }
                            }
                        }
                    }
                }
        }
    }
}
