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
import com.quotesin.quotesin.activities.HomeScreen;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.AppData;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class AfterQ_AcceptForm extends Fragment implements View.OnClickListener {

    View view;
    EditText etName, etaddress, etPostcode, etEmail, etPhone, etEveningPhone;
    Button btn_Submit;

    String Name, email, address, phone, alternateNo, postcode;

    String qid, b_name, eid;

    String TAG = this.getClass().getName();

    public AfterQ_AcceptForm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.after_quote_accept, container, false);

        initViews();
        getDataFromBundle();

        registerOnClickListener();

        return view;

    }

    private void initViews() {
        etName = view.findViewById(R.id.etName);
        etaddress = view.findViewById(R.id.etaddress);
        etPostcode = view.findViewById(R.id.etPostcode);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etEveningPhone = view.findViewById(R.id.etEveningPhone);

        btn_Submit = view.findViewById(R.id.btn_Submit);
    }

    private void registerOnClickListener() {

        btn_Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Submit:
                if (checkValidation())
                    if (CommonMethod.isNetworkAvailable(getActivity())) {
                        hideSoftKeyboard(getActivity());
                        new FormAcceptAsyncTask().execute();
                    } else {
                        Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
                    }


                break;
        }

    }


    private void getDataFromBundle() {
        Bundle b = this.getArguments();
        assert b != null;

        eid = b.getString("eid");
        b_name = b.getString("b_name");
        qid = b.getString("qid");

        Log.e(TAG, "eid--" + eid);
        Log.e(TAG, "b_name--" + b_name);
        Log.e(TAG, "qid--" + qid);


    }


    private boolean checkValidation() {

        Name = etName.getText().toString();
        email = etEmail.getText().toString();
        alternateNo = etEveningPhone.getText().toString();
        phone = etPhone.getText().toString();
        postcode = etPostcode.getText().toString();
        address = etaddress.getText().toString();

        if (Name.equals("")) {
            //    CommonMethod.showAlert("Enter Name", getActivity());
            final PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog
                    .setTitle("QuotesIn")
                    .setMessage("Please input Name")
                    .setIcon(R.mipmap.quotes_logo)
                    .addButton(
                            "OK",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.splash_color,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                }
                            }
                    )
                    .setAnimationEnabled(true)
                    .show();

            return false;
        } else if (email.equals("")) {
            //CommonMethod.showAlert("Enter Email", getActivity());
            final PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog
                    .setTitle("QuotesIn")
                    .setMessage("Enter Email")
                    .setIcon(R.mipmap.quotes_logo)
                    .addButton(
                            "OK",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.splash_color,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                }
                            }
                    )
                    .setAnimationEnabled(true)
                    .show();
            return false;
        } else if (!etEmail.getText().toString().equals("")) {
            if (!CommonMethod.isValidEmaillId(etEmail.getText().toString().trim())) {
                final PrettyDialog pDialog = new PrettyDialog(getActivity());
                pDialog
                        .setTitle("QuotesIn")
                        .setMessage("Enter valid Email")
                        .setIcon(R.mipmap.quotes_logo)
                        .addButton(
                                "OK",                    // button text
                                R.color.pdlg_color_white,        // button text color
                                R.color.splash_color,        // button background color
                                new PrettyDialogCallback() {        // button OnClick listener
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                    }
                                }
                        )
                        .setAnimationEnabled(true)
                        .show();

                return false;
            }
            return true;
        } else if (phone.equals("")) {
            // CommonMethod.showAlert("Enter Phone Number", getActivity());
            final PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog
                    .setTitle("QuotesIn")
                    .setMessage("Enter Phone Number")
                    .setIcon(R.mipmap.quotes_logo)
                    .addButton(
                            "OK",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.splash_color,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                }
                            }
                    )
                    .setAnimationEnabled(true)
                    .show();
            return false;
        } else if (postcode.equals("")) {
            // CommonMethod.showAlert("Enter Postcode", getActivity());
            final PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog
                    .setTitle("QuotesIn")
                    .setMessage("Enter Postcode")
                    .setIcon(R.mipmap.quotes_logo)
                    .addButton(
                            "OK",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.splash_color,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                }
                            }
                    )
                    .setAnimationEnabled(true)
                    .show();
            return false;
        } else if (address.equals("")) {
            // CommonMethod.showAlert("Enter Address", getActivity());

            final PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog
                    .setTitle("QuotesIn")
                    .setMessage("Enter Address")
                    .setIcon(R.mipmap.quotes_logo)
                    .addButton(
                            "OK",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.splash_color,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                }
                            }
                    )
                    .setAnimationEnabled(true)
                    .show();
            return false;
        }

        return true;
    }

    public class FormAcceptAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            result = callService();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "get states api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();

                        //    finishCurrentFragment();

                        Intent intent = new Intent(getActivity(), HomeScreen.class);
                        startActivity(intent);


                        JSONArray jsonArray = jObject.getJSONArray("result");


                    } else if (status.equals("failed")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();
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
            String url = APIUrl.Accept_Quote_contact;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "Indox after connection url: " + url);

                client.addFormPart("quote_id", AppData.getInstance().getQid());
                client.addFormPart("quote_status", "1");
                client.addFormPart("quote_amount", AppData.getInstance().getQamt());
                client.addFormPart("business_username", AppData.getInstance().getBname());
                client.addFormPart("consumer_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username());

                client.addFormPart("name", Name);
                client.addFormPart("address", address);
                client.addFormPart("postal_code", postcode);
                client.addFormPart("email", email);
                client.addFormPart("phone", phone);
                client.addFormPart("alter_phone", alternateNo);
                client.addFormPart("username", AppData.getInstance().getBname());
                client.addFormPart("enq_id", eid);
                client.addFormPart("role_id", LoginPreferences.getActiveInstance(getActivity()).getRole_id());


                Log.e(TAG, "quote_id" + AppData.getInstance().getQid());
                Log.e(TAG, "quote_amount" + AppData.getInstance().getQamt());
                Log.e(TAG, "business_username" + AppData.getInstance().getBname());
                Log.e(TAG, "consumer_username" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());

                Log.e(TAG, "name: " + Name);
                Log.e(TAG, "address: " + address);
                Log.e(TAG, "postcode: " + postcode);
                Log.e(TAG, "email: " + email);
                Log.e(TAG, "phone: " + phone);
                Log.e(TAG, "alter_phone: " + alternateNo);
                Log.e(TAG, "username: " + AppData.getInstance().getBname());
                Log.e(TAG, "enq_id: " + eid);
                Log.e(TAG, "role_id: " + LoginPreferences.getActiveInstance(getActivity()).getRole_id());


                client.finishMultipart();

                result = client.getResponse();
                Log.e(TAG, "Accept_Quote_contact response :" + result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }

    private void finishCurrentFragment() {
        int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount >= 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

}
