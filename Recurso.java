package com.example.practica3;

public class Recurso {
    //false=no procesado / true=procesado
    private boolean estado;

    public Recurso() {
        this.estado = false;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Recurso{estado=" + (estado ? "procesando" : "esperando") + "}";
    }
}