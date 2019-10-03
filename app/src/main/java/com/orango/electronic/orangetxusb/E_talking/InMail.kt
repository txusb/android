package com.orango.electronic.orangetxusb.E_talking




import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.orango.electronic.orangetxusb.E_talking.Messageitem.CalculateTime
import com.orango.electronic.orangetxusb.E_talking.Messageitem.loadResPic
import com.orango.electronic.orangetxusb.R


class InMail(private val a: Messageitem)
    : RecyclerView.Adapter<InMail.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.in_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.me.visibility=View.GONE
        holder.you.visibility=View.GONE
        if(position==a.admin.size){
            holder.you.visibility=View.VISIBLE
            holder.image.visibility=View.GONE
            holder.po.text="您好，請詳細說明產品問題"
            loadResPic(holder.mView.context,holder.head,R.mipmap.mastericon)
            holder.name.text="客服專員"
        }else{
            if(a.admin[position].equals(E_Command.admin)){
                holder.me.visibility=View.VISIBLE
                if(a.file[position].equals("nodata")){holder.imageme.visibility=View.GONE}else{holder.imageme.visibility=View.VISIBLE
                    holder.imageme.setImageURI(a.file[position])
                    holder.imageme.setOnClickListener { var intent= Intent(holder.mView.context, ShowImage::class.java)
                        intent.putExtra("url",a.file[position])
                        holder.mView.context.startActivity(intent)}
                }
                holder.pome.setText(a.message[position])
                holder.timeme.text=CalculateTime(a.time[position])
            }else{
                holder.you.visibility=View.VISIBLE
                if(a.file[position].equals("nodata")){holder.image.visibility=View.GONE}else{
                    holder.image.setOnClickListener { var intent= Intent(holder.mView.context, ShowImage::class.java)
                        intent.putExtra("url",a.file[position])
                        holder.mView.context.startActivity(intent)}

                    holder.image.visibility=View.VISIBLE
                    holder.image.setImageURI(a.file[position])}
                holder.po.text=a.message[position]
                loadResPic(holder.mView.context,holder.head,R.mipmap.mastericon)
                holder.name.text="客服專員"
                holder.time.text=CalculateTime(a.time[position])
            }
        }
    }

    override fun getItemCount(): Int = a.admin.size+1

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val me: RelativeLayout = mView.findViewById(R.id.me)
        val you: RelativeLayout = mView.findViewById(R.id.you)
        val imageme:SimpleDraweeView=mView.findViewById(R.id.imageme);
        val pome:TextView=mView.findViewById(R.id.pome)
        val timeme:TextView=mView.findViewById(R.id.timeme)
        val image:SimpleDraweeView=mView.findViewById(R.id.image);
        val head:SimpleDraweeView=mView.findViewById(R.id.head);
        val name:TextView=mView.findViewById(R.id.name);
        val po:TextView=mView.findViewById(R.id.po)
        val time:TextView=mView.findViewById(R.id.time)
        override fun toString(): String {
            return super.toString() + " ''"
        }
    }


}