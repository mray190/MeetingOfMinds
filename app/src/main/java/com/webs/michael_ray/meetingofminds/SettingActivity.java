package com.webs.michael_ray.meetingofminds;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by mray on 30/09/14.
 */
public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}