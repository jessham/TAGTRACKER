package com.weebly.taggtracker.tagtracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CadastraChecklistsActivity extends AppCompatActivity {
    private DatabaseHelper bd;
    private SparseBooleanArray sparseBooleanArray ;
    private String itensSelecionados = "" ;
    private Toolbar toolbar;
    private boolean modoEditar;
    private String tituloChecklist; //checklist que será visualizada ou editada


    /* ********************************************************************************************
        GETTERS AND SETTERS
     ******************************************************************************************** */

    public CadastraChecklistsActivity() {
        bd = new DatabaseHelper(this);
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public String getTituloChecklist() {
        return tituloChecklist;
    }

    public void setTituloChecklist(String tituloChecklist) {
        this.tituloChecklist = tituloChecklist;
    }

    public boolean isModoEditar() {
        return modoEditar;
    }

    public void setModoEditar(boolean modoEditar) {
        this.modoEditar = modoEditar;
    }

    /* ********************************************************************************************
        MÉTODOS DE INICIALIZAÇÃO
     ******************************************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_checklists);

        //Configura a toolbar
        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar_cadastrachecklist);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Verifica se esta no modulo de edicao
        Bundle extras = getIntent().getExtras();
        ArrayList<String> itens = null;

        if (extras != null) {
            itens = extras.getStringArrayList("checklist-edit-key");

            //O titulo esta na ultima posicao do array
            setTituloChecklist(itens.get(itens.size() - 1));
            setModoEditar(true);

            EditText txtTitulo = (EditText) findViewById(R.id.txtTitleChecklist);
            txtTitulo.setText(getTituloChecklist());
        }



        //Criamos o array de dados e o colocamos no adapter
        final ArrayList array;
        final ArrayAdapter<String> adaptador;

        array = bd.leTags();
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, array);


        //Colocamos o adaptador na listview
        final ListView listView = (ListView) findViewById(R.id.listviewAddTags);
        listView.setAdapter(adaptador);

        //Impede a seleçao das checkboxs pelo usuario


        //Seleciona os itens que for editar
        if (isModoEditar()) {
            if (itens != null)
                for (int i = 0; i < itens.size() - 1; i++){
                    int pos = array.indexOf(itens.get(i));
                    listView.setItemChecked(pos, true);

                    sparseBooleanArray = listView.getCheckedItemPositions();
                    itensSelecionados += array.toArray()[sparseBooleanArray.keyAt(i)] + ",";
                }
        }


        //A listview tem comportamento pra salvar e editar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                sparseBooleanArray = listView.getCheckedItemPositions();
                itensSelecionados = "";

                int i = 0;
                while (i < sparseBooleanArray.size()) {
                    if (sparseBooleanArray.valueAt(i)) {
                        itensSelecionados += array.toArray()[sparseBooleanArray.keyAt(i)] + ",";
                    }
                    i++;
                }
            }
        });


        //Comportamento para salvar a checklist
        View btnSalvar = findViewById(R.id.btnSalvarChecklist);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Salva e limpa a checklist
                salvaChecklist();
                limpaListView(listView);
            }
        });



        //Comportamento para cancelar salvar a tag
        View btnCancelar = findViewById(R.id.btnCancelarChecklist);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void limpaListView(ListView l){
        sparseBooleanArray = l.getCheckedItemPositions();
        itensSelecionados = "";

        int i = 0 ;
        while (i < sparseBooleanArray.size()) {
            if (sparseBooleanArray.valueAt(i)) {
                l.setItemChecked(i, false);
            }
            i++ ;
        }
    }

    public void salvaChecklist(){
        final EditText txtTitulo = (EditText) findViewById(R.id.txtTitleChecklist);

        //Verifica se vazio
        if (txtTitulo.getText().toString().isEmpty()) {
            txtTitulo.setError("Digite um título!");
            txtTitulo.setText("");
            return;
        }
        //Verifica se maior que 3 e 30 caracteres
        if (txtTitulo.getText().length() < 3 || txtTitulo.getText().length() > 30) {
            txtTitulo.setError("O título deve ter entre 3 e 30 caracteres!");
            txtTitulo.setText("");
            return;
        }


        if (!isModoEditar()) {
            //Verifica se existe checklist igual
            ArrayList<String> listaTotal = bd.leChecklist();

            for (int i = 0; i < listaTotal.size(); i++) {
                if (listaTotal.get(i).toLowerCase().contains(txtTitulo.getText().toString().toLowerCase())) {
                    txtTitulo.setError("Já existe uma checklist com esse título!");
                    txtTitulo.setText("");
                    return;
                }
            }
        }

        //Verifica se não há item selecionado
        if (itensSelecionados == ""){
            Toast.makeText(getApplicationContext(), "Selecione alguma tag!", Toast.LENGTH_LONG).show();
            return;
        }


        if (isModoEditar()) {

            int pos = bd.buscaIdChecklist(getTituloChecklist());


            if (bd.atualizaChecklists(txtTitulo.getText().toString(), pos, itensSelecionados)){
                Toast.makeText(this, R.string.checklist_updated,Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, getIntent());
                finish();
            } else Toast.makeText(this, R.string.checklist_notupdated,Toast.LENGTH_SHORT).show();



        } else if (bd.insereGeral(txtTitulo.getText().toString(), itensSelecionados)) {
            Toast.makeText(this, R.string.checklist_inserted,Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, getIntent());
            finish();
        }
        txtTitulo.setText("");
    }



}
