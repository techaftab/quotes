package com.quotesin.quotesin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.quotesin.quotesin.R;
import com.quotesin.quotesin.activities.welcome;
import com.quotesin.quotesin.model.BannerListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapterSlider extends PagerAdapter {

    private Context mContext;
    private List<BannerListResponse.Message> mResources;

    public ViewPagerAdapterSlider(welcome mContext, List<BannerListResponse.Message> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==  object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView imageView =  itemView.findViewById(R.id.img_pager_item);
        Picasso.get().load("http://dev.webmobril.services/quotesinapp/" + mResources.get(position).image).into(imageView);

        Log.e("image--","http://dev.webmobril.services/quotesinapp/"+mResources.get(position).image);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
