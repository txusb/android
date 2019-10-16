package com.orango.electronic.orangetxusb.Adapter

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.orango.electronic.orangetxusb.UsbPad.Pad_Idcopy
import com.orango.electronic.orangetxusb.UsbPad.StartProgram
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.UsbCable.Id_copy
import com.orango.electronic.orangetxusb.UsbCable.Cable_Program
import com.orango.electronic.orangetxusb.mainActivity.*
import java.util.ArrayList


class ShowYear(private val years: ArrayList<String>, private val make: String, private val makeImg: String, private val fragmentManager: FragmentManager,private  val model:String)
    : RecyclerView.Adapter<ShowYear.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.text_item.text=years[position]
    holder.mView.setOnClickListener{
        Relarm.position=1
        val fragment = Relarm()
        val args = Bundle()
        args.putString(Cable_Program.stringMake, make)
        args.putString(Cable_Program.stringMakeImg, makeImg)
        args.putString(Cable_Program.stringModel, model)
        args.putString(Cable_Program.stringYear, years[position])
        fragment.arguments=args
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment, "Relarm")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
            .addToBackStack("Relarm")
            // 提交事務
            .commit()
}
    }

    override fun getItemCount(): Int = years.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val select_img: ImageView = mView.findViewById(R.id.select_img)
        val text_item: TextView = mView.findViewById(R.id.text_item)
        override fun toString(): String {
            return super.toString() + " '" + text_item.text + "'"
        }
    }
}