package com.quotesin.quotesin.model;

import java.util.Collections;

public class QuotesDescModel implements Comparable<QuotesDescModel>{

    public String Q_id, e_id, business_username, quote_image, quote_price, quote_message, quote_date, quote_won, quote_won_date, quote_status, subject, q_price_type, q_occur, q_start, user_id;
    public String business_name, business_number, business_address, business_email, business_telephone, flag, business_logo, createDate;

    String haveQuote;


    public String getHaveQuote() {
        return haveQuote;
    }

    public void setHaveQuote(String haveQuote) {
        this.haveQuote = haveQuote;
    }

    Float newPrice;


    public Float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Float newPrice) {
        this.newPrice = newPrice;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getBusiness_logo() {
        return business_logo;
    }

    public void setBusiness_logo(String business_logo) {
        this.business_logo = business_logo;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_number() {
        return business_number;
    }

    public void setBusiness_number(String business_number) {
        this.business_number = business_number;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    public String getBusiness_email() {
        return business_email;
    }

    public void setBusiness_email(String business_email) {
        this.business_email = business_email;
    }

    public String getBusiness_telephone() {
        return business_telephone;
    }

    public void setBusiness_telephone(String business_telephone) {
        this.business_telephone = business_telephone;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getQ_id() {
        return Q_id;
    }

    public void setQ_id(String q_id) {
        Q_id = q_id;
    }

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getBusiness_username() {
        return business_username;
    }

    public void setBusiness_username(String business_username) {
        this.business_username = business_username;
    }

    public String getQuote_image() {
        return quote_image;
    }

    public void setQuote_image(String quote_image) {
        this.quote_image = quote_image;
    }

    public String getQuote_price() {
        return quote_price;
    }

    public void setQuote_price(String quote_price) {
        this.quote_price = quote_price;
    }

    public String getQuote_message() {
        return quote_message;
    }

    public void setQuote_message(String quote_message) {
        this.quote_message = quote_message;
    }

    public String getQuote_date() {
        return quote_date;
    }

    public void setQuote_date(String quote_date) {
        this.quote_date = quote_date;
    }

    public String getQuote_won() {
        return quote_won;
    }

    public void setQuote_won(String quote_won) {
        this.quote_won = quote_won;
    }

    public String getQuote_won_date() {
        return quote_won_date;
    }

    public void setQuote_won_date(String quote_won_date) {
        this.quote_won_date = quote_won_date;
    }

    public String getQuote_status() {
        return quote_status;
    }

    public void setQuote_status(String quote_status) {
        this.quote_status = quote_status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQ_price_type() {
        return q_price_type;
    }

    public void setQ_price_type(String q_price_type) {
        this.q_price_type = q_price_type;
    }

    public String getQ_occur() {
        return q_occur;
    }

    public void setQ_occur(String q_occur) {
        this.q_occur = q_occur;
    }

    public String getQ_start() {
        return q_start;
    }

    public void setQ_start(String q_start) {
        this.q_start = q_start;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    @Override
    public int compareTo(QuotesDescModel st) {
        if(newPrice==st.newPrice)
            return 0;
        else if(newPrice>st.newPrice)
            return 1;
        else
            return -1;
    }

}
