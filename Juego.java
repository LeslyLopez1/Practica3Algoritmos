package com.example.practica3;

import java.util.ArrayList;

public class Juego {
    private int numEstaciones = 10;
    private int recursosIniciales = 4;

    private ColaCircular<Estacion> colaEstaciones;
    //bolitas finales matriz
    private ArrayList<Recurso> salidaRecursos;
    private Estacion[] estaciones;
    private int[] ultimosValoresDados;
    private int ronda;
    private boolean iniciado;
    private boolean dadosLanzados;

    public Juego() {
        colaEstaciones = new ColaCircular<>(numEstaciones);
        salidaRecursos = new ArrayList<>();
        estaciones = new Estacion[numEstaciones];
        ultimosValoresDados = new int[numEstaciones];
        ronda = 0;
        iniciado = false;
        dadosLanzados = false;
    }

    public void iniciar() {
        colaEstaciones = new ColaCircular<>(numEstaciones);
        salidaRecursos = new ArrayList<>();
        estaciones = new Estacion[numEstaciones];
        ultimosValoresDados = new int[numEstaciones];
        ronda = 0;
        dadosLanzados = false;

        for (int i = 0; i < numEstaciones; i++) {
            estaciones[i] = new Estacion(i + 1);
            colaEstaciones.insertar(estaciones[i]);
        }

        for (int i = 1; i < numEstaciones; i++) {
            for (int j = 0; j < recursosIniciales; j++) {
                estaciones[i].agregarRecurso(new Recurso());
            }
        }

        iniciado = true;
    }

    public void lanzarDados() {
        if (!iniciado) return;
        for (int i = 0; i < numEstaciones; i++) {
            ultimosValoresDados[i] = estaciones[i].lanzarDado();
        }
        dadosLanzados = true;
    }

    public void moverRecursos() {
        if (!iniciado || !dadosLanzados) return;
        int[] cantidadAntes = new int[numEstaciones];
        for (int i = 0; i < numEstaciones; i++) {
            cantidadAntes[i] = estaciones[i].getCantidadEnCola();
        }

        ColaCircular<Recurso>[] transferencias = new ColaCircular[numEstaciones];

        //estacion 1 + recursos
        ColaCircular<Recurso> nuevos = new ColaCircular<>(ultimosValoresDados[0] + 1);
        for (int j = 0; j < ultimosValoresDados[0]; j++) {
            nuevos.insertar(new Recurso());
        }
        transferencias[0] = nuevos;

        for (int i = 1; i < numEstaciones; i++) {
            int dado = ultimosValoresDados[i];
            int enCola = cantidadAntes[i];
            int cantidad;

            if (dado < enCola) {
                cantidad = dado;
            } else {
                cantidad = enCola;
            }

            transferencias[i] = estaciones[i].procesarRecursos(cantidad);
        }

        for (int i = 1; i < numEstaciones; i++) {
            estaciones[i].recibirRecursos(transferencias[i - 1]);
        }
        //los recursos que salen de la last estacion van a la matriz de bolitas
        while (!transferencias[numEstaciones - 1].isEmpty()) {
            Recurso r = transferencias[numEstaciones - 1].eliminar();
            if (r != null) {
                salidaRecursos.add(r);
            }
        }

        ronda++;
        dadosLanzados = false;
    }
//getters
    public int[] getUltimosValoresDados() {
        return ultimosValoresDados;
    }

    public Estacion[] getEstaciones() {
        return estaciones;
    }

    public ArrayList<Recurso> getSalidaRecursos() {
        return salidaRecursos;
    }

    public int getRonda() {
        return ronda;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public int getTotalEnSistema() {
        int total = 0;
        for (Estacion e : estaciones) {
            total += e.getCantidadEnCola();
        }
        return total;
    }
}