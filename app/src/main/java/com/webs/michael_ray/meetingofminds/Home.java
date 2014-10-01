package com.webs.michael_ray.meetingofminds;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.webs.michael_ray.meetingofminds.adapters.TabsAdapter;
import com.webs.michael_ray.meetingofminds.logic.DatabaseManager;

public class Home extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager mPager;
    private TabsAdapter mPagerAdapter;
    private ActionBar actionBar;
    private FragmentManager fm;
    private SharedPreferences prefs;

    private void managePageNavigation() {
        mPager = (ViewPager) findViewById(R.id.pager);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Sets an action bar with tabs
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.color

        fm = getSupportFragmentManager();

        mPagerAdapter = new TabsAdapter(fm);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(5);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) { actionBar.setSelectedNavigationItem(position); }
            public void onPageScrolled(int arg0, float arg1, int arg2) { }
            public void onPageScrollStateChanged(int arg0) { }
        });

        //Create the tabs and fragments
        ActionBar.Tab tab1 = actionBar.newTab().setText(getResources().getString(R.string.tab1));
        //tab1
        actionBar.addTab(tab1.setTabListener(this));
        ActionBar.Tab tab2 = actionBar.newTab().setText(getResources().getString(R.string.tab2));
        actionBar.addTab(tab2.setTabListener(this));
        ActionBar.Tab tab3 = actionBar.newTab().setText(getResources().getString(R.string.tab3));
        actionBar.addTab(tab3.setTabListener(this));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        managePageNavigation();
        login();
    }

    private void login() {
        try {
            int id = DatabaseManager.dm.authUser(prefs.getString("pref_username",""),prefs.getString("pref_password",""));
            Toast.makeText(this, "ID: " + Integer.toString(id), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
        }
    }

    public void addPoint(View view) {
        AddPoint dialog = new AddPoint();
        dialog.show(getFragmentManager(), "dialog");
    }

    public static class AddPoint extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dial_add, null))
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String category = ((EditText)getActivity().findViewById(R.id.cat_add)).getText().toString();
                            String name = ((EditText)getActivity().findViewById(R.id.name_add)).getText().toString();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AddPoint.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) { }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) { }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) { }
}
