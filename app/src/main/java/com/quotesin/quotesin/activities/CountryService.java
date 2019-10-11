package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.CityAdapter;
import com.quotesin.quotesin.adapter.CountryAdapter;
import com.quotesin.quotesin.adapter.ServicesAdapter;
import com.quotesin.quotesin.adapter.StateAdapter;
import com.quotesin.quotesin.model.CitiesModel;
import com.quotesin.quotesin.model.CountriesModel;
import com.quotesin.quotesin.model.ServicesModel;
import com.quotesin.quotesin.model.StatesModel;
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

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

public class CountryService extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();
    FrameLayout flCountry, flState, flCity, flServices;
    private RecyclerView recyclerView;
    Spinner spinner1;
    Button btn_submit;

    String name, userName, email, phone, pass;
    String user_name, user_phone, id;
    String user_profile_pic, user_email;


    TextView tvCountry, tvService, tvCity, tvState;

    ArrayList<CountriesModel> countriesArrayList = new ArrayList<CountriesModel>();
    ArrayList<StatesModel> statesModelArrayList = new ArrayList<StatesModel>();
    ArrayList<CitiesModel> citiesModelArrayList = new ArrayList<CitiesModel>();


    CountryAdapter countryAdapter;
    StateAdapter stateAdapter;
    CityAdapter cityAdapter;

    ArrayAdapter<String> adapterSpCategory;
    ServicesAdapter servicesAdapter;

    ArrayList<spinner_model_category> categoryArrayList = new ArrayList<spinner_model_category>();
    ArrayList<ServicesModel> servicesModelArrayList = new ArrayList<ServicesModel>();

    private String selectedIds, StateSelectedIds, citySelectedIds, selectedServicesIds;
    String selectedCategoriesId;
    String location_code, country_code, service_id, category_id, role_id, state_code;

    public static CheckBox chkAll, chkAllState, chkAllCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_service);
        gettingCountry();
        gettingCategory();
        initViews();
        registerClickListener();


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner1.setSelection(position);
                if (categoryArrayList.size() > 0) {
                    try {
                        selectedCategoriesId = categoryArrayList.get(position - 1).getId();

                        Log.e(TAG, "item getId : " + categoryArrayList.get(position - 1).getId());
                        Log.e(TAG, "item getId-- : " + selectedCategoriesId);
                        tvService.setText("Choose Service");
                        selectedServicesIds = "";

                        getServicesList();

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
        flCountry = findViewById(R.id.flCountry);
        flState = findViewById(R.id.flState);
        flCity = findViewById(R.id.flCity);
        flServices = findViewById(R.id.flServices);

        tvCountry = findViewById(R.id.tvCountry);
        tvState = findViewById(R.id.tvState);
        tvCity = findViewById(R.id.tvCity);
        tvService = findViewById(R.id.tvService);

        btn_submit = findViewById(R.id.btn_submit);

        spinner1 = findViewById(R.id.spinner1);
        adapterSpCategory = new ArrayAdapter<String>(CountryService.this, R.layout.spinner_layout_black) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

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
        flCountry.setOnClickListener(this);
        flState.setOnClickListener(this);
        flCity.setOnClickListener(this);
        flServices.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flCountry:
                AlertDialog.Builder builder = new AlertDialog.Builder(CountryService.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_recyclerview, null);

                chkAll = dialogLayout.findViewById(R.id.chkAll);
                chkAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chkAll.isChecked()) {
                            countryAdapter.selectAll();
                        } else {
                            countryAdapter.unselectall();

                        }
                    }
                });

                recyclerView = dialogLayout.findViewById(R.id.recyclerview);
                setCountryAdapter(countriesArrayList);
                builder.setView(dialogLayout);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getList();
                        gettingState();
                        tvState.setText("Choose State");
                        tvCity.setText("Choose City");
                        StateSelectedIds = "";
                        citySelectedIds = "";
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


                break;

            case R.id.flState:
                if (selectedIds != null && !tvCountry.getText().toString().equalsIgnoreCase("Choose Country")) {
                    AlertDialog.Builder builderState = new AlertDialog.Builder(CountryService.this);
                    LayoutInflater inflater2 = getLayoutInflater();
                    View dialogLayout2 = inflater2.inflate(R.layout.dialog_recyclerview, null);

                    chkAllState = dialogLayout2.findViewById(R.id.chkAll);

                    chkAllState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (chkAllState.isChecked()) {
                                stateAdapter.selectAll();
                            } else {
                                stateAdapter.unselectall();
                            }
                        }
                    });


                    recyclerView = dialogLayout2.findViewById(R.id.recyclerview);
                    setStateAdapter(statesModelArrayList);
                    builderState.setView(dialogLayout2);

                    builderState.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getListState();
                            gettingcities();
                            tvCity.setText("Choose City");
                            citySelectedIds = "";
                        }
                    });

                    builderState.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    builderState.show();
                } else {
                    CommonMethod.showAlert("Please Select Country", CountryService.this);
                }
                break;

            case R.id.flCity:
                if (StateSelectedIds != null && !tvState.getText().toString().equalsIgnoreCase("Choose State")) {
                    String stcity = String.valueOf(statesModelArrayList.size());
                    Log.e(TAG, stcity);
                    if (!stcity.equals("0")) {
                        AlertDialog.Builder builderCity = new AlertDialog.Builder(CountryService.this);
                        LayoutInflater inflater3 = getLayoutInflater();
                        View dialogLayout3 = inflater3.inflate(R.layout.dialog_recyclerview, null);

                        chkAllCity = dialogLayout3.findViewById(R.id.chkAll);

                        chkAllCity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (chkAllCity.isChecked()) {
                                    cityAdapter.selectAll();
                                } else {
                                    cityAdapter.unselectall();
                                }
                            }
                        });


                        recyclerView = dialogLayout3.findViewById(R.id.recyclerview);
                        setCityAdapter(citiesModelArrayList);
                        builderCity.setView(dialogLayout3);

                        builderCity.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getListCities();

                            }
                        });

                        builderCity.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                        builderCity.show();
                    }
                } else {
                    CommonMethod.showAlert("Please Select State", CountryService.this);

                }

                break;

            case R.id.flServices:
                String flSe = String.valueOf(categoryArrayList.size());
                Log.e(TAG, "categoryArrayList==" + flSe);
                if (!flSe.equals("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(CountryService.this);
                    LayoutInflater inflater1 = getLayoutInflater();
                    View dialogLayout1 = inflater1.inflate(R.layout.dialog_recyclerview, null);
                    LinearLayout Linear = dialogLayout1.findViewById(R.id.L1);
                    Linear.setVisibility(View.GONE);
                    recyclerView = dialogLayout1.findViewById(R.id.recyclerview);
                    setServicesAdapter(servicesModelArrayList);
                    builder1.setView(dialogLayout1);

                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getListServices();

                        }
                    });

                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

                    builder1.show();
                } else {
                    CommonMethod.showAlert("Please Select Category", CountryService.this);

                }

                break;

            case R.id.btn_submit:
                if (checkValid())
                    if (CommonMethod.isNetworkAvailable(CountryService.this)) {
                        CommonMethod.hideSoftKeyboard(CountryService.this);
                        new SignupAsyncTask().execute();
                    } else {
                        Toast.makeText(CountryService.this, "Check the internet connection.", Toast.LENGTH_SHORT).show();
                    }
                break;
        }
    }

    private boolean checkValid() {
        if (selectedIds == null) {
            CommonMethod.showAlert("Please Choose Country", CountryService.this);
            return false;
        } else if (citySelectedIds == null) {
            CommonMethod.showAlert("Please Choose city", CountryService.this);
            return false;
        } else if (StateSelectedIds == null) {
            CommonMethod.showAlert("Please Choose State", CountryService.this);
            return false;
        } else if (selectedCategoriesId == null) {
            CommonMethod.showAlert(" Please Choose Category", CountryService.this);
            return false;
        } else if (spinner1.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
            CommonMethod.showAlert(" Please Choose Category", CountryService.this);
            return false;
        } else if (selectedServicesIds == null) {
            CommonMethod.showAlert("Please Choose Services", CountryService.this);
            return false;
        } else if (tvCountry.getText().toString().equalsIgnoreCase("Choose Country")) {
            CommonMethod.showAlert("Please Select Country", CountryService.this);
            return false;
        } else if (tvState.getText().toString().equalsIgnoreCase("Choose State")) {
            CommonMethod.showAlert("Please Select State", CountryService.this);
            return false;
        } else if (tvCity.getText().toString().equalsIgnoreCase("Choose City")) {
            CommonMethod.showAlert("Please Select City", CountryService.this);
            return false;
        } else if (tvService.getText().toString().equalsIgnoreCase("Choose Service")) {
            CommonMethod.showAlert("Please Select Service", CountryService.this);
            return false;
        }
        return true;
    }

    private void gettingCountry() {
        if (CommonMethod.isNetworkAvailable(CountryService.this)) {
            hideSoftKeyboard(CountryService.this);
            new GetCountriesAsyncTask().execute();
        } else {
            Toast.makeText(CountryService.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingState() {
        if (CommonMethod.isNetworkAvailable(CountryService.this)) {
            hideSoftKeyboard(CountryService.this);
            new GetStateAsyncTask().execute();
        } else {
            Toast.makeText(CountryService.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingcities() {
        if (CommonMethod.isNetworkAvailable(CountryService.this)) {
            hideSoftKeyboard(CountryService.this);
            new GetCityAsyncTask().execute();
        } else {
            Toast.makeText(CountryService.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingCategory() {
        if (CommonMethod.isNetworkAvailable(CountryService.this)) {
            hideSoftKeyboard(CountryService.this);

            new GetCategoryAsyncTask().execute();
        } else {
            Toast.makeText(CountryService.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getServicesList() {
        if (CommonMethod.isNetworkAvailable(CountryService.this)) {
            hideSoftKeyboard(CountryService.this);
            new GetServicesAsyncTask().execute();
        } else {
            Toast.makeText(CountryService.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }


    private String getList() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (CountriesModel number : countriesArrayList) {
            if (number.isSelected()) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append(",");
                stringBuilder1.append(",");
                stringBuilder.append(number.getId());
                stringBuilder1.append(number.getCountry_name());

            }
        }
        selectedIds = stringBuilder.toString();
        String countryname = stringBuilder1.toString();
        if (!countryname.equals("")) {
            String serviceName1 = countryname.substring(1);

            if (chkAll.isChecked()) {
                tvCountry.setText("All Selected");
            } else {
                tvCountry.setText(serviceName1);
            }


            Log.e("selectedIds---", selectedIds);
            Log.e("tvServicesCho---", countryname);
        } else {
            tvCountry.setText("Choose Country");
            Log.e("selectedIds---", selectedIds);
            Log.e("tvServicesCho---", countryname);
        }
        return selectedIds;
    }

    private String getListState() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (StatesModel number : statesModelArrayList) {
            if (number.isSelected()) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append(",");
                stringBuilder1.append(",");

                stringBuilder.append(number.getState_id());
                stringBuilder1.append(number.getState_name());
            }
        }
        StateSelectedIds = stringBuilder.toString();
        String statename = stringBuilder1.toString();
        if (!statename.equals("")) {
            String stateName1 = statename.substring(1);

            if (chkAllState.isChecked()) {
                tvState.setText("All Selected");
            } else {
                tvState.setText(stateName1);
            }


            Log.e("StateSelectedIds---", StateSelectedIds);
            Log.e("tvServicesCho---", stateName1);
        } else {
            tvState.setText("Choose State");
            Log.e("StateSelectedIds---", StateSelectedIds);
            Log.e("tvServicesCho---", statename);
        }

        return StateSelectedIds;
    }

    private String getListCities() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (CitiesModel number : citiesModelArrayList) {
            if (number.isSelected()) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append(",");
                stringBuilder1.append(",");

                stringBuilder.append(number.getCity_id());
                stringBuilder1.append(number.getCity_name());
            }
        }
        citySelectedIds = stringBuilder.toString();
        String statename = stringBuilder1.toString();
        if (!statename.equals("")) {
            String stateName1 = statename.substring(1);

            if (chkAllCity.isChecked()) {
                tvCity.setText("All Selected");
            } else {
                tvCity.setText(stateName1);
            }


            Log.e("citySelectedIds---", citySelectedIds);
            Log.e("tvCity---", stateName1);
        } else {
            tvCity.setText("Choose City");

        }

        return citySelectedIds;
    }

    private String getListServices() {
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
        selectedServicesIds = stringBuilder.toString();
        String service = stringBuilder1.toString();
        if (!service.equals("")) {
            String serviceName1 = service.substring(1);
            tvService.setText(serviceName1);
            Log.e("selectedServicesIds---", selectedServicesIds);
            Log.e("tvService---", serviceName1);
        } else {
            tvService.setText("Choose Service");

        }

        return selectedServicesIds;
    }


    @SuppressLint("StaticFieldLeak")
    public class GetCountriesAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(CountryService.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();

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
                Toast.makeText(CountryService.this, "Server Error Please try Again", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");
                        countriesArrayList = new ArrayList<CountriesModel>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);

                            CountriesModel model = new CountriesModel();

                            model.setId(post.getString("id"));
                            model.setCountry_name(post.getString("name"));

                            countriesArrayList.add(model);
                        }

                        Log.e(TAG, " countriesArrayList size: " + countriesArrayList.size() + "");

                    } else if (status.equals("failed")) {
                        Toast.makeText(CountryService.this, responseMessage, Toast.LENGTH_LONG).show();

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }
    }

    private void setCountryAdapter(ArrayList<CountriesModel> countriesArrayList) {
        Log.e(TAG, "countriesArrayList --> " + countriesArrayList.size());
        countryAdapter = new CountryAdapter(this, countriesArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(CountryService.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(countryAdapter);

    }

    public class GetStateAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(CountryService.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url1 = APIUrl.get_all_state + "?country_id=" + selectedIds;
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
                Toast.makeText(CountryService.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");
                        if (jsonArray != null) {

                            statesModelArrayList = new ArrayList<StatesModel>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);

                                StatesModel model = new StatesModel();

                                model.setState_id(post.getString("id"));
                                model.setState_name(post.getString("name"));

                                statesModelArrayList.add(model);
                            }
                            Log.e(TAG, " statesModelArrayList size: " + statesModelArrayList.size() + "");
                        }

                    } else if (status.equals("Failed")) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }

    }

    private void setStateAdapter(ArrayList<StatesModel> statesModelArrayList) {
        Log.e(TAG, "statesModelArrayList --> " + statesModelArrayList.size());
        stateAdapter = new StateAdapter(this, statesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(CountryService.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(stateAdapter);

    }


    public class GetCityAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String response;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(CountryService.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url1 = APIUrl.get_all_city + "?country_id=" + selectedIds + "&" + "state_id=" + StateSelectedIds;
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
                Toast.makeText(CountryService.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");
                        if (jsonArray != null) {

                            citiesModelArrayList = new ArrayList<CitiesModel>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);

                                CitiesModel model = new CitiesModel();

                                model.setCity_id(post.getString("id"));
                                model.setCity_name(post.getString("name"));

                                citiesModelArrayList.add(model);
                            }

                            Log.e(TAG, " citiesModelArrayList size: " + statesModelArrayList.size() + "");

                        }

                    } else if (status.equals("Failed")) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }

    }

    private void setCityAdapter(ArrayList<CitiesModel> citiesModelArrayList) {
        Log.e(TAG, "citiesModelArrayList --> " + citiesModelArrayList.size());
        cityAdapter = new CityAdapter(this, citiesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(CountryService.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cityAdapter);

    }


    @SuppressLint("StaticFieldLeak")
    public class GetCategoryAsyncTask extends AsyncTask<String, Void, String> {


        JSONObject jObject;
        private String result;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            Log.e(TAG, "get countries api response is " + result);
            if (result == null) {
                Toast.makeText(CountryService.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");

                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jObject.getJSONArray("result");

                        if (jsonArray != null) {
                            categoryArrayList = new ArrayList<spinner_model_category>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);

                                spinner_model_category model = new spinner_model_category();

                                model.setId(post.getString("id"));
                                model.setTitle(post.getString("category_name"));

                                categoryArrayList.add(model);
                            }
                            Log.e(TAG, " Countries size: " + categoryArrayList.size() + "");
                            setUpCountryAdapter();
                        }

                    } else if (status.equals("Failed")) {
                        setUpCountryAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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


    private void setUpCountryAdapter() {
        Log.e(TAG, " Countries size: " + categoryArrayList.size() + "");
        ArrayList<String> countries = new ArrayList<>();
        countries.clear();
        countries.add("Select Category");

        for (spinner_model_category model1 : categoryArrayList) {
            countries.add(model1.getTitle());
            adapterSpCategory.add(model1.getTitle());
        }
        spinner1.setAdapter(new ArrayAdapter<String>(CountryService.this, R.layout.spinner_layout_black, countries));
    }


    public class GetServicesAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String result;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(CountryService.this, "Connecting", true);
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

            Log.e(TAG, "get states api response is " + result);
            if (result == null) {
                Toast.makeText(CountryService.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
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

    private void setServicesAdapter(ArrayList<ServicesModel> servicesModelArrayList) {

        Log.e(TAG, "servicesModelArrayList --> " + servicesModelArrayList.size());
        servicesAdapter = new ServicesAdapter(this, servicesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(CountryService.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(servicesAdapter);
    }

    public class SignupAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(CountryService.this, "Connecting", true);
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
                        name = jsonObjectData.getString("name");


                        LoginPreferences.getActiveInstance(CountryService.this).setId(id);
                        LoginPreferences.getActiveInstance(CountryService.this).setUser_first_name(name);
                        LoginPreferences.getActiveInstance(CountryService.this).setUser_username(user_name);
                        LoginPreferences.getActiveInstance(CountryService.this).setLocationId(location_code);
                        LoginPreferences.getActiveInstance(CountryService.this).setCategory_id(category_id);
                        LoginPreferences.getActiveInstance(CountryService.this).setService_id(service_id);
                        LoginPreferences.getActiveInstance(CountryService.this).setCountry_code(country_code);
                        LoginPreferences.getActiveInstance(CountryService.this).setState_id(state_code);
                        LoginPreferences.getActiveInstance(CountryService.this).setRole_id(role_id);

                        LoginPreferences.getActiveInstance(CountryService.this).setRole_id("5");
                        LoginPreferences.getActiveInstance(CountryService.this).setProfileScreen("3");

                        Log.e(TAG, "id :" + LoginPreferences.getActiveInstance(CountryService.this).getId());
                        Log.e(TAG, "first_name :" + LoginPreferences.getActiveInstance(CountryService.this).getUser_first_name());
                        Log.e(TAG, "email :" + LoginPreferences.getActiveInstance(CountryService.this).getUser_username());
                        Log.e(TAG, "profile_pic :" + LoginPreferences.getActiveInstance(CountryService.this).getUser_profile_pic());
                        Log.e(TAG, "isLogin :" + LoginPreferences.getActiveInstance(CountryService.this).getIsLoggedIn());
                        Log.e(TAG, "role_id :" + LoginPreferences.getActiveInstance(CountryService.this).getRole_id());

                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();


                        if (!TextUtils.isEmpty(LoginPreferences.getActiveInstance(CountryService.this).getSocialLoginType()))
                            if (LoginPreferences.getActiveInstance(CountryService.this).getSocialLoginType().equals("yes")) {
                                LoginPreferences.getActiveInstance(CountryService.this).setIsLoggedIn(true);

/*
                                final Dialog dialog = new Dialog(CountryService.this);
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
                                        Intent i = new Intent(CountryService.this, HomeScreen.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();*/

                                final PrettyDialog pDialog = new PrettyDialog(CountryService.this);
                                pDialog
                                        .setTitle("Congratulations! \n Registered Successfully")
                                        .setMessage("You have been successfully registered.")
                                        .setIcon(R.mipmap.quotes_logo)
                                        .addButton(
                                                "OK",                    // button text
                                                R.color.pdlg_color_white,        // button text color
                                                R.color.splash_color,        // button background color
                                                new PrettyDialogCallback() {        // button OnClick listener
                                                    @Override
                                                    public void onClick() {
                                                        Intent i = new Intent(CountryService.this, HomeScreen.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                                        startActivity(i);
                                                        finish();
                                                        pDialog.dismiss();
                                                    }
                                                }
                                        )
                                        .setAnimationEnabled(true)
                                        .show();


                            } else {
                                /*LoginPreferences.getActiveInstance(CountryService.this).setIsLoggedIn(false);
                                final Dialog dialog = new Dialog(CountryService.this);
                                dialog.setContentView(R.layout.customdialog);
                                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                                TextView dtv = dialog.findViewById(R.id.text);
                                TextView dtv1 = dialog.findViewById(R.id.text1);
                                TextView dtv2 = dialog.findViewById(R.id.text2);
                                dtv.setText("Congratulations!!");
                                dtv1.setText("You have been successfully registered.");
                                dtv2.setText("An email has been sent to your registered email for verification.Please verify email.");

                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(CountryService.this, Login.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();*/


                                LoginPreferences.getActiveInstance(CountryService.this).setIsLoggedIn(false);
                                final PrettyDialog pDialog = new PrettyDialog(CountryService.this);
                                pDialog
                                        .setTitle("Congratulations! \n Registered Successfully")
                                        .setMessage("An email has been sent to your \n registered email for verification.")
                                        .setIcon(R.mipmap.quotes_logo)
                                        .addButton(
                                                "OK",                    // button text
                                                R.color.pdlg_color_white,        // button text color
                                                R.color.splash_color,        // button background color
                                                new PrettyDialogCallback() {        // button OnClick listener
                                                    @Override
                                                    public void onClick() {
                                                        Intent i = new Intent(CountryService.this, Login.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                                                        startActivity(i);
                                                        finish();
                                                        pDialog.dismiss();
                                                    }
                                                }
                                        )
                                        .setAnimationEnabled(true)
                                        .show();
                            }


                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (status.equals("failed")) {
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

                Log.e(TAG, "user_id" + LoginPreferences.getActiveInstance(CountryService.this).getId());
                Log.e(TAG, "role_id" + LoginPreferences.getActiveInstance(CountryService.this).getRole_id());
                Log.e(TAG, "category_id" + selectedCategoriesId);
                Log.e(TAG, "service_id" + selectedServicesIds);
                Log.e(TAG, "country_code" + selectedIds);
                Log.e(TAG, "state_code" + StateSelectedIds);
                Log.e(TAG, "location_code" + citySelectedIds);

                client.addFormPart("user_id", LoginPreferences.getActiveInstance(CountryService.this).getId());
                client.addFormPart("role_id", LoginPreferences.getActiveInstance(CountryService.this).getRole_id());
                client.addFormPart("category_id", selectedCategoriesId);
                client.addFormPart("service_id", selectedServicesIds);
                client.addFormPart("country_code", selectedIds);
                client.addFormPart("state_code", StateSelectedIds);
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
