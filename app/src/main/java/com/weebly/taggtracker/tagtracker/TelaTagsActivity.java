package com.weebly.taggtracker.tagtracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import java.util.ArrayList;

public class TelaTagsActivity extends ListFragment {
    private DatabaseHelper bd;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> selecionados = new ArrayList<Integer>();
    public ArrayAdapter<String> getAdapter(){
        return this.adapter;
    }
    private CheckBox checkbox;
    private View v;
    private ListView listview;
    boolean isChecked = false;
    private Toolbar toolbar;
    private boolean ativaModoSelecao;


    /* ********************************************************************************************
     * METODOS DE CRIAÇÃO DA LISTFRAGMENT
     * *******************************************************************************************/

    int mNum;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static TelaTagsActivity newInstance(int num) {
        TelaTagsActivity f = new TelaTagsActivity();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    //Coloca a view do xml definido: tela_tags
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tela_tags, container, false);

        //Administra a checkbox da seleção total
        checkbox = (CheckBox) v.findViewById(R.id.check_alltags);
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
        listview = getListView();

        //pega os dados das tags
        ArrayList<String> values = bd.leTags();

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
                    view.setBackgroundColor(getResources().getColor(R.color.itemSelecionado));
                } else {
                    int pos = selecionados.indexOf(position);
                    selecionados.remove(pos);
                    view.setBackgroundColor(getResources().getColor(R.color.itemNormal));
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
                v.setBackgroundColor(getResources().getColor(R.color.itemSelecionado));
            } else {
                int pos = selecionados.indexOf(position);
                selecionados.remove(pos);
                v.setBackgroundColor(getResources().getColor(R.color.itemNormal));
            }

            //As ações da toolbar dependem desse resultado
            configuraToolbar();
        }

        VisualizaTagActivity visualizacao = new VisualizaTagActivity();
        Intent it = new Intent(getActivity(), visualizacao.getClass());
        it.putExtra("tag-visual-key", l.getItemAtPosition(position).toString().trim());
        getActivity().startActivityForResult(it, 4);
    }


    /* *********************************************************************************************
     * GETTERS E SETTERS
     * ********************************************************************************************/

    public void instanciaBD(DatabaseHelper bd){
        this.bd = bd;
    }

    public ArrayList<String> getSelecionadosTitulos (){
        ArrayList<String> resp = new ArrayList<String>();

        for (int i = 0; i < selecionados.size(); i++){
            int item = selecionados.get(i);
            String titulo = listview.getItemAtPosition(item).toString();

            resp.add(titulo);
        }

        return resp;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }



    /* *********************************************************************************************
     * TOOLBAR E SELEÇÃO
     * ********************************************************************************************/

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

    public void deselecionaTudo(){
        selecionados.clear();

        for (int i = 0; i < listview.getCount(); i ++) {
            View item = listview.getChildAt(i);
            if (item != null)
                item.setBackgroundColor(getResources().getColor(R.color.itemNormal));
        }
        configuraToolbar();
    }

    public void selecionaTudo(){
        selecionados.clear();

        for (int i = 0; i < listview.getCount(); i ++) {
            selecionados.add(i);
            View item = listview.getChildAt(i);
            if (item != null)
                item.setBackgroundColor(getResources().getColor(R.color.itemSelecionado));
        }
        configuraToolbar();
    }
}






