package com.mristudio.blooddonation.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.ChatUserListAdapter;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.view.activity.SendMessageActivity;
import com.mristudio.blooddonation.view.activity.UserSignInActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static android.content.ContentValues.TAG;


public class ContactsFragment extends Fragment {

    private RecyclerView rvChatUserList;
    private List<UserInformation> userList = new ArrayList<>();
    private ChatUserListAdapter adapter;
    private EditText searchUserEt;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        fetchUserDatafromFirebase();
        rvChatUserList = view.findViewById(R.id.rvChatUserList);
        searchUserEt = view.findViewById(R.id.searchUserEt);


        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvChatUserList.setLayoutManager(gridLayoutManager);
        adapter = new ChatUserListAdapter(getActivity(), new ChatUserListAdapter.ClickButton() {
            @Override
            public void setChatLesenner(UserInformation userInformation) {

                Intent intent = new Intent(getActivity(), SendMessageActivity.class);
                intent.putExtra("userChatId", userInformation.getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);

            }
        }, false);

        adapter.setHasStableIds(true);
        rvChatUserList.setAdapter(adapter);

        searchUserEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchUserEt(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUserEt(String search) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("generalUserTable").orderByChild("uname").startAt(search).endAt(search + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    userList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        UserInformation userInformation = snap.getValue(UserInformation.class);
                        assert userInformation != null;
                        assert currentUser != null;
                        if (!userInformation.getUserId().equals(currentUser.getUid())) {
                            userList.add(userInformation);
                        }
                    }
                    adapter.setData(userList);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchUserDatafromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("generalUserTable");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (searchUserEt.getText().toString().equals("")) {
                        if (snapshot.exists()) {
                            userList.clear();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                UserInformation userInformation = data.getValue(UserInformation.class);
                                if (userInformation.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    continue;
                                } else {
                                    userList.add(userInformation);
                                }


                            }
                            adapter.setData(userList);
                            Log.e(TAG, "Contacts Size: " + snapshot.getChildrenCount());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.getMessage());
                }
            });

        } else {
            startActivity(new Intent(getActivity(), UserSignInActivity.class));
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserDatafromFirebase();

    }


}