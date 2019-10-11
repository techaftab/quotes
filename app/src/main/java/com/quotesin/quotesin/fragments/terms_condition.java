package com.quotesin.quotesin.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.quotesin.quotesin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class terms_condition extends Fragment {
    View view;

    public terms_condition() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.terms_condition, container, false);
        WebView mywebview = view.findViewById(R.id.webView);
        mywebview.loadUrl("http://dev.webmobril.services/quotesin/terms-conditions/");

        return view;
    }

}
