package com.quotesin.quotesin.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethod {


    public static final String DISPLAY_MESSAGE_ACTION =
            "com.codecube.broking.gcm";

    public static final String EXTRA_MESSAGE = "message";


   /* public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }*/


    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE,-1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static boolean isToday(long date){
        return DateUtils.isToday(date);
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }


    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        Toast.makeText(context, "Device doesn't support camera.", Toast.LENGTH_SHORT).show();
        return false;
    }


    public static File createImageFile(Context context) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //create image file name under Pictures directory
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "TweetImages");

        //if directory doesn't exist create directory
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = null;
        try {
            //now create jpg image under created directory
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static String isTodayOrYesterday(long date){
        String str="";
        Calendar now = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE,-1);
        tomorrow.add(Calendar.DATE,+1);

        if (DateUtils.isToday(date)){
            str="Today";

        }else if (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE)){
            str= "Yesterday";
        }else if (tomorrow.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && tomorrow.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && tomorrow.get(Calendar.DATE) == cdate.get(Calendar.DATE)){
            str= "Tomorrow";
        }
        return str;
    }

    public static String SimpleDateTime(String findate) {
        Log.d("SimpleDateTime", "1: " + findate);
        DateFormat fromFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        DateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat toFormatTime = new SimpleDateFormat("HH:mm");
        toFormat.setLenient(false);

        String dateStr = findate;
        String timeStr = findate;

        Date date = null;
        Date dateTime = null;
        try {
            date = fromFormat.parse(dateStr);
            dateTime = fromFormat.parse(timeStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateNew = toFormat.format(date) + " at " + toFormatTime.format(dateTime);
        //String timeNew=	toFormatTime.format(dateTime);


        return dateNew;
    }

    public static String rupeesFormat(String value) {
        value = value.replace(",", "");
        char lastDigit = value.charAt(value.length() - 1);
        String result = "";
        int len = value.length() - 1;
        int nDigits = 0;
        for (int i = len - 1; i >= 0; i--) {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0)) {
                result = "," + result;
            }
        }
        return (result + lastDigit);
    }

    public  static String removeLastCommaFromString(String str){
        return str = str.replaceAll(",$", "");
    }

    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    public static String displayOnlyTime(String str){
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(str);
        return currentDateTimeString;
    }

    public static Date stringToDate(String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = format.parse(str);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date;
    }

    public static String dateToString(Date dates){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateTime = null;
        try {
            Date date;
            date = new Date();
            dateTime = dateFormat.format(date);
            System.out.println("Current Date Time : " + dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String displayOnlyDate(String str){
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        String currentDateTimeString = sdf.format(str);
        return currentDateTimeString;
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void myCustomAddPopup(final Activity activity) {
        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle("Are you sure you want to exit?")
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                activity.finish();

                            }
                        }).setNegativeButton("No", null).show();

    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static String getCurrentDateTime(){
        String thisDate;

        //Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date todayDate = new Date();

        return  thisDate = currentDate.format(todayDate);
    }

   /* public static void createDirectory(Activity activity){
        File exportDir = new File(Environment.getExternalStorageDirectory(), "Sales - "+ LoginPreferences.getActiveInstance(activity).getFullname()+"");

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
    }*/

    public static void showAlert(String message, Activity context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        if(m.matches())
            return true;
        else
            return false;
    }

    public static boolean isValidEmaillId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{1,14})$").matcher(email).matches();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
       // final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        //final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*[A-Z]).{8,20})";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }


    public static boolean isValidName(final String name) {

        Pattern pattern;
        Matcher matcher;
        // final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        //final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*[A-Z]).{6,20})";
        //final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*[A-Z]))";
        final String PASSWORD_PATTERN = "[a-zA-Z][a-zA-Z ]*";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(name);

        return matcher.matches();
    }



    public  static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isInternetAvailable(Context ctx) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }

    public static String DateFormatApp(String findate) {

        DateFormat fromFormat = new SimpleDateFormat("dd-MM-yyyy");

        DateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
        toFormat.setLenient(false);
        String dateStr = findate;
        Date date = null;
        try {
            date = fromFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateNew=	toFormat.format(date);

        return dateNew;
    }

    public static String DateFormatServer(String findate) {

        DateFormat fromFormat = new SimpleDateFormat("dd-MMM-yyyy");

        DateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
        toFormat.setLenient(false);
        String dateStr = findate;
        Date date = null;
        try {
            date = fromFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateNew=	toFormat.format(date);

        return dateNew;
    }

    public static String SimpleDateFormat(String findate) {

        DateFormat fromFormat = new SimpleDateFormat("dd-MM-yyyy");

        DateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
        toFormat.setLenient(false);
        String dateStr = findate;
        Date date = null;
        try {
            date = fromFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateNew=	toFormat.format(date);

        return dateNew;
    }


    /**
     * method check if permission is granted or not if not then it will show dialog
     * @param context of the calling class
     * @param permission to check if granted or not
     * @return
     */
    public static int checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    /**
     * method to check if all multiple/single permission is granted or not
     * @param appCompatActivity calling activity
     * @param permissionsToGrant permission list in string array
     * @param PERMISSION_REQUEST_CODE to identify every permission check
     * @return if granted or not
     */
    public static boolean checkMashMallowPermissions(AppCompatActivity appCompatActivity, String[] permissionsToGrant, int PERMISSION_REQUEST_CODE) {
        ArrayList<String> permissions = new ArrayList<>();
        for (String permission : permissionsToGrant) {
            if (checkPermission(appCompatActivity, permission) != 0) {
                permissions.add(permission);
            }
        }
        //if all permission is granted
        if (permissions.size() == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(appCompatActivity,  permissions.toArray(new String[permissions.size()]), PERMISSION_REQUEST_CODE);
        return false;
    }

    public static String getDayOfWeekFromDate(String yourDate){

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date myDate = null;
        try {
            myDate = format.parse(yourDate);
            System.out.println(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(myDate); // yourdate is an object of type Date

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String myDayOfWeek = null;
        if (dayOfWeek==1){
            myDayOfWeek= "Sunday";
        }else if (dayOfWeek==2){
            myDayOfWeek= "Monday";
        }else if (dayOfWeek==3){
            myDayOfWeek= "Tuesday";
        }else if (dayOfWeek==4){
            myDayOfWeek= "Wednesday";
        }else if (dayOfWeek==5){
            myDayOfWeek= "Thursday";
        }else if (dayOfWeek==6){
            myDayOfWeek= "Friday";
        }else if (dayOfWeek==7){
            myDayOfWeek= "Saturday";
        }

        return myDayOfWeek;
    }

    public static String getDayOfWeekFromDate2(String yourDate){

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date myDate = null;
        try {
            myDate = format.parse(yourDate);
            System.out.println(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(myDate); // yourdate is an object of type Date

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String myDayOfWeek = null;
        if (dayOfWeek==1){
            myDayOfWeek= "Sunday";
        }else if (dayOfWeek==2){
            myDayOfWeek= "Monday";
        }else if (dayOfWeek==3){
            myDayOfWeek= "Tuesday";
        }else if (dayOfWeek==4){
            myDayOfWeek= "Wednesday";
        }else if (dayOfWeek==5){
            myDayOfWeek= "Thursday";
        }else if (dayOfWeek==6){
            myDayOfWeek= "Friday";
        }else if (dayOfWeek==7){
            myDayOfWeek= "Saturday";
        }

        return myDayOfWeek;
    }
}
