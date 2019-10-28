package com.orango.electronic.orangetxusb.mainActivity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orango.electronic.orangetxusb.R
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.facebook.drawee.view.SimpleDraweeView
import com.orango.electronic.orangetxusb.Adapter.ShowItemImage
import com.orango.electronic.orangetxusb.mmySql.Item
import com.orango.electronic.orangetxusb.tool.ComPressImage.load
import java.io.IOException
import java.lang.Exception


class MakeFragment : Fragment() {
    lateinit var navActivity: NavigationActivity
    lateinit var rootView: View
    lateinit var grid: RecyclerView
    lateinit var makes: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navActivity = activity as NavigationActivity
        when(NavigationActivity.Action){
            "PROGRAM"->{
                makes = navActivity.itemDAO.getMake(activity!!)!!
            }
            "IDCOPY"->{
makes=navActivity.itemDAO.getMake(activity!!)!!
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_make, container, false)
        val gridAdapter = ShowItemImage(navActivity,makes,fragmentManager)
        grid = rootView.findViewById(R.id.make_grid)
        grid.adapter = gridAdapter
        val mgr =GridLayoutManager(this@MakeFragment.context,3)
        grid.layoutManager=mgr
        navActivity.back.visibility=View.VISIBLE
        Thread{     navActivity.command.Setserial(navActivity)
        Log.d("serial",navActivity.serialnum)
        }.start()
        return rootView
    }

}
