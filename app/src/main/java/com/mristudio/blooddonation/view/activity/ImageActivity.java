package com.mristudio.blooddonation.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mristudio.blooddonation.R;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    private ImageButton closeImageButton;
    private ImageView imageView_image;
    private TouchImageView touchImage;
    String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        closeImageButton = findViewById(R.id.closeImageButton);
        imageView_image = findViewById(R.id.ImageView_image);
        touchImage = findViewById(R.id.touchImage);
        imageurl = getIntent().getExtras().getString("imageurl");
        if(imageurl!=null){
            Picasso.get().load(imageurl).into(touchImage);
        }


        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        finish();
    }
}