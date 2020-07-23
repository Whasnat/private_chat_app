package com.example.privatechat.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.privatechat.R;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTextView;
    private ScrollView mScrollView;
    private EditText mEditText;
    private ImageButton sendBtn;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        init();
        groupName = getIntent().getExtras().get("GROUP NAMe").toString();
        mToolbar.setTitle(groupName);
    }

    private void init() {
        mToolbar = findViewById(R.id.main_app_bar);
        mTextView = findViewById(R.id.chat_window_textView);
        mScrollView = findViewById(R.id.text_Scroll_View);
        mEditText = findViewById(R.id.chat_editText);
        sendBtn = findViewById(R.id.send_text_Btn);
    }
}