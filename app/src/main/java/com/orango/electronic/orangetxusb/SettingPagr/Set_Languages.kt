package com.orango.electronic.orangetxusb.SettingPagr


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.orango.electronic.orangetxusb.R
import android.widget.Spinner
import android.widget.Toast
import com.orango.electronic.orangetxusb.mainActivity.HomeFragment
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import com.orango.electronic.orangetxusb.tool.FileDowload
import com.orango.electronic.orangetxusb.tool.LanguageUtil
import kotlinx.android.synthetic.main.fragment_set__languages.view.*
import java.util.ArrayList


class Set_Languages : Fragment() {
    lateinit var AreaSpinner: Spinner
    lateinit var LanguagesSpinner: Spinner
    lateinit var profilePreferences: SharedPreferences
    lateinit var profileEditor: SharedPreferences.Editor
    var Arealist = ArrayList<String>()
    var LanguageList = ArrayList<String>()
    lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_set__languages, container, false)
        profilePreferences = activity!!.getSharedPreferences("Setting", Context.MODE_PRIVATE)
        AreaSpinner = rootView.findViewById(R.id.spinner2)
        LanguagesSpinner = rootView.findViewById(R.id.spinner4)
        Arealist.add("Select")
        Arealist.add("EU")
        Arealist.add("North America")
        Arealist.add("台灣")
        Arealist.add("中國大陸")
        val arrayAdapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, Arealist)
        AreaSpinner.adapter = arrayAdapter
        LanguageList.add("Select")
        LanguageList.add("繁體中文")
        LanguageList.add("简体中文")
        LanguageList.add("Deutsch")
        LanguageList.add("English")
        LanguageList.add("Italiano")
        LanguageList.add("dansk")
        val lanAdapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, LanguageList)
        LanguagesSpinner.adapter = lanAdapter

        rootView.next2.setOnClickListener {
            if (LanguagesSpinner.selectedItem.toString().equals("Select") || AreaSpinner.selectedItem.toString().equals(
                    "Select"
                )
            ) {
                return@setOnClickListener
            }
            profileEditor = profilePreferences.edit()
            profileEditor.putString("Area", AreaSpinner.selectedItem.toString())
            profileEditor.putString("Language", LanguagesSpinner.selectedItem.toString())
            profileEditor.commit()
            Log.d("Language", LanguagesSpinner.selectedItem.toString())
            when (LanguagesSpinner.selectedItem.toString()) {
                "繁體中文" -> {
                    LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_TAIWAIN);
                }
                "简体中文" -> {
                    LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_CHINESE);
                }
                "Deutsch" -> {
                    LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_DE);
                }
                "English" -> {
                    LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_ENGLISH);
                }
                "Italiano" -> {
                    LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_ITALIANO);
                }
                "dansk" -> {
                    LanguageUtil.updateLocale(activity, LanguageUtil.LOCALE_DANSK);
                }
            }
            (activity as NavigationActivity).loadingMMy()
            fragmentManager!!.popBackStack(null, 1)
        }
        return rootView
    }
var handler= Handler()
    override fun onResume() {
        super.onResume()
        (activity as NavigationActivity).setActionBarTitle(activity!!.resources.getString(R.string.AreaLanguage))
        (activity as NavigationActivity).back.visibility = View.VISIBLE
        (activity as NavigationActivity).RightTop.visibility = View.GONE
    }
}
