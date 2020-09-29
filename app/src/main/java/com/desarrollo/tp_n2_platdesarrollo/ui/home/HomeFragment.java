package com.desarrollo.tp_n2_platdesarrollo.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.desarrollo.tp_n2_platdesarrollo.MainActivity;
import com.desarrollo.tp_n2_platdesarrollo.R;
import com.desarrollo.tp_n2_platdesarrollo.models.FabCustom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    private ProgressBar progressBar;
    private  SensorManager manager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


       // homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });
        */



        final TextView tMedicion = root.findViewById(R.id.tMedicion);
        /*
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tMedicion.setText(s);
            }
        });
         */

        progressBar = root.findViewById(R.id.progressBar);

        FabCustom fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new OnClickMedicion());
        return root;
    }

    private class OnClickMedicion implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            Timer task = new Timer();

            if(view.getClass() == FabCustom.class){

                FabCustom fab = (FabCustom) view;

                if (fab.isActivated()) { // desactiva el boton y ejecuta la tarea
                    fab.desactivar();
                    task.execute();

                } else { // activa el boton y finaliza la tarea
                    fab.activar();
                }


            }
        }
    }

    public  class Timer extends AsyncTask<Void, Integer, Boolean> implements SensorEventListener {

        Long firstTime;
        Long LastTime;
        Long tend;
        Long periodo;
        Sensor sensor;
        boolean flagUp;
        boolean flagDown;

        float medicionAnterior;
        float medicion;

        @Override
        protected Boolean doInBackground(Void... voids) {

            manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            firstTime = Long.valueOf(-1);


            manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST);

            periodo = 0L;
            while(true){

                if(flagUp && !flagDown){

                }

                Log.i("EVENTO", flagUp? "pulso positivo": "pulso bajo");
            }




          //  return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progreso = values[0].intValue();
            progressBar.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);


            // TODO: valores de prueba refactorizar despues
            flagUp = false;
            flagDown = false;

            medicionAnterior = -1;

        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {

            super.onPostExecute(aBoolean);
            Toast.makeText(getContext(),"Tarea Finalizada en " + tend , Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(),"Tarea Cancelada",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float medicion = sensorEvent.values[0];
            float dmax = sensorEvent.sensor.getMaximumRange();

            if(medicion != dmax) { // flag decendente
                flagDown =true;
                flagUp = false;
                medicionAnterior = medicion;

            }else if(medicionAnterior != dmax && medicion == dmax){ //flag acendente
                medicionAnterior = medicion;
                flagUp = true;
                flagDown = false;
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // no se usa ya que la presicion del sensor de proximidad no va a cambiar
        }
    }

}