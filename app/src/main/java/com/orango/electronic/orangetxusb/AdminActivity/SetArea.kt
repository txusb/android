package com.orango.electronic.orangetxusb.AdminActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.orango.electronic.orangetxusb.R
import android.widget.Spinner
import com.orango.electronic.orangetxusb.mainActivity.HomeFragment
import com.orango.electronic.orangetxusb.tool.LanguageUtil
import java.util.ArrayList




class SetArea : AppCompatActivity() {
    lateinit var AreaSpinner: Spinner
    lateinit var profileEditor : SharedPreferences.Editor
    lateinit var LanguagesSpinner: Spinner
    lateinit var profilePreferences:SharedPreferences
    var Arealist=ArrayList<String>()
    var LanguageList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_area)
        profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        AreaSpinner=findViewById(R.id.spinner)
        LanguagesSpinner=findViewById(R.id.spinner3)
        Arealist.add("Select")
        Arealist.add("EU")
        Arealist.add("North America")
        Arealist.add("台灣")
        Arealist.add("中國大陸")
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arealist)
        AreaSpinner.adapter=arrayAdapter
        LanguageList.add("Select")
        LanguageList.add("繁體中文")
        LanguageList.add("简体中文")
        LanguageList.add("Deutsche")
        LanguageList.add("English")
        LanguageList.add("Italiano")
        val lanAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LanguageList)
        LanguagesSpinner.adapter=lanAdapter
    }
fun onclick(view: View){
    when(view.id){
        R.id.next->{
            if(LanguagesSpinner.selectedItem.toString().equals("Select")||AreaSpinner.selectedItem.toString().equals("Select")){return}
            profileEditor = profilePreferences.edit()
            profileEditor.putString("Area",AreaSpinner.selectedItem.toString())
            profileEditor.putString("Language",LanguagesSpinner.selectedItem.toString())
            profileEditor.commit()
            Log.d("Language",LanguagesSpinner.selectedItem.toString())
            when(LanguagesSpinner.selectedItem.toString()){
                "繁體中文"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_TAIWAIN);}
                "简体中文"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_CHINESE);}
                "Deutsche"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_DE);}
                "English"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_ENGLISH);}
                "Italiano"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_ITALIANO);}
            }
            val intent = Intent(this,PrivaryPolice::class.java)
            startActivity(intent)
            finish()
        }
    }
}
}
