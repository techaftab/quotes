package com.quotesin.quotesin.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.Login;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONException;
import org.json.JSONObject;



public class ResetPassword extends Fragment {
    String TAG="ResetPassword";
    View view;
    Button btn_submit;
    EditText etConfrmPass, etNewPass, etCurrent;

    public ResetPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.reset_password, container, false);

        initViews();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidation()) {
                    if (CommonMethod.isNetworkAvailable(getActivity())) {
                        CommonMethod.hideSoftKeyboard(getActivity());
                        new ResetPassAsyncTask().execute();
                    } else {
                        Toast.makeText(getActivity(), "Check the internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return view;
    }

    private boolean checkValidation() {
        if (etCurrent.getText().toString().trim().length() == 0) {
            CommonMethod.showAlert("Please input Current Password", getActivity());
            return false;
        } else if (etNewPass.getText().toString().length() == 0) {
            CommonMethod.showAlert("Please input New Password", getActivity());
            return false;
        } else if (etConfrmPass.getText().toString().length() == 0) {
            CommonMethod.showAlert("Please input Confirm Password", getActivity());
            return false;
        } else if (!etNewPass.getText().toString().equals(etConfrmPass.getText().toString())) {
            CommonMethod.showAlert("Confirm Password Not Match", getActivity());
            return false;
        }
        return true;
    }

    private void initViews() {
        etCurrent = view.findViewById(R.id.etCurrent);
        etConfrmPass = view.findViewById(R.id.etConfrmPass);
        etNewPass = view.findViewById(R.id.etNewPass);
        btn_submit = view.findViewById(R.id.btn_submit);
    }

    public class ResetPassAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", true);
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
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(result);

                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("result");

                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getActivity(), "Reset Password Successfully...", Toast.LENGTH_SHORT).show();

                        LoginPreferences.getActiveInstance(getActivity()).setIsLoggedIn(false);
                        Intent in = new Intent(getActivity(), Login.class);
                        startActivity(in);
                        getActivity().finish();

                    } else if (status.equals("failed")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.Reset_Password;

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);

            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);


                Log.e(TAG, "user_id :" + LoginPreferences.getActiveInstance(getActivity()).getId());
                Log.e(TAG, "old_password is :" + etCurrent.getText().toString());
                Log.e(TAG, "new_password is :" + etNewPass.getText().toString());

                client.addFormPart("user_id", LoginPreferences.getActiveInstance(getActivity()).getId());
                client.addFormPart("old_password", etCurrent.getText().toString());
                client.addFormPart("new_password", etNewPass.getText().toString());

                client.finishMultipart();
                response = client.getResponse();
                Log.e("response", response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Reset Password");
        super.onResume();
    }
}
