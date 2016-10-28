package com.weebly.taggtracker.tagtracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class AlgunsTestes extends AppCompatActivity {
    DatabaseUtil util;
    static String pasta = "/storage/3450-BF40/aulas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_alguns_testes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btnDB = (Button) findViewById(R.id.button2);
        final TextView result = (TextView) findViewById(R.id.result);
        btnDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String msg;
                if(isExternalStorageWritable()) {
                    //calling from MainActivity
                    util.copyDatabaseToExtStg(AlgunsTestes.this);
                    msg = "copia database " + util.FOLDER_EXTERNAL_DIRECTORY;

                } else msg = "ERRO";



                result.setText(msg);
            }
        });

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        return true;
    }

    public static class DatabaseUtil {
        //You need to declare permission
        // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        //in your Manifest file in order to use this class

        //______________________________________________________________________________________________

        //todo -> rename the database according to your application
        final static String DATABASE_NAME = "TAGTRACKER.db";
        //example WhatsApp :  /data/data/com.whatsapp/databases/msgstore.db
        final static String FOLDER_EXTERNAL_DIRECTORY = pasta;

        //______________________________________________________________________________________________
        /**
         * Call this method from any activity in your app (
         * for example ->    DatabaseUtil.copyDatabaseToExtStg(MainActivity.this);
         * this method will copy the database of your application into SDCard folder "shanraisshan/MyDatabase.sqlite" (DATABASE_NAME)
         */
        public static void copyDatabaseToExtStg(Context ctx) {
            //external storage file
            File externalDirectory = new File(FOLDER_EXTERNAL_DIRECTORY);
            if(!externalDirectory.exists())
                externalDirectory.mkdirs();
            File toFile = new File(externalDirectory, DATABASE_NAME);
            //internal storage file
            //https://developer.android.com/reference/android/content/Context.html#getDatabasePath(java.lang.String)
            File fromFile = ctx.getDatabasePath(DATABASE_NAME);
            //example WhatsApp :  /data/data/com.whatsapp/databases/msgstore.db
            if (fromFile.exists())
                copy(fromFile, toFile);


        }




        //______________________________________________________________________________________________ Utility function
        /**
         * @param fromFile source location
         * @param toFile destination location
         * copy file from 1 location to another
         */
        static void copy(File fromFile, File toFile) {
            try {
                FileInputStream is = new FileInputStream(fromFile);
                FileChannel src = is.getChannel();
                FileOutputStream os = new FileOutputStream(toFile);
                FileChannel dst = os.getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();	is.close();
                dst.close();	os.close();
            } catch (Exception e) {
                //todo in case of exception
                e.printStackTrace();
            }
        }
    }

}
