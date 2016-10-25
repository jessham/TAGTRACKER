package com.weebly.taggtracker.tagtracker;

import android.content.Context;
import android.database.ContentObservable;
import android.widget.EditText;

import com.weebly.taggtracker.tagtracker.models.CheckList;

import junit.framework.TestCase;

/**
 * Created by marioamigos on 24/10/2016.
 */

public class testesChecklists extends TestCase {


    public void testTamanhoMenor(){
        CheckList p = new CheckList("Lista academia", 11);
        boolean resultado = p.TamanhoMenorOk();
        assertTrue(resultado);
    }

    public void testTamanhoMenorCasoFalso(){
        CheckList p = new CheckList("12345678910111213141516171819202122", 12);
        boolean resultado = p.TamanhoMenorOk();
        assertFalse(resultado);
    }

    public void testTamanhoMaior(){
        CheckList p = new CheckList("12345678910111213141516171819202122", 13);
        boolean resultado = p.TamanhoMaiorOk();
        assertTrue(resultado);
    }

    public void testTamanhoMaiorCasoFalso(){
        CheckList p = new CheckList("1", 14);
        boolean resultado = p.TamanhoMaiorOk();
        assertFalse(resultado);
    }

    public void testVazio(){
        CheckList p = new CheckList("", 15);
        boolean resultado = p.Vazio();
        assertTrue(resultado);
    }

    public void testVazioFalse(){
        CheckList p = new CheckList("Lista da escola", 16);
        boolean resultado = p.Vazio();
        assertFalse(resultado);
    }

     /*

    public void testeTentaInserirValorValido() {
        DatabaseHelper d =  new DatabaseHelper(null);
        boolean resultado = d.insereChecklist("Lista Academia");
        assertTrue(resultado);
    }

    public void testeTentaInserirValorPequeno() {
        DatabaseHelper e =  new DatabaseHelper(null);
        boolean resultado = e.insereChecklist("Li");
        assertFalse(resultado);
    }

    public void testeTentaInserirValorGrande(){
        DatabaseHelper f = new DatabaseHelper(null);
        boolean resultado = f.insereChecklist("12345678901234567890123456789012345667");
        assertTrue(resultado);

    }

    public void testeValorVazio(){
        DatabaseHelper g = new DatabaseHelper(null);
        boolean resultado = g.insereChecklist("");
        assertTrue(resultado);
    }


    public void testeInserir(){
        DatabaseHelper h = new DatabaseHelper(null);
        boolean resultado = h.insereChecklist("");
        assertTrue(resultado);
    }
    */
}