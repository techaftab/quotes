package com.quotesin.quotesin.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.OpenableColumns;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;


import com.quotesin.quotesin.R;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.FilePath;
import com.quotesin.quotesin.utils.HttpClient;
import com.quotesin.quotesin.utils.LoginPreferences;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import static android.app.Activity.RESULT_OK;
import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class Enquiry_Reply extends Fragment implements View.OnClickListener {

    View view;

    private static final int PICK_FILE_REQUEST = 1;
    private String update_quote;
    ImageView ivAttach, ivDelete, ivBack;
    Button btn_submit;
    EditText etMsg, etSubject, etPrice;
    Spinner spPrice, spRate, spPeriod;
    TextView tvFileName;
    String TAG = this.getClass().getSimpleName();
    String mail_id;
    ProgressDialog dialog;

    private Uri filePath;
    private String path;
    private static final int STORAGE_PERMISSION_CODE = 123;

    public Enquiry_Reply() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.enquiry__reply, container, false);

        Bundle b = this.getArguments();
        assert b != null;

        mail_id = b.getString("mail_id");
        update_quote = b.getString("update_quote");
        Log.e(TAG, "mail_id is :" + mail_id);
        Log.e(TAG, "update_quote is :" + update_quote);

        requestStoragePermission();

        initViews();
        registerClickListener();
        AndroidNetworking.initialize(getActivity());

        return view;
    }

    private void initViews() {
        ivAttach = view.findViewById(R.id.ivAttach);
        ivDelete = view.findViewById(R.id.ivDelete);
        ivBack = view.findViewById(R.id.ivBack);
        btn_submit = view.findViewById(R.id.btn_submit);
        etMsg = view.findViewById(R.id.etMsg);
        etSubject = view.findViewById(R.id.etSubject);
        etPrice = view.findViewById(R.id.etPrice);
        spPeriod = view.findViewById(R.id.spPeriod);
        spRate = view.findViewById(R.id.spRate);
        spPrice = view.findViewById(R.id.spPrice);
        tvFileName = view.findViewById(R.id.tvFileName);

    }

    private void registerClickListener() {
        ivAttach.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        super.onResume();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAttach:
                showFileChooser();
                break;

            case R.id.btn_submit:
                if (CheckValidation()) {
                    if (CommonMethod.isNetworkAvailable(getContext())) {
                        hideSoftKeyboard(getActivity());
                        uploadMultipart();
                    } else {
                        Toast.makeText(getActivity(), "Check internet connectivity!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.ivBack:
                int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if (backStackCount >= 1) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }


    private void showFileChooser() {
      /*  Intent intent = new Intent();
        intent.setType("image/*|application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_FILE_REQUEST);*/

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


    private boolean CheckValidation() {
        String price1 = null;
        if (!etPrice.getText().toString().equals("")) {
            String price = etPrice.getText().toString();
            price1 = price.substring(0, 1);
        }


        if (etSubject.getText().toString().trim().length() == 0) {
            CommonMethod.showAlert("Please enter subject", getActivity());
            return false;
        } else if (etMsg.getText().toString().trim().length() == 0) {
            CommonMethod.showAlert("Please enter Message", getActivity());
            return false;
        } else if (etPrice.getText().toString().trim().length() == 0) {
            CommonMethod.showAlert("Please enter price ", getActivity());
            return false;
        } else if (price1.equals("0")) {
            CommonMethod.showAlert("Price not zero", getActivity());
            return false;
        } else if (spPrice.getSelectedItem().equals("Select Price type")) {
            CommonMethod.showAlert("Select Price Type ", getActivity());
            return false;
        } else if (spRate.getSelectedItem().equals("Select Payment Rate")) {
            CommonMethod.showAlert("Select Payment Rate ", getActivity());
            return false;
        } else if (spPeriod.getSelectedItem().equals("Select Time Frame")) {
            CommonMethod.showAlert("Select Time Frame ", getActivity());
            return false;
        }

        return true;
    }


    private void PostAPI(String path) {
        Log.e(TAG, "enquiry_id--" + mail_id);
        Log.e(TAG, "business_username--" + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
        Log.e(TAG, "q_price_type" + spPrice.getSelectedItem().toString());
        Log.e(TAG, "q_occur" + spRate.getSelectedItem().toString());
        Log.e(TAG, "q_start" + spPeriod.getSelectedItem().toString());
        Log.e(TAG, "quote_price" + etPrice.getText().toString());
        Log.e(TAG, "subject" + etSubject.getText().toString());
        Log.e(TAG, "update_quote" + update_quote);

        if (update_quote.equals("no")) {
            if (path == null) {

                new SendQuoteAsyncTask("no").execute();

            } else {
                final ProgressD progressD = ProgressD.show(getActivity(), "Connecting", false);
                AndroidNetworking.upload("http://dev.webmobril.services/quotesinapp/api/send_quote")
                        .addMultipartParameter("enquiry_id", mail_id)
                        .addMultipartParameter("business_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username())
                        .addMultipartParameter("q_price_type", spPrice.getSelectedItem().toString())
                        .addMultipartParameter("q_occur", spRate.getSelectedItem().toString())
                        .addMultipartParameter("q_start", spPeriod.getSelectedItem().toString())
                        .addMultipartParameter("quote_price", etPrice.getText().toString())
                        .addMultipartParameter("subject", etSubject.getText().toString())
                        .addMultipartParameter("message", etMsg.getText().toString())
                        .addMultipartFile("attachment", new File(path))
                        .setTag("test")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("onError", response.toString());
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

        } else {
            if (path == null) {
                new SendQuoteAsyncTask("update").execute();
            } else {
                final ProgressD progressD = ProgressD.show(getActivity(), "Connecting", false);
                AndroidNetworking.upload("http://dev.webmobril.services/quotesinapp/api/update_send_quote")
                        .addMultipartParameter("enquiry_id", mail_id)
                        .addMultipartParameter("business_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username())
                        .addMultipartParameter("q_price_type", spPrice.getSelectedItem().toString())
                        .addMultipartParameter("q_occur", spRate.getSelectedItem().toString())
                        .addMultipartParameter("q_start", spPeriod.getSelectedItem().toString())
                        .addMultipartParameter("quote_price", etPrice.getText().toString())
                        .addMultipartParameter("subject", etSubject.getText().toString())
                        .addMultipartParameter("message", etMsg.getText().toString())
                        .addMultipartFile("attachment", new File(path))

                        .setTag("test")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("onError", response.toString());
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
    }


    private class SendQuoteAsyncTask extends AsyncTask<String, Void, String> {

        ProgressD mProgressD;
        private String response;
        JSONObject jObject;
        private String status = "";
        private String responseMessage = "";
        String id, type;

        public SendQuoteAsyncTask(String type) {
            this.type = type;
        }

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

            Log.e(TAG, "SendEnquiryAsyncTask api response is " + response);
            if (response == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(response);
                    status = jObject.getString("Status");
                    responseMessage = jObject.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        finishCurrentFragment();
                    } else if (status.equals("Failed")) {
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
            String url;

            if (type.equals("no"))
                url = APIUrl.SEND_QUOTES;
            else
                url = APIUrl.Update_Send_Quote;

            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();
                Log.e(TAG, "SEND_QUOTES after connection url: " + url);
                Log.e(TAG, "user id== " + LoginPreferences.getActiveInstance(getActivity()).getId());

                client.addFormPart("enquiry_id", mail_id);
                client.addFormPart("business_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("q_price_type", spPrice.getSelectedItem().toString());
                client.addFormPart("q_occur", spRate.getSelectedItem().toString());
                client.addFormPart("q_start", spPeriod.getSelectedItem().toString());
                client.addFormPart("quote_price", etPrice.getText().toString());
                client.addFormPart("subject", etSubject.getText().toString());
                client.addFormPart("message", etMsg.getText().toString());
                client.addFormPart("attachment", "");

                client.finishMultipart();

                response = client.getResponse();
                Log.e(TAG, "SEND_QUOTES response :" + response);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private void finishCurrentFragment() {
        int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount >= 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(APIUrl.SEND_QUOTES);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("enquiry_id", mail_id);
                connection.setRequestProperty("business_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                connection.setRequestProperty("q_price_type", spPrice.getSelectedItem().toString());
                connection.setRequestProperty("q_occur", spRate.getSelectedItem().toString());
                connection.setRequestProperty("q_start", spPeriod.getSelectedItem().toString());
                connection.setRequestProperty("quote_price", etPrice.getText().toString());
                connection.setRequestProperty("subject", etSubject.getText().toString());
                connection.setRequestProperty("message", etMsg.getText().toString());
                connection.setRequestProperty("attachment", selectedFilePath);


                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {

                    try {

                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getActivity(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                try {
                    serverResponseCode = connection.getResponseCode();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(getActivity(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                }
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/" + fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

                /*if (wakeLock.isHeld()) {

                    wakeLock.release();
                }*/


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "URL Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }


  /*  public class SendQuotesAsyncTask extends AsyncTask<String, Void, String> {

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

            Log.e("SEND_QUOTES", "SEND_QUOTES api response is " + result);
            if (result == null) {
                Toast.makeText(getActivity(), "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {

                try {
                    jObject = new JSONObject(result);
                    status = jObject.getString("status");
                    responseMessage = jObject.getString("message");

                    if (status.equalsIgnoreCase("success")) {
                        JSONObject jsonObject = jObject.getJSONObject("result");
                        if (jsonObject != null) {

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

        private String callService() {
            String url = APIUrl.SEND_QUOTES;
            HttpClient client = new HttpClient(url);
            Log.e(TAG, "Before connection url: " + url);

            try {
                client.connectForMultipart();

                client.addFormPart("enquiry_id", mail_id);
                client.addFormPart("business_username", LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                client.addFormPart("q_price_type", spPrice.getSelectedItem().toString());
                client.addFormPart("q_occur", spRate.getSelectedItem().toString());
                client.addFormPart("q_start", spPeriod.getSelectedItem().toString());
                client.addFormPart("quote_price", etPrice.getText().toString());
                client.addFormPart("subject", etSubject.getText().toString());
                client.addFormPart("message", etMsg.getText().toString());

                client.addFilePart("attachment", selectedFilePath);


                Log.e(TAG, "file vData: " + bytes);

                Log.e(TAG, "MID: " + mail_id);
                Log.e(TAG, "Name: " + LoginPreferences.getActiveInstance(getActivity()).getUser_username());
                Log.e(TAG, "SPPRICE: " + spPrice.getSelectedItem().toString());
                Log.e(TAG, "SPRATE: " + spRate.getSelectedItem().toString());
                Log.e(TAG, "SPPERIOD: " + spPeriod.getSelectedItem().toString());
                Log.e(TAG, "ETPRICE: " + etPrice.getText().toString());
                Log.e(TAG, "ETSUBJECT: " + etSubject.getText().toString());
                Log.e(TAG, "ETMSG: " + etMsg.getText().toString());


                client.finishMultipart();

                result = client.getResponse();
                Log.e(TAG, "SEND_QUOTES response :" + result);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "getMessage response :" + e.getMessage());

            }
            return result;
        }

    }*/


}
