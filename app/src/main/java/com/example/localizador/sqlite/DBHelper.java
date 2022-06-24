package com.example.localizador.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String BANCO_LOCALIZACAO = "bd_Localizador";

    private static final String TABELA_LOCALIZACAO = "tb_Localizador";

    private static final String COLUNA_DATA = "data";
    private static final String COLUNA_USER = "usuario";
    private static final String COLUNA_LOC = "localizacao";
    private static final String COLUNA_DISTANCIA = "distancia";
    private static final String COLUNA_TEMPODESLOCAMENTO = "TempoDeslocamento";
    private static final String COLUNA_TEMPOIMOVEL = "TempoImovel";


    public DBHelper(Context context) {
        super(context, BANCO_LOCALIZACAO, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String QUERY_COLUNA = "CREATE TABLE " + TABELA_LOCALIZACAO
                + "(" + COLUNA_DATA + " TEXT, "
                + COLUNA_USER + " TEXT, "
                + COLUNA_LOC + " TEXT, "
                + COLUNA_DISTANCIA + " FLOAT, "
                + COLUNA_TEMPODESLOCAMENTO + " TEXT, "
                + COLUNA_TEMPOIMOVEL + " TEXT)";

        db.execSQL(QUERY_COLUNA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void addLocalizacao(Localizador localizador) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUNA_DATA, localizador.getDate());
        values.put(COLUNA_USER, localizador.getNome());
        values.put(COLUNA_LOC, localizador.getLocal());
        values.put(COLUNA_DISTANCIA, localizador.getDistancia());
        values.put(COLUNA_TEMPODESLOCAMENTO, localizador.getTempoDeslocamento());
        values.put(COLUNA_TEMPOIMOVEL, localizador.getTempoImovel());

        db.insert(TABELA_LOCALIZACAO, null, values);
        db.close();
    }

    /*public Localizador selecionarLocalizacao(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA_LOCALIZACAO, new String[] {COLUNA_DATA, COLUNA_USER,
                        COLUNA_LOC, COLUNA_DISTANCIA, COLUNA_TEMPODESLOCAMENTO,
                        COLUNA_TEMPOIMOVEL}, COLUNA_USER + " = ?",
                        new String[] {String.valueOf(nome)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Localizador localizador = new Localizador(
                cursor.getString(0), cursor.getString(1),
                cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                cursor.getString(4), cursor.getString(5));

        return localizador;

    }*/
}

