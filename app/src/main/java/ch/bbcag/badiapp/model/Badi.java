package ch.bbcag.badiapp.model;

import java.util.ArrayList;
import java.util.List;

public class Badi {

    private int id;
    private String name;
    private List<Becken> beckenList = new ArrayList<>();
    private String kanton;
    private String ort;


    public Badi(int id, String name, String ort, String kanton) {
        this.setId(id);
        this.setName(name);
        this.setOrt(ort);
        this.setKanton(kanton);
    }

    public Badi() {
    }

    @Override
    public String toString() {
        return getName() + " (" + getKanton() + ")";
    }

    public void addBecken(Becken b) {
        this.getBeckenList().add(b);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Becken> getBeckenList() {
        return beckenList;
    }

    public void setBeckenList(List<Becken> beckenList) {
        this.beckenList = beckenList;
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
}
