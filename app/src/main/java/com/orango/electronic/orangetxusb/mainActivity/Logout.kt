package com.orango.electronic.orangetxusb.mainActivity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.orango.electronic.orangetxusb.R

class Logout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout)
        Thread{}.start()
    }
    fun onclick(view: View){
        when(view.id){
            R.id.textView14->{
                this.finish()}
            R.id.textView15->{
                val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
                profilePreferences.edit().putString("admin","nodata").commit()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }
}
