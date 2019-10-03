package com.orango.electronic.orangetxusb.AdminActivity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.orango.electronic.orangetxusb.HttpCommand.Fuction
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import java.util.ArrayList

class Enroll : AppCompatActivity() {
    lateinit var AreaSpinner: Spinner
    lateinit var Store: Spinner
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repeatpassword: EditText
    lateinit var serialnumber: EditText
    lateinit var firstname: EditText
    lateinit var lastname: EditText
    lateinit var company: EditText
    lateinit var programAnimator: LottieAnimationView
    lateinit var phone: EditText
    lateinit var streat: EditText
    lateinit var city: EditText
    lateinit var state: EditText
    lateinit var zpcode: EditText
    lateinit var loadtitle: TextView
    lateinit var load: RelativeLayout

    var Arealist= ArrayList<String>()
    var Arealist2= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll)
        load=findViewById(R.id.load)
        loadtitle=findViewById(R.id.textView11)
        programAnimator=findViewById(R.id.animation_view6)
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)
        repeatpassword=findViewById(R.id.repeatpassword)
        serialnumber=findViewById(R.id.serialnumber)
        firstname=findViewById(R.id.firstname)
        lastname=findViewById(R.id.lastname)
        company=findViewById(R.id.company)
        phone=findViewById(R.id.phone)
        streat=findViewById(R.id.streat)
        city=findViewById(R.id.city)
        state=findViewById(R.id.state)
        zpcode=findViewById(R.id.zpcode)
        Store=findViewById(R.id.spinner6)
        AreaSpinner=findViewById(R.id.spinner5)
        Arealist.add("Select")
        Arealist.add("EU")
        Arealist.add("North America")
        Arealist.add("台灣")
        Arealist.add("中國大陸")
        Arealist2.add(getString(R.string.Distributor))
        Arealist2.add(getString(R.string.Retailer))
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arealist)
        val arrayAdapter2 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arealist2)
        AreaSpinner.adapter=arrayAdapter
        Store.adapter=arrayAdapter2
    }
    var isrunn=false
    fun onclick(view: View){
        when(view.id){
            R.id.imageView76->{this.finish()}
            R.id.cancel->{
               this.finish()
            }
            R.id.next->{
                if(isrunn){
                    return
                }
                loading()
                var email=email.text.toString()
                 var password=password.text.toString()
                 var repeatpassword=repeatpassword.text.toString()
                 var serialnumber=serialnumber.text.toString()
                 var firstname=firstname.text.toString()
                 var lastname=lastname.text.toString()
                 var company=company.text.toString()
                 var phone=phone.text.toString()
                 var streat=streat.text.toString()
                 var city=city.text.toString()
                 var state=AreaSpinner.selectedItem.toString()
                var storetype=Store.selectedItem.toString()
                 var zpcode=zpcode.text.toString()
                if(!password.equals(repeatpassword)){
                    Toast.makeText(this,resources.getString(R.string.confirm_password),Toast.LENGTH_SHORT).show()
                }
                Thread{
                    isrunn=true
                    var a=0
                    if(storetype.equals(getString(R.string.Distributor))){
                         a=Fuction.Register(email,password,serialnumber,"Distributor",company,firstname,lastname,phone,state,city,streat,zpcode)
                    }else{
                         a=Fuction.Register(email,password,serialnumber,"Retailer",company,firstname,lastname,phone,state,city,streat,zpcode)
                    }
                    handler.post {
                        LoadingSuccess()
                        val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
                        if(a==-1){
                            Toast.makeText(this,resources.getString(R.string.error),Toast.LENGTH_SHORT).show()
                        }else if(a==1){
                            Toast.makeText(this,resources.getString(R.string.be_register),Toast.LENGTH_SHORT).show()
                        }else{
                            profilePreferences.edit().putString("admin",email).putString("password",password).commit()
                            val intent = Intent(this, NavigationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    isrunn=false
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
    fun LoadingSuccess(){
        load.visibility=View.GONE
        programAnimator.visibility=View.GONE
    }
}
