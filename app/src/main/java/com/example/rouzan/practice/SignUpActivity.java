package com.example.rouzan.practice;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    String firstNameStr,lastNameStr,usernameStr,emailStr,passwordStr,confirmPasswordStr,categoryStr;
    EditText firstNameEt,lastNameEt,usernameEt,emailEt,confirmPasswordET,passwordEt;
    RadioGroup categoryRg;
    Button SignUp;

    void defineView(){
        firstNameEt=findViewById(R.id.first_name_et);
        lastNameEt=findViewById(R.id.last_name_et);
        usernameEt=findViewById(R.id.username_et);
        emailEt=findViewById(R.id.email_et);
        passwordEt=findViewById(R.id.password_et);
        confirmPasswordET=findViewById(R.id.confirm_password_et);
        categoryRg=findViewById(R.id.category_rg);
        SignUp=findViewById(R.id.signup_button);
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
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(SignUpActivity.this, "Registered ", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SignUpActivity.this, "Error!"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(SignUpActivity.this, "Error!"+task.getException(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        defineView();
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(validate()){
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
