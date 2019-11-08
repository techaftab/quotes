package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SocialLogin_Type extends AppCompatActivity implements View.OnClickListener {
    Button btn_business, btn_consumer;
    String user_id, user_email, user_profile, user_name, type;

    String TAG = this.getClass().getSimpleName();
 //   String gid;
    String id1, username1, email1, profile_pic1, role_id1, role_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_login__type);

        initViews();
        registerClickListener();

        Intent intent = getIntent();
        if (intent != null) {
            user_id = intent.getStringExtra("user_id");
            user_email = intent.getStringExtra("user_email");
            user_profile = intent.getStringExtra("user_profile");
            user_name = intent.getStringExtra("user_name");
            type = intent.getStringExtra("type");

            Log.e(TAG, "user_id" + user_id);
            Log.e(TAG, "user_email" + user_email);
            Log.e(TAG, "user_profile" + user_profile);
            Log.e(TAG, "user_name" + user_name);
            Log.e(TAG, "type" + type);

        }
    }

    private void initViews() {
        btn_business =  findViewById(R.id.btn_business);
        btn_consumer =  findViewById(R.id.btn_consumer);
    }

    private void registerClickListener() {
        btn_business.setOnClickListener(this);
        btn_consumer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_business:
                CallBussinessApi("5");
                role_id = "5";
                break;

            case R.id.btn_consumer:
                CallBussinessApi("2");
                role_id = "2";
                break;
        }
    }

    private void CallBussinessApi(String s) {
        if (CommonMethod.isNetworkAvailable(SocialLogin_Type.this)) {
            CommonMethod.hideSoftKeyboard(SocialLogin_Type.this);
            new LoginAsyncTask(s).execute();
        } else {
            Toast.makeText(SocialLogin_Type.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class LoginAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
       // private String result;
        String status = "";
        String responseMessage = "";
        String available = "";
        String id, acc_type;
        private String response;


        LoginAsyncTask(String s) {
            this.acc_type = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(SocialLogin_Type.this, "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "Social login api response is " + result);
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    LoginPreferences.getActiveInstance(SocialLogin_Type.this).setRole_id(role_id);
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    available = jObject.getString("available");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            id1 = c.getString("id");
                            username1 = c.getString("name");
                            email1 = c.getString("business_email");
                            profile_pic1 = c.getString("business_logo");
                            role_id1 = c.getString("role_id");

                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setId(id1);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setFullname(email1);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setUser_username(username1);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setUser_first_name(username1);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setUser_profile_pic(profile_pic1);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setRole_id(role_id1);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setIsLoggedIn(true);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setAlready_register(available);


                            Log.e(TAG, "id :" + LoginPreferences.getActiveInstance(SocialLogin_Type.this).getId());
                            Log.e(TAG, "Fullname :" + LoginPreferences.getActiveInstance(SocialLogin_Type.this).getFullname());
                            Log.e(TAG, "email :" + LoginPreferences.getActiveInstance(SocialLogin_Type.this).getUser_username());
                            Log.e(TAG, "isLogin :" + LoginPreferences.getActiveInstance(SocialLogin_Type.this).getIsLoggedIn());
                            Log.e(TAG, "available :" + LoginPreferences.getActiveInstance(SocialLogin_Type.this).getAlready_register());
                        }

                        if (available.equals("1")) {
                            Intent i = new Intent(SocialLogin_Type.this, HomeScreen.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            LoginPreferences.getActiveInstance(SocialLogin_Type.this).setProfileScreen("3");
                            overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                            startActivity(i);
                            finish();
                        } else {
                            if (role_id1.equals("5")) {
                                Intent i = new Intent(SocialLogin_Type.this, CountryService.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(SocialLogin_Type.this, ConsSelectCountry.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                startActivity(i);
                            }
                        }


                    } else if (status.equals("failed")) {
                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = null;
            if (type != null) {
                switch (type) {
                    case "gm":
                        url = APIUrl.google_login;
                        break;
                    case "fb":
                        url = APIUrl.fb_login;
                        break;
                    case "tw":
                        url = APIUrl.twitter_login;
                        break;
                }
            }

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);

            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);

                if (type.equals("gm")) {
                    Log.e(TAG, "id is :" + user_id);
                    Log.e(TAG, "username is :" + user_name);
                    Log.e(TAG, "gmailEmail is :" + user_email);
                    Log.e(TAG, "gmailPhotoUrl is :" + user_profile);
                    Log.e(TAG, "role_id is :" + acc_type);

                    client.addFormPart("gmailId", "null");
                    client.addFormPart("profile_pic", user_profile);
                    client.addFormPart("gmailName", user_name);
                    client.addFormPart("gmailEmail", user_email);
                    client.addFormPart("role_id", acc_type);

                    client.addFormPart("device_id", LoginPreferences.getActiveInstance(SocialLogin_Type.this).getDeviceToken());
                    client.addFormPart("device_type", "1");

                } else if (type.equals("fb")) {
                    Log.e(TAG, "id is :" + user_id);
                    Log.e(TAG, "username is :" + user_name);
                    Log.e(TAG, "gmailEmail is :" + user_email);
                    Log.e(TAG, "gmailPhotoUrl is :" + user_profile);
                    Log.e(TAG, "role_id is :" + acc_type);

                    client.addFormPart("user_id", user_id);
                    client.addFormPart("user_profile", user_profile);
                    client.addFormPart("user_name", user_name);
                    client.addFormPart("user_email", user_email);
                    client.addFormPart("role_id", acc_type);
                    client.addFormPart("device_id", LoginPreferences.getActiveInstance(SocialLogin_Type.this).getDeviceToken());
                    client.addFormPart("device_type", "1");
                } else {
                    Log.e(TAG, "id is :" + AppData.getInstance().getTwId());
                    Log.e(TAG, "username is :" + AppData.getInstance().getTwName());
                    Log.e(TAG, "gmailEmail is :" + AppData.getInstance().getTwEmail());
                    Log.e(TAG, "gmailPhotoUrl is :" + AppData.getInstance().getTwPhotoUrl());
                    Log.e(TAG, "role_id is :" + acc_type);

                    client.addFormPart("twitter_id", AppData.getInstance().getTwId());
                    client.addFormPart("profile_pic", AppData.getInstance().getTwPhotoUrl());
                    client.addFormPart("fullname", AppData.getInstance().getTwName());
                    client.addFormPart("email", AppData.getInstance().getTwEmail());
                    client.addFormPart("role_id", acc_type);

                    client.addFormPart("device_id", LoginPreferences.getActiveInstance(SocialLogin_Type.this).getDeviceToken());
                    client.addFormPart("device_type", "1");
                }
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
