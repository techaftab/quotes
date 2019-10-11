package com.quotesin.quotesin.model;

import java.util.ArrayList;

public class ServiceQues_Model {
    public  String qid,ques_name,ques_type,ques_position,service_id,aid,mul_name, chck, txtView,type;

    public ServiceQues_Model(String type) {
        this.type = type;
    }

    public ServiceQues_Model() {

    }


    public ArrayList<InnerServiceQues_Model> innerServiceQuesModelArrayList() {
        return innerServiceQuesModelArrayList;
    }

    public void setServiceModellist(ArrayList<InnerServiceQues_Model> innerServiceQuesModelArrayList) {
        this.innerServiceQuesModelArrayList = innerServiceQuesModelArrayList;
    }

    private ArrayList<InnerServiceQues_Model> innerServiceQuesModelArrayList;




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChck() {
        return chck;
    }

    public void setChck(String chck) {
        this.chck = chck;
    }

    public String getTxtView() {
        return txtView;
    }

    public void setTxtView(String txtView) {
        this.txtView = txtView;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQues_name() {
        return ques_name;
    }

    public void setQues_name(String ques_name) {
        this.ques_name = ques_name;
    }

    public String getQues_type() {
        return ques_type;
    }

    public void setQues_type(String ques_type) {
        this.ques_type = ques_type;
    }

    public String getQues_position() {
        return ques_position;
    }

    public void setQues_position(String ques_position) {
        this.ques_position = ques_position;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getMul_name() {
        return mul_name;
    }

    public void setMul_name(String mul_name) {
        this.mul_name = mul_name;
    }


}
