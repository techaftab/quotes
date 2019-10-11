package com.quotesin.quotesin.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.utils.ProgressD;

/**
 * A simple {@link Fragment} subclass.
 */
public class webview extends Fragment {
    View view;
    WebView webView;
    ProgressD progressD;

    public webview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_webview, container, false);


        webView = view.findViewById(R.id.webView);

        Bundle b = getArguments();
        String url = b.getString("url");

        webView.setWebViewClient(new myWebClient());
        progressD = ProgressD.show(getActivity(), "Connecting", false);

        webView.loadUrl(url);


        return view;
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressD.dismiss();
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            progressD.dismiss();
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressD.dismiss();
            super.onPageFinished(view, url);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
