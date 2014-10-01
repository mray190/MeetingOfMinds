package com.webs.michael_ray.meetingofminds;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.webs.michael_ray.meetingofminds.R;

/**
 * Created by mray on 30/09/14.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}