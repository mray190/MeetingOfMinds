package com.webs.michael_ray.meetingofminds;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
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
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.webs.michael_ray.meetingofminds.adapters.TabsAdapter;
import com.webs.michael_ray.meetingofminds.logic.DatabaseManager;
import com.webs.michael_ray.meetingofminds.logic.Point;

public class Home extends FragmentActivity implements LocationListener, ActionBar.TabListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private ViewPager mPager;
    private TabsAdapter mPagerAdapter;
    private ActionBar actionBar;
    private FragmentManager fm;
    private SharedPreferences prefs;
    private LocationClient mLocationClient;
    private Location currentLoc;
    private int userID;

    private void managePageNavigation() {
        mPager = (ViewPager) findViewById(R.id.pager);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Sets an action bar with tabs
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.color

        mLocationClient = new LocationClient(this, this, this);

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
        userID = 0;
        Login login = new Login(this);
        login.execute();
    }

    private class Login extends AsyncTask<Void, Integer, Void> {
        private Context context;
        public Login(Context context) {
            this.context = context;
        }
        @Override
        protected Void doInBackground(Void...params) {
            try {
                int id = DatabaseManager.dm.authUser(prefs.getString("pref_username",""),prefs.getString("pref_password",""));
                publishProgress(id);
            } catch (Exception e) {
                e.printStackTrace();
                publishProgress(-1);
            }
            return null;
        }
        protected void onProgressUpdate(Integer...progress) {
            userID = progress[0];
            Toast.makeText(context, "ID: " + Integer.toString(progress[0]), Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onPostExecute(Void result) {
        }
    }

    private static class InsertPoint extends AsyncTask<Void, Integer, Void> {
        private Context context;
        public InsertPoint(Context context) {
            this.context = context;
        }
        @Override
        protected Void doInBackground(Void...params) {

            return null;
        }
        protected void onProgressUpdate(Integer...progress) {

        }
        @Override
        protected void onPostExecute(Void result) {
        }
    }

    private void updateLists() {
        ((NearFragment) mPagerAdapter.getRegisteredFragment(0)).updateList(currentLoc);
        ((FavoritesFragment) mPagerAdapter.getRegisteredFragment(2)).updateList(userID);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLoc = location;
        updateLists();
    }

    public void addPoint(View view) {
        AddPoint dialog = new AddPoint();
        Location location = null;
        if (servicesConnected())
            location = mLocationClient.getLastLocation();
        dialog.insertLocation(location,userID);
        dialog.show(getFragmentManager(), "dialog");
    }


    public static class AddPoint extends DialogFragment {
        private Location location;
        private int userID;
        public void insertLocation(Location location, int userID) {
            this.userID = userID;
            this.location = location;
        }
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
                            Point point = new Point(userID,category,name,location.getLatitude(),location.getLongitude());
                            InsertPoint addPoint = new InsertPoint(getActivity());
                            addPoint.execute();
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
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        currentLoc = mLocationClient.getLastLocation();
        updateLists();
    }

    @Override
    public void onDisconnected() { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

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
        } else if (id== R.id.action_map) {
            Intent intent = new Intent(this, Maps.class);
            intent.putExtra("latitude",currentLoc.getLatitude());
            intent.putExtra("longitude",currentLoc.getLongitude());
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
