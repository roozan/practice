package com.example.rouzan.practice;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

public class ShowUserAdapter extends RecyclerView.Adapter<ShowUserAdapter.viewHolder>{

    List<User> userList;
    onUserClickListener onUserClickListener;

    public ShowUserAdapter(List<User> userList,onUserClickListener onUserClickListener) {
        this.userList = userList;
        this.onUserClickListener=onUserClickListener;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_user,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        holder.bindView(userList.get(position));


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView userProfilePhoto;
        TextView username, category;
        Button follow;
        LinearLayout parentShowUser;

        public viewHolder(View itemView) {
            super(itemView);
            userProfilePhoto = itemView.findViewById(R.id.user_profile_photo);
            username = itemView.findViewById(R.id.username);
            category = itemView.findViewById(R.id.category);
            follow = itemView.findViewById(R.id.follow_button);
            parentShowUser=itemView.findViewById(R.id.parent_show_user);

        }

        private void bindView(final User user) {

            Glide.with(userProfilePhoto.getContext()).load(user.getProfilePhotoUrl()).into(userProfilePhoto);
            username.setText(user.getUserName());
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onUsernameClicked(user);
                }
            });
            category.setText(user.getCategory());

          //  Log.d("Adapter","the user id is:"+user.getUserId());

            FirebaseDatabase.getInstance().getReference().child("following")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(user.getUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                if(dataSnapshot.getValue(Boolean.class))
                                    parentShowUser.setBackgroundColor(Color.parseColor("#f2f2f2"));
                                else
                                    parentShowUser.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


         /*   if(follow)
                parentShowUser.setBackgroundColor(Color.parseColor("#f2f2f2"));
            else
                parentShowUser.setBackgroundColor(Color.parseColor("#ffffff"));*/
          //  Log.d("Adapter","the user id is:"+user.getUserId());



        }


    }
    interface onUserClickListener {
        void onUsernameClicked(User user);
    }
}
