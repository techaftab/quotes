package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.adapter.viewFormAdapter;
import com.quotesin.quotesin.model.ViewEmailResponse;
import com.quotesin.quotesin.model.viewformModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class AfterContactInboxDe extends Fragment {
    View view;

    String username, desc, deadline, tittle, location_code, b_name, eid, con_email, post_date, qid, delete_flag, enq_logo, update_visFlag;
    TextView tvDesc, tvSubject, tvB_name, tvPrice, tvDate, tvPriceDesc, tvRate, tvReady, tvDiscription;
    TextView tvRefNo, tvFName, tvBAddress, tvTel, tvMobile, tvCEmail, tvTitle, tvDeadDate, tvForm;
    RecyclerView recyclerview;
    Button btn_Feedback;
    String price;
    ImageView ivDocument;

    viewFormAdapter enqIndoxAdapter;
    ArrayList<viewformModel> enqIndoxModelArrayList = new ArrayList<>();

    String TAG = "AfterContactInboxDe";

    public AfterContactInboxDe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.contact_inbox_de, container, false);

        initViews();
        getDataFromBundle();
        //registerclickListener();
        callApi();

        return view;
    }


    private void initViews() {
        tvDesc = view.findViewById(R.id.tvDesc);
        ivDocument = view.findViewById(R.id.ivDocument);
        tvForm = view.findViewById(R.id.tvForm);
        tvFName = view.findViewById(R.id.tvFName);
        tvBAddress = view.findViewById(R.id.tvBAddress);
        tvTel = view.findViewById(R.id.tvTel);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvCEmail = view.findViewById(R.id.tvCEmail);
        tvDeadDate = view.findViewById(R.id.tvDeadDate);
        tvRefNo = view.findViewById(R.id.tvRefNo);
        tvTitle = view.findViewById(R.id.tvTitle);

        tvSubject = view.findViewById(R.id.tvSubject);
        tvB_name = view.findViewById(R.id.tvB_name);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvRate = view.findViewById(R.id.tvRate);
        tvReady = view.findViewById(R.id.tvReady);
        tvDiscription = view.findViewById(R.id.tvDiscription);
        tvPriceDesc = view.findViewById(R.id.tvPriceDesc);

        recyclerview = view.findViewById(R.id.recyclerview);

        btn_Feedback = view.findViewById(R.id.btn_Feedback);

        btn_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) v.getContext();
                LeaveFeedBAck enquiryReply = new LeaveFeedBAck();
                bundle.putString("qid", qid);
                bundle.putString("price", price);

                enquiryReply.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, enquiryReply).addToBackStack(null).commit();


            }
        });

    }

    private void callApi() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new EnquiryDetailAsyncTask().execute();
            new ViewFormAsyncTask().execute();

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromBundle() {
        Bundle b = this.getArguments();
        assert b != null;

        eid = b.getString("eid");
        username = b.getString("username");
        desc = b.getString("desc");
        deadline = b.getString("deadline");
        tittle = b.getString("tittle");
        location_code = b.getString("location_code");
        con_email = b.getString("con_email");
        post_date = b.getString("post_date");
        qid = b.getString("qid");
        b_name = b.getString("b_name");
        delete_flag = b.getString("delete_flag");
        enq_logo = b.getString("enq_logo");
        update_visFlag = b.getString("update_visFlag");

        Log.e(TAG, "username--" + username);
        Log.e(TAG, "desc--" + desc);
        Log.e(TAG, "deadline--" + deadline);
        Log.e(TAG, "tittle--" + tittle);
        Log.e(TAG, "location_code--" + location_code);
        // Log.e(TAG, "qid--" + qid);
        Log.e(TAG, "eid--" + eid);
        Log.e(TAG, "con_email--" + con_email);
        Log.e(TAG, "post_date--" + post_date);
        Log.e(TAG, "b_name--" + b_name);
        Log.e(TAG, "qid--" + qid);
        Log.e(TAG, "delete_flag--" + delete_flag);
        Log.e(TAG, "update_visFlag--" + update_visFlag);

        AppData.getInstance().setFeedbacktitle(tittle);
        tvTitle.setText(tittle);
        tvDeadDate.setText(deadline);
        if (!TextUtils.isEmpty(desc))
            tvDesc.setText(desc);

    }


    private class ViewFormAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, " ViewForm api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Gson gson = new Gson();
                    ViewEmailResponse viewEmailResponse = gson.fromJson(response, ViewEmailResponse.class);
                    if (viewEmailResponse.status.equals("Success")) {
                        if (viewEmailResponse.attachment.size() > 0) {
                            setAdapter(viewEmailResponse.attachment);
                        }

                        if (viewEmailResponse.attachment.size() == 1) {
                            if (viewEmailResponse.attachment.get(0).answerContent.equals("")) {
                                tvForm.setVisibility(View.GONE);
                            }

                        }
                    } else {
                        Toast.makeText(getActivity(), "Server Issue", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.View_Email;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "Inbox after connection url: " + url);
                Log.e(TAG, "email_id: " + eid);


                client.addFormPart("email_id", eid);
                client.addFormPart("review_status", "0");
                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "Inbox response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }


    @SuppressLint("WrongConstant")
    private void setAdapter(List<ViewEmailResponse.Attachment> enqIndoxModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + enqIndoxModelArrayList.size());
        enqIndoxAdapter = new viewFormAdapter(getActivity(), enqIndoxModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(enqIndoxAdapter);

    }

    @SuppressLint("StaticFieldLeak")
    private class EnquiryDetailAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "Inbox api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        try {
                            JSONObject jsonObject = jObject.getJSONObject("result");
                            if (jsonObject != null) {
                                enqIndoxModelArrayList.clear();

                            }

                            if (!jObject.isNull("contact")) {
                                JSONObject json2 = jObject.getJSONObject("contact");
                                {
                                    if (json2 != null) {

                                        String ref_no = json2.getString("quote_md5");
                                        Log.e(TAG, "ref_no" + ref_no);

                                        tvReady.setText(json2.getString("q_start"));
                                        tvRate.setText(json2.getString("q_occur"));
                                        tvPriceDesc.setText(json2.getString("q_price_type"));
                                        tvSubject.setText(json2.getString("subject"));
                                        tvPrice.setText(json2.getString("quote_price"));
                                        tvB_name.setText(json2.getString("business_username"));
                                        tvDiscription.setText(json2.getString("quote_message"));


                                        final String quote_image = json2.getString("quote_image");

                                        if (!TextUtils.isEmpty(quote_image)) {
                                            String str = quote_image;
                                            Log.e("str", str);

                                            String str1 = str.substring(0, 4);
                                            Log.e("str", str1);

                                            if (str1.equals("site")) {
                                                ivDocument.setVisibility(View.VISIBLE);
                                                ivDocument.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        String url = "http://dev.webmobril.services/quotesinapp/" + quote_image;
                                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                                        i.setData(Uri.parse(url));
                                                        startActivity(i);
                                                    }
                                                });
                                            } else {
                                                ivDocument.setVisibility(View.GONE);
                                            }
                                        }


                                        JSONObject json3 = json2.getJSONObject("contact");

                                        tvFName.setText(json3.getString("username"));
                                        tvBAddress.setText(json3.getString("address"));
                                        tvCEmail.setText(json3.getString("email"));
                                        tvMobile.setText(json3.getString("phone"));
                                        tvTel.setText(json3.getString("evening_phone"));


                                        if (!TextUtils.isEmpty(ref_no)) {
                                            String reference = ref_no.substring(0, 10);
                                            tvRefNo.setText("Reference No:" + reference.toUpperCase());
                                        }

                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (status.equals("Failed")) {
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
            String url = APIUrl.View_Email;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "View_Email after connection url: " + url);
                //    Log.e(TAG, "location_id: " + location_code);
                Log.e(TAG, "email_id: " + eid);


                client.addFormPart("email_id", eid);
                client.addFormPart("review_status", "0");
                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "View_Email response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
