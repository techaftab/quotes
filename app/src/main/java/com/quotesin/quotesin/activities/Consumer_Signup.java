package com.quotesin.quotesin.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.config.Configuration;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONException;
import org.json.JSONObject;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Consumer_Signup extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etUserName, etEmail, etPhone, etPass,etConfirmPass, etaddress;
    Button btn_login;
    TextView tvSignin;

    String TAG = this.getClass().getSimpleName();
    String name, userName, email, phone, pass;
    String user_name, user_phone, id;
    String user_profile_pic, user_email;
    String Base_url = "https://www.webmobril.org/dev/quotesinapp/";
    String role_id = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_signup);

        initViews();
        registerClickListener();


    }

    private void initViews() {
        btn_login =  findViewById(R.id.btn_login);
        etName =  findViewById(R.id.etName);
        etUserName =  findViewById(R.id.etUserName);
        etEmail =  findViewById(R.id.etEmail);
        etPhone =  findViewById(R.id.etPhone);
        etPass =  findViewById(R.id.etPass);
        etConfirmPass = findViewById(R.id.etPass_confirm);
        etaddress = findViewById(R.id.etaddress);
        tvSignin =  findViewById(R.id.tvSignin);
    }

    private void registerClickListener() {
        btn_login.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
    }

    private boolean checkValidation() {
        userName = etUserName.getText().toString();
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();
        phone = etPhone.getText().toString();

        Log.e(TAG, "username is:" + userName);
        Log.e(TAG, "password is:" + pass);
        if (TextUtils.isEmpty(name)){
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_fullname));
            return false;
        }else if (etUserName.getText().toString().length() == 0) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_username));
            return false;
        }else if (etEmail.getText().toString().trim().length() == 0) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_email));
            return false;
        } else if (!CommonMethod.isValidEmaillId(etEmail.getText().toString().trim())) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_valid_email));
            return false;
        }  else if (TextUtils.isEmpty(etPhone.getText().toString())) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_phone));
            return false;
        } else if (etPhone.getText().toString().length()<10) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_valid_mobile));
            return false;
        }else if (etaddress.getText().toString().length() == 0) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_address));
            return false;
        }else if (etPass.getText().toString().length() == 0) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_password));
            return false;
        } else if (!Configuration.isPasswordValid(etPass.getText().toString())){
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_valid_password));
            return false;
        } else if (etPass.getText().toString().length() < 6 || etPass.getText().toString().length()>16) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_valid_password));
            return false;
        } else if (TextUtils.isEmpty(etConfirmPass.getText().toString())) {
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.enter_confirm_pass));
            return false;
        } else if (!TextUtils.equals(etPass.getText().toString(),etConfirmPass.getText().toString())){
            Configuration.openPrettyDialog(Consumer_Signup.this,getString(R.string.password_not_match));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkValidation()) {
                    if (CommonMethod.isNetworkAvailable(Consumer_Signup.this)) {
                        CommonMethod.hideSoftKeyboard(Consumer_Signup.this);
                        new ConsumerSignupAsyncTask().execute();
                    } else {
                        Toast.makeText(Consumer_Signup.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.tvSignin:
                Intent intent = new Intent(Consumer_Signup.this, Login.class);
                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                startActivity(intent);
                break;

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ConsumerSignupAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        String status = "";
        String responseMessage = "";
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(Consumer_Signup.this, "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "Consumer signup api response is " + result);
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Server Error Please try Again", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(result);
                    responseMessage = jObject.getString("message");
                    status = jObject.getString("Status");

                    if (status.equalsIgnoreCase("Success")) {
                        JSONObject jsonObjectData = jObject.getJSONObject("result");

                        Log.e(TAG, "jsonObjectData :" + jsonObjectData);
                        id = jsonObjectData.getString("id");
                        user_name = jsonObjectData.getString("business_username");
                        user_phone = jsonObjectData.getString("business_telephone");
                        user_profile_pic = jsonObjectData.getString("business_logo");
                        user_email = jsonObjectData.getString("business_email");
                        name = jsonObjectData.getString("name");
                        role_id = jsonObjectData.getString("role_id");

                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setId(id);
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setUser_first_name(name);
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setUser_profile_pic(Base_url + user_profile_pic);
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setUser_username(user_name);
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setEmail(user_email);
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setProfileScreen("0");
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setIsLoggedIn(true);
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setRole_id("2");
                        LoginPreferences.getActiveInstance(Consumer_Signup.this).setSocialLoginType("no");

                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(Consumer_Signup.this, ConsSelectCountry.class);
                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    } else if (status.equals("failed")) {
                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.signup;

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);

            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);
                Log.e(TAG, "business_username is :" + userName);
                Log.e(TAG, "business_email is :" + email);
                Log.e(TAG, "pass is :" + pass);
                Log.e(TAG, "phone is :" + phone);
                Log.e(TAG, "name is :" + name);

                Log.e(TAG, "token is :" + LoginPreferences.getActiveInstance(Consumer_Signup.this).getDeviceToken());

                client.addFormPart("business_username", userName);
                client.addFormPart("business_email", email);
                client.addFormPart("business_password", pass);
                client.addFormPart("business_number", phone);
                client.addFormPart("name", name);
                client.addFormPart("role_id", "2");
                client.addFormPart("address", etaddress.getText().toString());
                client.addFormPart("device_type", "1");
                client.addFormPart("device_id", LoginPreferences.getActiveInstance(Consumer_Signup.this).getDeviceToken());
                client.addFormPart("business_website","");

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
