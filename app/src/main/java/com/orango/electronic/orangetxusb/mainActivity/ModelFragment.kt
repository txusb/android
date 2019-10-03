package com.orango.electronic.orangetxusb.mainActivity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.orango.electronic.orangetxusb.Adapter.ShowModel
import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mmySql.Item
import kotlinx.android.synthetic.main.fragment_model.view.*


class ModelFragment : Fragment() {
    companion object {
        const val stringMake = "make"
        const val stringMakeImg = "make.img"
    }

    lateinit var navActivity: NavigationActivity
    lateinit var rootView: View
    lateinit var grid: RecyclerView
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var models: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        navActivity = activity as NavigationActivity

        make = arguments!!.getString(stringMake)!!
        makeImg = arguments!!.getString(stringMakeImg)!!
        when(NavigationActivity.Action){
            "PROGRAM"->{
                models = navActivity.itemDAO.getModel(make)!!
            }
            "IDCOPY"->{
                models=navActivity.itemDAO.getModel(make)!!
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_model, container, false)
        navActivity.back.visibility=View.VISIBLE
//        val mipmapId = resources.getIdentifier(makeImg, "mipmap", activity!!.packageName)
//        Glide.with(this@ModelFragment).load(mipmapId)
//            .centerCrop().into(rootView.make_img)
//        rootView.make_img.setImageResource(mipmapId)
//        rootView.make_text.text = make
        val gridAdapter = ShowModel(models,make,makeImg,fragmentManager!!)
        grid = rootView.findViewById(R.id.model_grid)
        val mgr = GridLayoutManager(this@ModelFragment.context,2)
        grid.layoutManager=mgr
        grid.adapter = gridAdapter
        return rootView
    }

}
