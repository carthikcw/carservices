package com.service.car.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.service.car.R;
import com.service.car.models.User;
import com.service.car.utils.ClickListener;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User> users;
    private Context context;
    private TextView textViewNoUsers;
    private String[] datetime;
    private ClickListener clickListener;

    public UsersAdapter(List<User> users, Context mcontext, TextView tv_noUsers,ClickListener clickListener) {
        this.users = users;
        this.context = mcontext;
        this.textViewNoUsers = tv_noUsers;
        this.clickListener = clickListener;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.primaryNumber.setText("Contact " + users.get(position).getPrimaryNumber());
        holder.secondaryNumber.setText("Alternate Number"+users.get(position).getSecondaryNumber());
        holder.vehicleType.setText("Vehicle Type "+ users.get(position).getVehicleType());
        holder.washingType.setText("Washing Type  "+users.get(position).getWashingType());
        holder.requestDate.setText("Date - Time "+(users.get(position).getDate()).replace("T"," - "));
        holder.userLocation.setText(users.get(position).getUserLocation());
        holder.userMessage.setText(users.get(position).getUserMessage());

        holder.primaryNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(context,position,users.get(position).getPrimaryNumber());
            }
        });
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

            this.primaryNumber.setPaintFlags(this.primaryNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
