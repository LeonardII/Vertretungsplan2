package com.satoaki.vertretungsplan;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Button;

/**
 * Created by Leonard on 17.01.2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.);
    }

}