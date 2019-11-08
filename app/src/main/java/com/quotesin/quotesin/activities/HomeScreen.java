package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.fragments.Commision_list;
import com.quotesin.quotesin.fragments.Inbox;
import com.quotesin.quotesin.fragments.Settings;
import com.quotesin.quotesin.fragments.TrashList;
import com.quotesin.quotesin.fragments.myprofile;
import com.quotesin.quotesin.fragments.send;
import com.quotesin.quotesin.fragments.terms_condition;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.MyApplication;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

public class HomeScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<ItemsModel> itemModel;
    public static DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private DrawerAdapter drawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    TextView mTitle;
    private static CharSequence mTitlee;
    ImageView user_profile_image;
    TextView tvName;
    LinearLayout lldrawer;
    private int lastIndex = 0;
   // private ImageLoader1 imgload;

    String TAG = this.getClass().getName();
    @SuppressLint("StaticFieldLeak")
    public static HomeScreen mainActivity;
    public static MenuItem item1;

    Boolean isShowable = false;
    Boolean isClickable;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout header,lnDrawer;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = HomeScreen.this;

       //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // hideKeyboard();
        setContentView(R.layout.home_sreen);
        mGoogleApiClient = ((MyApplication) getApplication()).getGoogleApiClient(HomeScreen.this, this);
        header=findViewById(R.id.ln_header);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        lnDrawer=findViewById(R.id.ln_drawer);
        if (!LoginPreferences.getActiveInstance(HomeScreen.this).getIsLoggedIn()) {
            Intent intent = new Intent(HomeScreen.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }

        initViews();
        setupToolbar();
        registerClickListener();

        TypedArray itemImages = getResources().obtainTypedArray(R.array.user_type_teacher);
      //  View header = getLayoutInflater().inflate(R.layout.nav_header_main, null);

       // mDrawerList.addHeaderView(header);
        mDrawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);

        itemModel.add(new ItemsModel("Inbox", R.mipmap.inbox));
        itemModel.add(new ItemsModel("Sent", R.mipmap.sent));

        if (LoginPreferences.getActiveInstance(HomeScreen.this).getRole_id().equals("5")) {
            itemModel.add(new ItemsModel("Trash", R.mipmap.trash));
            isShowable = true;
            isClickable = true;
        }
        if (LoginPreferences.getActiveInstance(HomeScreen.this).getRole_id().equals("5")) {
            itemModel.add(new ItemsModel("Commission History", R.mipmap.comm));
            isShowable = true;
            isClickable = true;
        }

        itemModel.add(new ItemsModel("Membership", R.mipmap.mrmbership));
        itemModel.add(new ItemsModel("How it works?", R.mipmap.how_work));
        itemModel.add(new ItemsModel("Terms & Conditions", R.mipmap.ter_cond));
        itemModel.add(new ItemsModel("Settings", R.mipmap.setting));
        itemModel.add(new ItemsModel("Logout", R.mipmap.logout));

        itemImages.recycle();
        drawerAdapter = new DrawerAdapter(itemModel);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(HomeScreen.this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Objects.requireNonNull(getSupportActionBar()).setTitle(mTitlee);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        user_profile_image = findViewById(R.id.user_profile_image);
        tvName =  findViewById(R.id.tvName);


        if (!TextUtils.isEmpty(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic())) {
            if (LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic() != null) {
                Picasso.get()
                        .load(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic())
                        .placeholder(R.mipmap.avatar_male)
                        .error(R.mipmap.avatar_male)
                        .into(user_profile_image);
            } else {
                Log.e("user image ", "is  Null");
                Picasso.get()
                        .load(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic())
                        .placeholder(R.mipmap.avatar_male)
                        .error(R.mipmap.avatar_male)
                        .into(user_profile_image);
            }

        }

        tvName.setText(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_first_name());

        if (!TextUtils.isEmpty(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic())) {


            if (LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic() != null) {
                Picasso.get()
                        .load(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic())
                        .placeholder(R.mipmap.avatar_male)
                        .error(R.mipmap.avatar_male)
                        .into(user_profile_image);
                if (!LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic().isEmpty()) {
                    Picasso.get()
                            .load(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic())
                            .placeholder(R.mipmap.avatar_male)
                            .error(R.mipmap.avatar_male)
                            .into(user_profile_image);
                }
            } else {
                Picasso.get()
                        .load(LoginPreferences.getActiveInstance(HomeScreen.this).getUser_profile_pic())
                        .placeholder(R.mipmap.avatar_male)
                        .error(R.mipmap.avatar_male)
                        .into(user_profile_image);
            }
        }

        header.setOnClickListener(v -> {
            myprofile myprofile = new myprofile();
            replaceMyFragment(myprofile, "PROFILE");
            Log.e(TAG, "Click on user profile");
            mDrawerLayout.closeDrawer(lnDrawer);

        });

        if (LoginPreferences.getActiveInstance(HomeScreen.this).getProfileScreen().equalsIgnoreCase("1")) {
            Intent intent1 = new Intent(HomeScreen.this, CountryService.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        } else if (LoginPreferences.getActiveInstance(HomeScreen.this).getProfileScreen().equalsIgnoreCase("0")) {
            Intent intent1 = new Intent(HomeScreen.this, ConsSelectCountry.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Inbox inbox = new Inbox();
        transaction.add(R.id.flContent, inbox);
        setTitle("Inbox");
        transaction.commit();

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

    private void setupToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            mTitle =  toolbar.findViewById(R.id.mTitle);
            mTitle.setText("");
            mTitlee = getTitle();
            toolbar.inflateMenu(R.menu.main);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        menu.findItem(R.id.notifications).setVisible(false);

        item1 = menu.findItem(R.id.app_bar_search);
        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }

        switch (item.getItemId()) {
            case R.id.app_bar_search:
                break;

            case R.id.notifications:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        mDrawerList =  findViewById(R.id.left_drawer);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        lldrawer =  findViewById(R.id.lldrawer);

        Log.d("called", "main activity");

        itemModel = new ArrayList<>();
    }

    private void registerClickListener() {

    }

    public void syncMyToggle() {
        mDrawerToggle.syncState();
    }


    public class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

           // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Log.e("position", "DrawerItemClickListener" + position);

            if (LoginPreferences.getActiveInstance(HomeScreen.this).getRole_id().equals("5")) {
                if (position != 0) {
                    lastIndex = position;
                    selectItem(position);
                } else {
                    mDrawerList.setItemChecked(lastIndex, true);
                }
            } else {
                if (position != 0) {
                    lastIndex = position;
                    if (isShowable) {
                        selectItem(position+1);
                    } else {
                        if (position >= 2)
                            selectItem(position + 2);
                        else {
                            selectItem(position);
                        }
                    }
                }else {
                    selectItem(0);
                }
            }

        }
    }

    private void selectItem(int position) {
        Log.d("POS : ", position + "  == ");

        Log.e("position", "is Client : " + position);

        switch (position) {
            case 0:
                Inbox inbox = new Inbox();
                replaceMyFragment(inbox, "Inbox");
                break;
            case 1:
                send send = new send();
                replaceMyFragment(send, "Sent");
                break;
            case 2:
                TrashList trashList = new TrashList();
                replaceMyFragment(trashList, "Trash");
                break;
            case 3:
                Commision_list commisionList = new Commision_list();
                replaceMyFragment(commisionList, "Commission");
                break;

            case 4:
                Intent subscription = new Intent(HomeScreen.this, SubscriptionActivity.class);
                subscription.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(subscription);
                break;

            case 5:
                Intent howItWork = new Intent(HomeScreen.this, How_Work.class);
                howItWork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(howItWork);
                break;

            case 6:
                terms_condition termCondition = new terms_condition();
                replaceMyFragment(termCondition, "Term and Condition");
                break;

            case 7:
                Settings settings = new Settings();
                replaceMyFragment(settings, "Settings");
                break;

            case 8:
                logoutUser();
               /* Intent logout = new Intent(HomeScreen.this, LogOut.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);*/
                break;

        }
        mDrawerLayout.closeDrawer(lnDrawer);
    }

    private void logoutUser() {
        Dialog dialog=new Dialog(HomeScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout._log_out);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button btn_logout_ok;
        Button btn_logout_cancel;
        btn_logout_ok =  dialog.findViewById(R.id.btn_logout_ok);
        btn_logout_cancel =  dialog.findViewById(R.id.btn_logout_cancel);
        btn_logout_ok.setOnClickListener(v -> {
            if (LoginPreferences.getActiveInstance(this).getId() != null) {
                if (CommonMethod.isNetworkAvailable(this)) {
                    hideSoftKeyboard(this);
                    new LogOutAsyncTask().execute();
                    LoginPreferences.getActiveInstance(HomeScreen.this).setIsLoggedIn(false);
                    PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
                    logoutFromGmail();
                    logoutTwitter();
                } else {
                    Toast.makeText(HomeScreen.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_logout_cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
    private void logoutFromGmail() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                status -> {
                    Intent login = new Intent(HomeScreen.this, Login.class);
                    startActivity(login);
                    finish();
                });
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

    public void replaceMyFragment(Fragment feeds, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, feeds);
        transaction.detach(feeds);
        transaction.attach(feeds);
        transaction.addToBackStack(null);
        setTitle(title);
        transaction.commit();
      /*  if (feeds.e("Inbox")){
            mDrawerList.setAdapter(drawerAdapter);
        }*/
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

            if (backStackCount >= 1) {
                getSupportFragmentManager().popBackStack();
                // Change to hamburger icon if at bottom of stack

                if (backStackCount == 1) {

                    syncMyToggle();
                }
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit Application")
                        .setMessage("Are you sure you want to Exit from this app?")
                        .setPositiveButton("Yes", (dialog, which) -> finish()).setNegativeButton("No", null).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
        lastIndex=0;
        mDrawerList.setItemChecked(lastIndex, true);
    }

    @Override
    protected void onPostResume() {
        hideKeyboard();
        mDrawerToggle.syncState();
        mDrawerList.setAdapter(drawerAdapter);
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawerToggle.syncState();
        hideKeyboard();
       // selectItem(0);
       // invalidateOptionsMenu();
        mDrawerList.setAdapter(drawerAdapter);
    }

    @Override
    public  void setTitle(CharSequence title) {
        mTitlee = title;
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mTitlee);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private class DrawerAdapter extends BaseAdapter {

        ArrayList<ItemsModel> itemsmodel;

        DrawerAdapter(ArrayList<ItemsModel> itemModel) {
            this.itemsmodel = itemModel;
        }

        @Override
        public int getCount() {
            return itemsmodel.size();
        }

        @Override
        public Object getItem(int position) {
            return itemsmodel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View view, ViewGroup parent) {

            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                assert mInflater != null;
                view = mInflater.inflate(R.layout.list_adapter, null);
            }
            ImageView img =  view.findViewById(R.id.img);
            TextView name =  view.findViewById(R.id.name);
            name.setText(itemsmodel.get(position).getItemName());
            img.setImageResource(itemsmodel.get(position).getItemImage());
            return view;
        }
    }

    private class ItemsModel {
        private String itemName/*, subtitle*/;
        private int itemImage;

        ItemsModel(String name, int image) {
            this.itemName = name;
            this.itemImage = image;
        }

       /* public ItemsModel(String itemName, String subtitle, int itemImage) {
            this.itemName = itemName;
            this.subtitle = subtitle;
            this.itemImage = itemImage;
        }*/

        /*public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
*/
        String getItemName() {
            return itemName;
        }

       /* public void setItemName(String itemName) {
            this.itemName = itemName;
        }*/

        int getItemImage() {
            return itemImage;
        }

       /* public void setItemImage(int itemImage) {
            this.itemImage = itemImage;
        }*/

    }

    public void hideKeyboard() {

            View view = HomeScreen.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) HomeScreen.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
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
                        LoginPreferences.getActiveInstance(HomeScreen.this).setIsLoggedIn(false);
                        LoginPreferences.getActiveInstance(HomeScreen.this).setUser_first_name("");
                        LoginPreferences.getActiveInstance(HomeScreen.this).setId("");

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
                Log.e(TAG, "id is :" + LoginPreferences.getActiveInstance(HomeScreen.this).getId());

                client.addFormPart("id", LoginPreferences.getActiveInstance(HomeScreen.this).getId());


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
