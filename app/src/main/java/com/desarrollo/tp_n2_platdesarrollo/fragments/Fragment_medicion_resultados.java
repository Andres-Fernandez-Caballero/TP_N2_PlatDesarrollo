package com.desarrollo.tp_n2_platdesarrollo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desarrollo.tp_n2_platdesarrollo.R;
import com.desarrollo.tp_n2_platdesarrollo.models.Resultado;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_medicion_resultados#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_medicion_resultados extends Fragment {

    private static final String ARG_PARAM_PERIODO = "param_result_medicion_periodo";

    private Long periodo;
    private float frecuencia;
    private Resultado resultado;

    public Fragment_medicion_resultados() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resultado resultado del calculo del periodo.
     * @return A new instance of fragment Fragment_medicion_resultados.
     */

    public static Fragment_medicion_resultados newInstance(Long resultado) {
        Fragment_medicion_resultados fragment = new Fragment_medicion_resultados();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM_PERIODO, resultado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            resultado = new Resultado(getArguments().getLong(ARG_PARAM_PERIODO));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_medicion_resultados, container, false);
        TextView tCampo_periodo = view.findViewById(R.id.fragment_contenedor_resultados_tv_periodo);

        if(resultado != null){
            tCampo_periodo.setText(Html.fromHtml(getString(R.string.string_template_result_periodo_frecuencia
                    ,resultado.getPeriodo()
                    ,resultado.getFrecuencia()
            )));
        }
        return view;
    }
}