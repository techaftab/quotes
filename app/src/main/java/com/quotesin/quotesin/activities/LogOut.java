package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.MyApplication;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

public class LogOut extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button btn_logout_ok;
    Button btn_logout_cancel;

    private GoogleApiClient mGoogleApiClient;
    public String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Twitter.initialize(this);


        setContentView(R.layout._log_out);
        mGoogleApiClient = ((MyApplication) getApplication()).getGoogleApiClient(LogOut.this, this);
        this.setFinishOnTouchOutside(false);

        btn_logout_ok =  findViewById(R.id.btn_logout_ok);
        btn_logout_cancel =  findViewById(R.id.btn_logout_cancel);

        btn_logout_ok.setOnClickListener(v -> {

            logoutFromGmail();

            logoutTwitter();

            if (LoginPreferences.getActiveInstance(LogOut.this).getId() != null) {
                if (CommonMethod.isNetworkAvailable(LogOut.this)) {
                    hideSoftKeyboard(LogOut.this);
                    new LogOutAsyncTask().execute();
                } else {
                    Toast.makeText(LogOut.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                }
            }

            LoginPreferences.getActiveInstance(LogOut.this).setPhone("");
            LoginPreferences.getActiveInstance(LogOut.this).setFullname("");
            LoginPreferences.getActiveInstance(LogOut.this).setAlready_register("");
            LoginPreferences.getActiveInstance(LogOut.this).setUser_username("");
            LoginPreferences.getActiveInstance(LogOut.this).setUser_profile_pic("");
            LoginPreferences.getActiveInstance(LogOut.this).setProfileScreen("");
            LoginPreferences.getActiveInstance(LogOut.this).setIsLoggedIn(false);
         //   PreferenceManager.getDefaultSharedPreferences(LogOut.this).edit().clear().apply();

            startIntent();
            finish();

        });

        btn_logout_cancel.setOnClickListener(v -> finish());
    }

    private void logoutFromGmail() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                status -> {
                    Intent login = new Intent(LogOut.this, Login.class);
                    startActivity(login);
                    finish();
                });
    }


    private void startIntent() {
        Intent intent = new Intent(LogOut.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        startActivity(intent);
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void logoutTwitter() {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            ClearCookies(getApplicationContext());
            TwitterCore.getInstance().getSessionManager().clearActiveSession();

        }
    }

    public static void ClearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class LogOutAsyncTask extends AsyncTask<String, Void, String> {

        JSONObject jObject;
       // private String status = "";
        String responseMessage = "";
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "login api response is " + result);
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(result);
                    responseMessage = jObject.getString("message");
                    String error = jObject.getString("Status");
                    if (error.equals("success")) {
                        LoginPreferences.getActiveInstance(LogOut.this).setIsLoggedIn(false);
                        LoginPreferences.getActiveInstance(LogOut.this).setUser_first_name("");
                        LoginPreferences.getActiveInstance(LogOut.this).setId("");

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String callService() {
            String url = APIUrl.logout;

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);

            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);
                Log.e(TAG, "id is :" + LoginPreferences.getActiveInstance(LogOut.this).getId());

                client.addFormPart("id", LoginPreferences.getActiveInstance(LogOut.this).getId());


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
