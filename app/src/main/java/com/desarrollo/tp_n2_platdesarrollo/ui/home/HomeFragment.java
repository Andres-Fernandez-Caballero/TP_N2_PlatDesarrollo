package com.desarrollo.tp_n2_platdesarrollo.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.desarrollo.tp_n2_platdesarrollo.R;
import com.desarrollo.tp_n2_platdesarrollo.fragments.Fragment_medicion_sin_resultados;
import com.desarrollo.tp_n2_platdesarrollo.fragments.Fragment_medicion_sin_sensor;
import com.desarrollo.tp_n2_platdesarrollo.models.FabCustom;
import com.desarrollo.tp_n2_platdesarrollo.models.SensorTrigger;

public class HomeFragment extends Fragment {

    /* valores por defecto de las mediciones no son los valores de la configuracion estan para evitar nulls... */
    private static final String MUESTRAS_ESTABLECIDAS = "3";
    private static final String NUMERO_ASPAS_ESTABLECIDAS = "2";

    //manager de fragmentos
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    // manager de sensor
    SensorManager sensorManager;

    // sensor de proximidad
    Sensor sensorProximidad;


    //private HomeViewModel homeViewModel;

    // elementos de la intefaz visual
    private ProgressBar progressBar;
    private FabCustom fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // obtengo una instancia de sensormanager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        // defino un sensor de tipo proximidad si no existe devuelve null
        sensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        fragmentTransaction = fragmentManager.beginTransaction();
        if (sensorProximidad != null) {
            // si el dispositivo tiene sensor de proximidad muestra la pantalla normal
            fragmentTransaction.add(R.id.fragment_contenedor_resultados, new Fragment_medicion_sin_resultados());
        } else {
            // si el dispositivo no tiene sensor de proximidad muestra el fragment de no disponible
            fragmentTransaction.add(R.id.fragment_contenedor_resultados, new Fragment_medicion_sin_sensor());

        }
        fragmentTransaction.commit();


        SensorManager manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        progressBar = root.findViewById(R.id.progressBar);

        fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new OnClickMedicion());
        return root;
    }

    private class OnClickMedicion implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            CalculadorPeriodo task = new CalculadorPeriodo();

            if (view.getClass() == FabCustom.class) {

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

    public class CalculadorPeriodo extends AsyncTask<Void, Integer, Long> {


        /* metodo que realiza el hilo en segundo plano */
        @Override
        protected Long doInBackground(Void... voids) {

            /* recogo las variables de las configuraciones de la aplicacion */
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(getContext());

            int cant_aspas = Integer.parseInt(preferences.getString("num_aspas", NUMERO_ASPAS_ESTABLECIDAS));
            int cant_muestras = Integer.parseInt(preferences.getString("cant_muestras", MUESTRAS_ESTABLECIDAS));

            int progreso = (int) 100 / cant_muestras; // constante de progreso

            Long periodoMedio = 0L;
            for (int muestra = 1; muestra <= cant_muestras; muestra++) {
                Long periodo = 0L;

                for (int subPeriodo = 1; subPeriodo <= cant_aspas; subPeriodo++) {
                    periodo += medirTs();
                }

                publishProgress(progreso * muestra); //cada vez q termina una muestra avanza la progress bar
                periodoMedio += periodo;
            }

            periodoMedio = periodoMedio / cant_muestras;

            return periodoMedio;
        }

        private long medirTs() {

            Long firstTime = 0L;
            Long lastTime = 0L;

            SensorTrigger sensorTrigger = new SensorTrigger();

            sensorManager.registerListener(sensorTrigger, sensorProximidad, SensorManager.SENSOR_DELAY_FASTEST);

            while (lastTime == 0L) {

                if (sensorTrigger.isTrigger() && firstTime == 0L) {
                    firstTime = System.currentTimeMillis();
                    sensorTrigger.resetTrigger();
                } else if (sensorTrigger.isTrigger() && firstTime != 0L) {
                    lastTime = System.currentTimeMillis();
                    sensorTrigger.resetTrigger();
//                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            return lastTime - firstTime;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progreso = values[0];
            progressBar.setProgress(progreso);
        }

        /* eventos antes de empezar el hilo */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        /* eventos al terminar el hilo */
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            fab.activar();
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(), "Tarea Cancelada", Toast.LENGTH_LONG).show();
        }
    }
}