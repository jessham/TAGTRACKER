package com.weebly.taggtracker.tagtracker;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;



public class TelaInicialActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener
{

    private NfcAdapter mNfcAdapter;
    private Dialog dialog;
    private DatabaseHelper bdhelper;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TelaChecklistActivity telaC;
    private Toolbar toolbar;

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        toolbar = (Toolbar) findViewById(R.id.toolbar_cadastrachecklist);
        setSupportActionBar(toolbar);


        bdhelper = new DatabaseHelper(this);
        bdhelper.getWritableDatabase();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //SOBRE AS TABS
        arrumaTabs();

        //SOBRE O BOTAO SUBMENU
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreMenu(view);
            }
        });


        //SOBRE A NAVEGACAO
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }





    /* ********************************************************************************************
        MÉTODOS DA TAB
     ******************************************************************************************** */

    public void arrumaTabs(){

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = (ViewPager) findViewById(R.id.pager);
        Pager adapter = new Pager(getSupportFragmentManager());

        telaC = new TelaChecklistActivity();
        telaC.instanciaBD(bdhelper);
        adapter.addFragment(telaC, "CHECKLISTS", false);

        TelaTagsActivity telaT = new TelaTagsActivity();
        telaT.instanciaBD(bdhelper);
        adapter.addFragment(telaT, "TAGS", false);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {    }






    /* ********************************************************************************************
        MÉTODOS DO BOTÃO FAB
     ******************************************************************************************** */
    //Gerencia o comportamento do botao fab
    public void abreMenu(final View _view){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

/*
        if (menuExpandido == true){
            menuExpandido = false;
            fab.setImageResource(ic_input_add);
            dialog.dismiss();
            return;

        } else {
            menuExpandido = true;
            fab.setImageResource(ic_menu_close_clear_cancel);
        }
*/
        dialog = new Dialog(TelaInicialActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_fab);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();

        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        dialog.setCanceledOnTouchOutside(true);


        //Comportamento para fechar o menu
        final View usaBtnFechar = dialog.findViewById(R.id.btnFechaMenu);
        usaBtnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Comportamento para menu Checklists
        View usaBtnChecklists =(View) dialog.findViewById(R.id.btnChecklists);
        usaBtnChecklists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregaAddChecklists();
                usaBtnFechar.performClick();
            }
        });

        //Comportamento para menu Tags
        View usaBtnTags =(View) dialog.findViewById(R.id.btnTags);
        usaBtnTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (verificaNFC(_view))
                carregaAddTags();
                usaBtnFechar.performClick();
            }
        });


        dialog.show();

    }

    //Abre a tela de cadastra checklist
    public void carregaAddChecklists(){
        // Toast.makeText(this, "Carrega a tela de add as checklist", Toast.LENGTH_LONG).show();
        CadastraChecklistsActivity cadastra = new CadastraChecklistsActivity();
        Intent it = new Intent(TelaInicialActivity.this, cadastra.getClass());
        startActivityForResult(it, 1);

    }

    //Abre a tela de cadastra tag
    public void carregaAddTags(){
        CadastraTagsActivity ct = new CadastraTagsActivity();
        Intent it = new Intent(TelaInicialActivity.this, ct.getClass());
        startActivityForResult(it, 2);
    }

    //Retorna o resultado das telas abertas
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Caso insere checklists ou tags, atualiza a exibição das tabs
        if (requestCode == 1 || requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                arrumaTabs();
            }
        }
    }






    /* ********************************************************************************************
        MÉTODOS DO NFC
     ******************************************************************************************** */
    //VERIFICA NFC AQUI
    public boolean verificaNFC(View view){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, R.string.no_nfc, Toast.LENGTH_LONG).show();
            //finish();
            return false;
        }

        if (!mNfcAdapter.isEnabled()) {
            Snackbar.make(view, R.string.nfc_disabled, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        } else {
            Snackbar.make(view, R.string.nfc_enabled, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }
    }







    /* ********************************************************************************************
        MÉTODOS DA TOOLBAR
     ******************************************************************************************** */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_toolbar, menu);
        apareceMenu(true, false);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.app_bar_search:
                pesquisa();
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
    public void apareceMenu(boolean pesquisa, boolean deleta){
        getToolbar().getMenu().findItem(R.id.app_bar_delete).setVisible(deleta);
        getToolbar().getMenu().findItem(R.id.app_bar_search).setVisible(pesquisa);
    }

    //Faz a pesquisa dos itens
    public void pesquisa(){

    }

    //Faz a deleção dos itens
    public void deleta(){

    }













    /* ********************************************************************************************
        MÉTODOS DE NAVEGAÇÃO
     ******************************************************************************************** */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lista_checklists) {
            // Ir para a tela inicial (lista de checklists)
            Intent intent = new Intent(this, TelaInicialActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_como_usar) {
            // Ir para a tela de como usar o app
            Intent intent = new Intent(this, ComoUsarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_compra_tags) {
            // Ir para a tela de compra de tags
            Intent intent = new Intent(this, CompraTagsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            // Ir para a tela sobre o app
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_premium) {
            // Ir para a tela sobre a versao premium
            Intent intent = new Intent(this, PremiumActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_testes) {
            // Ir para a tela sobre a versao premium
            Intent intent = new Intent(this, AlgunsTestes.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}

