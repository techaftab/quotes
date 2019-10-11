package com.quotesin.quotesin.fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment implements View.OnClickListener {
    View view;
    AppCompatTextView tvReset;
    Switch simpleSwitch;
    String TAG = "Settings";


    public Settings() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        initViews();
        registerClickListener();


        if (LoginPreferences.getActiveInstance(getActivity()).getOnlineStatus().equals("1")) {
            simpleSwitch.setChecked(true);

        } else if (LoginPreferences.getActiveInstance(getActivity()).getOnlineStatus().equals("0")) {
            simpleSwitch.setChecked(false);
        } else {
            simpleSwitch.setChecked(true);
        }


        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (CommonMethod.isOnline(Objects.requireNonNull(getActivity()))) {
                    if (isChecked)
                        new OnlineOfflineTask("1").execute();
                    else
                        new OnlineOfflineTask("0").execute();

                } else {
                    CommonMethod.showAlert("Please check internet connection !!", getActivity());
                }
            }

        });


        return view;
    }

    private void registerClickListener() {
        tvReset.setOnClickListener(this);
    }

    private void initViews() {
        tvReset = view.findViewById(R.id.tvReset);
        simpleSwitch = view.findViewById(R.id.simpleSwitch); // initiate Switch

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvReset:
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ResetPassword resetPassword = new ResetPassword();
                transaction.replace(R.id.flContent, resetPassword);
                transaction.detach(resetPassword);
                transaction.attach(resetPassword);
                transaction.addToBackStack(null);
                getActivity().setTitle("Reset Password");
                transaction.commit();
                break;


            case R.id.simpleSwitch:
                String status;
                if (simpleSwitch.isChecked())
                    status = simpleSwitch.getTextOn().toString();
                else
                    status = simpleSwitch.getTextOff().toString();

                Log.e(TAG, "Status" + status);

                break;


        }
    }


    @SuppressLint("StaticFieldLeak")
    public class OnlineOfflineTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        private String status = "";
        private String responseMessage = "";
        String textStatus;

        OnlineOfflineTask(String textOn) {
            this.textStatus = textOn;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
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
            mProgressD.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), "Please check internet Connection", Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "result-- " + result);
                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("States");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        try {
                            JSONObject jsonObject = jObject.getJSONObject("result");

                            if (jsonObject != null) {
                                LoginPreferences.getActiveInstance(getActivity()).setOnlineStatus(jsonObject.getString("Notification_alert"));

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        private String callService() {
            String url = APIUrl.Notification_Status;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "Notification_Status after connection url: " + url);

                Log.e(TAG, "textStatus " + textStatus);

                client.addFormPart("id", LoginPreferences.getActiveInstance(getActivity()).getId());
                client.addFormPart("business_name", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("notification_status", textStatus);
                client.finishMultipart();

                result = client.getResponse();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
