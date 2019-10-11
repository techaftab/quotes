package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.adapter.viewFormAdapter;
import com.quotesin.quotesin.model.ViewEmailResponse;
import com.quotesin.quotesin.model.viewformModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class AfterContactQuoteDet extends Fragment {
    View view;

    String fbflag, qid1, username, desc, deadline, tittle, location_code, b_name, eid, con_email, post_date, qid, delete_flag, enq_logo, update_visFlag;
    TextView tvDeadDate, tvDesc, tvWeb, tvForm, tvRefNo, tvFName, tvBAddress, tvTel, tvMobile, tvCEmail, tvTitle;

    RecyclerView recyclerview;
    Button btn_Feedback;
    String price, fbFlag;
    String TAG = "AfterContactQuoteDet";

    viewFormAdapter enqIndoxAdapter;
    ArrayList<viewformModel> enqIndoxModelArrayList = new ArrayList<>();
    private String ref_no;

    public AfterContactQuoteDet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.after_contact_quote_det, container, false);

        initViews();
        getDataFromBundle();

        //registerclickListener();
        callApi();
        return view;
    }

    private void initViews() {
        tvFName = view.findViewById(R.id.tvFName);
        tvBAddress = view.findViewById(R.id.tvBAddress);
        tvTel = view.findViewById(R.id.tvTel);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvCEmail = view.findViewById(R.id.tvCEmail);
        tvRefNo = view.findViewById(R.id.tvRefNo);
        tvForm = view.findViewById(R.id.tvForm);
        tvWeb = view.findViewById(R.id.tvWeb);
        tvDesc = view.findViewById(R.id.tvDesc);
        tvDeadDate = view.findViewById(R.id.tvDeadDate);
        tvTitle = view.findViewById(R.id.tvTitle);

        recyclerview = view.findViewById(R.id.recyclerview);
        btn_Feedback = view.findViewById(R.id.btn_Feedback);

    }

    private void callApi() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new QuotesDetailAsyncTask().execute();
            new ViewFormAsyncTask().execute();

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromBundle() {
        Bundle b = this.getArguments();
        assert b != null;
        //  qid = b.getString("qid");
        eid = b.getString("eid");
        username = b.getString("username");
        desc = b.getString("desc");
        deadline = b.getString("deadline");
        tittle = b.getString("tittle");
        location_code = b.getString("location_code");
        con_email = b.getString("con_email");
        post_date = b.getString("post_date");
        qid = b.getString("qid");
        b_name = b.getString("b_name");
        delete_flag = b.getString("delete_flag");
        enq_logo = b.getString("enq_logo");
        update_visFlag = b.getString("update_visFlag");

        fbflag = b.getString("fbflag");

        Log.e(TAG, "username--" + username);
        Log.e(TAG, "desc--" + desc);
        Log.e(TAG, "deadline--" + deadline);
        Log.e(TAG, "tittle--" + tittle);
        Log.e(TAG, "location_code--" + location_code);
        // Log.e(TAG, "qid--" + qid);
        Log.e(TAG, "eid--" + eid);
        Log.e(TAG, "con_email--" + con_email);
        Log.e(TAG, "post_date--" + post_date);
        Log.e(TAG, "b_name--" + b_name);
        Log.e(TAG, "qid--" + qid);
        Log.e(TAG, "delete_flag--" + delete_flag);
        Log.e(TAG, "update_visFlag--" + update_visFlag);

        Log.e(TAG, "fbflag--" + fbflag);

        AppData.getInstance().setFeedbacktitle(tittle);

        tvTitle.setText(tittle);
        tvDesc.setText(desc);

        tvDeadDate.setText(deadline);


       /* tvTitle.setText(tittle);
        tvDate.setText(post_date);
        tvDeadDate.setText(deadline);
        tvEmail.setText(con_email);
        tvName.setText(username);
        tvDesc.setText(desc);

        image_letter = String.valueOf(tittle.charAt(0));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(image_letter, Color.parseColor("#168add"));
        title_img.setImageDrawable(drawable);


        if (!TextUtils.isEmpty(enq_logo)) {
            String str = enq_logo;
            Log.e("str", str);

            String str1 = str.substring(0, 4);
            Log.e("str", str1);

            if (str1.equals("site")) {
                ivDownload.setVisibility(View.VISIBLE);
            } else {
                ivDownload.setVisibility(View.GONE);
            }
        }*/
    }


    private class ViewFormAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, " ViewForm api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Gson gson = new Gson();
                    ViewEmailResponse viewEmailResponse = gson.fromJson(response, ViewEmailResponse.class);
                    if (viewEmailResponse.status.equals("Success")) {

                        if (viewEmailResponse.attachment.size() > 0) {
                            tvForm.setVisibility(View.VISIBLE);
                            setAdapter(viewEmailResponse.attachment);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Server Issue", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.View_Email;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "Indox after connection url: " + url);
                Log.e(TAG, "email_id: " + AppData.getInstance().getEid());


                client.addFormPart("email_id", eid);
                client.addFormPart("review_status", "0");
                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "Indox response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @SuppressLint("WrongConstant")
    private void setAdapter(List<ViewEmailResponse.Attachment> enqIndoxModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + enqIndoxModelArrayList.size());
        enqIndoxAdapter = new viewFormAdapter(getActivity(), enqIndoxModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(enqIndoxAdapter);

    }

    @SuppressLint("StaticFieldLeak")
    private class QuotesDetailAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "QuotesDetailAsyncTask api response is " + response);
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
                            if (jsonarry != null) {
                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);

                                    qid1 = data.getString("id");
                                    price = data.getString("quote_price");
                                }
                            }


                            if (!jObject.isNull("contact")) {
                                JSONObject json2 = jObject.getJSONObject("contact");
                                {
                                    if (json2 != null) {

                                        ref_no = json2.getString("quote_md5");
                                        Log.e(TAG, "ref_no" + ref_no);

                                        JSONObject json3 = json2.getJSONObject("user");


                                        tvFName.setText(json3.getString("business_username").replace("null", "Not Available"));
                                        tvBAddress.setText(json3.getString("business_address").replace("null", "Not Available"));
                                        tvCEmail.setText(json3.getString("business_email").replace("null", "Not Available"));
                                        tvMobile.setText(json3.getString("business_evening_telephone").replace("null", "Not Available"));
                                        tvTel.setText(json3.getString("business_telephone").replace("null", "Not Available"));
                                        tvWeb.setText(json3.getString("business_website").replace("null", "Not Available"));


                                        if (!TextUtils.isEmpty(ref_no)) {
                                            String reference = ref_no.substring(0, 10);
                                            tvRefNo.setText("Reference No:" + reference.toUpperCase());
                                        }

                                    }
                                }
                            }


                            if (!jObject.isNull("commission")) {
                                JSONObject json3 = jObject.getJSONObject("commission");
                                {
                                    if (json3 != null) {

                                        String service_status = json3.getString("service_status");

                                        Log.e(TAG, "service_status" + service_status);

                                        AppData.getInstance().setFeedbackflag("service_status");


                                        if (service_status.equals("1")) {
                                            btn_Feedback.setVisibility(View.GONE);
                                        } else {
                                            btn_Feedback.setVisibility(View.VISIBLE);

                                            btn_Feedback.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Bundle bundle = new Bundle();
                                                    HomeScreen activity = (HomeScreen) v.getContext();
                                                    LeaveFeedBAck enquiryReply = new LeaveFeedBAck();
                                                    Log.e(TAG, qid1);
                                                    bundle.putString("qid", qid1);
                                                    bundle.putString("price", price);
                                                    bundle.putString("eid", eid);

                                                    enquiryReply.setArguments(bundle);
                                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, enquiryReply).addToBackStack(null).commit();


                                                }
                                            });

                                        }


                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (status.equals("Failed")) {
                        // Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();

        }

        private String callService() {

            String url = APIUrl.consumer_quotes_details;

            /*if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5")) {
                url = APIUrl.Quotes_Details;
            } else {
                url = APIUrl.consumer_quotes_details;
            }*/

            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "QuotesDetailAsyncTask after connection url: " + url);
                Log.e(TAG, "enq_id: " + eid);
                client.addFormPart("enq_id", eid);

                if (LoginPreferences.getActiveInstance(getActivity()).getRole_id().equals("5"))
                    client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());

              /*  {
                    client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                }
*/
                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "QuotesDetailAsyncTask response :" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }


}
