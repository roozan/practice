package com.example.rouzan.practice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {
    Bitmap bitmap;
    Button Post,choosePhoto;
    CheckBox checkBoxSpicy,checkBoxSour,checkBoxSweet,checkBoxSalty,checkBoxBitter;
    String foodNameStr,foodCategoryStr;
    ArrayList<String> categoryList=new ArrayList<>();
    EditText foodNameEt,foodCategoryEt;
    ImageView photoImageView;
    Uri filePath;
    final int PICK_IMAGE_REQUEST =71;
    ProgressBar addPostPb;

    FirebaseStorage storage;
    StorageReference storageReference;



    void defineView(){
        foodNameEt=findViewById(R.id.food_name_et);
        foodCategoryEt=findViewById(R.id.food_category_et);

        checkBoxSpicy=findViewById(R.id.checkbox_spicy);
        checkBoxSour=findViewById(R.id.checkbox_sour);
        checkBoxSweet=findViewById(R.id.checkbox_sweet);
        checkBoxSalty=findViewById(R.id.checkbox_salty);
        checkBoxBitter=findViewById(R.id.checkbox_bitter);

        choosePhoto=findViewById(R.id.choose_photo_button);
        photoImageView=findViewById(R.id.photo_image_view);
        addPostPb=findViewById(R.id.add_post_pb);



    }

    void choosePhoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);

    }
     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photoImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void uploadPhoto(){
        if(filePath != null)
        {

        }
    }
    void uploadPhotoToDatabase(Post post){
        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(FirebaseAuth.getInstance().getUid()).setValue(post);

    }


  /*  public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {

            case R.id.checkbox_spicy:
                if (checked)
                    categoryList.add(checkBoxSpicy.getText().toString());
                else
                categoryList.remove(checkBoxSpicy.getText().toString());
                break;

            case R.id.checkbox_salty:
                if (checked)
                    categoryList.add(checkBoxSalty.getText().toString());
                else
                categoryList.remove(checkBoxSalty.getText().toString());
                break;

            case R.id.checkbox_sour:
                if(checked)
                    categoryList.add(checkBoxSour.getText().toString());
                else
                    categoryList.remove(checkBoxSour.getText().toString());
                    break;

            case R.id.checkbox_sweet:
                if(checked)
                    categoryList.add(checkBoxSweet.getText().toString());
                else
                    categoryList.remove(checkBoxSweet.getText().toString());
                break;

            case R.id.checkbox_bitter:
                if(checked)
                    categoryList.add(checkBoxBitter.getText().toString());
                else
                    categoryList.remove(checkBoxBitter.getText().toString());

            // TODO: Veggie sandwich
        }
    }*/


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
        foodCategoryStr=foodCategoryEt.getText().toString();

        if(TextUtils.isEmpty(foodNameStr))
            foodNameEt.setError("Required!");
        else if (TextUtils.isEmpty(foodCategoryStr))
            foodCategoryEt.setError("Required!");
        else if (checkBoxStatus())
                Toast.makeText(this, "Select at least one category!", Toast.LENGTH_SHORT).show();

        else
            value=true;
        return value;

    }

    void registerPostToFirebase(){
        final Post post=new Post();
        post.setFoodName(foodNameStr);
        post.setCategoryName(foodCategoryStr);
        post.setCategoryList(categoryList);
        post.setPostUploaderId(FirebaseAuth.getInstance().getUid());
        post.setPostUploadTime(System.currentTimeMillis());
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
    void uploadPostToFirebase(Post post){
        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(post.getPostId())
                .setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            addPostPb.setVisibility(View.GONE);
                            Toast.makeText(AddPostActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            addPostPb.setVisibility(View.GONE);
                            Toast.makeText(AddPostActivity.this, "error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        defineView();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        Post=findViewById(R.id.post_button);
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





        }






}
