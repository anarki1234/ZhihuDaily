package com.kevin.zhihudaily.ui;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.zhihudaily.R;

public class TopStoryAdapter extends BaseAdapter {

    private WeakReference<Context> mContextWR;

    static class ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;
    }

    public TopStoryAdapter(Context context) {
        this.mContextWR = new WeakReference<Context>(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContextWR.get()).inflate(R.layout.fragment_top_story, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_story_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set up viewflow item
        setupFlowView(holder, position);

        return convertView;
    }

    private void setupFlowView(ViewHolder holder, int position) {

    }
}
