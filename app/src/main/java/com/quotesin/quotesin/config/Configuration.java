package com.quotesin.quotesin.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.quotesin.quotesin.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/**
 * Created by aftab on 1/10/2018.
 */

public class Configuration {

    public static boolean hasNetworkConnection(Context context) {
        // TODO Auto-generated method stub
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        boolean isNetworkAvailable = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
       /* if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info)
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(500); //choose your own timeframe
                        urlc.setReadTimeout(500); //choose your own timeframe
                        urlc.connect();
                        int networkcode2 = urlc.getResponseCode();
                        isNetworkAvailable=true;
                        return (networkcode2 == 200);
                    } catch (IOException e) {
                        isNetworkAvailable=false;
                        return (false);  //connectivity exists, but no internet.
                    }
                }
        }
        return isNetworkAvailable;*/
    }

    public static void showDialog(String message, ProgressDialog dialog)
    {
        dialog.show();
        dialog.setMessage(message);
        dialog.setCancelable(false);
    }

 /*   public static void saveValue(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getValue(Context context, String key)
    {
        SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);

        return prefs.getString(key, "empty");
    }*/
    public static void showcalendar(final TextView edtDob, Context context)
    {
        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if ((monthOfYear+1)<10){
                            String mont="0"+ (monthOfYear + 1);
                            edtDob.setText(dayOfMonth + "-" + mont + "-" + year);
                        }else {
                            edtDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }



                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public static String encodePath(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        //Base64.de
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Dialog openDialog(Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.setContentView(R.layout.dialog);
        final Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return  dialog;
    }


    public static Dialog openBdayDialog(Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.setContentView(R.layout.birthday_dialog);
        final Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return  dialog;
    }


    public static void closeDialog(Dialog dialog, TextView textView, String bloodGroup)
    {

        dialog.dismiss();
        textView.setText(bloodGroup);
    }

    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPasswordValid(String password){
        boolean isValid=false;

        String expression="((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
        CharSequence inputStr = password;

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
            System.out.println("valid");
        }else {
            System.out.println("Invalid");
        }

        return isValid;
    }

    public static void showCustomToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void openPopupUpDown(Context context, int animationSource, String error, String message) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.notice_info);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_notice_info);
        TextView txtMessage=dialg.findViewById(R.id.txt_notice_info);
        Button btnOk=dialg.findViewById(R.id.btn_notice_info);
        txtMessage.setText(message);
        if (error.equalsIgnoreCase("error")){
            imageView.setImageResource(R.drawable.warning);
        }else if (error.equalsIgnoreCase("internetError")){
            imageView.setImageResource(R.drawable.nointernet);
        }else {
            imageView.setImageResource(R.drawable.success);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialg.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialg.getWindow()).getAttributes().windowAnimations = animationSource;
        }
        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
    public static  void openPrettyDialog(Context context,String message){
        final PrettyDialog pDialog = new PrettyDialog(context);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        pDialog.setTitle("QuotesIn")
                .setMessage(message)
                .setIcon(R.mipmap.quotes_logo)
                .addButton(
                        "OK",
                        R.color.pdlg_color_white,
                        R.color.splash_color,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                pDialog.dismiss();
                            }
                        }
                )
                .setAnimationEnabled(true)
                .show();
        Window window = pDialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    public static void openDialogUnderMaintenace(Context context){
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_status);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus=dialg.findViewById(R.id.txt_status);
        TextView txtTitle=dialg.findViewById(R.id.txt_title_popup);
        Button btnReciept=dialg.findViewById(R.id.btn_recipt);
        Button btnOk= dialg.findViewById(R.id.btn_okay);
        txtTitle.setVisibility(View.GONE);
        txtStatus.setText("Service Under Maintenance!!!\nComing Soon...");
        imageView.setImageResource(R.drawable.maintenance);
        btnReciept.setVisibility(View.GONE);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                dialg.dismiss();
            }
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static void openPopupUpDownBack(final Context context, int animationSource,
                                           final String back, String error, String message) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.notice_info);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_notice_info);
        TextView txtMessage=dialg.findViewById(R.id.txt_notice_info);
        Button btnOk=dialg.findViewById(R.id.btn_notice_info);
        txtMessage.setText(message);
        if (error.equalsIgnoreCase("error")){
            imageView.setImageResource(R.drawable.warning);
        }else if (error.equalsIgnoreCase("internetError")){
            imageView.setImageResource(R.drawable.nointernet);
        }else {
            imageView.setImageResource(R.drawable.success);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialg.dismiss();
                if (back.equalsIgnoreCase("userList")) {

                } else if (back.equalsIgnoreCase("transfer")) {

                } else if (back.equalsIgnoreCase("main")) {

                } else {

                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialg.getWindow()).getAttributes().windowAnimations = animationSource;
        }
        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }


    public static void showSnackbar(String message, View v) {
        Snackbar mSnackBar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.RED);
        TextView mainTextView =  (view).findViewById(com.google.android.material.R.id.snackbar_text);
        mainTextView.setTextColor(Color.WHITE);
        mainTextView.setPadding(0,20,0,20);
        mSnackBar.show();
    }


}
