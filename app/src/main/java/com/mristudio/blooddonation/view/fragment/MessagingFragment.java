package com.mristudio.blooddonation.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.MessageViewPagerAdapter;
import com.mristudio.blooddonation.adapter.ViewPagerAdapter;
import com.mristudio.blooddonation.model.ChatModel;
import com.mristudio.blooddonation.notification.Token;
import com.mristudio.blooddonation.view.activity.MainActivity;

import static android.content.ContentValues.TAG;

public class MessagingFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
//    private static MessageViewPagerAdapter adapter;
    FirebaseUser currentuser;
    DatabaseReference reference;
    FragmentManager childFragment;
    ViewPagerAdapter adapter;


    public MessagingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);
//        MainActivity.toolbar_Hompage_titleTV.setText("Message");

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutMessage);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerMessage);
        childFragment = getChildFragmentManager();
         adapter = new ViewPagerAdapter(childFragment);

        adapter.addFragment(new MyChatsFragment(), "CHATS");
        adapter.addFragment(new ContactsFragment(), "CONTACTS");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void tabNotify() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //   ViewPagerAdapter adapter = new ViewPagerAdapter(childFragment);
                    int unread =0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(currentuser.getUid()) && !chatModel.getIsseen()) {
                        unread++;
                    }
                }
                if (unread == 0) {
                    adapter.setTittles(0, "CHATS");
                } else {
                    adapter.setTittles(0, "(" + unread + ")" + "CHATS");
                }

//                if (unread == 0) {
//                    adapter.addFragment(new MyChatsFragment(), "CHATS");
//                } else {
//                    adapter.addFragment(new MyChatsFragment(), "(" + unread + ")" + "CHATS");
//                }
//
//                adapter.addFragment(new ContactsFragment(), "CONTACTS");
//                viewPager.setAdapter(adapter);
//                tabLayout.setupWithViewPager(viewPager);
                Log.e(TAG, "onDataChange: "+unread );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: "+error );
            }
        });
    }

    //Update Firebase Tokens
    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token tokenl = new Token(token);
        reference.child(currentuser.getUid()).setValue(tokenl);


    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tittleTv = MainActivity.toolbar.findViewById(R.id.toolbar_Hompage_titleTV);
        tittleTv.setText("LPI BLOOD BANK");
       tabNotify();

    }

    @Override
    public void onPause() {
        super.onPause();
     
       tabNotify();
    }
}