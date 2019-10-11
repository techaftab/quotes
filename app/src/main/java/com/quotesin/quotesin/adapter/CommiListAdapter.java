package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.CommisListResponse;

import java.util.List;

public class CommiListAdapter extends RecyclerView.Adapter<CommiListAdapter.ViewHolder> {

    public List<CommisListResponse.Result> android;
    private Context context;
    public static String total;

    public CommiListAdapter(FragmentActivity activity, List<CommisListResponse.Result> result) {
        this.android = result;
        this.context = activity;
    }

    @NonNull
    @Override
    public CommiListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_histry, parent, false);
        return new CommiListAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final CommiListAdapter.ViewHolder holder, int i) {

        holder.tvTitle.setText(android.get(i).quote.enquiry.enquiryTitle);
        holder.tvQuotePrice.setText("Quote price : $" + android.get(i).quote.quotePrice);
        holder.tvPrice.setText("$" + android.get(i).commissionAmount);
        holder.tvName.setText(android.get(i).consumerUsername + " " + android.get(i).acceptDate);

        if (!TextUtils.isEmpty(android.get(i).serviceStatus) && !TextUtils.isEmpty(android.get(i).paidStatus))
            if (android.get(i).serviceStatus.equalsIgnoreCase("1") && android.get(i).paidStatus.equalsIgnoreCase("0")) {
                holder.tvStatus.setTextColor(Color.parseColor("#ff0000"));
                holder.tvStatus.setText("Outstanding");
            } else if (android.get(i).serviceStatus.equalsIgnoreCase("1")) {
                holder.tvStatus.setTextColor(Color.parseColor("#ffA500"));
                holder.tvStatus.setText("Completed");
            } else {
                holder.tvStatus.setTextColor(Color.parseColor("#fac917"));
                holder.tvStatus.setText("Awaiting Confirmation");
            }
    }

    @Override
    public int getItemCount() {
        return android.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPrice, tvStatus, tvQuotePrice, tvName, tvTitle;


        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvQuotePrice = view.findViewById(R.id.tvQuotePrice);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvPrice = view.findViewById(R.id.tvPrice);

        }
    }
}
