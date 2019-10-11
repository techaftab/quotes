package com.quotesin.quotesin.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.ServiceQuestions;
import com.quotesin.quotesin.model.QuesAnsResponse;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;

import static com.quotesin.quotesin.activities.ServiceQuestions.quesAnsResponse;
import static com.quotesin.quotesin.activities.ServiceQuestions.recyclerview;


public class ServiceQues_Adapter extends RecyclerView.Adapter<ServiceQues_Adapter.ViewHolder> {

    public static EditText edt;
    private List<QuesAnsResponse.Result> android1;
    private Context context;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public ServiceQues_Adapter(List<QuesAnsResponse.Result> result) {
        this.android1 = result;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    static void hideKeyboard(EditText edt) {
        CommonMethod.hideSoftKeyboard(ServiceQuestions.base);
        edt.setFocusable(true);
    }

    @NonNull
    @Override
    public ServiceQues_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_service_ques, viewGroup, false);
        context = view.getContext();
        ViewHolder singleItemRowHolder = new ViewHolder(view);
        return singleItemRowHolder;
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        recyclerview.setItemViewCacheSize(i);
        List<QuesAnsResponse.Result.Answer> questionList11 = android1.get(i).answers;
        String questionTitle = String.valueOf(Html.fromHtml(Html.fromHtml(android1.get(i).quesName.replace(",", "/")).toString()));
        String questionType = android1.get(i).quesType;
        viewHolder.tvQuestion.setText(questionTitle);

        switch (questionType) {
            case "Textbox":
                viewHolder.et1.setVisibility(View.VISIBLE);
                viewHolder.et2.setVisibility(View.GONE);
                viewHolder.recyclerView.setVisibility(View.GONE);
                viewHolder.spinner1.setVisibility(View.GONE);
                viewHolder.flSpinner.setVisibility(View.GONE);
                viewHolder.radioGroup.setVisibility(View.GONE);
                viewHolder.RadioLayout.setVisibility(View.GONE);
                viewHolder.flSpinnerRadio.setVisibility(View.GONE);
                viewHolder.spinnerRadio.setVisibility(View.GONE);

                break;

            case "Textarea":
                viewHolder.flSpinnerRadio.setVisibility(View.GONE);
                viewHolder.spinnerRadio.setVisibility(View.GONE);
                viewHolder.et2.setVisibility(View.VISIBLE);
                viewHolder.et1.setVisibility(View.GONE);
                viewHolder.recyclerView.setVisibility(View.GONE);
                viewHolder.flSpinner.setVisibility(View.GONE);
                viewHolder.radioGroup.setVisibility(View.GONE);
                viewHolder.RadioLayout.setVisibility(View.GONE);

                break;

            case "Select": {
                viewHolder.flSpinnerRadio.setVisibility(View.GONE);
                viewHolder.spinnerRadio.setVisibility(View.GONE);
                viewHolder.flSpinner.setVisibility(View.VISIBLE);
                viewHolder.spinner1.setVisibility(View.VISIBLE);
                viewHolder.et2.setVisibility(View.GONE);
                viewHolder.et1.setVisibility(View.GONE);
                viewHolder.recyclerView.setVisibility(View.GONE);
                viewHolder.radioGroup.setVisibility(View.GONE);
                viewHolder.RadioLayout.setVisibility(View.GONE);

                List<QuesAnsResponse.Result.Answer> questionList1 = android1.get(i).answers;
                ArrayList<String> list = new ArrayList<>();

                for (int i1 = 0; i1 < questionList1.size(); i1++) {
                    list.add(String.valueOf(Html.fromHtml(Html.fromHtml(questionList1.get(i1).mulName).toString())));
                }
                AppData.getInstance().setList(list);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, AppData.getInstance().getList());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.spinner1.setAdapter(dataAdapter);

                break;
            }

            case "Radio": {
                viewHolder.radioGroup.setVisibility(View.VISIBLE);
                viewHolder.et2.setVisibility(View.GONE);
                viewHolder.et1.setVisibility(View.GONE);
                viewHolder.flSpinner.setVisibility(View.GONE);
                viewHolder.RadioLayout.setVisibility(View.GONE);

                viewHolder.flSpinnerRadio.setVisibility(View.VISIBLE);


                List<QuesAnsResponse.Result.Answer> questionList1 = android1.get(i).answers;
                ArrayList<String> listRadio = new ArrayList<>();

                for (int i1 = 0; i1 < questionList1.size(); i1++) {
                    listRadio.add(questionList1.get(i1).mulName);
                }
                AppData.getInstance().setRadiolist(listRadio);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, AppData.getInstance().getRadiolist());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.spinnerRadio.setAdapter(dataAdapter);



               /* ArrayList<String> list1 = new ArrayList<>();
                for (int i1 = 0; i1 < questionList11.size(); i1++) {
                    list1.add(questionList11.get(i1).mulName);
                }
                final RadioButton[] rb = new RadioButton[list1.size()];
                final RadioGroup rg = new RadioGroup(context);
                rg.setOrientation(RadioGroup.VERTICAL);

                for (int i1 = 0; i1 < list1.size(); i1++) {
                    rb[i1] = new RadioButton(context);
                    rb[i1].setText(list1.get(i1));
                    rb[i1].setId(i1 + 100);
                    rg.addView(rb[i1]);
                }
                viewHolder.RadioLayout.addView(rg);*/
                break;
            }

            case "Checkbox": {
                viewHolder.flSpinnerRadio.setVisibility(View.GONE);
                viewHolder.spinnerRadio.setVisibility(View.GONE);
                viewHolder.flSpinner2.setVisibility(View.GONE);
                viewHolder.spinner2.setVisibility(View.GONE);
                viewHolder.et2.setVisibility(View.GONE);
                viewHolder.et1.setVisibility(View.GONE);
                viewHolder.radioGroup.setVisibility(View.GONE);
                viewHolder.RadioLayout.setVisibility(View.GONE);
                viewHolder.flSpinner.setVisibility(View.GONE);
                viewHolder.spinner1.setVisibility(View.GONE);

                viewHolder.recyclerView.setVisibility(View.VISIBLE);

                final ArrayList<String> selectedcheckList = new ArrayList<>();
                List<QuesAnsResponse.Result.Answer> questionList = android1.get(i).answers;
                ServiceInnerDataAdapter adapter = new ServiceInnerDataAdapter(questionList, questionType, new ServiceInnerDataAdapter.OnItemCheckListener() {
                    @Override
                    public void onItemCheck(QuesAnsResponse.Result.Answer item) {
                        selectedcheckList.add(item.mulName);

                        String selectedChecks = selectedcheckList.toString().replace(",", "@")
                                .replace("[", "").replace("]", "");
                        Log.e("Checks--", selectedChecks);
                        quesAnsResponse.result.get(i).answer = selectedChecks;
                    }

                    @Override
                    public void onItemUncheck(QuesAnsResponse.Result.Answer item) {
                        selectedcheckList.remove(item.mulName);
                        Log.e("Remove Checks--", selectedcheckList.toString());
                    }
                });
                viewHolder.recyclerView.setHasFixedSize(true);
                viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                viewHolder.recyclerView.setAdapter(adapter);
                viewHolder.recyclerView.setRecycledViewPool(recycledViewPool);

                break;

            }

        }
    }

    @Override
    public int getItemCount() {
        return android1.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView recyclerView;
        TextView tvQuestion;
        EditText et1, et2;
        Spinner spinner1, spinner2, spinnerRadio;
        FrameLayout flSpinner, flSpinner2, flSpinnerRadio;
        RadioGroup radioGroup;
        LinearLayout ll, RadioLayout;

        public ViewHolder(View view) {
            super(view);

            tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
            recyclerView = view.findViewById(R.id.recycler_view_list);

            flSpinnerRadio = view.findViewById(R.id.flSpinnerRadio);
            spinnerRadio = view.findViewById(R.id.spinnerRadio);

            et1 = view.findViewById(R.id.et1);
            et2 = view.findViewById(R.id.et2);
            edt = view.findViewById(R.id.edt);
            spinner1 = view.findViewById(R.id.spinner1);
            flSpinner = view.findViewById(R.id.flSpinner);
            radioGroup = view.findViewById(R.id.radioGroup);
            //  checkbox = view.findViewById(R.id.checkbox);
            RadioLayout = view.findViewById(R.id.RadioLayout);

            spinner2 = view.findViewById(R.id.spinner2);
            flSpinner2 = view.findViewById(R.id.flSpinner2);


            et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        hideKeyboard(edt);
                    }
                    return false;
                }
            });


            et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        Log.e("Focus Status-", "Loose Focus");
                        if (!TextUtils.isEmpty(et1.getText().toString().trim())) {
                            quesAnsResponse.result.get(getAdapterPosition()).answer = et1.getText().toString();
                        }

                    } else {
                        Log.e("Focus Status-", "Get Focus");
                    }

                }
            });


            et2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        hideKeyboard(edt);
                    }
                    return false;
                }
            });

            et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        Log.e("Focus Status-", "Loose Focus");
                        if (!TextUtils.isEmpty(et2.getText().toString().trim())) {
                            quesAnsResponse.result.get(getAdapterPosition()).answer = et2.getText().toString();
                        }

                    } else {
                        Log.e("Focus Status-", "Get Focus");
                    }

                }
            });

           /* et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_NEXT) {
                        InputMethodManager inputMethodManager = (InputMethodManager) et1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                        quesAnsResponse.result.get(getAdapterPosition()).answer = et1.getText().toString();
                        return true;
                    }
                    return false;
                }
            });*/

            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    quesAnsResponse.result.get(getAdapterPosition()).answer = parentView.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

            spinnerRadio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    quesAnsResponse.result.get(getAdapterPosition()).answer = parentView.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

        }
    }

}
