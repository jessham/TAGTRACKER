package com.weebly.taggtracker.tagtracker;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

//Boa referencia aqui: http://www.vogella.com/tutorials/AndroidListView/article.html
//Outro: http://www.tutorialsbuzz.com/2014/05/android-listfragment-using-arrayadapter.html

public class TelaChecklistActivity extends ListFragment {
    private DatabaseHelper bd;
    private ArrayAdapter<String> adapter;

    public ArrayAdapter<String> getAdapter(){
        return this.adapter;
    }


    public void instanciaBD(DatabaseHelper bd){
        this.bd = bd;
    }

    public void atualizaAdapter(){
        this.adapter.notifyDataSetChanged();
    }


    @Override
    //Coloca a view do xml definido: tela_checklists
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tela_checklists, container, false);
    }

    @Override
    //Coloca os dados dp BD no adapter
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //pega os dados das checklists
        ArrayList<String> values = bd.leChecklist();

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);

        adapter.notifyDataSetChanged();


        setListAdapter(adapter);
    }

    @Override
    //Faz alguma coisa quando o item é clicado: MELHORAR!!
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        //Encontra a id da checklist selecionada
        String item = l.getItemAtPosition(position).toString();
        int checklist = bd.buscaIdChecklist(item);

        //Econtra as tags relacionadas a essa checklist
        ArrayList<String> lista = bd.leItensListas(Integer.toString(checklist));

        String mensagem = "";
        if (lista.isEmpty()) mensagem = "VAZIO";
        else mensagem = lista.toString();


        Toast.makeText(getActivity(), "Contém: " + lista.toString(), Toast.LENGTH_SHORT)
                .show();
    }
}






