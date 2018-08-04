package com.oneawadhcenter.halwits;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> img=new ArrayList<String>();


    public CustomPagerAdapter(Context context,ArrayList<String> image) {
        mContext = context;
        img=image;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==  object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        mLayoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.pager_item,null);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        imageView.setImageResource(R.drawable.gola2);
        Picasso.with(mContext)
                .load("https:"+img.get(position))
                .placeholder(R.drawable.gola2)
                .into(imageView);


        ViewPager vp=(ViewPager)container;
        vp.addView(itemView,0);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp=(ViewPager)container;
        View v=(View)object;
        vp.removeView(v);
    }
}