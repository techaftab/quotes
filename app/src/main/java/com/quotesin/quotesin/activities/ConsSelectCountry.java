package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.model.CitiesModel;
import com.quotesin.quotesin.model.CountriesModel;
import com.quotesin.quotesin.model.StatesModel;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;

import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

public class ConsSelectCountry extends AppCompatActivity implements View.OnClickListener {

    Spinner spCountry, spCity, spState;

    ArrayList<CountriesModel> countryArrayList = new ArrayList<>();
    ArrayList<StatesModel> statesModelArrayList = new ArrayList<>();
    ArrayList<CitiesModel> citiesModelArrayList = new ArrayList<>();

    ArrayAdapter<String> adapterSpCountry;
    ArrayAdapter<String> adapterSpState;
    ArrayAdapter<String> adapterSpCity;

    FrameLayout flCity;

    Button btn_submit;
    String TAG = "ConsSelectCountry";
    private String selectedCountryId, selectedStateId, citySelectedIds;
    String selectCounTitle, selectStateTitle, selectCityTitle;

    TextView tvState, tvCity;

    String location_code, country_code, service_id, category_id, role_id, state_code;

    String user_name, user_phone, id;
    String user_profile_pic, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._con_select_country);

        initViews();
        registerClickListener();
        gettingCountry();

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spCountry.setSelection(position);

                if (countryArrayList.size() > 0) {
                    try {
                        selectedCountryId = String.valueOf(countryArrayList.get(position - 1).getId());
                        selectCounTitle = String.valueOf(countryArrayList.get(position - 1).getClass());


                        tvState.setVisibility(View.GONE);
                        selectedStateId = "";
                        spCity.setAdapter(null);
                        gettingState(selectedCountryId);
                        tvCity.setVisibility(View.VISIBLE);

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spState.setSelection(position);

                if (statesModelArrayList.size() > 0) {
                    try {
                        selectedStateId = String.valueOf(statesModelArrayList.get(position - 1).getState_id());
                        selectStateTitle = String.valueOf(statesModelArrayList.get(position - 1).getClass());


                        Log.e(TAG, "item state : " + statesModelArrayList.get(position - 1).getState_name());
                        Log.e(TAG, "item getstateId : " + statesModelArrayList.get(position - 1).getState_id());

                        tvCity.setVisibility(View.GONE);

                        citySelectedIds = "";
                        gettingcities();

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spCity.setSelection(position);

                if (citiesModelArrayList.size() > 0) {
                    try {
                        citySelectedIds = String.valueOf(citiesModelArrayList.get(position - 1).getCity_id());
                        selectCityTitle = String.valueOf(citiesModelArrayList.get(position - 1).getCity_name());

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void initViews() {
        spCity = findViewById(R.id.spCity);
        spCountry = findViewById(R.id.spCountry);
        spState = findViewById(R.id.spState);
        btn_submit = findViewById(R.id.btn_submit);
        tvState = findViewById(R.id.tvState);
        tvCity = findViewById(R.id.tvCity);
        flCity = findViewById(R.id.flCity);


        adapterSpCountry = new ArrayAdapter<String>(ConsSelectCountry.this, R.layout.spinner_layout_black) {
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


        adapterSpState = new ArrayAdapter<String>(ConsSelectCountry.this, R.layout.spinner_layout_black) {
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
        adapterSpCity = new ArrayAdapter<String>(ConsSelectCountry.this, R.layout.spinner_layout_black) {
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

    private void gettingCountry() {
        if (CommonMethod.isNetworkAvailable(ConsSelectCountry.this)) {
            hideSoftKeyboard(ConsSelectCountry.this);
            new GetCountriesAsyncTask().execute();
        } else {
            Toast.makeText(ConsSelectCountry.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingState(String selectedCountryId) {
        if (CommonMethod.isNetworkAvailable(ConsSelectCountry.this)) {
            hideSoftKeyboard(ConsSelectCountry.this);
            new GetStateAsyncTask(selectedCountryId).execute();
        } else {
            Toast.makeText(ConsSelectCountry.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }


    private void gettingcities() {
        if (CommonMethod.isNetworkAvailable(ConsSelectCountry.this)) {
            hideSoftKeyboard(ConsSelectCountry.this);
            new GetCityAsyncTask().execute();
        } else {
            Toast.makeText(ConsSelectCountry.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerClickListener() {
        btn_submit.setOnClickListener(this);
        tvState.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvState:
                if (TextUtils.isEmpty(selectedCountryId))
                    CommonMethod.showAlert("Please select Country First", ConsSelectCountry.this);

                break;
            case R.id.tvCity:
                if (TextUtils.isEmpty(selectedCountryId))
                    CommonMethod.showAlert("Please select Country First", ConsSelectCountry.this);
                else if (TextUtils.isEmpty(selectedStateId))
                    CommonMethod.showAlert("Please select State First", ConsSelectCountry.this);
                break;

            case R.id.btn_submit:
                if (checkValidation())
                    if (CommonMethod.isNetworkAvailable(ConsSelectCountry.this)) {
                        CommonMethod.hideSoftKeyboard(ConsSelectCountry.this);
                        new SignupAsyncTask().execute();
                    } else {
                        Toast.makeText(ConsSelectCountry.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                    }
                break;

        }
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(selectedCountryId)) {
            CommonMethod.showAlert("Please select Country", ConsSelectCountry.this);
            return false;

        } else if (TextUtils.isEmpty(selectedStateId)) {
            CommonMethod.showAlert("Please select State ", ConsSelectCountry.this);
            return false;
        } else if (TextUtils.isEmpty(citySelectedIds)) {
            CommonMethod.showAlert("Please select City", ConsSelectCountry.this);
            return false;
        }
        return true;

    }


    @SuppressLint("StaticFieldLeak")
    public class GetCountriesAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
         String status = "";
         String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(ConsSelectCountry.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
              //  ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
                result = CustomHttpClient.executeHttpGet(APIUrl.get_all_countries);
                System.out.print(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("Contry Service", "get states api response is " + result);
            if (result == null) {
                Toast.makeText(ConsSelectCountry.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");
                        countryArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);

                            CountriesModel model = new CountriesModel();

                            model.setId(post.getString("id"));
                            model.setCountry_name(post.getString("name"));

                            countryArrayList.add(model);

                        }

                        Log.e(TAG, " countriesArrayList size: " + countryArrayList.size() + "");

                        if (statesModelArrayList.size() > 0 && citiesModelArrayList.size() > 0) {
                            statesModelArrayList.clear();
                            citiesModelArrayList.clear();
                        }

                        setUpCountryAdapter();

                    } else if (status.equals("failed")) {
                        setUpCountryAdapter();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }
    }

    private void setUpCountryAdapter() {

        Log.e(TAG, " countries size: " + countryArrayList.size() + "");

        ArrayList<String> countries = new ArrayList<>();
        countries.clear();

        countries.add("Select Country");

        for (CountriesModel model1 : countryArrayList) {
            countries.add(model1.getCountry_name());
            adapterSpCountry.add(model1.getCountry_name());
        }

        spCountry.setAdapter(new ArrayAdapter<>(ConsSelectCountry.this, R.layout.spinner_layout_black, countries));
    }


    @SuppressLint("StaticFieldLeak")
    public class GetStateAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
         String status = "";
         String responseMessage = "";

        String CountryId;

        GetStateAsyncTask(String selectedCountryId) {
            this.CountryId = selectedCountryId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(ConsSelectCountry.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url1 = APIUrl.get_all_state + "?country_id=" + CountryId;
                response = CustomHttpClient.executeHttpGet(url1);
                System.out.print(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("State Service", "get states api response is " + result);
            if (result == null) {
                Toast.makeText(ConsSelectCountry.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");

                        statesModelArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);

                            StatesModel model = new StatesModel();

                            model.setState_id(post.getString("id"));
                            model.setState_name(post.getString("name"));

                            statesModelArrayList.add(model);
                        }

                        Log.e(TAG, " statesModelArrayList size: " + statesModelArrayList.size() + "");
                        setUpStateAdapter();

                    } else if (status.equals("failed")) {
                        setUpStateAdapter();

                    } else {
                        setUpStateAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }
    }

    private void setUpStateAdapter() {

        Log.e(TAG, " state size: " + statesModelArrayList.size() + "");
        ArrayList<String> states = new ArrayList<>();
        states.clear();
        states.add("Select State");
        for (StatesModel model1 : statesModelArrayList) {
            states.add(model1.getState_name());
            adapterSpCountry.add(model1.getState_name());
        }

        spState.setAdapter(new ArrayAdapter<>(ConsSelectCountry.this, R.layout.spinner_layout_black, states));
    }


    @SuppressLint("StaticFieldLeak")
    public class GetCityAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
        String status = "";
        String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(ConsSelectCountry.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url1 = APIUrl.get_all_city + "?country_id=" + selectedCountryId + "&" + "state_id=" + selectedStateId;
                response = CustomHttpClient.executeHttpGet(url1);
                System.out.print(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("City Service", "get City api response is " + result);
            if (result == null) {
                Toast.makeText(ConsSelectCountry.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");

                        citiesModelArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);
                            CitiesModel model = new CitiesModel();
                            model.setCity_id(post.getString("id"));
                            model.setCity_name(post.getString("name"));

                            citiesModelArrayList.add(model);
                        }

                        Log.e(TAG, " citiesModelArrayList size: " + statesModelArrayList.size() + "");
                        setUpCityAdapter();

                    } else if (status.equals("failed")) {
                        setUpCityAdapter();

                    } else {
                        setUpStateAdapter();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressD.dismiss();
        }
    }

    private void setUpCityAdapter() {
        Log.e(TAG, " city size: " + citiesModelArrayList.size() + "");
        ArrayList<String> cities = new ArrayList<>();
        cities.clear();
        cities.add("Select City");
        for (CitiesModel model1 : citiesModelArrayList) {
            cities.add(model1.getCity_name());
            adapterSpCountry.add(model1.getCity_name());
        }
        spCity.setAdapter(new ArrayAdapter<>(ConsSelectCountry.this, R.layout.spinner_layout_black, cities));
    }


    @SuppressLint("StaticFieldLeak")
    public class SignupAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        String status = "";
        String responseMessage = "";
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(ConsSelectCountry.this, "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            response = callService();
            return response;
        }

        @SuppressLint("SetTextI18n")
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
                    status = jObject.getString("Status");

                    if (status.equalsIgnoreCase("success")) {
                        JSONObject jsonObjectData = jObject.getJSONObject("result");

                        Log.e(TAG, "jsonObjectData :" + jsonObjectData);
                        id = jsonObjectData.getString("id");
                        user_name = jsonObjectData.getString("business_username");
                        user_phone = jsonObjectData.getString("business_telephone");
                        user_profile_pic = jsonObjectData.getString("business_logo");
                        user_email = jsonObjectData.getString("business_email");

                        location_code = jsonObjectData.getString("location_code");
                        category_id = jsonObjectData.getString("category_id");
                        service_id = jsonObjectData.getString("service_id");
                        country_code = jsonObjectData.getString("country_code");
                        state_code = jsonObjectData.getString("state_code");
                        role_id = jsonObjectData.getString("role_id");

                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setId(id);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setUser_first_name(user_name);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setUser_username(user_email);

                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setLocationId(location_code);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setCategory_id(category_id);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setService_id(service_id);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setCountry_code(country_code);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setState_id(state_code);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setRole_id(role_id);
                        // LoginPreferences.getActiveInstance(ConsSelectCountry.this).setIsLoggedIn(false);
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setRole_id("2");
                        LoginPreferences.getActiveInstance(ConsSelectCountry.this).setProfileScreen("3");


                        Log.e(TAG, "id :" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getId());
                        Log.e(TAG, "first_name :" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getUser_first_name());
                        Log.e(TAG, "email :" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getUser_username());
                        Log.e(TAG, "profile_pic :" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getUser_profile_pic());
                        Log.e(TAG, "isLogin :" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getIsLoggedIn());
                        Log.e(TAG, "role_id :" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getRole_id());


                       /* final AlertDialog.Builder builder = new AlertDialog.Builder(ConsSelectCountry.this);
                        builder.setMessage("               Congratulations \n" +
                                "Welcome to Quotesin your free plan             has been activated").setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(ConsSelectCountry.this, HomeScreen.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                        positiveButton.gravity = Gravity.CENTER;

                                    }
                                });*/
                        if (!TextUtils.isEmpty(LoginPreferences.getActiveInstance(ConsSelectCountry.this).getSocialLoginType()))
                            if (LoginPreferences.getActiveInstance(ConsSelectCountry.this).getSocialLoginType().equals("yes")) {
                                LoginPreferences.getActiveInstance(ConsSelectCountry.this).setIsLoggedIn(true);
                             /*   final Dialog dialog = new Dialog(ConsSelectCountry.this);
                                dialog.setContentView(R.layout.customdialog);
                                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                                TextView dtv = dialog.findViewById(R.id.text);
                                TextView dtv1 = dialog.findViewById(R.id.text1);
                                TextView dtv2 = dialog.findViewById(R.id.text2);
                                dtv.setText("Congratulations!!");
                                dtv1.setText("You have been successfully registered.");
                                dtv2.setVisibility(View.GONE);

                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(ConsSelectCountry.this, HomeScreen.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();*/

                                final PrettyDialog pDialog = new PrettyDialog(ConsSelectCountry.this);
                                // button OnClick listener
                                pDialog
                                        .setTitle("Congratulations! \n Registered Successfully")
                                        .setMessage("You have been successfully registered.")
                                        .setIcon(R.mipmap.quotes_logo)
                                        .addButton(
                                                "OK",                    // button text
                                                R.color.pdlg_color_white,        // button text color
                                                R.color.splash_color,        // button background color
                                                () -> {
                                                    Intent i = new Intent(ConsSelectCountry.this, HomeScreen.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                                    startActivity(i);
                                                    finish();
                                                    pDialog.dismiss();
                                                }
                                        )
                                        .setAnimationEnabled(true)
                                        .show();


                            } else {
                                LoginPreferences.getActiveInstance(ConsSelectCountry.this).setIsLoggedIn(false);


                                final PrettyDialog pDialog = new PrettyDialog(ConsSelectCountry.this);
                                // button OnClick listener
                                pDialog.setTitle("Congratulations! \n Registered Successfully")
                                        .setMessage("An email has been sent to your \n registered email for verification.")
                                        .setIcon(R.mipmap.quotes_logo)
                                        .addButton(
                                                "OK",                    // button text
                                                R.color.pdlg_color_white,        // button text color
                                                R.color.splash_color,        // button background color
                                                () -> {
                                                    Intent i = new Intent(ConsSelectCountry.this, Login.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                                    startActivity(i);
                                                    finish();
                                                    pDialog.dismiss();
                                                }
                                        )
                                        .setAnimationEnabled(true)
                                        .show();

                            }


                    } else if (status.equals("Failed")) {
                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }

        private String callService() {
            String url = APIUrl.signup_second_step;

            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);

            try {
                client.connectForMultipart();

                Log.e("after connection", "" + url);
                Log.e(TAG, "user_id" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getId());
                Log.e(TAG, "role_id" + LoginPreferences.getActiveInstance(ConsSelectCountry.this).getRole_id());
                Log.e(TAG, "category_id" + "");
                Log.e(TAG, "service_id" + "");
                Log.e(TAG, "country_code" + selectedCountryId);
                Log.e(TAG, "state_code" + selectedStateId);
                Log.e(TAG, "location_code" + citySelectedIds);

                client.addFormPart("user_id", LoginPreferences.getActiveInstance(ConsSelectCountry.this).getId());
                client.addFormPart("role_id", LoginPreferences.getActiveInstance(ConsSelectCountry.this).getRole_id());
                client.addFormPart("category_id", "0");
                client.addFormPart("service_id", "0");
                client.addFormPart("country_code", selectedCountryId);
                client.addFormPart("state_code", selectedStateId);
                client.addFormPart("location_code", citySelectedIds);


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
    public void onBackPressed() {

    }

}
