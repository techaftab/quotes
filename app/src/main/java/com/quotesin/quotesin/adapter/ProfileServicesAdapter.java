package com.quotesin.quotesin.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.ProfileServicesModel;


import java.util.ArrayList;

public class ProfileServicesAdapter  extends RecyclerView.Adapter<ProfileServicesAdapter.ViewHolder> {

    public ArrayList<ProfileServicesModel> android;
    private Context context;

    public ProfileServicesAdapter(FragmentActivity activity, ArrayList<ProfileServicesModel> servicesModelArrayList) {
        this.android = servicesModelArrayList;
        this.context = activity;
    }

    @NonNull
    @Override
    public ProfileServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile_services, parent, false);
        return new ProfileServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvServiceName.setText(android.get(position).getService_name());
    }


    @Override
    public int getItemCount() {
        return android.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvServiceName;



        public ViewHolder(View view) {
            super(view);
            tvServiceName = view.findViewById(R.id.tvServiceName);

        }
    }
}
