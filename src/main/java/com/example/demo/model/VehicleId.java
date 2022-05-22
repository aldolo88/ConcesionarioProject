package com.example.demo.model;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VehicleId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private String model;

    private String colour;

    private int horsePower;

    @Enumerated(EnumType.STRING)
    private Type type;


    public VehicleId(){

    }

    public VehicleId(Provider provider, String model, String colour, int horsePower, Type type) {
        this.provider = provider;
        this.model = model;
        this.colour = colour;
        this.horsePower = horsePower;
        this.type = type;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getStringType() {
        return type.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleId vehicleId = (VehicleId) o;
        return horsePower == vehicleId.horsePower && provider.equals(vehicleId.provider) && model.equals(vehicleId.model) && colour.equals(vehicleId.colour) && type == vehicleId.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, model, colour, horsePower, type);
    }

    @Override
    public String toString() {
        return "VehicleId{" +
                "provider='" + this.provider + '\'' +
                ", model='" + this.model + '\'' +
                ", colour=" + this.colour + '\'' +
                ", horsePower='" + this.horsePower + '\'' +
                ", type='" + this.type + '\'' +
                '}';
    }

    public enum Type {COCHE, MOTO}
}

