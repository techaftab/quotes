package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.fragments.AfterAcceptBussDetail;
import com.quotesin.quotesin.fragments.RQuotes_Detail;
import com.quotesin.quotesin.model.SendEnquiryModel;
import com.quotesin.quotesin.utils.AppData;

import java.util.ArrayList;
import java.util.Locale;

public class Send_Enquiry extends RecyclerView.Adapter<Send_Enquiry.ViewHolder> {
    public ArrayList<SendEnquiryModel> android;
    private Context context;
    private String letter;

    private ArrayList<SendEnquiryModel> sortedData;
    private String won_status;

    public Send_Enquiry(Context context, ArrayList<SendEnquiryModel> android) {
        this.android = android;
        this.context = context;
        this.sortedData= new ArrayList<>();
        this.sortedData.addAll(android);
    }

    @NonNull
    @Override
    public Send_Enquiry.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_indox_enquiries, viewGroup, false);
        return new Send_Enquiry.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Send_Enquiry.ViewHolder viewHolder, final int i) {
        viewHolder.tvHeading.setText(android.get(i).getEnquiry_title());
        viewHolder.tvCmpyName.setText(android.get(i).getConsumer_username());
        viewHolder.tvDate.setText(android.get(i).getEnquiry_expired());

        String Enquiry = android.get(i).getEnquiry_status();
        Log.e("Enquiry",Enquiry);
        if (!Enquiry.equals(""))
        {
            switch (Enquiry) {
                case "0": {
                    viewHolder.tvStatus.setText("Awaiting Quotes");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FFC000"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FFC000"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#FFC000"));
                    viewHolder.letter.setImageDrawable(drawable);

                    break;
                }
                case "1": {
                    viewHolder.tvStatus.setText("Accepted");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#5fc825"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#5fc825"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#5fc825"));
                    viewHolder.letter.setImageDrawable(drawable);

                    break;
                }
                case "2": {
                    viewHolder.tvStatus.setText("Cancelled");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FF6868"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FF6868"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#FF6868"));
                    viewHolder.letter.setImageDrawable(drawable);
                    break;
                }
                case "3": {
                    viewHolder.tvStatus.setText("Deadline Reached");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FFC000"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FFC000"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#FFC000"));
                    viewHolder.letter.setImageDrawable(drawable);

                    break;
                }
                case "4": {
                    viewHolder.tvStatus.setText("Complete");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#A6A6A6"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#A6A6A6"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#A6A6A6"));
                    viewHolder.letter.setImageDrawable(drawable);

                    break;
                }
                case "5": {
                    viewHolder.tvStatus.setText("Action Needed");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#6FA2FF"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#6FA2FF"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#6FA2FF"));
                    viewHolder.letter.setImageDrawable(drawable);
                    break;
                }
            }
        }


        Log.e("send_en","en_status"+android.get(i).getEnquiry_status());

        if (Enquiry.equals("1")) {
            viewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) view.getContext();
                AfterAcceptBussDetail contactQuoteDet = new AfterAcceptBussDetail();
                bundle.putString("eid", android.get(i).getEid());
                bundle.putString("username", android.get(i).getConsumer_username());
                bundle.putString("desc", android.get(i).getEnquiry_description());
                bundle.putString("deadline", android.get(i).getEnquiry_expired());
                bundle.putString("tittle", android.get(i).getEnquiry_title());
                bundle.putString("location_code", android.get(i).getLocation_code());
                bundle.putString("con_email", android.get(i).getConsumer_email());
                bundle.putString("post_date", " ");               //
                bundle.putString("qid", " ");
                bundle.putString("enq_logo", android.get(i).getEnquiry_Image());
                bundle.putString("fbflag", " ");
                contactQuoteDet.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, contactQuoteDet).addToBackStack(null).commit();

            });

        } else  {
            viewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) view.getContext();
                RQuotes_Detail rQuotesDetail = new RQuotes_Detail();
                bundle.putString("eid", android.get(i).getEid());
                bundle.putString("username", android.get(i).getConsumer_username());
                bundle.putString("desc", android.get(i).getEnquiry_description());
                bundle.putString("deadline", android.get(i).getEnquiry_expired());
                bundle.putString("tittle", android.get(i).getEnquiry_title());
                bundle.putString("location_code", android.get(i).getLocation_code());
                bundle.putString("con_email", android.get(i).getConsumer_email());
                bundle.putString("delete_flag", "yes");
                bundle.putString("enq_logo", android.get(i).getEnquiry_Image());
                bundle.putString("post_date", android.get(i).getEnquiry_post_date());
                bundle.putString("userFlag", "other");
                rQuotesDetail.setArguments(bundle);

                AppData.getInstance().setUpd_rej_flag("no");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, rQuotesDetail).addToBackStack(null).commit();


            });

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        android.clear();
        if (charText.length() == 0) {
            android.addAll(sortedData);
        } else {
            for (SendEnquiryModel wp : sortedData) {
                if (wp!=null){

                    if (wp.getEnquiry_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                        android.add(wp);
                    }else if (wp.getConsumer_username().toLowerCase(Locale.getDefault()).contains(charText)){
                        android.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return android.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeading;
        TextView tvStatus, tvCmpyName, tvDate, tvSizeColor;
        ImageView letter;


        public ViewHolder(View view) {
            super(view);
            tvHeading = view.findViewById(R.id.tvHeading);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvCmpyName = view.findViewById(R.id.tvCmpyName);
            tvDate = view.findViewById(R.id.tvDate);
            tvSizeColor = view.findViewById(R.id.tvSizeColor);
            letter = view.findViewById(R.id.gmailitem_letter);
        }
    }

}