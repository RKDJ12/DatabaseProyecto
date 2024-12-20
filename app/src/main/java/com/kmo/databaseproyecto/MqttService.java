package com.kmo.databaseproyecto;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttService {

    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String TOPIC = "songs/add";
    private MqttClient client;
    private MessageListener messageListener;

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    public MqttService() {
        try {
            client = new MqttClient(BROKER_URL, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e("MqttService", "Conexión perdida", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    // Notificar el mensaje recibido a la actividad
                    if (messageListener != null) {
                        messageListener.onMessageReceived(new String(message.getPayload()));
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("MqttService", "Entrega completada");
                }
            });
            client.connect(connectOptions);
        } catch (Exception e) {
            Log.e("MqttService", "Error al conectar con el broker", e);
        }
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public void publishSong(Song song) {
        try {
            String message = "Título: " + song.getTitle() + ", Artista: " + song.getArtist();
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(TOPIC, mqttMessage);
            Log.d("MqttService", "Mensaje publicado: " + message);
        } catch (Exception e) {
            Log.e("MqttService", "Error al publicar", e);
        }
    }
}
