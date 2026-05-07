package com.example.practica3;

import java.util.Random;

public class Dado {
    private int valor;
    private final Random random;

    public Dado() {
        this.random = new Random();
        this.valor = 1;
    }

    public int lanzar() {
        valor = random.nextInt(6) + 1; // 1 a 6
        return valor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}