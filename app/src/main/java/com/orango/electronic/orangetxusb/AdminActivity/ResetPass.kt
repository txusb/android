package com.orango.electronic.orangetxusb.AdminActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.orango.electronic.orangetxusb.HttpCommand.Fuction.ResetPassword
import com.orango.electronic.orangetxusb.R

class ResetPass : AppCompatActivity() {
var run=false
    lateinit var edit:EditText
    lateinit var load:RelativeLayout
    lateinit var programAnimator: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)
        edit=findViewById(R.id.editText2)
        load=findViewById(R.id.load)
        programAnimator=findViewById(R.id.animation_view8)
    }
    fun onclick(view: View){
        when(view.id){
            R.id.button14->{
if(run){return}
                loading()
                var email=edit.text.toString()
                Thread{
                   var isok= ResetPassword(email)
                    handler.post {
                        run=false
                        loadsuccess()
                        if(isok){
this.finish()
                        }else{
                 Toast.makeText(this,R.string.nointernet,Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            }
            R.id.imageView80->{
                this.finish()
            }
        }
    }
    var handler= Handler()
    fun loading(){
        load.visibility=View.VISIBLE
        programAnimator.visibility=View.VISIBLE
    }
    fun loadsuccess(){
        load.visibility=View.GONE
        programAnimator.visibility=View.GONE
    }
}
