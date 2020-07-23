package com.example.privatechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.privatechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseUser currentUser;
    private Button loginBtn, phoneLoginBtn;
    private EditText email, password;
    private TextView registerText, forgotPassText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: Current User: " + currentUser);

        initialize();

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String userEmail = email.getText().toString();
        String userPass = password.getText().toString();
        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email de magi", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userPass)){
            Toast.makeText(this, "Password de Madarchod", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        currentUser = mAuth.getCurrentUser();
                        sendUserToMainActivity();
                    }
                    else {
                        String errMsg = Objects.requireNonNull(task.getException()).toString();
                        Toast.makeText(LoginActivity.this, "Error !!" + errMsg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onComplete: error!!" + errMsg);
                    }
                }
            });
        }
    }

    //initialize the Components of activity_login.xml
    private void initialize() {
        loginBtn = findViewById(R.id.login_btn);
        phoneLoginBtn = findViewById(R.id.phone_login_btn);
        email = findViewById(R.id.email_field);
        password = findViewById(R.id.password_field);
        registerText = findViewById(R.id.register_text);
        forgotPassText = findViewById(R.id.forgot_pass_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            sendUserToMainActivity();
        }
    }


    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

}

