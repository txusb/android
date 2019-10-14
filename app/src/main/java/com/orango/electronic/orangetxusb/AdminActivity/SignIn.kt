package com.orango.electronic.orangetxusb.AdminActivity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.orango.electronic.orangetxusb.HttpCommand.Fuction
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity

class SignIn : AppCompatActivity() {
    lateinit var loadtitle: TextView
    lateinit var load: RelativeLayout
    lateinit var admin: EditText
    lateinit var password: EditText
    lateinit var programAnimator: LottieAnimationView
    var run=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        admin=findViewById(R.id.editText3)
        password=findViewById(R.id.editText4)
        load=findViewById(R.id.load)
        programAnimator=findViewById(R.id.animation_view7)
        loadtitle=findViewById(R.id.textView11)
    }
    fun onclick(view:View){
        when(view.id){
            R.id.textView26->{
                val intent = Intent(this, ResetPass::class.java)
                startActivity(intent)
            }
            R.id.imageView22 ->{
                val intent = Intent(this, ResetPass::class.java)
                startActivity(intent)
            }
            R.id.button2->{
                val intent = Intent(this, Enroll::class.java)
                startActivity(intent)
            }
            R.id.button4->{
                if(run){return}
                run=true
                loading()
                var admin=admin.text.toString()
                var password=password.text.toString()
                Thread{
                    var a=Fuction.ValidateUser(admin,password)
                    run=false
                    handler.post {
                        LoadingSuccessUI()
                        if(a){
                            val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
                            profilePreferences.edit().putString("admin",admin).putString("password",password).commit()
                            val intent = Intent(this, NavigationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                           Toast.makeText(this,R.string.signfall,Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            }
        }
    }
    var handler= Handler()
    fun loading(){
        loadtitle.text=resources.getString(R.string.Data_Loading)
        load.visibility=View.VISIBLE
        programAnimator.visibility=View.VISIBLE
    }
    fun LoadingSuccessUI(){
        load.visibility=View.GONE
        programAnimator.visibility=View.GONE
    }
}
