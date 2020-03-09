package com.example.siscomputera36;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;


//todo se debe guardar primero el registro antes de tomar las fotos. deshabilitar el boton de guardar
// y que aparezca unmensaje de "registro almacenado"

public class MainReparaciones extends AppCompatActivity {
    EditText ed_fecha_in;
    EditText ed_fecha_entrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reparaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//eventos al presionar en las fechas
        ed_fecha_in=(EditText) findViewById(R.id.ed_fecha_in);
        ed_fecha_in.setInputType(InputType.TYPE_NULL);
        ed_fecha_entrega=(EditText) findViewById(R.id.ed_fecha_entrega);
        ed_fecha_entrega.setInputType(InputType.TYPE_NULL);


        ed_fecha_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(ed_fecha_in);
            }
        });

        ed_fecha_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(ed_fecha_entrega);
            }
        });
//hasta aqui eventos fechas


    }

    public void showDatePickerDialog(final EditText editText) {  //muestra la fecha
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(MainReparaciones.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
//        return editText;
    }




}
