package com.example.privatechat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.privatechat.R;
import com.example.privatechat.activities.GroupChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    private View groupFragment;
    private ListView groupListView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> groupList = new ArrayList<>();
    private DatabaseReference groupRef;
    private FirebaseUser currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        groupFragment = inflater.inflate(R.layout.fragment_group, container, false);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("Groups");

        init();
        retrieveGroupData();
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nameOfGroup = adapterView.getItemAtPosition(i).toString();
                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("GROUP NAMe", nameOfGroup);
                startActivity(groupChatIntent);

            }
        });
        return groupFragment;
    }


    private void init() {
        groupListView = groupFragment.findViewById(R.id.group_listView);
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.group_items, R.id.groupItem_nameText, groupList);
        groupListView.setAdapter(arrayAdapter);
    }

    private void retrieveGroupData() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = snapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }
                groupList.clear();
                groupList.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}