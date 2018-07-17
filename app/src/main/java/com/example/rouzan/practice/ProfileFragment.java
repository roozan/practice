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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private final String TAG= ProfileFragment.class.getSimpleName();

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
                        List<String> postIdList=new ArrayList<>();
                        Iterator<DataSnapshot> dataSnapshotIterator= dataSnapshot.getChildren().iterator();
                        while(dataSnapshotIterator.hasNext()){
                            DataSnapshot snapshot = dataSnapshotIterator.next();
                            Post post=snapshot.getValue(Post.class);
                            postList.add(post);
                            postIdList.add(post.getPostId());
                        }


                        getUserDetails(postIdList);
                        ShowFeedAdapter adapter = new ShowFeedAdapter(postIdList, new ShowFeedAdapter.onFeedClickListener() {
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

    void getUserDetails(final List<String> postIdList){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUserName());
                category.setText(user.getCategory());
                Glide.with(getContext()).load(user.getProfilePhotoUrl()).into(profilePhoto);
                Log.d(TAG,"the logged in user id is:"+FirebaseAuth.getInstance().getUid());
                Log.d(TAG,"the user if to be checked is:"+user.getUserId());


                FirebaseDatabase.getInstance().getReference().child("following")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(user.getUserId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG,"the key is:"+dataSnapshot.getKey());
                                Log.d(TAG,"the value is:"+dataSnapshot.getValue(Boolean.class));

                                if(dataSnapshot.getValue()!=null) {
                                    followButton.setText("Followed!");
                                }
                                else{
                                    followButton.setText("Follow");
                                    followButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

                                            reference.child("followers").child(user.getUserId()).child(FirebaseAuth.getInstance().getUid()).setValue(true);
                                            reference.child("following").child(FirebaseAuth.getInstance().getUid()).child(user.getUserId()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){
                                                        followButton.setText("Followed");
                                                    }
                                                }
                                            });

                                            if(postIdList.size()>0)
                                                for (int i=0;i<postIdList.size();i++)
                                                    reference.child("userFeed").child(FirebaseAuth.getInstance().getUid()).child(postIdList.get(i)).setValue(0);
                                        }
                                    });


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

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




        // Inflate the layout for this fragment
        return view;
    }



}
