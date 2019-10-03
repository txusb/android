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
import com.orango.electronic.orangetxusb.Adapter.ShowYear
import com.orango.electronic.orangetxusb.R
import kotlinx.android.synthetic.main.fragment_year.view.*

class YearFragment : Fragment() {
    companion object {
        const val stringMake = "make"
        const val stringMakeImg = "make.img"
        const val stringModel = "model"
    }

    lateinit var navActivity: NavigationActivity
    lateinit var rootView: View
    lateinit var grid: RecyclerView
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var model: String
    lateinit var years: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        navActivity = activity as NavigationActivity

        make = arguments!!.getString(stringMake)!!
        makeImg = arguments!!.getString(stringMakeImg)!!
        model = arguments!!.getString(stringModel)!!
        when(NavigationActivity.Action){
            "PROGRAM"->{
                years = navActivity.itemDAO.getYear(make,model)!!
            }
            "IDCOPY"->{
                years = navActivity.itemDAO.getYear(make,model)!!
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_year, container, false)
//
//        val mipmapId = resources.getIdentifier(makeImg, "mipmap", activity!!.packageName)
//        Glide.with(this@YearFragment).load(mipmapId)
//            .centerCrop().into(rootView.make_img)
//        rootView.model_text.text = model
        navActivity.back.visibility=View.VISIBLE
        val gridAdapter = ShowYear(years,make,makeImg,fragmentManager!!,model)
        grid = rootView.findViewById(R.id.year_grid)
        val mgr = GridLayoutManager(this@YearFragment.context,2)
        grid.layoutManager=mgr
        grid.adapter = gridAdapter
        return rootView
    }


}
