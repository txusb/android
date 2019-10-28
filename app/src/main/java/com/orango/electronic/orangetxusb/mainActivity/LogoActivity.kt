package com.orango.electronic.orangetxusb.mainActivity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.orango.electronic.orangetxusb.AdminActivity.Enroll
import com.orango.electronic.orangetxusb.AdminActivity.SetArea
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.tool.LanguageUtil
import java.util.ArrayList

class LogoActivity : AppCompatActivity() {
    companion object {
        private val permissionRequestCode = 10
        private val Permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        var admin=""
        var password=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)
        SetWindows()
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun SetWindows() {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequestCode ->
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults.indices) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i])
                        }
                    }
                }
        }
    }

    private fun checkPermissions() {
        val permissionDeniedList = ArrayList<String>()
        for (permission in Permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            val deniedPermissions = permissionDeniedList.toTypedArray()
            ActivityCompat.requestPermissions(this, deniedPermissions, permissionRequestCode)
        }
    }

    private fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
            }
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
                val a= profilePreferences.getString("Language","English")
                when(a){
                    "繁體中文"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_TAIWAIN);}
                    "简体中文"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_CHINESE);}
                    "Deutsch"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_DE);}
                    "English"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_ENGLISH);}
                    "Italiano"->{ LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_ITALIANO);}
                }
                val handler = Handler()
//                handler.postDelayed(Runnable {
//                    val intent = Intent(this,NavigationActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                },2000)
                admin=profilePreferences.getString("admin","nodata")
                password=profilePreferences.getString("password","nodata")
                if(admin.equals("nodata")){
                    handler.postDelayed(Runnable {
                        val intent = Intent(this,SetArea::class.java)
                        startActivity(intent)
                        finish()
                    },2000)
                }else{
                    handler.postDelayed(Runnable {
                        val intent = Intent(this,NavigationActivity::class.java)
                        startActivity(intent)
                        finish()
                    },2000)
                }

//                LanguageUtil.updateLocale(this, LanguageUtil.LOCALE_ENGLISH);
//                val handler = Handler()
//                handler.postDelayed(Runnable {
//                    val intent = Intent(this,SetArea::class.java)
//                    startActivity(intent)
//                    finish()
//                },2000)
            }
        }
    }
}
