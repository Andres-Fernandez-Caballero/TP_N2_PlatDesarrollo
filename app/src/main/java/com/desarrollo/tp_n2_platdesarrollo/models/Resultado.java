package com.desarrollo.tp_n2_platdesarrollo.models;

public class Resultado {

    private Long periodo;
    private float frecuencia;

    public Resultado(Long periodo) {

        this.periodo = periodo;

        if(this.periodo != 0L){
            this.frecuencia = (float) Math.pow(10,3)/ periodo;
        }else{
            this.frecuencia = 0;
        }
    }

    public float getFrecuencia() {
        return frecuencia;
    }

    public Long getPeriodo() {
        return periodo;
    }
}
