package com.quotesin.quotesin.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.ReviewModel;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    public ArrayList<ReviewModel> android;
    private Context context;

    public ReviewAdapter(FragmentActivity activity, ArrayList<ReviewModel> ReviewModelArrayList) {
        this.android = ReviewModelArrayList;
        this.context = activity;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ReviewAdapter.ViewHolder holder, int i) {

        holder.tvby.setText(android.get(i).getUser_id());
        holder.tvReviewDate.setText(android.get(i).getDate());
        holder.tvReviewMsg.setText(android.get(i).getComment());


        int  q1 = Integer.parseInt(android.get(i).getQ1());
        int  q2 = Integer.parseInt(android.get(i).getQ2());
        int  q3 = Integer.parseInt(android.get(i).getQ2());

        int qtot = (q1+q2+q3)/ 3;
        float num = qtot/2;

        holder.ratingBar_review.setRating(num);
        Log.e("REVIEWADAPTER", String.valueOf(num));
    }

    @Override
    public int getItemCount() {
        return android.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvby, tvReviewDate,  tvReviewMsg;

        RatingBar ratingBar_review;


        public ViewHolder(View view) {
            super(view);

            tvby = view.findViewById(R.id.tvby);
            tvReviewDate = view.findViewById(R.id.tvReviewDate);
            tvReviewMsg = view.findViewById(R.id.tvReviewMsg);
            ratingBar_review = view.findViewById(R.id.ratingBar_review);

        }
    }
}
