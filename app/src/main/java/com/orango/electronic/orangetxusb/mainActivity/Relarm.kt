package com.orango.electronic.orangetxusb.mainActivity


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.UsbCable.Cable_Program
import kotlinx.android.synthetic.main.fragment_relarm.view.*
import kotlinx.android.synthetic.main.fragment_mmy.view.mmy_text as mmy_text1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Relarm : Fragment() {
    lateinit var rootView: View
    lateinit var text:TextView
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var model: String
    lateinit var year: String
    lateinit var navActivity: NavigationActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.fragment_relarm, container, false)
        navActivity = activity as NavigationActivity
        retainInstance = true
        navActivity.back.visibility=View.VISIBLE
        rootView.menu3.setOnClickListener {
            val fragment = HomeFragment()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment, "HomeFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Program Sensor")
                // 提交事務
                .commit()
        }
        rootView.mmy_text.text = "$make/$model /$year"
        rootView.textView10.text=navActivity.itemDAO.GetreLarm(make,model,year,navActivity)
        return rootView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        navActivity = activity as NavigationActivity
        make = arguments!!.getString(Cable_Program.stringMake)!!
        makeImg = arguments!!.getString(Cable_Program.stringMakeImg)!!
        model = arguments!!.getString(Cable_Program.stringModel)!!
        year = arguments!!.getString(Cable_Program.stringYear)!!

    }
    override fun onResume() {
        super.onResume()
        navActivity.setActionBarTitle(navActivity.resources.getString(R.string.Relearn_Procedure))
    }

}
