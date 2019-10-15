package com.orango.electronic.orangetxusb.tool

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.AppCompatActivity
import com.orange.etalkinglibrary.E_talking.TalkingActivity
import com.orango.electronic.orangetxusb.R

class NotificationManager{

    fun AddAdvice(title:String,content:String,context:Context){
         val NOTIFICATION_ID_1 = 1
        var myManager =  context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager;
        val pi = PendingIntent.getActivity(
            context,
            100,
            Intent(context, TalkingActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        var  LargeBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        val myBuilder = Notification.Builder(context)
        myBuilder.setContentTitle(title)
            .setContentText(content)
            //設定狀態列中的小圖片，尺寸一般建議在24×24，這個圖片同樣也是在下拉狀態列中所顯示
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(LargeBitmap)
            //設定預設聲音和震動
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
            .setAutoCancel(true)//點選後取消
            .setWhen(System.currentTimeMillis())//設定通知時間
            .setPriority(Notification.PRIORITY_HIGH)//高優先順序
            .setVisibility(Notification.VISIBILITY_PRIVATE)
            .setContentIntent(pi)
       var myNotification = myBuilder.build()
        //4.通過通知管理器來發起通知，ID區分通知
        myManager!!.notify(NOTIFICATION_ID_1, myNotification)
    }
}