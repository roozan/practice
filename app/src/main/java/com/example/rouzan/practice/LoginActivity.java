package com.example.rouzan.practice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    String emailStr,passwordStr;
    EditText usernameEt,passwordEt;
    ProgressBar loginPb;

    Boolean validate() {
        boolean vali=false;

        emailStr = usernameEt.getText().toString();
        passwordStr = passwordEt.getText().toString();
        if(TextUtils.isEmpty(emailStr))
            usernameEt.setError("Required!");
        else if (TextUtils.isEmpty(passwordStr))
            passwordEt.setError("Required");
        else
            vali=true;
        return vali;
    }

    void LoginUserToFirebase(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    loginPb.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(FirebaseAuth.getInstance().getUid())) {


            setContentView(R.layout.activity_login);

            Button signup = findViewById(R.id.signup_button);
            usernameEt = findViewById(R.id.email_et);
            passwordEt = findViewById(R.id.password_et);
            loginPb=findViewById(R.id.login_pb);


            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
            final Button login = findViewById(R.id.login_button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validate()) {
                        loginPb.setVisibility(View.VISIBLE);
                        LoginUserToFirebase();
                    }
                }
            });
        }
    else{
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}

