package com.orango.electronic.orangetxusb.SettingPagr


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.orango.electronic.orangetxusb.R
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class PrivaryPolicy : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privary_policy, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as NavigationActivity).setActionBarTitle(activity!!.resources.getString(R.string.Privacy_Policy))
        (activity as NavigationActivity).back.visibility=View.VISIBLE
        (activity as NavigationActivity).RightTop.visibility=View.GONE
    }

}
