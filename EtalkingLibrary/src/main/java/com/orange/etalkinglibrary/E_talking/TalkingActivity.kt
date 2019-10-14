package com.orange.etalkinglibrary.E_talking;


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.orange.etalkinglibrary.E_talking.E_Command
import com.orange.etalkinglibrary.E_talking.E_Command.*
import com.orange.etalkinglibrary.E_talking.Glide4Engine
import com.orange.etalkinglibrary.E_talking.Messageitem
import com.orange.etalkinglibrary.E_talking.upload
import com.orange.etalkinglibrary.R
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.util.*
import kotlin.concurrent.schedule

class TalkingActivity : AppCompatActivity() {
    lateinit var re: RecyclerView
    var it= Messageitem()
    lateinit var file: Uri
    var REQUEST_CODE_CHOOSE = 1023
    lateinit var showimage:RelativeLayout
    var cell= InMail(it)
    lateinit var im: ImageView
    lateinit var timer: Timer
    var refresh=true
    var image="nodata"
    var talkingwho="admin"
    lateinit var tit:TextView
    lateinit var sender: EditText
    lateinit var InternalError:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_talking_fragement)
        val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        E_Command.admin=profilePreferences.getString("admin","nodata")
        re=findViewById(R.id.re)
        tit=findViewById(R.id.title)
        sender=findViewById(R.id.sender)
        InternalError=findViewById(R.id.InternetError)
        im=findViewById(R.id.im)
        showimage=findViewById(R.id.showimage)
        re.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,true)
        tit.text=resources.getString(R.string.Online_customer_service)
        re.adapter=cell
        re.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val b = recyclerView.canScrollVertically(-1)
                Log.d("hey","assa${b}size${it.admin.size}${it.button}")
                if(!b&&!it.button&&it.admin.size>49){
                    logdata(it.id[it.admin.size-1])
                }
            }
        })
        logdata("0")
        timer=Timer()
        timer.schedule(0,1000){
            newMail()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
    fun onclick(view:View){
        when(view.id){
            R.id.close->{
                image="nodata"
                showimage.visibility=View.GONE
            }
            R.id.back->{
                this.finish()
            }
            R.id.camera->{
                tool()
            }
            R.id.send->{
                if(sender.text.isEmpty()){
                    Toast.makeText(this,R.string.notempty, Toast.LENGTH_SHORT).show()
                    return
                }
                if(image.equals("nodata")){
                    E_Command.show(this)
                    send()
                }else{
                    E_Command.show(this)
                    upload.upload(file,this)
                }
            }
        }
    }
    var handler= Handler()
    fun newMail(){
        if(refresh){
            refresh=false
            if(it.id.size==0){
                refresh=true
                logdata("0")
                return}
            Thread{
                GetNewMail(it.id[0],it,talkingwho)
                handler.post {
                    refresh=true
                    if(it.success){
                        InternalError.visibility=View.GONE
                        cell.notifyDataSetChanged()
                    }
                }
            }.start()
        }
    }
    fun logdata(take:String){
        if(refresh){
            E_Command.show(this)
            refresh=false
            Thread{
                GetMail(take,it,talkingwho)
                handler.post {
                    InternalError.visibility=View.GONE
                    E_Command.dismiss()
                    refresh=true
                    if(it.success){
                        cell.notifyDataSetChanged()
                    }else{
                        InternalError.visibility=View.VISIBLE
                    }
                }
            }.start()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainResult(data)
            im.setImageURI(result[0])
            file=result[0]
            image="havedata"
            showimage.visibility=View.VISIBLE
        }
    }
    fun tool() {
        Matisse.from(this)
            .choose(MimeType.ofImage())//图片类型
            .countable(false)//true:选中后显示数字;false:选中后显示对号
            .maxSelectable(1)
            .showSingleMediaType(true)
            .capture(false)//选择照片时，是否显示拍照
            .imageEngine( Glide4Engine())
            .captureStrategy(
                CaptureStrategy(
                    true,
                    "com.example.xx.fileprovider"
                )
            )//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
            .forResult(REQUEST_CODE_CHOOSE)//
    }
    fun send(){
        var a=sender.text.toString()
        Thread{
            var response=SendMail(talkingwho,image,a)
            handler.post {
                image="nodata"
                E_Command.dismiss()
                if(response){
                    Toast.makeText(this,R.string.sendsuccess, Toast.LENGTH_SHORT).show()
                    sender.setText("")
                    showimage.visibility=View.GONE
                }else{
                    Toast.makeText(this,R.string.sendfalse, Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
