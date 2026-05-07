package com.example.practica3;

import java.util.ArrayList;

public class Juego {
    private int numEstaciones = 10;

    private ColaCircular<Estacion> colaEstaciones;
    private ArrayList<Recurso> salidaRecursos;
    private int[] ultimosValoresDados;
    private int ronda;
    private boolean iniciado;

    public Juego() {
        colaEstaciones = new ColaCircular<>(numEstaciones);
        salidaRecursos = new ArrayList<>();
        ultimosValoresDados = new int[numEstaciones];
        ronda = 0;
        iniciado = false;
    }

    public void iniciar() {
        colaEstaciones = new ColaCircular<>(numEstaciones);
        salidaRecursos = new ArrayList<>();
        ultimosValoresDados = new int[numEstaciones];
        ronda = 0;

        // Crear e insertar las 10 estaciones en la cola
        for (int i = 0; i < numEstaciones; i++) {
            Estacion est = new Estacion(i + 1);
            // Poner recursos iniciales en todas menos la primera
            if (i > 0) {
                for (int j = 0; j <4 ; j++) {
                    est.agregarRecurso(new Recurso());
                }
            }
            colaEstaciones.insertar(est);
        }

        iniciado = true;
    }

    public void lanzarDados() {
        for (int i = 0; i < numEstaciones; i++) {
            Estacion temp = colaEstaciones.eliminar();
            ultimosValoresDados[i] = temp.lanzarDado();
            colaEstaciones.insertar(temp);
        }
    }

    public void moverRecursos() {
        Estacion estacionPrimera = colaEstaciones.eliminar();

        ColaCircular<Recurso> colaProcesados = new ColaCircular<>(ultimosValoresDados[0]);
        for (int j = 0; j < ultimosValoresDados[0]; j++) {
            colaProcesados.insertar(new Recurso());
        }

        //recorrer cola
        for (int i = 0; i < numEstaciones - 1; i++) {
            Estacion estacionSiguiente = colaEstaciones.eliminar();

            // calcular cuánto puede procesar la siguiente estación
            int cantAntes = estacionSiguiente.getCantidadEnCola();
            int dadoSig = ultimosValoresDados[estacionSiguiente.getId() - 1];
            //cant de personas que se procesaran
            int cantSig;
            //dadoSig=dado que salio en la ronda
            //cantAntes= en cola
            if (dadoSig < cantAntes) {
                cantSig = dadoSig;
            } else {
                cantSig = cantAntes;
            }
            //sig estacion recibe lo que proceso la ant
            estacionSiguiente.recibirRecursos(colaProcesados);

            //reinsert la estacin primera al final de la cola
            colaEstaciones.insertar(estacionPrimera);
            colaProcesados = estacionSiguiente.procesarRecursos(cantSig);
            estacionPrimera = estacionSiguiente;
        }

        //para menejar lo de la ultima estacion fue el ciclo que se itero
        //cuando termino en dar la ultima vuelta ya por default es E10 que
        //es la ultima estacion, entocnes en el ciclo w ya lo de la ultima
        //estacion va a la salida finak
        while (!colaProcesados.isEmpty()) {
            salidaRecursos.add(colaProcesados.eliminar());
        }
        colaEstaciones.insertar(estacionPrimera);
        ronda++;
    }

    public int[] getUltimosValoresDados() {
        return ultimosValoresDados;
    }

    public ColaCircular<Estacion> getEstaciones() {
        return colaEstaciones;
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
        for (int i = 0; i < numEstaciones; i++) {
            Estacion temp = colaEstaciones.eliminar();
            total += temp.getCantidadEnCola();
            colaEstaciones.insertar(temp);
        }
        return total;
    }
}
