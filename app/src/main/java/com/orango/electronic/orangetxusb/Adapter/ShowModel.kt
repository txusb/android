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
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.ModelFragment
import com.orango.electronic.orangetxusb.mainActivity.YearFragment
import java.util.ArrayList


class ShowModel(private val models: ArrayList<String>, private val make: String,private val makeImg: String,private val fragmentManager: FragmentManager)
    : RecyclerView.Adapter<ShowModel.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text_item.text=models[position]
holder.mView.setOnClickListener{
    val args = Bundle()
    args.putString(ModelFragment.stringMake, make)
    args.putString(YearFragment.stringMakeImg, makeImg)
    args.putString(YearFragment.stringModel, models[position])
    val fragment = YearFragment()
    fragment.arguments = args
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.nav_host_fragment, fragment, "Select Year")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
        .addToBackStack("Select Year")
        .commit()
}
    }

    override fun getItemCount(): Int = models.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val select_img: ImageView = mView.findViewById(R.id.select_img)
        val text_item: TextView = mView.findViewById(R.id.text_item)
        override fun toString(): String {
            return super.toString() + " '" + text_item.text + "'"
        }
    }
}