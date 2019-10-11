package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.SubscriptionAdapter;
import com.quotesin.quotesin.model.SubscriptionModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.InItemClickListener;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.PayPalConfig;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;


public class SubscriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private String p_id, amt, subscription_name;

    float paymentAmount;
    RecyclerView recyclerView;
    ImageView ivBack;
    String intent, create_time, trn_id;

    ArrayList<SubscriptionModel> planModelArrayList = new ArrayList<>();
    SubscriptionAdapter subscriptionAdapter;

    public static final int PAYPAL_REQUEST_CODE = 123;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    public String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._subscription);

        initView();
        registerClickListener();
        callApi();

        Intent basic = new Intent(this, PayPalService.class);
        basic.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(basic);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        ivBack = findViewById(R.id.ivBack);
    }

    private void registerClickListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivBack) {
            finish();

           /* case R.id.tv_signupBasic:
                paymentAmount = "200";
                getPayment();
                break;
            case R.id.tv_signupPre:
                paymentAmount = "500";
                getPayment();
                break;*/
        }
    }


    private void callApi() {
        if (CommonMethod.isNetworkAvailable(SubscriptionActivity.this)) {
            new SubscriptionPlanAsyncTask().execute();

        } else {
            Toast.makeText(SubscriptionActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class SubscriptionPlanAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        String status = "";
        String responseMessage = "";
        String id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(SubscriptionActivity.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String url1 = APIUrl.subscription_plan + "?role_id=" + LoginPreferences.getActiveInstance(SubscriptionActivity.this).getRole_id();
                response = CustomHttpClient.executeHttpGet(url1);
                System.out.print(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "Subsc api response is " + response);
            if (response == null) {
                Toast.makeText(SubscriptionActivity.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");

                            planModelArrayList.clear();
                            for (int i = 0; i < jsonarry.length(); i++) {
                                JSONObject post = jsonarry.getJSONObject(i);

                                SubscriptionModel model = new SubscriptionModel();
                                model.setId(post.getString("id"));
                                model.setName(post.getString("name"));
                                model.setPrice(post.getString("price"));
                                model.setDescription(post.getString("description"));
                                model.setRole_id(post.getString("role_id"));
                                model.setSub_title(post.getString("sub_title"));

                                planModelArrayList.add(model);
                                Log.e(TAG, "id-->" + post.getString("id"));
                                p_id = post.getString("id");
                                amt = post.getString("price");
                                subscription_name = post.getString("name");
                            }
                            setupAdapter();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (status.equals("Failed")) {
                        Toast.makeText(SubscriptionActivity.this, responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SubscriptionActivity.this, "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }
    }

    private void setupAdapter() {
        if (planModelArrayList.size() > 0) {
            Log.e(TAG, "planModelArrayList  --> " + planModelArrayList);
            subscriptionAdapter = new SubscriptionAdapter(SubscriptionActivity.this, planModelArrayList, getItemClickListener());

            final LinearLayoutManager layoutManager = new LinearLayoutManager(SubscriptionActivity.this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(subscriptionAdapter);

            subscriptionAdapter.notifyDataSetChanged();
        }
    }

    public InItemClickListener getItemClickListener() {

        return new InItemClickListener() {
            @Override
            public void onItemPurchaseClick(View v, int position) {
                paymentAmount = Float.parseFloat(planModelArrayList.get(position).getPrice());

                if (AppData.getInstance().getSubsc_name().equalsIgnoreCase("FREE")) {
                    Intent intent = new Intent(SubscriptionActivity.this, HomeScreen.class);
                    startActivity(intent);
                } else {
                    getPayment();
                }
            }
        };
    }

    private void getPayment() {
        Log.e(TAG, "amt----" + paymentAmount);


        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "USD", "QuotesIn", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            mProgressD = ProgressD.show(SubscriptionActivity.this, "Connecting", false);
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
                Toast.makeText(SubscriptionActivity.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {

                        Intent intent = new Intent(SubscriptionActivity.this, HomeScreen.class);
                        startActivity(intent);

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");

                            if (jsonarry != null) {

                                for (int i = 0; i < jsonarry.length(); i++) {


                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (status.equals("Failed")) {
                        Toast.makeText(SubscriptionActivity.this, responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SubscriptionActivity.this, "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }


        private String callService() {
            String url = APIUrl.AFTER_TRANSACTION;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "AFTER_TRANSACTION after connection url: " + url);

                Log.e(TAG, "bid" + LoginPreferences.getActiveInstance(SubscriptionActivity.this).getId());
                Log.e(TAG, "subscription_paln_id" + AppData.getInstance().getSubsc_id());
                Log.e(TAG, "txn_id" + trn_id);
                Log.e(TAG, "mc_gross" + String.valueOf(paymentAmount));
                Log.e(TAG, "payment_date" + create_time);
                Log.e(TAG, "role_id" + LoginPreferences.getActiveInstance(SubscriptionActivity.this).getRole_id());


                client.addFormPart("business_id", LoginPreferences.getActiveInstance(SubscriptionActivity.this).getId());
                client.addFormPart("subscription_paln_id", AppData.getInstance().getSubsc_id());
                client.addFormPart("payment_status", "Pending");
                client.addFormPart("payer_email", "");
                client.addFormPart("first_name", "");
                client.addFormPart("last_name", "");
                client.addFormPart("address_name", "");
                client.addFormPart("address_street", "");
                client.addFormPart("address_city", "");
                client.addFormPart("address_zip", "");
                client.addFormPart("txn_id", trn_id);
                client.addFormPart("mc_gross", String.valueOf(paymentAmount));
                client.addFormPart("payment_gross", String.valueOf(paymentAmount));
                client.addFormPart("pending_reason", "");
                client.addFormPart("payment_date", "2019-08-09");
                client.addFormPart("role_id", LoginPreferences.getActiveInstance(SubscriptionActivity.this).getRole_id());


                client.finishMultipart();

                response = client.getResponse();

                Log.e(TAG, "AFTER_TRANSACTION response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();

    }
}
