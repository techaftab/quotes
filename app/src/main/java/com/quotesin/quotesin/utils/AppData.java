package com.quotesin.quotesin.utils;

import java.util.ArrayList;

public class AppData {
    private static final AppData ourInstance = new AppData();
    private String replyflag,twId, twEmail, twPhotoUrl, twName, type, subsc_id, subsc_name, subsc_amt,
            eid, bname,qid, count , qamt,cname , upd_rej_flag, update_flag ;
    private ArrayList<String> list;
    private ArrayList<String> Checklist;
    private ArrayList<String> radiolist;

    private String qwonstatus , replyDots, feedbackflag;


    private String Feedbacktitle;


    public String getFeedbackflag() {
        return feedbackflag;
    }

    public void setFeedbackflag(String feedbackflag) {
        this.feedbackflag = feedbackflag;
    }

    public String getReplyDots() {
        return replyDots;
    }

    public void setReplyDots(String replyDots) {
        this.replyDots = replyDots;
    }

    public String getQwonstatus() {
        return qwonstatus;
    }

    public void setQwonstatus(String qwonstatus) {
        this.qwonstatus = qwonstatus;
    }

    public String getFeedbacktitle() {
        return Feedbacktitle;
    }

    public void setFeedbacktitle(String feedbacktitle) {
        Feedbacktitle = feedbacktitle;
    }

    public static AppData getInstance() {
        return ourInstance;
    }

    private AppData() {
    }

    public String getReplyflag() {
        return replyflag;
    }

    public void setReplyflag(String replyflag) {
        this.replyflag = replyflag;
    }

    public String getUpdate_flag() {
        return update_flag;
    }

    public void setUpdate_flag(String update_flag) {
        this.update_flag = update_flag;
    }

    public String getUpd_rej_flag() {
        return upd_rej_flag;
    }

    public void setUpd_rej_flag(String upd_rej_flag) {
        this.upd_rej_flag = upd_rej_flag;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public static AppData getOurInstance() {
        return ourInstance;
    }

    public String getTwId() {
        return twId;
    }

    public void setTwId(String twId) {
        this.twId = twId;
    }

    public String getTwEmail() {
        return twEmail;
    }

    public void setTwEmail(String twEmail) {
        this.twEmail = twEmail;
    }

    public String getTwPhotoUrl() {
        return twPhotoUrl;
    }

    public void setTwPhotoUrl(String twPhotoUrl) {
        this.twPhotoUrl = twPhotoUrl;
    }

    public String getTwName() {
        return twName;
    }

    public void setTwName(String twName) {
        this.twName = twName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubsc_id() {
        return subsc_id;
    }

    public void setSubsc_id(String subsc_id) {
        this.subsc_id = subsc_id;
    }

    public String getSubsc_name() {
        return subsc_name;
    }

    public void setSubsc_name(String subsc_name) {
        this.subsc_name = subsc_name;
    }

    public String getSubsc_amt() {
        return subsc_amt;
    }

    public void setSubsc_amt(String subsc_amt) {
        this.subsc_amt = subsc_amt;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public ArrayList<String> getRadiolist() {
        return radiolist;
    }

    public void setRadiolist(ArrayList<String> radiolist) {
        this.radiolist = radiolist;
    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public ArrayList<String> getChecklist() {
        return Checklist;
    }

    public void setChecklist(ArrayList<String> checklist) {
        Checklist = checklist;
    }

    public String getQamt() {
        return qamt;
    }

    public void setQamt(String qamt) {
        this.qamt = qamt;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
