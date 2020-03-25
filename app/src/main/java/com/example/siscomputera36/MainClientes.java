package com.example.siscomputera36;

//todo gregar en el menu el orden que se necesita para los registros (por nombre)
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainClientes extends AppCompatActivity {
    ListView clientes_list;
    ArrayList<String> listado_clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clientes);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent3 = new Intent(MainClientes.this, ClientesDetail.class);
                intent3.putExtra("cliente_id", 0);
                startActivity(intent3);
            }
        });
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
                        + " \n Tlf: " + c.getString(2);
                //                       + " " + c.getString(3)
                //                       + " " + c.getString(4);
                datos.add(linea1);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return datos;
    }

    public void verListado() {
        listado_clientes = ListaPersonas();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,         // LAYOUT TIPO ITEMVIEW PERSONALIZAD  list_item.xml
                R.id.text,                  // textview a usar.. esta dentro de  list_item.xml
                listado_clientes);          //DATOS DEL ARREGLO QUE SE MOSTRARA

        clientes_list.setAdapter(adapter);
        clientes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //int pos = position + 1;
                int _key_id = Integer.parseInt(listado_clientes.get(position).split(" ")[1]);

                Intent intent2 = new Intent(MainClientes.this, ClientesView.class);
                intent2.putExtra("cliente_id", _key_id);
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

            Toast.makeText(getApplicationContext(), "Registros totales:  " + c.getCount(), Toast.LENGTH_SHORT).show();
            c.close();
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
