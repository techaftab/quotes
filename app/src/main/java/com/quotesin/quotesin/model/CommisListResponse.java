package com.quotesin.quotesin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommisListResponse {
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
    public List<Result> result = null;


    public class Result {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("quote_id")
        @Expose
        public String quoteId;
        @SerializedName("accept_date")
        @Expose
        public String acceptDate;
        @SerializedName("commission_amount")
        @Expose
        public String commissionAmount;
        @SerializedName("reminder_date")
        @Expose
        public String reminderDate;
        @SerializedName("business_username")
        @Expose
        public String businessUsername;
        @SerializedName("consumer_username")
        @Expose
        public String consumerUsername;
        @SerializedName("service_status")
        @Expose
        public String serviceStatus;
        @SerializedName("paid_status")
        @Expose
        public String paidStatus;
        @SerializedName("paid_date")
        @Expose
        public Object paidDate;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("quote")
        @Expose
        public Quote quote;

    }
    public class Enquiry {
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
        public String enquiryMinQuote;
        @SerializedName("enquiry_max_quote")
        @Expose
        public String enquiryMaxQuote;
        @SerializedName("enquiry_post_date")
        @Expose
        public String enquiryPostDate;
        @SerializedName("enquiry_expired")
        @Expose
        public String enquiryExpired;
        @SerializedName("enquiry_accepted_date")
        @Expose
        public String enquiryAcceptedDate;
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
        public String enquiryNumberReply;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("country_id")
        @Expose
        public Object countryId;
        @SerializedName("state_id")
        @Expose
        public Object stateId;
        @SerializedName("city_id")
        @Expose
        public Object cityId;
        @SerializedName("consumer_username")
        @Expose
        public String consumerUsername;
        @SerializedName("consumer_type")
        @Expose
        public String consumerType;
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
        public String enquiryWinner;
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
        public String enquiryImage;
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
        public String actCode;
        @SerializedName("enquiry_loco")
        @Expose
        public String enquiryLoco;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

    }

    public class Quote {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("enquiry_id")
        @Expose
        public String enquiryId;
        @SerializedName("business_username")
        @Expose
        public String businessUsername;
        @SerializedName("quote_image")
        @Expose
        public String quoteImage;
        @SerializedName("quote_price")
        @Expose
        public String quotePrice;
        @SerializedName("quote_message")
        @Expose
        public String quoteMessage;
        @SerializedName("quote_date")
        @Expose
        public String quoteDate;
        @SerializedName("quote_won")
        @Expose
        public String quoteWon;

        @SerializedName("quote_status")
        @Expose
        public String quoteStatus;
        @SerializedName("quote_invisible")
        @Expose
        public String quoteInvisible;
        @SerializedName("subject")
        @Expose
        public String subject;

        @SerializedName("user_id")
        @Expose
        public String userId;

        @SerializedName("enquiry")
        @Expose
        public Enquiry enquiry;

    }

}
