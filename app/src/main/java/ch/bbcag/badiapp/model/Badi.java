package ch.bbcag.badiapp.model;

import java.util.ArrayList;
import java.util.List;


public class Badi {

    private int id;
    private String name;
    private List<Becken> becken;
    private String kanton;
    private String ort;
    private boolean isFavorite;

    public Badi(){
        becken = new ArrayList<>();
    }

    public String getKanton() {
        return kanton;
    }

    public void setKanton(String kanton) {
        this.kanton = kanton;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addBecken(Becken becken) {
        this.becken.add(becken);
    }

    public List<Becken> getBecken() {
        return becken;
    }

    public String toString() {
        return String.format("%s %s (%s)", ort, name, kanton);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}
