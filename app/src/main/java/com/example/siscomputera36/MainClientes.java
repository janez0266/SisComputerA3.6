package com.example.siscomputera36;
//todo organizar las acividades y los layout en subcarpetas
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainClientes extends AppCompatActivity {
    ListView clientes_list;
    ArrayList<String> listado_clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clientes);

        clientes_list = (ListView) findViewById(R.id.clientes_list); //clientes_list es el id del listview del layout
        verListado();
    }

    private ArrayList<String> ListaPersonas() {

        ArrayList<String> datos = new ArrayList<String>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("Select * from clientes", null);
        if (c.moveToFirst()) {
            do {
                String linea1 = " " + c.getInt(0)
                        + " " + c.getString(1)
                        + " " + c.getString(2);
                //                       + " " + c.getString(3)
                //                       + " " + c.getString(4);
                datos.add(linea1);
            } while (c.moveToNext());
        }
        db.close();
        return datos;
    }

    public void verListado() {
        listado_clientes = ListaPersonas();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listado_clientes);
        clientes_list.setAdapter(adapter);
        clientes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //int pos = position + 1;
                int posi = Integer.parseInt(listado_clientes.get(position).split(" ")[1]);

                Intent intent2 = new Intent(MainClientes.this, ClientesView.class);
                intent2.putExtra("cliente_id", posi);
                startActivity(intent2);


//                Toast.makeText(getApplicationContext(), "Registro:  " + posi, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
        verListado();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add) {
            Intent intent3 = new Intent(MainClientes.this, ClientesDetail.class);
            intent3.putExtra("cliente_id", 0);
            startActivity(intent3);
            return true;
        }
        if (id == R.id.nr_registros) {
            DBHelper dbHelper = new DBHelper(MainClientes.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT key_id FROM clientes", null);
 //todo no funciona snackbar
 //           Snackbar.make(view, "Cantidad de registros: ", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Registros totales:  " + c.getCount(), Toast.LENGTH_SHORT).show();
            db.close();
            return true;
        }

        if (id == R.id.close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent3 = new Intent(MainClientes.this, ClientesDetail.class);
            intent3.putExtra("cliente_id", 0);
            startActivity(intent3);
            return true;
        }
        if (id == R.id.add) {
            Intent intent3 = new Intent(MainClientes.this, ClientesDetail.class);
            intent3.putExtra("cliente_id", 0);
            startActivity(intent3);
            return true;
        }
        if (id == R.id.nr_registros) {
            DBHelper dbHelper = new DBHelper(MainClientes.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT key_id FROM clientes", null);
//            Toast.makeText(getApplicationContext(), "Registros totales:  " + c.getCount(), Toast.LENGTH_SHORT).show();
            db.close();
            return true;
        }

        if (id == R.id.close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


*/


}