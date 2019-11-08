package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.activities.Login;
import com.quotesin.quotesin.activities.SubscriptionActivity;
import com.quotesin.quotesin.adapter.AdsPagerAdapterSlider;
import com.quotesin.quotesin.adapter.Indox_Enquiries;
import com.quotesin.quotesin.adapter.RQuotesAdapter;
import com.quotesin.quotesin.config.Configuration;
import com.quotesin.quotesin.model.Indox_Enquiry_Model;
import com.quotesin.quotesin.model.RQuotesModel;
import com.quotesin.quotesin.model.adsResponse;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.PayPalConfig;
import com.quotesin.quotesin.utils.ProgressD;
import com.quotesin.quotesin.utils.SwipeToDeleteCallback;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class Inbox extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int PAYPAL_REQUEST_CODE = 123;
    private static PopupWindow popupWindow;
    public static Date Current;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    private TextView tvRead, tvUnread, tvAttachment;
    private LinearLayout linearLayout1, llEnquiry, LLQuote;
    View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView1, recyclerView2;
    private TextView tvConsumerQuotes;
    private ImageView tvFilter;
    private ImageView ivEmpty, ivEmpty2;
    //Dot indicator
    private DotsIndicator dotsIndicator;

    private Indox_Enquiries indoxEnquiries;
    private ArrayList<Indox_Enquiry_Model> indoxEnquiryModelArrayList = new ArrayList<>();

    private RQuotesAdapter rQuotesAdapter;
    private ArrayList<RQuotesModel> quotesModelArrayList = new ArrayList<>();

    String TAG = this.getClass().getSimpleName();
    private FrameLayout flEnquiry, flQuotes;
  //  boolean isLoading = false;
    private ImageView fab, fabQuote;
    private FloatingActionButton  fab1, fab2;
 //   private CoordinatorLayout coordinatorLayout;
    private RadioGroup toggle;
    private RadioButton enquiry, quotes;
    SearchManager searchManager;
    private String filterFlag = "Normal";
   // String undoFlag = "0";

    private ViewPager pager_intro;
    private LinearLayout viewPagerCountDots;
    private AdsPagerAdapterSlider mAdapter;
    private Timer timer1;
    private int count1 = 0;
    private ArrayList<String> ZeroOccur = new ArrayList<>();
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private int dotsCount;
    private ImageView[] dots;
    ProgressD mProgressD;
    private CardView cardViewMembership,cardViewProfile;

    public Inbox() {
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_inbox, container, false);

        Log.e(TAG, "token--" + LoginPreferences.getActiveInstance(getActivity()).getDeviceToken());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        String formattedDate = df.format(c.getTime());
        String date11 = formattedDate.substring(0, 10);

        try {

            df = new SimpleDateFormat("yyyy-MM-dd");
            Current = df.parse(date11);
            System.out.println("Dateformat Current: " + Current);
            System.out.println("Date in dd/MM/yyyy format is: " + df.format(Current));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        initViews();
        registerClickListener();

        Log.e(TAG, LoginPreferences.getActiveInstance(getActivity()).getRole_id());

        if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("2")) {
            toggle.setVisibility(View.GONE);
            tvConsumerQuotes.setVisibility(View.VISIBLE);
            new RecievedQuotesAsyncTask().execute();
            flEnquiry.setVisibility(View.GONE);
            flQuotes.setVisibility(View.VISIBLE);
            tvFilter.setVisibility(View.GONE);
        } else {
            toggle.setVisibility(View.VISIBLE);
            callApi();
            new RecievedQuotesAsyncTask().execute();
        }
        enableSwipeToDeleteAndUndo();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("2")) {
                toggle.setVisibility(View.GONE);
                tvConsumerQuotes.setVisibility(View.VISIBLE);
                new RecievedQuotesAsyncTask().execute();

            } else {
                callApi();
                new RecievedQuotesAsyncTask().execute();
            }

            new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 3000);
        });
        toggle.setOnCheckedChangeListener((group, checkedId) -> {
            if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5")) {
                if (enquiry.isChecked()) {
                    enquiry.setTextColor(Color.WHITE);
                    quotes.setTextColor(getResources().getColor(R.color.colorAccent));
                    flEnquiry.setVisibility(View.VISIBLE);
                    flQuotes.setVisibility(View.GONE);
                    tvFilter.setVisibility(View.VISIBLE);
                }
                if (quotes.isChecked()) {
                    quotes.setTextColor(Color.WHITE);
                    enquiry.setTextColor(getResources().getColor(R.color.colorAccent));
                    flEnquiry.setVisibility(View.GONE);
                    flQuotes.setVisibility(View.VISIBLE);
                    tvFilter.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
             new BannerAddTask().execute();
        }

        Intent basic = new Intent(getActivity(), PayPalService.class);
        basic.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        Objects.requireNonNull(getActivity()).startService(basic);

        return view;
    }

    private void initViews() {
        dotsIndicator=view.findViewById(R.id.dots_indicator);
        pager_intro = view.findViewById(R.id.pager_intro);
        viewPagerCountDots = view.findViewById(R.id.viewPagerCountDots);
       // coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        cardViewProfile=view.findViewById(R.id.cardview_profile);
        cardViewMembership=view.findViewById(R.id.cardview_membership);
        toggle = view.findViewById(R.id.toggle);
        quotes = view.findViewById(R.id.quotes);
        enquiry = view.findViewById(R.id.enquiry);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView1 = view.findViewById(R.id.recyclerview1);
        recyclerView2 = view.findViewById(R.id.recyclerview2);
        tvConsumerQuotes = view.findViewById(R.id.tvConsumerQuotes);

        fab = view.findViewById(R.id.fab);
        fabQuote = view.findViewById(R.id.fabQuote);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        flQuotes = view.findViewById(R.id.flQuotes);
        flEnquiry = view.findViewById(R.id.flContent);
        tvFilter = view.findViewById(R.id.tvFilter);
        linearLayout1 = view.findViewById(R.id.linearLayout1);

        LLQuote = view.findViewById(R.id.LLQuote);
        ivEmpty2 = view.findViewById(R.id.ivEmpty2);
        llEnquiry = view.findViewById(R.id.llEnquiry);
        ivEmpty = view.findViewById(R.id.ivEmpty);
        recyclerView1.setNestedScrollingEnabled(false);

    }

    private void registerClickListener() {
        fab.setOnClickListener(this);
        fabQuote.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        tvConsumerQuotes.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        cardViewMembership.setOnClickListener(this);
        cardViewProfile.setOnClickListener(this);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                fab.setVisibility(View.GONE);
                final int position = viewHolder.getAdapterPosition();
               // final String item = indoxEnquiries.android.toString();

                indoxEnquiries.removeItem(position);


             /*   Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                        .setDuration(10000);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callApi();
                        recyclerView1.scrollToPosition(position);


                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();*/

                String eid1 = AppData.getInstance().getEid();

                new DeleteEnquiryAsyncTask(eid1).execute();
                fab.setVisibility(View.VISIBLE);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onResume() {
        Objects.requireNonNull(getActivity()).setTitle("Inbox");
        super.onResume();
    }

    private void callApi() {
        if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            new IndoxAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.indox_main, menu);

        menu.findItem(R.id.refresh).setVisible(true);
        menu.findItem(R.id.app_bar_search).setVisible(true);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            assert searchManager != null;
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    if (flEnquiry.getVisibility() == View.VISIBLE) {
                        if (indoxEnquiryModelArrayList.size() != 0)
                            indoxEnquiries.filter(newText);
                    } else {
                        if (quotesModelArrayList.size() != 0)
                            rQuotesAdapter.filter(newText);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    if (flEnquiry.getVisibility() == View.VISIBLE) {
                        if (indoxEnquiryModelArrayList.size() != 0)
                            indoxEnquiries.filter(query);
                    } else {
                        if (quotesModelArrayList.size() != 0)
                            rQuotesAdapter.filter(query);
                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.app_bar_search:
                break;

            case R.id.refresh:
                if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("2")) {
                    toggle.setVisibility(View.GONE);
                    tvConsumerQuotes.setVisibility(View.VISIBLE);
                    new RecievedQuotesAsyncTask().execute();

                } else {
                    callApi();
                    new RecievedQuotesAsyncTask().execute();
                }
                return true;
            default:
                searchView.setOnQueryTextListener(queryTextListener);
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                HomeScreen activity = (HomeScreen) v.getContext();
                post_new_enquiry postNewEnquiry = new post_new_enquiry();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, postNewEnquiry).addToBackStack(null).commit();
                break;
            case R.id.fabQuote:
                HomeScreen activit1y = (HomeScreen) v.getContext();
                post_new_enquiry postNewEnquiry1 = new post_new_enquiry();
                activit1y.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContent, postNewEnquiry1)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.tvFilter:
                openFilterPopup();
                break;
            case R.id.cardview_membership:
                Intent subscription = new Intent(getActivity(), SubscriptionActivity.class);
                subscription.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(subscription);
                break;
            case R.id.cardview_profile:
                myprofile myprofile = new myprofile();
               // HomeScreen.replaceMyFragment(myprofile, "PROFILE");
                Log.e(TAG, "Click on user profile");
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, myprofile);
                transaction.detach(myprofile);
                transaction.attach(myprofile);
                transaction.addToBackStack(null);
             //   setTitle("PROFILE");
                transaction.commit();
                HomeScreen.mDrawerLayout.closeDrawer(HomeScreen.lnDrawer);
                break;
        }
    }

    private void openFilterPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View customView = layoutInflater.inflate(R.layout.filter_popup, null);

        tvRead = customView.findViewById(R.id.tvRead);
        tvUnread = customView.findViewById(R.id.tvUnread);
        tvAttachment = customView.findViewById(R.id.tvAttachment);

        popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(linearLayout1, Gravity.TOP | Gravity.RIGHT, 50, 280);

        tvRead.setOnClickListener(v -> {
            if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                filterFlag = "read";
                new ReadInboxAsyncTask().execute();

            } else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
            }
            popupWindow.dismiss();
        });
        tvUnread.setOnClickListener(v -> {
            if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                indoxEnquiryModelArrayList.clear();
                indoxEnquiries.android.clear();
                filterFlag = "unread";
                new ReadInboxAsyncTask().execute();

            } else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
            }
            popupWindow.dismiss();
        });
        tvAttachment.setOnClickListener(v -> {
            if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                filterFlag = "attachment";
                new IndoxAsyncTask().execute();

            } else {
                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
            }
            popupWindow.dismiss();
        });
    }


    private void showAlert(String message, final String amount) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> getPayment(amount))
                .setNegativeButton("NO", (dialog, id) -> {

                    LoginPreferences.getActiveInstance(getActivity()).setIsLoggedIn(false);
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                });


        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPayment(String paymentAmount) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "USD", "QuotesIn", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                        /*intent = jsonObject.getString("intent");
                        trn_id = jsonObject.getString("id");
                        create_time = jsonObject.getString("create_time");

                        new SubscriptionActivity.AfterTransAsyncTask().execute();*/

                        Intent intent = new Intent(getActivity(), HomeScreen.class);
                        startActivity(intent);

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

    @SuppressLint("WrongConstant")
    private void setAdapter(ArrayList<Indox_Enquiry_Model> indoxEnquiryModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + indoxEnquiryModelArrayList.size());
        indoxEnquiries = new Indox_Enquiries(getActivity(), indoxEnquiryModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(indoxEnquiries);
    }

    @SuppressLint("WrongConstant")
    private void setAdapterQuotes(ArrayList<RQuotesModel> quotesModelArrayList) {
        Log.e(TAG, "quotesModelArrayList --> " + quotesModelArrayList.size());
        rQuotesAdapter = new RQuotesAdapter(getActivity(), quotesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setAdapter(rQuotesAdapter);

    }

    private void setUpIntroParger(List<adsResponse.Advert> result) {
        mAdapter = new AdsPagerAdapterSlider(getActivity(), result);
        pager_intro.setAdapter(mAdapter);
        dotsIndicator.setViewPager(pager_intro);
        pager_intro.setCurrentItem(0);
        pager_intro.setOnPageChangeListener(this);
        setUiPageViewController();

        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (count1 <= 2) {
                            pager_intro.setCurrentItem(count1);
                            count1++;
                        } else {
                            count1 = 0;
                            pager_intro.setCurrentItem(count1);
                        }
                    });
                }

            }
        }, 2000, 4000);
    }


    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(4, 0, 4, 0);

            viewPagerCountDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
    }

    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().show();

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteEnquiryAsyncTask extends AsyncTask<String, Void, String> {

        JSONObject jObject;
        String mail_id;
        private String response;
        String status = "";
        String responseMessage = "";

        DeleteEnquiryAsyncTask(String mail_id) {
            this.mail_id = mail_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mProgressD.isShowing()&&mProgressD!=null){
                mProgressD.dismiss();
            }
            Log.e(TAG, "QuotesDetailAsyncTask api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    } else if (status.equals("Failed")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (mProgressD!=null&&mProgressD.isShowing()) {
                mProgressD.dismiss();
            }

        }

        private String callService() {
            String url = APIUrl.Delete_Enq;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "DeleteAsyncTask after connection url: " + url);

                Log.e(TAG, "enq_id: " + mail_id);

                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("enquiry_id", mail_id);
                client.addFormPart("review_status", "2");

                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "DeleteAsyncTask response :" + response);
                Log.e(TAG, "DeleteAsyncTask response :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ReadInboxAsyncTask extends AsyncTask<String, Void, String> {
        JSONObject jObject;
        private String response;
        String status = "";
        String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "ReadInboxAsyncTask api response is " + response);
            if (response == null) {
                //Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    if (status.equalsIgnoreCase("Success")) {
                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            if (jsonarry != null) {
                                indoxEnquiryModelArrayList.clear();
                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);
                                    Indox_Enquiry_Model indoxEnquiryModel = new Indox_Enquiry_Model();
                                    indoxEnquiryModel.setId(data.getString("enquiry_id"));
                                    indoxEnquiryModel.setConsumer_username(data.getString("business_username"));
                                    indoxEnquiryModel.setReview_Status(data.getString("review_status"));

                                    JSONObject enquiry = data.getJSONObject("enquiry");

                                    indoxEnquiryModel.setEnquiry_title(enquiry.getString("enquiry_title"));
                                    indoxEnquiryModel.setEnquiry_description(enquiry.getString("enquiry_description"));
                                    indoxEnquiryModel.setEnquiry_post_date(enquiry.getString("enquiry_post_date"));
                                    indoxEnquiryModel.setEnquiry_expired(enquiry.getString("enquiry_expired"));
                                    indoxEnquiryModel.setUser_id(enquiry.getString("user_id"));
                                    indoxEnquiryModel.setConsumer_username(enquiry.getString("consumer_username"));
                                    indoxEnquiryModel.setConsumer_type(enquiry.getString("consumer_type"));
                                    indoxEnquiryModel.setConsumer_email(enquiry.getString("consumer_email"));
                                    indoxEnquiryModel.setEnquiry_status(enquiry.getString("enquiry_status"));
                                    indoxEnquiryModel.setLocation_code(enquiry.getString("location_code"));
                                    indoxEnquiryModelArrayList.add(indoxEnquiryModel);
                                }
                                if (indoxEnquiryModelArrayList.size() > 0) {
                                    setAdapter(indoxEnquiryModelArrayList);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (status.equals("failed")) {
                        setAdapter(indoxEnquiryModelArrayList);
                    } else {
                        // Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (mProgressD.isShowing()&&mProgressD!=null){
                mProgressD.dismiss();
            }
        }

        private String callService() {
            String url = APIUrl.Filter_Read_Enquiry_List;

            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "Inbox after connection url: " + url);

                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());

                if (filterFlag.equals("read"))
                    client.addFormPart("review_status", "1");
                else
                    client.addFormPart("review_status", "0");


                response = client.getResponse();
                Log.e(TAG, "Inbox response :" + response);
                Log.e(TAG, "Login user_name :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class IndoxAsyncTask extends AsyncTask<String, Void, String> {
        JSONObject jObject;
        String count;
        String id;
        private String response;
        String status = "";
        String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mProgressD.isShowing()&&mProgressD!=null){
                mProgressD.dismiss();
            }
            Log.e(TAG, "Inbox api response is " + response);
            if (response == null) {
                //Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");


                    if (status.equalsIgnoreCase("Success")) {
                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            if (jsonarry != null) {
                                indoxEnquiryModelArrayList.clear();
                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);

                                    Indox_Enquiry_Model indoxEnquiryModel = new Indox_Enquiry_Model();
                                    indoxEnquiryModel.setId(data.getString("id"));
                                    indoxEnquiryModel.setEnquiry_title(data.getString("enquiry_title"));
                                    indoxEnquiryModel.setEnquiry_description(data.getString("enquiry_description"));
                                    indoxEnquiryModel.setEnquiry_post_date(data.getString("enquiry_post_date"));
                                    indoxEnquiryModel.setEnquiry_expired(data.getString("enquiry_expired"));
                                    indoxEnquiryModel.setUser_id(data.getString("user_id"));
                                    indoxEnquiryModel.setConsumer_username(data.getString("consumer_username"));
                                    indoxEnquiryModel.setConsumer_type(data.getString("consumer_type"));
                                    indoxEnquiryModel.setConsumer_email(data.getString("consumer_email"));
                                    indoxEnquiryModel.setEnquiry_status(data.getString("enquiry_status"));
                                    indoxEnquiryModel.setLocation_code(data.getString("location_code"));
                                    indoxEnquiryModel.setEnquiry_logo(data.getString("enquiry_loco"));
                                    indoxEnquiryModel.setQ_count(data.getString("quote_count"));


                                    if (!LoginPreferences.getActiveInstance(getActivity()).getId().equals(data.getString("user_id")))
                                        indoxEnquiryModelArrayList.add(indoxEnquiryModel);
                                }
                            }
                            if (indoxEnquiryModelArrayList.size() > 0) {
                                setAdapter(indoxEnquiryModelArrayList);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (responseMessage.equals("No attachment enquiries")) {
                        setAdapter(indoxEnquiryModelArrayList);
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    }
                    if (indoxEnquiryModelArrayList.size() == 0) {
                        ivEmpty.setVisibility(View.VISIBLE);
                        llEnquiry.setVisibility(View.GONE);

                    } else if (responseMessage.equalsIgnoreCase("You need to pay your commission first")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();

                        JSONObject jsonarry = jObject.getJSONObject("result");
                        String amount = jsonarry.getString("commission_amount");
                        showAlert(responseMessage, amount);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (mProgressD.isShowing()&&mProgressD!=null){
                mProgressD.dismiss();
            }
        }

        private String callService() {
            String url;

            if (filterFlag.equals("Normal")) {
                url = APIUrl.Indox_Email;
            } else {
                url = APIUrl.attachment_enquiry_list;

            }


            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "Inbox after connection url: " + url);

                if (filterFlag.equals("Normal")) {
                    client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                    client.addFormPart("location_id", LoginPreferences.getActiveInstance(getActivity()).getLocationId());
                    client.addFormPart("service_id", LoginPreferences.getActiveInstance(getActivity()).getService_id());
                    client.addFormPart("skip", "0");
                    client.addFormPart("take", "1000");
                } else {
                    client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());

                    filterFlag = "Normal";
                }

                client.finishMultipart();

                response = client.getResponse();

                Log.e(TAG, "Inbox response :" + response);
                Log.e(TAG, "Login user_name :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                Log.e(TAG, "Login user_id :" + LoginPreferences.getActiveInstance(getActivity()).getId());
                Log.e(TAG, "Login location_id :" + LoginPreferences.getActiveInstance(getActivity()).getLocationId());
                Log.e(TAG, "Login role_id :" + LoginPreferences.getActiveInstance(getActivity()).getRole_id());
                Log.e(TAG, "Login service_id :" + LoginPreferences.getActiveInstance(getActivity()).getService_id());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RecievedQuotesAsyncTask extends AsyncTask<String, Void, String> {

        JSONObject jObject;
        String id;
        private String response;
        String status = "";
        String responseMessage = "";

        @Override
        protected void onPreExecute() {
            mProgressD = ProgressD.show(getActivity(), "", false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mProgressD.isShowing()&&mProgressD!=null){
                mProgressD.dismiss();
            }
            Log.e(TAG, "RecievedQuotesAsyncTask api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            //if (jsonarry != null) {
                                quotesModelArrayList.clear();

                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);

                                    RQuotesModel rQuotesModel = new RQuotesModel();
                                    //  rQuotesModel.setQuote_id(data.getString("id"));
                                    rQuotesModel.setEnquiry_id(data.getString("id"));
                                    rQuotesModel.setQuotes_title(data.getString("enquiry_title"));
                                    rQuotesModel.setQuotes_desc(data.getString("enquiry_description"));
                                    rQuotesModel.setQuotes_post_date(data.getString("enquiry_post_date"));
                                    rQuotesModel.setQuotes_expired(data.getString("enquiry_expired"));
                                    rQuotesModel.setUser_id(data.getString("user_id"));
                                    rQuotesModel.setConsumer_username(data.getString("consumer_username"));
                                    rQuotesModel.setConsumer_type(data.getString("consumer_type"));
                                    rQuotesModel.setConsumer_email(data.getString("consumer_email"));
                                    rQuotesModel.setQuotes_status(data.getString("enquiry_status"));
                                    rQuotesModel.setLocation_code(data.getString("location_code"));
                                    rQuotesModel.setEnquiry_logo(data.getString("enquiry_loco"));
                                    rQuotesModel.setFeedbackStatus(data.getString("feedback_status"));
                                    rQuotesModel.setqCount(data.getString("quote_count"));


                                    Log.e(TAG, "dsjkld" + data.getString("enquiry_title"));


                                    JSONArray jsonarryquote = data.getJSONArray("quotes");


                                    ZeroOccur.clear();
                                    rQuotesModel.setWon_status("0");
                                    if (!jsonarryquote.isNull(0)) {
                                        for (int i1 = 0; i1 < jsonarryquote.length(); i1++) {
                                            JSONObject data1 = jsonarryquote.getJSONObject(i1);

                                            rQuotesModel.setqStatus(data1.getString("status"));
                                            ZeroOccur.add(data1.getString("status"));

                                            rQuotesModel.setWon_status(data1.getString("quote_won").replace("null", ""));
                                            rQuotesModel.setQwon_date(data1.getString("quote_won_date").replace("null", ""));
                                            Log.e("q_won----", data1.getString("quote_won"));

                                            if (data1.getString("quote_won").equals("1") && !rQuotesModel.getQwon_date().equals("")) {

                                                AppData.getInstance().setQwonstatus("1");
                                            }
                                        }
                                    }

                                    int occurrences = Collections.frequency(ZeroOccur, "0");

                                    rQuotesModel.setUnreadTot(String.valueOf(occurrences));
                                    Log.e(TAG, "occurence" + occurrences);
                                    quotesModelArrayList.add(rQuotesModel);
                             //   }
                            }
                            if (quotesModelArrayList.size() > 0) {
                                setAdapterQuotes(quotesModelArrayList);
                                Log.e("Adapter working", "---");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (responseMessage.equalsIgnoreCase("No Quotes Found")) {
                        ivEmpty2.setVisibility(View.VISIBLE);
                        LLQuote.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String callService() {
            String url = APIUrl.RECIEVED_QUOTES;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "RECIEVED_QUOTES after connection url: " + url);

                client.addFormPart("user_id", LoginPreferences.getActiveInstance(getActivity()).getId());

                Log.e(TAG, "RECIEVED_QUOTES user_id :" + LoginPreferences.getActiveInstance(getActivity()).getId());
                client.finishMultipart();

                response = client.getResponse();

                Log.e(TAG, "RECIEVED_QUOTES response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class BannerAddTask extends AsyncTask<String, Void, String> {
        public String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = CustomHttpClient.executeHttpGet(APIUrl.API_ADVERT + "?location_code=1,2,3&role_id=5");
                Log.e(TAG, "BANNER_IMAGES Response :" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {

            } else {

                try {
                    Gson gson = new Gson();
                    adsResponse response = gson.fromJson(result, adsResponse.class);

                    if (response.status.equals("success")) {

                        if (response.advert.size() != 0) {
                            // for (int i=0; i<=response.advert.size() ; i++)
                            setUpIntroParger(response.advert);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
