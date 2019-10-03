package com.orango.electronic.orangetxusb.Adapter


import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.orango.electronic.orangetxusb.EventBus.ConnectBle
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.ScanBle
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity.Companion.blename
import kotlinx.android.synthetic.main.fragment_pad_select.*
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList


class SelectBle(private val a:ArrayList<BluetoothDevice>,private val scanner:ScanBle)
    : RecyclerView.Adapter<SelectBle.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.selectble, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(a[position].name==null){holder.device.text="Unknown Device"}else{holder.device.text=a[position].name}

        holder.address.text=a[position].address
        holder.mView.setOnClickListener(View.OnClickListener {
            blename=a[position].address
            scanner.scan.scanLeDevice(false)
            scanner.finish()
                EventBus.getDefault().post(ConnectBle(false))
        })
    }

    override fun getItemCount(): Int = a.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val device: TextView = mView.findViewById(R.id.textView)
        val address: TextView = mView.findViewById(R.id.textView2)
        override fun toString(): String {
            return super.toString() + " ''"
        }
    }
}