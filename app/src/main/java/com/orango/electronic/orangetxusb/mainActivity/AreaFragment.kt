package com.orango.electronic.orangetxusb.mainActivity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.orango.electronic.orangetxusb.R
import kotlinx.android.synthetic.main.fragment_area.view.*


class AreaFragment : Fragment() {
    lateinit var rootView: View
    lateinit var Idcopy: Button
    lateinit var Program: Button
    lateinit var act: NavigationActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_area, container, false)
        act=activity as NavigationActivity
        NavigationActivity.PAD_OR_USB="USB"
        act.back.visibility=View.VISIBLE
        rootView.scan.setOnClickListener {
            RequestPermission()
        }
        rootView.serch.setOnClickListener {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, MakeFragment(), "Vehicle Selection")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Vehicle Selection")
                // 提交事務
                .commit()
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
        (activity as NavigationActivity).setActionBarTitle("Program (USB TPMS)")
        (activity as NavigationActivity).RightTop.visibility=View.GONE
        (activity as NavigationActivity).RightTop.setOnClickListener { }
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
        }else{   val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Scanner")
                // 提交事務
                .commit()}
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
                            val transaction = fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.nav_host_fragment, QrcodeScanner(), "Scanner")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                .addToBackStack("Scanner")
                                // 提交事務
                                .commit()
                        }
                    }
                }
        }
    }
}
