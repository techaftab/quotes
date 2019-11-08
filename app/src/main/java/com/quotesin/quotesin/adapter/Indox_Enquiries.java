package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.fragments.AfterAcceptConsDetail;
import com.quotesin.quotesin.fragments.Inbox_Detail;
import com.quotesin.quotesin.model.Indox_Enquiry_Model;
import com.quotesin.quotesin.utils.AppData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.quotesin.quotesin.fragments.Inbox.Current;


public class Indox_Enquiries extends RecyclerView.Adapter<Indox_Enquiries.ViewHolder> {
    public ArrayList<Indox_Enquiry_Model> android;
    private Context context;
    private String letter, Enquiry_deadline;
    private SparseBooleanArray mSelectedItemsIds;

    ArrayList<Indox_Enquiry_Model> sortedData;
    private Date date1;
    String count, Enquiry;

    public Indox_Enquiries(Context context, ArrayList<Indox_Enquiry_Model> android) {
        this.android = android;
        this.context = context;
        this.sortedData = new ArrayList<>();
        this.sortedData.addAll(android);
        mSelectedItemsIds = new SparseBooleanArray();
    }


    @Override
    public Indox_Enquiries.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_indox_enquiries, viewGroup, false);
        return new Indox_Enquiries.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final Indox_Enquiries.ViewHolder viewHolder, final int i) {
        viewHolder.tvHeading.setText(android.get(i).getEnquiry_title());
        viewHolder.tvCmpyName.setText(android.get(i).getConsumer_username());
        viewHolder.tvDate.setText(android.get(i).getEnquiry_expired().replace("-", "/"));

        Enquiry = android.get(i).getEnquiry_status();
        Enquiry_deadline = android.get(i).getEnquiry_expired();
        count = android.get(i).getQ_count();

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date1 = sdf.parse(Enquiry_deadline);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("Enquiry", Enquiry);

        if (Enquiry.equals("0") && count.equals("0")) {
            viewHolder.tvStatus.setText("Send a Quote");
            viewHolder.tvStatus.setTextColor(Color.parseColor("#168add"));
            viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#168add"));

            letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, Color.parseColor("#168add"));
            viewHolder.letter.setImageDrawable(drawable);


        } else if (Enquiry.equals("0") && !count.equalsIgnoreCase("0")) {
            final int secs = 5;
            new CountDownTimer((secs + 1) * 1000, 1000) // Wait 5 secs, tick every 1 sec
            {
                @Override
                public final void onTick(final long millisUntilFinished) {
                    viewHolder.tvStatus.setText("Action Needed");
                }

                public final void onFinish() {
                    viewHolder.tvStatus.setText("Quote Send");

                }
            }.start();


            viewHolder.tvStatus.setTextColor(Color.parseColor("#FFC000"));
            viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FFC000"));

            letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, Color.parseColor("#FFC000"));
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


        if (Enquiry.equals("0")) {
            viewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) view.getContext();
                Inbox_Detail indoxDetail = new Inbox_Detail();
                bundle.putString("id", android.get(i).getId());
                bundle.putString("username", android.get(i).getConsumer_username());
                bundle.putString("desc", android.get(i).getEnquiry_description());
                bundle.putString("deadline", android.get(i).getEnquiry_expired());
                bundle.putString("tittle", android.get(i).getEnquiry_title());
                bundle.putString("location_code", android.get(i).getLocation_code());
                bundle.putString("enq_logo", android.get(i).getEnquiry_logo());
                bundle.putString("enq_status", android.get(i).getEnquiry_status());
                bundle.putString("q_count", android.get(i).getQ_count());
                bundle.putString("status", "0");
                indoxDetail.setArguments(bundle);
                activity.setTitle("Inbox Detail");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, indoxDetail).addToBackStack(null).commit();

            });
        } else if (Enquiry.equals("2") || Enquiry.equals("timeout")) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    HomeScreen activity = (HomeScreen) view.getContext();
                    Inbox_Detail indoxDetail = new Inbox_Detail();
                    bundle.putString("id", android.get(i).getId());
                    bundle.putString("username", android.get(i).getConsumer_username());
                    bundle.putString("desc", android.get(i).getEnquiry_description());
                    bundle.putString("deadline", android.get(i).getEnquiry_expired());
                    bundle.putString("tittle", android.get(i).getEnquiry_title());
                    bundle.putString("location_code", android.get(i).getLocation_code());
                    bundle.putString("enq_logo", android.get(i).getEnquiry_logo());
                    bundle.putString("enq_status", android.get(i).getEnquiry_status());
                    bundle.putString("status", "1");
                    indoxDetail.setArguments(bundle);
                    activity.setTitle("Inbox Detail");
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, indoxDetail).addToBackStack(null).commit();

                }
            });
        } else {
            viewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) view.getContext();
                AfterAcceptConsDetail contactQuoteDet = new AfterAcceptConsDetail();
                bundle.putString("eid", android.get(i).getId());
                bundle.putString("username", android.get(i).getConsumer_username());
                bundle.putString("desc", android.get(i).getEnquiry_description());
                bundle.putString("deadline", android.get(i).getEnquiry_expired());
                bundle.putString("tittle", android.get(i).getEnquiry_title());
                bundle.putString("location_code", android.get(i).getLocation_code());
                bundle.putString("enq_logo", android.get(i).getEnquiry_logo());
                bundle.putString("enq_status", android.get(i).getEnquiry_status());
                bundle.putString("post_date", android.get(i).getEnquiry_post_date());
                contactQuoteDet.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, contactQuoteDet).addToBackStack(null).commit();

            });
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        android.clear();
        if (charText.length() == 0) {
            android.addAll(sortedData);
        } else {
            for (Indox_Enquiry_Model wp : sortedData) {
                if (wp != null) {
                    if (wp.getEnquiry_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                        android.add(wp);
                    } else if (wp.getConsumer_username().toLowerCase(Locale.getDefault()).contains(charText)) {
                        android.add(wp);
                    } else if (wp.getEnquiry_description().toLowerCase(Locale.getDefault()).contains(charText)) {
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


    public void removeItem(int position) {
        android.remove(position);
        AppData.getInstance().setEid(android.get(position).getId());
        notifyItemRemoved(position);
    }

   /* public void restoreItem(String item, int position) {
        android.add(position, item);
        notifyItemInserted(position);
    }*/


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeading;
        TextView tvStatus, tvCmpyName, tvDate, tvSizeColor;
        ImageView letter;
        LinearLayout Llitem;


        public ViewHolder(View view) {
            super(view);
            tvHeading = view.findViewById(R.id.tvHeading);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvCmpyName = view.findViewById(R.id.tvCmpyName);
            tvDate = view.findViewById(R.id.tvDate);
            tvSizeColor = view.findViewById(R.id.tvSizeColor);
            letter = view.findViewById(R.id.gmailitem_letter);

            Llitem = view.findViewById(R.id.Llitem);

           /* Llitem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Indox_Enquiry_Model.setSelected1(!Indox_Enquiry_Model.isSelected1());
                    Llitem.setBackgroundColor(Indox_Enquiry_Model.isSelected1() ? 0xFFB7DED7 : 0xf1f4f9);

                    if (Indox_Enquiry_Model.isSelected1()){
                        android.get(getAdapterPosition()).setSelected(true);
                    }else {
                        android.get(getAdapterPosition()).setSelected(false);
                    }

                    return true;
                }
            });*/


        }
    }

}