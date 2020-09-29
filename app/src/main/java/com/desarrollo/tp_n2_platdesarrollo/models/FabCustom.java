package com.desarrollo.tp_n2_platdesarrollo.models;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.desarrollo.tp_n2_platdesarrollo.ui.home.HomeFragment;
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

    public void desactivar(){
        //TODO: hacer que se desactive el boton y cambie de aspecto
        this.activated = false;
        this.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0, 145, 234)));

    }

    public void activar(){
        //TODO: hacer que se active y vuelva a su aspecto original
        this.activated = true;
        this.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(3, 218, 197))); //colorAccent

    }
}
