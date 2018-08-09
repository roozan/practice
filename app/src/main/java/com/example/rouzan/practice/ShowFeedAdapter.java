package com.example.rouzan.practice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rouzan on 10-06-2018.
 */

public class ShowFeedAdapter extends RecyclerView.Adapter<ShowFeedAdapter.ViewHolder> {
    List <String> feedIdList;
    onFeedClickListener onFeedClickListener;

    private final String TAG=ShowFeedAdapter.class.getSimpleName();
    public ShowFeedAdapter(List<String> feedIdList,onFeedClickListener onFeedClickListener) {
        this.feedIdList = feedIdList;
        this.onFeedClickListener=onFeedClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(feedIdList.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post=dataSnapshot.getValue(Post.class);
                holder.bindView(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedIdList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
TextView showUsername,showFoodName,tastedCount;
ImageButton toggleButton;
ImageView foodImage;
    public ViewHolder(View itemView) {
        super(itemView);
        showUsername=itemView.findViewById(R.id.username);
        showFoodName=itemView.findViewById(R.id.food_name);
        toggleButton=itemView.findViewById(R.id.toggle_button);
        foodImage=itemView.findViewById(R.id.food_image);
        tastedCount=itemView.findViewById(R.id.tasted_count);

    }
    private void bindView(final Post feed){
        //showUsername.setText(feed.getCategoryName());

        toggleButton.setImageResource(R.drawable.nottasted);
        toggleButton.setSelected(false);
       // tastedCount.setText(String.valueOf(feed.getTastedCount())+" people have tasted it!");



        FirebaseDatabase.getInstance().getReference().child("users")
                .child(feed.getPostUploaderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user=dataSnapshot.getValue(User.class);
                showUsername.setText(user.getUserName());

                showUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFeedClickListener.onUsernameClicked(user);
                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        FirebaseDatabase.getInstance().getReference().child("tasted")
                .child(FirebaseAuth.getInstance().getUid())
                .child(feed.getPostId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue()!=null) {

                            Log.d(TAG,"inside on data vhange");
                            Log.d(TAG,"inside  value :"+dataSnapshot.getValue(Boolean.class));

                            if(dataSnapshot.getValue(Boolean.class)) {
                                toggleButton.setImageResource(R.drawable.tasted);
                                toggleButton.setSelected(true);
                            }
                            else {
                                toggleButton.setImageResource(R.drawable.nottasted);
                                toggleButton.setSelected(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.isSelected()){
                    onFeedClickListener.onTastedButtonToggled(feed,false);
                    toggleButton.setImageResource(R.drawable.nottasted);
                    toggleButton.setSelected(false);
                    Log.d(TAG,"inside false click");
                    tastedCount.setText(String.valueOf(feed.getTastedCount())+" people have tasted it!");


                }else {
                    onFeedClickListener.onTastedButtonToggled(feed,true);
                    toggleButton.setImageResource(R.drawable.tasted);
                    toggleButton.setSelected(true);
                    Log.d(TAG,"inside true click");
                    tastedCount.setText(String.valueOf(feed.getTastedCount())+" people have tasted it!");

                }
            }
        });

        if(TextUtils.isEmpty(feed.getFoodImageUrl())){
            foodImage.setVisibility(View.GONE);
        }
        else {
            foodImage.setVisibility(View.VISIBLE);
            Glide.with(foodImage.getContext()).load(feed.getFoodImageUrl()).into(foodImage);
        }

        showFoodName.setText(feed.getFoodName());
        tastedCount.setText(String.valueOf(feed.getTastedCount())+" people have tasted it!");


    }
}
    interface onFeedClickListener {
        void onUsernameClicked(User user);
        void onTastedButtonToggled(Post post,boolean tasted);
    }
}

