package com.orango.electronic.orangetxusb.tool

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import com.orange.etalkinglibrary.E_talking.E_Command
import com.orango.electronic.orangetxusb.R
import java.util.*
import kotlin.concurrent.schedule

class AdService : Service() {
    lateinit var timer: Timer
    override fun onCreate() {
        super.onCreate()
        Log.v("wang", "OnCreate 服务启动时调用")
        timer=Timer()
        timer.schedule(0,5000){
            GetMessage()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    //服务被关闭时调用
    override fun onDestroy() {
        super.onDestroy()
        Log.v("wang", "onDestroy 服务关闭时")
        timer.cancel()
    }
    var handler= Handler()
    fun GetMessage(){
        Log.v("wang", "timer運行中")
        handler.post {  NotificationManager().AddAdvice(resources.getString(R.string.Online_customer_service),resources.getString(
            R.string.Customer_service_specialist),this)  }

    }
}
