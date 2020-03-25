package com.example.siscomputera36;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainReparaciones extends AppCompatActivity {
    int key_id;
    EditText ed_fecha_in;
    EditText ed_fecha_entrega;
    EditText ed_equipo;
    EditText ed_caract;
    EditText ed_acc;
    EditText ed_fallas;
    EditText ed_diag;
    EditText ed_informe;
    EditText ed_notas;
    EditText ed_costo;
    EditText ed_tecnico;
    RadioGroup Rg_opcion;
    RadioButton Rb_enTaller;
    String _Rb_enTaller;
    RadioButton Rb_entregado;
    CheckBox chkbx_reparado;
    String _chkbx_reparado;
    TextView txt_id;
    TextView txt_nombre;
    Button btn_fotos;
    Button btn_guardar;
    Button btn_cerrar;
    public int _Cliente_Id=0;
    int _id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reparaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//eventos al presionar en las fechas
        ed_fecha_in= findViewById(R.id.ed_fecha_in);
        ed_fecha_in.setInputType(InputType.TYPE_NULL);
        ed_fecha_entrega= findViewById(R.id.ed_fecha_entrega);
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
        ed_equipo= findViewById(R.id.ed_equipo);
        ed_caract = findViewById(R.id.ed_caract);
        ed_acc = findViewById(R.id.ed_acc);
        ed_fallas = findViewById(R.id.ed_fallas);
        ed_diag = findViewById(R.id.ed_diag);
        ed_informe = findViewById(R.id.ed_informe);
        ed_notas = findViewById(R.id.ed_notas);
        ed_costo = findViewById(R.id.ed_costo);
        ed_tecnico = findViewById(R.id.ed_tecnico);
        Rb_enTaller = findViewById(R.id.Rb_enTaller);
        Rb_entregado = findViewById(R.id.Rb_entregado);
        chkbx_reparado= findViewById(R.id.chkbx_reparado);
        btn_fotos = findViewById(R.id.btn_fotos);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_cerrar = findViewById(R.id.btn_cerrar);
        txt_id = findViewById(R.id.txt_id);
        txt_nombre = findViewById(R.id.txt_nombre);
        _id = 0;
        _Cliente_Id =0;

//recuperamos los datos de la activity que envia
        Bundle b = getIntent().getExtras(); //traer el key_id del cliente para relacionar la BD
        _Cliente_Id = b.getInt("cliente_id");
        _id = b.getInt("rep_id");
//        Toast.makeText(getApplicationContext(), "Registro:  " + _id + " | Cliente: " + _Cliente_Id, Toast.LENGTH_LONG).show();

//Abrimos la tabla Clientes para buscar su nombre
        DBHelper dbHelper2 = new DBHelper(getApplicationContext());
        SQLiteDatabase db2 = dbHelper2.getWritableDatabase();
        Cursor cursor2 = db2.rawQuery("select nombre from clientes where key_id = "
                + String.valueOf(_Cliente_Id) , null);
        if (cursor2.moveToFirst()) {
            do {
                txt_nombre.setText(cursor2.getString(cursor2.getColumnIndex("nombre"))); //obtenemos el nombre del cliente

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db2.close();
        txt_id.setText(String.valueOf(_Cliente_Id));  //usamos la variable para el id del cliente y ponerlo en la de reparaciones
 //       chkbx_reparado.setChecked(true);   //se puede poner en true desde el layout
// ahora evaluamos si es un registro nuevo o un registro a actualizar
        // si _id = 0 entonces es un registro nuevo
        btn_fotos.setEnabled(false);
        Rb_enTaller.setChecked(true);
        if(_id != 0){   // si es diferente a 0 entonces debemos traer la data de la tabla Reparaciones para su edicion, usando el _id
            // buscamos y cargamos la data de reparaciones para el _id suministrado
            btn_fotos.setEnabled(true);
            leerRegistros();
        }

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_fecha_in.getText().toString().trim().equalsIgnoreCase("")){
                    ed_fecha_in.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "ERROR....Debe colocar la fecha de recibido.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (ed_equipo.getText().toString().trim().equalsIgnoreCase("")) {
                        ed_equipo.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "ERROR....Debe colocar la descripcion del equipo.", Toast.LENGTH_LONG).show();
                    } else {
                        guardarRegistro();
                    }
                }
            }
        });
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainReparaciones.this, TomarFoto.class);
                intent2.putExtra("rep_id", _id);
                intent2.putExtra("nombre",  txt_nombre.getText().toString());
                startActivity(intent2);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainReparaciones.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
    public void leerRegistros(){

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select key_id, fecha_in, equipo, caract, acc, fallas, diag, informe, notas, " +
                "costo, tecnico, fecha_en, en_taller, reparado from reparaciones where id = "
                + String.valueOf(_id) , null);
        if (cursor.moveToFirst()) {
            do {
                txt_id.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("key_id"))));
                ed_fecha_in.setText(cursor.getString(cursor.getColumnIndex("fecha_in")));
                ed_equipo.setText(cursor.getString(cursor.getColumnIndex("equipo")));
                ed_caract.setText(cursor.getString(cursor.getColumnIndex("caract")));
                ed_acc.setText(cursor.getString(cursor.getColumnIndex("acc")));
                ed_fallas.setText(cursor.getString(cursor.getColumnIndex("fallas")));
                ed_diag.setText(cursor.getString(cursor.getColumnIndex("diag")));
                ed_informe.setText(cursor.getString(cursor.getColumnIndex("informe")));
                ed_notas.setText(cursor.getString(cursor.getColumnIndex("notas")));
                ed_costo.setText(cursor.getString(cursor.getColumnIndex("costo")));
                ed_tecnico.setText(cursor.getString(cursor.getColumnIndex("tecnico")));
                _Rb_enTaller = cursor.getString(cursor.getColumnIndex("en_taller"));
                _chkbx_reparado = cursor.getString(cursor.getColumnIndex("reparado"));
                ed_fecha_entrega.setText(cursor.getString(cursor.getColumnIndex("fecha_en")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        if (_chkbx_reparado.equals("true")) {
            chkbx_reparado.setChecked(true);
        } else { chkbx_reparado.setChecked(false);}

        if (_Rb_enTaller.equals("true")) {
            Rb_enTaller.setChecked(true);
        } else {
            Rb_entregado.setChecked(true);
        }
    }
    public void guardarRegistro(){

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final ContentValues registro = new ContentValues();
        registro.put("key_id", _Cliente_Id);
        registro.put("fecha_in", ed_fecha_in.getText().toString());
        registro.put("equipo", ed_equipo.getText().toString());
        registro.put("caract", ed_caract.getText().toString());
        registro.put("acc", ed_acc.getText().toString());
        registro.put("fallas", ed_fallas.getText().toString());
        registro.put("diag", ed_diag.getText().toString());
        registro.put("informe", ed_informe.getText().toString());
        registro.put("notas", ed_notas.getText().toString());
        registro.put("costo", ed_costo.getText().toString());
        registro.put("tecnico", ed_tecnico.getText().toString());
        registro.put("fecha_en", ed_fecha_entrega.getText().toString());
        if (chkbx_reparado.isChecked()){ registro.put("reparado", "true");
        }else{registro.put("reparado", "false");}

        if (Rb_enTaller.isChecked()){ registro.put("en_taller", "true");
        }else{registro.put("en_taller", "false");}

        if(_id != 0) {
            db.update("reparaciones", registro, "id = ?",
                    new String[]{String.valueOf(_id)});
        }else{
            db.insert("reparaciones", null, registro);
            //se debe recuperar el id del nuevo registro agregado
            Cursor c = db.rawQuery("Select * from reparaciones", null);
            if (c.moveToLast()) {
                    _id =c.getInt(0);
            }
            c.close();
        }
        db.close();
        Toast.makeText(MainReparaciones.this, "Registro agregado exitosamente ", Toast.LENGTH_LONG).show();
        btn_fotos.setEnabled(true);
        btn_guardar.setEnabled(false);
//        finish();

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
    }

}
