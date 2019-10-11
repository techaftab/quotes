package com.quotesin.quotesin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.ViewEmailResponse;

import java.util.List;

public class viewFormAdapter extends RecyclerView.Adapter<viewFormAdapter.ViewHolder> {
    public List<ViewEmailResponse.Attachment> models;
    private Context context;


    public viewFormAdapter(Context context, List<ViewEmailResponse.Attachment> models) {
        this.models = models;
        this.context = context;

    }

    @NonNull
    @Override
    public viewFormAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_form_view, parent, false);
        return new viewFormAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewFormAdapter.ViewHolder viewHolder, final int i) {

        if (!models.get(i).answerContent.equals("")) {
            viewHolder.tvQues.setText("Ques:" + models.get(i).answerQuestion);
            viewHolder.tvAnswer.setText("Ans :" + models.get(i).answerContent);
        }


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvAnswer, tvQues;

        public ViewHolder(View view) {
            super(view);

            tvAnswer = view.findViewById(R.id.tvAnswer);
            tvQues = view.findViewById(R.id.tvQues);


        }
    }
}
