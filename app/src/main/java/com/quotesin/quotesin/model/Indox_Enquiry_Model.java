package com.quotesin.quotesin.model;

public class Indox_Enquiry_Model {

    private String q_count, Review_Status, id, enquiry_title, enquiry_logo, enquiry_description, enquiry_post_date, enquiry_expired, user_id, consumer_username, consumer_type, consumer_email, location_code, enquiry_status;

    private static boolean isSelected1 = false;
    private boolean isSelected = false;

    public String getQ_count() {
        return q_count;
    }

    public void setQ_count(String q_count) {
        this.q_count = q_count;
    }

    public String getReview_Status() {
        return Review_Status;
    }

    public void setReview_Status(String review_Status) {
        Review_Status = review_Status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnquiry_title() {
        return enquiry_title;
    }

    public void setEnquiry_title(String enquiry_title) {
        this.enquiry_title = enquiry_title;
    }

    public String getEnquiry_description() {
        return enquiry_description;
    }

    public void setEnquiry_description(String enquiry_description) {
        this.enquiry_description = enquiry_description;
    }

    public String getEnquiry_post_date() {
        return enquiry_post_date;
    }

    public void setEnquiry_post_date(String enquiry_post_date) {
        this.enquiry_post_date = enquiry_post_date;
    }


    public String getEnquiry_logo() {
        return enquiry_logo;
    }

    public void setEnquiry_logo(String enquiry_logo) {
        this.enquiry_logo = enquiry_logo;
    }

    public String getEnquiry_expired() {
        return enquiry_expired;
    }

    public void setEnquiry_expired(String enquiry_expired) {
        this.enquiry_expired = enquiry_expired;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getConsumer_username() {
        return consumer_username;
    }

    public void setConsumer_username(String consumer_username) {
        this.consumer_username = consumer_username;
    }

    public String getConsumer_type() {
        return consumer_type;
    }

    public void setConsumer_type(String consumer_type) {
        this.consumer_type = consumer_type;
    }

    public String getConsumer_email() {
        return consumer_email;
    }

    public void setConsumer_email(String consumer_email) {
        this.consumer_email = consumer_email;
    }

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public String getEnquiry_status() {
        return enquiry_status;
    }

    public void setEnquiry_status(String enquiry_status) {
        this.enquiry_status = enquiry_status;
    }

    public static boolean isSelected1() {
        return isSelected1;
    }

    public static void setSelected1(boolean selected1) {
        isSelected1 = selected1;
    }

    public  void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean isSelected() {
        return isSelected;
    }
}
