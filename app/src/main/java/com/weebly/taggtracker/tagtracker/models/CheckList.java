package com.weebly.taggtracker.tagtracker.models;



/**
 * Created by marioamigos on 24/10/2016.
 */

public class CheckList  {

    private String Nome;
    private int Id;

    public CheckList(String Nome, int Id){
        this.Nome = Nome;
        this.Id=Id;

    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean TamanhoMenorOk(){
        return this.Nome.length() < 30;
    }

    public boolean TamanhoMaiorOk(){
        return this.Nome.length() > 3;
    }

    public boolean Vazio(){
        return this.Nome.isEmpty();
    }

}
