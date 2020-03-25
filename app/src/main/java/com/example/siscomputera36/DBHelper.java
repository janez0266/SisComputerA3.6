package com.example.siscomputera36;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by janez0266 on 20/02/20.
 */
public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 14;

    // Database Name
    private static final String DATABASE_NAME = "siscomputer.db";


    public static String CREATE_TABLE_CLIENTES = "CREATE TABLE clientes ("
            //tipo string con datos
            + "key_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "nombre TEXT, "
            + "telefono TEXT, "
            + "email TEXT, "
            + "nota TEXT)";

    public static String CREATE_TABLE_REPARACIONES = "CREATE TABLE reparaciones ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "key_id INTEGER, "
            + "fecha_in TEXT, "
            + "equipo TEXT, "
            + "caract TEXT, "
            + "acc TEXT, "
            + "fallas TEXT, "
            + "diag TEXT, "
            + "informe TEXT, "
            + "notas TEXT, "
            + "costo TEXT, "
            + "tecnico TEXT, "
            + "fecha_en TEXT, "
            + "en_taller TEXT, "
            + "reparado TEXT)";
    public static String CREATE_TABLE_FOTOS = "CREATE TABLE fotos ("
            //tipo string con datos
            + "reg_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "id INTEGER, "
            + "ruta TEXT, "
            + "nota TEXT)";

    DBHelper(Context context) {                        // constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {               // aqui se crea la base de datos
        //All necessary tables you like to create will create here

        db.execSQL(CREATE_TABLE_CLIENTES);
        db.execSQL(CREATE_TABLE_REPARACIONES);
        db.execSQL(CREATE_TABLE_FOTOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS reparaciones");
        db.execSQL("DROP TABLE IF EXISTS fotos");
        // Create tables again
        db.execSQL(CREATE_TABLE_CLIENTES);
        db.execSQL(CREATE_TABLE_REPARACIONES);
        db.execSQL(CREATE_TABLE_FOTOS);

//        onCreate(db);

    }

}