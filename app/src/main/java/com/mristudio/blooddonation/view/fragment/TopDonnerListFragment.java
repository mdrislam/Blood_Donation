package com.mristudio.blooddonation.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.TopDonnerDemoAdapter;
import com.mristudio.blooddonation.model.RatingModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class TopDonnerListFragment extends Fragment {

    private static final String TAG = "TopDonnerListFragment";
    RecyclerView rvTopDonner;
    private List<UserInformation> userInformationList = new ArrayList<>();

    public TopDonnerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_donner_list, container, false);

        rvTopDonner = view.findViewById(R.id.rvTopDonner);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        TopDonnerDemoAdapter adapter = new TopDonnerDemoAdapter(getActivity(), userInformationList, new TopDonnerDemoAdapter.TopDonnerClickLesenner() {
            @Override
            public void donnerPostClick(String userId) {
//                Bundle bundle = new Bundle();
//                bundle.putString("uId", userId); // Put anything what you want
//                DonnerProfileFragment donnerProfileFragment = new DonnerProfileFragment();
//                donnerProfileFragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
//                        .replace(R.id.fragment_container, donnerProfileFragment).addToBackStack(null).commit();

            }
        });
        rvTopDonner.setLayoutManager(llm);


        FirebaseDatabase.getInstance().getReference().child("generalUserTable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    userInformationList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);

                        DataSnapshot ratingSnapshort = dataSnapshot.child("RATINGS");
                        if (ratingSnapshort.exists()) {
                            List<RatingModel> ratingModelList = new ArrayList<>();

                            for (DataSnapshot rating : ratingSnapshort.getChildren()) {
                                RatingModel ratingModel = rating.getValue(RatingModel.class);
                                ratingModelList.add(ratingModel);
                            }

                            userInformation.setRatingModelList(ratingModelList);

                        } else {
                            Log.e(TAG, "onDataChange: Rating Not found ");
                        }

                        userInformationList.add(userInformation);
                    }
                    rvTopDonner.setAdapter(adapter);

                } else {
                    Log.e(TAG, "onDataChange: ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });


//        HomeDemoAdapter adaptdder = new HomeDemoAdapter(getActivity(), new HomeDemoAdapter.HomePostClickLesenner() {
//            @Override
//            public void homePostClick(int position) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindDonnerDetailsFragment()).addToBackStack(null).commit();
//
//            }
//        });


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Top Donners ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
      //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Top Donners ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }
}