package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.adapter.Rec_QuotesAdapter;
import com.quotesin.quotesin.model.QuotesDescModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.GetMyItem;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class RQuotes_Detail extends Fragment implements GetMyItem, View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    public static RQuotes_Detail quotes_detail;
    public ArrayList<Float> priceArrayList = new ArrayList<>();
    View view;
    TextView tv1, tv2, tvDesc, tvDeadDate, tvDate, tvEmail, tvName, tvFName, tvTitle;
    ImageView title_img, ivDelete, ivDownload, ivEnqDocument, ivEnqInfo, ivEnqLocation;
    String image_letter;
    String userFlag, username, desc, deadline, tittle, location_code, b_name, eid, con_email, post_date, qid, delete_flag, enq_logo, update_visFlag;
    String TAG = this.getClass().getSimpleName();
    Rec_QuotesAdapter recQuotesAdapter;
    ArrayList<QuotesDescModel> quotesDescModelArrayList = new ArrayList<>();
    ArrayList<QuotesDescModel> quotesDescModelArrayList2 = new ArrayList<>();
    ArrayList<QuotesDescModel> filterArraylist = new ArrayList<>();
    RecyclerView recyclerview;
    TextView tvCEmail, tvMobile, tvTel, tvBAddress;
    LinearLayout LLFormDetail;
    String enquiry_postalcode;
    String[] language = {"Sort by best price", "Sort by Attachment"};
    Spinner spFilter;

    LinearLayout llSpinner;

    public RQuotes_Detail() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.rquotes__detail, container, false);
        quotes_detail = RQuotes_Detail.this;

        initViews();
        getDataFromBundle();
        registerclickListener();
        callApi();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, language);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(adapter);

        AdapterView.OnItemSelectedListener countrySelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {

               /* Collections.reverse(priceArrayList);
                Log.e(TAG, "arraylist2-" + priceArrayList);

                recQuotesAdapter.notifyDataSetChanged();*/

                if (position == 0) {
                    Log.e(TAG, " position 0 ");
                    priceFilter();
                } else if (position == 1) {
                    Log.e(TAG, " position 1 ");
                    quoteFilter();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        spFilter.setOnItemSelectedListener(countrySelectedListener);


        return view;
    }

    private void quoteFilter() {
        Log.e(TAG, "filter quoteFilter");
        quotesDescModelArrayList2.clear();
        recyclerview.removeAllViews();

        for (int i = 0; i < quotesDescModelArrayList.size(); i++) {
            if (quotesDescModelArrayList.get(i).getHaveQuote().equalsIgnoreCase("Y")) {
                quotesDescModelArrayList2.add(quotesDescModelArrayList.get(i));

                Log.e(TAG, "filter priceFilter : " + quotesDescModelArrayList.get(i).getHaveQuote());
            }
        }
        setQuotesAdapter(quotesDescModelArrayList2);
    }

    private void priceFilter() {
        Log.e(TAG, "filter priceFilter");
        quotesDescModelArrayList2.clear();
        recyclerview.removeAllViews();

        Collections.sort(quotesDescModelArrayList);
        setQuotesAdapter(quotesDescModelArrayList);
    }


    private void callApi() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new QuotesDetailAsyncTask().execute();
            new EnquiryDetailAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromBundle() {
        Bundle b = this.getArguments();
        assert b != null;
        //  qid = b.getString("qid");
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
        userFlag = b.getString("userFlag");


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
        tvDate.setText(post_date);
        tvDeadDate.setText(deadline);
        tvEmail.setText(con_email);
        tvName.setText(username);
        tvDesc.setText(desc);

        image_letter = String.valueOf(tittle.charAt(0));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(image_letter, Color.parseColor("#168add"));
        title_img.setImageDrawable(drawable);


        if (delete_flag.equals("no")) {
            ivDelete.setVisibility(View.GONE);
        } else {
            ivDelete.setVisibility(View.VISIBLE);
        }
    }

    private void registerclickListener() {
        ivDelete.setOnClickListener(this);
        ivDownload.setOnClickListener(this);
        ivEnqLocation.setOnClickListener(this);
        ivEnqInfo.setOnClickListener(this);
        ivEnqDocument.setOnClickListener(this);

    }


    @Override
    public void GetClickedItem(String q_id, String userId) {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            if (userId.equals("reject"))
                new QuotesAcceptAsyncTask(userId, q_id).execute();
            else if (userId.equals("accept")) {
                Bundle bundle = new Bundle();
                HomeScreen activity = (HomeScreen) view.getContext();
                AfterQ_AcceptForm afterQAcceptForm = new AfterQ_AcceptForm();
                bundle.putString("eid", eid);
                bundle.putString("b_name", AppData.getInstance().getBname());
                bundle.putString("qid", q_id);
                getActivity().setTitle("Contact Details");
                afterQAcceptForm.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, afterQAcceptForm).addToBackStack(null).commit();
            } else {
                new ReadQuoteAsyncTask(q_id).execute();
            }

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    private void initViews() {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvDesc = view.findViewById(R.id.tvDesc);
        tvDeadDate = view.findViewById(R.id.tvDeadDate);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvName = view.findViewById(R.id.tvName);
        title_img = view.findViewById(R.id.gmailitem_letter);
        recyclerview = view.findViewById(R.id.recyclerview);
        ivDelete = view.findViewById(R.id.ivDelete);
        ivDownload = view.findViewById(R.id.ivDownload);
        LLFormDetail = view.findViewById(R.id.LLFormDetail);
        tvCEmail = view.findViewById(R.id.tvCEmail);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvTel = view.findViewById(R.id.tvTel);
        tvBAddress = view.findViewById(R.id.tvBAddress);
        tvFName = view.findViewById(R.id.tvFName);
        ivEnqDocument = view.findViewById(R.id.ivEnqDocument);
        ivEnqInfo = view.findViewById(R.id.ivEnqInfo);
        ivEnqLocation = view.findViewById(R.id.ivEnqLocation);

        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        spFilter = view.findViewById(R.id.spFilter);

        llSpinner = view.findViewById(R.id.llSpinner);
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.ivDelete:
                showAlert();

                break;

            case R.id.ivDownload:
                String url = "http://dev.webmobril.services/quotesinapp/" + enq_logo;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.ivEnqLocation:
                if (!TextUtils.isEmpty(enquiry_postalcode)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + enquiry_postalcode));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }

                break;

        }
    }


    private void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to delete this Enquiry!!").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (CommonMethod.isNetworkAvailable(getActivity())) {
                            new DeleteEnquiryAsyncTask(eid).execute();
                        } else {
                            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });


        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void finishCurrentFragment() {
        int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount >= 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void setQuotesAdapter(ArrayList<QuotesDescModel> quotesDescModelArrayList) {
        recyclerview.removeAllViews();
        Log.e(TAG, "quotesDescModelArrayList --> " + quotesDescModelArrayList.size());
        recQuotesAdapter = new Rec_QuotesAdapter(getActivity(), quotesDescModelArrayList, this, update_visFlag);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(recQuotesAdapter);
        recQuotesAdapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteEnquiryAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        String mail_id;
        private String response;
        private String status = "";
        private String responseMessage = "";

        public DeleteEnquiryAsyncTask(String mail_id) {
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
                    if (status.equalsIgnoreCase("Success")) {

                        finishCurrentFragment();
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();

                        try {

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
            String url = APIUrl.CANCEL_ENQUIRY;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "DeleteAsyncTask after connection url: " + url);
                Log.e(TAG, "DeleteAsyncTask response :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                Log.e(TAG, "enq_id: " + eid);

                client.addFormPart("enquiry_id", eid);


                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "DeleteAsyncTask response :" + response);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class QuotesAcceptAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        String q_id, status_accept;
        private String response;
        private String status = "";
        private String responseMessage = "";

        public QuotesAcceptAsyncTask(String status, String qid) {
            this.q_id = qid;
            this.status_accept = status;

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
                    if (status.equalsIgnoreCase("Success")) {
                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            if (jsonarry != null) {

                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);

                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (responseMessage.equalsIgnoreCase("Quote Rejected")) {
                            finishCurrentFragment();
                        } else if (responseMessage.equalsIgnoreCase("Your quote accepted")) {
                       /*     Bundle bundle = new Bundle();
                            HomeScreen activity = (HomeScreen) view.getContext();
                            AfterQ_AcceptForm afterQAcceptForm = new AfterQ_AcceptForm();
                            bundle.putString("eid", AppData.getInstance().getEid());
                            bundle.putString("b_name", AppData.getInstance().getBname());
                            bundle.putString("qid", AppData.getInstance().getQid());
                            getActivity().setTitle("Contact Details");
                            afterQAcceptForm.setArguments(bundle);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, afterQAcceptForm).addToBackStack(null).commit();

*/
                        }
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
            String url = APIUrl.reject_quote;

            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();

                Log.e(TAG, "accept_quote after connection url: " + url);
                Log.e(TAG, "quote_id: " + q_id);
                Log.e(TAG, "business_username: " + AppData.getInstance().getBname());
                Log.e(TAG, "consumer_username: " + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                Log.e(TAG, "amt: " + AppData.getInstance().getQamt());

                client.addFormPart("quote_id", q_id);
                client.addFormPart("quote_invisible", "1");


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
    private class QuotesDetailAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
        private String status = "";
        private String responseMessage = "";
        private String count = "";

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
                        llSpinner.setVisibility(View.VISIBLE);

                    }

                    if (status.equalsIgnoreCase("Success")) {

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");

                            if (jsonarry != null) {
                                quotesDescModelArrayList.clear();
                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);
                                    Log.e("quote_price==", data.getString("quote_price"));

                                    QuotesDescModel quotesDescModel = new QuotesDescModel();

                                    quotesDescModel.setQ_id(data.getString("id"));
                                    quotesDescModel.setE_id(data.getString("enquiry_id"));
                                    quotesDescModel.setBusiness_username(data.getString("business_username"));
                                    quotesDescModel.setQuote_image(data.getString("quote_image"));
                                    quotesDescModel.setQuote_price(data.getString("quote_price"));
                                    quotesDescModel.setNewPrice(Float.valueOf(data.getString("quote_price")));
                                    quotesDescModel.setQuote_message(data.getString("quote_message"));
                                    quotesDescModel.setQuote_date(data.getString("quote_date"));
                                    quotesDescModel.setQuote_won(data.getString("quote_won"));
                                    quotesDescModel.setQuote_status(data.getString("quote_status"));
                                    quotesDescModel.setSubject(data.getString("subject"));
                                    quotesDescModel.setUser_id(data.getString("user_id"));
                                    quotesDescModel.setQ_price_type(data.getString("q_price_type"));
                                    quotesDescModel.setQ_occur(data.getString("q_occur"));
                                    quotesDescModel.setQ_start(data.getString("q_start"));
                                    quotesDescModel.setQuote_won_date(data.getString("quote_won_date"));

                                    Log.e(TAG, "haveQuote : " + data.getString("quote_image"));

                                    String quote = data.getString("quote_image").replace("null", "");

                                    if (quote.isEmpty()) {
                                        quotesDescModel.setHaveQuote("N");
                                    } else {
                                        quotesDescModel.setHaveQuote("Y");
                                    }

                                    Log.e(TAG, "myhaveQuote : " + quotesDescModel.getHaveQuote());

                                    float price = Float.valueOf(data.getString("quote_price"));

                                    priceArrayList.add(price);

                                    JSONObject jsonObject = data.getJSONObject("user");
                                    {
                                        quotesDescModel.setBusiness_logo(APIUrl.IMAGE_BASE_URL + jsonObject.getString("business_logo"));
                                        Log.e("user_img", APIUrl.IMAGE_BASE_URL + jsonObject.getString("business_logo"));

                                    }
                                    quotesDescModelArrayList.add(quotesDescModel);
                                }
                            }

                            if (quotesDescModelArrayList.size() > 0) {

                                setQuotesAdapter(quotesDescModelArrayList);
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
                            if (jsonObject != null) {

                                enq_logo = jsonObject.getString("enquiry_loco");
                                enquiry_postalcode = jsonObject.getString("enquiry_postalcode");

                                if (!TextUtils.isEmpty(enq_logo)) {
                                    String str = enq_logo;
                                    Log.e("enquiry_loco str", str);

                                    String str1 = str.substring(0, 4);
                                    Log.e("str--", str1);

                                    if (str1.equals("site")) {
                                        ivEnqDocument.setVisibility(View.VISIBLE);
                                        ivEnqDocument.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String url = "http://dev.webmobril.services/quotesinapp/" + enq_logo;
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(url));
                                                startActivity(i);
                                            }
                                        });
                                    } else {
                                        ivEnqDocument.setImageResource(R.mipmap.no_file);
                                        ivEnqDocument.setEnabled(false);
                                    }
                                }

                               /* JSONObject jsonObject1 = jsonObject.getJSONObject("service");
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
                                }*/
                            }

                            JSONArray jsonArrayAttach = jObject.getJSONArray("attachment");
                            if (jsonArrayAttach.length() <= 1) {
                                String answer = null;
                                for (int i = 0; i < jsonArrayAttach.length(); i++) {
                                    JSONObject attachDetail = jsonArrayAttach.getJSONObject(i);
                                    answer = attachDetail.getString("answer_question");
                                }
                                if (TextUtils.isEmpty(answer)) {
                                    ivEnqInfo.setImageResource(R.mipmap.no_attachform);
                                    ivEnqInfo.setEnabled(false);
                                } else {
                                    ivEnqInfo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AppData.getInstance().setEid(eid);
                                            HomeScreen activity1 = (HomeScreen) getContext();
                                            view_form viewForm = new view_form();
                                            activity1.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, viewForm).addToBackStack(null).commit();

                                        }
                                    });
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
    private class ReadQuoteAsyncTask extends AsyncTask<String, Void, String> {
        JSONObject jObject;
        String q_id;
        private String response;
        private String status = "";

        public ReadQuoteAsyncTask(String q_id) {
            this.q_id = q_id;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "QUOTE_READ api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    if (status.equalsIgnoreCase("Success")) {
                        try {
                            // Toast.makeText(getActivity(), "ksjsd", Toast.LENGTH_LONG).show();

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
        }

        private String callService() {

            String url = APIUrl.QUOTE_READ;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "QUOTE_READ after connection url: " + url);
                client.addFormPart("quote_id", q_id);

                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "QUOTE_READ response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
