package com.example.rouzan.practice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ProfileFragment extends Fragment {

    TextView username,category;
    String userId;
    ImageView profilePhoto;
    RecyclerView showFeedList;
    Button followButton;
    Boolean status;

    @SuppressLint("ValidFragment")
    public ProfileFragment(String userId,boolean status){
        this.userId=userId;
        this.status=status;
    }


    public ProfileFragment() {
        // Required empty public constructor
    }



    void defineView(View view){

        username=view.findViewById(R.id.username);
        category=view.findViewById(R.id.category);
        showFeedList=view.findViewById(R.id.show_feed_list);
        profilePhoto=view.findViewById(R.id.profile_photo);
        followButton=view.findViewById(R.id.follow_button);

    }
    private void GetFeedList() {
        FirebaseDatabase.getInstance().getReference().child("posts").orderByChild("postUploaderId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Post> postList = new ArrayList<>();
                        Iterator<DataSnapshot> dataSnapshotIterator= dataSnapshot.getChildren().iterator();
                        while(dataSnapshotIterator.hasNext()){
                            DataSnapshot snapshot = dataSnapshotIterator.next();
                            postList.add(snapshot.getValue(Post.class));
                        }
                        ShowFeedAdapter adapter = new ShowFeedAdapter(postList, new ShowFeedAdapter.onFeedClickListener() {
                            @Override
                            public void onUsernameClicked(User user) {
                            }
                        });
                        showFeedList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    void getUserDetails(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUserName());
                category.setText(user.getCategory());
                Glide.with(profilePhoto.getContext()).load(user.getProfilePhotoUrl()).into(profilePhoto);

                followButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap <String,Boolean> hashFollowersList=new HashMap<>();
                        hashFollowersList.put(user.getUserId(), true);

                        HashMap <String,Boolean> hashFollowingList=new HashMap<>();
                        hashFollowingList.put(FirebaseAuth.getInstance().getUid(), true);

                        user.setFollowersList(hashFollowersList);

                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(FirebaseAuth.getInstance().getUid()).child("followingList").setValue(hashFollowingList);

                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(user.getUserId()).setValue(user);
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error:"+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();

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

        if(status)
         followButton.setVisibility(View.GONE);
        else
            followButton.setVisibility(View.VISIBLE);

        showFeedList.setLayoutManager(Manager);
        GetFeedList();

        getUserDetails();




        // Inflate the layout for this fragment
        return view;
    }



}
