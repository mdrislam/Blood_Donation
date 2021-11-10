package com.mristudio.blooddonation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.CommentModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.view.activity.PostDetailsActivity;
import com.mristudio.blooddonation.view.activity.PostDetails_Activity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<CommentModel> commentModelList = new ArrayList<>();
    private Context context;
    UserInformation userInformation;

    public CommentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_coment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel commentModel = commentModelList.get(position);


        holder.tvMessage.setText(commentModel.getCommentMsg());

        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userInformation != null) {

                    if (userInformation.getUserId() != null) {

                        Intent intent = new Intent(context, PostDetailsActivity.class);
                        intent.putExtra("userPostId", userInformation.getUserId());
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

                    } else {
                        Toasty.error(context, "Unable to Open Profile", Toasty.LENGTH_SHORT).show();
                    }
                }
            }
        });


        FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(commentModel.getuId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserInformation userInformation = snapshot.getValue(UserInformation.class);

                        if (userInformation != null) {
                            holder.tVProfileName.setText(userInformation.getName());
                            Picasso.get().load(userInformation.getUserProfilePicture()).into(holder.ivProfileImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Error: " + error.getMessage());

                    }
                });

    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public void setComment(List<CommentModel>commentModelList){
        this.commentModelList = commentModelList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivProfileImage;
        private TextView tVProfileName, tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tVProfileName = itemView.findViewById(R.id.tVProfileName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
