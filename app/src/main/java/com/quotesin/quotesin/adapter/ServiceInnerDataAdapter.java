package com.quotesin.quotesin.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.QuesAnsResponse;

import java.util.List;


public class ServiceInnerDataAdapter extends RecyclerView.Adapter<ServiceInnerDataAdapter.SingleItemRowHolder> {


    private final OnItemCheckListener onItemClick;
    ServiceInnerDataAdapter adapter;
    private String questionType;
    private List<QuesAnsResponse.Result.Answer> itemModels;
    private Context mContext;

    public ServiceInnerDataAdapter(List<QuesAnsResponse.Result.Answer> itemModels, String questionType,
                                   @NonNull OnItemCheckListener onItemCheckListener) {
        this.itemModels = itemModels;
        this.questionType = questionType;
        this.onItemClick = onItemCheckListener;

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inner_service, parent, false);
        mContext = v.getContext();
        SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder holder, final int position) {


        final QuesAnsResponse.Result.Answer ansModel = itemModels.get(position);

        switch (questionType) {
            case "Textbox":
                holder.et1.setVisibility(View.VISIBLE);
                holder.checkbox1.setVisibility(View.GONE);
                holder.et2.setVisibility(View.GONE);
                holder.radioGroup.setVisibility(View.GONE);
                holder.tvSelect.setVisibility(View.GONE);
                break;

            case "Checkbox":
                holder.checkbox1.setVisibility(View.VISIBLE);
                holder.et1.setVisibility(View.GONE);
                holder.et2.setVisibility(View.GONE);
                holder.radioGroup.setVisibility(View.GONE);
                holder.tvSelect.setVisibility(View.GONE);
                holder.checkbox1.setText(itemModels.get(position).mulName);
                holder.checkbox1.setTag(position);


                ((SingleItemRowHolder) holder).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SingleItemRowHolder) holder).checkbox1.setChecked(
                                !((SingleItemRowHolder) holder).checkbox1.isChecked());
                        if (((SingleItemRowHolder) holder).checkbox1.isChecked()) {
                            onItemClick.onItemCheck(ansModel);
                        } else {
                            onItemClick.onItemUncheck(ansModel);
                        }
                    }
                });

                break;

            case "Textarea":
                holder.et2.setVisibility(View.VISIBLE);
                holder.et1.setVisibility(View.GONE);
                holder.radioGroup.setVisibility(View.GONE);
                holder.checkbox1.setVisibility(View.GONE);
                holder.tvSelect.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    interface OnItemCheckListener {
        void onItemCheck(QuesAnsResponse.Result.Answer item);

        void onItemUncheck(QuesAnsResponse.Result.Answer item);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        EditText et1, et2;
        CheckBox checkbox1;
        TextView tvSelect;

        RadioGroup radioGroup;
        RadioButton radioButton, radioButton1;

        public SingleItemRowHolder(View itemView) {
            super(itemView);

            tvSelect = itemView.findViewById(R.id.tvSelect);
            et1 = itemView.findViewById(R.id.et1);
            checkbox1 = itemView.findViewById(R.id.checkbox1);
            et2 = itemView.findViewById(R.id.et2);

            checkbox1.setClickable(false);

            radioGroup = itemView.findViewById(R.id.radioGroup);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButton1 = itemView.findViewById(R.id.radioButton1);


        }


        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}


