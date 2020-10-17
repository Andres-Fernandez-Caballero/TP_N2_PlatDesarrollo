package com.desarrollo.tp_n2_platdesarrollo.models;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import com.desarrollo.tp_n2_platdesarrollo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabCustom extends FloatingActionButton  {

    private boolean activated;

    public FabCustom(Context context) {
        super(context);
        this.activar();
    }

    public FabCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        activar();
    }

    public FabCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activar();
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    /**
     * Este metodo dispone la apariencia del FabCustom en la forma DESACTIVADA,
     * cambia su fondo e icono y coloca el flag de activaded en FALSE
     */
    public void desactivar(){
        //TODO: hacer que se desactive el boton y cambie de aspecto
        this.activated = false;
        this.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0, 145, 234)));
        this.setImageResource(R.drawable.ic_cruz);
    }

    /**
     * Este metodo dispone la apariencia del FabCustom en la forma ACTIVADA,
     *      * cambia su fondo e icono y coloca el flag de activaded en TRUE
     */
    public void activar(){
        this.activated = true;
        this.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(3, 218, 197))); //colorAccent
        this.setImageResource(R.drawable.ic_compaz);
    }

    public void ocultar(){
        this.hide();
    }

    public void mostrar(){
        this.show();
    }

}
