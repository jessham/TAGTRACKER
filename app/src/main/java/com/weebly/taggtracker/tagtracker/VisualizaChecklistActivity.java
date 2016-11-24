package com.weebly.taggtracker.tagtracker;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import static java.sql.DriverManager.println;

public class VisualizaChecklistActivity extends AppCompatActivity {
    private DatabaseHelper bd = new DatabaseHelper(this);
    private String tituloChecklist; //checklist que será visualizada ou editada
    private Toolbar toolbar;
    private ArrayList<String> array;
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;
    private Tag mytag;
    private ListView listView;

    /* *********************************************************************************************
     * GETTERS E SETTERS
     * ********************************************************************************************/


    public String getTituloChecklist() {
        return tituloChecklist;
    }

    public void setTituloChecklist(String tituloChecklist) {
        this.tituloChecklist = tituloChecklist;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public DatabaseHelper getBd() {
        return bd;
    }

    public void setBd(DatabaseHelper bd) {
        this.bd = bd;
    }

    VisualizaChecklistActivity(String titulo){
        setTituloChecklist(titulo);
    }

    public VisualizaChecklistActivity(){}



    /* *********************************************************************************************
     * MÉTODOS DE INICIALIZAÇÃO
     * ********************************************************************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_checklist);

        //Configura a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_visualizachecklist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Habilitando o NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setTituloChecklist(extras.getString("checklist-visual-key"));

            //Comportamento dos itens texto
            TextView titulo = (TextView) findViewById(R.id.txtTitleVisualChecklist);
            titulo.setText(getTituloChecklist());
        }


        //Criamos o array de dados e o colocamos no adapter
        final ArrayAdapter<String> adaptador;

        String ti = getTituloChecklist();
        int d =  bd.buscaIdChecklist(getTituloChecklist());

        array = bd.leItensListas(String.valueOf(d));
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, array);



        //Colocamos o adaptador na listview
        listView = (ListView) findViewById(R.id.listviewTags);

        if (adaptador == null){
            finish();
        }
        listView.setAdapter(adaptador);
        listView.setEnabled(false);


    }


    /* *********************************************************************************************
     * TOOLBAR E MÉTODOS
     * ********************************************************************************************/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_toolbar, menu);
        apareceMenu(false, true, true);
        toolbar.setTitle(R.string.title_VisualizaChecklistsActivity);
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

    //Deleta a checklist, confirma primeiro
    public void deleta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.msg_deleteThisChecklist);

        builder.setPositiveButton(R.string.btnYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletaChecklist();
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

    public void deletaChecklist(){
        //Deleta a checklist e termina a atividade
        bd.deletaChecklists(bd.buscaIdChecklist(getTituloChecklist()));
        finish();
    }

    //Edita a checklist
    public void edita(){
        CadastraChecklistsActivity ct = new CadastraChecklistsActivity();
        Intent it = new Intent(this, ct.getClass());
        array.add(getTituloChecklist());
        it.putExtra("checklist-edit-key", array);
        startActivity(it);

        finish();
    }

    /* ********************************************************************************************
        MÉTODOS RELACIONADOS AO NFC
     ******************************************************************************************** */

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */

        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */

        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent){
        if (mNfcAdapter != null) {
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                int detectedTagId = readTag(mytag);
                for (int i=0; i <= listView.getLastVisiblePosition(); i++) {
                    if (bd.buscaIdTag(array.get(i)) == detectedTagId) {
                        Toast.makeText(this, array.get(i) + " detectado(a)!", Toast.LENGTH_LONG).show();
                        listView.setItemChecked(i,true);
                        if (listView.getLastVisiblePosition()+1 == listView.getCheckedItemCount()) {
                            // checklist com todos os itens checados

                            // 1. Instantiate an AlertDialog.Builder with its constructor
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            // 2. Chain together various setter methods to set the dialog characteristics
                            builder.setMessage("Todos os itens foram detectados!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                            // 3. Get the AlertDialog from create()
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                }
            }
        }
    }

    public int readTag(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            byte[] payload = mifare.readPages(4);
            byte[] fourBytes = Arrays.copyOfRange(payload,0,4);
            String fourBytesToString = new String(fourBytes, Charset.forName("UTF-8"));
            int idNumber = Integer.parseInt(new String(fourBytesToString));

            return idNumber;    //new String(Integer.toString(idNumber));
        } catch (IOException e) {
            Toast.makeText(this, "Ocorreu um problema ao ler a tag. Tente novamente.", Toast.LENGTH_LONG).show();
        } finally {
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (IOException e) {
                    Toast.makeText(this, "Ocorreu um problema ao ler a tag. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
        return -1;
    }

}