package com.quotesin.quotesin.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.quotesin.quotesin.R;

import java.util.ArrayList;


public class ServiceQue_CheckboxAdapter extends RecyclerView.Adapter<ServiceQue_CheckboxAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ServiceQue_CheckboxAdapter> listState;
    private ServiceQue_CheckboxAdapter myAdapter;
    private boolean isFromView = false;


    public ServiceQue_CheckboxAdapter(Context context, int resource, ArrayList<ServiceQue_CheckboxAdapter> listVOs) {
        this.mContext = context;
        this.listState = (ArrayList<ServiceQue_CheckboxAdapter>) listVOs;
        this.myAdapter = this;
    }

    @NonNull
    @Override
    public ServiceQue_CheckboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return new ServiceQue_CheckboxAdapter.ViewHolder(view);
    }


    public void onBindViewHolder(final ServiceQue_CheckboxAdapter.ViewHolder holder, int i) {
/*
        holder.text1.setText(android.get(i).getCity_name());

        holder.checkBox.setChecked(android.get(i).isSelected());
        holder.checkBox.setTag(i);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkBox.getTag();
                if (android.get(pos).isSelected()) {
                    android.get(pos).setSelected(false);
                } else {
                    android.get(pos).setSelected(true);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;
        public CheckBox checkBox;


        public ViewHolder(View view) {
            super(view);
            text1 = view.findViewById(R.id.text1);
            checkBox = view.findViewById(R.id.checkbox);
        }
    }
}
