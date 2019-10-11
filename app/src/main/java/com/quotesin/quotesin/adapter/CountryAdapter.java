package com.quotesin.quotesin.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.CountriesModel;

import java.util.ArrayList;

import static com.quotesin.quotesin.activities.CountryService.chkAll;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    public ArrayList<CountriesModel> android;
    private Context context;
    private boolean isSelectedAll;


    public CountryAdapter(FragmentActivity activity, ArrayList<CountriesModel> countriesArrayList) {
        this.android = countriesArrayList;
        this.context = activity;
    }

    @NonNull
    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return new CountryAdapter.ViewHolder(view);
    }


    public void onBindViewHolder(final CountryAdapter.ViewHolder holder, int i) {
        holder.text1.setText(android.get(i).getCountry_name());

        holder.checkBox.setChecked(android.get(i).isSelected());
        holder.checkBox.setTag(i);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkBox.getTag();
                if (android.get(pos).isSelected()) {
                    android.get(pos).setSelected(false);
                    chkAll.setChecked(false);


                } else {
                    android.get(pos).setSelected(true);

                }
            }
        });

        if (!isSelectedAll) {
            holder.checkBox.setChecked(false);
            for (int i1 = 0; i1 < android.size(); i1++) {
                android.get(i).setSelected(false);
            }
        } else {
            holder.checkBox.setChecked(true);
            for (int i1 = 0; i1 < android.size(); i1++) {
                android.get(i).setSelected(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public void selectAll() {
        isSelectedAll = true;
        notifyDataSetChanged();
    }

    public void unselectall() {
        isSelectedAll = false;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;
        public CheckBox checkBox;


        public ViewHolder(View view) {
            super(view);
            text1 = view.findViewById(R.id.text1);
            checkBox = view.findViewById(R.id.checkbox);
        }
    }
}
