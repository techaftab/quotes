package com.quotesin.quotesin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by manoj on 15/3/19.
 */
public class QuesAnsResponse {

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
        @SerializedName("ques_name")
        @Expose
        public String quesName;
        @SerializedName("ques_type")
        @Expose
        public String quesType;
        @SerializedName("ques_position")
        @Expose
        public String quesPosition;
        @SerializedName("service_id")
        @Expose
        public String serviceId;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("answers")
        @Expose
        public List<Answer> answers = null;

        public String answer = "";


        public class Answer {

            @SerializedName("id")
            @Expose
            public Integer id;
            @SerializedName("ques_id")
            @Expose
            public String quesId;
            @SerializedName("mul_name")
            @Expose
            public String mulName;
            @SerializedName("created_at")
            @Expose
            public String createdAt;
            @SerializedName("updated_at")
            @Expose
            public String updatedAt;

        }

    }


}
