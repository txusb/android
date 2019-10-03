package com.orango.electronic.orangetxusb.ManualActivity


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.orango.electronic.orangetxusb.R
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.widget.LinearLayout
import com.orango.electronic.orangetxusb.ManualActivity.Idcopy_USB.*
import com.orango.electronic.orangetxusb.ManualActivity.PAD_PROGRAM.*
import com.orango.electronic.orangetxusb.ManualActivity.PadCopy.*
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU1
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU8
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU7
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU6
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU5
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU4
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU3
import com.orango.electronic.orangetxusb.ManualActivity.Program_USB.PU2
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity
import kotlinx.android.synthetic.main.fragment_manual_detail.view.*
import java.util.*
import android.support.v4.view.ViewPager.OnPageChangeListener as OnPageChangeListener1


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ManualDetail : Fragment() {
    companion object {
        var position=0
    }
lateinit var rootView:View
   lateinit var fragments: ArrayList<Fragment>
    lateinit var ImageViews: ArrayList<ImageView>
    lateinit var li:LinearLayout
    lateinit var act:NavigationActivity
    lateinit var viewpager:ViewPager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_manual_detail, container, false)
        viewpager=rootView.findViewById(R.id.viewpager)
        li=rootView.findViewById(R.id.li)
        fragments=ArrayList<Fragment>()
        ImageViews=ArrayList<ImageView>()
        act=activity!! as NavigationActivity
        rootView.button11.setOnClickListener {
            act.onBackPressed()
        }
        when(position){
            0->{
                fragments.add(PU1())
                fragments.add(PU2())
                fragments.add(PU3())
                fragments.add(PU4())
                fragments.add(PU5())
                fragments.add(PU6())
                fragments.add(PU7())
                fragments.add(PU8())
            }
            1->{
                fragments.add(UID1())
                fragments.add(UID2())
                fragments.add(UID3())
                fragments.add(UID4())
                fragments.add(UID5())
                fragments.add(UID6())
                fragments.add(UID7())
                fragments.add(UID8())
                fragments.add(UID9())
            }
            2->{
                fragments.add(Pad1())
                fragments.add(Pad2())
                fragments.add(Pad3())
                fragments.add(Pad4())
                fragments.add(Pad5())
                fragments.add(Pad6())
                fragments.add(Pad7())
                fragments.add(Pad8())
                fragments.add(Pad9())
            }
            3->{
                fragments.add(PC1())
                fragments.add(PC2())
                fragments.add(PC3())
                fragments.add(PC4())
                fragments.add(PC5())
                fragments.add(PC6())
                fragments.add(PC7())
                fragments.add(PC8())
                fragments.add(PC9())
                fragments.add(PC10())
                fragments.add(PC11())
            }
        }
        for(i in 0 until fragments.size){
            var a= ImageView(activity)
            a.layoutParams= ViewGroup.LayoutParams(40,40)
            if(i==0){
                a.setImageDrawable(activity!!.getDrawable(R.drawable.circlespot))
            }else{   a.setImageDrawable(activity!!.getDrawable(R.drawable.circlespot2))}
            a.setPadding(10,10,10,10)
            li.addView(a)
            ImageViews.add(a)
        }
        viewpager.adapter=MyPagerAdapter(fragmentManager!!)
        viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                ImageViews.get(position).setImageDrawable(activity!!.getDrawable(R.drawable.circlespot))
           if(position-1>=0){ImageViews.get(position-1).setImageDrawable(activity!!.getDrawable(R.drawable.circlespot2))}
                if(position+1<ImageViews.size){  ImageViews.get(position+1).setImageDrawable(activity!!.getDrawable(R.drawable.circlespot2))}
            }
        })

            return rootView
    }
    private inner class MyPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            return fragments[i]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
    override fun onResume() {
        super.onResume()
        when(position){
            0->{ (activity as NavigationActivity).setActionBarTitle("Program")}
            1->{ (activity as NavigationActivity).setActionBarTitle("ID COPY")}
            2->{ (activity as NavigationActivity).setActionBarTitle("Program")}
            3->{ (activity as NavigationActivity).setActionBarTitle("ID COPY")}
        }
       }
}
