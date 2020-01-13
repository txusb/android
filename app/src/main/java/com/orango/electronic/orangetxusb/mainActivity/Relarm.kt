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
import com.orango.electronic.orangetxusb.UsbCable.Id_copy
import com.orango.electronic.orangetxusb.UsbPad.Pad_Idcopy
import com.orango.electronic.orangetxusb.UsbPad.StartProgram
import kotlinx.android.synthetic.main.fragment_relarm.view.*
import kotlinx.android.synthetic.main.fragment_mmy.view.mmy_text as mmy_text1

class Relarm : Fragment() {
    companion object {
        var position = 0
    }

    lateinit var rootView: View
    lateinit var text: TextView
    lateinit var make: String
    lateinit var makeImg: String
    lateinit var model: String
    lateinit var year: String
    lateinit var navActivity: NavigationActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_relarm, container, false)
        navActivity = activity as NavigationActivity
        retainInstance = true
        navActivity.back.visibility = View.VISIBLE
        if (position == 0) {
            rootView.menu3.text = resources.getString(R.string.MENU)
        } else {
            rootView.menu3.text = resources.getString(R.string.Next)
        }
        rootView.menu3.setOnClickListener {
            if (position == 0) {
                val fragment = HomeFragment()
                val transaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, fragment, "HomeFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                    .addToBackStack("Program Sensor")
                    // 提交事務
                    .commit()
            } else {
                when (NavigationActivity.Action) {
                    "PROGRAM" -> {
                        if (NavigationActivity.PAD_OR_USB.equals("USB")) {
                            val args = Bundle()
                            args.putString(Cable_Program.stringMake, make)
                            args.putString(Cable_Program.stringMakeImg, makeImg)
                            args.putString(Cable_Program.stringModel, model)
                            args.putString(Cable_Program.stringYear, year)
                            val fragment = Cable_Program()
                            fragment.arguments = args
                            val transaction = fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.nav_host_fragment, fragment, "Program")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                .addToBackStack("Program")
                                // 提交事務
                                .commit()
                        } else {
                            val args = Bundle()
                            args.putString(Cable_Program.stringMake, make)
                            args.putString(Cable_Program.stringMakeImg, makeImg)
                            args.putString(Cable_Program.stringModel, model)
                            args.putString(Cable_Program.stringYear, year)
                            args.putString(Cable_Program.LFID, "")
                            args.putString(Cable_Program.LRID, "")
                            args.putString(Cable_Program.RRID, "")
                            args.putString(Cable_Program.RFID, "")
                            val fragment = StartProgram()
                            fragment.arguments = args
                            val transaction = fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.nav_host_fragment, fragment, "Pad_Program")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                .addToBackStack("Pad_Program")
                                // 提交事務
                                .commit()
                        }
                    }
                    "IDCOPY" -> {
                        if (NavigationActivity.PAD_OR_USB.equals("USB")) {
                            val args = Bundle()
                            args.putString(Cable_Program.stringMake, make)
                            args.putString(Cable_Program.stringMakeImg, makeImg)
                            args.putString(Cable_Program.stringModel, model)
                            args.putString(Cable_Program.stringYear, year)
                            val fragment = Id_copy()
                            fragment.arguments = args
                            val transaction = fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.nav_host_fragment, fragment, "Id_Copy")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                .addToBackStack("Id_Copy")
                                // 提交事務
                                .commit()
                        } else {
                            val args = Bundle()
                            args.putString(Cable_Program.stringMake, make)
                            args.putString(Cable_Program.stringMakeImg, makeImg)
                            args.putString(Cable_Program.stringModel, model)
                            args.putString(Cable_Program.stringYear, year)
                            val fragment = Pad_Idcopy()
                            fragment.arguments = args
                            val transaction = fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.nav_host_fragment, fragment, "Id_Copy")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                                .addToBackStack("Id_Copy")
                                // 提交事務
                                .commit()
                        }
                    }
                }
            }

        }
        rootView.mmy_text.text = "$make/$model /$year"
        val s19 = navActivity.itemDAO.getMMY(make, model, year)
        rootView.textView10.text =
            "OE Part # :\n${navActivity.itemDAO.getOePart(s19)}\n\nFor OrangeSensor:\n${navActivity.itemDAO.SencsorModel(
                s19
            )}\n\nRelearn:\n${navActivity.itemDAO.GetreLarm(make, model, year, navActivity)}"
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
