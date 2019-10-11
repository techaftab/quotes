package com.quotesin.quotesin.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quotesin.quotesin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class contact_admin extends Fragment {
    View view;

    public contact_admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.contact_admin, container, false);

        return view;
    }

}
