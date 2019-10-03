package com.orango.electronic.orangetxusb.tool

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class NoInternet{
    fun dol(path:String,context: Context):Boolean{
          val mInputStream = context.assets.open("usb_tx_mmy.db")
            val mOutputStream = FileOutputStream(path)
            val buffer = ByteArray(1024)
            var count = 0
            while (count != -1) {
                if(count !=0){
                    mOutputStream.write(buffer, 0, count)
                }
                count = mInputStream.read(buffer, 0, buffer.size)
            }
            mOutputStream.flush()
            mOutputStream.close()
            mInputStream.close()
        return  true
    }
    fun dols19(path:String,context: Context,s19:String):Boolean{
        val mInputStream = context.assets.open(s19)
        val mOutputStream = FileOutputStream(path)
        val buffer = ByteArray(1024)
        var count = 0
        while (count != -1) {
            if(count !=0){
                mOutputStream.write(buffer, 0, count)
            }
            count = mInputStream.read(buffer, 0, buffer.size)
        }
        mOutputStream.flush()
        mOutputStream.close()
        mInputStream.close()
        return  true
    }
}