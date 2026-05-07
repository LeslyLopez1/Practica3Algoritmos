package com.example.practica3;


public class ColaCircular<T> {
    private final T[] colaCircular;
    private int inicio;
    private int fin;
    private int size;

    public ColaCircular() {
        this(10);
    }

    public ColaCircular(int max) {
        colaCircular = (T[]) new Object[max];
        inicio = -1;
        fin = -1;
        size = 0;
    }

    public void insertar(T dato) {
        if (size == colaCircular.length) {
            return;
        }
        if (fin == colaCircular.length - 1) fin = 0;
        else fin++;

        colaCircular[fin] = dato;
        if (inicio == -1) inicio = 0;
        size++;
    }

    public T eliminar() {
        if (inicio == -1) {
            return null;
        }
        T dato = colaCircular[inicio];
        colaCircular[inicio] = null;

        if (inicio == fin) {
            inicio = -1;
            fin = -1;
        } else {
            if (inicio == colaCircular.length - 1) inicio = 0;
            else inicio++;
        }
        size--;
        return dato;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public T peek() {
        if (inicio == -1) return null;
        return colaCircular[inicio];
    }
}