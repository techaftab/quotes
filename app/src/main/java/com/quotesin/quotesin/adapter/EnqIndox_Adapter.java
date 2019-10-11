package com.quotesin.quotesin.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.fragments.Enquiry_Reply;
import com.quotesin.quotesin.model.EnqIndox_Model;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.GetDeleteItem;

import java.util.ArrayList;

public class EnqIndox_Adapter extends RecyclerView.Adapter<EnqIndox_Adapter.ViewHolder> {
    public ArrayList<EnqIndox_Model> models;
    private Context context;
    String letter, enq_status;
    public static PopupWindow popupWindow;
    public TextView tvReply, tvDelete, tvForward;
    public LinearLayout linearLayout1;

    GetDeleteItem getMyItem;

    public EnqIndox_Adapter(Context context, ArrayList<EnqIndox_Model> models, GetDeleteItem getMyItem, String enq_status) {
        this.models = models;
        this.context = context;
        this.getMyItem = getMyItem;
        this.enq_status = enq_status;
    }

    @NonNull
    @Override
    public EnqIndox_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_indox_details, parent, false);
        return new EnqIndox_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        int pos = i;

        viewHolder.tvUserName.setText(models.get(i).getConsumer_username());
        viewHolder.tvDead_Date.setText("Deadline : " + models.get(i).getEnquiry_expired());
        viewHolder.tvDate.setText(models.get(i).getEnquiry_post_date());
        viewHolder.tvContent.setText(models.get(i).getEnquiry_description());
        viewHolder.tvEmail.setText(models.get(i).getConsumer_email());


        if (!TextUtils.isEmpty(models.get(i).getFlag())) {
            if (models.get(i).getFlag().equals("1")) {
                viewHolder.LLFormDetail.setVisibility(View.VISIBLE);

                viewHolder.tvBAddress.setText(models.get(i).getBusiness_address());
                viewHolder.tvCEmail.setText(models.get(i).getBusiness_email());
                viewHolder.tvMobile.setText(models.get(i).getBusiness_number());
                viewHolder.tvTel.setText(models.get(i).getBusiness_telephone());
                viewHolder.tvName.setText(models.get(i).getBusiness_name());
            }
        }


        letter = String.valueOf(models.get(i).getEnquiry_title().charAt(0));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, Color.parseColor("#168add"));
        viewHolder.letter.setImageDrawable(drawable);


        viewHolder.ivPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup, null);
                tvReply = (TextView) customView.findViewById(R.id.tvReply);
                tvDelete = (TextView) customView.findViewById(R.id.tvDelete);

                //instantiate popup window
                popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                //display the popup window
                popupWindow.showAtLocation(linearLayout1, Gravity.TOP | Gravity.RIGHT, 60, 400);


                if (!TextUtils.isEmpty(AppData.getOurInstance().getReplyflag()))
                    if (AppData.getOurInstance().getReplyflag().equals("1")) {
                        tvReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "You don't reply on this enquiry", Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (enq_status.equals("0") && AppData.getInstance().getReplyDots().equals("0")) {
                        tvReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                HomeScreen activity = (HomeScreen) v.getContext();
                                Enquiry_Reply enquiryReply = new Enquiry_Reply();
                                bundle.putString("mail_id", models.get(i).getMail_id());
                                bundle.putString("update_quote", "no");
                                enquiryReply.setArguments(bundle);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, enquiryReply).addToBackStack(null).commit();

                                Log.e("M_id", models.get(i).getMail_id());
                                popupWindow.dismiss();
                            }
                        });

                    } else {
                        Toast.makeText(context, "You don't reply on this enquiry", Toast.LENGTH_LONG).show();
                    }

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getMyItem.GetClickedItem(models.get(i).getMail_id());
                        popupWindow.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvDead_Date, tvDate, tvContent, tvEmail;
        ImageView letter, ivPopup, ivDownload;

        TextView tvCEmail, tvMobile, tvTel, tvBAddress, tvName;
        LinearLayout LLFormDetail;


        public ViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tvUserName);
            tvDead_Date = view.findViewById(R.id.tvDead_Date);
            tvContent = view.findViewById(R.id.tvContent);
            tvDate = view.findViewById(R.id.tvDate);
            letter = view.findViewById(R.id.gmailitem_letter);
            ivPopup = view.findViewById(R.id.ivPopup);
            ivDownload = view.findViewById(R.id.ivDownload);
            tvEmail = view.findViewById(R.id.tvEmail);
            linearLayout1 = view.findViewById(R.id.linearLayout1);

            LLFormDetail = view.findViewById(R.id.LLFormDetail);
            tvCEmail = view.findViewById(R.id.tvCEmail);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvTel = view.findViewById(R.id.tvTel);
            tvBAddress = view.findViewById(R.id.tvBAddress);
            tvName = view.findViewById(R.id.tvName);


        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

}
