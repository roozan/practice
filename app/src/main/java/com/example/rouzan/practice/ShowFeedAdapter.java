package com.example.rouzan.practice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rouzan on 10-06-2018.
 */

public class ShowFeedAdapter extends RecyclerView.Adapter<ShowFeedAdapter.ViewHolder> {
    ArrayList <Feed> feedList;

    public ShowFeedAdapter(ArrayList<Feed> feedList) {
        this.feedList = feedList;
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
    public ViewHolder(View itemView) {
        super(itemView);
        showUsername=itemView.findViewById(R.id.username);
        showFoodName=itemView.findViewById(R.id.food_name);
        tasted=itemView.findViewById(R.id.button_tasted);
        notTasted=itemView.findViewById(R.id.button_not_tasted);
    }
    private void bindView(Feed feed){
        showUsername.setText(feed.getUploaderName());
        showFoodName.setText(feed.getFoodName());

    }
}
}
