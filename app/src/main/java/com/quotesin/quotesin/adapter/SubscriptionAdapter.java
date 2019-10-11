package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.SubscriptionModel;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.InItemClickListener;

import java.util.ArrayList;


public class SubscriptionAdapter extends RecyclerView.Adapter{

    private String TAG ="SubscriptionAdapter";
    private Context context;
    private static ArrayList<SubscriptionModel> planModelArrayList;
    private InItemClickListener cityListingClickListener;

    public SubscriptionAdapter(Activity activity, ArrayList<SubscriptionModel> planModelArrayList,InItemClickListener cityListingClickListener) {
        this.context = activity;
        this.planModelArrayList = planModelArrayList;
        this.cityListingClickListener = cityListingClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subsc_plans, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder type_item = (ViewHolder) holder;
        final SubscriptionModel cityItem = planModelArrayList.get(position);


        type_item.tvLeadsno.setText(cityItem.getSub_title());
        type_item.tvDesc.setText(cityItem.getDescription());

        type_item.tvName.setText(cityItem.getName());
        type_item.tv_signupfree.setText("Get  "+cityItem.getName()+"  Membership ");

        type_item.tv_signupfree.setOnClickListener(v -> {
            AppData.getInstance().setSubsc_id(cityItem.getId());
            AppData.getInstance().setSubsc_amt(cityItem.getPrice());
            AppData.getInstance().setSubsc_name(cityItem.getName());

            Log.e(TAG,"ID in on click"+cityItem.getId());
            Log.e(TAG,"Amt in on click"+cityItem.getPrice());

            if (cityListingClickListener != null)
                cityListingClickListener.onItemPurchaseClick(v, type_item.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return planModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLeadsno, tvDesc , tvName, tv_signupfree ;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvLeadsno =  itemView.findViewById(R.id.tvLeadsno);
            this.tvDesc =  itemView.findViewById(R.id.tvDesc);
            this.tvName =  itemView.findViewById(R.id.tvName);
            this.tv_signupfree =  itemView.findViewById(R.id.tv_signupfree);

        }
    }

}
