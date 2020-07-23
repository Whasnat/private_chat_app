package com.example.privatechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.privatechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ImageView profileImage;
    private EditText username, status;
    private TextView userNameTextView;
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

        CurrentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        init();

        userNameTextView.setVisibility(View.VISIBLE);
        username.setVisibility(View.INVISIBLE);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        //  userNameTextView.isClickable();
        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameTextView.setVisibility(View.INVISIBLE);
                username.setVisibility(View.VISIBLE);
                username.callOnClick();

            }
        });

        retrieveUserInfo();
    }


    private void init() {
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username_field);
        status = findViewById(R.id.status_field);
        updateBtn = findViewById(R.id.update_profile_btn);
        userNameTextView = findViewById(R.id.userName_textView);
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
                        String errMsg = Objects.requireNonNull(task.getException()).toString();
                        Toast.makeText(SettingsActivity.this, "Error!!" + errMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void retrieveUserInfo() {
        rootRef.child("Users").child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                    final String displayName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    userNameTextView.setText(displayName);
                } else {
                    username.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendUserToMainActivity();
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        //  finish();
    }


}