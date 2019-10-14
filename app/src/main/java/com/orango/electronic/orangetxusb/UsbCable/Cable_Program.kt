package com.orango.electronic.orangetxusb.UsbCable

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.orango.electronic.orangetxusb.HttpCommand.Fuction.Upload_ProgramRecord
import com.orango.electronic.orangetxusb.HttpCommand.SensorRecord
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.HomeFragment
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.mainActivity.Relarm
import com.orango.electronic.orangetxusb.tool.FileDowload.DownS19
import kotlinx.android.synthetic.main.fragment_mmy.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Cable_Program : Fragment() {

    companion object {
        const val stringMake = "make"
        const val stringMakeImg = "make.img"
        const val stringModel = "model"
        const val stringYear = "year"
        const val LFID = "id1"
        const val  LRID = "id2"
        const val  RFID = "id3"
        const val  RRID = "id4"
    }
    var Lf="0"
    lateinit var navActivity: NavigationActivity
    lateinit var rootView: View
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var model: String
    lateinit var year: String
    lateinit var mmyNum: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        navActivity = activity as NavigationActivity
        make = arguments!!.getString(stringMake)!!
        makeImg = arguments!!.getString(stringMakeImg)!!
        model = arguments!!.getString(stringModel)!!
        year = arguments!!.getString(stringYear)!!
        mmyNum = navActivity.itemDAO.getMMY(make,model,year)
        Lf=navActivity.itemDAO.getLf(mmyNum)!!
        downs19()
    }
fun downs19(){
    navActivity.LoadingUI(resources.getString(R.string.Data_Loading),0)
    isProgramming=true
    Thread{
        var  a=DownS19(mmyNum,navActivity)
        handler.post {
            if(a){navActivity.LoadingSuccessUI()
                isProgramming=false
            }else{
                downs19()
            }
        }
    }.start()
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_mmy, container, false)
        navActivity.setActionBarTitle(navActivity.resources.getString(R.string.Program_USB_TPMS))
        navActivity.back.visibility=View.VISIBLE
        rootView.mmy_text.text = "$make/$model /$year"
        rootView.ProgramAgain.setOnClickListener {      Program()
        }
        rootView.program_sensor_btn.setOnClickListener {
            Program()
        }
        rootView.copy_id_btn.setOnClickListener {
            NavigationActivity.Action ="IDCOPY"
            navActivity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val fragment = CableSelect()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "Select Area")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Select Area")
                // 提交事務
                .commit()
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
        rootView.menu.setOnClickListener {     val fragment =
            HomeFragment()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "Home")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Home")
                // 提交事務
                .commit()}
        rootView.Relarm.setOnClickListener {
            val fragment = Relarm()
            val args = Bundle()
            args.putString(stringMake, make)
            args.putString(stringMakeImg, makeImg)
            args.putString(stringModel, model)
            args.putString(stringYear, year)
            fragment.arguments=args
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "Relarm")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Relarm")
                // 提交事務
                .commit()
        }
        UpdateUi(0)
        return rootView
    }
val handler: Handler =Handler()
    var isProgramming=false

    fun Program(){
        if(!isProgramming){
            isProgramming=true
            navActivity.back.isClickable=false
            UpdateUi(1)
            Log.d("mmy",mmyNum)
            Thread{
                val startime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                var a=navActivity.command.ProgramStep("${navActivity.applicationContext.filesDir.path}/$mmyNum.s19",Lf)
                val endtime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                val idrecord:ArrayList<SensorRecord> = ArrayList()
                var b = SensorRecord()
                b.SensorID = navActivity.command.writeid

                if(a){ b.IsSuccess = "true";}else{b.IsSuccess = "false";}
                idrecord.add(b)
                handler.post {
                    try {
                        isProgramming=false
                        navActivity.back.isClickable=true
                        if(a){
                            UpdateUi(2)
                        }else{ UpdateUi(3)  }
                    }catch (e:Exception){}
                }
                Upload_ProgramRecord(make,model,year,startime,endtime,"119500863501", "USBCable", "Program", 1, "ALL", idrecord)
            }.start()
        }

    }

    override fun onResume() {
        super.onResume()
        navActivity.setActionBarTitle("Program Sensor")
    }
    fun UpdateUi(a:Int){
        val PROGRAM_WAIT:RelativeLayout=rootView.findViewById(R.id.PROGRAM_WAIT)
        val PROGRAM_START:RelativeLayout=rootView.findViewById(R.id.PROGRAM_START)
        val PROGRAM_SUCCESS:RelativeLayout=rootView.findViewById(R.id.PROGRAM_SUCCESS)
        val TwiceBt:LinearLayout=rootView.findViewById(R.id.Twice)
        val Menu:Button=rootView.findViewById(R.id.Menu)
        val Statutext:TextView=rootView.findViewById(R.id.Statutext)
        val Statutext2:TextView=rootView.findViewById(R.id.Statutext2)
        val Statuim:ImageView=rootView.findViewById(R.id.Statuim)
        TwiceBt.visibility=View.GONE
        Menu.visibility=View.GONE
        PROGRAM_WAIT.visibility=View.GONE
        PROGRAM_START.visibility=View.GONE
        PROGRAM_SUCCESS.visibility=View.GONE
        when(a){
            0->{
                Menu.visibility=View.VISIBLE
                PROGRAM_WAIT.visibility=View.VISIBLE
            }
            1->{
                Menu.visibility=View.VISIBLE
                PROGRAM_START.visibility=View.VISIBLE}
            2->{
                PROGRAM_SUCCESS.visibility=View.VISIBLE
                TwiceBt.visibility=View.VISIBLE
                Statuim.setImageDrawable(resources.getDrawable(R.mipmap.v2_pt84ps,null))
                Statutext.setTextColor(resources.getColor(R.color.colorRecieveText))
                Statutext2.setTextColor(resources.getColor(R.color.colorRecieveText))
                Statutext.text=resources.getString(R.string.Programming_completed)
                Statutext2.text=resources.getString(R.string.Please_remove_the_sensor)
            }
            3->{
                PROGRAM_SUCCESS.visibility=View.VISIBLE
                TwiceBt.visibility=View.VISIBLE
                Statuim.setImageDrawable(resources.getDrawable(R.mipmap.fail,null))
                Statutext.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                Statutext2.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                Statutext.text=resources.getString(R.string.Programming_failed)
                Statutext2.text=resources.getString(R.string.Please_press_RE_PROGRAM)
            }
        }
    }
}
