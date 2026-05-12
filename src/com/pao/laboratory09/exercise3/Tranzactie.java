package com.pao.laboratory09.exercise3;

public record Tranzactie(int id, double suma, String data) {
    @Override
    public String toString() {
        return "Tranzactie #" + id + " " + suma + " RON";
    }
}
