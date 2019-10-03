package com.orango.electronic.orangetxusb.tool;

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

public class ComPressImage {
    public static void load(Context context,final Uri uri, final SimpleDraweeView draweeView, final int width, final int height) {

        Glide.with(context).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {

            @Override

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

                int we = bitmap.getWidth();

                int he = bitmap.getHeight();

                LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(width,height);

                draweeView.setLayoutParams(a);

                ImageRequest request =

                        ImageRequestBuilder.newBuilderWithSource(uri)

                                .setResizeOptions(new ResizeOptions(width,height))

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
}
