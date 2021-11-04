package com.mristudio.blooddonation.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.ChatUserListAdapter;
import com.mristudio.blooddonation.model.ChatList;
import com.mristudio.blooddonation.model.ChatModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.view.activity.SendMessageActivity;
import com.mristudio.blooddonation.view.activity.UserSignInActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyChatsFragment extends Fragment {
    private RecyclerView rvChatUserList;
    private List<UserInformation> muserList = new ArrayList<>();
    private List<ChatList> userList = new ArrayList<>();
    DatabaseReference reference;
    private FirebaseUser currentuser;


    private ChatUserListAdapter adapter;

    public MyChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_chats, container, false);


        rvChatUserList = view.findViewById(R.id.rvChatUserList);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();


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
        }, true);

        adapter.setHasStableIds(true);
        rvChatUserList.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(currentuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    userList.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void chatList() {
        reference = FirebaseDatabase.getInstance().getReference("generalUserTable");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    muserList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInformation userInformation = snapshot.getValue(UserInformation.class);
                        for (ChatList chatList : userList) {
                            if (userInformation.getUserId().equals(chatList.getId())) {
                                muserList.add(userInformation);
                            }
                        }
                    }
                    adapter.setData(muserList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//    private void readUserInfo() {
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("generalUserTable");
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user != null) {
//            database.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        muserList.clear();
//                        for (DataSnapshot data : snapshot.getChildren()) {
//
//                            UserInformation userInformation = data.getValue(UserInformation.class);
//                            for (String id : userList) {
//
//                                if (userInformation.getUserId().equals(id)) {
//
//                                    if (muserList.size() != 0) {
//
//                                        for (UserInformation user : muserList) {
//
//
//                                            if (!userInformation.getUserId().equals(user.getUserId())) {
//
//
//                                                muserList.add(userInformation);
//
//                                            }
//                                        }
//
//                                    } else {
//
//                                        muserList.add(userInformation);
//
//                                    }
//                                }
//                            }
//                        }
//
//
//                        adapter.setData(muserList);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.e(TAG, "onCancelled: " + error.getMessage());
//                }
//            });
//
//        } else {
//            startActivity(new Intent(getActivity(), UserSignInActivity.class));
//        }
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        setStatus("online");

    }

    @Override
    public void onPause() {
        super.onPause();
        setStatus("ofline");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setStatus(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("activeStatus", status);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("generalUserTable").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.updateChildren(hashMap);
    }


}