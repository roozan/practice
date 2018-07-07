package com.example.rouzan.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
RecyclerView showFeedList;
    ProgressBar mainPb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        showFeedList=findViewById(R.id.show_feed_list);
        mainPb=findViewById(R.id.main_pb);



        LinearLayoutManager Manager=new LinearLayoutManager(this);
        Manager.setOrientation(LinearLayoutManager.VERTICAL);
        Manager.setReverseLayout(true);
        Manager.setStackFromEnd(true);
        showFeedList.setLayoutManager(Manager);
        GetFeedList();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_post_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this,AddPostActivity.class);
               startActivity(intent);
            }
        });
    }

    private void GetFeedList() {
        FirebaseDatabase.getInstance().getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List <Post> postList = new ArrayList<>();
                Iterator <DataSnapshot> dataSnapshotIterator= dataSnapshot.getChildren().iterator();
                while(dataSnapshotIterator.hasNext()){
                    DataSnapshot snapshot = dataSnapshotIterator.next();
                    postList.add(snapshot.getValue(Post.class));
                }
                ShowFeedAdapter adapter = new ShowFeedAdapter(postList);
                showFeedList.setAdapter(adapter);
                mainPb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mainPb.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error:"+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

  /*  List<Post> postList=new ArrayList<>();
    Iterator <DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
        DataSnapshot snap=iterator.next();
        postList.add(snap.getValue(Post.class));

    }
    ShowFeedAdapter showFeedAdapter = new ShowFeedAdapter(postList);
                showFeedList.setAdapter(showFeedAdapter);*/

}
