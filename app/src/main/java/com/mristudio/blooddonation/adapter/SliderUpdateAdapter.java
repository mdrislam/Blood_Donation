package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderUpdateAdapter extends RecyclerView.Adapter<SliderUpdateAdapter.UpdateSliderViewHoalder> {
    private List<ImageSliderData> sliderList = new ArrayList<>();
    private Context context;
    private SliderImgesClick sliderImgesClick;


    public SliderUpdateAdapter(Context context, List<ImageSliderData> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
        sliderImgesClick = (SliderImgesClick) context;
    }

    @NonNull
    @Override
    public UpdateSliderViewHoalder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_slider_image_row, parent, false);
        return new UpdateSliderViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateSliderViewHoalder holder, int position) {

        //  holder.sliderImageView.setImageBitmap(sliderList.get(position).getImgUrl());
        Picasso.get().load(sliderList.get(position).getImgUrl()).into(holder.sliderImageView);

        holder.sliderHeadingTV.setText(sliderList.get(position).getHeadline());
        holder.createSliderByNameTV.setText(sliderList.get(position).getCreateBy());
        holder.slidepostingTimeTV.setText(sliderList.get(position).getCreateAt());
        holder.rootRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderImgesClick.clickSliderImages(sliderList.get(position));
            }
        });
        holder.deleteButtonIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderImgesClick.clickSliderImagesDelete(sliderList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return sliderList.size();
    }

    public class UpdateSliderViewHoalder extends RecyclerView.ViewHolder {
        private RelativeLayout rootRelativeLayout;
        private ImageView sliderImageView;
        private TextView sliderHeadingTV, createSliderByNameTV, slidepostingTimeTV;
        private ImageButton deleteButtonIB;

        public UpdateSliderViewHoalder(@NonNull View itemView) {
            super(itemView);
            rootRelativeLayout = itemView.findViewById(R.id.rootRelativeLayout);
            sliderImageView = itemView.findViewById(R.id.sliderImageView);
            sliderHeadingTV = itemView.findViewById(R.id.sliderHeadingTV);
            createSliderByNameTV = itemView.findViewById(R.id.createSliderByNameTV);
            slidepostingTimeTV = itemView.findViewById(R.id.slidepostingTimeTV);
            deleteButtonIB = itemView.findViewById(R.id.deleteButtonIB);

        }
    }

    /**
     * Decode Based64 String TO Image
     */
    public Bitmap getDecoded64StringFromBitmapImage(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public interface SliderImgesClick {
        void clickSliderImages(ImageSliderData imageSliderData);
        void clickSliderImagesDelete(ImageSliderData imageSliderData);
    }
}
