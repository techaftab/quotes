package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.CommiListAdapter;
import com.quotesin.quotesin.model.CommisListResponse;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.PayPalConfig;
import com.quotesin.quotesin.utils.ProgressD;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;
import static java.util.Objects.requireNonNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class Commision_list extends Fragment {

    private static int sum;
    View view;
    RecyclerView recyclerview;
    public CommisListResponse commisListResponse;
    public CommiListAdapter commiListAdapter;
    ImageView ivEmpty;

    String TotalAmt;
    Button btnPay;
    String intent, create_time, trn_id;
    public String TAG = this.getClass().getSimpleName();

    public static final int PAYPAL_REQUEST_CODE = 123;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    public Commision_list() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.commision_list, container, false);

        recyclerview = view.findViewById(R.id.recyclerview);
        ivEmpty = view.findViewById(R.id.ivEmpty);
        btnPay = view.findViewById(R.id.btnPay);
        callApi();

        Intent basic = new Intent(getActivity(), PayPalService.class);
        basic.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        requireNonNull(getActivity()).startService(basic);

        //  Log.e("TAG",TotalAmt);
        //
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(TotalAmt)) {
                    if (!TotalAmt.equalsIgnoreCase("0")) {
                        getPayment();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Payable Amount", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    private void callApi() {
        if (CommonMethod.isNetworkAvailable(requireNonNull(getActivity()))) {
            hideSoftKeyboard(getActivity());
            new GetCategoryAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getPayment() {
        Log.e(TAG, "amt----" + TotalAmt);


        PayPalPayment payment = new PayPalPayment(new BigDecimal(TotalAmt), "USD", "QuotesIn", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);

                        JSONObject jObject = new JSONObject(paymentDetails);
                        JSONObject jsonObject = jObject.getJSONObject("response");

                        Log.e(TAG, "intent" + jsonObject.getString("intent"));

                        intent = jsonObject.getString("intent");
                        trn_id = jsonObject.getString("id");
                        create_time = jsonObject.getString("create_time");

                        new AfterTransAsyncTask().execute();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class GetCategoryAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = APIUrl.COMMISSION_LISTING;

                ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
                postData.add(new BasicNameValuePair("business_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username()));

                result = CustomHttpClient.executeHttpPost(url, postData);

                System.out.print(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("COMMISSION_LISTING--", "get COMMISSION_LISTING  response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Gson gson = new Gson();
                    commisListResponse = gson.fromJson(result, CommisListResponse.class);
                    String count = String.valueOf(commisListResponse.count).trim();

                    if (count.equalsIgnoreCase("0")) {
                        ivEmpty.setVisibility(View.VISIBLE);
                        btnPay.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.GONE);
                    }

                    if (commisListResponse.status.equals("Success")) {

                        if (commisListResponse.result != null) {

                            ArrayList<Integer> amountArrayList = new ArrayList<>();
                            ArrayList<String> statusArrayList = new ArrayList<>();

                            List<Integer> amtArrayList = new ArrayList<>();

                            for (int i = 0; i < commisListResponse.result.size(); i++) {

                                statusArrayList.add(commisListResponse.result.get(i).serviceStatus);

                                if (commisListResponse.result.get(i).serviceStatus != null)
                                    if (commisListResponse.result.get(i).serviceStatus.equals("1")) {
                                        amtArrayList.add(Integer.valueOf(commisListResponse.result.get(i).commissionAmount));
                                        sum(amtArrayList);
                                    }

                            }

                            Log.e("Status--", statusArrayList.toString());
                            Log.e("amt--", amountArrayList.toString());
                            Log.e("amtArrayList--", amtArrayList.toString());

                            commiListAdapter = new CommiListAdapter(getActivity(), commisListResponse.result);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerview.setLayoutManager(layoutManager);
                            recyclerview.setAdapter(commiListAdapter);
                            recyclerview.setHasFixedSize(true);
                            commiListAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mProgressD.dismiss();
            }
        }

        @SuppressLint("SetTextI18n")
        public int sum(List<Integer> list) {
            sum = 0;
            for (int i : list) {
                sum += i;
                TotalAmt = String.valueOf(sum);

                btnPay.setVisibility(View.VISIBLE);
                btnPay.setText("Payable Amt : " + TotalAmt);
            }
            return sum;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class AfterTransAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";
        String id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                response = callService();
                return response;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "AFTER_TRANSACTION response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {

                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();

                        finishCurrentFragment();

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            if (jsonarry != null) {

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
            String url = APIUrl.commission_payment;

            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "AFTER_TRANSACTION after connection url: " + url);

                Log.e(TAG, "bid" + LoginPreferences.getActiveInstance(getActivity()).getId());
                Log.e(TAG, "subscription_paln_id" + AppData.getInstance().getSubsc_id());
                Log.e(TAG, "txn_id" + trn_id);
                Log.e(TAG, "mc_gross" + TotalAmt);
                Log.e(TAG, "payment_date" + create_time);
                Log.e(TAG, "role_id" + LoginPreferences.getActiveInstance(getActivity()).getRole_id());


                client.addFormPart("business_id", LoginPreferences.getActiveInstance(getActivity()).getId());
                client.addFormPart("payment_status", "Pending");
                client.addFormPart("payer_email", "");
                client.addFormPart("first_name", "");
                client.addFormPart("last_name", "");
                client.addFormPart("address_name", "");
                client.addFormPart("address_street", "");
                client.addFormPart("address_city", "");
                client.addFormPart("address_zip", "");
                client.addFormPart("txn_id", trn_id);
                client.addFormPart("mc_gross", String.valueOf(TotalAmt));
                client.addFormPart("payment_gross", String.valueOf(TotalAmt));
                client.addFormPart("pending_reason", "");
                client.addFormPart("payment_date", create_time);
                client.addFormPart("role_id", LoginPreferences.getActiveInstance(getActivity()).getRole_id());
                client.addFormPart("business_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username());


                client.finishMultipart();
                response = client.getResponse();

                Log.e(TAG, "AFTER_TRANSACTION response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

    }

    private void finishCurrentFragment() {
        int backStackCount = requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount >= 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requireNonNull(getActivity()).setTitle("Commission");
    }

    @Override
    public void onDestroy() {
        requireNonNull(getActivity()).stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
}
