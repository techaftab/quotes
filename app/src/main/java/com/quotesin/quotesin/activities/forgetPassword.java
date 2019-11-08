package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.ProgressD;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;


public class forgetPassword extends AppCompatActivity implements View.OnClickListener {
    Button btn_submit;
    EditText etEmail;
    String TAG = this.getClass().getSimpleName();
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._forget_password);

        initViews();
        registerClickListener();
    }

    private void registerClickListener() {
        btn_submit.setOnClickListener(this);
    }

    private void initViews() {
        btn_submit =  findViewById(R.id.btn_submit);
        etEmail =  findViewById(R.id.etEmail);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            if (checkValidation()) {
                if (CommonMethod.isNetworkAvailable(forgetPassword.this)) {
                    CommonMethod.hideSoftKeyboard(forgetPassword.this);
                    new ForgetPassAsyncTask().execute();
                } else {
                    Toast.makeText(forgetPassword.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean checkValidation() {
        userName = etEmail.getText().toString();

        Log.e(TAG, "username is:" + userName);

        if (etEmail.getText().toString().trim().length() == 0) {
           // CommonMethod.showAlert("Please enter Email", forgetPassword.this);
            final PrettyDialog pDialog = new PrettyDialog(this);
            // button OnClick listener
            pDialog
                    .setTitle("QuotesIn")
                    .setMessage("Please enter Email")
                    .setIcon(R.mipmap.quotes_logo)
                    .addButton(
                            "OK",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.splash_color,        // button background color
                            pDialog::dismiss
                    )
                    .setAnimationEnabled(true)
                    .show();

            return false;
        } else if (!CommonMethod.isValidEmaillId(etEmail.getText().toString().trim())) {
           // CommonMethod.showAlert("Please enter valid Email", forgetPassword.this);
            final PrettyDialog pDialog = new PrettyDialog(this);
            // button OnClick listener
            pDialog
                    .setTitle("QuotesIn")
                    .setMessage("Please enter correct email address in correct format.")
                    .setIcon(R.mipmap.quotes_logo)
                    .addButton(
                            "OK",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.splash_color,        // button background color
                            pDialog::dismiss
                    )
                    .setAnimationEnabled(true)
                    .show();

            return false;
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class ForgetPassAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        String status = "";
        String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(forgetPassword.this, "Connecting", true);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            try {
                Log.e(TAG, "Email is :" +etEmail.getText().toString());
                ArrayList<NameValuePair> postData = new ArrayList<>();
                postData.add(new BasicNameValuePair("email",etEmail.getText().toString()));
                result = CustomHttpClient.executeHttpPost(APIUrl.forget_password, postData);
                System.out.print(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "forget api response is " + result);
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Response is " + result, Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                        finish();

                    } else if (status.equals("failed")) {
                        CommonMethod.showAlert(responseMessage,forgetPassword.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }
    }
}
