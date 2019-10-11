package com.quotesin.quotesin.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.ViewPagerAdapterSlider;
import com.quotesin.quotesin.config.Configuration;
import com.quotesin.quotesin.model.BannerListResponse;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class welcome extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    String TAG = this.getClass().getSimpleName();
    ViewPager pager_intro;
    LinearLayout viewPagerCountDots;
    ViewPagerAdapterSlider mAdapter;
    private int dotsCount;
    private ImageView[] dots;

    Timer timer1;
    int count1 = 0;
    
    Button btnCreate;
    TextView tvSignin;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        initViews();
        registerClickListner();
        Log.e(TAG, "devicetoken====" + LoginPreferences.getActiveInstance(getApplicationContext()).getDeviceToken());
      //  changeStatusBarColor();
        if (Configuration.hasNetworkConnection(welcome.this)){
            new BannerImageTask().execute();
        }else {
            Configuration.openPopupUpDown(welcome.this, R.style.Dialod_UpDown, "internetError",
                    "No Internet Connectivity" +
                            ", Thanks");
        }

    }

    private void initViews() {
        pager_intro =  findViewById(R.id.pager_intro);
        viewPagerCountDots =  findViewById(R.id.viewPagerCountDots);
        btnCreate =  findViewById(R.id.btnCreate);
        tvSignin =  findViewById(R.id.tvSignin);
    }

    private void registerClickListner() {
        tvSignin.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }
  /*  private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignin:
                startIntent(Login.class);
                break;
            case R.id.btnCreate:
                startIntent(CreateAccount.class);
                break;
        }
    }

    private void startIntent(Class<?> cls) {
        Intent intent = new Intent(welcome.this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(welcome.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    Intent launchNextActivity = new Intent(Intent.ACTION_MAIN);
                    launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                    launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchNextActivity);
                    finish();
                }).create().show();
    }

    private void setUpIntroParger(List<BannerListResponse.Message> result) {
        mAdapter = new ViewPagerAdapterSlider(welcome.this, result);
        pager_intro.setAdapter(mAdapter);
        pager_intro.setCurrentItem(0);
        pager_intro.setOnPageChangeListener(this);
        setUiPageViewController();

        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (count1 <= 2) {
                        pager_intro.setCurrentItem(count1);
                        count1++;
                    } else {
                        count1 = 0;
                        pager_intro.setCurrentItem(count1);
                    }
                });
            }
        }, 5000, 8000);
    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(4, 0, 4, 0);

            viewPagerCountDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i1 = 0; i1 < dotsCount; i1++) {
            dots[i1].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("StaticFieldLeak")
    public class BannerImageTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        public String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(welcome.this, "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = CustomHttpClient.executeHttpGet(APIUrl.BANNER_IMAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressD.dismiss();
            if (result == null) {
                //  Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_LONG).show();
            } else {

                try {
                    Gson gson = new Gson();
                    BannerListResponse response = gson.fromJson(result, BannerListResponse.class);
                    if (response.status.equals("success")) {
                        if (response.message.size() != 0) {
                            setUpIntroParger(response.message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

