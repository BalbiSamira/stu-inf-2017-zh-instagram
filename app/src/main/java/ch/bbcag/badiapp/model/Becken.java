package ch.bbcag.badiapp.model;

public class Becken {

    private String name;
    private Double temperature;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String toString() {
        // TODO Replace C° with value from SharedPreferences and if it sould be °F calculate it.
        if (temperature == null) {
            return String.format("%s: -", name);
        }
        return String.format("%s: %s °C", name, temperature);
    }

}
