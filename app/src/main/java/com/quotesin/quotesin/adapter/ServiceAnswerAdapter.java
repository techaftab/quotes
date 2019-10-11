package com.quotesin.quotesin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.InnerServiceQues_Model;

import java.util.ArrayList;

public class ServiceAnswerAdapter extends RecyclerView.Adapter<ServiceAnswerAdapter.ViewHolder> {

    private AlertDialog.Builder dialogLayout;
    public ArrayList<InnerServiceQues_Model> android;
    private Context context;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

    public int mSelectedItem = -1;


    public ServiceAnswerAdapter(Context mContext, ArrayList<InnerServiceQues_Model> itemModels, AlertDialog.Builder dialogLayout) {
        this.android = itemModels;
        this.context = mContext;
        this.dialogLayout = dialogLayout;
    }

    @NonNull
    @Override
    public ServiceAnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return new ServiceAnswerAdapter.ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull final ServiceAnswerAdapter.ViewHolder holder, final int i) {

        holder.text1.setText(android.get(i).getMul_name());

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
        });

    }

    @Override
    public int getItemCount() {
        return android.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        CheckBox checkBox;


        public ViewHolder(View view) {
            super(view);
            text1 = view.findViewById(R.id.text1);
            checkBox = view.findViewById(R.id.checkbox);
        }
    }
}
