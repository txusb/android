package com.orango.electronic.orangetxusb.E_talking

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.orango.electronic.orangetxusb.R

class ShowImage : AppCompatActivity() {
    lateinit var title:TextView
    var link=""
    lateinit var image:SimpleDraweeView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)
        title=findViewById(R.id.title)
        link=intent.getStringExtra("url")
        title.setText(link)
        image=findViewById(R.id.image2)
        image.setImageURI(link)

    }
    fun onclick(view:View){
        this.finish()
    }

}
