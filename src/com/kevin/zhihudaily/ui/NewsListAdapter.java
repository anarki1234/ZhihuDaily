package com.kevin.zhihudaily.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;
import com.kevin.zhihudaily.ui.PinnedSectionListView.PinnedSectionListAdapter;
import com.squareup.picasso.Picasso;

public class NewsListAdapter extends BaseAdapter implements PinnedSectionListAdapter {

    private static final String TAG = "NewsListAdapter";
    private WeakReference<Context> mContextWR;
    private List<ListItem> mItemList = null;

    static class ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;
        public TextView dateTextView;
    }

    static class ListItem {
        public static final int ITEM = 0;
        public static final int SECTION = 1;

        public final int type;
        public final String section;
        public final int sectionSize;
        public final NewsModel model;
        public final int indexOfDay;
        public final String date;

        public ListItem(int type, NewsModel model, String section, int size, int index, String date) {
            this.type = type;
            this.model = model;
            this.section = section;
            this.sectionSize = size;
            this.indexOfDay = index;
            this.date = date;
        }

        public NewsModel getModel() {
            return model;
        }

        public int getType() {
            return type;
        }

        public String getSection() {
            return section;
        }

        public int getSectionSize() {
            return sectionSize;
        }

        public int getIndexOfDay() {
            return indexOfDay;
        }

        public String getDate() {
            return date;
        }

    }

    public NewsListAdapter(Context context) {
        this.mContextWR = new WeakReference<Context>(context);
        mItemList = new ArrayList<ListItem>();
    }

    public void updateList(DailyNewsModel dailyModel) {
        if (dailyModel != null) {
            List<NewsModel> newsList = dailyModel.getNewsList();
            String date = dailyModel.getDate();
            int len = newsList.size();

            ListItem section = new ListItem(ListItem.SECTION, null, dailyModel.getDisplay_date(), len, -1, date);
            mItemList.add(section);

            for (int j = 0; j < len; j++) {
                ListItem item = new ListItem(ListItem.ITEM, newsList.get(j), null, len, j, date);
                Log.e(TAG, "==item[" + j + "]" + "=title=" + newsList.get(j).getTitle());
                mItemList.add(item);
            }
        }

        notifyDataSetChanged();
    }

    public void updateAllList(List<DailyNewsModel> list) {
        if (list != null) {
            // clear all
            mItemList.clear();

            int size = list.size();
            for (int i = 0; i < size; i++) {
                DailyNewsModel dailyModel = list.get(i);
                List<NewsModel> newsList = dailyModel.getNewsList();
                String date = dailyModel.getDate();
                int len = newsList.size();

                ListItem section = new ListItem(ListItem.SECTION, null, dailyModel.getDisplay_date(), len, -1, date);
                mItemList.add(section);

                for (int j = 0; j < len; j++) {
                    ListItem item = new ListItem(ListItem.ITEM, newsList.get(j), null, len, j, date);
                    mItemList.add(item);
                }

            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mItemList.get(position);
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

            ListItem item = mItemList.get(position);
            if (item.getType() == ListItem.ITEM) {
                convertView = LayoutInflater.from(mContextWR.get()).inflate(R.layout.news_list_item, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.iv_thumbnai);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                convertView = LayoutInflater.from(mContextWR.get()).inflate(R.layout.news_list_section, null);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.tv_section_title);
                convertView.setTag(holder);
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // setup list item info
        setupItemView(holder, position);

        return convertView;
    }

    private void setupItemView(ViewHolder holder, int position) {
        if (mItemList == null || mItemList.size() == 0) {
            return;
        }
        ListItem item = mItemList.get(position);

        if (item.getType() == ListItem.ITEM) {
            NewsModel model = item.getModel();
            holder.titleTextView.setText(model.getTitle());

            Picasso.with(mContextWR.get()).load(model.getThumbnail()).placeholder(R.drawable.spinner_76_inner_holo)
                    .fit().into(holder.imageView);
        } else {
            holder.titleTextView.setText(item.getSection());
        }

    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        //        return super.getItemViewType(position);
        return ((ListItem) (getItem(position))).type;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        // TODO Auto-generated method stub
        return viewType == ListItem.SECTION;
    }
}
