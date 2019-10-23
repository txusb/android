package com.orango.electronic.orangetxusb.UsbCable


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.HomeFragment
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.mainActivity.QrcodeScanner
import com.orango.electronic.orangetxusb.mainActivity.Relarm
import com.orango.electronic.orangetxusb.tool.CustomTextWatcher
import com.orango.electronic.orangetxusb.tool.FileDowload
import kotlinx.android.synthetic.main.fragment_id_copy.*
import kotlinx.android.synthetic.main.fragment_id_copy.view.*
import kotlinx.android.synthetic.main.fragment_mmy.view.*
import java.lang.Exception
import kotlinx.android.synthetic.main.fragment_id_copy.view.Menu as Menu1
import kotlinx.android.synthetic.main.fragment_mmy.view.Program_bt as Program_bt1


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Id_copy : Fragment() {
    lateinit var navActivity: NavigationActivity
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var model: String
    lateinit var rootView: View
    lateinit var year: String
    var Lf="0"
    var  Scanid=""
     var need=-1
    var isProgramming=false
    var mmyNum=""
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
            need=  navActivity.itemDAO.GetCopyId( mmyNum)
            downs19()
        }else{navActivity.finish()}
    }
    fun downs19(){
        navActivity.LoadingUI(resources.getString(R.string.Data_Loading),0)
        Thread{
            var  a=FileDowload.DownS19(mmyNum, navActivity)
            handler.post {
                if(a){navActivity.LoadingSuccessUI()}else{
                    downs19()
                }
            }
        }.start()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.fragment_id_copy, container, false)
        navActivity.setActionBarTitle(navActivity.resources.getString(R.string.Program_USB_TPMS))
        rootView.textView81.text=resources.getString(R.string.Scan_Code)+"\n(For Orange Sensor)"
        rootView.mmy_text3.text = "$make/$model /$year"
        rootView.editText.addTextChangedListener(CustomTextWatcher(rootView.editText))
        navActivity.back.visibility=View.VISIBLE
        rootView.editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(need))
        rootView.button3.setOnClickListener {
            if(editText.text.toString().length==need){ProgramFirst()}else{        Toast.makeText(navActivity,resources.getString(R.string.ID_code_should_be_8_characters).replace("8",""+need),Toast.LENGTH_SHORT).show()}
        }
        rootView.repg.setOnClickListener{
                        if(editText.text.toString().length==need){ProgramFirst()}else{       Toast.makeText(navActivity,resources.getString(R.string.ID_code_should_be_8_characters).replace("8",""+need),Toast.LENGTH_SHORT).show()}
        }
        rootView.Menu.setOnClickListener {
            val fragment = HomeFragment()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "Home")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Home")
                // 提交事務
                .commit()
        }
        rootView.Program_bt.setOnClickListener {
            NavigationActivity.Action ="PROGRAM"
            val fragment = CableSelect()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "Select Area")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Select Area")
                // 提交事務
                .commit()
        }
        UpdateUi(0)
        rootView.scaner.setOnClickListener {
            rootView.Select_Key.visibility=View.GONE
            val frag=QrcodeScanner()
            frag.Scan_For=frag.CableId
            frag.IdcopyCable=this
            frag.need=need
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, frag, "Scanner")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Scanner")
                // 提交事務
                .commit()
        }
        rootView.keyin.setOnClickListener {
            rootView.Select_Key.visibility=View.GONE
        }
rootView.relearm.setOnClickListener {
    Relarm.position=0
    val fragment = Relarm()
    val args = Bundle()
    args.putString(Cable_Program.stringMake, make)
    args.putString(Cable_Program.stringMakeImg, makeImg)
    args.putString(Cable_Program.stringModel, model)
    args.putString(Cable_Program.stringYear, year)
    fragment.arguments=args
    val transaction = fragmentManager!!.beginTransaction()
    transaction.replace(R.id.nav_host_fragment, fragment, "Relarm")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
        .addToBackStack("Relarm")
        // 提交事務
        .commit()
}
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if(Scanid.isNotEmpty()){
            rootView.editText.setText(Scanid)
            rootView.Select_Key.visibility=View.GONE
        }
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
var handler:Handler= Handler()
    fun Program(){
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        if(editText.text.toString().length==need){
            UpdateUi(1)
            Thread{
                val success=navActivity.command.writesensorID(insert())
                handler.post {
                    try {
                        if(success){
                            rootView.showid.text=navActivity.command.writeid.substring(8-need)
                            if((mmyNum.equals("RN1628")||mmyNum.equals("SI2048"))){
                                val WriteLftmp=navActivity.command.writeid.toString().substring(0,2)+"XX"+navActivity.command.writeid.toString().substring(4,6)+"YY"
                                val WriteLf=WriteLftmp.replace("XX",navActivity.command.writeid.toString().substring(6,8)).replace("YY",navActivity.command.writeid.toString().substring(2,4))
                                rootView.showid.text=WriteLf.substring(8-need)
                            }else{
                                rootView.showid.text=navActivity.command.writeid.substring(8-need)
                        }
                            rootView.showid.setTextColor(resources.getColor(R.color.colorRecieveText))
                            UpdateUi(2)
                        }else{
                            rootView.showid.text=editText.text.toString()
                            rootView.showid.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                            UpdateUi(3)
                        }
                    }catch (e:Exception){}

                }
            }.start()


        } else{
            Toast.makeText(navActivity,resources.getString(R.string.ID_code_should_be_8_characters).replace("8",""+need),Toast.LENGTH_SHORT).show()
        }
    }
fun insert():String{
    val result = StringBuffer(editText.text.toString())
    while (result.length < 8) {
        result.insert(0, "0")
    }
    if((mmyNum.equals("RN1628")||mmyNum.equals("SI2048"))){
        val WriteLftmp=result.toString().substring(0,2)+"XX"+result.toString().substring(4,6)+"YY"
        val WriteLf=WriteLftmp.replace("XX",result.toString().substring(6,8)).replace("YY",result.toString().substring(2,4))
        return WriteLf
    }
    return result.toString().toUpperCase()
}
    fun ProgramFirst(){
        if(!isProgramming){
            UpdateUi(1)
            isProgramming=true
            Thread{
                var a=navActivity.command.ProgramStep("${navActivity.applicationContext.filesDir.path}/$mmyNum.s19",Lf)
                handler.post {
                    isProgramming=false
                    try {
                        rootView.repg.visibility=View.VISIBLE
                        rootView.Menu.visibility=View.GONE
                        navActivity.GoMenu=true
                        navActivity.back.setImageResource(R.mipmap.menu)
                        if(a){
                            Program()
                        }else{
                            rootView.showid.text=editText.text.toString()
                            rootView.showid.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                            UpdateUi(3) }
                    }catch (e:Exception){}
                }
            }.start()
        }
       }

    fun UpdateUi(a:Int){
        val PROGRAM_WAIT: RelativeLayout =rootView.findViewById(R.id.PROGRAM_WAIT)
        val PROGRAM_START: RelativeLayout =rootView.findViewById(R.id.PROGRAM_START)
        val PROGRAM_SUCCESS: RelativeLayout =rootView.findViewById(R.id.PROGRAM_SUCCESS)
        val Statutext: TextView =rootView.findViewById(R.id.Statutext)
        val Statutext2: TextView =rootView.findViewById(R.id.Statutext2)
        val Statuim: ImageView =rootView.findViewById(R.id.Statuim)
        PROGRAM_WAIT.visibility=View.GONE
        PROGRAM_START.visibility=View.GONE
        PROGRAM_SUCCESS.visibility=View.GONE
        rootView.relearm.visibility=View.GONE
        rootView.repg.visibility=View.GONE
        rootView.Menu.visibility=View.GONE
        when(a){
            0->{
                PROGRAM_WAIT.visibility=View.VISIBLE
                rootView.Menu.visibility=View.VISIBLE
            }
            1->{
                PROGRAM_START.visibility=View.VISIBLE
            }
            2->{
                PROGRAM_SUCCESS.visibility=View.VISIBLE
                Statuim.setImageDrawable(resources.getDrawable(R.mipmap.v2_pt84ps,null))
                Statutext.setTextColor(resources.getColor(R.color.colorRecieveText))
                Statutext2.setTextColor(resources.getColor(R.color.colorRecieveText))
                Statutext.text=resources.getString(R.string.Programming_completed)
                Statutext2.text=resources.getString(R.string.Please_remove_the_sensor)
                rootView.relearm.visibility=View.VISIBLE
                rootView.repg.visibility=View.VISIBLE
            }
            3->{
                PROGRAM_SUCCESS.visibility=View.VISIBLE
                Statuim.setImageDrawable(resources.getDrawable(R.mipmap.fail,null))
                Statutext.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                Statutext2.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                Statutext.text=resources.getString(R.string.Programming_failed)
                Statutext2.text=resources.getString(R.string.Please_press_RE_PROGRAM)
                rootView.relearm.visibility=View.GONE
                rootView.repg.visibility=View.VISIBLE
            }
        }
    }
}
