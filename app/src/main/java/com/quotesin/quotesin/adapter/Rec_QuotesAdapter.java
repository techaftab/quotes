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
import com.quotesin.quotesin.fragments.LeaveFeedBAck;
import com.quotesin.quotesin.model.QuotesDescModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.GetMyItem;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Rec_QuotesAdapter extends RecyclerView.Adapter<Rec_QuotesAdapter.ViewHolder> {
    private ArrayList<QuotesDescModel> models;
    private Context context;
    private GetMyItem getMyItem;
    private String update_show;
    private String visibleFlag = "0";
    private String firstTimeVisi = "0";

    public Rec_QuotesAdapter(Context context, ArrayList<QuotesDescModel> models, GetMyItem getMyItem, String update_visFlag) {
        this.models = models;
        this.context = context;
        this.getMyItem = getMyItem;
        this.update_show = update_visFlag;

    }

    @NonNull
    @Override
    public Rec_QuotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_send_qdesc, parent, false);
        return new Rec_QuotesAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final Rec_QuotesAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        viewHolder.tv1.setText(models.get(i).getSubject());
        Picasso.get().load(models.get(i).getBusiness_logo()).placeholder(R.mipmap.avatar_male).error(R.mipmap.avatar_male).into(viewHolder.ivUserImage);
        viewHolder.tvCompName.setText(models.get(i).getBusiness_username());

        viewHolder.tvSubject.setText(models.get(i).getSubject());
        viewHolder.tvDate.setText("Date : " + models.get(i).getQuote_date().substring(0, 10));
        viewHolder.tvB_name.setText(models.get(i).getBusiness_username());
        viewHolder.tvPrice.setText("$ " + models.get(i).getQuote_price());
        viewHolder.tvQuotePrice.setText("$ " + models.get(i).getQuote_price());

        viewHolder.tvDiscription.setText(models.get(i).getQuote_message());
        viewHolder.tvRate.setText(models.get(i).getQ_occur());
        viewHolder.tvReady.setText(models.get(i).getQ_start());
        viewHolder.tvPriceDesc.setText(models.get(i).getQ_price_type());
        viewHolder.tvRecieveDate.setText(models.get(i).getQuote_date().substring(0, 10).replace("-", "/"));

        Log.e("q_won_date", models.get(i).getQuote_won_date());
        Log.e("name--", models.get(i).getBusiness_username());


        if (!TextUtils.isEmpty(models.get(i).getQuote_image())) {
            final String str = models.get(i).getQuote_image();
            Log.e("str", str);

            String str1 = str.substring(0, 4);
            Log.e("str", str1);

            if (str1.equals("site")) {
                viewHolder.ivQuoteImage.setVisibility(View.VISIBLE);

                viewHolder.ivQuoteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = APIUrl.IMAGE_BASE_URL + str;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                });

            } else {
                viewHolder.ivQuoteImage.setImageResource(R.mipmap.no_attach);
            }
        }


        if (LoginPreferences.getActiveInstance(context).getRole_id().equals("5")) {
            if (!TextUtils.isEmpty(AppData.getInstance().getUpdate_flag())) {
                if (!AppData.getInstance().getUpdate_flag().equals("no")) {
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
                } else if (!TextUtils.isEmpty(update_show)) {
                    if (update_show.equals("yes"))
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
                }
            }
        }

        String string = models.get(i).getQuote_won_date();
        String newString = string.substring(0, 1);
        Log.e("ssss", newString);

        if (!TextUtils.isEmpty(AppData.getInstance().getUpd_rej_flag()))
            if (!AppData.getInstance().getUpd_rej_flag().equals("no"))
                if (newString.equals("2")) {
                    viewHolder.RlFeedback.setVisibility(View.VISIBLE);
                    viewHolder.LLAccept.setVisibility(View.GONE);

                    viewHolder.btn_Feedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            HomeScreen activity = (HomeScreen) v.getContext();
                            LeaveFeedBAck enquiryReply = new LeaveFeedBAck();
                            bundle.putString("qid", models.get(i).getQ_id());
                            bundle.putString("price", models.get(i).getQuote_price());

                            enquiryReply.setArguments(bundle);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, enquiryReply).addToBackStack(null).commit();
                        }

                    });

                } else {
                    viewHolder.LLAccept.setVisibility(View.VISIBLE);
                    viewHolder.RlFeedback.setVisibility(View.GONE);
                }

        viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyItem.GetClickedItem(models.get(i).getQ_id(), "accept");
                AppData.getInstance().setQid(models.get(i).getQ_id());
                AppData.getInstance().setBname(models.get(i).getBusiness_username());
                AppData.getInstance().setEid(models.get(i).getE_id());
                AppData.getInstance().setQamt(models.get(i).getQuote_price());

            }
        });

        viewHolder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyItem.GetClickedItem(models.get(i).getQ_id(), "reject");
            }
        });

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibleFlag.equals("0")) {
                    viewHolder.linearLayout.setVisibility(View.VISIBLE);
                    visibleFlag = "1";

                    getMyItem.GetClickedItem(models.get(i).getQ_id(), "read_quote");

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
        TextView tvRecieveDate, tv1, tvCompName, tvQuotePrice, tvSubject, tvB_name, tvPrice, tvDate, tvPriceDesc, tvRate, tvReady, tvDiscription;
        LinearLayout LLAccept, linearLayout;
        Button btnAccept, btnReject;
        RelativeLayout RlUpdate, RlFeedback;
        Button btn_update, btn_Feedback;
        RelativeLayout relativeLayout;

        ImageView ivQuoteImage, ivUserImage;

        public ViewHolder(View view) {
            super(view);

            relativeLayout = view.findViewById(R.id.relativeLayout);
            linearLayout = view.findViewById(R.id.linearLayout);
            tv1 = view.findViewById(R.id.tvTitle);
            tvPriceDesc = view.findViewById(R.id.tvPriceDesc);
            tvRate = view.findViewById(R.id.tvRate);
            tvReady = view.findViewById(R.id.tvReady);
            tvDiscription = view.findViewById(R.id.tvDiscription);
            tvQuotePrice = view.findViewById(R.id.tvQuotePrice);
            tvSubject = view.findViewById(R.id.tvSubject);
            tvB_name = view.findViewById(R.id.tvB_name);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvDate = view.findViewById(R.id.tvDate);
            LLAccept = view.findViewById(R.id.LLAccept);
            btnAccept = view.findViewById(R.id.btnAccept);
            btnReject = view.findViewById(R.id.btnReject);

            RlUpdate = view.findViewById(R.id.RlUpdate);
            btn_update = view.findViewById(R.id.btn_update);
            btn_Feedback = view.findViewById(R.id.btn_Feedback);
            RlFeedback = view.findViewById(R.id.RlFeedback);

            ivQuoteImage = view.findViewById(R.id.ivDocument);
            ivUserImage = view.findViewById(R.id.ivUserImage);
            tvCompName = view.findViewById(R.id.tvCompName);
            tvRecieveDate = view.findViewById(R.id.tvRecieveDate);


            if (!TextUtils.isEmpty(update_show)) {
                Log.e("update_show==", update_show);
                if (update_show.equals("yes")) {
                    RlUpdate.setVisibility(View.VISIBLE);
                    btn_update.setVisibility(View.VISIBLE);
                }
            }


            Log.e(AppData.getInstance().getUpd_rej_flag(), "accept_rej");
            if (!TextUtils.isEmpty(AppData.getInstance().getUpd_rej_flag()))
                if (AppData.getInstance().getUpd_rej_flag().equals("no")) {
                    LLAccept.setVisibility(View.GONE);
                } else {
                    LLAccept.setVisibility(View.VISIBLE);
                }
        }
    }
}