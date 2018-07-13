package com.example.rouzan.practice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    String firstNameStr,lastNameStr,usernameStr,emailStr,passwordStr,confirmPasswordStr,categoryStr;
    EditText firstNameEt,lastNameEt,usernameEt,emailEt,confirmPasswordET,passwordEt;
    RadioGroup categoryRg;
    Button SignUp,uploadProfilePhoto;
    ProgressBar signupPb;
    Bitmap bitmap;
    Uri datapath;
    final int PICK_IMAGE_REQUEST =71;
    ImageView profilePhoto;

    FirebaseStorage storage;
    StorageReference storageReference;

    void defineView(){
        firstNameEt=findViewById(R.id.first_name_et);
        lastNameEt=findViewById(R.id.last_name_et);
        usernameEt=findViewById(R.id.username_et);
        emailEt=findViewById(R.id.email_et);
        passwordEt=findViewById(R.id.password_et);
        confirmPasswordET=findViewById(R.id.confirm_password_et);
        categoryRg=findViewById(R.id.category_rg);
        SignUp=findViewById(R.id.signup_button);
        signupPb=findViewById(R.id.signup_pb);
        uploadProfilePhoto=findViewById(R.id.upload_profile_photo);
        profilePhoto=findViewById(R.id.profile_photo);
    }

    Boolean validate() {
        boolean isVali=false;
        firstNameStr=firstNameEt.getText().toString();
        lastNameStr=lastNameEt.getText().toString();
        usernameStr=usernameEt.getText().toString();
        emailStr=emailEt.getText().toString();
        passwordStr=passwordEt.getText().toString();
        confirmPasswordStr=confirmPasswordET.getText().toString();

        if (TextUtils.isEmpty(firstNameStr))
            firstNameEt.setError("Required");
        else if (TextUtils.isEmpty(lastNameStr))
            lastNameEt.setError("Required");
        else if (TextUtils.isEmpty(usernameStr))
            usernameEt.setError("Required");
        else if (TextUtils.isEmpty(emailStr))
            emailEt.setError("Required");
        else if (TextUtils.isEmpty(passwordStr))
            passwordEt.setError(("Required"));
        else if (TextUtils.isEmpty(confirmPasswordStr))
            confirmPasswordET.setError("Required");
        else if (TextUtils.isEmpty(categoryStr))
            Toast.makeText(this, "Select at least one", Toast.LENGTH_SHORT).show();
        else
            isVali=true;
    return isVali;

    }


    void registerUserToFirebase(){
        final User user=new User();
        user.setFirstName(firstNameStr);
        user.setLastName(lastNameStr);
        user.setUserName(usernameStr);
        user.setEmail(emailStr);
        user.setPassword(passwordStr);
        user.setCategory(categoryStr);


        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user.setUserId(task.getResult().getUser().getUid());


                    storageReference= FirebaseStorage.getInstance().getReference().child("user_profile_photo")
                            .child(String.valueOf(System.currentTimeMillis()));
                    storageReference.putFile(datapath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                                throw task.getException();
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri uri = task.getResult();
                                String downloadUrl = uri.toString();
                                user.setProfilePhotoUrl(downloadUrl);
                                uploadUserDataToFirebase(user);
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(SignUpActivity.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();
                signupPb.setVisibility(View.GONE);
                }
            }
        });
    }

    void uploadUserDataToFirebase(User user){
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(user.getUserId())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {


                    signupPb.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Registered ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {

                    Toast.makeText(SignUpActivity.this, "Error!" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    signupPb.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null){
            datapath=data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),datapath);
                profilePhoto.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.getStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        defineView();

        uploadProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(validate()){
                 signupPb.setVisibility(View.VISIBLE);
                 registerUserToFirebase();
                // Toast.makeText(SignUpActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
             }
            }
        });
        categoryRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedButton=findViewById(i);
                categoryStr=checkedButton.getText().toString();
                Toast.makeText(SignUpActivity.this, "Selected:"+categoryStr, Toast.LENGTH_SHORT).show();

            }
        });






    }

}
