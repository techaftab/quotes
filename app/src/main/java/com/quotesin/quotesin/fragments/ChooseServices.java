package com.quotesin.quotesin.fragments;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.CountryAdapter;
import com.quotesin.quotesin.adapter.ServicesAdapter;
import com.quotesin.quotesin.model.CountriesModel;
import com.quotesin.quotesin.model.ServicesModel;
import com.quotesin.quotesin.model.spinner_model_category;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.ProgressD;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;
import static io.fabric.sdk.android.Fabric.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseServices extends Fragment implements View.OnClickListener {
    View view;
    Button btn;

    Spinner spinner1;
    ImageView ivOpen;
    TextView tvCountry;

    ArrayAdapter<String> adapterSpCategory;
    ServicesAdapter servicesAdapter;

    CountryAdapter countryAdapter;

    ArrayList<spinner_model_category> categoryArrayList = new ArrayList<spinner_model_category>();
    ArrayList<ServicesModel> servicesModelArrayList = new ArrayList<ServicesModel>();

    ArrayList<CountriesModel> countriesArrayList = new ArrayList<CountriesModel>();

    String selectedCountryId;
    TextView text;
    RecyclerView recyclerView;
    private String selectedIds;

    FrameLayout flCountry;

    public ChooseServices() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose_services, container, false);

        btn = view.findViewById(R.id.ok);

        gettingCountry();
        gettingCategory();

        initViews();
        RegisterClickListener();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner1.setSelection(position);

                if (categoryArrayList.size() > 0) {
                    try {
                        selectedCountryId = categoryArrayList.get(position - 1).getId();

                        Log.e(TAG, "item getId : " + categoryArrayList.get(position - 1).getId());

                        Log.e(TAG, "item getId-- : " + selectedCountryId);

                        // setUpStateAdapter();
                        getStatesList();

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ivOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_recyclerview); //layout for dialog
                dialog.setTitle("Add a new friend");

                recyclerView = dialog.findViewById(R.id.recyclerview);

                setServicesAdapter(servicesModelArrayList);

                dialog.show();

            }

        });

        return view;
    }

    private void RegisterClickListener() {
        flCountry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.flCountry:
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_recyclerview); //layout for dialog
                dialog.setTitle("Add a new friend");

                recyclerView = dialog.findViewById(R.id.recyclerview);

                setCountryAdapter(countriesArrayList);

                dialog.show();
                break;
        }
    }


    private void gettingCountry() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            hideSoftKeyboard(getActivity());
            new GetCountriesAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        spinner1 = view.findViewById(R.id.spinner1);
        ivOpen = view.findViewById(R.id.ivOpen);
        flCountry = view.findViewById(R.id.flCountry);

        adapterSpCategory = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout) {

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

                resource = R.layout.spinner_layout;
                super.setDropDownViewResource(resource);
            }
        };

    }

    private String getList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ServicesModel number : servicesModelArrayList) {
            if (number.isSelected()) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append(",");
                stringBuilder.append(number.getId());
            }
        }
        selectedIds = stringBuilder.toString();
        Log.e("selectedIds---", selectedIds);
        return selectedIds;
    }


    private void getStatesList() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            hideSoftKeyboard(getActivity());
            servicesModelArrayList.clear();
            new GetServicesAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpStateAdapter() {
        Log.e(TAG, " States size: " + servicesModelArrayList.size() + "");
        ArrayList<String> states = new ArrayList<>();
        states.clear();
        states.add("Select State");

        for (ServicesModel model1 : servicesModelArrayList) {
            states.add(model1.getSer_name());
            //  adapterSpServices.add(model1.getSer_name());

            text.setText(model1.getSer_name());

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


    public class GetCountriesAsyncTask extends AsyncTask<String, Void, String> {
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

            Log.e(TAG, "get states api response is " + result);
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
                            countriesArrayList = new ArrayList<CountriesModel>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);

                                CountriesModel model = new CountriesModel();

                                model.setId(post.getString("id"));
                                model.setCountry_name(post.getString("service_name"));

                                countriesArrayList.add(model);
                            }

                            Log.e(TAG, " countriesArrayList size: " + countriesArrayList.size() + "");

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

    private void setCountryAdapter(ArrayList<CountriesModel> countriesArrayList) {
        Log.e(TAG, "countriesArrayList --> " + countriesArrayList.size());
        countryAdapter = new CountryAdapter(getActivity(), countriesArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(countryAdapter);
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
            mProgressD = ProgressD.show(getActivity(), "Connecting", true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();


                postData.add(new BasicNameValuePair("catid", selectedCountryId));
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
                        setUpStateAdapter();
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
        servicesAdapter = new ServicesAdapter(getActivity(), servicesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(servicesAdapter);
    }


    public class GetCategoryAsyncTask extends AsyncTask<String, Void, String> {

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
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
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
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
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

        spinner1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, countries));
    }
}
