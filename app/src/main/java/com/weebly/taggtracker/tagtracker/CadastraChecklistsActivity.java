package com.weebly.taggtracker.tagtracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.weebly.taggtracker.tagtracker.models.CheckList;

import java.util.ArrayList;

public class CadastraChecklistsActivity extends AppCompatActivity {
    DatabaseHelper bd;
    MultiAutoCompleteTextView autocompleta;
    SparseBooleanArray sparseBooleanArray ;
    String itensSelecionados = "" ;



    public CadastraChecklistsActivity() {
        bd = new DatabaseHelper(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_checklists);


        //Criamos o array de dados e o colocamos no adapter
        final ArrayList array = bd.leTags();
        final ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, array);

        //Colocamos o adaptador na listview
        final ListView listView = (ListView) findViewById(R.id.listviewAddTags);
        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                sparseBooleanArray = listView.getCheckedItemPositions();
                itensSelecionados = "";

                int i = 0 ;
                while (i < sparseBooleanArray.size()) {
                    if (sparseBooleanArray.valueAt(i)) {
                        itensSelecionados += array.toArray()[sparseBooleanArray.keyAt(i)] + ",";
                    }
                     i++ ;
                }
            }
        });



        //Comportamento para salvar a checklist
        View btnSalvar = findViewById(R.id.btnSalvarChecklist);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
                //Verifica se existe checklist igual
                ArrayList<String> listaTotal = bd.leChecklist();

                for (int i = 0; i < listaTotal.size(); i++) {
                    if (listaTotal.get(i).toLowerCase().contains(txtTitulo.getText().toString().toLowerCase())) {
                        txtTitulo.setError("Já existe uma checklist com esse título!");
                        txtTitulo.setText("");
                        return;
                    }
                }

                //Verifica se não há item selecionado
                if (itensSelecionados == ""){
                    Toast.makeText(getApplicationContext(), "Selecione alguma tag!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (bd.insereGeral(txtTitulo.getText().toString(), itensSelecionados)) {
                    //Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }
                txtTitulo.setText("");


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

}
