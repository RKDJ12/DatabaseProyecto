package com.kmo.databaseproyecto;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocalStorageHelper {

    // Guardar audio en almacenamiento interno
    public static void saveAudioToInternalStorage(Context context, Uri audioUri, String fileName) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(audioUri);
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            inputStream.close();
            Toast.makeText(context, "Audio guardado en almacenamiento interno", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el audio", Toast.LENGTH_SHORT).show();
        }
    }

    // Guardar audio en almacenamiento externo
    public static void saveAudioToExternalStorage(Context context, Uri audioUri, String fileName) {
        // Ruta al almacenamiento externo
        File externalDir = new File(Environment.getExternalStorageDirectory(), "MyApp");
        if (!externalDir.exists()) {
            externalDir.mkdirs();
        }
        File audioFile = new File(externalDir, fileName);

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(audioUri);
            FileOutputStream fos = new FileOutputStream(audioFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            inputStream.close();
            Toast.makeText(context, "Audio guardado en almacenamiento externo", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el audio", Toast.LENGTH_SHORT).show();
        }
    }

    // Leer el archivo de audio desde almacenamiento interno
    public static File getAudioFromInternalStorage(Context context, String fileName) {
        return new File(context.getFilesDir(), fileName);
    }

    // Leer el archivo de audio desde almacenamiento externo
    public static File getAudioFromExternalStorage(String fileName) {
        File externalDir = new File(Environment.getExternalStorageDirectory(), "MyApp");
        return new File(externalDir, fileName);
    }
}
