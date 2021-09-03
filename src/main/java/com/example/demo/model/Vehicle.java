package com.example.demo.model;

public class Vehicle {

    private long id;
    private long providerId;
    private String model;
    private String colour;
    private int horsePower;
    private Type type;
    private double PVP;

    public Vehicle() {}

}

enum Type{COCHE,MOTO}