package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.config.Configuration;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    String twId, twName, twPhotoUrl, twEmail;
    TextView tvSignup;
    ImageView tvGoogle, tvTwitter;

    String TwitterProfileUrl;
    private TwitterAuthClient client;

    private static final int RC_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    String gmailId, gmailName, gmailPhotoUrl, gmailEmail;
    Button btn_login;
    EditText etEmail, etPass;
    String userName, password, business_email;
    TextView tvforgetPass;

  //String Base_url = "https://www.webmobril.org/dev/quotesinapp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);
        client = new TwitterAuthClient();

        setContentView(R.layout.activity_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initViews();
        registerClickListener();

    }

    private void initViews() {
        tvTwitter = findViewById(R.id.tvTwitter);
        tvGoogle = findViewById(R.id.tvGoogle);
        btn_login = findViewById(R.id.btn_login);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        tvforgetPass = findViewById(R.id.tvforgetPass);
        tvSignup = findViewById(R.id.tvSignup);
    }

    private void registerClickListener() {
        tvTwitter.setOnClickListener(this);
        tvGoogle.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tvforgetPass.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGoogle:
                googleSignIn();
                break;

            case R.id.tvTwitter:
                customLoginTwitter();
                break;

            case R.id.btn_login:
                if (checkValidation()) {
                    if (CommonMethod.isNetworkAvailable(Login.this)) {
                        CommonMethod.hideSoftKeyboard(Login.this);
                        new LoginAsyncTask().execute();
                    } else {
                        Toast.makeText(Login.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.tvSignup:
                Intent intent = new Intent(Login.this, welcome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_left);
                break;

            case R.id.tvforgetPass:
                Intent forgetpass = new Intent(this, forgetPassword.class);
                startActivity(forgetpass);
                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                break;
        }
    }


    private boolean checkValidation() {
        userName = etEmail.getText().toString();
        password = etPass.getText().toString();

        Log.e(TAG, "username is:" + userName);
        Log.e(TAG, "password is:" + password);

        if (TextUtils.isEmpty(userName)&&TextUtils.isEmpty(password)){
            Configuration.openPrettyDialog(Login.this,getString(R.string.enter_credential));
            return false;
        }else if (etEmail.getText().toString().trim().length() == 0) {
            Configuration.openPrettyDialog(Login.this,getString(R.string.enter_email));
            return false;
        }else if (TextUtils.isEmpty(password)){
            Configuration.openPrettyDialog(Login.this,getString(R.string.enter_password));
            return false;
        }else if (etPass.getText().toString().length()<6||etPass.getText().toString().length()>16) {
            Configuration.openPrettyDialog(Login.this,getString(R.string.enter_valid_password));
            return false;
        }
        return true;
    }

    public void customLoginTwitter() {
        if (getTwitterSession() == null) {
            client.authorize(this, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    TwitterSession twitterSession = result.data;

                    fetchTwitterEmail(twitterSession);
                    fetchTwitterImage();
                }
                @Override
                public void failure(TwitterException e) {
                    Toast.makeText(Login.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is already authenticated direct call fetch twitter email api
            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show();
            fetchTwitterEmail(getTwitterSession());
            fetchTwitterImage();
        }
    }

    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                Log.e("Success", result.data);
                twName = twitterSession.getUserName();
                twEmail = result.data;
                twId = String.valueOf(twitterSession.getUserId());

                Log.e(TAG, "twEmail : " + twEmail);
                Log.e(TAG, "twName : " + twName);
                Log.e(TAG, "twId : " + twId);

                LoginPreferences.getActiveInstance(Login.this).setFullname(twName);
                LoginPreferences.getActiveInstance(Login.this).setUser_username(twEmail);

                AppData.getInstance().setTwEmail(twEmail);
                AppData.getInstance().setTwId(twId);
                AppData.getInstance().setTwName(twName);
                AppData.getInstance().setType("tw");
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(Login.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchTwitterImage() {
        if (getTwitterSession() != null) {
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
            call.enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    User user = result.data;
                    TwitterProfileUrl = user.profileImageUrl;
                    Log.e(TAG, "Data : " + TwitterProfileUrl);

                    TwitterProfileUrl = TwitterProfileUrl.replace("_normal", "");

                    LoginPreferences.getActiveInstance(Login.this).setUser_profile_pic(TwitterProfileUrl);


                    if (LoginPreferences.getActiveInstance(Login.this).getAlready_register().equals("1")) {
                        Intent gmailintent = new Intent(Login.this, HomeScreen.class);
                        startActivity(gmailintent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                        LoginPreferences.getActiveInstance(Login.this).setIsLoggedIn(true);
                    } else {
                        Intent gmailintent = new Intent(Login.this, SocialLogin_Type.class);
                        gmailintent.putExtra("user_id", AppData.getInstance().getTwId());
                        gmailintent.putExtra("user_email", AppData.getInstance().getTwEmail());
                        gmailintent.putExtra("user_profile", AppData.getInstance().getTwPhotoUrl());
                        gmailintent.putExtra("user_name", AppData.getInstance().getTwName());
                        gmailintent.putExtra("type", "tw");

                        Log.e(TAG, "user_id" + twId);
                        Log.e(TAG, "user_email" + twEmail);
                        Log.e(TAG, "user_profile" + twPhotoUrl);
                        Log.e(TAG, "twName" + twName);

                        startActivity(gmailintent);
                    }
                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(Login.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }

    }


    private TwitterSession getTwitterSession() {

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return TwitterCore.getInstance().getSessionManager().getActiveSession();
    }


    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (client != null)
            client.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            Log.e(TAG, "display name: " + acct.getDisplayName());

            gmailName = acct.getDisplayName();

            if (acct.getPhotoUrl() != null) {
                gmailPhotoUrl = acct.getPhotoUrl().toString();
            }

            gmailEmail = acct.getEmail();
            gmailId = acct.getId();
            gmailId = acct.getIdToken();

            Log.e(TAG, "gmailToken : " + acct.getIdToken() + " gmailId: " + gmailId + ",  Name: " + gmailName + ", email: " + gmailEmail + ", Image: " + gmailPhotoUrl);


            if (gmailName != null) {
                LoginPreferences.getActiveInstance(Login.this).setFullname(gmailName);
                LoginPreferences.getActiveInstance(Login.this).setUser_username(gmailEmail);
                LoginPreferences.getActiveInstance(Login.this).setUser_profile_pic(gmailPhotoUrl);

                if (LoginPreferences.getActiveInstance(Login.this).getAlready_register().equals("1")) {
                    Intent gmailintent = new Intent(Login.this, HomeScreen.class);
                    overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                    startActivity(gmailintent);
                    LoginPreferences.getActiveInstance(Login.this).setIsLoggedIn(true);
                } else {
                    Intent gmailintent = new Intent(Login.this, SocialLogin_Type.class);
                    gmailintent.putExtra("user_id", gmailId);
                    gmailintent.putExtra("user_email", gmailEmail);
                    gmailintent.putExtra("user_profile", gmailPhotoUrl);
                    gmailintent.putExtra("user_name", gmailName);
                    gmailintent.putExtra("type", "gm");
                    overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                    startActivity(gmailintent);
                }
            } else {
                CommonMethod.showAlert("Something wents wrong", this);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("StaticFieldLeak")
    public class LoginAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        String status = "";
        String responseMessage = "";
        String id, username, /*user_last_name,*/ location_code, service_id, category_id, name, user_phone, user_profile_pic;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(Login.this, "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.e(TAG, "username is :" + userName);
                Log.e(TAG, "password is :" + password);
                Log.e(TAG, "device_id :" + LoginPreferences.getActiveInstance(Login.this).getDeviceToken());
                Log.e(TAG, "device_type is :" + "1");

                ArrayList<NameValuePair> postData = new ArrayList<>();
                postData.add(new BasicNameValuePair("username", userName));
                postData.add(new BasicNameValuePair("password", password));
                postData.add(new BasicNameValuePair("device_id", LoginPreferences.getActiveInstance(Login.this).getDeviceToken()));
                postData.add(new BasicNameValuePair("device_type", "1"));

                result = CustomHttpClient.executeHttpPost(APIUrl.LOGIN, postData);
                System.out.print(result);
                Log.e(TAG, "result is :" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
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
                    status = jObject.getString("Status");

                    if (status.equalsIgnoreCase("Success")) {
                        JSONObject jsonObjectData = jObject.getJSONObject("result");

                        Log.e(TAG, "jsonObjectData :" + jsonObjectData);
                        id = jsonObjectData.getString("id");
                        username = jsonObjectData.getString("business_username");
                        user_phone = jsonObjectData.getString("business_telephone");
                        user_profile_pic = jsonObjectData.getString("business_logo");
                        location_code = jsonObjectData.getString("location_code");
                        service_id = jsonObjectData.getString("service_id");
                        category_id = jsonObjectData.getString("category_id");
                        business_email = jsonObjectData.getString("business_email");
                        String role_id = jsonObjectData.getString("role_id");


                        if (!jsonObjectData.isNull("name")) {

                            name = jsonObjectData.getString("name");
                        } else {
                            name = jsonObjectData.getString("business_username");
                        }

                        LoginPreferences.getActiveInstance(Login.this).setId(id);
                        LoginPreferences.getActiveInstance(Login.this).setUser_first_name(name);
                        LoginPreferences.getActiveInstance(Login.this).setUser_username(username);
                        LoginPreferences.getActiveInstance(Login.this).setRole_id(role_id);
                        LoginPreferences.getActiveInstance(Login.this).setFullname(userName);
                        LoginPreferences.getActiveInstance(Login.this).setUser_profile_pic(APIUrl.IMAGE_BASE_URL + user_profile_pic);
                        LoginPreferences.getActiveInstance(Login.this).setLocationId(location_code);
                        LoginPreferences.getActiveInstance(Login.this).setService_id(service_id);
                        LoginPreferences.getActiveInstance(Login.this).setCategory_id(category_id);
                        LoginPreferences.getActiveInstance(Login.this).setEmail(business_email);
                        LoginPreferences.getActiveInstance(Login.this).setIsLoggedIn(true);


                        Log.e(TAG, "id :" + LoginPreferences.getActiveInstance(Login.this).getId());
                        Log.e(TAG, "first_name :" + LoginPreferences.getActiveInstance(Login.this).getUser_first_name());
                        Log.e(TAG, "email :" + LoginPreferences.getActiveInstance(Login.this).getUser_username());
                        Log.e(TAG, "profile_pic :" + LoginPreferences.getActiveInstance(Login.this).getUser_profile_pic());
                        Log.e(TAG, "Locationid :" + LoginPreferences.getActiveInstance(Login.this).getLocationId());
                        Log.e(TAG, "service_id :" + LoginPreferences.getActiveInstance(Login.this).getService_id());
                        Log.e(TAG, "category_id :" + LoginPreferences.getActiveInstance(Login.this).getCategory_id());
                        Log.e(TAG, "isLogin :" + LoginPreferences.getActiveInstance(Login.this).getIsLoggedIn());
                        Log.e(TAG, "role_id :" + LoginPreferences.getActiveInstance(Login.this).getRole_id());

                        Intent i = new Intent(Login.this, HomeScreen.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                        startActivity(i);
                        finish();

                    } else if (status.equalsIgnoreCase("Failed")) {
                        Configuration.openPrettyDialog(Login.this,responseMessage);

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, welcome.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_left);
        finish();
    }
}