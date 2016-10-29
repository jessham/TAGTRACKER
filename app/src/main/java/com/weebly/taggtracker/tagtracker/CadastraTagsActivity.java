package com.weebly.taggtracker.tagtracker;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class CadastraTagsActivity extends AppCompatActivity {
    DatabaseHelper bd;
    Toolbar toolbar;

    public CadastraTagsActivity() {
        bd = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_tags);

        //Coloca a toolbar que permite voltar
        toolbar = (Toolbar) findViewById(R.id.toolbar_cadastrachecklist);
        toolbar.getMenu().getItem(R.id.app_bar_delete).setVisible(false);
        toolbar.getMenu().getItem(R.id.app_bar_search).setVisible(false);
        toolbar.setTitle(R.string.title_CadastraTagsActivity);
        setSupportActionBar(toolbar);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Comportamento para salvar a tag
        View btnSalvar = findViewById(R.id.btnSalvarTag);
        final EditText txtTitulo = (EditText ) findViewById(R.id.txtTitleTag);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica se vazio
                if (txtTitulo.getText().toString().isEmpty()) {
                    txtTitulo.setError("Digite um rótulo!");
                    txtTitulo.setText("");
                    return;
                }
                //Verifica se maior que 3  30 caracteres
                if (txtTitulo.getText().length() < 3 || txtTitulo.getText().length() > 30) {
                    txtTitulo.setError("O rótulo deve ter entre 3 e 30 caracteres!");
                    txtTitulo.setText("");
                    return;
                }
                //Verifica se existe checklist igual
                ArrayList<String> listaTotal = bd.leTags();

                for (int i = 0; i < listaTotal.size(); i++) {
                    if (listaTotal.get(i).toLowerCase().contains(txtTitulo.getText().toString().toLowerCase())) {
                        txtTitulo.setError("Já existe uma checklist com esse título!");
                        txtTitulo.setText("");
                        return;
                    }
                }

                if (bd.insereTags(txtTitulo.getText().toString())) {
                    //Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }
                txtTitulo.setText("");
            }
        });

        //Comportamento para cancelar salvar a tag
        View btnCancelar = findViewById(R.id.btnCancelartag);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        } );
    }


}

