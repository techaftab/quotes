package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quotesin.quotesin.R;

import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.fragments.Enquiry_Reply;
import com.quotesin.quotesin.model.QuotesDescModel;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class QuotesDescAdapter extends RecyclerView.Adapter<QuotesDescAdapter.ViewHolder> {
    public ArrayList<QuotesDescModel> models;
    private Context context;
    String status;
    String visibleFlag = "0";


    public QuotesDescAdapter(Context context, ArrayList<QuotesDescModel> models, String status) {
        this.models = models;
        this.context = context;
        this.status = status;
    }

    @NonNull
    @Override
    public QuotesDescAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quotes_desc, parent, false);
        return new QuotesDescAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final QuotesDescAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tvSubject.setText(models.get(i).getSubject());
        viewHolder.tvDate.setText("Date : " + models.get(i).getQuote_date().substring(0, 10));
        viewHolder.tvB_name.setText(models.get(i).getBusiness_username());
        viewHolder.tvPrice.setText("$ " + models.get(i).getQuote_price());
        viewHolder.tvQuotePrice.setText("$ " + models.get(i).getQuote_price());
        viewHolder.tvTitle.setText(models.get(i).getSubject());
        viewHolder.tvCompName.setText(models.get(i).getBusiness_username());
        viewHolder.tvRecieveDate.setText(models.get(i).getCreateDate().substring(0, 10).replace("-", "/"));

        Picasso.get().load("http://dev.webmobril.services/quotesinapp/" + models.get(i).getBusiness_logo()).into(viewHolder.ivLogo);

        viewHolder.tvDiscription.setText(models.get(i).getQuote_message());
        viewHolder.tvRate.setText(models.get(i).getQ_occur());
        viewHolder.tvReady.setText(models.get(i).getQ_start());
        viewHolder.tvPriceDesc.setText(models.get(i).getQ_price_type());

        viewHolder.tvPriceDesc.setText(models.get(i).getQ_price_type());


        viewHolder.RlUpdate.setVisibility(View.VISIBLE);
        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) v.getContext();
                Enquiry_Reply enquiryReply = new Enquiry_Reply();
                bundle.putString("mail_id", models.get(i).getE_id());
                bundle.putString("update_quote", "update");
                enquiryReply.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, enquiryReply).addToBackStack(null).commit();
            }
        });


        if (!TextUtils.isEmpty(models.get(i).getQuote_image())) {
            String str = models.get(i).getQuote_image();
            Log.e("str", str);

            String str1 = str.substring(0, 4);
            Log.e("str", str1);

            if (str1.equals("site")) {
                viewHolder.ivDocument.setVisibility(View.VISIBLE);
                viewHolder.ivDocument.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String url = "http://dev.webmobril.services/quotesinapp/" + models.get(i).getQuote_image();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                });
            } else {
                viewHolder.ivDocument.setVisibility(View.GONE);
            }
        }


        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibleFlag.equals("0")) {
                    viewHolder.linearLayout.setVisibility(View.VISIBLE);
                    visibleFlag = "1";
                } else {
                    viewHolder.linearLayout.setVisibility(View.GONE);
                    visibleFlag = "0";
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubject, tvB_name, tvPrice, tvDate, tvPriceDesc, tvRate, tvReady, tvDiscription, tvQuotePrice, tvTitle, tvCompName, tvRecieveDate;
        LinearLayout LLAccept, linearLayout;
        RelativeLayout RlUpdate;
        Button btnAccept, tvReject;
        Button btn_update;
        RelativeLayout RlFeedback;
        ImageView ivDocument, ivLogo;
        RelativeLayout relativeLayout;


        public ViewHolder(View view) {
            super(view);

            //  LlDeatail = view.findViewById(R.id.Ll);
            tvRecieveDate = view.findViewById(R.id.tvRecieveDate);
            tvCompName = view.findViewById(R.id.tvCompName);
            ivLogo = view.findViewById(R.id.ivLogo);
            ivDocument = view.findViewById(R.id.ivDocument);
            tvPriceDesc = view.findViewById(R.id.tvPriceDesc);
            tvRate = view.findViewById(R.id.tvRate);
            tvReady = view.findViewById(R.id.tvReady);
            tvDiscription = view.findViewById(R.id.tvDiscription);
            tvQuotePrice = view.findViewById(R.id.tvQuotePrice);
            tvTitle = view.findViewById(R.id.tvTitle);

            tvSubject = view.findViewById(R.id.tvSubject);
            tvB_name = view.findViewById(R.id.tvB_name);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvDate = view.findViewById(R.id.tvDate);
            LLAccept = view.findViewById(R.id.LLAccept);
            btnAccept = view.findViewById(R.id.btnAccept);
            tvReject = view.findViewById(R.id.btnReject);
            RlFeedback = view.findViewById(R.id.RlFeedback);
            btn_update = view.findViewById(R.id.btn_update);
            RlUpdate = view.findViewById(R.id.RlUpdate);

            linearLayout = view.findViewById(R.id.linearLayout);
            relativeLayout = view.findViewById(R.id.relativeLayout);

            LLAccept.setVisibility(View.GONE);
            RlFeedback.setVisibility(View.GONE);

            if (linearLayout.getVisibility() == View.VISIBLE) {

                RlUpdate.setVisibility(View.VISIBLE);
                btn_update.setVisibility(View.VISIBLE);

                if (status.equals("1")) {
                    btn_update.setVisibility(View.GONE);
                } else {
                    btn_update.setVisibility(View.VISIBLE);
                }
            }


        }

    }
}