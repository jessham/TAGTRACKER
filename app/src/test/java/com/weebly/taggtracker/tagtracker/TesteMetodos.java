package com.weebly.taggtracker.tagtracker;

import android.content.Context;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.view.MenuItem;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
//import org.mockito.cglib.proxy.CallbackGenerator;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static com.weebly.taggtracker.tagtracker.DatabaseHelper.DATABASE_NAME;
import static com.weebly.taggtracker.tagtracker.DatabaseHelper.DATABASE_VERSION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.booleanThat;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.stubVoid;
import static org.mockito.Mockito.when;

/**
 * Created by marioamigos on 23/11/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class TesteMetodos extends AndroidTestCase {


    private static final int proximo = 1;
    private static final boolean CaseVerdadeiro= true;
    private static final boolean CaseFalso = false;

    @Mock
    Context mMockContext;



    @Mock
    Toast toast;


    @Mock
    MenuItem item;


    @Test
    public void ProximoIDchecklist(){
        when(mMockContext.getString(R.string.hello_blank_fragment)).thenReturn(String.valueOf(proximo));
        DatabaseHelper d = new DatabaseHelper(mMockContext);
        int result = d.retornaProxIDChecklists();

        assertThat(result, is(proximo));
    }


    @Test
    public void ProximoIDTags(){

        when(mMockContext.getString(R.string.hello_blank_fragment)).thenReturn(String.valueOf(proximo));
        DatabaseHelper d = new DatabaseHelper(mMockContext);

        int result = d.retornaProxIDTags();
        assertThat(result, is(proximo));
    }



    @Test
    public void isModoModificar(){
        CadastraChecklistsActivity h = new CadastraChecklistsActivity();
        boolean resultado = h.isModoEditar();
        assertThat(resultado, is(false));
    }

    ArrayList<String> resp = new ArrayList<String>();

    public ArrayList<String> getSelecionadosTitulos() {
        return null;
    }


    @Test
    public void BuscaNaoAchaTag(){
        DatabaseHelper h = new DatabaseHelper(mMockContext);
        int resultado = h.buscaIdTag("");
        boolean result;

        if(resultado == -1)
            result=true;
        else
            result = false;

        assertTrue(result);

    }

    @Test
    public void BuscaNaoAchaCheckList(){
        DatabaseHelper h = new DatabaseHelper(mMockContext);
        int resultado = h.buscaIdChecklist("");
        boolean result;

        if(resultado == -1)
            result=true;
        else
            result = false;

        assertTrue(result);

    }
    /*
    @Test
    public void insereAssociada(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        //when(mMockContext.getString(R.string.hello_blank_fragment)).thenReturn(String.valueOf(proximo));
        //x.super(mMockContext,null,mMockContext);
        //Toast.makeText(mMockContext, R.string.checklist_notinserted,Toast.LENGTH_SHORT).show();
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);

        boolean resultado = x.insereAssocia(1,2);
        assertTrue(true);

    }
    */
    @Test
    public void insereCheckListVazia(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereChecklist("");
        assertThat(resultado, is(false));

    }
    @Test
    public void insereCheckList1Caracter(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereChecklist("1");
        assertThat(resultado, is(false));

    }

    @Test
    public void insereTagsMenorQue3(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereTags("12");
        assertThat(resultado, is(false));
    }
    
    @Test
    public void insereCheckListMenos3(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereChecklist("12");
        assertThat(resultado, is(false));

    }



    @Test
    public void insereCheckListExtensa(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereChecklist("12345678912820928902829802789978097");
        assertThat(resultado, is(false));

    }

    @Test
    public void insereTagsVazia(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereTags("");
        assertThat(resultado, is(false));
    }

    // public boolean insereAssocia(int idChecklists, int idTags)


    @Test
    public void insereAssociaNegativaTag(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereAssocia(-1,1);
        assertThat(resultado, is(false));
    }


    @Test
    public void insereAssociaNegativaCheck(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.insereAssocia(1,-1);
        assertThat(resultado, is(false));
    }

    //public boolean atualizatags(String titulo, int tagsID) {
    @Test
    public void atualizaTagsNegativa(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.atualizatags("nome",-1);
        assertThat(resultado, is(false));
    }

    @Test
    public void atualizaTagsVazia(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.atualizatags("",4);
        assertThat(resultado, is(false));
    }

    @Test
    public void atualizarParametrosEquivo(){
        DatabaseHelper x = new DatabaseHelper(mMockContext);
        boolean resultado = x.atualizatags("",-1);
        assertThat(resultado, is(false));
    }





}