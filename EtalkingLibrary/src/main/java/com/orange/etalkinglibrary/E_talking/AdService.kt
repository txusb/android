package com.orange.etalkinglibrary.E_talking

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import com.orange.etalkinglibrary.R
import java.util.*
import kotlin.concurrent.schedule

class AdService : Service() {
    lateinit var timer: Timer
    companion object{var count=0}

    override fun onCreate() {
        super.onCreate()
        Log.v("wang", "OnCreate 服务启动时调用")
        timer=Timer()
        timer.schedule(0,10000){
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
        val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        E_Command.admin=profilePreferences.getString("admin","nodata")
        Thread{
            var messagecount=E_Command.GetTopMessage()
            handler.post {
                Log.d("message", "$messagecount")
                if (messagecount != 0 && messagecount != -1 && messagecount != count) {
                    handler.post {  NotificationManager().AddAdvice(resources.getString(R.string.Online_customer_service),resources.getString(
                        R.string.Customer_service_specialist),this)  }
                }
                count=messagecount
            }

        }.start()
    }
}
