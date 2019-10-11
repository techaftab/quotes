package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.quotesin.quotesin.R;

import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.fragments.AfterAcceptConsDetail;

import com.quotesin.quotesin.fragments.RQuotes_Detail;
import com.quotesin.quotesin.model.SendQuotesModel;
import com.quotesin.quotesin.utils.AppData;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.quotesin.quotesin.fragments.Indox.Current;

public class Send_Quotes extends RecyclerView.Adapter<Send_Quotes.ViewHolder> {
    public ArrayList<SendQuotesModel> android;
    ArrayList<SendQuotesModel> sortedData;
    private Context context;
    private String letter;
    private Date date1;
    private String won_status;

    public Send_Quotes(Context context, ArrayList<SendQuotesModel> android) {
        this.android = android;
        this.context = context;
        this.sortedData = new ArrayList<SendQuotesModel>();     //spare Arraylist to store sorted data
        this.sortedData.addAll(android);
    }

    @NonNull
    @Override
    public Send_Quotes.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rec_quotes, viewGroup, false);
        return new Send_Quotes.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Send_Quotes.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.tvHeading.setText(android.get(i).getEnquiry_title());
        viewHolder.tvCmpyName.setText(android.get(i).getConsumer_username());
        viewHolder.tvDate.setText(android.get(i).getQuote_date().substring(0, 11));
        String enquiry_deadline = android.get(i).getEnquiry_expired();

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date1 = sdf.parse(enquiry_deadline);

        } catch (Exception e) {
            e.printStackTrace();
        }


        String Enquiry = android.get(i).getEnquiry_status();
        String count = android.get(i).getQ_count();
        //  Log.e("Enquiry", Enquiry);

        if (!TextUtils.isEmpty(Enquiry))
            if (!Enquiry.equals("")) {
                if (Enquiry.equals("0") && count.equals("0")) {
                    viewHolder.tvStatus.setText("Send a Quote");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FFC000"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FFC000"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#FFC000"));
                    viewHolder.letter.setImageDrawable(drawable);

                } else if (!count.equals("0") && Enquiry.equals("0")) {
                    viewHolder.tvStatus.setText("Quote Send");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FF6868"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FF6868"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#FF6868"));
                    viewHolder.letter.setImageDrawable(drawable);

                    AppData.getOurInstance().setReplyflag("0");

                } else if (Enquiry.equals("1")) {
                    viewHolder.tvStatus.setText("Accepted");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#5fc825"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#5fc825"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#5fc825"));
                    viewHolder.letter.setImageDrawable(drawable);
                } else if (Enquiry.equals("2")) {
                    viewHolder.tvStatus.setText("Cancelled");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#FF6868"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FF6868"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#FF6868"));
                    viewHolder.letter.setImageDrawable(drawable);

                    AppData.getOurInstance().setReplyflag("1");

                } else if (Enquiry.equals("3")) {
                    viewHolder.tvStatus.setText("Completed");
                    viewHolder.tvStatus.setTextColor(Color.parseColor("#A6A6A6"));
                    viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#A6A6A6"));

                    letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, Color.parseColor("#A6A6A6"));
                    viewHolder.letter.setImageDrawable(drawable);
                }

                if (!Enquiry.equalsIgnoreCase("1") && !Enquiry.equalsIgnoreCase("2") && (!Enquiry.equals("0") && !count.equals("0")))
                    if (date1.before(Current)) {
                        viewHolder.tvStatus.setText("Time out");
                        viewHolder.tvStatus.setTextColor(Color.parseColor("#BF360C"));
                        viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#BF360C"));

                        letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRound(letter, Color.parseColor("#BF360C"));
                        viewHolder.letter.setImageDrawable(drawable);

                        Enquiry = "timeout";

                        AppData.getOurInstance().setReplyflag("1");
                    }
            }
   /*     won_status= android.get(i).getQ_won();

        Log.e("Send_Quote","won_status--"+won_status);
        Log.e("Send_Quote","getQuote_status--"+android.get(i).getQuote_status());*/


        if (Enquiry.equals("1")) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    HomeScreen activity = (HomeScreen) view.getContext();
                    AfterAcceptConsDetail contactInboxDe = new AfterAcceptConsDetail();
                    bundle.putString("eid", android.get(i).getEnquiry_id());
                    bundle.putString("username", android.get(i).getConsumer_username());
                    bundle.putString("desc", android.get(i).getQuote_message());
                    bundle.putString("deadline", android.get(i).getEnquiry_expired());
                    bundle.putString("tittle", android.get(i).getEnquiry_title());
                    bundle.putString("location_code", android.get(i).getLocation_code());
                    bundle.putString("con_email", android.get(i).getConsumer_email());
                    bundle.putString("post_date", " ");               //
                    bundle.putString("qid", " ");
                    bundle.putString("enq_logo", android.get(i).getEnquiry_Image());
                    bundle.putString("fbflag", " ");
                    contactInboxDe.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, contactInboxDe).addToBackStack(null).commit();

                }
            });

        } else {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    HomeScreen activity = (HomeScreen) view.getContext();
                    RQuotes_Detail rQuotesDetail = new RQuotes_Detail();
                    bundle.putString("eid", android.get(i).getEnquiry_id());
                    bundle.putString("username", android.get(i).getConsumer_username());
                    bundle.putString("desc", android.get(i).getEnquiry_description());
                    bundle.putString("deadline", android.get(i).getEnquiry_expired());
                    bundle.putString("tittle", android.get(i).getEnquiry_title());
                    bundle.putString("location_code", android.get(i).getLocation_code());
                    bundle.putString("con_email", android.get(i).getConsumer_email());
                    bundle.putString("post_date", android.get(i).getEnquiry_post_date());
                    bundle.putString("b_name", android.get(i).getBusiness_username());
                    bundle.putString("enq_logo", android.get(i).getEnquiry_Image());
                    bundle.putString("delete_flag", "no");
                    bundle.putString("userFlag", "me");

                    if (android.get(i).getEnquiry_status().equals("1")) {
                        AppData.getInstance().setUpdate_flag("no");
                    } else {
                        bundle.putString("update_visFlag", "yes");
                    }

                    bundle.putString("qid", android.get(i).getId());
                    Log.e("Send_Quotes", "id" + android.get(i).getEnquiry_id());
                    rQuotesDetail.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, rQuotesDetail).addToBackStack(null).commit();


                }
            });
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        android.clear();
        if (charText.length() == 0) {
            android.addAll(sortedData);
        } else {
            for (SendQuotesModel wp : sortedData) {
                if (wp != null) {

                    if (wp.getEnquiry_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                        android.add(wp);
                    } else if (wp.getConsumer_username().toLowerCase(Locale.getDefault()).contains(charText)) {
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