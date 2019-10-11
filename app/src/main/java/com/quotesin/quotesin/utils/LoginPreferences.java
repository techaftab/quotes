package com.quotesin.quotesin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginPreferences {
    private static LoginPreferences preferences = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private String isLoggedIn = "isLoggedIn";

    private String id = "id";
    private String otp = "otp";
    private String user_first_name="user_first_name";
    private String user_last_name="user_last_name";
    private String fullname="fullname";
    private String email="email";
    private String user_username="user_username";
    private String role_id="role_id";
    private String phone="phone";
    private String already_register="already_register";
    private String user_profile_pic="user_profile_pic";
    private String LocationId="location_id";
    private String category_id="category_id";
    private String service_id="service_id";
    private String country_code="country_code";
    private String state_id="state_id";
    private String profileScreen="profileScreen";
    private String DeviceToken = "DeviceToken";
    private String OnlineStatus="OnlineStatus";

    private String FeedbackFlag = "0";

    private String socialLoginType = "socialLoginType";

    public String getSocialLoginType() {
        return mPreferences.getString(this.socialLoginType,"");
    }

    public void setSocialLoginType(String socialLoginType) {
        editor = mPreferences.edit();
        editor.putString(this.socialLoginType, socialLoginType);
        editor.commit();
    }

    public String getDeviceToken() {
        return mPreferences.getString(this.DeviceToken, "");
    }

    public void setDeviceToken(String DeviceToken) {
        editor = mPreferences.edit();
        editor.putString(this.DeviceToken, DeviceToken);
        editor.commit();
    }


    public LoginPreferences(Context context) {
        this.context = context;
        setmPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }


    public SharedPreferences getmPreferences() {
        return mPreferences;
    }

    public void setmPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }


    public static LoginPreferences getActiveInstance(Context context) {
        if (preferences == null) {
            preferences = new LoginPreferences(context);
        }
        return preferences;
    }

    public boolean getIsLoggedIn() {
        return mPreferences.getBoolean(this.isLoggedIn, false);
    }

    public void setIsLoggedIn(boolean isLoggedin) {
        editor = mPreferences.edit();
        editor.putBoolean(this.isLoggedIn, isLoggedin);
        editor.commit();
    }

    public String getProfileScreen() {
        return mPreferences.getString(this.profileScreen,"");
    }

    public void setProfileScreen(String profileScreen) {
        editor=mPreferences.edit();
        editor.putString(this.profileScreen, profileScreen);
        editor.commit();
    }

    public String getUser_profile_pic() {
        return mPreferences.getString(this.user_profile_pic,"");
    }

    public void setUser_profile_pic(String user_profile_pic) {
        editor=mPreferences.edit();
        editor.putString(this.user_profile_pic, user_profile_pic);
        editor.commit();
    }

    public String getRole_id() {
        return mPreferences.getString(this.role_id,"");
    }

    public void setRole_id(String role_id) {
        editor=mPreferences.edit();
        editor.putString(this.role_id, role_id);
        editor.commit();
    }

    public String getFeedbackFlag() {
        return mPreferences.getString(this.FeedbackFlag,"");
    }

    public void setFeedbackFlag(String feedbackFlag) {
        editor=mPreferences.edit();
        editor.putString(this.FeedbackFlag, feedbackFlag);
        editor.commit();
    }

    public String getOnlineStatus() {
        return mPreferences.getString(this.OnlineStatus,"");
    }

    public void setOnlineStatus(String OnlineStatus) {
        editor=mPreferences.edit();
        editor.putString(this.OnlineStatus, OnlineStatus);
        editor.apply();
    }

    public String getPhone() {
        return mPreferences.getString(this.phone,"");
    }

    public void setPhone(String phone) {
        editor=mPreferences.edit();
        editor.putString(this.phone, phone);
        editor.commit();
    }

    public String getUser_username() {
        return mPreferences.getString(this.user_username,"");
    }

    public void setUser_username(String user_username) {
        editor=mPreferences.edit();
        editor.putString(this.user_username, user_username);
        editor.commit();
    }

    public String getFullname() {
        return mPreferences.getString(this.fullname,"");
    }

    public void setFullname(String fullname) {
        editor=mPreferences.edit();
        editor.putString(this.fullname, fullname);
        editor.commit();
    }

    public String getEmail() {
        return mPreferences.getString(this.email,"");
    }

    public void setEmail(String email) {
        editor=mPreferences.edit();
        editor.putString(this.email, email);
        editor.commit();
    }

    public String getUser_last_name() {
        return mPreferences.getString(this.user_last_name,"");
    }

    public void setUser_last_name(String user_last_name) {
        editor=mPreferences.edit();
        editor.putString(this.user_last_name, user_last_name);
        editor.commit();
    }

    public String getAlready_register() {
        return mPreferences.getString(this.already_register,"");
    }

    public void setAlready_register(String already_register) {
        editor=mPreferences.edit();
        editor.putString(this.already_register, already_register);
        editor.commit();
    }

    public String getUser_first_name() {
        return mPreferences.getString(this.user_first_name,"");
    }

    public void setUser_first_name(String user_first_name) {
        editor=mPreferences.edit();
        editor.putString(this.user_first_name, user_first_name);
        editor.commit();
    }

    public String getId() {
        return mPreferences.getString(this.id,"");
    }

    public void setId(String id) {
        editor=mPreferences.edit();
        editor.putString(this.id, id);
        editor.commit();
    }

    public String getLocationId() {
        return mPreferences.getString(this.LocationId,"");
    }

    public void setLocationId(String locationId) {
        editor=mPreferences.edit();
        editor.putString(this.LocationId, locationId);
        editor.commit();
    }
    public String getCategory_id() {
        return mPreferences.getString(this.category_id,"");
    }

    public void setCategory_id(String category_id) {
        editor=mPreferences.edit();
        editor.putString(this.category_id, category_id);
        editor.commit();
    }
    public String getService_id() {
        return mPreferences.getString(this.service_id,"");
    }

    public void setService_id(String service_id) {
        editor=mPreferences.edit();
        editor.putString(this.service_id, service_id);
        editor.commit();
    }

    public String getCountry_code() {
        return mPreferences.getString(this.country_code,"");
    }

    public void setCountry_code(String country_code) {
        editor=mPreferences.edit();
        editor.putString(this.country_code, country_code);
        editor.commit();
    }

    public String getState_id() {
        return mPreferences.getString(this.state_id,"");
    }

    public void setState_id(String state_id) {
        editor=mPreferences.edit();
        editor.putString(this.state_id, state_id);
        editor.commit();
    }

}
