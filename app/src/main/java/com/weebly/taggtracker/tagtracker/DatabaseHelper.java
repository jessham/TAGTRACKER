package com.weebly.taggtracker.tagtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.security.AccessController.getContext;
import static java.sql.DriverManager.println;

/**
 * Created by Barbara on 23/10/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TAGTRACKER.db";
    public Context contexto;

    public static abstract class tabelaChecklists implements BaseColumns {
        public static final String nomeTabela = "checklists";
        public static final String colunaID = "ID";
        public static final String colunaTitulo = "titulo";
    }

    public static abstract class tabelaTags implements BaseColumns {
        public static final String nomeTabela = "tags";
        public static final String colunaID = "ID";
        public static final String colunaTitulo = "titulo";
    }

    public static abstract class tabelaAssocia implements BaseColumns {
        public static final String nomeTabela = "associa";
        public static final String checklistsID = "checklistsID";
        public static final String tagsID = "tagsID";
    }


    private static final String SQL_CREATE_TABLE1 =
            "CREATE TABLE IF NOT EXISTS " + tabelaTags.nomeTabela + " (" +
                    tabelaTags.colunaID + " INTEGER PRIMARY KEY, " +
                    tabelaTags.colunaTitulo + " TEXT not null ); ";

    private static final String SQL_CREATE_TABLE2 =
                    "CREATE TABLE IF NOT EXISTS " + tabelaChecklists.nomeTabela + " (" +
                    tabelaChecklists.colunaID + " INTEGER PRIMARY KEY, " +
                    tabelaChecklists.colunaTitulo + " TEXT not null);" ;

    private static final String SQL_CREATE_TABLE3 =
                    "CREATE TABLE IF NOT EXISTS " + tabelaAssocia.nomeTabela + " ( " +
                    tabelaAssocia.tagsID + " INTEGER not null, " +
                    tabelaAssocia.checklistsID + " INTEGER not null, " +
                    "FOREIGN KEY (" + tabelaAssocia.tagsID + ") REFERENCES " +
                            tabelaTags.nomeTabela + "( " + tabelaTags.colunaID + "), " +
                    "FOREIGN KEY (" + tabelaAssocia.checklistsID + ") REFERENCES " +
                            tabelaChecklists.nomeTabela + "(" + tabelaChecklists.colunaID + "))";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + tabelaAssocia.nomeTabela + "; " +
                    "DROP TABLE IF EXISTS " + tabelaChecklists.nomeTabela + "; " +
                    "DROP TABLE IF EXISTS " + tabelaTags.nomeTabela;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.contexto = context;
    }

    public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQL_CREATE_TABLE1);
            db.execSQL(SQL_CREATE_TABLE2);
            db.execSQL(SQL_CREATE_TABLE3);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //INSERÇÃO
    public boolean insereGeral (String titulo, String rotulo){
        //insere o titulo da checklist

        if (!insereChecklist(titulo)) return false;

        //pega o id da checklist recém-criada
        int idchecklist = buscaIdChecklist(titulo);
        if (idchecklist == -1) return false;



        /*depois retorna o id das tags selecionadas*/
        List<String> lista = Arrays.asList(rotulo.split(","));

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++){
            String a = lista.get(i);
            //Toast.makeText(contexto,"PROCURA ITEM '" + a + "'",Toast.LENGTH_LONG).show();
            int ab = buscaIdTag(a);
            if (ab < 0 ) return false;
            else list.add(ab);
            if (list.get(i) == -1) return false;
        }


        //Finalmente, insere na tabela associa
        for (int i = 0; i < lista.size(); i++){
            if (!insereAssocia(idchecklist, list.get(i)))
                return false;
        }
        return true;
    }

    public boolean insereChecklist(String titulo) {
    /* primeiro insere o titulo da checklist */

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(tabelaChecklists.colunaID, String.valueOf(retornaProxIDChecklists()));
            values.put(tabelaChecklists.colunaTitulo, titulo);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            SQLiteDatabase db = getWritableDatabase();
            newRowId = db.insert(
                    tabelaChecklists.nomeTabela,
                    null,
                    values);
            db.close();
        } catch (Exception e){
            Toast.makeText(contexto, e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;
        }
       return true;
    }

    public boolean insereTags(String titulo) {

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(tabelaTags.colunaID, retornaProxIDTags());
            values.put(tabelaTags.colunaTitulo, titulo);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            SQLiteDatabase db = getWritableDatabase();
            newRowId = db.insert(
                    tabelaTags.nomeTabela,
                    null,
                    values);
            db.close();
            //Toast.makeText(contexto,R.string.tag_inserted,Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(contexto,R.string.tag_notinserted,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean insereAssocia(int idChecklists, int idTags) {
        // Gets the data repository in write mode

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(tabelaAssocia.checklistsID, idChecklists);
            values.put(tabelaAssocia.tagsID, idTags);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            SQLiteDatabase db = getWritableDatabase();
            newRowId = db.insert(
                    tabelaAssocia.nomeTabela,
                    null,
                    values);
            db.close();
            //Toast.makeText(contexto, R.string.checklist_inserted,Toast.LENGTH_SHORT).show();
        }  catch (Exception e){
            Toast.makeText(contexto, R.string.checklist_notinserted,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public int buscaIdTag(String rotulo){
        int resp = -1;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select " + tabelaTags.colunaID +
                    " from " + tabelaTags.nomeTabela + " where " +
                    tabelaTags.colunaTitulo + " = ?", new String[] {rotulo});

            String temp = "";
            if (cursor.moveToFirst()){
                temp = cursor.getString(cursor.getColumnIndex(tabelaTags.colunaID));
            }
            if (temp != "") resp = Integer.parseInt(temp);

            cursor.close();
            db.close();
            //Toast.makeText(contexto,"BUSCA TAG ID ()" + resp,Toast.LENGTH_LONG).show();
        }  catch (Exception e){
            Toast.makeText(contexto,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resp;
    }

    public int buscaIdChecklist(String titulo){
        int resp = -1;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select " + tabelaChecklists.colunaID +
                    " from " + tabelaChecklists.nomeTabela + " where " +
                    tabelaChecklists.colunaTitulo + " = ?" , new String[]  {titulo});

            String temp = "";
            if (cursor.moveToFirst()){
                temp = cursor.getString(cursor.getColumnIndex(tabelaChecklists.colunaID));
            }
            if (temp != "") resp = Integer.parseInt(temp);
            cursor.close();
            db.close();
            //Toast.makeText(contexto,"BUSCA CH ID ()" + resp,Toast.LENGTH_SHORT).show();
        }  catch (Exception e){
            Toast.makeText(contexto,"BUSCA CH ID " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resp;
    }


    //LEITURA
    public ArrayList<String> leChecklist(){
        ArrayList<String> resp = new ArrayList<String>();

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select " + tabelaChecklists.colunaTitulo +
                                                    " from " + tabelaChecklists.nomeTabela, null);

            if (cursor.moveToFirst()) {
                do {
                    String linea = cursor.getString(0);
                    resp.add(linea);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }  catch (Exception e){
            Toast.makeText(contexto,R.string.erro_exibirChecklist,Toast.LENGTH_SHORT).show();
        }

        return resp;
    }

    public ArrayList<String> leTags(){
        ArrayList<String> resp = new ArrayList<String>();

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select " + tabelaTags.colunaTitulo +
                                        " from " + tabelaTags.nomeTabela, null);

            if (cursor.moveToFirst()) {
                do {
                    String linea = cursor.getString(0);
                    resp.add(linea);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e){
            Toast.makeText(contexto,R.string.erro_exibirTags,Toast.LENGTH_SHORT).show();
        }

        return resp;
    }

    public ArrayList<String> leItensListas(String idChecklist) {
        ArrayList<String> resp = new ArrayList<String>();

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(" select " + tabelaTags.colunaTitulo + " from " + tabelaTags.nomeTabela +
                    " where " + tabelaTags.colunaID + " in ( select " + tabelaAssocia.tagsID +
                    " from " + tabelaAssocia.nomeTabela + " where " + tabelaAssocia.checklistsID + " = ? )", new String[] {idChecklist});

            if (cursor.moveToFirst()) {
                do {
                    String linea = cursor.getString(0);
                    resp.add(linea);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }  catch (Exception e){
            Toast.makeText(contexto,R.string.erro_leItens,Toast.LENGTH_LONG).show();
        }
        return resp;
    }


    //REMOÇÃO
    public boolean deletaAssocia(int checklistsID, int tagsID){

        if (tagsID != -1 && checklistsID != -1){

            try{
                // Define 'where' part of query.
                String selection = tabelaAssocia.checklistsID + " = ? and " + tabelaAssocia.tagsID + " = ? ";
                // Specify arguments in placeholder order.
                String[] selectionArgs = {String.valueOf(checklistsID), String.valueOf(tagsID)};
                // Issue SQL statement.


                SQLiteDatabase db = getWritableDatabase();
                db.delete(tabelaAssocia.nomeTabela, selection, selectionArgs);

                db.close();
                return true;

            }  catch (Exception e){
                Toast.makeText(contexto,"ERROR DEL ASS 1 " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        } else if (tagsID == -1) {

            try{
                String selection2 = tabelaAssocia.checklistsID + " = ?";
                String[] selectionArgs2 = { String.valueOf(checklistsID) };

                SQLiteDatabase db = getWritableDatabase();
                db.delete(tabelaAssocia.nomeTabela, selection2, selectionArgs2);

                db.close();
                return true;

            }  catch (Exception e){
                Toast.makeText(contexto,"ERROR DEL ASS 2 " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        } else if (checklistsID == -1){

            try{
                String selection3 = tabelaAssocia.tagsID + " = ?";
                String[] selectionArgs3 = { String.valueOf(tagsID) };

                SQLiteDatabase db = getWritableDatabase();
                db.delete(tabelaAssocia.nomeTabela, selection3, selectionArgs3);

                db.close();
                return true;

            }  catch (Exception e){
                Toast.makeText(contexto,"ERROR DEL ASS 3 " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }

    public boolean deletaChecklists(int checklistsID){
        try {

            // Define 'where' part of query.
            String selection = tabelaChecklists.colunaID + " = ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(checklistsID)};


            if (!deletaAssocia(checklistsID, -1))
                return false;


            // Issue SQL statement.
            SQLiteDatabase db = getWritableDatabase();
            db.delete(tabelaChecklists.nomeTabela, selection, selectionArgs);


            db.close();
            return true;

        }  catch (Exception e){
            Toast.makeText(contexto,"ERROR DEK CH " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean deletaTags(int tagsID){

        try {
            // Define 'where' part of query.
            String selection = tabelaTags.colunaID + " = ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(tagsID)};


            if (!deletaAssocia(-1, tagsID))
                return false;

            // Issue SQL statement.
            SQLiteDatabase db = getWritableDatabase();
            db.delete(tabelaTags.nomeTabela, selection, selectionArgs);


            db.close();
            return true;

        }  catch (Exception e){
            Toast.makeText(contexto,"ERROR DEL TAG" + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return false;
    }



    //ATUALIZAÇÃO

    public boolean atualizaAssocia(int checklistsID, int tagsID){

        if (tagsID != -1 && checklistsID != -1){

            try{
                // Define 'where' part of query.
                String selection = tabelaAssocia.checklistsID + " = ? and " + tabelaAssocia.tagsID + " = ? ";
                // Specify arguments in placeholder order.
                String[] selectionArgs = {String.valueOf(checklistsID), String.valueOf(tagsID)};
                // Issue SQL statement.


                SQLiteDatabase db = getWritableDatabase();
                db.delete(tabelaAssocia.nomeTabela, selection, selectionArgs);

                db.close();
                return true;

            }  catch (Exception e){
                Toast.makeText(contexto,"ERROR DEL ASS 1 " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        } else if (tagsID == -1) {

            try{
                String selection2 = tabelaAssocia.checklistsID + " = ?";
                String[] selectionArgs2 = { String.valueOf(checklistsID) };

                SQLiteDatabase db = getWritableDatabase();
                db.delete(tabelaAssocia.nomeTabela, selection2, selectionArgs2);

                db.close();
                return true;

            }  catch (Exception e){
                Toast.makeText(contexto,"ERROR DEL ASS 2 " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        } else if (checklistsID == -1){

            try{
                String selection3 = tabelaAssocia.tagsID + " = ?";
                String[] selectionArgs3 = { String.valueOf(tagsID) };

                SQLiteDatabase db = getWritableDatabase();
                db.delete(tabelaAssocia.nomeTabela, selection3, selectionArgs3);

                db.close();
                return true;

            }  catch (Exception e){
                Toast.makeText(contexto,"ERROR DEL ASS 3 " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }

    public boolean atualizaChecklists(String titulo, int checklistsID) {
        try {
            //Toast.makeText(contexto, "Começa atualiza para o id = " + String.valueOf(checklistsID), Toast.LENGTH_LONG).show();
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(tabelaChecklists.colunaTitulo, titulo);


            // Which row to update, based on the ID
            String selection = tabelaChecklists.colunaID + " = ?";
            String[] selectionArgs = {String.valueOf(checklistsID)};

            int count = getWritableDatabase().update(
                    tabelaChecklists.nomeTabela,
                    values,
                    selection,
                    selectionArgs);
            Toast.makeText(contexto,R.string.checklist_updated,Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e){
            Toast.makeText(contexto,R.string.checklist_notupdated,Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean atualizatags(String titulo, int tagsID) {
        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(tabelaTags.colunaTitulo, titulo);

            // Which row to update, based on the ID
            String selection = tabelaTags.colunaID + " = ?";
            String[] selectionArgs = {String.valueOf(tagsID)};

            int count = getWritableDatabase().update(
                    tabelaTags.nomeTabela,
                    values,
                    selection,
                    selectionArgs);

            return true;
        }  catch (Exception e){
            Toast.makeText(contexto, "Erro ao editar a tag",Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    //BUSCA
    public int retornaProxIDTags(){
        int id = 0;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select coalesce( max( " + tabelaTags.colunaID
                                        + " ), 0) from " + tabelaTags.nomeTabela, null);

            cursor.moveToFirst();
            id = cursor.getInt(0);

            cursor.close();
            db.close();
        }  catch (Exception e){}
        return id + 1;
    }

    public int retornaProxIDChecklists(){
        int id = 0;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select coalesce( max( id ), 0) from "
                    + tabelaChecklists.nomeTabela, null);

            cursor.moveToFirst();
            id = cursor.getInt(0);

            cursor.close();
            db.close();
        }  catch (Exception e){}
        return id + 1;
    }
}


