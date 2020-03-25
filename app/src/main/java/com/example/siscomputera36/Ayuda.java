package com.example.siscomputera36;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Ayuda extends AppCompatActivity {


    Button btn_close;

    TextView textTitulo;
    TextView textContenido;
    ImageView imageView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        btn_close = findViewById(R.id.close);
        textTitulo = findViewById(R.id.textTitulo);
        textContenido =  findViewById(R.id.textContenido);
        imageView5 = findViewById(R.id.imageView5);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });









    }
}
