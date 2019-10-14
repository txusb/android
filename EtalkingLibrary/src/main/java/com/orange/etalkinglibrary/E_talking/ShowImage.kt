package com.orange.etalkinglibrary.E_talking;

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.orange.etalkinglibrary.R

class ShowImage : AppCompatActivity() {
    lateinit var title:TextView
    var link=""
    lateinit var image: SimpleDraweeView
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
