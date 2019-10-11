package com.quotesin.quotesin.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quotesin.quotesin.R;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button btnCreateAcc;
    String TAG = this.getClass().getSimpleName();
    ImageView tw_btn, tvGoogle;
    TextView tvSignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initViews();
        registerClickListner();
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    private void initViews() {
        tvGoogle =  findViewById(R.id.tvGoogle);
        btnCreateAcc =  findViewById(R.id.btnCreateAcc);
        tw_btn = findViewById(R.id.tw_btn);
        tvSignin= findViewById(R.id.tvSignin);

    }

    private void registerClickListner() {
        tvGoogle.setOnClickListener(this);
        tw_btn.setOnClickListener(this);
        btnCreateAcc.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateAcc:
                Intent intent1 = new Intent(CreateAccount.this, CreateAccountType.class);
                intent1.putExtra("normal", "1");
                intent1.putExtra("gm", "0");
                intent1.putExtra("tw", "0");
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                break;

            case R.id.tvGoogle:
                Intent intent = new Intent(CreateAccount.this, CreateAccountType.class);
                intent.putExtra("normal", "0");
                intent.putExtra("gm", "1");
                intent.putExtra("tw","0");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);

                break;

            case R.id.tw_btn:
                Intent tw = new Intent(CreateAccount.this, CreateAccountType.class);
                tw.putExtra("normal", "0");
                tw.putExtra("gm", "0");
                tw.putExtra("tw", "1");
                startActivity(tw);
                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);

                break;

            case R.id.tvSignin:
                Intent intent2= new  Intent(CreateAccount.this,Login.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
                break;
        }
    }

    private void startIntent(Class<?> cls) {
        Intent intent = new Intent(CreateAccount.this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.slide_in, R.anim.slider_out);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
