package com.kmo.databaseproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashSC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sc);

        // Retraso de 3 segundos antes de abrir el MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashSC.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza SplashActivity para que no vuelva con "back"
        }, 3000); // 3000 ms = 3 segundos
    }
}