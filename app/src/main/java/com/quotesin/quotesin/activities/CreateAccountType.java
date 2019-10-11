package com.quotesin.quotesin.activities;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
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

import retrofit2.Call;


public class CreateAccountType extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button btn_business, btn_consumer;
    String normal, gmailLogin, twitter;
    String TwitterProfileUrl;
    RelativeLayout LLAccountType;
    String user_id, user_email, user_profile, user_name, type;

    String twId, twName, twPhotoUrl, twEmail;

    private TwitterAuthClient client;
    String TAG = this.getClass().getSimpleName();
    String gm, tw, fb;
    String id1, username1, email1, profile_pic1, role_id1;
    private static final int RC_SIGN_IN = 100;

    String gmailId, gmailName, gmailPhotoUrl, gmailEmail;

    String Base_url = "https://www.webmobril.org/dev/quotesinapp/";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient1;
    ProgressD mProgressD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_account_type);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                        getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        Twitter.initialize(config);
        client = new TwitterAuthClient();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            gmailLogin = bundle.getString("gm");
            normal = bundle.getString("normal");
            twitter = bundle.getString("tw");
            Log.e(TAG, "Gmail flag-->" + gmailLogin);
            Log.e(TAG, "Normal flag-->" + normal);
            Log.e(TAG, "Twitter flag-->" + twitter);
        }
        if (gmailLogin.equalsIgnoreCase("1"))
            type = "gm";
        else if (normal.equalsIgnoreCase("1")) {
            type = "normal";
        } else {
            type = "twitter";
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient1 = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initViews();
        registerClickListener();
    }

    private void initViews() {
        btn_business =  findViewById(R.id.btn_business);
        btn_consumer =  findViewById(R.id.btn_consumer);
        LLAccountType = findViewById(R.id.LLAccountType);
    }

    private void registerClickListener() {
        btn_consumer.setOnClickListener(this);
        btn_business.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_business:
                if (normal.equalsIgnoreCase("1")) {
                    startIntent(Business_Signup.class);
                } else if (gmailLogin.equals("1")) {
                    mProgressD = ProgressD.show(CreateAccountType.this, "Connecting...", true);
                    googleSignIn();
                    role_id1 = "5";
                } else {
                    customLoginTwitter();
                    role_id1 = "5";
                }
                break;

            case R.id.btn_consumer:
                if (normal.equalsIgnoreCase("1")) {
                    startIntent(Consumer_Signup.class);
                } else if (gmailLogin.equals("1")) {
                    mProgressD = ProgressD.show(CreateAccountType.this, "Connecting...", true);
                    googleSignIn();
                    role_id1 = "2";
                } else {
                    customLoginTwitter();
                    mProgressD = ProgressD.show(CreateAccountType.this, "Connecting...", true);
                    role_id1 = "2";
                }
                break;
        }
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
                    Toast.makeText(CreateAccountType.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
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
                //here it will give u only email and rest of other information u can get from TwitterSession
                //  userDetailsLabel.setText("User Id : " + twitterSession.getUserId() + "\nScreen Name : " + twitterSession.getUserName() + "\nEmail Id : " + result.data);

                twName = twitterSession.getUserName();
                twEmail = result.data;
                twId = String.valueOf(twitterSession.getUserId());

                Log.e(TAG, "twEmail : " + twEmail);
                Log.e(TAG, "twName : " + twName);
                Log.e(TAG, "twId : " + twId);

                LoginPreferences.getActiveInstance(CreateAccountType.this).setFullname(twName);
                LoginPreferences.getActiveInstance(CreateAccountType.this).setUser_username(twEmail);

                AppData.getInstance().setTwEmail(twEmail);
                AppData.getInstance().setTwId(twId);
                AppData.getInstance().setTwName(twName);
                AppData.getInstance().setType("tw");

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(CreateAccountType.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
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

                    LoginPreferences.getActiveInstance(CreateAccountType.this).setUser_profile_pic(TwitterProfileUrl);
                    if (mProgressD.isShowing()){
                        mProgressD.dismiss();
                    }
                    new LoginAsyncTask().execute();
                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(CreateAccountType.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }

    }


    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return session;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
       // updateUI(account);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
       /* Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient1);
        startActivityForResult(signInIntent, RC_SIGN_IN);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try{
                mProgressD.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInGmail(task);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (client != null)
            client.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInGmail(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
           // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
           // updateUI(null);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        mProgressD = ProgressD.show(CreateAccountType.this, "Please wait...", true);
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
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

            Log.e(TAG, "gmailToken : " + acct.getIdToken() + " gmailId: " + gmailId + "," +
                    "  Name: " + gmailName + ", email: " + gmailEmail + ", Image: " + gmailPhotoUrl);
            if (mProgressD.isShowing()) {
                mProgressD.dismiss();
            }

            if (gmailName != null) {
                LoginPreferences.getActiveInstance(CreateAccountType.this).setFullname(gmailName);
                LoginPreferences.getActiveInstance(CreateAccountType.this).setUser_username(gmailEmail);
                LoginPreferences.getActiveInstance(CreateAccountType.this).setUser_profile_pic(gmailPhotoUrl);

                if (LoginPreferences.getActiveInstance(CreateAccountType.this).getAlready_register().equals("1")) {
                    Intent gmailintent = new Intent(CreateAccountType.this, HomeScreen.class);
                    startActivity(gmailintent);
                    LoginPreferences.getActiveInstance(CreateAccountType.this).setIsLoggedIn(true);
                } else {
                    new LoginAsyncTask().execute();
                }
            } else {
                CommonMethod.showAlert("Something wents wrong", this);
            }
        }
    }

    public class LoginAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        private String status = "";
        private String responseMessage = "";
        private String available = "";
        String id;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(CreateAccountType.this, "Connecting", true);
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
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    available = jObject.getString("available");
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            id1 = c.getString("id");
                            username1 = c.getString("name");
                            email1 = c.getString("business_email");
                            profile_pic1 = c.getString("business_logo");
                            role_id1 = c.getString("role_id");

                            LoginPreferences.getActiveInstance(CreateAccountType.this).setId(id1);
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setUser_first_name(username1);
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setUser_username(email1);
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setUser_profile_pic(profile_pic1);
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setRole_id(role_id1);
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setAlready_register(available);
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setIsLoggedIn(true);
                           // LoginPreferences.getActiveInstance(CreateAccountType.this).setProfileScreen("1");

                            Log.e(TAG, "id :" + LoginPreferences.getActiveInstance(CreateAccountType.this).getId());
                            Log.e(TAG, "Fullname :" + LoginPreferences.getActiveInstance(CreateAccountType.this).getFullname());
                            Log.e(TAG, "email :" + LoginPreferences.getActiveInstance(CreateAccountType.this).getUser_username());
                            Log.e(TAG, "isLogin :" + LoginPreferences.getActiveInstance(CreateAccountType.this).getIsLoggedIn());
                            Log.e(TAG, "available :" + LoginPreferences.getActiveInstance(CreateAccountType.this).getAlready_register());

                        }


                        if (role_id1.equals("5")) {
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setSocialLoginType("yes");
                            Intent i = new Intent(CreateAccountType.this, CountryService.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.e(TAG, "CountryService ==:" + "CountryService");
                            startActivity(i);
                            finish();

                        } else {
                            LoginPreferences.getActiveInstance(CreateAccountType.this).setSocialLoginType("yes");
                            Intent i = new Intent(CreateAccountType.this, ConsSelectCountry.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            Log.e(TAG, "ConsSelectCountry :" + "ConsSelectCountry");
                            finish();
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
            Log.e(TAG, "type-->" + type);
            if (type != null) {
                if (type.equals("gm")) {
                    url = APIUrl.google_login;
                } else if (type.equals("fb")) {
                    url = APIUrl.fb_login;
                } else {
                    url = APIUrl.twitter_login;
                }
            }
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);

            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);

                if (type.equals("gm")) {
                    Log.e(TAG, "id is :" + user_id);
                    Log.e(TAG, "username is :" + gmailName);
                    Log.e(TAG, "gmailEmail is :" + gmailEmail);
                    Log.e(TAG, "gmailPhotoUrl is :" + user_profile);
                    Log.e(TAG, "gmailPhotoUrl is :" + gmailPhotoUrl);
                    Log.e(TAG, "roleid is :" + role_id1);

                    client.addFormPart("gmailId", "null");
                    client.addFormPart("profile_pic", user_profile);
                    client.addFormPart("gmailName", gmailName);
                    client.addFormPart("gmailEmail", gmailEmail);
                    client.addFormPart("role_id", role_id1);
                    client.addFormPart("device_id", LoginPreferences.getActiveInstance(CreateAccountType.this).getDeviceToken());
                    client.addFormPart("device_type", "1");

                } else if (type.equals("fb")) {
                    Log.e(TAG, "id is :" + user_id);
                    Log.e(TAG, "username is :" + user_name);
                    Log.e(TAG, "gmailEmail is :" + user_email);
                    Log.e(TAG, "gmailPhotoUrl is :" + user_profile);

                    client.addFormPart("user_id", user_id);
                    client.addFormPart("user_profile", user_profile);
                    client.addFormPart("user_name", user_name);
                    client.addFormPart("user_email", user_email);
                    client.addFormPart("role_id", role_id1);
                    client.addFormPart("device_id", LoginPreferences.getActiveInstance(CreateAccountType.this).getDeviceToken());
                    client.addFormPart("device_type", "1");
                } else {
                    Log.e(TAG, "id is :" + twId);
                    Log.e(TAG, "username is :" + twName);
                    Log.e(TAG, "gmailEmail is :" + twEmail);
                    Log.e(TAG, "gmailPhotoUrl is :" + TwitterProfileUrl);

                    client.addFormPart("twitter_id", twId);
                    client.addFormPart("profile_pic", TwitterProfileUrl);
                    client.addFormPart("fullname", AppData.getInstance().getTwName());
                    client.addFormPart("email", AppData.getInstance().getTwEmail());
                    client.addFormPart("role_id", role_id1);
                    client.addFormPart("device_id", LoginPreferences.getActiveInstance(CreateAccountType.this).getDeviceToken());
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

    private void startIntent(Class<?> cls) {
        Intent intent = new Intent(CreateAccountType.this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
