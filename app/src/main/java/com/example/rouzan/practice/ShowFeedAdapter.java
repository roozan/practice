package com.example.rouzan.practice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    List <Post> feedList;
    onFeedClickListener onFeedClickListener;

    public ShowFeedAdapter(List<Post> feedList,onFeedClickListener onFeedClickListener) {
        this.feedList = feedList;
        this.onFeedClickListener=onFeedClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bindView(feedList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
TextView showUsername,showFoodName;
Button tasted,notTasted;
ImageView foodImage;
    public ViewHolder(View itemView) {
        super(itemView);
        showUsername=itemView.findViewById(R.id.username);
        showFoodName=itemView.findViewById(R.id.food_name);
        tasted=itemView.findViewById(R.id.button_tasted);
        notTasted=itemView.findViewById(R.id.button_not_tasted);
        foodImage=itemView.findViewById(R.id.food_image);
    }
    private void bindView(Post feed){
        //showUsername.setText(feed.getCategoryName());
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
        showFoodName.setText(feed.getFoodName());
        if(TextUtils.isEmpty(feed.getFoodImageUrl())){
            foodImage.setVisibility(View.GONE);
        }
        else {
            foodImage.setVisibility(View.VISIBLE);
            Glide.with(foodImage.getContext()).load(feed.getFoodImageUrl()).into(foodImage);
        }

    }
}
    interface onFeedClickListener {
        void onUsernameClicked(User user);
    }

}

