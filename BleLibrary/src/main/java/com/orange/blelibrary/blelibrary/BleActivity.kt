package com.orange.blelibrary.blelibrary;

import android.app.Instrumentation
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.orange.blelibrary.R
import com.orange.blelibrary.blelibrary.EventBus.ConnectBle
import com.orange.blelibrary.blelibrary.ScanBle
import com.orange.blelibrary.blelibrary.Server.BleServiceControl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.concurrent.schedule

open class BleActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    override fun onBackStackChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    var RXDATA=""
    lateinit var Translation:Fragment
    var ConnectDelay=10
    var bleServiceControl = BleServiceControl()
    var id=0

    fun init(){
        EventBus.getDefault().register(this)
        supportFragmentManager.addOnBackStackChangedListener(this)
        bleServiceControl.act=this
    }
    public override fun onDestroy() {
        if(!bleServiceControl.first){
            unbindService(bleServiceControl.mServiceConnection)
        }
        super.onDestroy()
    }

    open  fun  goback(){
      supportFragmentManager!!.popBackStack()
    }
     var handler= Handler()

    open fun LoadingUI(a:String,pass:Int){

    }
    open fun LoadingSuccessUI(){

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(a: ConnectBle) {
        LoadingUI(resources.getString(R.string.paired_with_your_device),0)
        bleServiceControl.connect(a.reback)
        Thread{
            var fal=0
            while(true){
                if(bleServiceControl.isconnect||fal==ConnectDelay){break}
                Thread.sleep(1000)
                fal++
            }
            handler.post {LoadingSuccessUI()
                if(bleServiceControl.isconnect){
                    val transaction = supportFragmentManager!!.beginTransaction()
                    transaction.replace(id, Translation)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                            .addToBackStack(null)
                            .commit()
                }
            }
        }.start()
    }
    open fun ConnectSituation(boolean: Boolean){
        if (boolean) {
            Log.d("連線","連線ok")
        } else {
            LoadingSuccessUI()
            Log.d("連線","Bluetooth is disconnected")
            handler.post {
                Toast.makeText(this,"Bluetooth is disconnected",Toast.LENGTH_SHORT).show()
            }
        }
    }
    open fun RX(a:String){
        try {
//            Log.w("BLEDATA", "RX:$a")
        } catch (e: Exception) {
            Log.w("error", e.message)
        }
    }
    open fun TX(a:String){
        try {
            Log.w("BLEDATA", "TX:$a")
        } catch (e: Exception) {
            Log.w("error", e.message)
        }
    }
    open fun GoScanner(Translation:Fragment,DelayTime:Int,id:Int,tag:String){
        ConnectDelay=DelayTime
        this.Translation=Translation
        this.id=id
        if(bleServiceControl.isconnect){
            val transaction = supportFragmentManager!!.beginTransaction()
            transaction.replace(id, Translation,tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                    .addToBackStack(null)
                    .commit()
        }else{
            startActivity(Intent(this, ScanBle::class.java))
        }
    }
    open fun ChangePage(Translation:Fragment,id:Int,tag:String,goback:Boolean){
        if(goback){
            val transaction = supportFragmentManager!!.beginTransaction()
            transaction.replace(id, Translation,tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack(tag)
                .commit()
        }else{  val transaction = supportFragmentManager!!.beginTransaction()
            transaction.replace(id, Translation,tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .commit()}

    }
}
