package com.desarrollo.tp_n2_platdesarrollo.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.desarrollo.tp_n2_platdesarrollo.R;
import com.desarrollo.tp_n2_platdesarrollo.fragments.Fragment_medicion_resultados;
import com.desarrollo.tp_n2_platdesarrollo.fragments.Fragment_medicion_sin_resultados;
import com.desarrollo.tp_n2_platdesarrollo.fragments.Fragment_medicion_sin_sensor;
import com.desarrollo.tp_n2_platdesarrollo.models.FabCustom;
import com.desarrollo.tp_n2_platdesarrollo.models.SensorTrigger;

public class HomeFragment extends Fragment {

    // manager de sensor
    private SensorManager sensorManager;

    // sensor de proximidad
    private Sensor sensorProximidad;

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


        if (sensorProximidad != null) {
            // si el dispositivo tiene sensor de proximidad muestra la pantalla normal
           lanzarFragment(new Fragment_medicion_sin_resultados());
        } else {
            // si el dispositivo no tiene sensor de proximidad muestra el fragment de no disponible y oculta el boton de accion
            lanzarFragment(new Fragment_medicion_sin_sensor());
            fab.ocultar();
        }

        /* inicializo el sensormanager y el senso le asigno el tipo de sensor q debe usar */
        SensorManager manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        /* asigno los objetos a las vistas */
        progressBar = root.findViewById(R.id.progressBar);
        fab = root.findViewById(R.id.fab);

        /* asigno el evento al boton */
        fab.setOnClickListener(new OnClickMedicion());

        return root;
    }

    private class OnClickMedicion implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view.getClass() == FabCustom.class) {

                FabCustom fab = (FabCustom) view;
                CalculadorPeriodo task = new CalculadorPeriodo();
                if (fab.isActivated()) { // desactiva el boton y ejecuta la tarea
                    fab.desactivar();
                    task.execute();

                } else { // activa el boton y finaliza la tarea

                    task.cancel(true); // cancelo la tarea propiamente dicho
                    fab.activar();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     *  Esta clase crea una tarea asincronica extendiendose de AsyncTask,
     *  segun la documentacion de Android esta obsoleta y no deberia usarse mas.
     *  Para el caso practico de este proyecto es aceptable usar esta clase heredada.
     *  <p>
     *      La funcionalidad de esta clase es realizar un numero de muestras determinado por las
     *      configuraciones de la aplicacion calcular la suma de los subperiodos probocados por las
     *      aspas de un motor, estas aspas tambien estan definidas en las configuraciones
     *      como resultado creara un nuevo fragment con toda la informacion necesaria.
     *  </p>
     */
    public class CalculadorPeriodo extends AsyncTask<Void, Integer, Long> {

        /* metodo que realiza el hilo en segundo plano */
        @Override
        protected Long doInBackground(Void... voids) {

            final String MUESTRAS_ESTABLECIDAS = "3"; // valor por defecto
            final String NUMERO_ASPAS_ESTABLECIDAS = "2"; // valor por defecto

            /* recogo las variables de las configuraciones de la aplicacion */
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(getContext());

            //int cant_aspas = Integer.parseInt(preferences.getString("num_aspas", NUMERO_ASPAS_ESTABLECIDAS));
            int cant_aspas = Integer.parseInt(preferences.getString("num_aspas",NUMERO_ASPAS_ESTABLECIDAS));
            int cant_muestras = Integer.parseInt(preferences.getString("cant_muestras", MUESTRAS_ESTABLECIDAS));

            int progreso = (int) 100 / cant_muestras; // constante de progreso

            Long periodoMedio = 0L;
            for (int muestra = 1; muestra <= cant_muestras; muestra++) {
                Log.i("calculo_periodo","for de muestras muestra numero: " + muestra );
                Long periodo = 0L;

                for (int subPeriodo = 1; subPeriodo <= cant_aspas; subPeriodo++) {
                    periodo += medirTs();
                    Log.i("calculo_periodo","for de muestras ts numero: " + subPeriodo );
                    if(isCancelled()) break; // cancelo la tarea y salgo del hilo
                }

                publishProgress(progreso * muestra); //cada vez q termina una muestra avanza la progress bar
                periodoMedio += periodo; // sumo el periodo calculado al periodo total
                if (isCancelled()) break; // cancelo la tarea y salgo del hilo
            }

            periodoMedio = periodoMedio / cant_muestras; // hago el calculo de la media del periodo total


            return periodoMedio; // envio el periodo medio al metodo onPostExecute
        }

        private long medirTs() {

            Long firstTime = 0L;
            Long lastTime = 0L;

            SensorTrigger sensorTrigger = new SensorTrigger();

            sensorManager.registerListener(sensorTrigger, sensorProximidad, SensorManager.SENSOR_DELAY_FASTEST);

            while (lastTime == 0L) {

                if (sensorTrigger.isFired() && firstTime == 0L) {
                    firstTime = System.currentTimeMillis();
                    sensorTrigger.resetTrigger();
                    Log.i("calculo_periodo","gatillo1 disparado" );
                } else if (sensorTrigger.isFired() && firstTime != 0L) {
                    lastTime = System.currentTimeMillis();
                    sensorTrigger.resetTrigger();
                    Log.i("calculo_periodo","gatillo2 disparado");
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
            lanzarFragment(new Fragment_medicion_sin_resultados());
        }

        /* eventos al terminar el hilo */
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            fab.activar();
            progressBar.setVisibility(View.INVISIBLE);

            lanzarFragment(Fragment_medicion_resultados.newInstance(result));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(), "Tarea Cancelada", Toast.LENGTH_LONG).show();
            fab.activar();
        }
    }

    private void lanzarFragment(Fragment fragment){

        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_contenedor_resultados, fragment);

        fragmentTransaction.commit();
    }

}