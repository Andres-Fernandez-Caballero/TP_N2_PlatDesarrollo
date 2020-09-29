package com.desarrollo.tp_n2_platdesarrollo.ui.home;

import android.content.Context;
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
import androidx.fragment.app.Fragment;

import com.desarrollo.tp_n2_platdesarrollo.R;
import com.desarrollo.tp_n2_platdesarrollo.models.FabCustom;
import com.desarrollo.tp_n2_platdesarrollo.models.SensorTrigger;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    private ProgressBar progressBar;
    private FabCustom fab;

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
        SensorManager manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        float maxRange = sensor.getMaximumRange();
        tMedicion.setText(getString(R.string.perdiodo, maxRange) );
        /*
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tMedicion.setText(s);
            }
        });
         */

        progressBar = root.findViewById(R.id.progressBar);

        fab = root.findViewById(R.id.fab);

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

    public  class Timer extends AsyncTask<Void, Integer, Long> {

        Long firstTime;
        Long lastTime;
        Long periodo;
        Sensor sensor;


        @Override
        protected Long doInBackground(Void... voids) {

            SensorManager manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);



            SensorTrigger sensorTrigger = new SensorTrigger();

            manager.registerListener(sensorTrigger,sensor,SensorManager.SENSOR_DELAY_FASTEST);

            Log.i("ENTRADA","Entro al evento");

            while(lastTime == null){

                if(sensorTrigger.isTrigger() && firstTime == null ){
                    firstTime = System.currentTimeMillis();
                    sensorTrigger.resetTrigger();
                    publishProgress(50);
                }else if(sensorTrigger.isTrigger() && firstTime != null){
                    lastTime = System.currentTimeMillis();
                    sensorTrigger.resetTrigger();
                    publishProgress(100);

                    periodo = lastTime - firstTime;
                    fab.activar();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
            return periodo;
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

            firstTime = null;
            lastTime = null;
            periodo = null;
        }


        @Override
        protected void onPostExecute(Long aLong) {

            super.onPostExecute(aLong);
            Toast.makeText(getContext(),"medio periodo: " + aLong + "miliseg" , Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(),"Tarea Cancelada",Toast.LENGTH_LONG).show();
        }

    }

}