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

import static com.quotesin.quotesin.utils.APIUrl.IMAGE_BASE_URL;
import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

public class Business_Signup extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etUserName, etEmail, etPhone, etPass,etConfirmPass, etaddress, etweb;
    Button btn_login;
    TextView tvSignin;

    String TAG = this.getClass().getSimpleName();
    String name, userName, email, phone, pass,website;
    String user_name, user_phone, id;
    String user_profile_pic, user_email;
    String role_id = "5";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_signup);
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
        tvSignin =  findViewById(R.id.tvSignin);
        etaddress = findViewById(R.id.etaddress);
        etConfirmPass = findViewById(R.id.etPass_bconfirm);
        etweb = findViewById(R.id.etweb);

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
        website=etweb.getText().toString();

        Log.e(TAG, "username is:" + userName);
        Log.e(TAG, "password is:" + pass);
        if (TextUtils.isEmpty(name)) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_bussiness_name));
            return false;
        }else if (etUserName.getText().toString().length() == 0) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_username));
            return false;
        } else if (etEmail.getText().toString().trim().length() == 0) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_email));
            return false;
        } else if (!CommonMethod.isValidEmaillId(etEmail.getText().toString().trim())) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_valid_email));
            return false;
        }else if (TextUtils.isEmpty(phone)) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_phone));
            return false;
        } else if (etPhone.getText().toString().length() <10) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_valid_mobile));
            return false;
        } else if (TextUtils.isEmpty(website)){
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_website));
            return false;
        } else if (etaddress.getText().toString().length() == 0) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_address));
            return false;
        } else if (etPass.getText().toString().length() == 0) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_password));
            return false;
        }else if (etPass.getText().toString().length() < 6 || etPass.getText().toString().length()>16) {
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_valid_password));
            return false;
        }else if (!Configuration.isPasswordValid(etPass.getText().toString())){
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_valid_password));
            return false;
        }else if (TextUtils.isEmpty(etConfirmPass.getText().toString())){
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.enter_confirm_pass));
            return false;
        }else if (!TextUtils.equals(etPass.getText().toString(),etConfirmPass.getText().toString())){
            Configuration.openPrettyDialog(Business_Signup.this,getString(R.string.password_not_match));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkValidation()) {
                    if (CommonMethod.isNetworkAvailable(Business_Signup.this)) {
                        hideSoftKeyboard(Business_Signup.this);
                        new BusinessSignupAsyncTask().execute();
                    } else {
                        Toast.makeText(Business_Signup.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.tvSignin:
                Intent intent = new Intent(Business_Signup.this, Login.class);
                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class BusinessSignupAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(Business_Signup.this, "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "BusinessSignup api response is " + result);
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Please check your Internet.", Toast.LENGTH_LONG).show();
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
                        name = jsonObjectData.getString("name");
                        user_email = jsonObjectData.getString("business_email");
                        role_id = jsonObjectData.getString("role_id");

                        LoginPreferences.getActiveInstance(Business_Signup.this).setId(id);
                        LoginPreferences.getActiveInstance(Business_Signup.this).setUser_first_name(name);
                        LoginPreferences.getActiveInstance(Business_Signup.this).setUser_profile_pic(IMAGE_BASE_URL + user_profile_pic);
                        LoginPreferences.getActiveInstance(Business_Signup.this).setUser_username(user_name);
                        LoginPreferences.getActiveInstance(Business_Signup.this).setEmail(user_email);
                        LoginPreferences.getActiveInstance(Business_Signup.this).setIsLoggedIn(true);
                        LoginPreferences.getActiveInstance(Business_Signup.this).setProfileScreen("1");
                        LoginPreferences.getActiveInstance(Business_Signup.this).setRole_id("5");
                        LoginPreferences.getActiveInstance(Business_Signup.this).setSocialLoginType("no");

                        Log.e(TAG, "id :" + LoginPreferences.getActiveInstance(Business_Signup.this).getId());
                        Log.e(TAG, "first_name :" + LoginPreferences.getActiveInstance(Business_Signup.this).getUser_first_name());
                        Log.e(TAG, "email :" + LoginPreferences.getActiveInstance(Business_Signup.this).getUser_username());
                        Log.e(TAG, "profile_pic :" + LoginPreferences.getActiveInstance(Business_Signup.this).getUser_profile_pic());
                        Log.e(TAG, "isLogin :" + LoginPreferences.getActiveInstance(Business_Signup.this).getIsLoggedIn());
                        Log.e(TAG, "role id :" + LoginPreferences.getActiveInstance(Business_Signup.this).getRole_id());
                        Log.e(TAG, "profilescreen:" + LoginPreferences.getActiveInstance(Business_Signup.this).getProfileScreen());

                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(Business_Signup.this, CountryService.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                        startActivity(i);

                    } else if (status.equals("failed")) {
                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                    } else if (responseMessage.equals("Email Already Registered")) {
                        Toast.makeText(getApplicationContext(), "Email Already Registered", Toast.LENGTH_LONG).show();

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
                Log.e(TAG, "address is :" + etaddress.getText().toString());

                client.addFormPart("business_username", userName);
                client.addFormPart("business_email", email);
                client.addFormPart("business_password", pass);
                client.addFormPart("business_number", phone);
                client.addFormPart("name", name);
                client.addFormPart("role_id", "5");
                client.addFormPart("address", etaddress.getText().toString());

                client.addFormPart("device_type", "1");
                client.addFormPart("device_id", LoginPreferences.getActiveInstance(Business_Signup.this).getDeviceToken());

                client.addFormPart("business_website", etweb.getText().toString());

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
