package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.viewFormAdapter;
import com.quotesin.quotesin.model.ViewEmailResponse;
import com.quotesin.quotesin.model.viewformModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class view_form extends Fragment {

    View view;
    String TAG="";
    RecyclerView recyclerview;

    viewFormAdapter enqIndoxAdapter;
    ArrayList<viewformModel> enqIndoxModelArrayList = new ArrayList<>();

    public view_form() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_feedback, container, false);

        recyclerview = view.findViewById(R.id.recyclerview);


        if (CommonMethod.isNetworkAvailable(getActivity())) {
            new ViewFormAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Attach Form");
    }

    @SuppressLint("StaticFieldLeak")
    private class ViewFormAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
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
                        if (viewEmailResponse != null) {
                            if (viewEmailResponse.attachment != null) {
                                setAdapter(viewEmailResponse.attachment);
                            }
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


                client.addFormPart("email_id", AppData.getInstance().getEid());
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
}
