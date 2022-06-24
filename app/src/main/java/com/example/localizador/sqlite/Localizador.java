package com.example.localizador.sqlite;

import java.util.Date;

public class Localizador {
    String date;
    String hora;
    String nome;
    String local;
    String distancia;
    String tempoDeslocamento;
    String tempoImovel;

    public Localizador(String date, String hora, String nome, String local, String distancia, String tempoDeslocamento, String tempoImovel){
        this.date = date;
        this.hora = hora;
        this.nome = nome;
        this.local = local;
        this.distancia = distancia;
        this.tempoDeslocamento = tempoDeslocamento;
        this.tempoImovel =  tempoImovel;
    }

    public String getNome() {
        return nome;
    }

    public String getLocal() {
        return local;
    }

    public String getDate() {
        return date;
    }

    public String getHora() {
        return hora;
    }

    public String getDistancia() {
        return distancia;
    }

    public String getTempoDeslocamento() {
        return tempoDeslocamento;
    }

    public String getTempoImovel() {
        return tempoImovel;
    }

    @Override
    public String toString() {
        return date + " " + nome + " " + distancia;
    }

}
