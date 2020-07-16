package com.example.privatechat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private ImageView profileImage;
    private EditText username, status;
    private Button updateBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String CurrentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();

        CurrentUserID = mAuth.getCurrentUser().getUid();

        init();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }


    private void init() {
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username_field);
        status = findViewById(R.id.status_field);
        updateBtn = findViewById(R.id.update_profile_btn);
    }


    private void updateProfile() {
        final String user = username.getText().toString();
        String stat = status.getText().toString();
        if (TextUtils.isEmpty(user) /*&& TextUtils.isEmpty(stat)*/) {
            Toast.makeText(this, "Provide a Username", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> profileMap = new HashMap<>();

            profileMap.put("uid", CurrentUserID);
            profileMap.put("name", user);
            profileMap.put("status", stat);


            rootRef.child("Users").child(CurrentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this, "Profile Updated successfully", Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
                    } else {
                        String errMsg = task.getException().toString();
                        Toast.makeText(SettingsActivity.this, "Error!!" + errMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        //  finish();
    }


}