package com.quotesin.quotesin.activities;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.utils.ProgressD;

public class How_Work extends AppCompatActivity {
    WebView mywebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how__work);

        mywebview = (WebView) findViewById(R.id.webView);
        mywebview.setWebViewClient(new myWebClient());

        mywebview.loadUrl("http://dev.webmobril.services/quotesin/how-it-works/");
    }

    public class myWebClient extends WebViewClient {
        ProgressD progressD;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressD = ProgressD.show(How_Work.this, "Connecting", false);
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            progressD = ProgressD.show(How_Work.this, "Connecting", false);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressD != null)
                progressD.dismiss();
            super.onPageFinished(view, url);
        }

    }
}
