package com.example.rouzan.practice;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ProfileFragment extends Fragment {

    TextView username,category;
    RecyclerView showFeedList;


    public ProfileFragment() {
        // Required empty public constructor
    }



    void defineView(View view){

        username=view.findViewById(R.id.username);
        category=view.findViewById(R.id.category);
        showFeedList=view.findViewById(R.id.show_feed_list);

    }
    private void GetFeedList() {
        FirebaseDatabase.getInstance().getReference().child("posts").orderByChild("postUploaderId").equalTo(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Post> postList = new ArrayList<>();
                        Iterator<DataSnapshot> dataSnapshotIterator= dataSnapshot.getChildren().iterator();
                        while(dataSnapshotIterator.hasNext()){
                            DataSnapshot snapshot = dataSnapshotIterator.next();
                            postList.add(snapshot.getValue(Post.class));
                        }
                        ShowFeedAdapter adapter = new ShowFeedAdapter(postList);
                        showFeedList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error:"+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    void getUsername(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUserName());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error:"+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    void getCategory(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                category.setText(user.getCategory());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error:"+databaseError.getMessage().toString() ,Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        defineView(view);

        LinearLayoutManager Manager=new LinearLayoutManager(getContext());
        Manager.setOrientation(LinearLayoutManager.VERTICAL);
        Manager.setReverseLayout(true);
        Manager.setStackFromEnd(true);
        showFeedList.setLayoutManager(Manager);
        GetFeedList();



        getUsername();
        getCategory();

        // Inflate the layout for this fragment
        return view;
    }



}
