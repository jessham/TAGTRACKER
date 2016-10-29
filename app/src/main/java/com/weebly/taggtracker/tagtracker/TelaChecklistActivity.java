package com.weebly.taggtracker.tagtracker;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

//Boa referencia aqui: http://www.vogella.com/tutorials/AndroidListView/article.html
//Outro: http://www.tutorialsbuzz.com/2014/05/android-listfragment-using-arrayadapter.html

public class TelaChecklistActivity extends ListFragment {
    private DatabaseHelper bd;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> selecionados = new ArrayList<Integer>();
    public ArrayAdapter<String> getAdapter(){
        return this.adapter;
    }
    CheckBox checkbox;
    View v;
    boolean isChecked = false;
    private Toolbar toolbar;
    private boolean ativaModoSelecao;


    public void instanciaBD(DatabaseHelper bd){
        this.bd = bd;
    }

    public void atualizaAdapter(){
        this.adapter.notifyDataSetChanged();
    }


    @Override
    //Coloca a view do xml definido: tela_checklists
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tela_checklists, container, false);

        //Administra a checkbox da seleção total
        checkbox = (CheckBox) v.findViewById(R.id.check_allchecklists);
        checkbox.setVisibility(View.INVISIBLE);

        checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isChecked = checkbox.isChecked();

                if (isChecked){
                    checkbox.setChecked(isChecked);
                    selecionaTudo();
                } else {
                    checkbox.setChecked(isChecked);
                    deselecionaTudo();
                }

            }
        });

        return v;
    }

    @Override
    //Coloca os dados dp BD no adapter
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //pega os dados das checklists
        ArrayList<String> values = bd.leChecklist();
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);


        //comportamento para qndo o usuario pressionar por um longo tempo
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ativaModoSelecao = true;

                //Ajusta a lista de itens selecionados
                if (!selecionados.contains(position)) {
                    selecionados.add(position);
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    int pos = selecionados.indexOf(position);
                    selecionados.remove(pos);
                    view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                }

                //As ações da toolbar dependem desse resultado
                configuraToolbar();
                return true;
            }
        } );


    }

    @Override
    //Faz alguma coisa quando o item é clicado: MELHORAR!!
    public void onListItemClick(ListView l, View v, int position, long id) {
        //se estiver no modo seleção
        if(ativaModoSelecao){
            //Ajusta a lista de itens selecionados
            if (!selecionados.contains(position)) {
                selecionados.add(position);
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            } else {
                int pos = selecionados.indexOf(position);
                selecionados.remove(pos);
                v.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            //As ações da toolbar dependem desse resultado
            configuraToolbar();
        }



        //Encontra a id da checklist selecionada
        String item = l.getItemAtPosition(position).toString();
        int checklist = bd.buscaIdChecklist(item);

        //Econtra as tags relacionadas a essa checklist
        ArrayList<String> lista = bd.leItensListas(Integer.toString(checklist));
        Toast.makeText(getActivity(), "Contém: " + lista.toString(), Toast.LENGTH_SHORT)
                .show();
    }

    public void configuraToolbar(){
        //Altera o título e altera ícones da toolbar
        int contagem = selecionados.size();

        //Cuida da visibilidade do ícone da remoção
        Menu menu = toolbar.getMenu();
        MenuItem item = menu.findItem(R.id.app_bar_delete);

        if (contagem >= 1){
            item.setVisible(true);
            checkbox.setVisibility(View.VISIBLE);
        }
        else {
            item.setVisible(false);
            checkbox.setVisibility(View.INVISIBLE);
            ativaModoSelecao = false;
        }


        //Cuida do subtitulo representando quantos itens estão selecionados no momento
        if (contagem > 1){
            toolbar.setSubtitle(contagem + " itens selecionados");
        } else if (contagem == 1){
            toolbar.setSubtitle("1 item selecionado");
        } else {
            toolbar.setSubtitle("");
        }
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }



    public void deselecionaTudo(){
        ListView lista = getListView();
        selecionados.clear();

        for (int i = 0; i < lista.getCount(); i ++) {
            View item = getListView().getChildAt(i);
            item.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        }

        configuraToolbar();
    }

    public void selecionaTudo(){
        selecionados.clear();
        ListView lista = getListView();

        for (int i = 0; i < lista.getCount(); i ++) {
            selecionados.add(i);
            View item = getListView().getChildAt(i);
            item.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        configuraToolbar();
    }




}






