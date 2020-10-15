package com.desarrollo.tp_n2_platdesarrollo.ui.configuracion;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.desarrollo.tp_n2_platdesarrollo.R;

public class fragment_configuraciones extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}