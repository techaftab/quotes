package com.quotesin.quotesin.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.quotesin.quotesin.R;
import com.quotesin.quotesin.adapter.ServiceQues_Adapter;
import com.quotesin.quotesin.fragments.post_new_enquiry;
import com.quotesin.quotesin.model.QuesAnsResponse;
import com.quotesin.quotesin.utils.APIUrl;
import com.quotesin.quotesin.utils.CommonMethod;
import com.quotesin.quotesin.utils.CustomHttpClient;
import com.quotesin.quotesin.utils.ProgressD;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.quotesin.quotesin.utils.CommonMethod.hideSoftKeyboard;

public class ServiceQuestions extends AppCompatActivity {

    public static ArrayList<String> questionList = new ArrayList<>();
    public static ArrayList<String> answerList = new ArrayList<>();
    ImageView ivCancel;

    public static RecyclerView recyclerview;
    public static QuesAnsResponse quesAnsResponse;

    Button btnAttach;
    String TAG = this.getClass().getName();
    String servicesId;
    ServiceQues_Adapter serviceQuesAdapter;
    @SuppressLint("StaticFieldLeak")
    public static ServiceQuestions base;
    public static String qFlag1 = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_questions);
        base = ServiceQuestions.this;

        recyclerview = findViewById(R.id.recyclerview);
        ivCancel = findViewById(R.id.ivCancel);
        btnAttach = findViewById(R.id.btnAttach);


        ivCancel.setOnClickListener(v -> finish());

        btnAttach.setOnClickListener(v -> {
            questionList.clear();
            answerList.clear();

            for (int i = 0; i < quesAnsResponse.result.size(); i++) {
                questionList.add(quesAnsResponse.result.get(i).quesName.replace(",", "/"));
                if (!TextUtils.isEmpty(quesAnsResponse.result.get(i).answer))
                    answerList.add(quesAnsResponse.result.get(i).answer.replace(",", "/"));
            }

            Log.e("QuestionList--", questionList.toString().replace("[", "").replace("]", ""));
            Log.e("AnswerList--", answerList.toString().replace("[", "").replace("]", ""));

            Log.e("QuestionList SIZE--", String.valueOf(questionList.size()));
            Log.e("AnswerList SIZE--", String.valueOf(answerList.size()));


            if (!answerList.contains(null)) {
                post_new_enquiry.answerList1 = answerList.toString().replace("[", "").replace("]", "");
                post_new_enquiry.questionList1 = questionList.toString().replace("[", "").replace("]", "");
                finish();
                qFlag1 = "1";
            } else {
                Toast.makeText(ServiceQuestions.this, "Enter all field", Toast.LENGTH_SHORT).show();
            }
        });

        servicesId = getIntent().getStringExtra("selectedServicesIds");

        if (CommonMethod.isNetworkAvailable(ServiceQuestions.this)) {
            hideSoftKeyboard(ServiceQuestions.this);
            new GetServiceQuesAsyncTask().execute();
        } else {
            Toast.makeText(ServiceQuestions.this, "Check internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class GetServiceQuesAsyncTask extends AsyncTask<String, Void, String> {
        ProgressD mProgressD;
        JSONObject jObject;
        private String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressD = ProgressD.show(ServiceQuestions.this, "Connecting", false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url1 = APIUrl.Get_Service_Ques + "?service_id=" + servicesId;
                result = CustomHttpClient.executeHttpGet(url1);
                System.out.print(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("service quest--", "get service quest api response is " + result);
            if (result == null) {
                Toast.makeText(ServiceQuestions.this, "Please check your Internet.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    quesAnsResponse = new Gson().fromJson(result, QuesAnsResponse.class);

                    if (quesAnsResponse.status.equals("Success")) {
                        if (quesAnsResponse.result != null) {
                            serviceQuesAdapter = new ServiceQues_Adapter(quesAnsResponse.result);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ServiceQuestions.this);
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            recyclerview.setLayoutManager(layoutManager);
                            recyclerview.setAdapter(serviceQuesAdapter);
                            recyclerview.setHasFixedSize(true);
                            serviceQuesAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mProgressD.dismiss();
            }
        }
    }


    /*@Override
    public void onBackPressed() {
      //  super.onBackPressed();
    }*/
}

