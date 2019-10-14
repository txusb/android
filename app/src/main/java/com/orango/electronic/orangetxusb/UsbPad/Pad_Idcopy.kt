package com.orango.electronic.orangetxusb.UsbPad


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.mainActivity.QrcodeScanner
import com.orango.electronic.orangetxusb.UsbCable.Cable_Program
import com.orango.electronic.orangetxusb.tool.CustomTextWatcherForpad
import com.orango.electronic.orangetxusb.tool.FileDowload
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.*
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.Lft
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.Rft
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.Rrt
import kotlinx.android.synthetic.main.fragment_start_program.view.*
import java.lang.Exception
import kotlinx.android.synthetic.main.fragment_id_copy.view.Program_bt as Program_bt1
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.program as program1
import kotlinx.android.synthetic.main.fragment_start_program.view.Lrt as Lrt1
import kotlinx.android.synthetic.main.fragment_start_program.view.Program_bt as Program_bt1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Pad_Idcopy : Fragment() {
    var first=true
    lateinit var navActivity: NavigationActivity
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var mmyNum:String
    lateinit var model: String
    lateinit var year: String
    lateinit var rootView:View
    var ScanLf=""
    var ScanRf=""
    var ScanRr=""
    var ScanLr=""
    var scanner=QrcodeScanner()
     var run=false
     var ShowSelect=true
    var need=0
var SCAN_OR_KEY=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        navActivity = activity as NavigationActivity
        make = arguments!!.getString(Cable_Program.stringMake)!!
        makeImg = arguments!!.getString(Cable_Program.stringMakeImg)!!
        model = arguments!!.getString(Cable_Program.stringModel)!!
        year = arguments!!.getString(Cable_Program.stringYear)!!
        mmyNum = navActivity.itemDAO.getMMY(make,model,year)
        if(!mmyNum.equals("")){
            downs19()
            need=navActivity.itemDAO.GetCopyId( mmyNum)
        }else{navActivity.finish()}
    }
    val handler=Handler()
        fun downs19(){
            navActivity.LoadingUI(resources.getString(R.string.Data_Loading),0)
            Thread{
                var  a= FileDowload.DownS19(mmyNum, navActivity)
                handler.post {
                    if(a){navActivity.LoadingSuccessUI()}else{
                        downs19()
                    }
                }
            }.start()
        }

    override fun onResume() {
        super.onResume()
        first=true
    }
    override fun onDestroy() {
        super.onDestroy()
        first=false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.fragment_pad__idcopy, container, false)
        if(ShowSelect){rootView.Select_Key.visibility=View.VISIBLE}else{rootView.Select_Key.visibility=View.GONE}
        if(SCAN_OR_KEY==0){
            scanner.Scan_For=scanner.ID
            rootView.Lft.isFocusable=false
            rootView.Rrt.isFocusable=false
            rootView.Rft.isFocusable=false
            rootView.Lrt.isFocusable=false
            rootView.Lft.setOnClickListener {
                scanner.place=scanner.LF
                scanner.edit=rootView.Lft
                RequestPermission() }
            rootView.Rrt.setOnClickListener {
                scanner.place=scanner.Rr
                scanner.edit=rootView.Rrt
                RequestPermission()
            }
            rootView.Rft.setOnClickListener {
                scanner.place=scanner.Rf
                scanner.edit=rootView.Rft
                RequestPermission()
            }
            rootView.Lrt.setOnClickListener {
                scanner.place=scanner.Lr
                scanner.edit=rootView.Lrt
                RequestPermission() } }
        first=true
        rootView.mmy_text6.text="$make/$model /$year"
        rootView.Program_bt.setOnClickListener {
            NavigationActivity.Action="PROGRAM"
            navActivity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, PadSelect(), "PadSelect")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("PadSelect")
                // 提交事務
                .commit()
        }
        rootView.program.setOnClickListener {
            var lf=rootView.Lft.text.toString()
            var Rf=rootView.Rft.text.toString()
            var Rr=rootView.Rrt.text.toString()
            var lr=rootView.Lrt.text.toString()
            if(!rootView.Lft.isEnabled){lf="00000000"}
            if(!rootView.Rft.isEnabled){Rf="00000000"}
            if(!rootView.Rrt.isEnabled){Rr="00000000"}
            if(!rootView.Lrt.isEnabled){lr="00000000"}
            if(lf.length<need||Rf.length<need||Rr.length<need||lr.length<need){
                Toast.makeText(navActivity,resources.getString(R.string.ID_code_should_be_8_characters).replace("8",""+need),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            first=false
            val args = Bundle()
            args.putString(Cable_Program.stringMake, make)
            args.putString(Cable_Program.stringMakeImg, makeImg)
            args.putString(Cable_Program.stringModel, model)
            args.putString(Cable_Program.stringYear, year)
            args.putString(Cable_Program.LFID, insert(lf))
            args.putString(Cable_Program.LRID,insert(lr))
            args.putString(Cable_Program.RRID,  insert(Rr))
            args.putString(Cable_Program.RFID,  insert(Rf))
            val fragment = StartProgram()
            fragment.arguments = args
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "Pad_Program")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Pad_Program")
                // 提交事務
                .commit()
            ShowSelect=false
        }
        scanner.Idcopy=this
        scanner.need=need
        rootView.Lft.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(need))
        rootView.Lft.addTextChangedListener(CustomTextWatcherForpad(rootView.Lft,need))
        rootView.Rrt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(need))
        rootView.Rrt.addTextChangedListener(CustomTextWatcherForpad(rootView.Rrt,need))
        rootView.Rft.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(need))
        rootView.Rft.addTextChangedListener(CustomTextWatcherForpad(rootView.Rft,need))
        rootView.Lrt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(need))
        rootView.Lrt.addTextChangedListener(CustomTextWatcherForpad(rootView.Lrt,need))
        CheckUnlinked()
        rootView.scaner.setOnClickListener{
            rootView.Select_Key.visibility=View.GONE
            scanner.Scan_For=scanner.ID
            rootView.Lft.isFocusable=false
            rootView.Rrt.isFocusable=false
            rootView.Rft.isFocusable=false
            rootView.Lrt.isFocusable=false
            rootView.Lft.setOnClickListener {
                scanner.place=scanner.LF
                scanner.edit=rootView.Lft
                RequestPermission() }
            rootView.Rrt.setOnClickListener {
                scanner.place=scanner.Rr
                scanner.edit=rootView.Rrt
                RequestPermission()
            }
            rootView.Rft.setOnClickListener {
                scanner.place=scanner.Rf
                scanner.edit=rootView.Rft
                RequestPermission()
            }
            rootView.Lrt.setOnClickListener {
                scanner.place=scanner.Lr
                scanner.edit=rootView.Lrt
                RequestPermission() }
            SCAN_OR_KEY=0
        }
        rootView.keyin.setOnClickListener {rootView.Select_Key.visibility=View.GONE}
        rootView.Lft.setText(ScanLf)
        rootView.Rrt.setText(ScanRr)
        rootView.Rft.setText(ScanRf)
        rootView.Lrt.setText(ScanLr)
        return rootView
    }

    fun getMem(str: String, m: String): Int {
        var str = str
        var i = 0
        while (str.indexOf(m) != -1) {
            val a = str.indexOf(m)
            str = str.substring(a + 1)
            i++
        }
        return i
    }
    fun insert(txt:String):String{
        val result = StringBuffer(txt)
        while (result.length < 8) {
            result.insert(0, "0")
        }
        return result.toString()
    }

    fun CheckUnlinked(){
        if(run){return}
        run=true
        Thread{
            try{
                    for (i in 0..1) {
                        val Ch1 = navActivity.command.Command_11(i, 1)
                        val Ch2 = navActivity.command.Command_11(i, 2)
                        handler.post {  if (Ch1) {
                            if(i==0){
                                rootView.Lft.isEnabled=true
                                rootView.Lft.hint = "Original sensor ID"
                                if(SCAN_OR_KEY==0){rootView.Lft.setText(ScanLf)}
                            }else{
                                rootView.Rft.isEnabled=true
                                rootView.Rft.hint = "Original sensor ID"
                                if(SCAN_OR_KEY==0){rootView.Rft.setText(ScanRf)}
                            }
                        } else {
                            if(i==0){
                                rootView.Lft.isEnabled=false
                                rootView.Lft.setText("")
                                rootView.Lft.hint = navActivity.resources.getString(R.string.Unlinked)
                            }else{
                                rootView.Rft.isEnabled=false
                                rootView.Rft.setText("")
                                rootView.Rft.hint = navActivity.resources.getString(R.string.Unlinked)
                            }
                        }
                            if (Ch2) {
                                if(i==0){
                                    rootView.Lrt.isEnabled=true
                                    rootView.Lrt.hint = "Original sensor ID"
                                    if(SCAN_OR_KEY==0){rootView.Lrt.setText(ScanLr)}
                                }else{
                                    rootView.Rrt.hint = "Original sensor ID"
                                    rootView.Rrt.isEnabled=true
                                    if(SCAN_OR_KEY==0){rootView.Rrt.setText(ScanRr)}
                                }
                            } else {
                                if(i==0){
                                    rootView.Lrt.isEnabled=false
                                    rootView.Lrt.setText("")
                                    rootView.Lrt.hint = navActivity.resources.getString(R.string.Unlinked)
                                }else{
                                    rootView.Rrt.isEnabled=false
                                    rootView.Rrt.setText("")
                                    rootView.Rrt.hint = navActivity.resources.getString(R.string.Unlinked)
                                }
                            }
                        }

                }
                Thread.sleep(2000)
                run=false
                if(first){CheckUnlinked()}
            }catch (e:Exception){e.printStackTrace()}
        }.start()
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
            first=false
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, scanner, "Scanner")
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
                            first=false
                            val transaction = fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.nav_host_fragment, scanner, "Scanner")
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
