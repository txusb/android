package com.orango.electronic.orangetxusb.UsbPad


import android.app.Instrumentation
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orango.electronic.orangetxusb.HttpCommand.Fuction.Upload_IDCopyRecord
import com.orango.electronic.orangetxusb.HttpCommand.Fuction.Upload_ProgramRecord
import com.orango.electronic.orangetxusb.HttpCommand.SensorRecord

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.mainActivity.Relarm
import com.orango.electronic.orangetxusb.UsbCable.Cable_Program
import com.orango.electronic.orangetxusb.mainActivity.HomeFragment
import com.orango.electronic.orangetxusb.tool.FileDowload
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.*
import kotlinx.android.synthetic.main.fragment_start_program.view.*
import kotlinx.android.synthetic.main.fragment_start_program.view.Lf
import kotlinx.android.synthetic.main.fragment_start_program.view.Lfi
import kotlinx.android.synthetic.main.fragment_start_program.view.Lft
import kotlinx.android.synthetic.main.fragment_start_program.view.Lr
import kotlinx.android.synthetic.main.fragment_start_program.view.Lri
import kotlinx.android.synthetic.main.fragment_start_program.view.Lrt
import kotlinx.android.synthetic.main.fragment_start_program.view.Relarm
import kotlinx.android.synthetic.main.fragment_start_program.view.Rf
import kotlinx.android.synthetic.main.fragment_start_program.view.Rfi
import kotlinx.android.synthetic.main.fragment_start_program.view.Rft
import kotlinx.android.synthetic.main.fragment_start_program.view.Rr
import kotlinx.android.synthetic.main.fragment_start_program.view.Rri
import kotlinx.android.synthetic.main.fragment_start_program.view.Rrt
import kotlinx.android.synthetic.main.fragment_start_program.view.menu
import kotlinx.android.synthetic.main.fragment_start_program.view.program
import java.lang.Exception
import java.util.*
import com.orango.electronic.orangetxusb.R.id.animation_view2 as animation_view21
import kotlinx.android.synthetic.main.fragment_mmy.view.Relarm as Relarm1
import kotlinx.android.synthetic.main.fragment_mmy.view.menu as menu1
import kotlinx.android.synthetic.main.fragment_mmy.view.mmy_text as mmy_text1
import kotlinx.android.synthetic.main.fragment_pad__idcopy.view.copy_id_btn as copy_id_btn1
import kotlinx.android.synthetic.main.fragment_start_program.view.Program_bt as Program_bt1
import java.text.SimpleDateFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class StartProgram : Fragment() {
var first=true
    lateinit var rootView: View
    val UNLINK=0
    val PROGRAN_SUCCESS=1
    val PROGAMMING=4
    val PROGRAN_FAULSE=2
    val PROGRAN_WAIT=3
    var LFID=""
    var RFID=""
    var LRID=""
    var RRID=""
    var WriteLf=""
    var WriteRf=""
    var WriteLr=""
    var WriteRR=""
    var ISPROGRAMMING=false
    var Idcount=0
    val LF=0
    val RF=1
    val LR=2
    val timer=Timer()
    val RR=3
    var Lf="0"
    lateinit var navActivity: NavigationActivity
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var mmyNum:String
    lateinit var model: String
    lateinit var year: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        navActivity = activity as NavigationActivity
        make = arguments!!.getString(Cable_Program.stringMake)!!
        makeImg = arguments!!.getString(Cable_Program.stringMakeImg)!!
        model = arguments!!.getString(Cable_Program.stringModel)!!
        year = arguments!!.getString(Cable_Program.stringYear)!!
        WriteLf= arguments!!.getString(Cable_Program.LFID)!!
        WriteRf= arguments!!.getString(Cable_Program.RFID)!!
        WriteLr= arguments!!.getString(Cable_Program.LRID)!!
        WriteRR= arguments!!.getString(Cable_Program.RRID)!!
        mmyNum = navActivity.itemDAO.getMMY(make,model,year)
        Idcount=8-navActivity.itemDAO.GetCopyId( mmyNum)
        Lf=navActivity.itemDAO.getLf(mmyNum)!!
        if((mmyNum.equals("RN1628")||mmyNum.equals("SI2048"))&&WriteLf.length==8){
            val WriteLftmp=WriteLf.substring(0,2)+"XX"+WriteLf.substring(4,6)+"YY"
            val WriteRftmp=WriteRf.substring(0,2)+"XX"+WriteRf.substring(4,6)+"YY"
            val WriteLrtmp=WriteLr.substring(0,2)+"XX"+WriteLr.substring(4,6)+"YY"
            val WriteRRtmp=WriteRR.substring(0,2)+"XX"+WriteRR.substring(4,6)+"YY"
            WriteLf=WriteLftmp.replace("XX",WriteLf.substring(6,8)).replace("YY",WriteLf.substring(2,4))
            WriteRf=WriteRftmp.replace("XX",WriteRf.substring(6,8)).replace("YY",WriteRf.substring(2,4))
            WriteLr=WriteLrtmp.replace("XX",WriteLr.substring(6,8)).replace("YY",WriteLr.substring(2,4))
            WriteRR=WriteRRtmp.replace("XX",WriteRR.substring(6,8)).replace("YY",WriteRR.substring(2,4))
        }
        if(WriteLf.length!=8){downs19()}
    }
    fun downs19(){
        navActivity.LoadingUI(resources.getString(R.string.Data_Loading),0)
        ISPROGRAMMING=true
        Thread{
            var  a= FileDowload.DownS19(mmyNum, navActivity)
            handler.post {
                if(a){navActivity.LoadingSuccessUI()
                    ISPROGRAMMING=false
                }else{
                    downs19()
                }
            }
        }.start()
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.fragment_start_program, container, false)
        rootView.mmy_text.text = "$make/$model /$year"
        first=true
        when(NavigationActivity.Action){
            "IDCOPY"->{ navActivity.setActionBarTitle(activity!!.resources.getString(R.string.ID_COPY))}
            "PROGRAM"->{navActivity.setActionBarTitle(navActivity.resources.getString(R.string.Program))}
        }
        UpdateUi(LF,UNLINK)
        UpdateUi(RF,UNLINK)
        UpdateUi(LR,UNLINK)
        UpdateUi(RR,UNLINK)
        if(WriteLf.length==8){
            rootView.copy_id_btn.setBackgroundResource(R.drawable.solid)
            rootView.copy_id_btn.setTextColor(navActivity.resources.getColor(R.color.white))
            rootView.Program_bt.setBackgroundResource(R.drawable.stroke)
            rootView.Program_bt.setTextColor(navActivity.resources.getColor(R.color.buttoncolor))
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
        }else{
            rootView.copy_id_btn.setOnClickListener {
                NavigationActivity.Action="IDCOPY"
                navActivity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val transaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, PadSelect(), "PadSelect")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                    .addToBackStack("PadSelect")
                    // 提交事務
                    .commit()
            }
        }

        rootView.animation_view2.speed = 1.0f
        UpdateUiCondition(PROGRAN_WAIT)
        rootView.program.setOnClickListener {
            program()
        }
        rootView.menu.setOnClickListener {
            Thread{
                try{
                    var inst = Instrumentation()
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
                }
                catch ( e:java.lang.Exception) {
                    Log.e("Exception when onBack", e.toString())
                }
            }.start();}
        rootView.Relarm.setOnClickListener {
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
        UdCondition()

        return rootView
    }
var  run=false
    fun UdCondition(){
        handler.post {     navActivity.back.isClickable=false}

        run=true
            Thread(Runnable {
                try{
                        for (i in 0..1) {
                            val Ch1 = navActivity.command.Command_11(i, 1)
                            var Id1 = navActivity.command.ID
                            val Ch2 = navActivity.command.Command_11(i, 2)
                            var Id2 = navActivity.command.ID
                            handler.post(Runnable {
                                if (Ch1) {
                                    if(mmyNum.equals("RN1628")||mmyNum.equals("SI2048")){
                                        val Writetmp=Id1.substring(0,2)+"XX"+Id1.substring(4,6)+"YY"
                                        Id1=Writetmp.replace("XX",Id1.substring(6,8)).replace("YY",Id1.substring(2,4))
                                    }
                                    Id1=Id1.substring(Idcount)
                                    if(i==0){
                                        LFID=Id1
                                        if(first){UpdateUi(LF,PROGRAN_WAIT)}
                                        rootView.Lft.text=LFID
                                    }else{
                                        RFID=Id1
                                        if(first){UpdateUi(RF,PROGRAN_WAIT)}
                                        rootView.Rft.text=RFID
                                    }
                                } else {
                                    if(i==0){
                                        LFID=navActivity.resources.getString(R.string.Unlinked)
                                        if(first){UpdateUi(LF,UNLINK)}
                                        rootView.Lft.text=LFID
                                    }else{
                                        RFID=navActivity.resources.getString(R.string.Unlinked)
                                        if(first){UpdateUi(RF,UNLINK)}
                                        rootView.Rft.text=RFID
                                    }
                                }
                                if (Ch2) {
                                    if(mmyNum.equals("RN1628")||mmyNum.equals("SI2048")){
                                        val Writetmp=Id2.substring(0,2)+"XX"+Id2.substring(4,6)+"YY"
                                        Id2=Writetmp.replace("XX",Id2.substring(6,8)).replace("YY",Id2.substring(2,4))
                                    }
                                    Id2=Id2.substring(Idcount)
                                    if(i==0){
                                        LRID=Id2
                                        if(first){UpdateUi(LR,PROGRAN_WAIT)}
                                        rootView.Lrt.text=LRID
                                    }else{
                                        RRID=Id2
                                        if(first){UpdateUi(RR,PROGRAN_WAIT)}
                                        rootView.Rrt.text=RRID
                                    }
                                } else {
                                    if(i==0){
                                        LRID=navActivity.resources.getString(R.string.Unlinked)
                                        rootView.Lrt.text=navActivity.resources.getString(R.string.Unlinked)
                                        if(first){UpdateUi(LR,UNLINK)}
                                    }else{
                                        RRID=navActivity.resources.getString(R.string.Unlinked)
                                        if(first){UpdateUi(RR,UNLINK)}
                                        rootView.Rrt.text=navActivity.resources.getString(R.string.Unlinked)
                                    }
                                }
                            })
                        }

                    handler.post {navActivity.back.isClickable=true
                        run=false
                    }
                    Thread.sleep(2000)
                    if(first){
                        UdCondition()}
                }catch (e:Exception){e.printStackTrace()}
//            first=false
            }).start()


    }
    private var handler =Handler()
fun UpdateUi(position:Int,situation:Int){
    when(position){
        LF->{
         when(situation){
             UNLINK->{
                 rootView.Lft.text=navActivity.resources.getString(R.string.Unlinked)
                 rootView.Lfi.visibility=View.GONE
                 rootView.Lft.setBackgroundResource(R.mipmap.icon_input_box_locked)
                 rootView.Lf.setBackgroundResource(R.mipmap.icon_tire_normal)
             }
             PROGRAN_SUCCESS->{
                 rootView.Lft.text=LFID
                 rootView.Lfi.visibility=View.VISIBLE
                 rootView.Lfi.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.correct))
                 rootView.Lft.setBackgroundResource(R.mipmap.icon_input_box_ok)
                 rootView.Lf.setBackgroundResource(R.mipmap.icon_tire_ok)
             }
             PROGRAN_FAULSE->{
                 rootView.Lft.text=LFID
                 rootView.Lfi.visibility=View.VISIBLE
                 rootView.Lfi.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.error))
                 rootView.Lft.setBackgroundResource(R.mipmap.icon_input_box_fail)
                 rootView.Lf.setBackgroundResource(R.mipmap.icon_tire_fail)
             }
             PROGRAN_WAIT->{
                 rootView.Lft.text=LFID
                 rootView.Lfi.visibility=View.GONE
                 rootView.Lft.setBackgroundResource(R.mipmap.icon_input_box_write)
                 rootView.Lf.setBackgroundResource(R.mipmap.icon_tire_normal)
             }
         }
        }
        RF->{
            when(situation){
                UNLINK->{
                    rootView.Rft.text=navActivity.resources.getString(R.string.Unlinked)
                    rootView.Rfi.visibility=View.GONE
                    rootView.Rft.setBackgroundResource(R.mipmap.icon_input_box_locked)
                    rootView.Rf.setBackgroundResource(R.mipmap.icon_tire_normal)
                }
                PROGRAN_SUCCESS->{
                    rootView.Rft.text=RFID
                    rootView.Rfi.visibility=View.VISIBLE
                    rootView.Rfi.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.correct))
                    rootView.Rft.setBackgroundResource(R.mipmap.icon_input_box_ok)
                    rootView.Rf.setBackgroundResource(R.mipmap.icon_tire_ok)
                }
                PROGRAN_FAULSE->{
                    rootView.Rft.text=RFID
                    rootView.Rfi.visibility=View.VISIBLE
                    rootView.Rfi.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.error))
                    rootView.Rft.setBackgroundResource(R.mipmap.icon_input_box_fail)
                    rootView.Rf.setBackgroundResource(R.mipmap.icon_tire_fail)
                }
                PROGRAN_WAIT->{
                    rootView.Rft.text=RFID
                    rootView.Rfi.visibility=View.GONE
                    rootView.Rft.setBackgroundResource(R.mipmap.icon_input_box_write)
                    rootView.Rf.setBackgroundResource(R.mipmap.icon_tire_normal)
                }
            }
        }
        LR->{
            when(situation){
                UNLINK->{
                    rootView.Lrt.text=navActivity.resources.getString(R.string.Unlinked)
                    rootView.Lri.visibility=View.GONE
                    rootView.Lrt.setBackgroundResource(R.mipmap.icon_input_box_locked)
                    rootView.Lr.setBackgroundResource(R.mipmap.icon_tire_normal)
                }
                PROGRAN_SUCCESS->{
                    rootView.Lrt.text=LRID
                    rootView.Lri.visibility=View.VISIBLE
                    rootView.Lri.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.correct))
                    rootView.Lrt.setBackgroundResource(R.mipmap.icon_input_box_ok)
                    rootView.Lr.setBackgroundResource(R.mipmap.icon_tire_ok)
                }
                PROGRAN_FAULSE->{
                    rootView.Lrt.text=LRID
                    rootView.Lri.visibility=View.VISIBLE
                    rootView.Lri.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.error))
                    rootView.Lrt.setBackgroundResource(R.mipmap.icon_input_box_fail)
                    rootView.Lr.setBackgroundResource(R.mipmap.icon_tire_fail)
                }
                PROGRAN_WAIT->{
                    rootView.Lrt.text=LRID
                    rootView.Lri.visibility=View.GONE
                    rootView.Lrt.setBackgroundResource(R.mipmap.icon_input_box_write)
                    rootView.Lr.setBackgroundResource(R.mipmap.icon_tire_normal)
                }
            }
        }
        RR->{
            when(situation){
                UNLINK->{
                    rootView.Rrt.text=navActivity.resources.getString(R.string.Unlinked)
                    rootView.Rri.visibility=View.GONE
                    rootView.Rrt.setBackgroundResource(R.mipmap.icon_input_box_locked)
                    rootView.Rr.setBackgroundResource(R.mipmap.icon_tire_normal)
                }
                PROGRAN_SUCCESS->{
                    rootView.Rrt.text=RRID
                    rootView.Rri.visibility=View.VISIBLE
                    rootView.Rri.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.correct))
                    rootView.Rrt.setBackgroundResource(R.mipmap.icon_input_box_ok)
                    rootView.Rr.setBackgroundResource(R.mipmap.icon_tire_ok)
                }
                PROGRAN_FAULSE->{
                    rootView.Rrt.text=RRID
                    rootView.Rri.visibility=View.VISIBLE
                    rootView.Rri.setImageDrawable(navActivity.resources.getDrawable(R.mipmap.error))
                    rootView.Rrt.setBackgroundResource(R.mipmap.icon_input_box_fail)
                    rootView.Rr.setBackgroundResource(R.mipmap.icon_tire_fail)
                }
                PROGRAN_WAIT->{
                    rootView.Rrt.text=RRID
                    rootView.Rri.visibility=View.GONE
                    rootView.Rrt.setBackgroundResource(R.mipmap.icon_input_box_write)
                    rootView.Rr.setBackgroundResource(R.mipmap.icon_tire_normal)
                }
            }
        }
    }
}
fun UpdateUiCondition(position: Int){
    rootView.Relarm.visibility=View.GONE
    rootView.menu.visibility=View.GONE
    rootView.program.visibility=View.GONE
    rootView.programing.visibility=View.GONE
    rootView.animation_view2.visibility=View.GONE
    rootView.success.visibility=View.GONE
    when(position){
        PROGRAN_SUCCESS->{
            rootView.success.visibility=View.VISIBLE
            rootView.condition.text=navActivity.resources.getString(R.string.Programming_completed)
            rootView.condition.setTextColor(navActivity.resources.getColor(R.color.buttoncolor))
            rootView.Relarm.visibility=View.VISIBLE
            rootView.menu.visibility=View.VISIBLE
        }
        PROGRAN_WAIT->{
            rootView.program.visibility=View.VISIBLE
            rootView.program.text=navActivity.resources.getString(R.string.Program_sensor)
            rootView.condition.text=navActivity.resources.getString(R.string.Please_insert_the_sensor_into_the_USB_PAD)
            rootView.condition.setTextColor(navActivity.resources.getColor(R.color.buttoncolor))}
        PROGRAN_FAULSE->{
            rootView.program.visibility=View.VISIBLE
            rootView.program.text=navActivity.resources.getString(R.string.RE_PROGRAM)
            rootView.condition.text=navActivity.resources.getString(R.string.Programming_failed_where)
            rootView.condition.setTextColor(navActivity.resources.getColor(R.color.colorPrimary))
        }
        PROGAMMING->{
            rootView.programing.visibility=View.VISIBLE
            rootView.animation_view2.visibility=View.VISIBLE
            rootView.program.visibility=View.VISIBLE
            rootView.program.text=navActivity.resources.getString(R.string.Program_sensor)
            rootView.condition.text=navActivity.resources.getString(R.string.Programming_do_not_move_sensors)
            rootView.condition.setTextColor(navActivity.resources.getColor(R.color.buttoncolor))
        }
    }
}

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        first=false
    }
    fun Getid(a:String):String
    {
        var id = a.substring(3, a.length)
        if (mmyNum == "RN1628" || mmyNum == "SI2048")
        {
            var Writetmp = id.substring(0, 2) + "XX" + id.substring(4, 6) + "YY"
            id = Writetmp.replace("XX", id.substring(6, 8)).replace("YY", id.substring(2, 4))
        }

        id = id.substring(Idcount)
        Log.d("reid",id)
        return id
    }
    fun program(){
        if(run){return}
        if(!ISPROGRAMMING){
            navActivity.back.isClickable=false
                first=false
                ISPROGRAMMING=true
                UpdateUiCondition(PROGAMMING)
                Thread(Runnable {
                    var condition:Boolean
                    if(WriteLf.length==8&&WriteLr.length==8&&WriteRR.length==8&&WriteRf.length==8){
                        val startime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        condition=navActivity.command.ProgramAll("${navActivity.applicationContext.filesDir.path}/$mmyNum.s19",WriteLf,WriteLr,WriteRf,WriteRR,Lf)
                        val endtime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                       val idrecord:ArrayList<SensorRecord> = ArrayList()
                        if (!WriteLf.equals("00000000")){
                            var a=SensorRecord()
                            a.Car_SensorID = LFID
                            if(navActivity.command.FALSE_CHANNEL.contains("01")){a.IsSuccess = "false"; } else { a.IsSuccess = "true"; }
                            a.SensorID = WriteLf
                            idrecord.add(a)
                        }
                        if (!WriteLr.equals("00000000")){
                            var a=SensorRecord()
                            a.Car_SensorID = LRID
                            if(navActivity.command.FALSE_CHANNEL.contains("02")){a.IsSuccess = "false"; } else { a.IsSuccess = "true"; }
                            a.SensorID = WriteLr
                            idrecord.add(a)
                        }
                        if (!WriteRf.equals("00000000")){
                            var a=SensorRecord()
                            a.Car_SensorID = RFID
                            if(navActivity.command.FALSE_CHANNEL.contains("03")){a.IsSuccess = "false"; } else { a.IsSuccess = "true"; }
                            a.SensorID = WriteRf
                            idrecord.add(a)
                        }
                        if (!WriteRR.equals("00000000")){
                            var a=SensorRecord()
                            a.Car_SensorID = RRID
                            if(navActivity.command.FALSE_CHANNEL.contains("04")){a.IsSuccess = "false"; } else { a.IsSuccess = "true"; }
                            a.SensorID = WriteRR
                            idrecord.add(a)
                        }
                        Upload_IDCopyRecord(make,model,year,startime,endtime,navActivity.serialnum, "USBPad", "IDCOPY", idrecord.size, "ALL", idrecord)
                    }else{
                        val startime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        condition=navActivity.command.ProgramAll("${navActivity.applicationContext.filesDir.path}/$mmyNum.s19",Lf)
                        val idrecord:ArrayList<SensorRecord> = ArrayList()
                       val endtime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                        for (i in 0 until navActivity.command.CHANNEL_BLE.size){
                           var a=navActivity.command.CHANNEL_BLE[i]
                            if(a.substring(0, 2).equals("04")){
                                RRID = Getid(a)
                                var b = SensorRecord()
                                b.SensorID = RRID
                                b.IsSuccess = "true"
                                idrecord.add(b)
                            }
                            if(a.substring(0, 2).equals("03")){
                                RFID = Getid(a)
                                var b = SensorRecord()
                                b.SensorID = RFID
                                b.IsSuccess = "true"
                                idrecord.add(b)
                            }
                            if(a.substring(0, 2).equals("02")){
                                LRID = Getid(a)
                                var b = SensorRecord()
                                b.SensorID = LRID
                                b.IsSuccess = "true"
                                idrecord.add(b)
                            }
                            if(a.substring(0, 2).equals("01")){
                                LFID = Getid(a)
                                var b = SensorRecord()
                                b.SensorID = LFID
                                b.IsSuccess = "true"
                                idrecord.add(b)
                            }
                        }
 for ( i in 0 until  navActivity.command.FALSE_CHANNEL.size){
     var b= SensorRecord()
     b.IsSuccess = "false"
     b.SensorID = "error"
     idrecord.add(b)
 }
                        Upload_ProgramRecord(make,model,year,startime,endtime,navActivity.serialnum, "USBPad", "IDCOPY", idrecord.size, "ALL", idrecord)
                    }
                    handler.post {
                        navActivity.back.isClickable=true
                        navActivity.back.setOnClickListener {
                            navActivity.supportFragmentManager.popBackStack(0,1)
                        }
                        navActivity.back.setImageResource(R.mipmap.menu)
                        ISPROGRAMMING=false
                        try{
                            LFID=navActivity.resources.getString(R.string.Unlinked)
                            RFID=navActivity.resources.getString(R.string.Unlinked)
                            LRID=navActivity.resources.getString(R.string.Unlinked)
                            RRID=navActivity.resources.getString(R.string.Unlinked)
                            for(a in navActivity.command.CHANNEL_BLE){
                                if(a.substring(0,a.indexOf(".")).equals("04")){
                                    RRID=a.substring(a.indexOf(".")+1,a.length)
                                    if((mmyNum.equals("RN1628")||mmyNum.equals("SI2048"))){
                                        val Writetmp=RRID.substring(0,2)+"XX"+RRID.substring(4,6)+"YY"
                                        RRID=Writetmp.replace("XX",RRID.substring(6,8)).replace("YY",RRID.substring(2,4))
                                    }
                                    RRID=RRID.substring(Idcount)
                                    UpdateUi(RR,PROGRAN_SUCCESS)
                                }
                                if(a.substring(0,a.indexOf(".")).equals("03")){
                                    RFID=a.substring(a.indexOf(".")+1,a.length)
                                    if((mmyNum.equals("RN1628")||mmyNum.equals("SI2048"))){
                                        val Writetmp=RFID.substring(0,2)+"XX"+RFID.substring(4,6)+"YY"
                                        RFID=Writetmp.replace("XX",RFID.substring(6,8)).replace("YY",RFID.substring(2,4))
                                    }
                                    RFID=RFID.substring(Idcount)
                                    UpdateUi(RF,PROGRAN_SUCCESS)
                                }
                                if(a.substring(0,a.indexOf(".")).equals("02")){
                                    LRID=a.substring(a.indexOf(".")+1,a.length)
                                    if((mmyNum.equals("RN1628")||mmyNum.equals("SI2048"))){
                                        val Writetmp=LRID.substring(0,2)+"XX"+LRID.substring(4,6)+"YY"
                                        LRID=Writetmp.replace("XX",LRID.substring(6,8)).replace("YY",LRID.substring(2,4))
                                    }
                                    LRID=LRID.substring(Idcount)
                                    UpdateUi(LR,PROGRAN_SUCCESS)
                                }
                                if(a.substring(0,a.indexOf(".")).equals("01")){
                                    LFID=a.substring(a.indexOf(".")+1,a.length)
                                    if((mmyNum.equals("RN1628")||mmyNum.equals("SI2048"))){
                                        val Writetmp=LFID.substring(0,2)+"XX"+LFID.substring(4,6)+"YY"
                                        LFID=Writetmp.replace("XX",LFID.substring(6,8)).replace("YY",LFID.substring(2,4))
                                    }
                                    LFID=LFID.substring(Idcount)
                                    UpdateUi(LF,PROGRAN_SUCCESS)
                                }
                            }
                            UpdateUiCondition(PROGRAN_SUCCESS)
                            if (!condition)  {
                                for (a in navActivity.command.FALSE_CHANNEL) {
                                    UpdateUiCondition(PROGRAN_FAULSE)
                                    when(a){
                                        "04"->{
                                            RRID=navActivity.resources.getString(R.string.error)
                                            UpdateUi(RR,PROGRAN_FAULSE)
                                        }
                                        "03"->{
                                            RFID=navActivity.resources.getString(R.string.error)
                                            UpdateUi(RF,PROGRAN_FAULSE)
                                        }
                                        "02"->{
                                            LRID=navActivity.resources.getString(R.string.error)
                                            UpdateUi(LR,PROGRAN_FAULSE)
                                        }
                                        "01"->{
                                            LFID=navActivity.resources.getString(R.string.error)
                                            UpdateUi(LF,PROGRAN_FAULSE)
                                        }
                                    }
                                }
                                for (a in navActivity.command.BLANK_CHANNEL) {
                                    when(a){
                                        "04"->{
                                            UpdateUi(RR,UNLINK)
                                        }
                                        "03"->{
                                            UpdateUi(RF,UNLINK)
                                        }
                                        "02"->{
                                            UpdateUi(LR,UNLINK)
                                        }
                                        "01"->{
                                            UpdateUi(LF,UNLINK)
                                        }
                                    }
                                }
                                if(navActivity.command.FALSE_CHANNEL.size==0&&navActivity.command.BLANK_CHANNEL.size==0){
                                    UpdateUiCondition(PROGRAN_FAULSE)
                                    UpdateUi(LF,PROGRAN_FAULSE)
                                    UpdateUi(LR,PROGRAN_FAULSE)
                                    UpdateUi(RF,PROGRAN_FAULSE)
                                    UpdateUi(RR,PROGRAN_FAULSE)
                                }
                            }
                        }catch (e:Exception){e.printStackTrace()}
                    }
                }).start()


        }

    }
}
