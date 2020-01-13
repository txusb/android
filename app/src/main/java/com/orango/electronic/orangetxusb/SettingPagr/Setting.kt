package com.orango.electronic.orangetxusb.SettingPagr


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import kotlinx.android.synthetic.main.fragment_setting.view.*

class Setting : Fragment() {
lateinit var rootView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_setting, container, false)
        rootView.area.setOnClickListener{
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                Set_Languages(), "SetArea")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("SetArea")
                // 提交事務
                .commit()
        }
        rootView.policy.setOnClickListener{
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                PrivaryPolicy(), "policy")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("policy")
                // 提交事務
                .commit()
        }
        rootView.version.setOnClickListener{
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment,
                Update(), "Update")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .addToBackStack("Update")
                // 提交事務
                .commit()
        }
        return rootView
    }
    override fun onResume() {
        super.onResume()
        (activity as NavigationActivity).setActionBarTitle(activity!!.resources.getString(R.string.Users_manual))
        (activity as NavigationActivity).back.visibility=View.VISIBLE
        (activity as NavigationActivity).RightTop.visibility=View.GONE
    }

}
