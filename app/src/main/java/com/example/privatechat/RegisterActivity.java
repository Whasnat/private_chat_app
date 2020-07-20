package com.example.privatechat;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText regEmail, regPassword;
    private EditText confirmPass;
    private Button registerBtn, phoneRegBtn;
    private TextView loginText;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginPage();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUserAccount();
            }
        });
    }


    //Create a new User Account With Email and Password
    private void createNewUserAccount() {
        String userEmail = regEmail.getText().toString();

        String userPass = regPassword.getText().toString();
        Log.d(TAG, "createNewUserAccount: password " + userPass);

        String userConfirmPass = confirmPass.getText().toString();
        Log.d(TAG, "createNewUserAccount: confirm password  " + confirmPass);

        if (TextUtils.isEmpty(userEmail)) {
            Log.d(TAG, "createNewUserAccount: no_Email!!!!!");
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
        }
        //See if any fiends are empty
        else if (TextUtils.isEmpty(userPass)) {
            Log.d(TAG, "createNewUserAccount: no_Pass!!!");
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userConfirmPass)) {
            Log.d(TAG, "createNewUserAccount: no_Confirm_Pass!!!");
            Toast.makeText(this, "Enter Password again", Toast.LENGTH_SHORT).show();
        } else {
            if (!userConfirmPass.equals(userPass)) {
                Log.d(TAG, "createNewUserAccount: pass_no_match" + "PAss: " + userPass + "retype:" + confirmPass);
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.setTitle("Creating New Account...");
                loadingBar.setMessage("Wait a bit, Asshole"); // replace Asshole with a good word
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();
                mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String CurrentUser = mAuth.getCurrentUser().getUid();
                            rootRef.child("Users").child(CurrentUser).setValue("");
                            sendUserToMainPage();
                            Toast.makeText(RegisterActivity.this, "Account Created Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            String errMsg = task.getException().toString();
                            Toast.makeText(RegisterActivity.this, "Error !!" + errMsg, Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.dismiss();
                    }
                });
            }

        }

    }

    // Initialize the Components of activity_register.xml
    private void init() {
        regEmail = findViewById(R.id.reg_email_field);
        regPassword = findViewById(R.id.reg_password_field);
        confirmPass = findViewById(R.id.confirm_pass_field);
        registerBtn = findViewById(R.id.reg_btn);
        phoneRegBtn = findViewById(R.id.phone_reg_btn);
        loginText = findViewById(R.id.login_text);
        loadingBar = new ProgressDialog(this);
    }


    private void sendUserToLoginPage() {
        Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void sendUserToMainPage() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


}