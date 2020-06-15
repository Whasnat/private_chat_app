package com.example.privatechat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private ViewPager myViewPage;
    private TabLayout myTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewPage = (ViewPager) findViewById(R.id.main_tabs_pager);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.main_page_bar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("Private Chat");
    }

}