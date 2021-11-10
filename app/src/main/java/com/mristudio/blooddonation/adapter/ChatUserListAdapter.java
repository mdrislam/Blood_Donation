package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.ChatModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ChatViewHoalder> {
    private List<UserInformation> userInformationList = new ArrayList<>();
    private Context context;
    private ClickButton clickButton;
    private int selsctPosition = -1;
    private Boolean isChat;
    private String theLastMsg;

    public ChatUserListAdapter(Context context, ClickButton clickButton, Boolean isChat) {
        this.context = context;
        this.userInformationList = userInformationList;
        this.clickButton = clickButton;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ChatViewHoalder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_chat_profile, parent, false);
        return new ChatViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHoalder holder, int position) {
        holder.tvprofielName.setText(userInformationList.get(position).getName());
        //holder.tvLastMessage.setText(userInformationList.get(position));

//        Picasso.get()
//                .load( R.drawable.chat_active_indicator)
//                .into(holder.ivChatActiveIndicator);
        Picasso.get()
                .load(userInformationList.get(position).getUserProfilePicture())
                .into(holder.CIVchatProfileImages);

        holder.llChatLesenner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selsctPosition = position;
                notifyDataSetChanged();
                clickButton.setChatLesenner(userInformationList.get(position));
            }
        });

        if (isChat) {

            holder.ivChatActiveIndicator.setVisibility(View.VISIBLE);
            holder.tvLastMessage.setVisibility(View.VISIBLE);

            //Show Last Msg
            lastMessage(userInformationList.get(position).getUserId(), holder.tvLastMessage);

            if (userInformationList.get(position).getActiveStatus().equals("active")) {
                holder.ivChatActiveIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_active_indicator));
            } else if (userInformationList.get(position).getActiveStatus().equals("online")) {

                holder.ivChatActiveIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_online_indicator));


            } else {

                holder.ivChatActiveIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_inactive_indicator));

            }

        } else {

            holder.ivChatActiveIndicator.setVisibility(View.GONE);
            holder.tvLastMessage.setVisibility(View.GONE);
        }
    }

    public void setData(List<UserInformation> userInformationList) {
        this.userInformationList = userInformationList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userInformationList.size();
    }

    public class ChatViewHoalder extends RecyclerView.ViewHolder {
        private LinearLayout llChatLesenner;
        private CircleImageView CIVchatProfileImages;
        private ImageView ivChatActiveIndicator;
        private TextView tvprofielName, tvLastMessage;

        public ChatViewHoalder(@NonNull View itemView) {
            super(itemView);
            llChatLesenner = itemView.findViewById(R.id.llChatLesenner);

            CIVchatProfileImages = itemView.findViewById(R.id.CIVchatProfileImages);
            ivChatActiveIndicator = itemView.findViewById(R.id.ivChatActiveIndicator);
            tvprofielName = itemView.findViewById(R.id.tvprofielName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }

    //Cheak for Last Message
    private void lastMessage(String userId, TextView last_msg) {
        theLastMsg = "default";
        List<ChatModel> chatModelList = new ArrayList<>();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    chatModelList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatModel chatModel = snapshot.getValue(ChatModel.class);
                        if (chatModel.getReceiver().equals(currentuser.getUid()) && chatModel.getSender().equals(userId) ||
                                chatModel.getReceiver().equals(userId) && chatModel.getSender().equals(currentuser.getUid())) {
                            theLastMsg = chatModel.getMessage();
                            chatModelList.add(chatModel);
                        }

                    }
                    if (!chatModelList.isEmpty()) {

                        if (chatModelList.get(chatModelList.size() - 1).getIsseen()) {
                            last_msg.setTextColor(context.getResources().getColor(R.color.overlay_white));
                            last_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                            last_msg.setTypeface(null, Typeface.NORMAL);
                        } else {
                            last_msg.setTextColor(context.getResources().getColor(R.color.black));
                            last_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                            last_msg.setTypeface(null, Typeface.BOLD);
                        }
                    }

                    switch (theLastMsg) {
                        case "default":
                            last_msg.setText("No Message");
                            break;
                        default:
                            last_msg.setText(theLastMsg);
                    }
                    theLastMsg = "default";
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface ClickButton {
        void setChatLesenner(UserInformation userChatId);
    }
}
