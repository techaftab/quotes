package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.adapter.EnqIndox_Adapter;
import com.quotesin.quotesin.adapter.QuotesDescAdapter;
import com.quotesin.quotesin.model.EnqIndox_Model;
import com.quotesin.quotesin.model.QuotesDescModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.GetDeleteItem;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.quotesin.quotesin.adapter.EnqIndox_Adapter.popupWindow;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inbox_Detail extends Fragment implements View.OnClickListener, GetDeleteItem {

    public static Inbox_Detail indoxDetail;
    private TextView tvTitle;
    RecyclerView recyclerView, recyclerviewQuotes;
    View view;
    private RelativeLayout RlReply;
    private String username, desc, deadline, tittle, location_code, id, enq_logo, enq_status, status, q_count, enquiry_postalcode;
    String TAG = this.getClass().getSimpleName();
    private ImageView ivBack, ivDelete, ivReply;
    private ImageView ivInfo, ivDocument, ivLocation, ivViewForm, ivNoQuote;
    EnqIndox_Adapter enqIndoxAdapter;

    private TextView tv1, tv2;


    QuotesDescAdapter quotesDescAdapter;
    ArrayList<QuotesDescModel> quotesDescModelArrayList = new ArrayList<>();

    ArrayList<EnqIndox_Model> enqIndoxModelArrayList = new ArrayList<>();
    EnqIndox_Model enqIndoxModel = new EnqIndox_Model();

    public Inbox_Detail() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox__detail, container, false);

        indoxDetail = Inbox_Detail.this;

        initView();
        getDataFromBundle();
        registerClickListener();
        callApi();

        return view;
    }

    private void getDataFromBundle() {

        Bundle b = this.getArguments();
        assert b != null;
        id = b.getString("id");
        username = b.getString("username");
        desc = b.getString("desc");
        deadline = b.getString("deadline");
        tittle = b.getString("tittle");
        location_code = b.getString("location_code");
        enq_logo = b.getString("enq_logo");
        enq_status = b.getString("enq_status");
        status = b.getString("status");
        q_count = b.getString("q_count");
        AppData.getInstance().setEid(id);

        AppData.getInstance().setReplyDots(q_count);

        Log.e(TAG, "username--" + username);
        Log.e(TAG, "desc--" + desc);
        Log.e(TAG, "deadline--" + deadline);
        Log.e(TAG, "tittle--" + tittle);
        Log.e(TAG, "location_code--" + location_code);
        Log.e(TAG, "id--" + id);
        Log.e(TAG, "enq_logo--" + enq_logo);
        Log.e(TAG, "enq_status--" + enq_status);

        tvTitle.setText(tittle);

        if (status.equals("1")) {
            RlReply.setVisibility(View.GONE);
        } else {
            RlReply.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerviewQuotes = view.findViewById(R.id.recyclerviewQuotes);
        tvTitle = view.findViewById(R.id.tvTitle);
        ivBack = view.findViewById(R.id.ivBack);
        ivDelete = view.findViewById(R.id.ivDelete);
        ivReply = view.findViewById(R.id.ivReply);
        ivViewForm = view.findViewById(R.id.ivViewForm);
        RlReply = view.findViewById(R.id.RlReply);

        ivInfo = view.findViewById(R.id.ivInfo);
        ivDocument = view.findViewById(R.id.ivDocument);
        ivLocation = view.findViewById(R.id.ivLocation);

        ivNoQuote = view.findViewById(R.id.ivNoQuote);

        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
    }

    private void registerClickListener() {
        ivBack.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivReply.setOnClickListener(this);
        ivViewForm.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
    }

    private void callApi() {
        if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            new EnquiryDetailAsyncTask().execute();
            new QuotesDetailAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivViewForm:
                HomeScreen activity1 = (HomeScreen) v.getContext();
                view_form viewFeedback = new view_form();
                activity1.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, viewFeedback).addToBackStack(null).commit();
                break;

            case R.id.ivBack:
                finishCurrentFragment();
                break;

            case R.id.ivDelete:
                if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                    new DeleteEnquiryAsyncTask(id).execute();
                } else {
                    Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.ivReply:
                if (!enq_status.equals("2")) {
                    if (!username.equals(LoginPreferences.getActiveInstance(getActivity()).getUser_username())) {
                        Bundle bundle = new Bundle();
                        HomeScreen activity = (HomeScreen) v.getContext();
                        Enquiry_Reply enquiry_reply = new Enquiry_Reply();
                        bundle.putString("mail_id", id);
                        bundle.putString("update_quote", "no");
                        enquiry_reply.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, enquiry_reply).addToBackStack(null).commit();
                        Log.e("M_id", id);
                        Log.e("update_quote", "mo");
                    } else {
                        CommonMethod.showAlert("You Can't Reply to your Enquiry", getActivity());
                    }
                } else {
                    CommonMethod.showAlert("User Cancel this Enquiry", getActivity());
                }
                break;

            case R.id.ivLocation:
                if (!TextUtils.isEmpty(enquiry_postalcode)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + enquiry_postalcode));
                    if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;
        }


    }

    private void finishCurrentFragment() {
        int backStackCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount >= 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void GetClickedItem(String mail_id) {
        if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            new DeleteEnquiryAsyncTask(mail_id).execute();
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void setAdapter(ArrayList<EnqIndox_Model> indoxEnquiryModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + indoxEnquiryModelArrayList.size());
        enqIndoxAdapter = new EnqIndox_Adapter(getActivity(), enqIndoxModelArrayList, this, enq_status);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(enqIndoxAdapter);

    }

    private void setQuotesAdapter(ArrayList<QuotesDescModel> quotesDescModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + quotesDescModelArrayList.size());
        quotesDescAdapter = new QuotesDescAdapter(getActivity(), quotesDescModelArrayList, status);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerviewQuotes.setLayoutManager(layoutManager);
        recyclerviewQuotes.setAdapter(quotesDescAdapter);

    }

    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().show();

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onResume() {
        Objects.requireNonNull(getActivity()).setTitle("Inbox");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).hide();
        super.onResume();
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
                            enqIndoxModelArrayList.clear();
                            enqIndoxModel.setMail_id(jsonObject.getString("id"));
                            enqIndoxModel.setEnquiry_title(jsonObject.getString("enquiry_title"));
                            enqIndoxModel.setEnquiry_description(jsonObject.getString("enquiry_description"));
                            enqIndoxModel.setEnquiry_post_date(jsonObject.getString("enquiry_post_date"));
                            enqIndoxModel.setEnquiry_expired(jsonObject.getString("enquiry_expired"));

                            enqIndoxModel.setService_id(jsonObject.getString("service_id"));
                            enqIndoxModel.setUser_id(jsonObject.getString("user_id"));
                            enqIndoxModel.setConsumer_username(jsonObject.getString("consumer_username"));
                            enqIndoxModel.setConsumer_type(jsonObject.getString("consumer_type"));
                            enqIndoxModel.setConsumer_email(jsonObject.getString("consumer_email"));
                            enqIndoxModel.setLocation_code(jsonObject.getString("location_code"));
                            enqIndoxModel.setEnq_logo(jsonObject.getString("enquiry_loco"));


                            enq_logo = jsonObject.getString("enquiry_loco");
                            enquiry_postalcode = jsonObject.getString("enquiry_postalcode");

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


                            JSONArray jsonArrayAttach = jObject.getJSONArray("attachment");
                            if (jsonArrayAttach.length() <= 1) {
                                String answer = null;
                                for (int i = 0; i < jsonArrayAttach.length(); i++) {
                                    JSONObject attachDetail = jsonArrayAttach.getJSONObject(i);
                                    answer = attachDetail.getString("answer_question");
                                }
                                if (TextUtils.isEmpty(answer)) {
                                    ivInfo.setImageResource(R.mipmap.no_attachform);
                                    ivInfo.setEnabled(false);
                                } else {
                                    ivInfo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AppData.getInstance().setEid(id);
                                            HomeScreen activity1 = (HomeScreen) getContext();
                                            view_form viewForm = new view_form();
                                            assert activity1 != null;
                                            activity1.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, viewForm).addToBackStack(null).commit();

                                        }
                                    });
                                }
                            }


                            enqIndoxModelArrayList.add(enqIndoxModel);
                            if (enqIndoxModelArrayList.size() > 0) {
                                setAdapter(enqIndoxModelArrayList);
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
                Log.e(TAG, "email_id: " + id);


                client.addFormPart("email_id", id);
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
        private String response, count;
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

                    count = jObject.getString("count");


                    Log.e(TAG, "count--" + count);
                    if (!count.equals("0")) {
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                    }


                    if (status.equalsIgnoreCase("Success")) {
                        ivReply.setVisibility(View.GONE);
                        RlReply.setVisibility(View.GONE);

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            if (jsonarry.length()==0) {
                                tv1.setVisibility(View.GONE);
                                tv2.setVisibility(View.GONE);

                            }
                            quotesDescModelArrayList.clear();
                            for (int i = 0; i < jsonarry.length(); i++) {
                                JSONObject data = jsonarry.getJSONObject(i);

                                QuotesDescModel quotesDescModel = new QuotesDescModel();
                                quotesDescModel.setQ_id(data.getString("id"));
                                quotesDescModel.setE_id(data.getString("enquiry_id"));
                                quotesDescModel.setBusiness_username(data.getString("business_username"));
                                quotesDescModel.setQuote_image(data.getString("quote_image"));
                                quotesDescModel.setQuote_price(data.getString("quote_price"));

                                quotesDescModel.setQuote_message(data.getString("quote_message"));
                                quotesDescModel.setQuote_date(data.getString("quote_date"));
                                quotesDescModel.setQuote_won(data.getString("quote_won"));
                                quotesDescModel.setQuote_status(data.getString("quote_status"));
                                quotesDescModel.setSubject(data.getString("subject"));
                                quotesDescModel.setUser_id(data.getString("user_id"));
                                quotesDescModel.setQ_price_type(data.getString("q_price_type"));
                                quotesDescModel.setQ_occur(data.getString("q_occur"));
                                quotesDescModel.setQ_start(data.getString("q_start"));
                                quotesDescModel.setCreateDate(data.getString("created_at"));


                                /*JSONArray jsonarry1 = jObject.getJSONArray("attachment");
                                if (jsonarry1 != null) {
                                    JSONObject data1 = jsonarry1.getJSONObject(i);
                                    String ans = data1.getString("answer_question");
                                    Log.e(TAG, "ksj");

                                    if (ans.equals("")) {
                                        ivViewForm.setVisibility(View.GONE);
                                    }

                                }*/


                                JSONObject jsonObject = data.getJSONObject("user");
                                {
                                    quotesDescModel.setBusiness_logo(jsonObject.getString("business_logo"));
                                }
                                quotesDescModelArrayList.add(quotesDescModel);
                            }
                            if (quotesDescModelArrayList.size() > 0) {
                                setQuotesAdapter(quotesDescModelArrayList);
                            }
                            Log.e(TAG, "size" + quotesDescModelArrayList.size());
                            if (quotesDescModelArrayList.size() == 0) {
                                tv1.setVisibility(View.GONE);
                                tv2.setVisibility(View.GONE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (status.equals("Failed")) {
                        //  Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
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
            String url = APIUrl.Quotes_Details;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "QuotesDetailAsyncTask after connection url: " + url);

                Log.e(TAG, "enq_id: " + id);
                client.addFormPart("enq_id", id);
                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());

                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "QuotesDetailAsyncTask response :" + response);
                Log.e(TAG, "QuotesDetailAsyncTask response :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteEnquiryAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        String mail_id;
        private String response;
        private String status = "";
        private String responseMessage = "";

        DeleteEnquiryAsyncTask(String mail_id) {
            this.mail_id = mail_id;
        }

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
                    if (status.equalsIgnoreCase("success")) {

                        finishCurrentFragment();
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");


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
            String url = APIUrl.Delete_Enq;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "DeleteAsyncTask after connection url: " + url);

                Log.e(TAG, "enq_id: " + id);

                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("enquiry_id", id);
                client.addFormPart("review_status", "2");

                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "DeleteAsyncTask response :" + response);
                Log.e(TAG, "DeleteAsyncTask response :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}

