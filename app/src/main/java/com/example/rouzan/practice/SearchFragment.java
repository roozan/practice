package com.example.rouzan.practice;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    RecyclerView searchResult;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search,container,false);



        searchResult=view.findViewById(R.id.search_result);

        

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchResult.setLayoutManager(linearLayoutManager);
        getUserListFromDatabase();



        return view;
    }

    void getUserListFromDatabase(){

        FirebaseDatabase.getInstance().getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<User> userList = new ArrayList<>();
                        Iterator<DataSnapshot> dataSnapshotIterator= dataSnapshot.getChildren().iterator();
                        while(dataSnapshotIterator.hasNext()){
                            DataSnapshot snapshot = dataSnapshotIterator.next();
                            User user=snapshot.getValue(User.class);

                            if(!TextUtils.equals(user.getUserId(),FirebaseAuth.getInstance().getUid()))
                                userList.add(user);


                        }

                        ShowUserAdapter showUserAdapter=new ShowUserAdapter(userList, new ShowUserAdapter.onUserClickListener() {
                            @Override
                            public void onUsernameClicked(User user) {
                                Intent intent=new Intent(getContext(),UserProfileActivity.class);
                                intent.putExtra("userId", user.getUserId());
                                startActivity(intent);
                            }
                        });
                        searchResult.setAdapter(showUserAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
