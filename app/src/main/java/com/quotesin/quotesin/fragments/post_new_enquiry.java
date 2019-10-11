package com.quotesin.quotesin.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.ServiceQuestions;
import com.quotesin.quotesin.adapter.PostEnq_CityAdapter;
import com.quotesin.quotesin.adapter.PostEnq_CountryAdapter;
import com.quotesin.quotesin.adapter.PostEnq_StateAdapter;
import com.quotesin.quotesin.adapter.ServicesAdapter;
import com.quotesin.quotesin.model.CitiesModel;
import com.quotesin.quotesin.model.CountriesModel;
import com.quotesin.quotesin.model.ServicesModel;
import com.quotesin.quotesin.model.StatesModel;
import com.quotesin.quotesin.model.spinner_model_category;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.FilePath;
import com.quotesin.quotesin.utils.GPSTracker;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static android.app.Activity.RESULT_OK;
import static com.quotesin.quotesin.activities.ServiceQuestions.answerList;
import static com.quotesin.quotesin.activities.ServiceQuestions.qFlag1;
import static com.quotesin.quotesin.activities.ServiceQuestions.questionList;
import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;
import static java.util.Objects.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class post_new_enquiry extends Fragment implements View.OnClickListener, LocationListener {
    String TAG = "post_new_enquiry";
    private static final int PICK_FILE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int REQUEST_CODE_PERMISSION = 2;
    public static String questionList1 = " ";
    public static String answerList1 = " ";
    View view;
    private TextView tvDate, tvCountry, tvState, tvCity, tvView;
    private FrameLayout flCountry, flState, flCity, flServices, flCategory;
    RecyclerView recyclerview;
    ImageView ivCancel, ivattach;
    RelativeLayout LLDeadLine;
    Button btnSubmit;
    ImageView ivDeadQues;
    EditText etName, etEmail, etEmailConf, etSubject, etMsg, etLocation;
    ArrayList<spinner_model_category> categoryArrayList = new ArrayList<spinner_model_category>();
    ArrayList<ServicesModel> servicesModelArrayList = new ArrayList<ServicesModel>();
    ArrayAdapter<String> adapterSpCategory;
    ArrayAdapter<String> adapterServiceCategory;
    ServicesAdapter servicesAdapter;
    private Spinner spinner1, spinner2;
    ArrayList<CountriesModel> countriesArrayList = new ArrayList<CountriesModel>();
    ArrayList<StatesModel> statesModelArrayList = new ArrayList<StatesModel>();
    ArrayList<CitiesModel> citiesModelArrayList = new ArrayList<CitiesModel>();
    PostEnq_CountryAdapter countryAdapter;
    PostEnq_StateAdapter stateAdapter;
    PostEnq_CityAdapter cityAdapter;
    private String currentDate;
    private double latitude, longitude;
    private Uri filePath;
    private String path;
    private RecyclerView recyclerView1;
    private String selectedCategoriesId, selectedCountryIds, selectedStateIds, selectedCityIds;
    private String selectedIds, selectedServicesIds;
    private String qFlag = "0";

    private List<String> AllCountryList = new ArrayList<>();


    public static CheckBox chkAll, chkAllState, chkAllCity;

    public post_new_enquiry() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.post_new_enquiry, container, false);

        requestStoragePermission();

        if (answerList != null)
            answerList.clear();

        gettingCategory();
        getCurrentDateDay();
        gettingCountry();

        initViews();
        registerClickListeners();


        try {
            String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
            ActivityCompat.requestPermissions(getActivity(), new String[]{mPermission},
                    REQUEST_CODE_PERMISSION);

        } catch (Exception e) {
            e.printStackTrace();
        }

        getLocation();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner1.setSelection(position);
                if (categoryArrayList.size() > 0) {
                    try {
                        selectedCategoriesId = categoryArrayList.get(position - 1).getId();
                        Log.e(TAG, "item getId : " + categoryArrayList.get(position - 1).getId());
                        Log.e(TAG, "item getId-- : " + selectedCategoriesId);

                        tvView.setVisibility(View.GONE);
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


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner2.setSelection(position);
                if (servicesModelArrayList.size() > 0) {
                    try {
                        selectedServicesIds = servicesModelArrayList.get(position - 1).getId();

                        Log.e(TAG, "item getId : " + servicesModelArrayList.get(position - 1).getId());
                        Log.e(TAG, "item getId selectedServicesIds-- : " + selectedServicesIds);

                        new GetServiceQuesAsyncTask().execute();

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

    private void getLocation() {
        GPSTracker gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        } else {
            gps.showSettingsAlert();
        }

        Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        List<Address> addresses = null;

        {
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        assert addresses != null;
        String cityName = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();

        String addr = city + ", " + stateName + ", " + country;

        Log.e("address", cityName);
        Log.e("city", city);
        Log.e("stateName", stateName);

        etLocation.setText(addr);
    }


    private void registerClickListeners() {
        flServices.setOnClickListener(this);
        LLDeadLine.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        flCountry.setOnClickListener(this);
        flState.setOnClickListener(this);
        flCity.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ivattach.setOnClickListener(this);
        ivDeadQues.setOnClickListener(this);
    }

    public void initViews() {
        ivDeadQues = view.findViewById(R.id.ivDeadQues);
        tvView = view.findViewById(R.id.tvView);
        ivattach = view.findViewById(R.id.ivattach);
        flServices = view.findViewById(R.id.flServices);
        flCategory = view.findViewById(R.id.flCategory);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        recyclerview = view.findViewById(R.id.recyclerview);
        tvDate = view.findViewById(R.id.tvDate);
        ivCancel = view.findViewById(R.id.ivCancel);
        LLDeadLine = view.findViewById(R.id.LLDeadLine);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etEmailConf = view.findViewById(R.id.etEmailConf);
        etLocation = view.findViewById(R.id.etLocation);
        etMsg = view.findViewById(R.id.etMsg);
        etSubject = view.findViewById(R.id.etSubject);

        flCountry = view.findViewById(R.id.flCountry);
        flState = view.findViewById(R.id.flState);
        flCity = view.findViewById(R.id.flCity);
        tvCity = view.findViewById(R.id.tvCity);
        tvState = view.findViewById(R.id.tvState);
        tvCountry = view.findViewById(R.id.tvCountry);
        tvDate.setText(currentDate);


        etName.setText(LoginPreferences.getActiveInstance(getActivity()).getUser_username());
        etEmail.setText(LoginPreferences.getActiveInstance(getActivity()).getEmail());
        etEmailConf.setText(LoginPreferences.getActiveInstance(getActivity()).getEmail());


        spinner1 = view.findViewById(R.id.spinner1);

        adapterSpCategory = new ArrayAdapter<String>(requireNonNull(getActivity()), R.layout.spinner_layout) {

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


        spinner2 = view.findViewById(R.id.spinner2);

        //spinner2.setPrompt("Select your favorite Planet!");
        adapterServiceCategory = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout) {

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

    private void gettingCategory() {
        if (CommonMethod.isNetworkAvailable(requireNonNull(getActivity()))) {
            hideSoftKeyboard(getActivity());
            new GetCategoryAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivDeadQues:
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toast_layout_root));
                TextView text = layout.findViewById(R.id.text);
                text.setText("This will set a deadline for when you stop receiving quotes.");
                Toast toast = new Toast(getActivity());
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                break;

            case R.id.ivattach:
                showFileChooser();
                break;

            case R.id.LLDeadLine:
                openDateDialog(tvDate);
                break;

            case R.id.ivCancel:
                finishCurrentFragment();
                break;

            case R.id.flCountry:
                final AlertDialog.Builder builderCo = new AlertDialog.Builder(getActivity());
                LayoutInflater inflaterCo = getLayoutInflater();
                View dialogLayoutCo = inflaterCo.inflate(R.layout.dialog_recyclerview, null);

                chkAll = dialogLayoutCo.findViewById(R.id.chkAll);
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

                recyclerView1 = dialogLayoutCo.findViewById(R.id.recyclerview);
                setCountryAdapter(countriesArrayList);
                builderCo.setView(dialogLayoutCo);
                builderCo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getCountryIds();
                        dialog.dismiss();
                        gettingState();
                        tvState.setText("");
                        tvCity.setText("");
                        selectedStateIds = "";

                    }
                });

                builderCo.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builderCo.show();

                break;

            case R.id.flState:
                if (selectedCountryIds != null) {
                    AlertDialog.Builder builderState = new AlertDialog.Builder(getActivity());
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

                    recyclerView1 = dialogLayout2.findViewById(R.id.recyclerview);
                    setStateAdapter(statesModelArrayList);
                    builderState.setView(dialogLayout2);

                    builderState.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getListState();
                            gettingcities();
                            tvCity.setText("");
                            selectedCityIds = "";

                        }
                    });
                    builderState.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builderState.show();
                } else {
                    CommonMethod.showAlert("Choose country", getActivity());
                }

                break;

            case R.id.flCity:
                String stcity = String.valueOf(statesModelArrayList.size());
                Log.e(TAG, stcity);
                if (!stcity.equals("0")) {
                    AlertDialog.Builder builderCity = new AlertDialog.Builder(getActivity());
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

                    recyclerView1 = dialogLayout3.findViewById(R.id.recyclerview);
                    setCityAdapter(citiesModelArrayList);
                    builderCity.setView(dialogLayout3);

                    builderCity.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getCityIds();
                        }
                    });

                    builderCity.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builderCity.show();
                } else {
                    CommonMethod.showAlert("Choose State", getActivity());
                }

                break;

            case R.id.btnSubmit:
                if (CheckValidation()) {
                    uploadMultipart();
                }
                break;
        }
    }

    private Boolean CheckValidation() {
        if (questionList.size() != answerList.size()) {
            CommonMethod.showAlert("Please Fill Attachment Form ", getActivity());
            return false;
        }
        if (etSubject.getText().toString().equals("")) {
            CommonMethod.showAlert("Please Enter Subject", getActivity());
            return false;
        } else if (selectedCategoriesId == null) {
            CommonMethod.showAlert("Please Select Category", getActivity());
            return false;
        } else if (selectedServicesIds == null) {
            CommonMethod.showAlert("Please Service City", getActivity());
            return false;
        } else if (etLocation.getText().toString().trim().equals("")) {
            CommonMethod.showAlert("Please Enter Our Location", getActivity());
            return false;
        } else if (etLocation.getText().toString().contains("\\s")) {
            CommonMethod.showAlert("Please Enter Our Location", getActivity());
            return false;
        } else if (spinner1.getSelectedItem().toString().equalsIgnoreCase("Choose Category")) {
            CommonMethod.showAlert(" Please Choose Category", getActivity());
            return false;
        } else if (spinner2.getSelectedItem().toString().equalsIgnoreCase("Select Product/Service")) {
            CommonMethod.showAlert(" Please Choose Service", getActivity());
            return false;
        } else if (selectedCountryIds == null) {
            CommonMethod.showAlert("Please Select Country", getActivity());
            return false;
        } else if (selectedStateIds.equals("")) {
            CommonMethod.showAlert("Please Select State", getActivity());
            return false;
        } else if (selectedCityIds.equals("")) {
            CommonMethod.showAlert("Please Select City", getActivity());
            return false;
        } else if (tvCountry.getText().toString().equalsIgnoreCase("Choose Country")) {
            CommonMethod.showAlert("Please Select Country", getActivity());
            return false;
        } else if (tvState.getText().toString().equalsIgnoreCase("Choose State")) {
            CommonMethod.showAlert("Please Select State", getActivity());
            return false;
        } else if (tvCity.getText().toString().equalsIgnoreCase("Choose City")) {
            CommonMethod.showAlert("Please Select City", getActivity());
            return false;
        } else if (qFlag.equals("1")) {
            if (qFlag1.equals("0")) {
                CommonMethod.showAlert("Please Fill Attachment Form", getActivity());
                return false;
            }
            return true;
        }
        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*|application/pdf|application/txt|application/doc");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "application/pdf"});
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST && data != null && data.getData() != null) {
                filePath = data.getData();
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void uploadMultipart() {
        if (filePath != null)
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                path = getFilePathForN(filePath, getActivity());
                Log.e("sd", path);
            } else {
                path = FilePath.getPath(getActivity(), filePath);
                Log.e("sd", path);
            }

        PostAPI(path);

        //file_name.setText(path);
        // String name = file_name.getText().toString().trim();


      /*  if (path == null) {
            Toast.makeText(getActivity(), "Please Select Again", Toast.LENGTH_LONG).show();
        } else {
            PostAPI(path);
        }*/
    }

    private String getFilePathForN(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return file.getPath();
    }

    private void PostAPI(String path) {
        Log.e(TAG, "user_id--" + LoginPreferences.getActiveInstance(getActivity()).getId());
        Log.e(TAG, "service_catid--" + selectedCategoriesId);
        Log.e(TAG, "deadline--" + tvDate.getText().toString());
        Log.e(TAG, "subject--" + etSubject.getText().toString());
        Log.e(TAG, "ser id--" + selectedServicesIds);
        Log.e(TAG, "country_id--" + selectedCountryIds);
        Log.e(TAG, "selectedStateIds--" + selectedStateIds);
        Log.e(TAG, "name" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
        Log.e(TAG, "answer" + answerList1);
        Log.e(TAG, "ques_name" + questionList1);
        Log.e(TAG, "yourlocation" + etLocation.getText().toString());
        Log.e(TAG, "location_code" + selectedCityIds);
        Log.e(TAG, "email" + LoginPreferences.getActiveInstance(getActivity()).getEmail());


        if (path == null) {
            Log.e("path==", "null");
            new GetPostEnquiryAsyncTask().execute();

        } else {
            final ProgressD progressD = ProgressD.show(getActivity(), "Connecting", false);
            Log.e("path==", "not null");
            AndroidNetworking.upload("http://dev.webmobril.services/quotesinapp/api/post_enquery")

                    .addMultipartParameter("user_id", LoginPreferences.getActiveInstance(getActivity()).getId())
                    .addMultipartParameter("name", LoginPreferences.getActiveInstance(getActivity()).getUser_username())
                    .addMultipartParameter("email", LoginPreferences.getActiveInstance(getActivity()).getEmail())
                    .addMultipartParameter("service_catid", selectedCategoriesId)
                    .addMultipartParameter("service_id", selectedServicesIds)
                    .addMultipartParameter("deadline", tvDate.getText().toString())
                    .addMultipartParameter("subject", etSubject.getText().toString())
                    .addMultipartParameter("message", etMsg.getText().toString())
                    .addMultipartParameter("yourlocation", etLocation.getText().toString())
                    .addMultipartParameter("country_id", selectedCountryIds)
                    .addMultipartParameter("state_id", selectedStateIds)
                    .addMultipartParameter("location_code", selectedCityIds)
                    .addMultipartParameter("answer", answerList1)
                    .addMultipartParameter("ques_name", questionList1)
                    .addMultipartFile("attachment", new File(path))
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Toast.makeText(getActivity(), "Enquiry Sent Successfully.", Toast.LENGTH_SHORT).show();
                            finishCurrentFragment();
                            progressD.dismiss();
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.e("onError", error.toString());
                            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                            progressD.dismiss();
                        }
                    });
        }
    }

    private void getServicesList() {
        if (CommonMethod.isNetworkAvailable(getActivity())) {
            hideSoftKeyboard(getActivity());
            new GetServicesAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCountryIds() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (CountriesModel number : countriesArrayList) {
            if (number.isSelected()) {
                if (stringBuilder.length() > 0 || stringBuilder1.length() > 0)
                    stringBuilder.append(",");
                stringBuilder1.append(",");
                stringBuilder.append(number.getId());
                stringBuilder1.append(number.getCountry_name());
            }
        }
        selectedCountryIds = stringBuilder.toString();

        String countryName = stringBuilder1.toString();

        if (!countryName.equals("")) {
            String countryName1 = countryName.substring(1);


            if (chkAll.isChecked()) {
                tvCountry.setText("All Selected");
            } else {
                tvCountry.setText(countryName1);
            }


            Log.e("selectedCountryIds---", selectedCountryIds);
            Log.e("tvServicesCho---", countryName1);
        } else {
            tvCountry.setText("Choose Country");

        }
    }

    private void getListState() {
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
        selectedStateIds = stringBuilder.toString();

        String statename = stringBuilder1.toString();

        if (!statename.equals("")) {
            String stateName1 = statename.substring(1);
            if (chkAllState.isChecked()) {
                tvState.setText("All Selected");
            } else {
                tvState.setText(stateName1);
            }


            Log.e("selectedStateIds---", selectedStateIds);
            Log.e("selectedStateIds ---", stateName1);
        } else {
            tvState.setText("Choose State");
        }
    }

    private void getCityIds() {
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
        selectedCityIds = stringBuilder.toString();

        String cityname = stringBuilder1.toString();

        if (!cityname.equals("")) {
            String cityname1 = cityname.substring(1);
            tvCity.setText(cityname1);

            if (chkAllCity.isChecked()) {
                tvCity.setText("All Selected");
            } else {
                tvCity.setText(cityname1);
            }


            Log.e("selectedCityIds---", selectedCityIds);
            Log.e("tvCity---", cityname1);
        } else {
            tvCity.setText("Choose City");

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e(TAG, "longi" + location.getLongitude());
        Log.e(TAG, "lati" + location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void setUpCategoryAdapter() {
        if (getActivity() != null) {
            Log.e(TAG, " Countries size: " + categoryArrayList.size() + "");

            ArrayList<String> countries = new ArrayList<>();
            countries.clear();

            countries.add("Choose Category");

            for (spinner_model_category model1 : categoryArrayList) {
                countries.add(model1.getTitle());
                adapterSpCategory.add(model1.getTitle());
            }
            spinner1.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, countries));
        }
    }

    private void setUpServicesAdapter() {
        if (getActivity() != null) {
            Log.e(TAG, " Services size: " + servicesModelArrayList.size() + "");

            ArrayList<String> services = new ArrayList<>();
            services.clear();

            services.add("Select Product/Service");
            for (ServicesModel model1 : servicesModelArrayList) {
                String sername = String.valueOf(Html.fromHtml(Html.fromHtml(model1.getSer_name()).toString()));
                services.add(sername);
                adapterSpCategory.add(sername);
            }
            spinner2.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, services));
        }
    }

    private void getCurrentDateDay() {
        String strDateTime = CommonMethod.getCurrentDateTime();
        Log.e(TAG, "current date time is : " + strDateTime);
        String[] parts = strDateTime.split(" ");

        currentDate = parts[0];
        String startTime = parts[1];
        String finishTime = parts[1];

        Log.e(TAG, "current date is : " + currentDate);
        Log.e(TAG, "current StartTime is : " + startTime);
        Log.e(TAG, "current FinishTime is : " + finishTime);
    }

    private void openDateDialog(final TextView tvDate) {
        final Calendar calendar = Calendar.getInstance();
        Locale locale = Locale.getDefault();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", locale);
        Date date = null;
        try {
            date = simpleDateFormat.parse(CommonMethod.getCurrentDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long max = calendar.getTimeInMillis();
        if (date != null) calendar.setTime(date);

        DatePickerDialog datepickerdialog = new DatePickerDialog(this.tvDate.getContext(),
                android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {//THEME_TRADITIONAL
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                post_new_enquiry.this.tvDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datepickerdialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datepickerdialog.show();
    }

    private void gettingCountry() {
        if (CommonMethod.isNetworkAvailable(requireNonNull(getActivity()))) {
            hideSoftKeyboard(getActivity());
            new GetCountriesAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingState() {
        if (CommonMethod.isNetworkAvailable(requireNonNull(getActivity()))) {
            hideSoftKeyboard(getActivity());
            new GetStateAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingcities() {
        if (CommonMethod.isNetworkAvailable(requireNonNull(getActivity()))) {
            hideSoftKeyboard(getActivity());
            new GetCityAsyncTask().execute();
        } else {
            Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("WrongConstant")
    private void setCountryAdapter(ArrayList<CountriesModel> countriesArrayList) {
        Log.e(TAG, "countriesArrayList --> " + countriesArrayList.size());
        countryAdapter = new PostEnq_CountryAdapter(getActivity(), countriesArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(countryAdapter);
    }

    @SuppressLint("WrongConstant")
    private void setStateAdapter(ArrayList<StatesModel> statesModelArrayList) {
        Log.e(TAG, "statesModelArrayList --> " + statesModelArrayList.size());
        stateAdapter = new PostEnq_StateAdapter(getActivity(), statesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(stateAdapter);

    }

    @SuppressLint("WrongConstant")
    private void setCityAdapter(ArrayList<CitiesModel> citiesModelArrayList) {
        Log.e(TAG, "citiesModelArrayList --> " + citiesModelArrayList.size());
        cityAdapter = new PostEnq_CityAdapter(getActivity(), citiesModelArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setAdapter(cityAdapter);

    }

    private void finishCurrentFragment() {
        int backStackCount = requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount >= 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onResume() {
        requireNonNull(((AppCompatActivity) requireNonNull(getActivity())).getSupportActionBar()).hide();
        super.onResume();

        if (qFlag1.equals("1")) {
            if (questionList.size() == answerList.size()) {
                Toast.makeText(getActivity(), "Attachment Form added successfully!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) requireNonNull(getActivity())).getSupportActionBar().show();
    }

    @SuppressLint("StaticFieldLeak")
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

                        categoryArrayList = new ArrayList<spinner_model_category>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);

                            spinner_model_category model = new spinner_model_category();

                            model.setId(post.getString("id"));
                            model.setTitle(post.getString("category_name"));

                            categoryArrayList.add(model);
                        }
                        Log.e(TAG, " Countries size: " + categoryArrayList.size() + "");
                        setUpCategoryAdapter();

                    } else if (status.equals("Failed")) {
                        setUpCategoryAdapter();
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

    @SuppressLint("StaticFieldLeak")
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

                        servicesModelArrayList = new ArrayList<ServicesModel>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);

                            ServicesModel model = new ServicesModel();

                            model.setId(post.getString("id"));
                            model.setSer_name(post.getString("service_name"));
                            servicesModelArrayList.add(model);


                        }

                        Log.e(TAG, " Services size: " + servicesModelArrayList.size() + "");
                        setUpServicesAdapter();

                    } else if (status.equals("Failed")) {
                        setUpServicesAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }
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

            Log.e("Contry Service", "get Country api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
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

                            //  gettingState();

                            countriesArrayList.add(model);
                            AllCountryList.add(post.getString("id"));
                        }

                        Log.e(TAG, " countriesArrayList size: " + countriesArrayList.size() + "");
                        Log.e(TAG, " AllCountryList size: " + AllCountryList.size() + "");


                    } else if (status.equals("Failed")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            mProgressD.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class GetStateAsyncTask extends AsyncTask<String, Void, String> {

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
                String url1 = APIUrl.get_all_state + "?country_id=" + selectedCountryIds;
                result = CustomHttpClient.executeHttpGet(url1);
                System.out.print(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("State Service", "get states api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");

                        statesModelArrayList = new ArrayList<StatesModel>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);

                            StatesModel model = new StatesModel();
                            model.setState_id(post.getString("id"));
                            model.setState_name(post.getString("name"));

                            statesModelArrayList.add(model);
                        }

                        Log.e(TAG, " statesModelArrayList size: " + statesModelArrayList.size() + "");

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

    @SuppressLint("StaticFieldLeak")
    public class GetCityAsyncTask extends AsyncTask<String, Void, String> {

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
        protected String doInBackground(String... params) {
            try {
                String url1 = APIUrl.get_all_city + "?country_id=" + selectedCountryIds + "&" + "state_id=" + selectedStateIds;
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
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("message");

                        citiesModelArrayList = new ArrayList<CitiesModel>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);

                            CitiesModel model = new CitiesModel();

                            model.setCity_id(post.getString("id"));
                            model.setCity_name(post.getString("name"));

                            citiesModelArrayList.add(model);
                        }

                        Log.e(TAG, " citiesModelArrayList size: " + statesModelArrayList.size() + "");

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

    @SuppressLint("StaticFieldLeak")
    public class GetPostEnquiryAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        JSONObject jObject;
        String id, user_first_name, user_last_name, location_code, service_id, category_id, name, user_phone, user_profile_pic;
        private String result;
        private String status = "";
        private String responseMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(getActivity(), "Connecting", true);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
                postData.add(new BasicNameValuePair("user_id", LoginPreferences.getActiveInstance(getActivity()).getId()));
                postData.add(new BasicNameValuePair("name", LoginPreferences.getActiveInstance(getActivity()).getUser_username()));
                postData.add(new BasicNameValuePair("email", LoginPreferences.getActiveInstance(getActivity()).getEmail()));
                postData.add(new BasicNameValuePair("service_catid", selectedCategoriesId));
                postData.add(new BasicNameValuePair("service_id", selectedServicesIds));
                postData.add(new BasicNameValuePair("deadline", tvDate.getText().toString()));
                postData.add(new BasicNameValuePair("subject", etSubject.getText().toString()));
                postData.add(new BasicNameValuePair("message", etMsg.getText().toString()));
                postData.add(new BasicNameValuePair("yourlocation", etLocation.getText().toString()));


                postData.add(new BasicNameValuePair("attachment", ""));


                if (tvCountry.getText().equals("All Selected")) {
                    postData.add(new BasicNameValuePair("country_id", "1"));
                } else {
                    postData.add(new BasicNameValuePair("country_id", selectedCountryIds));
                }
                if (tvState.getText().equals("All Selected")) {
                    postData.add(new BasicNameValuePair("state_id", "11,12"));
                } else {
                    postData.add(new BasicNameValuePair("state_id", selectedStateIds));
                }
                if (tvCity.getText().equals("All Selected")) {
                    postData.add(new BasicNameValuePair("location_code", "55"));
                } else {
                    postData.add(new BasicNameValuePair("location_code", selectedCityIds));
                }


                if (!answerList1.equals(" ") && !questionList1.equals(" ")) {
                    Log.e(TAG, "if condition --");
                    postData.add(new BasicNameValuePair("answer", answerList1));
                    postData.add(new BasicNameValuePair("ques_name", questionList1));
                }
                Log.e(TAG, "GetPostEnquiryAsyncTask param is :" + postData.toString());

                result = CustomHttpClient.executeHttpPost(APIUrl.POST_ENQUIRY, postData);
                System.out.print(result);
                Log.e(TAG, "result is :" + result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "post enquiry api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jObject = new JSONObject(result);
                    responseMessage = jObject.getString("message");
                    status = jObject.getString("Status");

                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();
                        finishCurrentFragment();


                    } else if (status.equals("Failed")) {

                        final PrettyDialog pDialog = new PrettyDialog(getActivity());
                        pDialog.setTitle("QuotesIn")
                                .setMessage(responseMessage)
                                .setIcon(R.mipmap.quotes_logo)
                                .addButton(
                                        "OK",                    // button text
                                        R.color.pdlg_color_white,        // button text color
                                        R.color.splash_color,        // button background color
                                        new PrettyDialogCallback() {        // button OnClick listener
                                            @Override
                                            public void onClick() {
                                                finishCurrentFragment();
                                                pDialog.dismiss();
                                            }
                                        }
                                )
                                .setAnimationEnabled(true)
                                .show();

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
    }

    @SuppressLint("StaticFieldLeak")
    public class GetServiceQuesAsyncTask extends AsyncTask<String, Void, String> {

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
                String url1 = APIUrl.Get_Service_Ques + "?service_id=" + selectedServicesIds;
                result = CustomHttpClient.executeHttpGet(url1);
                System.out.print(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("service quest--", "get service quest api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jObject.getJSONArray("result");
                        qFlag = "1";
                        Intent intent = new Intent(getActivity(), ServiceQuestions.class);
                        intent.putExtra("selectedServicesIds", selectedServicesIds);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressD.dismiss();
            }
        }
    }


}
