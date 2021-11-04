package com.mristudio.blooddonation.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.HomeDemoAdapter;
import com.mristudio.blooddonation.adapter.Slider_Pager_Adapter;
import com.mristudio.blooddonation.adapter.firebase.FindRequestAdapter;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.view.activity.PostDetailsActivity;
import com.mristudio.blooddonation.view.activity.FindDonnerActivity;
import com.mristudio.blooddonation.view.activity.MainActivity;
import com.mristudio.blooddonation.view.activity.MyRequestActivity;
import com.mristudio.blooddonation.view.activity.RecentRequestActivity;
import com.mristudio.blooddonation.view.activity.TopDonnarActivity;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private List<UserInformation> userInformationList = new ArrayList<>();
    private RelativeLayout pagerLayout, rlFindDonnerbutton, rlTopDonner, rlRecentRequest, rlMyRequest;
    private ViewPager view_pager;
    private Timer timer;
    private Slider_Pager_Adapter slider_pager_adapter;
    int page_position = 0;
    private TextView[] dots;
    private DatabaseReference rootRer;
    private DatabaseReference sliderImageRef;
    private DatabaseReference adminRef;
    private DatabaseReference generalUserRef;
    private RecyclerView rVRequestPost;
    private FindRequestAdapter adapter;
    private List<RequestModel> postRequests = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        setImageSlider();
        setHasOptionsMenu(true);
        return view;
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
                }

                timer = new Timer();
                initSlider(imageSliderDataList);
                scheduleSlider(imageSliderDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {


        super.onPrepareOptionsMenu(menu);
    }

    /**
     * Initialize All HomeView
     *
     * @param view
     */
    private void initView(View view) {
        pagerLayout = view.findViewById(R.id.pagerLayout);
        view_pager = view.findViewById(R.id.view_pager);

        rootRer = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRer.child("userAdminTable");
        generalUserRef = rootRer.child("generalUserTable");
        rVRequestPost = view.findViewById(R.id.rVRequestPost);
//
        rlFindDonnerbutton = view.findViewById(R.id.rlFindDonnerbutton);
        rlTopDonner = view.findViewById(R.id.rlTopDonner);
        rlRecentRequest = view.findViewById(R.id.rlRecentRequest);
        rlMyRequest = view.findViewById(R.id.rlMyRequest);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        HomeDemoAdapter adapter = new HomeDemoAdapter(getActivity(), postRequests, new HomeDemoAdapter.HomePostClickLesenner() {
            @Override
            public void homePostClick(String tblName) {

                Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
                intent.putExtra("userPostId", tblName);
                getActivity().startActivity(intent);
                // getActivity().overridePendingTransition( R.anim.left_in, R.anim.left_out);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

            }
        });

        rVRequestPost.setLayoutManager(llm);
        rVRequestPost.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    postRequests.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RequestModel model = dataSnapshot.getValue(RequestModel.class);

                        DataSnapshot lovesSnapshot = dataSnapshot.child("LOVES");
                        DataSnapshot viewsSnapshot = dataSnapshot.child("VIEWS");

                        if (lovesSnapshot.exists()) {

                            List<String> loveslist = new ArrayList<>();
                            for (DataSnapshot love : lovesSnapshot.getChildren()) {

                                loveslist.add(love.getValue(String.class));
                            }
                            model.setLovesList(loveslist);
                        } else {


                        }
                        if (viewsSnapshot.exists()) {

                            List<String> viewsList = new ArrayList<>();
                            for (DataSnapshot love : lovesSnapshot.getChildren()) {

                                viewsList.add(love.getValue(String.class));
                            }
                            model.setViewsList(viewsList);
                        } else {
                            Log.e(TAG, "onDataChange: Views not found ! ");

                        }
                        postRequests.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });

        rlFindDonnerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FindDonnerActivity.class);
                getActivity().startActivity(intent);
                // getActivity().overridePendingTransition( R.anim.left_in, R.anim.left_out);

                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

            }
        });
        rlTopDonner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), TopDonnarActivity.class);
                getActivity().startActivity(intent);
                // getActivity().overridePendingTransition( R.anim.left_in, R.anim.left_out);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

            }
        });

        rlRecentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RecentRequestActivity.class);
                getActivity().startActivity(intent);
                // getActivity().overridePendingTransition( R.anim.left_in, R.anim.left_out);

                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

            }
        });
        rlMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), MyRequestActivity.class);
                getActivity().startActivity(intent);
                // getActivity().overridePendingTransition( R.anim.left_in, R.anim.left_out);

                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

            }
        });
    }

    /**
     * init home Slider method 1
     */
    public void initSlider(final List<ImageSliderData> slidersList) {

        slider_pager_adapter = new Slider_Pager_Adapter(getActivity(), slidersList);
        view_pager.setAdapter(slider_pager_adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

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

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.VISIBLE);

        TextView tittleTv = MainActivity.toolbar.findViewById(R.id.toolbar_Hompage_titleTV);
        tittleTv.setText("LPI BLOOD BANK");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        // MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.VISIBLE);

        TextView tittleTv = MainActivity.toolbar.findViewById(R.id.toolbar_Hompage_titleTV);
        tittleTv.setText("LPI BLOOD BANK");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        //  MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }


}