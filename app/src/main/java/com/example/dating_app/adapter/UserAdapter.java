package com.example.dating_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dating_app.R;
import com.example.dating_app.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.ageTextView.setText(String.valueOf(user.getAge()));
        holder.distanceTextView.setText(user.getDistance() + " km");

        Glide.with(holder.itemView.getContext())
                .load(user.getImageUrl())
                .into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView ageTextView;
        TextView distanceTextView;
        ImageView profileImageView;

        public UserViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvName);
            ageTextView = itemView.findViewById(R.id.tvAge);
            distanceTextView = itemView.findViewById(R.id.tvDistance);
            profileImageView = itemView.findViewById(R.id.ivProfileImage);
        }
    }
}
