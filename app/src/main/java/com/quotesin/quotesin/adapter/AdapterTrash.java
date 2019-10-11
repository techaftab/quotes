package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.Indox_Enquiry_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.quotesin.quotesin.fragments.Indox.Current;

public class AdapterTrash extends RecyclerView.Adapter<AdapterTrash.ViewHolder> {

    public ArrayList<Indox_Enquiry_Model> android;
    private Context context;
    String letter;
    Date date1;
    private String Enquiry_deadline;

    public AdapterTrash(FragmentActivity activity, ArrayList<Indox_Enquiry_Model> trasharrayList) {
        this.android=trasharrayList;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trash, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvHeading.setText(android.get(i).getEnquiry_title());
        viewHolder.tvCmpyName.setText(android.get(i).getConsumer_username());

        Enquiry_deadline = android.get(i).getEnquiry_expired();

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date1 = sdf.parse(Enquiry_deadline);

        } catch (Exception e) {
            e.printStackTrace();
        }



        String Enquiry = android.get(i).getEnquiry_status();
        String count = android.get(i).getQ_count();
        Log.e("Enquiry", Enquiry);
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

            } else if (Enquiry.equals("1")) {
                viewHolder.tvStatus.setText("Accepted");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#00FF00"));
                viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#00FF00"));

                letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, Color.parseColor("#00FF00"));
                viewHolder.letter.setImageDrawable(drawable);
            } else if (Enquiry.equals("2")) {
                viewHolder.tvStatus.setText("Cancelled");
                viewHolder.tvStatus.setTextColor(Color.parseColor("#FF6868"));
                viewHolder.tvSizeColor.setBackgroundColor(Color.parseColor("#FF6868"));

                letter = String.valueOf(android.get(i).getEnquiry_title().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, Color.parseColor("#FF6868"));
                viewHolder.letter.setImageDrawable(drawable);


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

                }
        }
    }

    @Override
    public int getItemCount() {
        return android.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeading;
        TextView tvStatus, tvCmpyName, tvSizeColor;
        ImageView letter ;

        public ViewHolder(View view) {
            super(view);
            tvHeading = view.findViewById(R.id.tvHeading);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvCmpyName = view.findViewById(R.id.tvCmpyName);
            tvSizeColor = view.findViewById(R.id.tvSizeColor);
            letter = view.findViewById(R.id.gmailitem_letter);

        }
    }
}
