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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.MessageAdapter;
import com.mristudio.blooddonation.model.ChatModel;
import com.mristudio.blooddonation.view.activity.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


public class ChatingFragment extends Fragment {


    private static final String TAG = "ChatingFragment";
    private Bundle bundle;
    private RecyclerView recyclerView;
    private EditText messageEt;
    private ImageButton sendIB;
    private FirebaseUser currentUser;
    private String receiverId;
    MessageAdapter adapter;
    List<ChatModel> chatModelList;
    LinearLayoutManager linearLayoutManager;
    ValueEventListener seenLesenner;
    DatabaseReference reference;

    public ChatingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chating, container, false);

        recyclerView = view.findViewById(R.id.rechycler_view);


        messageEt = view.findViewById(R.id.messageEt);
        sendIB = view.findViewById(R.id.sendIB);
        bundle = this.getArguments();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        receiverId = bundle.getString("userChatId");

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        sendIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageEt.getText().toString().equals("")) {
                    messageSend(currentUser.getUid(), receiverId, messageEt.getText().toString().trim());
                } else {
                    Toasty.warning(getContext(), "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        readMessage(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), bundle.getString("userChatId"), bundle.getString("pic"));
        seenMessage(bundle.getString("userChatId"));

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toolbar_profileLyt.setVisibility(View.VISIBLE);
        CircleImageView profileImage = (CircleImageView) MainActivity.toolbar_profileLyt.findViewById(R.id.toolbar_ProfileImage);
        TextView profileName = (TextView) MainActivity.toolbar_profileLyt.findViewById(R.id.toolbarMenuName);
        profileName.setText(bundle.getString("name"));
        Picasso.get().load(bundle.getString("pic")).into(profileImage);

        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        MainActivity.bottomNavigationView.setVisibility(View.GONE);
        setStatus("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.toolbar_profileLyt.setVisibility(View.VISIBLE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // MainActivity.bottomNavigationView.setVisibility(View.GONE);
        setStatus("ofline");
        reference.removeEventListener(seenLesenner);
    }

    //Message Send By Spacipic User
    private void messageSend(String sender, String receiver, String message) {
        messageEt.setText("");
        reference = FirebaseDatabase.getInstance().getReference();
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("sender", sender);
//        hashMap.put("receier", receiver);
//        hashMap.put("message", message);
        ChatModel chatModel = new ChatModel(sender, receiver, message, false);
        reference.child("Chats")
                .push()
                .setValue(chatModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override

                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   Log.e(TAG, "onComplete: " + task.isSuccessful());

                                               }
                                           }
                                       }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }
        });

    }

    private void readMessage(String myId, String userId, String imageUrl) {
        chatModelList = new ArrayList<>();
        Log.e(TAG, "readMessage: My Id: " + myId + " UserId: " + userId + " imageUrl: " + imageUrl);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    chatModelList.clear();
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        ChatModel chatModel = snapshot.getValue(ChatModel.class);

                        if (chatModel.getReceiver().equals(myId) && chatModel.getSender().equals(userId)
                                || chatModel.getSender().equals(myId) && chatModel.getReceiver().equals(userId)) {
                            chatModelList.add(chatModel);
                            Log.e(TAG, " Data Model: " + chatModel.toString());
                        }
                    }
                    adapter = new MessageAdapter(getActivity(), chatModelList, imageUrl);
                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUser.getUid()).child(userId);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setStatus(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("activeStatus", status);
        reference = FirebaseDatabase.getInstance().getReference("generalUserTable").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.updateChildren(hashMap);
    }

    private void seenMessage(final String userId) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        seenLesenner = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        ChatModel chatModel = snap.getValue(ChatModel.class);
                        if (chatModel.getReceiver().equals(currentUser.getUid()) && chatModel.getSender().equals(userId)) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen", true);
                            snap.getRef().updateChildren(hashMap);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}