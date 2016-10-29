package com.weebly.taggtracker.tagtracker;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class PremiumActivity extends TelaInicialActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    PendingIntent pendingIntent;
    Tag mytag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cadastrachecklist);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(4).setChecked(true);

        mTextView = (TextView) findViewById(R.id.textViewPremium);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

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
                // get tag info
                mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                String toWriteOnTag = "ESCREVENDO NA TAG";
                writeTag(mytag, toWriteOnTag);
                mTextView.setText(readTag(mytag));
                Toast.makeText(this, "NFC Tag detected", Toast.LENGTH_LONG).show();
            }
        }
    }

    //private final String TAG = MifareUltralightTagTester.class.getSimpleName();

    public void writeTag(Tag tag, String tagText) {
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();

            /*final byte[] utf8Bytes = tagText.getBytes("UTF-8");
            int remainingChars = 40 - utf8Bytes.length;

            byte[] blankBytes = new byte[remainingChars];
            for (int i = 0; i < remainingChars; i++) {
                blankBytes[i] = ' ';
            }

            byte[] text = new byte[40];
            System.arraycopy(utf8Bytes, 0, text, 0, utf8Bytes.length);
            System.arraycopy(blankBytes, 0, text, utf8Bytes.length, blankBytes.length);*/

            /*int pageNumber = 1;
            for (int i = 0; i < 40; i=i+4) {
                ultralight.writePage(pageNumber, Arrays.copyOfRange(text, i, i + 4));
                pageNumber++;
            }*/

            //mTextView.setText(text.toString());

            ultralight.writePage(4, "Oi, ".getBytes(Charset.forName("UTF-8")));
            ultralight.writePage(5, "tudo".getBytes(Charset.forName("UTF-8")));
            ultralight.writePage(6, " bem".getBytes(Charset.forName("UTF-8")));
            ultralight.writePage(7, "? :)".getBytes(Charset.forName("UTF-8")));
            ultralight.writePage(8, "oi  ".getBytes(Charset.forName("UTF-8")));
            ultralight.writePage(9, "tudo".getBytes(Charset.forName("UTF-8")));
            ultralight.writePage(10, " bem".getBytes(Charset.forName("UTF-8")));

        } catch (IOException e) {
            //Log.e(TAG, "IOException while closing MifareUltralight...", e);
            mTextView.setText("Erro ao tentar fechar MifareUltralight 1.");
        } finally {
            try {
                ultralight.close();
            } catch (IOException e) {
                //Log.e(TAG, "IOException while closing MifareUltralight...", e);
                mTextView.setText("Erro ao tentar fechar MifareUltralight 2.");
            }
        }
    }

    public String readTag(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            byte[] payload = mifare.readPages(4);
            return new String(payload, Charset.forName("UTF-8"));
        } catch (IOException e) {
            mTextView.setText("Erro ao tentar ler tag 1.");
        } finally {
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (IOException e) {
                    //Log.e(TAG, "Error closing tag...", e);
                    mTextView.setText("Erro ao tentar ler tag 2.");
                }
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.premium, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
