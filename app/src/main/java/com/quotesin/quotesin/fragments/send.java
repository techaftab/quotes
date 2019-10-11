package com.quotesin.quotesin.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.adapter.Send_Enquiry;
import com.quotesin.quotesin.adapter.Send_Quotes;
import com.quotesin.quotesin.model.SendEnquiryModel;
import com.quotesin.quotesin.model.SendQuotesModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class send extends Fragment implements View.OnClickListener {

    public LinearLayout llEnquiry, LLQuote;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView1, recyclerView2;
    TextView tvConsumerEnquiries;
    Send_Enquiry sendEnquiry;
    ArrayList<SendEnquiryModel> sendEnquiryModelArrayList = new ArrayList<>();
    Send_Quotes sendQuotes;
    ArrayList<SendQuotesModel> quotesModelArrayList = new ArrayList<>();
    String TAG = this.getClass().getSimpleName();
    FrameLayout flEnquiry, flQuotes;
    boolean isLoading = false;
    RadioGroup toggle;
    RadioButton enquiry, quotes;
    ImageView ivEmpty, ivEmpty2;
    private SearchView searchView = null;
    private OnQueryTextListener queryTextListener;

    public send() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_send, container, false);

        setHasOptionsMenu(true);
        initViews();
        registerClickListener();

        Log.e(TAG, LoginPreferences.getActiveInstance(getActivity()).getRole_id());

        if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("2")) {
            toggle.setVisibility(View.GONE);
            tvConsumerEnquiries.setVisibility(View.VISIBLE);
            callApi();
        } else {
            toggle.setVisibility(View.VISIBLE);
            callApi();
            new SendQuotesAsyncTask().execute();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("2")) {
                    toggle.setVisibility(View.GONE);
                    tvConsumerEnquiries.setVisibility(View.VISIBLE);
                    callApi();

                } else {
                    callApi();
                    new SendQuotesAsyncTask().execute();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5")) {
                    if (enquiry.isChecked()) {
                        quotes.setTextColor(0XFF168ADD);
                        enquiry.setTextColor(Color.WHITE);
                        flEnquiry.setVisibility(View.VISIBLE);
                        flQuotes.setVisibility(View.GONE);

                    }
                    if (quotes.isChecked()) {
                        enquiry.setTextColor(0XFF168ADD);
                        quotes.setTextColor(Color.WHITE);
                        flEnquiry.setVisibility(View.GONE);
                        flQuotes.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return view;
    }

    private void initViews() {
        toggle = view.findViewById(R.id.toggle);
        quotes = view.findViewById(R.id.quotes);
        enquiry = view.findViewById(R.id.enquiry);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView1 = view.findViewById(R.id.recyclerview1);
        recyclerView2 = view.findViewById(R.id.recyclerview2);
        tvConsumerEnquiries = view.findViewById(R.id.tvConsumerEnquiries);

        flQuotes = view.findViewById(R.id.flQuotes);
        flEnquiry = view.findViewById(R.id.flContent);

        LLQuote = view.findViewById(R.id.LLQuote);
        ivEmpty2 = view.findViewById(R.id.ivEmpty2);
        llEnquiry = view.findViewById(R.id.llEnquiry);
        ivEmpty = view.findViewById(R.id.ivEmpty);
    }

    private void registerClickListener() {
        tvConsumerEnquiries.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        getActivity().setTitle("Sent");
        super.onResume();
    }

    private void callApi() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new SendEnquiryAsyncTask().execute();

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.indox_main, menu);

        menu.findItem(R.id.refresh).setVisible(true);
        menu.findItem(R.id.app_bar_search).setVisible(true);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    if (flEnquiry.getVisibility() == View.VISIBLE) {
                        if (sendEnquiryModelArrayList.size() != 0)
                            sendEnquiry.filter(String.valueOf(newText));
                    } else {
                        if (quotesModelArrayList.size() != 0)
                            sendQuotes.filter(String.valueOf(newText));
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    if (flEnquiry.getVisibility() == View.VISIBLE) {
                        if (sendEnquiryModelArrayList.size() != 0)
                            sendEnquiry.filter(String.valueOf(query));
                    } else {
                        if (quotesModelArrayList.size() != 0)
                            sendQuotes.filter(String.valueOf(query));
                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.app_bar_search:
                break;

            case R.id.refresh:
                if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("2")) {
                    toggle.setVisibility(View.GONE);
                    tvConsumerEnquiries.setVisibility(View.VISIBLE);
                    callApi();

                } else {
                    callApi();
                    new SendQuotesAsyncTask().execute();
                }
                return true;
        }

        return false;
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                // Not implemented here
                return false;

            case R.id.notifications:
                Toast.makeText(getActivity(), "Calls notifications Click", Toast.LENGTH_SHORT).show();
                return true;

            default:
                searchView.setOnQueryTextListener(queryTextListener);
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab1:
                HomeScreen activity = (HomeScreen) v.getContext();
                post_new_enquiry postNewEnquiry = new post_new_enquiry();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, postNewEnquiry).addToBackStack(null).commit();
                break;
        }
    }

    private void setAdapter(ArrayList<SendEnquiryModel> sendEnquiryModelArrayList) {
        Log.e(TAG, "sent_quotes_list --> " + sendEnquiryModelArrayList.size());
        sendEnquiry = new Send_Enquiry(getActivity(), sendEnquiryModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(sendEnquiry);
    }

    private void setAdapterQuotes(ArrayList<SendQuotesModel> quotesModelArrayList) {
        Log.e(TAG, "sendEnquiryModelArrayList --> " + quotesModelArrayList.size());
        sendQuotes = new Send_Quotes(getActivity(), quotesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setAdapter(sendQuotes);

    }

    private class SendEnquiryAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        String id;
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

            Log.e(TAG, "SendEnquiryAsyncTask api response is " + response);
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
                                sendEnquiryModelArrayList.clear();
                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);

                                    SendEnquiryModel sendEnquiryModel = new SendEnquiryModel();

                                    sendEnquiryModel.setEid(data.getString("id"));
                                    sendEnquiryModel.setEnquiry_title(data.getString("enquiry_title"));
                                    sendEnquiryModel.setEnquiry_description(data.getString("enquiry_description"));
                                    sendEnquiryModel.setEnquiry_post_date(data.getString("enquiry_post_date"));
                                    sendEnquiryModel.setEnquiry_expired(data.getString("enquiry_expired"));
                                    sendEnquiryModel.setUser_id(data.getString("user_id"));
                                    sendEnquiryModel.setConsumer_username(data.getString("consumer_username"));
                                    sendEnquiryModel.setConsumer_type(data.getString("consumer_type"));
                                    sendEnquiryModel.setConsumer_email(data.getString("consumer_email"));
                                    sendEnquiryModel.setEnquiry_status(data.getString("enquiry_status"));
                                    sendEnquiryModel.setLocation_code(data.getString("location_code"));
                                    sendEnquiryModel.setEnquiry_Image(data.getString("enquiry_loco"));

                                    sendEnquiryModelArrayList.add(sendEnquiryModel);
                                }
                            }
                            if (sendEnquiryModelArrayList.size() > 0) {
                                setAdapter(sendEnquiryModelArrayList);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (sendEnquiryModelArrayList.size() == 0) {
                        ivEmpty.setVisibility(View.VISIBLE);
                        llEnquiry.setVisibility(View.GONE);
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
            String url = APIUrl.sent_enqueries;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "sent_enqueries after connection url: " + url);
                Log.e(TAG, "user id== " + LoginPreferences.getActiveInstance(getActivity()).getId());

                client.addFormPart("user_id", LoginPreferences.getActiveInstance(getActivity()).getId());


                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "sent_eny response :" + response);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class SendQuotesAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        String id;
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

            Log.e(TAG, "SendQuotesAsyncTask api response is " + response);
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
                                quotesModelArrayList.clear();
                                for (int i = 0; i < jsonarry.length(); i++) {


                                    JSONObject data = jsonarry.getJSONObject(i);

                                    SendQuotesModel rQuotesModel = new SendQuotesModel();

                                    rQuotesModel.setId(data.getString("id"));
                                    rQuotesModel.setEnquiry_id(data.getString("enquiry_id"));
                                    rQuotesModel.setBusiness_username(data.getString("business_username"));
                                    rQuotesModel.setQuote_image(data.getString("quote_image"));
                                    rQuotesModel.setQuote_price(data.getString("quote_price"));
                                    rQuotesModel.setQuote_message(data.getString("quote_message"));
                                    rQuotesModel.setQuote_status(data.getString("quote_status"));
                                    rQuotesModel.setQuote_date(data.getString("quote_date"));
                                    rQuotesModel.setQ_won(data.getString("quote_won"));

                                    if (!data.isNull("enquiry")) {
                                        JSONObject json2 = data.getJSONObject("enquiry");
                                        {
                                            if (json2 != null) {

                                                rQuotesModel.setEnquiry_title(json2.getString("enquiry_title"));
                                                rQuotesModel.setEnquiry_description(json2.getString("enquiry_description"));

                                                rQuotesModel.setEnquiry_expired(json2.getString("enquiry_expired"));
                                                rQuotesModel.setEnquiry_status(json2.getString("enquiry_status"));

                                                rQuotesModel.setConsumer_username(json2.getString("consumer_username"));
                                                rQuotesModel.setConsumer_type(json2.getString("consumer_type"));
                                                rQuotesModel.setConsumer_email(json2.getString("consumer_email"));
                                                rQuotesModel.setEnquiry_Image(json2.getString("enquiry_Image"));
                                                rQuotesModel.setQ_count(json2.getString("quote_count"));


                                            }
                                        }
                                    }


                                    quotesModelArrayList.add(rQuotesModel);
                                }
                            }
                            if (quotesModelArrayList.size() > 0) {
                                setAdapterQuotes(quotesModelArrayList);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (quotesModelArrayList.size() == 0) {
                        ivEmpty2.setVisibility(View.VISIBLE);
                        LLQuote.setVisibility(View.GONE);
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
            String url = APIUrl.sent_quotes_list;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "SendQuotesAsyncTask after connection url: " + url);

                client.addFormPart("username", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("role_id", LoginPreferences.getActiveInstance(getActivity()).getRole_id());
                client.addFormPart("skip", "0");
                client.addFormPart("take", "500");
                client.addFormPart("Location_id", LoginPreferences.getActiveInstance(getActivity()).getLocationId());


                Log.e(TAG, "SendQuotesAsyncTask user_id :" + LoginPreferences.getActiveInstance(getActivity()).getId());
                Log.e(TAG, "SendQuotesAsyncTask user fiest :" + LoginPreferences.getActiveInstance(getActivity()).getUser_first_name());
                Log.e(TAG, "getUser_username :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());

                client.finishMultipart();

                response = client.getResponse();

                Log.e(TAG, "SendQuotesAsyncTask response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
