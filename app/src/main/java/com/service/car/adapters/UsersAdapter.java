package com.service.car.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.service.car.R;
import com.service.car.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User> users;
    private Context context;
    private TextView textViewNoUsers;

    public UsersAdapter(List<User> users, Context mcontext, TextView tv_noUsers) {
        this.users = users;
        this.context = mcontext;
        this.textViewNoUsers = tv_noUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_user_childview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.primaryNumber.setText(users.get(position).getPrimaryNumber());
        holder.secondaryNumber.setText(users.get(position).getSecondaryNumber());
        holder.vehicleType.setText(users.get(position).getVehicleType());
        holder.washingType.setText(users.get(position).getWashingType());
        holder.requestDate.setText(users.get(position).getDate());
        holder.userLocation.setText(users.get(position).getUserLocation());
        holder.userMessage.setText(users.get(position).getUserMessage());
    }
    @Override
    public int getItemCount() {
        if (users.size() == 0){
            textViewNoUsers.setVisibility(View.VISIBLE);
        } else {
            textViewNoUsers.setVisibility(View.GONE);
        }
        return users.size();
    }

    //Setting the IDs
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView primaryNumber;
        private TextView secondaryNumber;
        private TextView vehicleType;
        private TextView washingType;
        private TextView userLocation;
        private TextView userMessage;
        private TextView requestDate;

        private ViewHolder(View listItem) {
            super(listItem);
            this.primaryNumber = (TextView) listItem.findViewById(R.id.tv_primaryNumber);
            this.secondaryNumber = (TextView) listItem.findViewById(R.id.tv_secondaryNumber);
            this.vehicleType = (TextView) listItem.findViewById(R.id.tv_vehicleType);
            this.washingType = (TextView) listItem.findViewById(R.id.tv_washingType);
            this.userLocation = (TextView) listItem.findViewById(R.id.tv_userLocation);
            this.userMessage = (TextView) listItem.findViewById(R.id.tv_userMessage);
            this.requestDate = (TextView) listItem.findViewById(R.id.tv_requestDate);
        }
    }
}
