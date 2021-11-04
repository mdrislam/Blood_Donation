package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.MessageAdapter;
import com.mristudio.blooddonation.model.ChatModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.notification.APIService;
import com.mristudio.blooddonation.notification.Client;
import com.mristudio.blooddonation.notification.Data;
import com.mristudio.blooddonation.notification.MyResponse;
import com.mristudio.blooddonation.notification.Sender;
import com.mristudio.blooddonation.notification.Token;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SendMessageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView profileNameTv;

    private static final String TAG = "ChatingFragment";
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
    UserInformation userInformation;

    APIService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileImage = (CircleImageView) toolbar.findViewById(R.id.toolbar_ProfileImage);
        profileNameTv = (TextView) toolbar.findViewById(R.id.toolbarMenuName);
        profileImage = findViewById(R.id.toolbar_ProfileImage);
        profileNameTv = findViewById(R.id.toolbarName);
        recyclerView = findViewById(R.id.rechycler_view);
        messageEt = findViewById(R.id.messageEt);
        sendIB = findViewById(R.id.sendIB);


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        receiverId = getIntent().getStringExtra("userChatId");
        loadUserData(receiverId);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        sendIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                if (!messageEt.getText().toString().equals("")) {
                    messageSend(currentUser.getUid(), receiverId, messageEt.getText().toString().trim());
                } else {
                    Toasty.warning(SendMessageActivity.this, "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();
                }
            }
        });


        seenMessage(getIntent().getStringExtra("userChatId"));


    }

    private void loadUserData(String receiverId) {
        Log.e(TAG, "chatProfile: " + receiverId);
        FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInformation = snapshot.getValue(UserInformation.class);
                if (userInformation != null) {
                    profileNameTv.setText(userInformation.getName());
                    Picasso.get().load(userInformation.getUserProfilePicture()).into(profileImage);
                    //Read Message
                    readMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), getIntent().getStringExtra("userChatId"), userInformation.getUserProfilePicture());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Toast.makeText(RecentRequestActivity.this, ""+tblId, Toast.LENGTH_SHORT).show();
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


        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("generalUserTable").child(currentUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                UserInformation userInformation = snapshot.getValue(UserInformation.class);
                if (notify) {
                    senNotification(receiver, userInformation.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setCurrentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
        editor.putString("currentuser",userid);
        editor.apply();


    }
    //Send Notify Specypic User
    private void senNotification(String receiver, String name, String msg) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(currentUser.getUid(), R.mipmap.ic_launcher, name + ":" + msg, "New Message",
                            getIntent().getStringExtra("userChatId"));

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    Toasty.error(SendMessageActivity.this, "Faild", Toasty.LENGTH_SHORT).show();
                                    Log.e(TAG, "onResponse: "+response.message() );
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: "+t.getMessage() );
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    adapter = new MessageAdapter(SendMessageActivity.this, chatModelList, imageUrl);
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


    @Override
    public void onResume() {
        super.onResume();
//        profileNameTv.setText(getIntent().getStringExtra("name"));
//        Picasso.get().load(getIntent().getStringExtra("pic")).into(profileImage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatus("active");
        setCurrentUser(getIntent().getStringExtra("userChatId"));
    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatus("ofline");
       // setCurrentUser(getIntent().getStringExtra("userChatId"));
        setCurrentUser("none");
        reference.removeEventListener(seenLesenner);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        finish();
        return true;
    }
}