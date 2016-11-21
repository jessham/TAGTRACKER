package com.weebly.taggtracker.tagtracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;

public class VisualizaTagActivity extends AppCompatActivity {
    private DatabaseHelper bd = new DatabaseHelper(this);
    private String rotuloTag; //tag que será visualizada ou editada
    private Toolbar toolbar;
    private ArrayList<String> array;


    /* *********************************************************************************************
     * GETTERS E SETTERS
     * ********************************************************************************************/


    public String getRotuloTag() {
        return rotuloTag;
    }

    public void setRotuloTag(String rotuloTag) {
        this.rotuloTag = rotuloTag;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    VisualizaTagActivity(String rotulo){
        setRotuloTag(rotulo);
    }

    public VisualizaTagActivity(){

    }



    /* *********************************************************************************************
     * MÉTODOS DE INICIALIZAÇÃO
     * ********************************************************************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_tag);

        //Configura a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_visualizaTag);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setRotuloTag(extras.getString("tag-visual-key"));

            //Comportamento dos itens texto
            TextView titulo = (TextView) findViewById(R.id.txtTitleVisualTag);
            titulo.setText(getRotuloTag());
        }


        //Comportamento para checar a checklist
        View btnCheck = findViewById(R.id.btnChecarTag);
        btnCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //faz alguma coisa
            }
        });


        //Comportamento para cancelar visualizar a tag
        View btnCancelar = findViewById(R.id.btnCancelarVisualTag);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    /* *********************************************************************************************
     * TOOLBAR E MÉTODOS
     * ********************************************************************************************/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_toolbar, menu);
        apareceMenu(false, true, true);
        toolbar.setTitle(R.string.title_VisualizaTagsActivity);
        toolbar.getMenu().findItem(R.id.app_bar_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.getMenu().findItem(R.id.app_bar_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.app_bar_edit:
                edita();
                return true;


            case R.id.app_bar_delete:
                deleta();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //Configura as opções visiveis do menu da toolbar
    public void apareceMenu(boolean pesquisa, boolean deleta, boolean edita){
        getToolbar().getMenu().findItem(R.id.app_bar_delete).setVisible(deleta);
        getToolbar().getMenu().findItem(R.id.app_bar_search).setVisible(pesquisa);
        getToolbar().getMenu().findItem(R.id.app_bar_edit).setVisible(edita);
    }

    //Deleta a tag, confirma primeiro
    public void deleta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.msg_deleteThisTag);

        builder.setPositiveButton(R.string.btnYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletaTag();
                dialog.dismiss();
            }

        });

        builder.setNegativeButton(R.string.btnNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void deletaTag(){
        //Deleta a tag e termina a atividade
        bd.deletaTags(bd.buscaIdTag(getRotuloTag()));
        finish();
    }

    //Edita a tag
    public void edita(){
        CadastraTagsActivity ct = new CadastraTagsActivity();
        Intent it = new Intent(this, ct.getClass());
        it.putExtra("tag-edit-key", getRotuloTag());
        startActivity(it);

        finish();
    }

}