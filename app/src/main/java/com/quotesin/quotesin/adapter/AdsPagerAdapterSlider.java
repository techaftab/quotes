package com.quotesin.quotesin.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.fragments.webview;
import com.quotesin.quotesin.model.adsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdsPagerAdapterSlider extends PagerAdapter {

    private FragmentActivity mContext;
    private List<adsResponse.Advert> mResources;



    public AdsPagerAdapterSlider(FragmentActivity activity, List<adsResponse.Advert> result) {
        this.mContext = activity;
        this.mResources = result;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.ads_pageritem, container, false);

        ImageView imageView =  itemView.findViewById(R.id.img_pager_item);
        Picasso.get().load("http://dev.webmobril.services/quotesinapp/" + mResources.get(position).advertMobileUrl).into(imageView);

        Log.e("image--","http://dev.webmobril.services/quotesinapp/"+mResources.get(position).advertMobileUrl);

        container.addView(itemView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview webviewFrag0 = new webview();
                webViewFragment(webviewFrag0, "", "22",mResources.get(position).advertUrl, "user_id");
            }
        });

        return itemView;
    }

    private void webViewFragment(webview webviewFrag0, String s, String s1, String advertUrl, String user_id) {
        FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        transaction.replace(R.id.flContent, webviewFrag0);
        transaction.detach(webviewFrag0);
        transaction.attach(webviewFrag0);
        transaction.addToBackStack(null);
        mContext.setTitle(s);
        bundle.putString("url", advertUrl);
        webviewFrag0.setArguments(bundle);
        transaction.commit();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }



}

