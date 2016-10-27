package com.weebly.taggtracker.tagtracker.models;

/**
 * Created by Barbara on 27/10/2016.
 */

public class Tag {
    private String nomeTabela = "tags";
    private String rotulo;
    private int id;

    public Tag(String rotulo, int id) {
        this.rotulo = rotulo;
        this.id = id;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }
}
