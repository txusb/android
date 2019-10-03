package com.orango.electronic.orangetxusb.Adapter;



import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orango.electronic.orangetxusb.R;
import com.orango.electronic.orangetxusb.mainActivity.ModelFragment;
import com.orango.electronic.orangetxusb.mainActivity.NavigationActivity;
import com.orango.electronic.orangetxusb.mmySql.Item;
import com.orango.electronic.orangetxusb.tool.MyApp;


import java.util.ArrayList;

import static com.orango.electronic.orangetxusb.tool.ComPressImage.load;


public class ShowItemImage extends RecyclerView.Adapter<ShowItemImage.ViewHolder> {
    public FragmentManager transaction;
    public ArrayList<Item> makes ;
    public NavigationActivity navigationActivity;
    public ShowItemImage(NavigationActivity navigationActivity,ArrayList<Item> makes,FragmentManager transaction) {
this.navigationActivity=navigationActivity;
this.makes=makes;
this.transaction=transaction;
    }



    // 建立ViewHolder

    class ViewHolder extends RecyclerView.ViewHolder{
public final SimpleDraweeView simpleDraweeView;
        ViewHolder(final View itemView) {
            super(itemView);
simpleDraweeView=itemView.findViewById(R.id.make_item);
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putString(ModelFragment.stringMake, makes.get(getAdapterPosition()).getMake());
                    args.putString(ModelFragment.stringMakeImg, makes.get(getAdapterPosition()).getMakeImg());
                    ModelFragment fragment = new ModelFragment();
                    fragment.setArguments(args);
                    transaction.beginTransaction().
                            replace(R.id.nav_host_fragment, fragment, "Select Model")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                            .addToBackStack("Select Model")
                            // 提交事務
                            .commit();
                }
            });

        }







    }





    @Override

    public ShowItemImage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.make_grid_item, parent, false);
        return new ShowItemImage.ViewHolder(view);

    }



    boolean a=true;

    @Override

    public void onBindViewHolder(final ShowItemImage.ViewHolder holder, final int position) {
        int mipmapId=holder.itemView.getContext().getResources().getIdentifier(makes.get(position).getMakeImg(),"mipmap",holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(mipmapId)
   .centerCrop().into(holder.simpleDraweeView);

    }





//    }





    @Override

    public int getItemCount() {

        return makes.size();

    }

    private Handler handler=new Handler(){

        @Override

        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            switch (msg.what){



            }

        }

    };

}