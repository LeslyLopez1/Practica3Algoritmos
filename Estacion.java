package com.example.practica3;

public class Estacion {
    private ColaCircular<Recurso> cola;
    private Dado dado;
    //identificador para cada estacion
    private int id;

    public Estacion(int id) {
        this.id = id;
        this.cola = new ColaCircular<>(50);
        this.dado = new Dado();
    }


    public int lanzarDado() {
        return dado.lanzar();
    }

    public ColaCircular<Recurso> procesarRecursos(int cantidad) {
        ColaCircular<Recurso> procesados = new ColaCircular<>(cantidad);
        int movidos = 0;
        while (movidos < cantidad && !cola.isEmpty()) {
            Recurso r = cola.eliminar();
            r.setEstado(true);
            procesados.insertar(r);
            movidos++;
        }
        return procesados;
    }

    public void recibirRecursos(ColaCircular<Recurso> recursos) {
        while (!recursos.isEmpty()) {
            Recurso r = recursos.eliminar();
            if (r != null) {
                r.setEstado(false);
                cola.insertar(r);
            }
        }
    }

    public void agregarRecurso(Recurso r) {
        r.setEstado(false);
        cola.insertar(r);
    }
    //getters
    public ColaCircular<Recurso> getCola() {
        return cola;
    }

    public Dado getDado() {
        return dado;
    }

    public int getId() {
        return id;
    }

    public int getCantidadEnCola() {
        return cola.getSize();
    }
}