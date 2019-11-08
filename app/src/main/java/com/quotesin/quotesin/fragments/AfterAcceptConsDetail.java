package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quotesin.quotesin.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AfterAcceptConsDetail extends Fragment implements View.OnClickListener {

    View view;
    String username, desc, deadline, tittle, location_code, b_name, eid, con_email, post_date, qid, delete_flag, enq_logo, update_visFlag;
    TextView tvpostDate, tvMsg, tvWeb, tvExpandText, tvCategory, tvService;
    TextView tvRefNo, tvFName, tvBAddress, tvTel, tvMobile, tvCEmail, tvTitle, tvDeadDate, tvForm;
    RecyclerView recyclerview;
    Button btn_Feedback;
    String price, consumer_add, consumer_phone;
    ImageView ivDocument, ivLocation2, ivInfo, ivEnqUserLogo, ivCall, ivEmail, ivWeb, ivLocation;

    LinearLayout LlExpandText;

    TextView tvSubject, tvB_name, tvPrice, tvDate, tvPriceDesc, tvRate, tvReady, tvDiscription, tvQuotePrice, tvQTitle, tvCompName, tvRecieveDate;

    ImageView ivQDocument, ivLogo;
    RelativeLayout relativeLayout;


    String TAG = this.getClass().getName();


    public AfterAcceptConsDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.after_accept_cons_detail, container, false);

        initViews();
        registerClickListener();
        getDataFromBundle();
        callApi();

        return view;
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

     /*   tvRecieveDate = view.findViewById(R.id.tvRecieveDate);
        tvCompName = view.findViewById(R.id.tvCompName);
        ivLogo = view.findViewById(R.id.ivLogo);
        ivQDocument = view.findViewById(R.id.ivQDocument);
        tvPriceDesc = view.findViewById(R.id.tvPriceDesc);
        tvRate = view.findViewById(R.id.tvRate);
        tvReady = view.findViewById(R.id.tvReady);
        tvDiscription = view.findViewById(R.id.tvDiscription);
        tvQuotePrice = view.findViewById(R.id.tvQuotePrice);
        tvQTitle = view.findViewById(R.id.tvQTitle);*/

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

        tvpostDate.setText("Posted : " + post_date.replace("-", "/"));

        tvDeadDate.setText(deadline);
        if (!TextUtils.isEmpty(desc))
            tvMsg.setText(desc);

    }


    private void registerClickListener() {
        ivLocation2.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivEmail.setOnClickListener(this);
        ivWeb.setOnClickListener(this);
    }


    private void callApi() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new EnquiryDetailAsyncTask().execute();

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLocation2:
                if (!TextUtils.isEmpty(consumer_add)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + consumer_add));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }

                break;

            case R.id.ivLocation:
                if (!TextUtils.isEmpty(consumer_add)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + tvBAddress.getText().toString()));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                ivcall.setData(Uri.parse("tel:" + consumer_phone));
                startActivity(ivcall);
                break;

        }
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


                            if (!jObject.isNull("contact")) {
                                JSONObject json2 = jObject.getJSONObject("contact");
                                {
                                    if (json2 != null) {

                                        String ref_no = json2.getString("quote_md5");
                                        Log.e(TAG, "ref_no" + ref_no);


                                       /* tvSubject.setText(json2.getString("subject"));
                                        tvB_name.setText(json2.getString("business_username"));

                                        tvQTitle.setText(json2.getString("subject"));
                                        tvCompName.setText(json2.getString("subject"));
                                        tvRecieveDate.setText(json2.getString("subject"));

                                        Picasso.get().load("http://dev.webmobril.services/quotesinapp/" + json2.getString("quote_image"));
*/
                                    /*    tvDiscription.setText(json2.getString("subject"));
                                        tvRate.setText(json2.getString("subject"));
                                        tvReady.setText(json2.getString("subject"));
                                        tvPriceDesc.setText(json2.getString("subject"));
                                        tvPriceDesc.setText(json2.getString("subject"));
*/

                                        final String quote_image = json2.getString("quote_image");

                                        if (!TextUtils.isEmpty(quote_image)) {
                                            Log.e("str", quote_image);

                                            String str1 = quote_image.substring(0, 4);
                                            Log.e("str", str1);

                                       /*     if (str1.equals("site")) {
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
                                            }*/
                                        }


                                        JSONObject json3 = json2.getJSONObject("contact");

                                        tvFName.setText(json3.getString("username"));
                                        tvBAddress.setText(json3.getString("address"));
                                        tvCEmail.setText(json3.getString("email"));
                                        tvMobile.setText(json3.getString("phone"));
                                        tvTel.setText(json3.getString("evening_phone"));

                                        consumer_add = json3.getString("address");
                                        consumer_phone = json3.getString("phone");

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
