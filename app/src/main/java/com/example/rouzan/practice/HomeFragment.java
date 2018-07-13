package com.example.rouzan.practice;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    RecyclerView showFeedList;
    ProgressBar mainPb;
    String userCategory;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        showFeedList=view.findViewById(R.id.show_feed_list);
        mainPb=view.findViewById(R.id.main_pb);

        LinearLayoutManager Manager=new LinearLayoutManager(getContext());
        Manager.setOrientation(LinearLayoutManager.VERTICAL);
        Manager.setReverseLayout(true);
        Manager.setStackFromEnd(true);
        showFeedList.setLayoutManager(Manager);
        getUserCategory();


        return view;
    }

    void getUserCategory(){
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                userCategory=user.getCategory();

                if(userCategory.equalsIgnoreCase("veg")){
                    getVegFeedList(userCategory);
                }
                else{
                    getFeedList();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getFeedList() {
        FirebaseDatabase.getInstance().getReference().child("posts")
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
                        mainPb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mainPb.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error:"+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void getVegFeedList(String userCategory) {
        FirebaseDatabase.getInstance().getReference().child("posts").orderByChild("categoryName").equalTo("Veg")
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
                        mainPb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mainPb.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error:"+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
