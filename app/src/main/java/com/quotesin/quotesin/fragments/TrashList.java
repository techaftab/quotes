package com.quotesin.quotesin.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.AdapterTrash;
import com.quotesin.quotesin.model.Indox_Enquiry_Model;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class TrashList extends Fragment {
    RecyclerView recyclerview;
    String TAG="TrashList";
    AdapterTrash adapterTrash;
    ArrayList<Indox_Enquiry_Model> indoxEnquiryModelArrayList = new ArrayList<>();

    ImageView ivEmpty;
    LinearLayout llTrash;

    View view;

    public TrashList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trash_list, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        recyclerview = view.findViewById(R.id.recyclerview);

        llTrash = view.findViewById(R.id.llTrash);
        ivEmpty = view.findViewById(R.id.ivEmpty);

        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new TrashListAsyncTask().execute();

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Trash");
        super.onResume();
    }

    private class TrashListAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";
        String id;

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

            Log.e(TAG, "TrashList api response is " + response);
            if (response == null) {
                //Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    if (status.equalsIgnoreCase("success")) {
                        try {
                            JSONArray jsonarry = jObject.getJSONArray("result");
                            if (jsonarry != null) {
                                indoxEnquiryModelArrayList.clear();
                                for (int i = 0; i < jsonarry.length(); i++) {
                                    JSONObject data = jsonarry.getJSONObject(i);
                                    Indox_Enquiry_Model indoxEnquiryModel = new Indox_Enquiry_Model();
                                    indoxEnquiryModel.setId(data.getString("id"));
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
                                    indoxEnquiryModel.setQ_count(enquiry.getString("quote_count"));

                                    indoxEnquiryModelArrayList.add(indoxEnquiryModel);

                                    Log.e(TAG, "indoxEnquiryModelArrayList :" + indoxEnquiryModelArrayList);
                                }
                            }
                            if (indoxEnquiryModelArrayList.size() > 0) {
                                setAdapter(indoxEnquiryModelArrayList);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (indoxEnquiryModelArrayList.size() == 0) {
                        ivEmpty.setVisibility(View.VISIBLE);
                        llTrash.setVisibility(View.GONE);
                    } else if (responseMessage.equals("No attachment enquiries")) {
                        setAdapter(indoxEnquiryModelArrayList);
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.Trash_List;

            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "TrashList after connection url: " + url);

                client.addFormPart("user_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("review_status", "2");

                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "Indox response :" + response);
                Log.e(TAG, "Login user_name :" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private void setAdapter(ArrayList<Indox_Enquiry_Model> indoxEnquiryModelArrayList) {
        Log.e(TAG, "indoxEnquiryModelArrayList --> " + indoxEnquiryModelArrayList.size());
        adapterTrash = new AdapterTrash(getActivity(), indoxEnquiryModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapterTrash);
    }

}
