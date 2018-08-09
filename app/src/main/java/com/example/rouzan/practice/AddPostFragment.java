package com.example.rouzan.practice;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {

    Bitmap bitmap;
    Button Post,choosePhoto,categoryVeg,categoryNonVeg;
    CheckBox checkBoxSpicy,checkBoxSour,checkBoxSweet,checkBoxSalty,checkBoxBitter;
    String foodNameStr,categoryStr,userCategory;
    ArrayList<String> categoryList=new ArrayList<>();
    EditText foodNameEt;
    ImageView photoImageView;
    Uri filePath;
    final int PICK_IMAGE_REQUEST =71;
    ProgressBar addPostPb;
    RadioGroup categoryRg;

    FirebaseStorage storage;
    StorageReference storageReference;


    public AddPostFragment() {
        // Required empty public constructor
    }
    void defineView(View view){
        foodNameEt=view.findViewById(R.id.food_name_et);

        checkBoxSpicy=view.findViewById(R.id.checkbox_spicy);
        checkBoxSour=view.findViewById(R.id.checkbox_sour);
        checkBoxSweet=view.findViewById(R.id.checkbox_sweet);
        checkBoxSalty=view.findViewById(R.id.checkbox_salty);
        checkBoxBitter=view.findViewById(R.id.checkbox_bitter);

        choosePhoto=view.findViewById(R.id.choose_photo_button);
        photoImageView=view.findViewById(R.id.photo_image_view);
        addPostPb=view.findViewById(R.id.add_post_pb);
        categoryRg=view.findViewById(R.id.category_rg);
        categoryVeg=view.findViewById(R.id.category_veg_rb);
        categoryNonVeg=view.findViewById(R.id.category_nonveg_rb);


    }

    void choosePhoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                photoImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void getUserCategory(){
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                userCategory=user.getCategory();

                if(userCategory.equalsIgnoreCase("veg")){
                    categoryVeg.setSelected(true);
                    categoryNonVeg.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    Boolean checkBoxStatus(){
        boolean value=false;
        if(checkBoxBitter.isChecked())
            categoryList.add(checkBoxBitter.getText().toString());
        else if(checkBoxSweet.isChecked())
            categoryList.add(checkBoxSweet.getText().toString());
        else if(checkBoxSalty.isChecked())
            categoryList.add(checkBoxSalty.getText().toString());
        else if(checkBoxSour.isChecked())
            categoryList.add(checkBoxSour.getText().toString());
        else if(checkBoxSpicy.isChecked())
            categoryList.add(checkBoxSpicy.getText().toString());

        else
            value =true;

        return value;

    }
    Boolean validate(){
        boolean value=false;

        foodNameStr=foodNameEt.getText().toString();

        if(TextUtils.isEmpty(foodNameStr))
            foodNameEt.setError("Required!");

        else if (TextUtils.isEmpty(categoryStr))
            Toast.makeText(getContext(), "Select at least one!", Toast.LENGTH_SHORT).show();

        else if (checkBoxStatus())
            Toast.makeText(getContext(), "Select at least one category!", Toast.LENGTH_SHORT).show();

        else
            value=true;
        return value;

    }

    void registerPostToFirebase(){
        final Post post=new Post();
        post.setFoodName(foodNameStr);
        post.setCategoryName(categoryStr);
        post.setCategoryList(categoryList);
        post.setPostUploaderId(FirebaseAuth.getInstance().getUid());
        post.setPostUploadTime(System.currentTimeMillis());
        post.setTastedCount(0);
        post.setNotTastedCount(0);
        String postId=FirebaseDatabase.getInstance().getReference().child("posts")
                .push().getKey();
        post.setPostId(postId);


        if(bitmap!=null){
            storageReference=FirebaseStorage.getInstance().getReference().child("post_pictures")
                    .child(String.valueOf(System.currentTimeMillis()));
            storageReference.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                        throw task.getException();
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri=task.getResult();
                        String downloadUrl=uri.toString();
                        post.setFoodImageUrl(downloadUrl);
                        uploadPostToFirebase(post);
                    }

                }
            });

        }
        else {
            post.setFoodImageUrl("");
            uploadPostToFirebase(post);
        }
    }



    void uploadPostToFirebase(final Post post){
        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(post.getPostId())
                .setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            addPostPb.setVisibility(View.GONE);
                            FirebaseDatabase.getInstance().getReference().child("followers")
                                    .child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator <DataSnapshot> snapshotIterator = dataSnapshot.getChildren().iterator();
                                    while(snapshotIterator.hasNext()){
                                        DataSnapshot snapshot=snapshotIterator.next();
                                        String userFollowerId=snapshot.getKey();
                                        FirebaseDatabase.getInstance().getReference().child("userFeed").child(userFollowerId)
                                                .child(post.getPostId()).setValue(0);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            addPostPb.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_add_post,container,false);
        defineView(view);
        getUserCategory();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        Post=view.findViewById(R.id.post_button);

        categoryRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedButton=view.findViewById(i);
                categoryStr=checkedButton.getText().toString();

            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                {
                    addPostPb.setVisibility(View.VISIBLE);
                    registerPostToFirebase();
                }
            }
        });


        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        return view;
    }

}
