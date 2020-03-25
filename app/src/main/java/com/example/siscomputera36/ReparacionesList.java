package com.example.siscomputera36;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class ReparacionesList extends AppCompatActivity {

    ListView reparaciones_listview;
    TextView txt_titulo;
    String linea1;
    int _Cliente_Id=0;
    String _Cadena_orden = " order by id";
    final ArrayList<String> lista_id = new ArrayList<>();
    final ArrayList<String> lista_cliente_id = new ArrayList<>();
    ArrayList<String> lista_fecha_in = new ArrayList<>();
    ArrayList<String> lista_equipo = new ArrayList<>();
    ArrayList<String> lista_fallas = new ArrayList<>();
    final ArrayList<String> lista_entaller = new ArrayList<>();
    ArrayList<String> lista_reparado = new ArrayList<>();
    String cadena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reparaciones_list);

// leer parametros de la actividad anterior
       _Cliente_Id = 0;
        Bundle b = getIntent().getExtras();
        _Cliente_Id = b.getInt("cliente_id");
        _Cadena_orden = b.getString("cadena_orden");
        if (_Cadena_orden == null) {_Cadena_orden = " order by id";}
 //leer datos de la cadena srting para abrir la bd
        if (_Cliente_Id == 0) {
            cadena = "Select * from reparaciones " + _Cadena_orden;
        }else{
            cadena = "Select * from reparaciones  where key_id = "
                    + _Cliente_Id + _Cadena_orden;
        }
// hasta aqui
        reparaciones_listview = findViewById(R.id.reparaciones_listview);
        txt_titulo = findViewById(R.id.txt_titulo);

        LlenarListas();
        mostrarlista();
    }

    public void buscarcliente(){
        //Abrimos la tabla Clientes para buscar su nombre y mostrarlo en el encabezado
        DBHelper dbHelper2 = new DBHelper(getApplicationContext());
        SQLiteDatabase db3 = dbHelper2.getWritableDatabase();
        Cursor cursor2 = db3.rawQuery("select * from clientes where key_id = "
                + String.valueOf(_Cliente_Id) , null);
        if (cursor2.moveToFirst()) {
            do {
                linea1 = " " + cursor2.getString(1); //obtenemos el nombre del cliente

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db3.close();
        String cadena1 = "Lista de Equipos de: " + linea1;
        txt_titulo.setText(cadena1);


    }
    private  void LlenarListas() {

        if (_Cliente_Id != 0) {
            buscarcliente();
        }
        lista_id.clear();
        lista_cliente_id.clear();
        lista_fecha_in.clear();
        lista_equipo.clear();
        lista_fallas.clear();
        lista_entaller.clear();
        lista_reparado.clear();
        // primero creamos las listas de cada campo de la bd, se debe colocar los valores de cada columna
        // en un array diferente
//        String cadena;
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db2 = dbHelper.getReadableDatabase();
        Cursor cursor = db2.rawQuery( cadena, null);
        if (cursor.moveToFirst()) {
            do {
                lista_id.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
                lista_cliente_id.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("key_id"))));
                lista_fecha_in.add(cursor.getString(cursor.getColumnIndex("fecha_in")));
                lista_equipo.add(cursor.getString(cursor.getColumnIndex("equipo")));
                lista_fallas.add(cursor.getString(cursor.getColumnIndex("fallas")));
                if (cursor.getString(cursor.getColumnIndex("en_taller")).equals("true")){
                    lista_entaller.add("En Taller");
                }else{lista_entaller.add("Entregado");}
                if ( cursor.getString(cursor.getColumnIndex("reparado")).equals("true") ){
                lista_reparado.add("Si");
                }else{lista_reparado.add("No"); }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db2.close();
    }

    public  ArrayList<HashMap<String,String>> ListaReparaciones() {
        final ArrayList<HashMap<String,String>> arraylist_datos=new ArrayList<>();
        for (int i=0; i<lista_cliente_id.size(); i++)
        {
            HashMap<String,String> hashMap = new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("cliente_id", lista_cliente_id.get(i));
            hashMap.put("fecha_in", lista_fecha_in.get(i));
            hashMap.put("equipo", lista_equipo.get(i));
            hashMap.put("fallas", lista_fallas.get(i));
            hashMap.put("entaller", lista_entaller.get(i));
            hashMap.put("reparado", lista_reparado.get(i));

            arraylist_datos.add(hashMap);//add the hashmap into arrayList
        }
        return arraylist_datos;
    }
    public void mostrarlista(){
        final ArrayList<HashMap<String,String>> arraylist_data = ListaReparaciones();

        String[] from={"cliente_id","fecha_in","equipo","fallas","entaller","reparado"};
        int[] to = {R.id.text_cliente_id,R.id.text_fecha_in, R.id.text_equipo, R.id.text_fallas, R.id.text_entaller, R.id.text_reparado};

        SimpleAdapter miAdapter=new SimpleAdapter(  //creamos el adaptador (simpleadapter)
                this,
                arraylist_data,
                R.layout.list_item_repara,   //plantilla del listitem personalizado
                from,
                to){

            public View getView(int i, View convertView, ViewGroup parent) {
                View view=super.getView(i, convertView, parent);

                String entaller = String.valueOf(lista_entaller.get(i)).trim();
                if (entaller.equals("En Taller")) {
                    view.setBackgroundResource(R.drawable.rectangle_r);
                }else{
                    view.setBackgroundResource(R.drawable.rectangle);
                }
                //                view.setBackgroundColor(Color.BLUE);
                return view;
            }

        };
        reparaciones_listview.setAdapter(miAdapter);
        reparaciones_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //int pos = position + 1;
                int _id = Integer.parseInt(lista_cliente_id.get(position));
                int _txt_id = Integer.parseInt(lista_id.get(position));
                Intent intent2 = new Intent(ReparacionesList.this, MainReparaciones.class);
                intent2.putExtra("cliente_id", _id);
                intent2.putExtra("rep_id", _txt_id);
                startActivity(intent2);


 //               Toast.makeText(ReparacionesList.this, "En Taller: " +  String.valueOf(lista_entaller.get(position)), Toast.LENGTH_LONG).show();
            }
        });
        reparaciones_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(ReparacionesList.this, "En Taller: " +  String.valueOf(lista_entaller.get(position)), Toast.LENGTH_LONG).show();
                // Devolvemos el valor true para evitar que se dispare
                // también el evento onListItemClick
                return true;
            }
        });
   }

   @Override
    protected void onPostResume () {
        super.onPostResume();
        LlenarListas();
        mostrarlista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reparaciones_list, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.search) {
            Toast.makeText(ReparacionesList.this, "buscar", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.nr_registros) {
            DBHelper dbHelper = new DBHelper(ReparacionesList.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM reparaciones", null);

            Toast.makeText(getApplicationContext(), "Registros totales:  " + c.getCount(), Toast.LENGTH_SHORT).show();
            c.close();
            db.close();
            return true;
        }
        if (id == R.id.refresh) {
            ReparacionesList.this.recreate();
            Toast.makeText(getApplicationContext(),"** Lista Recargada ** " , Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.todos) {
             Toast.makeText(getApplicationContext(),"** Mostrar todo el listado ** " , Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(ReparacionesList.this, ReparacionesList.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //cierra la activityanterior, o sea ella misma
            intent2.putExtra("cliente_id", _Cliente_Id);
            intent2.putExtra("cadena_orden", " order by id ");
            startActivity(intent2);

            return true;
        }
        if (id == R.id.entaller) {

            Intent intent2 = new Intent(ReparacionesList.this, ReparacionesList.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //cierra la activityanterior, o sea ella misma
            intent2.putExtra("cliente_id", _Cliente_Id);
            intent2.putExtra("cadena_orden", " order by en_taller desc ");
            startActivity(intent2);
            Toast.makeText(getApplicationContext(),"** Lista de equipos en taller ** " , Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.entregados) {
            Toast.makeText(getApplicationContext(),"** lista de equipos entregados ** " , Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(ReparacionesList.this, ReparacionesList.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //cierra la activityanterior, o sea ella misma
            intent2.putExtra("cliente_id", _Cliente_Id);
            intent2.putExtra("cadena_orden", " order by en_taller ");
            startActivity(intent2);
            return true;
        }
        if (id == R.id.reparados) {
            Toast.makeText(getApplicationContext(),"** lista de equipos reparados** " , Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(ReparacionesList.this, ReparacionesList.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //cierra la activityanterior, o sea ella misma
            intent2.putExtra("cliente_id", _Cliente_Id);
            intent2.putExtra("cadena_orden", " order by reparado desc ");
            startActivity(intent2);
            return true;
        }
        if (id == R.id.por_fecha) {
            Toast.makeText(getApplicationContext(),"** Lista de equipos ordenados por fecha ** " , Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(ReparacionesList.this, ReparacionesList.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //cierra la activityanterior, o sea ella misma
            intent2.putExtra("cliente_id", _Cliente_Id);
            intent2.putExtra("cadena_orden", " order by fecha_in desc ");
            startActivity(intent2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
