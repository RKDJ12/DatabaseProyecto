package com.kmo.databaseproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MqttMensaje extends AppCompatActivity {

    private TextView textViewMensaje;
    private EditText editTextMensaje;
    private MqttService mqttService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_mensaje);

        // Inicializar vistas
        textViewMensaje = findViewById(R.id.textViewMensaje);
        editTextMensaje = findViewById(R.id.editTextMensaje);
        Button buttonPublicarMensaje = findViewById(R.id.buttonPublicarMensaje);

        // Iniciar el servicio MQTT
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        startService(mqttServiceIntent);

        // Configurar el servicio para recibir mensajes
        mqttService = new MqttService();
        mqttService.setMessageListener(new MqttService.MessageListener() {
            @Override
            public void onMessageReceived(String message) {
                // Mostrar el mensaje recibido en el TextView
                textViewMensaje.setText("Mensaje recibido: " + message);
            }
        });

        // Configurar el botón para publicar un mensaje
        buttonPublicarMensaje.setOnClickListener(view -> onPublicarMensaje());
    }

    public void onPublicarMensaje() {
        String message = editTextMensaje.getText().toString();

        if (!message.isEmpty()) {
            Song song = new Song("Nuevo Título", "Nuevo Artista", "", "");  // Ejemplo de canción
            mqttService.publishSong(song);  // Aquí puedes enviar un mensaje con la canción

            // Limpiar el campo de texto después de publicar el mensaje
            editTextMensaje.setText("");
        }
    }

    public void onVerCanciones(View view) {
        // Lógica para mostrar canciones o realizar otra acción
    }
}
