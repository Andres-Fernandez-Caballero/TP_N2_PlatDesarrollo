package com.desarrollo.tp_n2_platdesarrollo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desarrollo.tp_n2_platdesarrollo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_medicion_sin_sensor extends Fragment {

    public Fragment_medicion_sin_sensor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medicion_sin_sensor, container, false);
    }
}