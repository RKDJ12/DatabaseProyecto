package com.kmo.databaseproyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "music.db";
    private static final String MUSIC_TABLE_NAME = "music";

    // Nombres de las columnas
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE_CANCION = "nombre_cancion";
    private static final String KEY_ARTISTA = "artista";
    private static final String KEY_IMAGEN_CANCION = "imagen_cancion";

    // Sentencia SQL para crear la tabla
    private static final String MUSIC_TABLE_CREATE =
            "CREATE TABLE " + MUSIC_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // id como clave primaria y autoincremental
                    KEY_NOMBRE_CANCION + " TEXT, " +
                    KEY_ARTISTA + " TEXT, " +
                    KEY_IMAGEN_CANCION + " BLOB);"; // la imagen asociada (puede ser una URL o nombre de archivo)

    // Constructor
    public MusicOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método onCreate para crear la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MUSIC_TABLE_CREATE);
    }
    public boolean updateSong(int id, String nombreCancion, String artista, byte[] imagenCancion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE_CANCION, nombreCancion);
        values.put(KEY_ARTISTA, artista);
        values.put(KEY_IMAGEN_CANCION, imagenCancion);

        // Actualiza la fila donde el id coincide con el id de la canción
        int result = db.update(MUSIC_TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    // Método onUpgrade para manejar actualizaciones de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MUSIC_TABLE_NAME);
        onCreate(db);
    }
}

