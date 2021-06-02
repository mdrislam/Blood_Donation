package com.mristudio.blooddonation.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.AllUserListAdapter;
import com.mristudio.blooddonation.adapter.Slider_Pager_Adapter;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    private List<UserInformation> userInformationList = new ArrayList<>();
    private RelativeLayout pagerLayout;
    private LinearLayout lytBook, lytDonation, lytDonners, lytMyList, lytUrgentRequest, lytMyRequest, lytPostRequest;
    private ViewPager view_pager;
    private LinearLayout layout_markers;
    private Timer timer;
    private Slider_Pager_Adapter slider_pager_adapter;
    int page_position = 0;
    private TextView[] dots;
    private DatabaseReference rootRer;
    private DatabaseReference sliderImageRef;
    private DatabaseReference adminRef;
    private DatabaseReference generalUserRef;
    private ChangeFragment changeFragment;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        setImageSlider();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        changeFragment= (ChangeFragment) context;
    }

    /**
     * Set Slider Image data
     */
    private void setImageSlider() {
        List<ImageSliderData> imageSliderDataList = new ArrayList<>();

        rootRer = FirebaseDatabase.getInstance().getReference();
        sliderImageRef = rootRer.child("userSliderImages");

        sliderImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ImageSliderData imageSliderData = ds.getValue(ImageSliderData.class);
                    imageSliderDataList.add(imageSliderData);
                    Log.e(TAG, "Data: " + ds.getValue(ImageSliderData.class).getCreateBy());
                }

                timer = new Timer();
                initSlider(imageSliderDataList);
                scheduleSlider(imageSliderDataList);
                Log.e(TAG, "Size: " + imageSliderDataList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });


    }

    /**
     * Initialize All HomeView
     *
     * @param view
     */
    private void initView(View view) {
        pagerLayout = view.findViewById(R.id.pagerLayout);
        view_pager = view.findViewById(R.id.view_pager);
        layout_markers = view.findViewById(R.id.layout_markers);

        rootRer = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRer.child("userAdminTable");
        generalUserRef = rootRer.child("generalUserTable");

        lytBook = view.findViewById(R.id.lytBook);
        lytDonation = view.findViewById(R.id.lytDonation);
        lytDonners = view.findViewById(R.id.lytDonners);
        lytMyList = view.findViewById(R.id.lytMyList);
        lytUrgentRequest = view.findViewById(R.id.lytUrgentRequest);
        lytMyRequest = view.findViewById(R.id.lytMyRequest);
        lytPostRequest = view.findViewById(R.id.lytPostRequest);

        lytPostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            changeFragment.callPostRequestFragment(new PostRequestFragment());
            }
        });

    }


    /**
     * init home Slider method 1
     */
    public void initSlider(final List<ImageSliderData> slidersList) {

        addBottomDots(0, slidersList);

        slider_pager_adapter = new Slider_Pager_Adapter(getActivity(), slidersList);
        view_pager.setAdapter(slider_pager_adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, slidersList);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * init home Slider method 2
     */
    public void scheduleSlider(List<ImageSliderData> imageSliderDataList) {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == imageSliderDataList.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                view_pager.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 4000);
    }

    /**
     * init home Slider method 3
     */
    public void addBottomDots(int currentPage, final List<ImageSliderData> slidersList) {
        dots = new TextView[slidersList.size()];
        Log.e(TAG, "dots " + slidersList.size());

        layout_markers.removeAllViews();
        layout_markers.setPadding(0, 0, 0, 20);
        if (dots != null) {
            for (int i = 0; i < dots.length; i++) {
                if (getActivity() != null) {
                    dots[i] = new TextView(getActivity());

                    dots[i].setText(Html.fromHtml("&#8226;"));
                    dots[i].setTextSize(35);
                    dots[i].setTextColor(getActivity().getResources().getColor(R.color.light_gray)); // un selected
                    layout_markers.addView(dots[i]);
                }
            }
        }

        if (getActivity() != null) {
            if (dots.length > 0)
                dots[currentPage].setTextColor(getActivity().getResources().getColor(R.color.red_light500)); // selected
        }
    }

public interface ChangeFragment{
        void callPostRequestFragment(PostRequestFragment postRequestFragment);
}
}