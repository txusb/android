package com.orango.electronic.orangetxusb.mainActivity


import android.Manifest
import android.app.Instrumentation
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.orango.electronic.orangetxusb.LotActivity.Pad_Idcopy

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.tool.Format
import kotlinx.android.synthetic.main.fragment_pad__idcopy.*
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.*
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.Lf
import kotlinx.android.synthetic.main.fragment_qrcode_scanner.*
import kotlinx.android.synthetic.main.fragment_qrcode_scanner.view.*
import kotlinx.android.synthetic.main.selectble.view.*
import me.dm7.barcodescanner.zxing.ZXingScannerView



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class QrcodeScanner : Fragment(), ZXingScannerView.ResultHandler{
    var S19=0
    var ID=1
    var need=8
    var Scan_For=S19
    val LF=1
    val Rf=2
    val Rr=3
    val Lr=4
    var place=0
    lateinit var Idcopy:Pad_Idcopy
    lateinit var edit:EditText
    override fun handleResult(rawResult: Result?) {
//        rootView.textView12.text="Contents = " + rawResult!!.getText()
        if(Scan_For==ID){
            val contents=rawResult!!.text.split("*",":")
            if(contents.size==3&&contents[1].length== need){
            edit.setText(contents[1])

                Log.d("place",""+place)
                when(place){
                    LF->{Idcopy.ScanLf=contents[1]}
                    Rf->{Idcopy.ScanRf=contents[1]}
                    Rr->{Idcopy.ScanRr=contents[1]}
                    Lr->{Idcopy.ScanLr=contents[1]}
                }
                Idcopy.ShowSelect=false
                Thread{
                    try{
                        var inst = Instrumentation()
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
                    }
                    catch ( e:java.lang.Exception) {
                        Log.e("Exception when onBack", e.toString())
                    }
                }.start()
            }else{
                Log.d("QRCODE",rawResult.text)
                val handler = Handler()
                Toast.makeText(act,act.resources.getString(R.string.Please_scan_the_correct_QR_code),Toast.LENGTH_SHORT).show()
                handler.postDelayed({ mScannerView!!.resumeCameraPreview(this) }, 2000)
            }
        }else{
            val contents=rawResult!!.text.split("*")
            if(contents.size==3){
                act.itemDAO.GoOk(contents[0],fragmentManager!!)
            }

        }

    }

    private var mScannerView: ZXingScannerView? = null
    var ALL_FORMATS: ArrayList<BarcodeFormat> = ArrayList(1)
    lateinit var rootView: View
    lateinit var frame: ViewGroup
    lateinit var act: NavigationActivity
    internal var formats: List<BarcodeFormat>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView= inflater.inflate(R.layout.fragment_qrcode_scanner, container, false)
        RequestPermission()
        act=activity!! as NavigationActivity
        mScannerView = ZXingScannerView(activity)
        ALL_FORMATS.add(BarcodeFormat.DATA_MATRIX)
        mScannerView!!.setFormats(ALL_FORMATS)
        mScannerView!!.resumeCameraPreview(this)
        mScannerView!!.setAutoFocus(true)
        mScannerView!!.setAspectTolerance(0.0f)
        frame=rootView.findViewById(R.id.frame)
        frame.addView(mScannerView)
        return rootView
    }
    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
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
        }
    }
}
