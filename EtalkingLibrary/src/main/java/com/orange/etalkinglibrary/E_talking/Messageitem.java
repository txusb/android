package com.orange.etalkinglibrary.E_talking;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orange.etalkinglibrary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Messageitem {
    public ArrayList<String> id=new ArrayList<>();
    public ArrayList<String> admin=new ArrayList<>();
    public ArrayList<String> message=new ArrayList<>();
    public ArrayList<String> time=new ArrayList<>();
    public ArrayList<String> head=new ArrayList<>();
    public ArrayList<String> pick=new ArrayList<>();
    public ArrayList<String> file=new ArrayList<>();
    public ArrayList<String> reader=new ArrayList<>();
    public boolean button=false;
    public boolean success=false;
    public void add(String id,String admin,String message,String time,String head,String pick,String reader){
        this.id.add(id);
        this.admin.add(admin);
        this.message.add(message);
        this.time.add(time);
        this.head.add(head);
        this.pick.add(pick);
        this.reader.add(reader);
    }
    public void add2(String id,String admin,String file,String message,String time,String head,String pick){
        this.id.add(id);
        this.admin.add(admin);
        this.message.add(message);
        this.time.add(time);
        this.head.add(head);
        this.pick.add(pick);
        this.file.add(file);
    }
    public void addzero(String id,String admin,String file,String message,String time,String head,String pick){
        this.id.add(0,id);
        this.admin.add(0,admin);
        this.message.add(0,message);
        this.time.add(0,time);
        this.head.add(0,head);
        this.pick.add(0,pick);
        this.file.add(0,file);
    }
    public static String CalculateTime(String time,Context context) {
        long nowTime = System.currentTimeMillis(); // 获取当前时间的毫秒数
        String msg = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 指定时间格式
        Date setTime = null; // 指定时间
        try {
            setTime = sdf.parse(time); // 将字符串转换为指定的时间格式
        } catch (ParseException e) {

            e.printStackTrace();
        }
        long reset = setTime.getTime(); // 获取指定时间的毫秒数
        long dateDiff = nowTime - reset;
        if (dateDiff < 0) {
            msg = context.getResources().getString(R.string.secago);
        } else {
            long dateTemp1 = dateDiff / 1000; // 秒
            long dateTemp2 = dateTemp1 / 60; // 分钟
            long dateTemp3 = dateTemp2 / 60; // 小时
            long dateTemp4 = dateTemp3 / 24; // 天数
            long dateTemp5 = dateTemp4 / 30; // 月数
            long dateTemp6 = dateTemp5 / 12; // 年数
            if (dateTemp6 > 0) {
                msg = context.getResources().getString(R.string.yeargo).replace("1", ""+dateTemp6);
            } else if (dateTemp5 > 0) {
                msg = context.getResources().getString(R.string.monthago).replace("1", ""+dateTemp5);
            } else if (dateTemp4 > 0) {
                msg = context.getResources().getString(R.string.dayago).replace("1", ""+dateTemp4);
            } else if (dateTemp3 > 0) {
                msg = context.getResources().getString(R.string.hourago).replace("1", ""+dateTemp3);;
            } else if (dateTemp2 > 0) {
                msg = context.getResources().getString(R.string.minuteago).replace("1", ""+dateTemp2);
            } else if (dateTemp1 > 0) {
                msg = context.getResources().getString(R.string.secago);
            }
        }
        return msg;
    }
    public static void load(final String uri, final SimpleDraweeView draweeView, final int width, final int height, Context context) {
        Glide.with(context).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int we = bitmap.getWidth();
                int he = bitmap.getHeight();
                LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(width, (int) ((float) (width * he) / (float) we));
                draweeView.setLayoutParams(a);
                ImageRequest request =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                                .setResizeOptions(new ResizeOptions(width, (int) ((float) (width * he) / (float) we)))
                                //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                                // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                                .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                                .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                                .build();
                PipelineDraweeController controller =
                        (PipelineDraweeController) Fresco.newDraweeControllerBuilder()

                                .setImageRequest(request)

                                .setOldController(draweeView.getController())

                                .setAutoPlayAnimations(true) //自动播放gif动画

                                .build();


                draweeView.setController(controller);
            }
        });

    }
    public static void loadResPic(Context context, SimpleDraweeView simpleDraweeView, int id) {
        Uri uri = Uri.parse("res://" +
                context.getPackageName() +
                "/" + id);
        simpleDraweeView.setImageURI(uri);
    }
}
