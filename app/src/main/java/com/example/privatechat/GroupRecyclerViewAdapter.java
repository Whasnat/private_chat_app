package com.example.privatechat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> groupImageItems;
    private ArrayList<String> groupNameItems;
    private Context context;
    private String TAG;

    public GroupRecyclerViewAdapter(ArrayList<String> groupImageItems, ArrayList<String> groupNameItems, Context context) {
        this.groupImageItems = groupImageItems;
        this.groupNameItems = groupNameItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //holder.groupImage.setImageBitmap(groupImageItems.get());
        holder.groupName.setText(groupImageItems.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on " + groupImageItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupImageItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImage;
        TextView groupName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.groupItem_imageView);
            groupName = itemView.findViewById(R.id.groupItem_nameText);
            parentLayout = itemView.findViewById(R.id.groupItem_parentLayout);

        }
    }
}
