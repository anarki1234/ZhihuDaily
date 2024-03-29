package com.kevin.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

import com.baidu.mobstat.StatService;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.ZhihuDailyApplication;

public class MainActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Drawable mActionBarDrawable;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;
    private String TAG = "MainActivity";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_main);

        // 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
        StatService.setDebugOn(false);

        mTitle = mDrawerTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mMenuTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setIcon(R.drawable.topbar_icon);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        mActionBarDrawable = new ColorDrawable(Color.parseColor("#ff33b5e5"));
        mActionBarDrawable.setAlpha(0);
        //        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getActionBar().setBackgroundDrawable(mActionBarDrawable);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarDrawable.setCallback(mDrawableCallback);
        }

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
        mDrawerLayout, /* DrawerLayout object */
        R.drawable.ic_navigation_drawer, /* nav drawer image to replace 'Up' caret */
        R.string.drawer_open, /* "open drawer" description for accessibility */
        R.string.drawer_close /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                                         // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                                         // onPrepareOptionsMenu()
            }

            public void onDrawerSlide(View drawerView, float slideOffet) {
                int alpha = 0;
                alpha = (int) (slideOffet * 255);
                setActionBarAlpha(alpha);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // TODO set
            gotoHomePage();
        }

        // check network info
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                ZhihuDailyApplication.sIsConnected = true;
            } else {
                ZhihuDailyApplication.sIsConnected = false;
            }
            ZhihuDailyApplication.sNetworkType = networkInfo.getType();
        }

        //        // init database
        //        DataBaseManager.newInstance(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //        DataBaseManager.getInstance().closeDB();
        //        DataCache.getInstance().clearAllCache();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //        MenuInflater inflater = getMenuInflater();
        //        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
        //        case R.id.action_refresh:
        //            // create intent to perform web search for this planet
        //            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        //            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
        //            // catch event that there's no activity to handle intent
        //            if (intent.resolveActivity(getPackageManager()) != null) {
        //                startActivity(intent);
        //            } else {
        //                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
        //            }
        //            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("deprecation")
    private class DrawerScrollListener implements OnDrawerScrollListener {

        @Override
        public void onScrollEnded() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onScrollStarted() {
            // TODO Auto-generated method stub

        }

    }

    @SuppressLint("NewApi")
    private class DragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch (position) {
        case 0:
            gotoSettingsPage();
            break;
        case 1:
            gotoAboutPage();
            break;
        default:
            break;
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        //        if (position != 0) {
        //            setTitle(mMenuTitles[position]);
        //        }
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    private void gotoHomePage() {
        // update the main content by replacing fragments
        Fragment fragment = new NewsListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void gotoSettingsPage() {
        Intent intent = new Intent(ZhihuDailyApplication.getInstance().getApplicationContext(), SettingActivity.class);
        startActivity(intent);
    }

    private void gotoAboutPage() {
        Intent intent = new Intent(ZhihuDailyApplication.getInstance().getApplicationContext(), AboutActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @SuppressLint("NewApi")
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    public void setActionBarAlpha(int alpha) {
        mActionBarDrawable.setAlpha(alpha);
    }
}
