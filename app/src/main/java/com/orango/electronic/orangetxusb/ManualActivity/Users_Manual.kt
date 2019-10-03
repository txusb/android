package com.orango.electronic.orangetxusb.ManualActivity


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import kotlinx.android.synthetic.main.fragment_users__manual.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Users_Manual : Fragment() {
lateinit var RootView:View
    lateinit var act: NavigationActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        RootView=inflater.inflate(R.layout.fragment_users__manual, container, false)
        act=activity as NavigationActivity
       act.RightTop.visibility=View.GONE
        act.back.visibility=View.VISIBLE
        RootView.button7.setOnClickListener {
            ManualDetail.position=0
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                ManualDetail(), "ManualDetail")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("ManualDetail")
                // 提交事務
                .commit()
        }
        RootView.button8.setOnClickListener {
            ManualDetail.position=1
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                ManualDetail(), "ManualDetail")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("ManualDetail")
                // 提交事務
                .commit()
        }
        RootView.button9.setOnClickListener {
            ManualDetail.position=2
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                ManualDetail(), "ManualDetail")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("ManualDetail")
                // 提交事務
                .commit()
        }
        RootView.button10.setOnClickListener {
            ManualDetail.position=3
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                ManualDetail(), "ManualDetail")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("ManualDetail")
                // 提交事務
                .commit()
        }
        return RootView
    }
    override fun onResume() {
        super.onResume()
        (activity as NavigationActivity).setActionBarTitle(act.resources.getString(R.string.Users_manual))}

}
