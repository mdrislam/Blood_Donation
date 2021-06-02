package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;


import com.mristudio.blooddonation.R;

import java.util.List;


public class Slider_Pager_Adapter extends PagerAdapter {
    private static final String TAG ="page Adapter" ;
    Context context;
    List<String> image_arraylist;
    private LayoutInflater layoutInflater;

    public Slider_Pager_Adapter(Context context, List<String> image_arraylist) {
        this.context = context;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_home_layout, container, false);
        ImageView im_slider = view.findViewById(R.id.im_slider);
        String path_thum = "https://nusuki.com.sa/hadaya/oms/public/uploads/slider/";

        if(image_arraylist.get(position).getSlider()!=null){

//            Glide.with(context).load(path_thum+image_arraylist.get(position).getSlider())
//                    .placeholder(R.drawable.cutlogo)
//                    .error(R.drawable.cutlogo)
//                    .into(im_slider);
//            im_slider.setScaleType(ImageView.ScaleType.FIT_XY);
            Log.e("image","image link "+path_thum+image_arraylist.get(position).getSlider());

//            Picasso.get()
//                    .load(path_thum+image_arraylist.get(position).getSlider())
//                    .placeholder(R.drawable.cutlogo)
//                    .error(R.drawable.cutlogo)
//                    .into(im_slider);
//            im_slider.setScaleType(ImageView.ScaleType.FIT_XY);
        }
       // im_slider.setImageResource(image_arraylist.get(position));

        container.addView(view);

        return view;
    }
    @Override
    public int getCount() {
        return image_arraylist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}