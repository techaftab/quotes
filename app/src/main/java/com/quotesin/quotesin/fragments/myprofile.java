package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.activities.SubscriptionActivity;
import com.quotesin.quotesin.adapter.ProfileServicesAdapter;
import com.quotesin.quotesin.adapter.ReviewAdapter;
import com.quotesin.quotesin.adapter.ServicesAdapter;
import com.quotesin.quotesin.model.ProfileServicesModel;
import com.quotesin.quotesin.model.ReviewModel;
import com.quotesin.quotesin.model.ServicesModel;
import com.quotesin.quotesin.model.spinner_model_category;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;
import static com.quotesin.quotesin.utils.CommonMethod.showAlert;

/**
 * A simple {@link Fragment} subclass.
 */
public class myprofile extends Fragment implements View.OnClickListener {
    String TAG = "myprofile";
    View view;
    private TextView tvPercen, tvPercen1, tvPostiveNo, tvNuturalNo, tvNegativeNo, tvSentEnq, tvQR, tvComPro;
    private TextView tv, tv1, tv2;
    private CircleImageView user_profile_image;
    private RatingBar ratingBar, ratingBar_review;
    private EditText etName;
    private Button btn_edit, btn_NameEdit, btn_AbtEdit, btn_ServiceEdit;
    private LinearLayout LlProfile, LlReview;
    private FrameLayout flServices, flCategory;
    private AlertDialog.Builder builder;
    private TextView tvAbout, tvUpgradePlan, tvService, tvPlanName, tvPlanDescription;
    private RadioGroup toggle;
    private RadioButton profile, reviews;
    private RelativeLayout LlEditProfile;
    private ImageView user_profile_image1, ivBack;

    private String change_flag = "0";
    private static final int CAMERA_REQUEST = 0;
    final public static int MAKE_CALL = 101;
    private double imageName;
    private byte[] mData;
    private int REQUEST_CAMERA = 0;
    private int PICK_IMAGE = 1;
    private String Base_url = "https://www.webmobril.org/dev/quotesinapp/";
    private String myId, b_username, b_name, about, service_id, service_name, name;
    RecyclerView recyclerview;
    private ProgressBar mProgress;
    private RecyclerView recyclerView1, recyclerviewReview;
    private LinearLayout LlUpdateSu, llReview, LlPlans;
    private RelativeLayout RLSP;
    private Spinner spinner1;

    private ArrayList<ProfileServicesModel> profileServicesModelArrayList = new ArrayList<ProfileServicesModel>();
    private ProfileServicesAdapter profileServicesAdapter;

    private ArrayList<ReviewModel> reviewModelArrayList = new ArrayList<ReviewModel>();
    private ReviewAdapter reviewAdapter;

    private ArrayList<spinner_model_category> categoryArrayList = new ArrayList<spinner_model_category>();
    private ArrayList<ServicesModel> servicesModelArrayList = new ArrayList<ServicesModel>();

    private ArrayAdapter<String> adapterSpCategory;
    private ServicesAdapter servicesAdapter;

    private String selectedCategoriesId;
    private String selectedIds;
    private String plan_name, plan_descri;

    public myprofile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myprofile, container, false);

        gettingCategory();
        initViews();
        callApiGetProfile();
        registerClickListener();

        toggle.setOnCheckedChangeListener((group, checkedId) -> {
            if (profile.isChecked()) {
                reviews.setTextColor(0XFF168ADD);
                profile.setTextColor(Color.WHITE);
                LlProfile.setVisibility(View.VISIBLE);
                LlReview.setVisibility(View.GONE);
                llReview.setVisibility(View.GONE);

            }
            if (reviews.isChecked()) {
                profile.setTextColor(0XFF168ADD);
                reviews.setTextColor(Color.WHITE);
                LlProfile.setVisibility(View.GONE);
                LlReview.setVisibility(View.VISIBLE);
                llReview.setVisibility(View.VISIBLE);
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner1.setSelection(position);
                if (categoryArrayList.size() > 0) {
                    try {
                      /*  if (position == 0 && ! service_id.equals("")) {
                            CommonMethod.showAlert("Please select Category", getActivity());
                            selectedCategoriesId = "";
                        } else {*/
                        selectedCategoriesId = categoryArrayList.get(position - 1).getId();

                        Log.e(TAG, "item getId : " + categoryArrayList.get(position - 1).getId());
                        Log.e(TAG, "item getId-- : " + selectedCategoriesId);

                        getServicesList();

                        tvService.setText("Choose Services");


                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void initViews() {
        ivBack = view.findViewById(R.id.ivBack);
        toggle = view.findViewById(R.id.toggle);
        reviews = view.findViewById(R.id.reviews);
        profile = view.findViewById(R.id.profile);
        tvService = view.findViewById(R.id.tvService);
        builder = new AlertDialog.Builder(getActivity());
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_ServiceEdit = view.findViewById(R.id.btn_ServiceEdit);
        btn_AbtEdit = view.findViewById(R.id.btn_AbtEdit);
        btn_NameEdit = view.findViewById(R.id.btn_NameEdit);
        user_profile_image = view.findViewById(R.id.user_profile_image);
        ratingBar = view.findViewById(R.id.ratingBar);
        etName = view.findViewById(R.id.etName);
        LlProfile = view.findViewById(R.id.LlProfile);
        LlReview = view.findViewById(R.id.LlReview);
        tvAbout = view.findViewById(R.id.tvAbout);
        tvUpgradePlan = view.findViewById(R.id.tvUpgradePlan);
        flServices = view.findViewById(R.id.flServices);
        flCategory = view.findViewById(R.id.flCategory);

        recyclerview = view.findViewById(R.id.recyclerview);
        ratingBar_review = view.findViewById(R.id.ratingBar_review);

        tvPercen = view.findViewById(R.id.tvPercen);
        tvPercen1 = view.findViewById(R.id.tvPercen1);
        tvPostiveNo = view.findViewById(R.id.tvPostiveNo);
        tvNuturalNo = view.findViewById(R.id.tvNuturalNo);
        tvNegativeNo = view.findViewById(R.id.tvNegativeNo);
        tvSentEnq = view.findViewById(R.id.tvSentEnq);
        tvQR = view.findViewById(R.id.tvQR);
        tvComPro = view.findViewById(R.id.tvComPro);
        llReview = view.findViewById(R.id.llReview);

        if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5")) {
            toggle.setVisibility(View.VISIBLE);
        } else {
            toggle.setVisibility(View.GONE);
        }


        LlEditProfile = view.findViewById(R.id.LlEditProfile);
        user_profile_image1 = view.findViewById(R.id.user_profile_image1);

        LlUpdateSu = view.findViewById(R.id.LlUpdateSu);

        recyclerviewReview = view.findViewById(R.id.recyclerviewReview);

        LlPlans = view.findViewById(R.id.LlPlans);
        tvPlanName = view.findViewById(R.id.tvPlanName);
        tvPlanDescription = view.findViewById(R.id.tvPlanDescription);

        tv = view.findViewById(R.id.tv);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);

        RLSP = view.findViewById(R.id.RLSP);
        spinner1 = view.findViewById(R.id.spinner1);

        if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("2")) {
            recyclerview.setVisibility(View.GONE);
            RLSP.setVisibility(View.GONE);
            flServices.setVisibility(View.GONE);
            flCategory.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.VISIBLE);
            RLSP.setVisibility(View.VISIBLE);
         /*   flCategory.setVisibility(View.VISIBLE);
            flServices.setVisibility(View.VISIBLE);*/
        }

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.green_progress_drawable);
        mProgress = view.findViewById(R.id.circularProgressbar);
        // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);


        etName.setText(LoginPreferences.getActiveInstance(getActivity()).getUser_first_name());

        if (LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic() != null) {
            Log.e("user image ", "not  Null");
            Picasso.get()
                    .load(LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic())
                    .error(R.mipmap.avatar_male)
                    .into(user_profile_image);

            Picasso.get()
                    .load(LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic())
                    .error(R.mipmap.avatar_male)
                    .into(user_profile_image1);
        } else {
            Log.e("user image ", "is  Null");
            Picasso.get()
                    .load(LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic())
                    .placeholder(R.mipmap.avatar_male)
                    .error(R.mipmap.avatar_male)
                    .into(user_profile_image);

            Log.e("user image ", LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic());

            Picasso.get()
                    .load(LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic())
                    .placeholder(R.mipmap.avatar_male)
                    .error(R.mipmap.avatar_male)
                    .into(user_profile_image1);
            Log.e("user image ", LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic());
        }


        adapterSpCategory = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout_black) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"

                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

            @Override
            public void setDropDownViewResource(int resource) {

                resource = R.layout.spinner_layout_black;
                super.setDropDownViewResource(resource);
            }
        };


    }

    private void registerClickListener() {
        toggle.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        LlProfile.setOnClickListener(this);
        LlReview.setOnClickListener(this);
        btn_NameEdit.setOnClickListener(this);
        btn_AbtEdit.setOnClickListener(this);
        btn_ServiceEdit.setOnClickListener(this);
        tvUpgradePlan.setOnClickListener(this);
        LlEditProfile.setOnClickListener(this);
        flServices.setOnClickListener(this);
        LlUpdateSu.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btn_NameEdit.setOnClickListener(this);
        flCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivBack:
                Intent intent = new Intent(getActivity(), HomeScreen.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                //finishCurrentFragment();
                break;


            case R.id.btn_edit:
                if (change_flag.equals("0")) {
                    callApiGetProfile();

                    ratingBar.setVisibility(View.GONE);
                    btn_AbtEdit.setVisibility(View.VISIBLE);
                    btn_NameEdit.setVisibility(View.VISIBLE);
                    btn_ServiceEdit.setVisibility(View.VISIBLE);
                    LlEditProfile.setVisibility(View.VISIBLE);

                    flCategory.setVisibility(View.GONE);
                    flServices.setVisibility(View.GONE);

                    if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5")) {
                        flCategory.setVisibility(View.VISIBLE);
                        flServices.setVisibility(View.VISIBLE);

                    } else {
                        flCategory.setVisibility(View.GONE);
                        flServices.setVisibility(View.GONE);
                    }

                    btn_edit.setBackgroundResource(R.mipmap.save_btn);
                    etName.setEnabled(true);
                    change_flag = "1";

                } else {
                    if (selectedIds != null) {
                        if (spinner1.getSelectedItem().toString().equalsIgnoreCase("Select Category") && !selectedIds.equals("")) {
                            showAlert("Please select Category", getActivity());
                        } else {
                            callApiEditProfile();

                            ratingBar.setVisibility(View.VISIBLE);
                            btn_AbtEdit.setVisibility(View.GONE);
                            btn_NameEdit.setVisibility(View.GONE);
                            btn_ServiceEdit.setVisibility(View.GONE);
                            LlEditProfile.setVisibility(View.GONE);
                            flCategory.setVisibility(View.GONE);
                            flServices.setVisibility(View.GONE);

                            btn_edit.setBackgroundResource(R.mipmap.edit_btn);

                            callApiGetProfile();

                            etName.setEnabled(false);
                            change_flag = "0";
                        }
                    } else {
                        callApiEditProfile();

                        ratingBar.setVisibility(View.VISIBLE);
                        btn_AbtEdit.setVisibility(View.GONE);
                        btn_NameEdit.setVisibility(View.GONE);
                        btn_ServiceEdit.setVisibility(View.GONE);
                        LlEditProfile.setVisibility(View.GONE);
                        flCategory.setVisibility(View.GONE);
                        flServices.setVisibility(View.GONE);

                        btn_edit.setBackgroundResource(R.mipmap.edit_btn);

                        callApiGetProfile();

                        etName.setEnabled(false);
                        change_flag = "0";
                    }

                }
                break;

            case R.id.btn_AbtEdit:
                openAboutDialog();
                break;

            case R.id.btn_ServiceEdit:
                break;

            case R.id.tvUpgradePlan:
                Intent subs = new Intent(getActivity(), SubscriptionActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(subs);
                break;

            case R.id.LlEditProfile:
                ChangeProfile();
                break;

            case R.id.flServices:
                if (selectedCategoriesId != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.dialog_recyclerview, null);
                    recyclerView1 = dialogLayout.findViewById(R.id.recyclerview);
                    setServicesAdapter(servicesModelArrayList);
                    builder.setView(dialogLayout);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getList();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {
                    CommonMethod.showAlert("Choose Category", getActivity());
                }

                break;


            case R.id.LlUpdateSu:
                Intent subscription = new Intent(getActivity(), SubscriptionActivity.class);
                subscription.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(subscription);
                break;
        }
    }

    public boolean checkServiceVal() {
        if (selectedIds != null)
            if (spinner1.getSelectedItem().toString().equalsIgnoreCase("Select Category") && !selectedIds.equals("")) {
                showAlert("Please select Category", getActivity());
                return false;
            }
        return true;
    }


    private String getList() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (ServicesModel number : servicesModelArrayList) {
            if (number.isSelected()) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append(",");
                stringBuilder1.append(",");
                stringBuilder.append(number.getId());
                stringBuilder1.append(number.getSer_name());
            }
        }
        selectedIds = stringBuilder.toString();
        String services = stringBuilder1.toString();
        if (!services.equals("")) {
            String services1 = services.substring(1);
            tvService.setText(services1);
            Log.e("selectedIds---", selectedIds);
            Log.e("tvService---", selectedIds);
        } else {
            tvService.setText("Choose Services");
        }
        return selectedIds;
    }

    private void callApiEditProfile() {
        if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            hideSoftKeyboard(getActivity());
            new EditProfileAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingCategory() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            hideSoftKeyboard(getActivity());
            new GetCategoryAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getServicesList() {
        if (CommonMethod.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            hideSoftKeyboard(getActivity());
            new GetServicesAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class GetCategoryAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        String status = "";
        String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = callService();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mProgressD!=null&&mProgressD.isShowing()){
                mProgressD.dismiss();
            }

            Log.e(TAG, "get countries api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");

                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jObject.getJSONArray("result");

                        if (jsonArray != null) {
                            categoryArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);

                                spinner_model_category model = new spinner_model_category();

                                model.setId(post.getString("id"));
                                model.setTitle(post.getString("category_name"));

                                categoryArrayList.add(model);
                            }
                            Log.e(TAG, " Countries size: " + categoryArrayList.size() + "");
                            setUpCategoryAdapter();
                        }

                    } else if (status.equals("Failed")) {
                        setUpCategoryAdapter();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (getActivity()!=null) {
                mProgressD.dismiss();
            }
        }


        private String callService() {
            String url = APIUrl.GET_CATEGORY;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "GET_CATEGORY after connection url: " + url);

                client.finishMultipart();

                result = client.getResponse();
                Log.e(TAG, "GET_CATEGORY response :" + result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }


    private void setUpCategoryAdapter() {
        if (getActivity() != null) {
            Log.e(TAG, " Countries size: " + categoryArrayList.size() + "");

            ArrayList<String> countries = new ArrayList<>();
            countries.clear();

            countries.add("Select Category");

            for (spinner_model_category model1 : categoryArrayList) {
                countries.add(model1.getTitle());
                adapterSpCategory.add(model1.getTitle());
            }
            spinner1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout_black, countries));
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class GetServicesAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        String status = "";
        String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "", true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
                postData.add(new BasicNameValuePair("catid", selectedCategoriesId));
                result = CustomHttpClient.executeHttpPost(APIUrl.GET_SERVICES, postData);
                System.out.print(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "get Services api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jObject.getJSONArray("result");
                        if (jsonArray != null) {

                            servicesModelArrayList = new ArrayList<ServicesModel>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);

                                ServicesModel model = new ServicesModel();

                                model.setId(post.getString("id"));
                                model.setSer_name(post.getString("service_name"));


                                servicesModelArrayList.add(model);
                            }

                            Log.e(TAG, " States size: " + servicesModelArrayList.size() + "");
                            /*if (servicesModelArrayList.size() > 0) {
                                setServicesAdapter(servicesModelArrayList);
                            }*/
                        }

                    } else if (status.equals("Failed")) {
                        setServicesAdapter(servicesModelArrayList);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }

    }

    @SuppressLint("WrongConstant")
    private void setServicesAdapter(ArrayList<ServicesModel> servicesModelArrayList) {
        Log.e(TAG, "servicesModelArrayList --> " + servicesModelArrayList.size());
        servicesAdapter = new ServicesAdapter(getActivity(), servicesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(servicesAdapter);
    }


    private void callApiGetProfile() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new GetProfileAsyncTask().execute();
            new GetReviewAsyncTask().execute();

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    public class GetProfileAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            Log.e("Sonal ", "#####Response" + response);

            if (response == null) {
                //Toast.makeText(getActivity(), "Please Check Internet ", Toast.LENGTH_LONG).show();
            } else {
                JSONObject object;
                try {
                    object = new JSONObject(response);
                    String success = object.getString("Status");
                    String responseMessage = object.getString("message");

                    Log.e("Sonal sucessss", "is  > " + responseMessage);

                    if (success.equalsIgnoreCase("Success")) {

                        JSONObject joData = object.getJSONObject("result");

                        myId = joData.getString("id");
                        b_username = joData.getString("business_username");
                        b_name = joData.getString("business_name");
                        name = joData.getString("name");

                        String user_profile_pic = joData.getString("business_logo");

                        LoginPreferences.getActiveInstance(getActivity()).setUser_first_name(name);
                        LoginPreferences.getActiveInstance(getActivity()).setUser_profile_pic(Base_url + user_profile_pic);

                        Log.e(TAG, "imageurl--->> : " + LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic());
                        Log.e(TAG, "imageurl--->> : " + name);

                        Picasso.get()
                                .load(Base_url + user_profile_pic)
                                .placeholder(R.mipmap.avatar_male)
                                .error(R.mipmap.avatar_male)
                                .into(user_profile_image1);

                        if (joData.getString("business_short_description") != null) {
                            about = joData.getString("business_short_description").replace("null", "You can write about yourself here..");
                        }
                        profileServicesModelArrayList.clear();


                        JSONObject plans = object.getJSONObject("subplan");

                        JSONObject plan1 = plans.getJSONObject("subscription_plans");

                        plan_name = plan1.getString("name");
                        plan_descri = plan1.getString("description");

                        tvPlanName.setText(plan_name);
                        tvPlanDescription.setText(plan_descri);

                        Log.e("TAG", plan_name);
                        Log.e("plan_descri", plan_descri);


                        JSONArray jsonarry = object.getJSONArray("services");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            JSONObject post = jsonarry.getJSONObject(i);

                            ProfileServicesModel profileServicesModel = new ProfileServicesModel();

                            service_id = post.getString("id");
                            service_name = post.getString("service_name");

                            profileServicesModel.setService_id(post.getString("id"));
                            profileServicesModel.setService_name(post.getString("service_name"));

                            Log.e("service_name", post.getString("service_name"));

                            profileServicesModelArrayList.add(profileServicesModel);
                        }
                        setupDataToViews();
                        if (profileServicesModelArrayList.size() > 0) {
                            setAdapter(profileServicesModelArrayList);
                        }

                    } else {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.GET_PROFILE;
            HttpClient client = new HttpClient(url);
            Log.e("Sonal before connection", "" + url);

            try {
                client.connectForMultipart();
                Log.e("Sonal after connection", "" + url);

                client.addFormPart("user_id", LoginPreferences.getActiveInstance(getActivity()).getId());
                Log.e("Sonal", "getUserId   -> " + LoginPreferences.getActiveInstance(getActivity()).getId());
                client.finishMultipart();

                response = client.getResponse();
                Log.e("Sonal response", response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }


    private void setAdapter(ArrayList<ProfileServicesModel> profileServicesModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + profileServicesModelArrayList.size());
        profileServicesAdapter = new ProfileServicesAdapter(getActivity(), profileServicesModelArrayList);
        final GridLayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 2);
        recyclerview.setLayoutManager(layoutManager2);
        recyclerview.setAdapter(profileServicesAdapter);

    }


    @SuppressLint("StaticFieldLeak")
    public class GetReviewAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (mProgressD!=null&&mProgressD.isShowing()){
                mProgressD.dismiss();
            }

            Log.e("Sonal ", "#####Response" + response);

            if (response == null) {
                //Toast.makeText(getActivity(), "Please Check Internet ", Toast.LENGTH_LONG).show();
            } else {
                JSONObject object;
                try {
                    object = new JSONObject(response);
                    String success = object.getString("Status");
                    String responseMessage = object.getString("message");

                    Log.e("Sonal sucessss", "is  > " + responseMessage);

                    if (success.equalsIgnoreCase("Success")) {

                        tvPercen.setText(object.getString("Average").substring(0, 1) + "%");
                        tvPercen1.setText(object.getString("Average").substring(0, 1));
                        tvPostiveNo.setText(object.getString("positive"));
                        tvNegativeNo.setText(object.getString("negative"));
                        tvNuturalNo.setText(object.getString("neutral"));

                        String overal = object.getString("Overall");
                        String overall = overal.substring(0, 1);

                        String avg = object.getString("Average");

                        float avgNum = Float.parseFloat(avg);
                        ratingBar_review.setRating(avgNum);

                        tvQR.setText(object.getString("received_quotes"));
                        tvSentEnq.setText(object.getString("sent_enquiries"));
                        tvComPro.setText(object.getString("complete"));


                        mProgress.setProgress(Integer.parseInt(overall));
                        tv.setText(overall + "%");
                        tv1.setText(overall + "%");
                        tv2.setText(overall + "%");

                        reviewModelArrayList.clear();

                        JSONArray jsonarry1 = object.getJSONArray("result");
                        for (int i = 0; i < jsonarry1.length(); i++) {

                            JSONObject post = jsonarry1.getJSONObject(i);

                            ReviewModel reviewModel = new ReviewModel();

                            reviewModel.setQuote_id(post.getString("quote_id"));
                            reviewModel.setLeft_for(post.getString("left_for"));
                            reviewModel.setQ1(post.getString("q1"));
                            reviewModel.setQ2(post.getString("q2"));
                            reviewModel.setQ3(post.getString("q3"));
                            reviewModel.setComment(post.getString("comment"));
                            reviewModel.setUser_id(post.getString("user_id"));
                            reviewModel.setConsumer_type(post.getString("consumer_type"));
                            reviewModel.setDate(post.getString("date"));


                            Log.e(TAG, "Q11111" + post.getString("q1"));
                            Log.e(TAG, "Q221" + post.getString("q2"));
                            Log.e(TAG, "Q333" + post.getString("q2"));


                            reviewModelArrayList.add(reviewModel);
                        }
                    }
                    if (reviewModelArrayList.size() > 0) {
                        setReviewAdater(reviewModelArrayList);
                        Log.e("Review Adapter working", "---");
                    } /*else if (success.equals("Failed")) {
                        // Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();

                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.view_feedback;
            HttpClient client = new HttpClient(url);
            Log.e("Sonal before connection", "" + url);

            try {
                client.connectForMultipart();
                Log.e("Sonal after connection", "" + url);

                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("user_id", LoginPreferences.getActiveInstance(getActivity()).getId());

                Log.e("Sonal", "user_name   -> " + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.finishMultipart();
                response = client.getResponse();
                Log.e("Sonal response", response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }


    private void setReviewAdater(ArrayList<ReviewModel> reviewModelArrayList) {
        Log.e(TAG, "reviewModelArrayList --> " + reviewModelArrayList.size());
        reviewAdapter = new ReviewAdapter(getActivity(), reviewModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewReview.setLayoutManager(layoutManager);
        recyclerviewReview.setAdapter(reviewAdapter);

    }


    private void setupDataToViews() {
        etName.setText(name);
        tvAbout.setText(about);
    }


    private void ChangeProfile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.CAMERA);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA}, 1);
            }
        } else {
            selectImage();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else if (items[item].equals("Select From Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "calllll");

        if (resultCode != RESULT_OK) {
            return;
        }

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bitmap bitmap;
                bitmap = (Bitmap) data.getExtras().get("data");
                // viewImage.setImageBitmap(bitmap);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                mData = bytes.toByteArray();

                Log.e("bytes is", "" + mData);
                user_profile_image.setImageBitmap(bitmap);

                if (mData != null)
                    if (CommonMethod.isNetworkAvailable(getActivity()))
                        new UpdateProfileAsyncTask().execute();
                    else {
                        Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                else {
                    Toast.makeText(getActivity(), "Please Select Profile Picture", Toast.LENGTH_SHORT).show();
                }
            }
            if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {

                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                    Log.e(TAG, String.valueOf(bitmap));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // viewImage.setImageBitmap(bitmap);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                mData = bytes.toByteArray();

                Log.e("bytes is", "" + mData);
                user_profile_image.setImageBitmap(bitmap);

                if (mData != null)
                    if (CommonMethod.isNetworkAvailable(getActivity()))
                        new UpdateProfileAsyncTask().execute();
                    else {
                        Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                else {
                    Toast.makeText(getActivity(), "Please Select Profile Picture", Toast.LENGTH_SHORT).show();
                }
            }
            imageName = System.currentTimeMillis();

        }
    }


    @SuppressLint("StaticFieldLeak")
    public class UpdateProfileAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        private String response;
        private String user_profile_pic;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressD = ProgressD.show(getActivity(), "Updating...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            Log.e(TAG, "#####Response" + response);
            //responseCode
            JSONObject object;
            try {
                object = new JSONObject(response);
                String success = object.getString("Status");
                String responseMessage = object.getString("message");
                if (success.equalsIgnoreCase("Success")) {

                    Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();

                    try {
                        JSONObject jObj = object.getJSONObject("result");
                        jObj.getString("id");
                        user_profile_pic = jObj.getString("business_logo");

                        LoginPreferences.getActiveInstance(getActivity()).setUser_profile_pic(Base_url + user_profile_pic);

                        Log.e(TAG, "UpdateProfile  imageurl--->> : " + LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic());

                        Picasso.get().load(LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic()).into(user_profile_image1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "imageurl : " + user_profile_pic);


                } else {
                    Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgressD.dismiss();

        }

        private String callService() {
            String url = APIUrl.UPDATE_PROFILE;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "before connection " + url);

            try {
                client.connectForMultipart();

                Log.e(TAG, "after connection " + url);

                client.addFormPart("id", LoginPreferences.getActiveInstance(getActivity()).getId());
                client.addFilePart("profile_img", imageName + ".jpg", mData);

                Log.e("imageeeeeeeee", imageName + ".jpg" + ", " + mData);
                Log.e("User_id", LoginPreferences.getActiveInstance(getActivity()).getId());

                client.finishMultipart();

                response = client.getResponse();
                Log.e("response", response);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }

    private void openAboutDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.edit_alertdialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("About");

        final EditText userInput = promptsView.findViewById(R.id.editData);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tvAbout.setText(userInput.getText().toString());


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    public class EditProfileAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            Log.e("Sonal ", "#####Response" + response);

            if (response == null) {
                Toast.makeText(getActivity(), "Please Check Internet ", Toast.LENGTH_LONG).show();
            } else {
                JSONObject object;
                try {
                    object = new JSONObject(response);
                    String success = object.getString("States");
                    String responseMessage = object.getString("message");

                    Log.e("Sonal sucessss", "is  > " + responseMessage);

                    if (success.equalsIgnoreCase("Success")) {

                        JSONObject joData = object.getJSONObject("result");

                        myId = joData.getString("id");
                        b_username = joData.getString("business_username");
                        b_name = joData.getString("business_name");
                        name = joData.getString("name");
                        about = joData.getString("business_short_description");


                        String user_profile_pic = joData.getString("business_logo");

                        LoginPreferences.getActiveInstance(getActivity()).setUser_profile_pic(Base_url + user_profile_pic);


                        Log.e(TAG, "imageurl--->> : " + LoginPreferences.getActiveInstance(getActivity()).getUser_profile_pic());

                        LoginPreferences.getActiveInstance(getActivity()).setUser_first_name(name);
                        Log.e(TAG, "b_name--->> : " + LoginPreferences.getActiveInstance(getActivity()).getUser_first_name());

                        profileServicesModelArrayList.clear();

                        JSONArray jsonarry = object.getJSONArray("services");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            JSONObject post = jsonarry.getJSONObject(i);

                            ProfileServicesModel profileServicesModel = new ProfileServicesModel();

                            service_id = post.getString("id");
                            service_name = post.getString("service_name");

                            profileServicesModelArrayList.add(profileServicesModel);
                        }
                        setupDataToViews();
                     /*   if (profileServicesModelArrayList.size() > 0) {
                            setAdapter(profileServicesModelArrayList);
                        }
*/
                    } else {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.Edit_Profile;
            HttpClient client = new HttpClient(url);
            Log.e("Sonal befo", "" + url);

            try {
                client.connectForMultipart();
                Log.e("Sonal after", "" + url);
                String role_id = LoginPreferences.getActiveInstance(getActivity()).getRole_id();
                if (role_id.equals("5")) {
                    if (selectedCategoriesId != null && selectedIds != null) {
                        client.addFormPart("id", LoginPreferences.getActiveInstance(getActivity()).getId());
                        client.addFormPart("business_name", etName.getText().toString());
                        client.addFormPart("about", tvAbout.getText().toString());
                        client.addFormPart("category_id", selectedCategoriesId);
                        client.addFormPart("service_id", selectedIds);
                    } else {
                        client.addFormPart("id", LoginPreferences.getActiveInstance(getActivity()).getId());
                        client.addFormPart("business_name", etName.getText().toString());
                        client.addFormPart("about", tvAbout.getText().toString());
                        client.addFormPart("category_id", "");
                        client.addFormPart("service_id", "");
                    }

                } else {
                    client.addFormPart("id", LoginPreferences.getActiveInstance(getActivity()).getId());
                    client.addFormPart("business_name", etName.getText().toString());
                    client.addFormPart("about", tvAbout.getText().toString());
                    client.addFormPart("category_id", "");
                    client.addFormPart("service_id", "");
                }


                Log.e("Sonal", "getUserId   -> " + LoginPreferences.getActiveInstance(getActivity()).getId());
                Log.e("Sonal", "getname   -> " + etName.getText().toString());
                Log.e("Sonal", "get about   -> " + tvAbout.getText().toString());
                Log.e("Sonal", "selectedCategoriesId   -> " + selectedCategoriesId);
                Log.e("Sonal", "selectedIds   -> " + selectedIds);

                client.finishMultipart();

                response = client.getResponse();
                Log.e("Edit_Profile response", response);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private void finishCurrentFragment() {
        int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount >= 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        callApiGetProfile();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
