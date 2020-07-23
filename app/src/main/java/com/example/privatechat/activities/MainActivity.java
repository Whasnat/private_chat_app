package com.example.privatechat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.privatechat.R;
import com.example.privatechat.adapters.TabAccessorAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabAccessorAdapter myTabAccessorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();


        myToolbar = findViewById(R.id.main_page_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Private Chat");

        myViewPager = findViewById(R.id.main_tabs_pager);
        myTabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabAccessorAdapter);

        myTabLayout = findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            Toast.makeText(this, "User = null", Toast.LENGTH_SHORT).show();
            sendUserToLoginActivity();
        } else {
            Toast.makeText(this, "User not NUll", Toast.LENGTH_SHORT).show();
            verifyUserExistance();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int optionId = item.getItemId();
        switch (optionId) {
            case R.id.find_friends_options:
                Toast.makeText(this, "Ha Ha You have no Friends.. Kill yourself",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.settings_options:
                sendUserToSettingsActivity();
                break;

            case R.id.create_group_options:
                newGroup();
                break;

            case R.id.logout_options:
                mAuth.signOut();
                sendUserToLoginActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void verifyUserExistance() {
        String CurrentUserID = currentUser.getUid();
        rootRef.child("Users").child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                } else {
                    sendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToSettingsActivity() {
        Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
        finish();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    private void newGroup() {
        AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogueBuilder.setTitle("Enter Group Name:");

        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("e.g. Group Name");
        alertDialogueBuilder.setView(groupNameField);

        alertDialogueBuilder.setPositiveButton("create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(MainActivity.this, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                } else {
                    createNewGroup(groupName);
                }
            }
        });
        alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogueBuilder.show();

    }

    private void createNewGroup(final String groupName) {
        rootRef.child("Users").child(currentUser.getUid()).child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(MainActivity.this, groupName + " is created successfully :)", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Oops!! Something went Wrong.. Try again please...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}