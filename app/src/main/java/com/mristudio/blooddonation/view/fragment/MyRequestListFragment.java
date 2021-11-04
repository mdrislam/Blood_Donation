package com.mristudio.blooddonation.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.firebase.MyBloodRequestAdapter;
import com.mristudio.blooddonation.model.PostRequestModel;

import static android.content.ContentValues.TAG;


public class MyRequestListFragment extends Fragment {

    private MyBloodRequestAdapter adapter;
    private RecyclerView rVMyRequest;
    public MyRequestListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_request_list, container, false);


        rVMyRequest = view.findViewById(R.id.rVMyRequest);
        rVMyRequest.setLayoutManager(new LinearLayoutManager(getActivity()));

        String uId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseRecyclerOptions<PostRequestModel> options =
                new FirebaseRecyclerOptions.Builder<PostRequestModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("BlOOD_REQUEST_FOR_ALL")
                                .orderByChild(uId).equalTo(uId), PostRequestModel.class)
                        .build();
        adapter = new MyBloodRequestAdapter(options);

        Toast.makeText(getActivity(), "" + options.getSnapshots().size(), Toast.LENGTH_SHORT).show();

        Log.e(TAG, "onCreateView: " + options.getSnapshots().size());
        rVMyRequest.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
