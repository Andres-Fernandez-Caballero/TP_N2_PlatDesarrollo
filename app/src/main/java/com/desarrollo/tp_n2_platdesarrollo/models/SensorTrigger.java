package com.desarrollo.tp_n2_platdesarrollo.models;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class SensorTrigger implements SensorEventListener {

    private boolean trigger;
    private float medicionAnterior;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float mMax = event.sensor.getMaximumRange();
        float medicion = event.values[0];

        Log.i("EVENTO","medicion sensor: " + medicion);


        if(medicionAnterior == mMax && medicion != mMax){
            trigger = true;
            Log.i("EVENTO_TRIGGER","dentro del evento trigger disparado");
        }

        if(medicion != medicionAnterior){
            medicionAnterior = medicion;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // metodo no necesario
    }

    public SensorTrigger( ) {

        trigger = false;
        medicionAnterior = -1; // como buena practica inicio el valor en uno que no dispare el trigger accidentalmente
    }

    public boolean isFired() {
        return trigger;
    }

    public void resetTrigger(){
        trigger = false;
    }
}
