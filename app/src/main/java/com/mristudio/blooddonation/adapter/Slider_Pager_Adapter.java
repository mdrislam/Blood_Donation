package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;


import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Slider_Pager_Adapter extends PagerAdapter {
    private static final String TAG = "page Adapter";
    Context context;
    List<ImageSliderData> imageSliderDataList;
    private LayoutInflater layoutInflater;

    public Slider_Pager_Adapter(Context context, List<ImageSliderData> imageSliderDataList) {
        this.context = context;
        this.imageSliderDataList = imageSliderDataList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);
        ImageView im_slider = view.findViewById(R.id.sliderImageView);
        TextView sliderHeadingTV = view.findViewById(R.id.sliderHeadingTV);
        TextView createSliderByNameTV = view.findViewById(R.id.createSliderByNameTV);
        TextView slidepostingTimeTV = view.findViewById(R.id.slidepostingTimeTV);
        ImageSliderData sliderData = imageSliderDataList.get(position);

        if (sliderData != null) {



            Picasso.get()
                    .load( sliderData.getImgUrl())
                    .placeholder(R.drawable.unnamed)
                    .error(R.drawable.unnamed)
                    .into(im_slider);
            im_slider.setScaleType(ImageView.ScaleType.FIT_XY);
            sliderHeadingTV.setText(sliderData.getHeadline().toString());
            createSliderByNameTV.setText(sliderData.getCreateBy().toString());
            slidepostingTimeTV.setText(getDaysFromCompairTwoDate(sliderData.getCreateAt().toString()) + " days to go");
        }


        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imageSliderDataList.size();
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

    private long getDaysFromCompairTwoDate(String depatureDate) {
        long days = 0;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
            Date depetureDate = (Date) formatter.parse(depatureDate);

            Date today = new Date();
            days = (today.getTime() - depetureDate.getTime()) / 86400000;

        } catch (ParseException e) {
            e.printStackTrace();

        }
        return Math.abs(days);
    }
}