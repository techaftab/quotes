package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
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
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AfterAcceptBussDetail extends Fragment implements View.OnClickListener {
    public String textExpandFlag = "1";
    View view;
    String TAG = this.getClass().getName();
    private String business_address, username, desc, deadline, tittle, location_code, b_name, eid, con_email, post_date, qid, delete_flag, enq_logo, update_visFlag;
    private TextView tvService, tvExpandText, tvMsg, tvCategory, tvDate, tvPriceDesc, tvRate, tvReady, tvDiscription;
    private TextView tvRefNo, tvFName, tvBAddress, tvTel, tvMobile, tvCEmail, tvTitle, tvDeadDate, tvForm, tvpostDate, tvWeb;
    RecyclerView recyclerview;
    private Button btn_Feedback;
    private String price, business_phone;
    private ImageView ivEnqUserLogo, ivInfo, ivDocument, ivLocation2;
    private LinearLayout LlExpandText;
    private viewFormAdapter enqIndoxAdapter;
    private ArrayList<viewformModel> enqIndoxModelArrayList = new ArrayList<>();
    private ImageView ivCall, ivEmail, ivWeb, ivLocation;
    private String qid1, ref_no;

    public AfterAcceptBussDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.enquiry_acceptdetail, container, false);

        initViews();
        getDataFromBundle();
        callApi();
        registerClickListener();

        return view;
    }

    private void registerClickListener() {
        ivLocation2.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivEmail.setOnClickListener(this);
        ivWeb.setOnClickListener(this);
    }

    private void initViews() {
        tvFName = view.findViewById(R.id.tvFName);
        tvBAddress = view.findViewById(R.id.tvBAddress);
        tvTel = view.findViewById(R.id.tvTel);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvCEmail = view.findViewById(R.id.tvCEmail);
        tvRefNo = view.findViewById(R.id.tvRefNo);
        tvForm = view.findViewById(R.id.tvForm);
        tvWeb = view.findViewById(R.id.tvWeb);
        tvMsg = view.findViewById(R.id.tvMsg);
        tvDeadDate = view.findViewById(R.id.tvDeadDate);
        tvTitle = view.findViewById(R.id.tvTitle);

        recyclerview = view.findViewById(R.id.recyclerview);
        btn_Feedback = view.findViewById(R.id.btn_Feedback);

        tvpostDate = view.findViewById(R.id.tvpostDate);
        ivLocation2 = view.findViewById(R.id.ivLocation2);
        ivInfo = view.findViewById(R.id.ivInfo);
        ivDocument = view.findViewById(R.id.ivDocument);
        tvExpandText = view.findViewById(R.id.tvExpandText);

        tvCategory = view.findViewById(R.id.tvCategory);
        tvService = view.findViewById(R.id.tvService);
        ivEnqUserLogo = view.findViewById(R.id.ivEnqUserLogo);

        LlExpandText = view.findViewById(R.id.LlExpandText);

        ivCall = view.findViewById(R.id.ivCall);
        ivEmail = view.findViewById(R.id.ivEmail);
        ivWeb = view.findViewById(R.id.ivWeb);
        ivLocation = view.findViewById(R.id.ivLocation);


        LlExpandText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textExpandFlag.equals("1")) {
                    tvMsg.setMaxLines(40);
                    textExpandFlag = "0";
                } else {
                    tvMsg.setMaxLines(2);
                    textExpandFlag = "1";
                }

            }
        });

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
        tvpostDate.setText("Posted : "+post_date.replace("-", "/"));
        tvDeadDate.setText(deadline);
        if (!TextUtils.isEmpty(desc))
            tvMsg.setText(desc);

    }


    private void callApi() {
        if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            new QuotesDetailAsyncTask().execute();
            new EnquiryDetailAsyncTask().execute();

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLocation2:
                if (!TextUtils.isEmpty(business_address)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + business_address));
                    if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }

                break;

            case R.id.ivLocation:
                if (!TextUtils.isEmpty(business_address)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + tvBAddress.getText().toString()));
                    if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;

            case R.id.ivWeb:
                String url = tvWeb.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.ivEmail:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] recipients = {"mailto@gmail.com"};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(intent, "Send mail"));
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }
                break;

            case R.id.ivCall:
                Intent ivcall = new Intent(Intent.ACTION_DIAL);
                ivcall.setData(Uri.parse("tel:" + business_phone));
                startActivity(ivcall);
                break;

        }
    }

    private void setAdapter(List<ViewEmailResponse.Attachment> enqIndoxModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + enqIndoxModelArrayList.size());
        enqIndoxAdapter = new viewFormAdapter(getActivity(), enqIndoxModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(enqIndoxAdapter);

    }

    @SuppressLint("StaticFieldLeak")
    private class EnquiryDetailAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
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

            Log.e(TAG, "EnquiryDetail response is " + response);
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
                            enqIndoxModelArrayList.clear();

                            enq_logo = jsonObject.getString("enquiry_loco");

                            if (!TextUtils.isEmpty(enq_logo)) {
                                String str = enq_logo;
                                Log.e("enquiry_loco str", str);

                                String str1 = str.substring(0, 4);
                                Log.e("str--", str1);

                                if (str1.equals("site")) {
                                    ivDocument.setVisibility(View.VISIBLE);
                                    ivDocument.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String url = "http://dev.webmobril.services/quotesinapp/" + enq_logo;
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        }
                                    });
                                } else {
                                    ivDocument.setImageResource(R.mipmap.no_file);
                                    ivDocument.setEnabled(false);
                                }
                            }

                            JSONObject jsonObject1 = jsonObject.getJSONObject("service");
                            {
                                tvService.setText(jsonObject1.getString("service_name"));
                            }
                            JSONObject jsonCat = jsonObject.getJSONObject("category");
                            {
                                tvCategory.setText(jsonCat.getString("category_name"));
                            }
                            JSONObject jsonUser = jsonObject.getJSONObject("user");
                            {
                                Picasso.get().load("http://dev.webmobril.services/quotesinapp/" + jsonUser.getString("business_logo")).into(ivEnqUserLogo);
                            }

                            JSONArray jsonArrayAttach = jObject.getJSONArray("attachment");
                            if (jsonArrayAttach.length() == 0) {
                                ivInfo.setImageResource(R.mipmap.no_attachform);
                                ivInfo.setEnabled(false);
                            } /*else {
                                HomeScreen activity1 = (HomeScreen) getContext();
                                view_form viewForm = new view_form();
                                activity1.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, viewForm).addToBackStack(null).commit();
                            }*/


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

    @SuppressLint("StaticFieldLeak")
    private class QuotesDetailAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
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

            Log.e(TAG, "QuotesDetailAsyncTask api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            for (int i = 0; i < jsonarry.length(); i++) {
                                JSONObject data = jsonarry.getJSONObject(i);

                                qid1 = data.getString("id");
                                price = data.getString("quote_price");
                            }

                            if (!jObject.isNull("contact")) {
                                JSONObject json2 = jObject.getJSONObject("contact");
                                {

                                    ref_no = json2.getString("quote_md5");
                                    Log.e(TAG, "ref_no con_quote--" + ref_no);

                                    JSONObject jsonObject3 = json2.getJSONObject("user");
                                    {
                                        tvFName.setText(jsonObject3.getString("business_username").replace("null", "Not Available"));
                                        tvBAddress.setText(jsonObject3.getString("business_address").replace("null", "Not Available"));
                                        tvCEmail.setText(jsonObject3.getString("business_email").replace("null", "Not Available"));
                                        tvMobile.setText(jsonObject3.getString("business_evening_telephone").replace("null", "Not Available"));
                                        tvTel.setText(jsonObject3.getString("business_telephone").replace("null", "Not Available"));
                                        tvWeb.setText(jsonObject3.getString("business_website").replace("null", "Not Available"));

                                        business_phone = jsonObject3.getString("business_telephone").replace("null", "9999999999");

                                        business_address = jsonObject3.getString("business_address");

                                        if (!TextUtils.isEmpty(ref_no)) {
                                            String reference = ref_no.substring(0, 10);
                                            tvRefNo.setText("Reference No:" .concat(reference.toUpperCase()) );
                                        }
                                    }
                                }
                            }

                            if (!jObject.isNull("commission")) {
                                JSONObject json3 = jObject.getJSONObject("commission");
                                {

                                    String service_status = json3.getString("service_status");

                                    Log.e(TAG, "service_status" + service_status);

                                    AppData.getInstance().setFeedbackflag("service_status");


                                    if (service_status.equals("1")) {
                                        btn_Feedback.setVisibility(View.GONE);
                                    } else {
                                        btn_Feedback.setVisibility(View.VISIBLE);

                                        btn_Feedback.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Bundle bundle = new Bundle();
                                                HomeScreen activity = (HomeScreen) v.getContext();
                                                LeaveFeedBAck enquiryReply = new LeaveFeedBAck();
                                                Log.e(TAG, qid1);
                                                bundle.putString("qid", qid1);
                                                bundle.putString("price", price);
                                                bundle.putString("eid", eid);

                                                enquiryReply.setArguments(bundle);
                                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, enquiryReply).addToBackStack(null).commit();

                                            }
                                        });

                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (status.equals("Failed")) {
                        // Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
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
            String url = APIUrl.consumer_quotes_details;

            /*if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5")) {
                url = APIUrl.Quotes_Details;
            } else {
                url = APIUrl.consumer_quotes_details;
            }*/

            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "QuotesDetailAsyncTask after connection url: " + url);
                Log.e(TAG, "enq_id: " + eid);
                client.addFormPart("enq_id", eid);

                if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5"))
                    client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());

              /*  {
                    client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                }
*/
                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "QuotesDetailAsyncTask response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ViewFormAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
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
                        if (viewEmailResponse != null) {
                            if (viewEmailResponse.attachment.size() > 0) {
                                setAdapter(viewEmailResponse.attachment);
                            }

                            if (viewEmailResponse.attachment.size() == 1) {
                                if (viewEmailResponse.attachment.get(0).answerContent.equals("")) {
                                    tvForm.setVisibility(View.GONE);
                                }

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
                Log.e(TAG, "Indox after connection url: " + url);
                Log.e(TAG, "email_id: " + eid);


                client.addFormPart("email_id", eid);
                client.addFormPart("review_status", "0");
                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "Indox response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
