package com.quotesin.quotesin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewEmailResponse {
    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("attachment")
    @Expose
    public List<Attachment> attachment = null;
    @SerializedName("Review")
    @Expose
    public Object review;
    @SerializedName("contact")
    @Expose
    public Object contact;



    public class Attachment {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("answer_question")
        @Expose
        public String answerQuestion;
        @SerializedName("answer_content")
        @Expose
        public String answerContent;
        @SerializedName("enquiry_id")
        @Expose
        public String enquiryId;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

    }

    public class Category {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("category_description")
        @Expose
        public String categoryDescription;
        @SerializedName("category_status")
        @Expose
        public String categoryStatus;
        @SerializedName("category_parent")
        @Expose
        public String categoryParent;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("created_at")
        @Expose
        public String createdAt;

    }


    public class Result {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("enquiry_title")
        @Expose
        public String enquiryTitle;
        @SerializedName("enquiry_description")
        @Expose
        public String enquiryDescription;
        @SerializedName("enquiry_min_quote")
        @Expose
        public Object enquiryMinQuote;
        @SerializedName("enquiry_max_quote")
        @Expose
        public Object enquiryMaxQuote;
        @SerializedName("enquiry_post_date")
        @Expose
        public String enquiryPostDate;
        @SerializedName("enquiry_expired")
        @Expose
        public String enquiryExpired;
        @SerializedName("enquiry_accepted_date")
        @Expose
        public Object enquiryAcceptedDate;
        @SerializedName("enquiry_status")
        @Expose
        public String enquiryStatus;
        @SerializedName("service_id")
        @Expose
        public String serviceId;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("enquiry_number_reply")
        @Expose
        public Object enquiryNumberReply;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("country_id")
        @Expose
        public String countryId;
        @SerializedName("state_id")
        @Expose
        public String stateId;
        @SerializedName("city_id")
        @Expose
        public Object cityId;
        @SerializedName("consumer_username")
        @Expose
        public String consumerUsername;
        @SerializedName("consumer_type")
        @Expose
        public Object consumerType;
        @SerializedName("consumer_email")
        @Expose
        public String consumerEmail;
        @SerializedName("consumer_invisible")
        @Expose
        public String consumerInvisible;
        @SerializedName("location_code")
        @Expose
        public String locationCode;
        @SerializedName("enquiry_postalcode")
        @Expose
        public String enquiryPostalcode;
        @SerializedName("enquiry_winner")
        @Expose
        public Object enquiryWinner;
        @SerializedName("business_invisible")
        @Expose
        public String businessInvisible;
        @SerializedName("quote_count")
        @Expose
        public String quoteCount;
        @SerializedName("enquiry_timestamp")
        @Expose
        public String enquiryTimestamp;
        @SerializedName("enquiry_Image")
        @Expose
        public Object enquiryImage;
        @SerializedName("inbox_invisible")
        @Expose
        public String inboxInvisible;
        @SerializedName("sent_invisible")
        @Expose
        public String sentInvisible;
        @SerializedName("history_invisible")
        @Expose
        public String historyInvisible;
        @SerializedName("act_code")
        @Expose
        public Object actCode;
        @SerializedName("enquiry_loco")
        @Expose
        public String enquiryLoco;
        @SerializedName("feedback_status")
        @Expose
        public String feedbackStatus;
        @SerializedName("email_varify")
        @Expose
        public String emailVarify;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("service")
        @Expose
        public Service service;
        @SerializedName("category")
        @Expose
        public Category category;
        @SerializedName("user")
        @Expose
        public User user;

    }


    public class Service {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("service_name")
        @Expose
        public String serviceName;
        @SerializedName("service_description")
        @Expose
        public String serviceDescription;
        @SerializedName("service_status")
        @Expose
        public String serviceStatus;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

    }


    public class User {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("business_username")
        @Expose
        public String businessUsername;
        @SerializedName("business_password")
        @Expose
        public String businessPassword;
        @SerializedName("password_txt")
        @Expose
        public String passwordTxt;
        @SerializedName("business_name")
        @Expose
        public Object businessName;
        @SerializedName("state_code")
        @Expose
        public String stateCode;
        @SerializedName("location_code")
        @Expose
        public String locationCode;
        @SerializedName("business_postcode")
        @Expose
        public Object businessPostcode;
        @SerializedName("business_address")
        @Expose
        public String businessAddress;
        @SerializedName("business_telephone")
        @Expose
        public Object businessTelephone;
        @SerializedName("business_evening_telephone")
        @Expose
        public Object businessEveningTelephone;
        @SerializedName("business_email")
        @Expose
        public String businessEmail;
        @SerializedName("business_website")
        @Expose
        public Object businessWebsite;
        @SerializedName("business_short_description")
        @Expose
        public Object businessShortDescription;
        @SerializedName("business_hearing")
        @Expose
        public Object businessHearing;
        @SerializedName("business_register_date")
        @Expose
        public Object businessRegisterDate;
        @SerializedName("business_expired")
        @Expose
        public Object businessExpired;
        @SerializedName("business_status")
        @Expose
        public Object businessStatus;
        @SerializedName("activation_code")
        @Expose
        public Object activationCode;
        @SerializedName("business_logo")
        @Expose
        public String businessLogo;
        @SerializedName("paid_fee")
        @Expose
        public Object paidFee;
        @SerializedName("paid_vat")
        @Expose
        public Object paidVat;
        @SerializedName("free_quotes")
        @Expose
        public Object freeQuotes;
        @SerializedName("operating_status")
        @Expose
        public String operatingStatus;
        @SerializedName("email_notification")
        @Expose
        public String emailNotification;
        @SerializedName("role_id")
        @Expose
        public String roleId;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("name")
        @Expose
        public String name;

    }
}
