package com.webs.michael_ray.meetingofminds;

import android.app.ActionBar;
import android.app.Activity;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
        if (servicesConnected()) {

        }
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
                            Dialog d = (Dialog)dialog;
                            String category = ((EditText)d.findViewById(R.id.cat_add)).getText().toString();
                            String name = ((EditText)d.findViewById(R.id.name_add)).getText().toString();
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

    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        break;
                }
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),"Location Updates");
            }
            return false;
        }
    }
}
