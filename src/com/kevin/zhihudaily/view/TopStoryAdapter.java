package com.kevin.zhihudaily.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.model.NewsModel;
import com.kevin.zhihudaily.ui.NewsDetailActivity;
import com.squareup.picasso.Picasso;

public class TopStoryAdapter extends BaseAdapter {
    private String TAG = "TopStoryAdapter";
    private WeakReference<Context> mContextWR;
    private List<NewsModel> mDataList;

    static class ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;
    }

    public TopStoryAdapter(Context context) {
        this.mContextWR = new WeakReference<Context>(context);
        mDataList = new ArrayList<NewsModel>();
    }

    public void updateList(List<NewsModel> list) {
        if (list == null) {
            return;
        }

        mDataList.addAll(list);

        // notify ui
        notifyDataSetChanged();

    }

    public void updateAllList(List<NewsModel> list) {
        if (list == null) {
            return;
        }

        // clear all data
        mDataList.clear();

        mDataList.addAll(list);

        // notify ui
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDataList.size() > 5 ? mDataList.size() : 5;
        //		return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position < mDataList.size()) {
            return mDataList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContextWR.get()).inflate(R.layout.fragment_top_story, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_story_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.tv_news_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set up viewflow item
        setupFlowView(holder, position);

        return convertView;
    }

    private void setupFlowView(ViewHolder holder, int position) {
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }

        NewsModel model = mDataList.get(position);
        holder.imageView.setOnClickListener(mClickListener);
        holder.imageView.setTag(position);
        Picasso.with(mContextWR.get()).load(model.getImage()).placeholder(R.drawable.image_top_default).fit()
                .centerCrop().into(holder.imageView);

        holder.titleTextView.setText(model.getTitle());
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int position = (Integer) v.getTag();
            NewsModel model = mDataList.get(position);
            Intent intent = new Intent(mContextWR.get(), NewsDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID, model.getId());
            intent.putExtra(Constants.INTENT_NEWS_DATE, model.getDate());
            //            Log.d(TAG, "==id=" + model.getId() + "  date=" + model.getDate() + "  pos=" + position);

            mContextWR.get().startActivity(intent);
        }
    };

}
