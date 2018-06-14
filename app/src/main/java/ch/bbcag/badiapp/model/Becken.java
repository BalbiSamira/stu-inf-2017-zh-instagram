package ch.bbcag.badiapp.model;

public class Becken {

    private String name;
    private double temperature;


    @Override
    public String toString() {
        return getName() + ":  " + getTemperature() + "°C";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
