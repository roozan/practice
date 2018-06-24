package com.example.rouzan.practice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
RecyclerView showFeedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        showFeedList=findViewById(R.id.show_feed_list);



        LinearLayoutManager Manager=new LinearLayoutManager(this);
        Manager.setOrientation(LinearLayoutManager.VERTICAL);
        showFeedList.setLayoutManager(Manager);

        ShowFeedAdapter showFeedAdapter = new ShowFeedAdapter(GetFeedList());
        showFeedList.setAdapter(showFeedAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private ArrayList<Feed> GetFeedList() {
        ArrayList<Feed> feedlist = new ArrayList<>();
            Feed feed = new Feed();
            feed.setFoodName("MO:Mo");
            feed.setUploaderName("Rojina");
            feedlist.add(feed);

            Feed feed1=new Feed();
            feed1.setUploaderName("Rojan");
            feed1.setFoodName("Chowmein");
            feedlist.add(feed1);

            Feed feed2=new Feed();
            feed2.setUploaderName("Rojan");
            feed2.setFoodName("Sekuwa");
            feedlist.add(feed2);

        return feedlist;
    }

}
