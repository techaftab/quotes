package com.quotesin.quotesin.utils;

public class APIUrl {
    public static String BASE_URL = "https://www.webmobril.org/dev/quotesinapp/api/";
    public static String IMAGE_BASE_URL = "https://www.webmobril.org/dev/quotesinapp/";

    public static String LOGIN = BASE_URL + "login";                                                //1
    public static String forget_password = BASE_URL + "forgot_password";                              //2
    public static String twitter_login = BASE_URL + "twitter_login";                                  //3
    public static String fb_login = BASE_URL + "fb_login";                                            //4
    public static String google_login = BASE_URL + "google_login";                                   //5
    public static String signup = BASE_URL + "signup";                                               //6
    public static String subscription_plan = BASE_URL + "subscription_plan";                         //7
    public static String Indox_Email = BASE_URL + "inbox_email";                                     //8
    public static String get_all_countries = BASE_URL + "country";                                   //9
    public static String get_all_state = BASE_URL + "state";                                         //10
    public static String get_all_city = BASE_URL + "city";                                          //11
    public static String GET_PROFILE = BASE_URL + "get_profile";                                    //12
    public static String View_Email = BASE_URL + "view_email";                                      //13
    public static String UPDATE_PROFILE = BASE_URL + "update_profile_pic";                          //14
    public static String GET_CATEGORY = BASE_URL + "get_service_categories";                        //15
    public static String Edit_Profile = BASE_URL + "update_profile";                                  //16
    public static String GET_SERVICES = BASE_URL + "get_services";                                  //17
    public static String POST_ENQUIRY = BASE_URL + "post_enquery";                                  //18
    public static String SEND_QUOTES = BASE_URL + "send_quote";                                     //19
    public static String RECIEVED_QUOTES = BASE_URL + "recieved_quotes";                            //20
    public static String sent_quotes_list = BASE_URL + "sent_quotes_list";                            //21
    public static String sent_enqueries = BASE_URL + "sent_enqueries";                            //22
    public static String Quotes_Details = BASE_URL + "quotes_details";                            //23
    public static String signup_second_step = BASE_URL + "signup_second_step";                            //24
    public static String view_feedback = BASE_URL + "view_form";                            //25
    public static String Get_Service_Ques = BASE_URL + "get_services_questions";
    public static String Reset_Password = BASE_URL + "reset_password";
    public static String Update_Send_Quote = BASE_URL + "update_send_quote";
    public static String consumer_quotes_details = BASE_URL + "consumer_quotes_details";
    public static String Filter_Read_Enquiry_List = BASE_URL + "read_enquiry_list";
    public static String attachment_enquiry_list = BASE_URL + "attachment_enquiry_list";
    public static String Delete_Enq = BASE_URL + "delete_enquiry";
    public static String Trash_List = BASE_URL + "delete_enquiry_list";
    public static String accept_quote = BASE_URL + "accept_quote";
    public static String reject_quote = BASE_URL + "reject_quote";
    public static String Accept_Quote_contact = BASE_URL + "accept_quote_contact";
    public static String logout = BASE_URL + "logout";
    public static String FEEDBACK_SUBMIT = BASE_URL + "feedback_submit";
    public static String CANCEL_ENQUIRY = BASE_URL + "cancle_enquiry";
    public static String COMMISSION_LISTING = BASE_URL + "commission_listing";

    public static String  commission_payment= BASE_URL+"commission_payment";
    public static String AFTER_TRANSACTION= BASE_URL+"transaction_details";
    public static String BANNER_IMAGE= BASE_URL+"slider_images";
    public static String Notification_Status=BASE_URL+"notification_status";
    public static String API_ADVERT=BASE_URL+"api_advert";
    public static String QUOTE_READ=BASE_URL+"quote_read";
}


//http://dev.webmobril.services/quotesinapp/api/login?username=varun&password=12345678
//http://dev.webmobril.services/quotesinapp/api/forgot_password?email=saurabh@webmobriltechnologies.com
//http://dev.webmobril.services/quotesinapp/api/twitter_login?twitter_id=123658&email=saurabhl@webmobriltechnologies.com&fullname=shubham&role_id=5
//http://dev.webmobril.services/quotesinapp/api/fb_login?fb_id=123658&email=saurabhl@webmobriltechnologies.com&fullname=shubham&username=shubham&role_id=5&profile_pic
//http://dev.webmobril.services/quotesinapp/api/google_login?gmailId=123658&gmailEmail=saurabhl@webmobriltechnologies.com&gmailName=shubham&role_id=5
//http://dev.webmobril.services/quotesinapp/api/signup?
//http://dev.webmobril.services/quotesinapp/api/inbox_email?location_id=1&role_id=5
//http://dev.webmobril.services/quotesinapp/api/subscription_plan?role_id=5
//http://dev.webmobril.services/quotesinapp/api/country
//http://dev.webmobril.services/quotesinapp/api/state?country_id=1
//http://dev.webmobril.services/quotesinapp/api/city?country_id=1&state_id=2
//http://dev.webmobril.services/quotesinapp/api/get_profile?user_id=250
//http://dev.webmobril.services/quotesinapp/api/view_email?location_id=71&email_id=1334
//http://dev.webmobril.services/quotesinapp/api/view_email?email_id=1502&review_status=0&user_name=emeriss
//http://dev.webmobril.services/quotesinapp/api/transaction_details?business_username=345&transaction_amount=2&transaction_id=5&transaction_date=2019-02-22&transaction_status=2&payer_email=6&subscription_type=9&role_id=2&intent=6


//http://dev.webmobril.services/quotesinapp/api/update_profile_pic?id=215&profile_img=abc.png
//http://dev.webmobril.services/quotesinapp/api/get_service_categories
//http://dev.webmobril.services/quotesinapp/api/get_services?catid=19
//http://dev.webmobril.services/quotesinapp/api/post_enquery?user_id=345&name=test&email=test@email.com&service_catid=55&service_id=3&deadline=2019-03-06&subject=test&message=test&yourlocation=noida&location_code=45
//http://dev.webmobril.services/quotesinapp/api/send_quote?enquiry_id=1334&business_username=test&q_price_type=dfds&q_occur=wd&q_start=df&quote_price=23&subject=dasd&message=test
//http://dev.webmobril.services/quotesinapp/api/recieved_quotes?user_id=298
//http://dev.webmobril.services/quotesinapp/api/sent_quotes_list
//http://dev.webmobril.services/quotesinapp/api/sent_enqueries?user_id=2
//http://dev.webmobril.services/quotesinapp/api/quotes_details?enq_id=1340
//http://dev.webmobril.services/quotesinapp/api/signup_second_step?user_id=345&role_id=2&category_id=5&service_id=5,6,9&country_code=2&state_code=6&location_code=9
//http://dev.webmobril.services/quotesinapp/api/view_feedback?user_name=emeriss
//http://dev.webmobril.services/quotesinapp/api/update_profile?id=433&business_name
//http://dev.webmobril.services/quotesinapp/api/get_services_questions?service_id=44
//http://dev.webmobril.services/quotesinapp/api/update_send_quote?enquiry_id=1334&business_username=test&q_price_type=fsdfsfsf&q_occur=wdsdadasdadasdas&q_start=dfdasdsad&quote_price=23&subject=dasd&message=test
//http://dev.webmobril.services/quotesinapp/api/cancle_enquiry?enquiry_id=1502

//http://dev.webmobril.services/quotesinapp/api/reset_password?user_id=489&old_password=1236547890&new_password=12365478
//http://dev.webmobril.services/quotesinapp/api/update_send_quote?enquiry_id=1334&business_username=test&q_price_type=fsdfsfsf&q_occur=wdsdadasdadasdas&q_start=dfdasdsad&quote_price=23&subject=dasd&message=test
//http://dev.webmobril.services/quotesinapp/api/consumer_quotes_details?enq_id=1815
//http://dev.webmobril.services/quotesinapp/api/read_enquiry_list?review_status=0&user_name=emeriss
//http://dev.webmobril.services/quotesinapp/api/attachment_enquiry_list?user_name=emeriss
//http://dev.webmobril.services/quotesinapp/api/delete_enquiry?user_name=emeriss&enquiry_id=1502&review_status=2
//http://dev.webmobril.services/quotesinapp/api/delete_enquiry_list?user_name=emeriss&review_status=2
//http://dev.webmobril.services/quotesinapp/api/reject_quote?quote_id=1256&quote_invisible=1
//http://dev.webmobril.services/quotesinapp/api/accept_quote_contact?name=sonal&address=sjdjaskd&postal_code=123654&email=sona@gmail.com&phone=123456789&alter_phone=987654321&username=Bsonal&enq_id=1265&role_id=2
//http://dev.webmobril.services/quotesinapp/api/logout?id=550
//http://dev.webmobril.services/quotesinapp/api/accept_quote?quote_id=1216&quote_status=1&quote_amount=99&business_username=dsdsa&consumer_username=sdsada
//http://dev.webmobril.services/quotesinapp/api/feedback_submit?quote_id=123&left_for=emeriss&pnn=1&q1=6&q2=8&q3=2&comment=sdada&user_id=sam&consumer_type=business
//http://dev.webmobril.services/quotesinapp/api/commission_listing?business_username=emeriss
//http://dev.webmobril.services/quotesinapp/api/slider_images
//http://dev.webmobril.services/quotesinapp/api/commission_payment?business_id=345&payment_status=Pending&payer_email=&first_name=&last_name=&address_name=&address_street=&address_city=&address_zip=&txn_id=sadsadsad&mc_gross=122&payment_gross=122&pending_reason=&payment_date=asds&role_id=2&
//http://dev.webmobril.services/quotesinapp/api/api_advert?location_code=1,2,3&role_id=5

//http://dev.webmobril.services/quotesinapp/api/quote_read?quote_id=1062