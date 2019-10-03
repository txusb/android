package com.orango.electronic.orangetxusb.AdminActivity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity

class PrivaryPolice : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privary_police)
    }
    fun onclick(view: View){
        when(view.id){
            R.id.button5->{
                this.finish()
            }
            R.id.button6->{
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            }
        }
    }
}
