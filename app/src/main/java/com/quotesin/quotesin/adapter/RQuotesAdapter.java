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
import com.quotesin.quotesin.fragments.AfterAcceptBussDetail;
import com.quotesin.quotesin.fragments.RQuotes_Detail;
import com.quotesin.quotesin.model.RQuotesModel;
import com.quotesin.quotesin.utils.AppData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.quotesin.quotesin.fragments.Indox.Current;

public class RQuotesAdapter extends RecyclerView.Adapter<RQuotesAdapter.ViewHolder> {
    public ArrayList<RQuotesModel> android;
    private Context context;
    private String letter, quote_deadline;

    private ArrayList<RQuotesModel> sortedData;
    private Date date1;

    public RQuotesAdapter(Context context, ArrayList<RQuotesModel> android) {
        this.android = android;
        this.context = context;
        this.sortedData = new ArrayList<>();
        this.sortedData.addAll(android);
    }

    @NonNull
    @Override
    public RQuotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rec_quotes, viewGroup, false);
        return new RQuotesAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RQuotesAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        viewHolder.tvHeading.setText(android.get(i).getQuotes_title());
        viewHolder.tvDate.setText(android.get(i).getQuotes_expired());
        String Enquiry = android.get(i).getQuotes_status();
        quote_deadline = android.get(i).getQuotes_expired();
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date1 = sdf.parse(quote_deadline);

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!TextUtils.isEmpty(android.get(i).getUnreadTot())) {
            viewHolder.tvCmpyName.setText(android.get(i).getqCount() + "(" + android.get(i).getUnreadTot() + " new)");

            Log.e("unreadTot", android.get(i).getUnreadTot());
        }


        Log.e("Enquiry", Enquiry);
        if (!Enquiry.equals("")) {
            if (Enquiry.equals("0") && date1.before(Current)) {
                viewHolder.tvStatus.setText("Time out");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#FF6868"));
                viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FF6868"));

                letter = String.valueOf(android.get(i).getQuotes_title().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, Color.parseColor("#FF6868"));
                viewHolder.letter.setImageDrawable(drawable);

            } else if (Enquiry.equals("0")) {
                viewHolder.tvStatus.setText("Awaiting Quotes");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#FFC000"));
                viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FFC000"));

                letter = String.valueOf(android.get(i).getQuotes_title().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, Color.parseColor("#FFC000"));
                viewHolder.letter.setImageDrawable(drawable);

            } else if (Enquiry.equals("1")) {
                viewHolder.tvStatus.setText("Accepted");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#5fc825"));
                viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#5fc825"));

                letter = String.valueOf(android.get(i).getQuotes_title().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, Color.parseColor("#5fc825"));
                viewHolder.letter.setImageDrawable(drawable);

            } else if (Enquiry.equals("2")) {
                viewHolder.tvStatus.setText("Cancelled");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#FF6868"));
                viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FF6868"));

                letter = String.valueOf(android.get(i).getQuotes_title().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, Color.parseColor("#FF6868"));
                viewHolder.letter.setImageDrawable(drawable);

            } else if (Enquiry.equals("3")) {
                viewHolder.tvStatus.setText("Quote Completed");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#A6A6A6"));
                viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#A6A6A6"));

                letter = String.valueOf(android.get(i).getQuotes_title().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, Color.parseColor("#A6A6A6"));
                viewHolder.letter.setImageDrawable(drawable);

            }
        }

        String won_status = android.get(i).getWon_status();


        if (android.get(i).getQuotes_status().equals("1")) {
            viewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) view.getContext();
                AfterAcceptBussDetail contactQuoteDet = new AfterAcceptBussDetail();
                bundle.putString("eid", android.get(i).getEnquiry_id());
                bundle.putString("username", android.get(i).getConsumer_username());
                bundle.putString("desc", android.get(i).getQuotes_desc());
                bundle.putString("deadline", android.get(i).getQuotes_expired());
                bundle.putString("tittle", android.get(i).getQuotes_title());
                bundle.putString("location_code", android.get(i).getLocation_code());
                bundle.putString("con_email", android.get(i).getConsumer_email());
                bundle.putString("post_date", android.get(i).getQuotes_post_date());               //
                bundle.putString("qid", android.get(i).getQuote_id());
                bundle.putString("enq_logo", android.get(i).getEnquiry_logo());
                bundle.putString("fbflag", android.get(i).getFeedbackStatus());
                contactQuoteDet.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, contactQuoteDet)
                        .addToBackStack(null).commit();

            });

        } else if (won_status.equals("0")) {
            viewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) view.getContext();
                RQuotes_Detail rQuotesDetail = new RQuotes_Detail();
                // bundle.putString("qid", android.get(i).getQuote_id());
                bundle.putString("eid", android.get(i).getEnquiry_id());
                bundle.putString("username", android.get(i).getConsumer_username());
                bundle.putString("desc", android.get(i).getQuotes_desc());
                bundle.putString("deadline", android.get(i).getQuotes_expired());
                bundle.putString("tittle", android.get(i).getQuotes_title());
                bundle.putString("location_code", android.get(i).getLocation_code());
                bundle.putString("con_email", android.get(i).getConsumer_email());
                bundle.putString("post_date", android.get(i).getQuotes_post_date());
                bundle.putString("qid", android.get(i).getQuote_id());
                bundle.putString("enq_logo", android.get(i).getEnquiry_logo());
                bundle.putString("delete_flag", "no");
                rQuotesDetail.setArguments(bundle);
                activity.setTitle("Quotes Detail");
                AppData.getInstance().setUpd_rej_flag("yes");
                AppData.getInstance().setUpdate_flag("no");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, rQuotesDetail)
                        .addToBackStack(null).commit();


            });
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        android.clear();
        if (charText.length() == 0) {
            android.addAll(sortedData);
        } else {
            for (RQuotesModel wp : sortedData) {
                if (wp != null) {

                    if (wp.getQuotes_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                        android.add(wp);
                    } else if (wp.getConsumer_username().toLowerCase(Locale.getDefault()).contains(charText)) {
                        android.add(wp);
                    } else if (wp.getQuotes_desc().toLowerCase(Locale.getDefault()).contains(charText)) {
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