package com.quotesin.quotesin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class adsResponse {
    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("advert")
    @Expose
    public List<Advert> advert = null;


    public class Advert {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("advert_name")
        @Expose
        public String advertName;
        @SerializedName("advert_image")
        @Expose
        public String advertImage;
        @SerializedName("advert_url")
        @Expose
        public String advertUrl;
        @SerializedName("advert_mobile_url")
        @Expose
        public String advertMobileUrl;
        @SerializedName("advert_status")
        @Expose
        public String advertStatus;
        @SerializedName("location_code")
        @Expose
        public String locationCode;
        @SerializedName("advert_type")
        @Expose
        public String advertType;
        @SerializedName("advert_text")
        @Expose
        public String advertText;
        @SerializedName("advert_expiry")
        @Expose
        public String advertExpiry;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

    }

}
