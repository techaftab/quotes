package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONException;
import org.json.JSONObject;

import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveFeedBAck extends Fragment implements View.OnClickListener {

    View view;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private RatingBar ratingBar, ratingBar2, ratingBar3;
    private Button btnSubmit;
    private EditText etMsg;

    String radioStringStr;
    private String radioString, eid;
    private RadioButton rb;

    private TextView tvQuoteTitle, tvQuotePrice;

    private Spinner spChoose;

    private String rate1, rate2, rate3, commentMsg;
    private String qid, price;
    private String strChooseLevel;
    String TAG = "LeaveFeedBAck";


    public LeaveFeedBAck() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_leave_feed_back, container, false);

        Bundle b = this.getArguments();
        assert b != null;
        // mail_id = Inbox_Detail.indoxDetail.id;
        // update_quote = Inbox_Detail.indoxDetail.id;

        qid = b.getString("qid");
        price = b.getString("price");
        eid = b.getString("eid");

        Log.e(TAG, "qid is :" + qid);
        Log.e(TAG, "price is :" + price);
        Log.e(TAG, "eid is :" + eid);

        initviews();
        registerClick();
        return view;
    }

    private void initviews() {

        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton2 = view.findViewById(R.id.radioButton2);
        radioButton3 = view.findViewById(R.id.radioButton3);
        ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar2 = view.findViewById(R.id.ratingBar2);
        ratingBar3 = view.findViewById(R.id.ratingBar3);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        etMsg = view.findViewById(R.id.etMsg);

        tvQuoteTitle = view.findViewById(R.id.tvQuoteTitle);
        tvQuotePrice = view.findViewById(R.id.tvQuotePrice);

        tvQuotePrice.setText(price);
        tvQuoteTitle.setText(AppData.getInstance().getFeedbacktitle());

        spChoose = view.findViewById(R.id.spChoose);

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 rb  = (RadioButton) group.findViewById(checkedId);
                 if (null != rb && checkedId > -1) {

                    radioStringStr = String.valueOf(rb.getText());

                    Toast.makeText(getActivity(), radioStringStr, Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }


    private void registerClick() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            getData();
        }
    }

    @SuppressLint("ResourceType")
    private void getData() {
        rate1 = String.valueOf(ratingBar.getRating());
        rate2 = String.valueOf(ratingBar2.getRating());
        rate3 = String.valueOf(ratingBar3.getRating());
        commentMsg = etMsg.getText().toString();

        int selectedId = radioGroup.getCheckedRadioButtonId();

        rb = view.findViewById(selectedId);
/*
        Toast.makeText(getActivity(), selectedId, Toast.LENGTH_SHORT).show();

        Toast.makeText(getActivity(), rb.getText(), Toast.LENGTH_SHORT).show();*/


        if (radioButton1.isChecked() || radioButton2.isChecked() || radioButton3.isChecked()) {
            switch (String.valueOf(rb.getText())) {
                case "Positive":
                    radioString = "1";
                    break;
                case "Neutral":
                    radioString = "2";
                    break;
                case "Negative":
                    radioString = "3";
                    break;

            }
        }

      /*  if (!TextUtils.isEmpty(rb.getText())) {
            Log.e(TAG, String.valueOf(rb.getText()));
            switch (String.valueOf(rb.getText())) {
                case "Positive":
                    radioString = "1";
                    break;
                case "Neutral":
                    radioString = "2";
                    break;
                case "Negative":
                    radioString = "3";
                    break;

            }
        }
*/
        if (spChoose.getSelectedItem().toString().equalsIgnoreCase("The service has been completed")) {
            strChooseLevel = "1";
        } else if (spChoose.getSelectedItem().toString().equalsIgnoreCase("The service has not yet been completed")) {
            strChooseLevel = "2";
        } else if (spChoose.getSelectedItem().toString().equalsIgnoreCase("The service was not completed")) {
            strChooseLevel = "3";
        }


        if (checkValid()) {
            if (CommonMethod.isNetworkAvailable(getActivity())) {
                hideSoftKeyboard(getActivity());
                new LeaveFeedsAsyncTask().execute();
            } else {
                Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkValid() {
        if (spChoose.getSelectedItem().equals("Please choose an option from the list below")) {
            CommonMethod.showAlert("Please Select type of Feedback", getActivity());
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Leave Feedback");
    }

    public class LeaveFeedsAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "LeaveFeeds api response is " + result);
            if (result == null) {
                // Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(result);
                    responseMessage = jObject.getString("message");
                    status = jObject.getString("Status");

                    if (status.equalsIgnoreCase("Success")) {
                        JSONObject jsonObjectData = jObject.getJSONObject("result");

                        Intent i = new Intent(getActivity(), HomeScreen.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    } else if (status.equals("failed")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.FEEDBACK_SUBMIT;

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);

            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);

                Log.e(TAG, "quote_id" + qid);
                Log.e(TAG, "left_for" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                Log.e(TAG, "pnn" + radioString);
                Log.e(TAG, "q1" + rate1);
                Log.e(TAG, "comment" + commentMsg);
                Log.e(TAG, "rate2" + rate2);
                Log.e(TAG, "rate3" + rate3);
                Log.e(TAG, "strChooseLevel" + strChooseLevel);
                Log.e(TAG, "id" + LoginPreferences.getActiveInstance(getActivity()).getId());

                client.addFormPart("quote_id", qid);
                client.addFormPart("left_for", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("pnn", radioString);
                client.addFormPart("q1", rate1);
                client.addFormPart("q2", rate2);
                client.addFormPart("q3", rate3);
                client.addFormPart("comment", commentMsg);
                client.addFormPart("user_id", LoginPreferences.getActiveInstance(getActivity()).getId());
                client.addFormPart("consumer_type", "consumer");
                client.addFormPart("service_status", strChooseLevel);
                client.addFormPart("enquiry_id", eid);

                client.finishMultipart();

                response = client.getResponse();
                Log.e("response", response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
