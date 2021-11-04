package com.mristudio.blooddonation.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.mristudio.blooddonation.adapter.RecentRequestCausesSelectedAdapter;
import com.mristudio.blooddonation.adapter.RecentRequestDemoAdapter;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class RecentRequestFragment extends Fragment {

    private RecyclerView rvCaseList, rvPostView;
    List<RequestModel> requestModelList = new ArrayList<>();

    public RecentRequestFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_request, container, false);
        rvCaseList = view.findViewById(R.id.rvCaseList);
        rvPostView = view.findViewById(R.id.rvPostView);
        provideCause();
        loadPostByCause("all");
        Log.e(TAG, "onCreateView: call");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated:  call");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadPostByCause("all");
        Log.e(TAG, "onCreate: ");
    }

    /**
     * Set Layout Three BloodGroup Data
     */
    private void provideCause() {
        List<String> causese = new ArrayList<>();
        causese.add("All");
        causese.add("Dengue");
        causese.add("Accident");
        causese.add("Cancer");
        causese.add("Thalassemia");
        causese.add("Delivery");
        causese.add("Operation");
        causese.add("Others");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvCaseList.setLayoutManager(gridLayoutManager);
        RecentRequestCausesSelectedAdapter adapter = new RecentRequestCausesSelectedAdapter(getActivity(), causese, new RecentRequestCausesSelectedAdapter.ClickButton() {
            @Override
            public void seLectedBloodGroup(String cause) {
                // Toasty.success(getActivity(), "" + groupName, Toasty.LENGTH_SHORT).show();
                loadPostByCause(cause);
            }
        });
        adapter.setHasStableIds(true);
        rvCaseList.setAdapter(adapter);


    }

    // load all 
    private void loadPostByCause(String cause) {
        Toasty.success(getActivity(), cause, Toasty.LENGTH_SHORT).show();
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPostView.setLayoutManager(gridLayoutManager);
        RecentRequestDemoAdapter adapter = new RecentRequestDemoAdapter(getActivity());
        adapter.setHasStableIds(true);
        rvPostView.setAdapter(adapter);
        if (cause.equalsIgnoreCase("all")) {
            FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST")
                    .orderByChild("caouse").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e(TAG, "onDataChange: All Causes" + snapshot.getChildrenCount());
                    requestModelList.clear();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        RequestModel model = snapshot1.getValue(RequestModel.class);
                       requestModelList.add(model);
                    }
                    adapter.setRecentRequestDemoAdapterData(requestModelList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.getMessage());
                }
            });

        } else {

            FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST")
                    .orderByChild("caouse")
                    .startAt(cause).endAt(cause).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e(TAG, "onDataChange: Other Causes" + snapshot.getChildrenCount());
                    requestModelList.clear();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        RequestModel model = snapshot1.getValue(RequestModel.class);
                       requestModelList.add(model);
                    }
                    adapter.setRecentRequestDemoAdapterData(requestModelList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.getMessage());

                }
            });
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Recent Request ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
       // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Recent Request ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }
}