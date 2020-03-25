package com.example.siscomputera36;


import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ClientesView extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;
    Button btnLlamar;
    Button btnRepara;

    Button btnClose;
    Button btn_ver_lista;
    TextView txtNombre;
    TextView txtTelefono;
    TextView txtEmail;
    TextView txtNota;
    TextView txtId;

    public int _Cliente_Id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ClientesView.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnClose = (Button) findViewById(R.id.btnClose);
        btnRepara = (Button) findViewById(R.id.btnRepara);
        btn_ver_lista = findViewById(R.id.btn_ver_lista);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtTelefono = (TextView) findViewById(R.id.txtTelefono);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtNota = (TextView) findViewById(R.id.txtNota);
        txtId = (TextView) findViewById(R.id.txtId);

// para mostrar los datos que viene de clientesmain y editarlos
        _Cliente_Id =0;
        Bundle b=getIntent().getExtras();
        _Cliente_Id=b.getInt("cliente_id");

        vercliente();
        btnRepara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ClientesView.this, MainReparaciones.class);
                intent4.putExtra("cliente_id", _Cliente_Id);
                startActivity(intent4);

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_ver_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ClientesView.this, ReparacionesList.class);
                intent2.putExtra("cliente_id", _Cliente_Id);
                startActivity(intent2);

            }
        });
    }
    public void vercliente(){

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select key_id, nombre, telefono, email, nota from clientes where key_id = "
                + String.valueOf(_Cliente_Id) , null);
        if (cursor.moveToFirst()) {
            do {
                txtNombre.setText(cursor.getString(cursor.getColumnIndex("nombre")));
                txtTelefono.setText(cursor.getString(cursor.getColumnIndex("telefono")));
                txtEmail.setText(cursor.getString(cursor.getColumnIndex("email")));
                txtNota.setText(cursor.getString(cursor.getColumnIndex("nota")));
                txtId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("key_id"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
        vercliente();
    }

    public void sendMessage(){

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Enviar SMS")//Título del diálogo
                .setMessage("Esta seguro de enviar un Mensaje de Texto " +
                        "con la informacion bancaria al Cliente?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Enviar SMS", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SmsManager smsManager = SmsManager.getDefault();
                        String contactUri = txtTelefono.getText().toString();
                        if(contactUri!=null) {


                           // String strPhone = "04246422480";
                            String strMessage = "** Mi banco es BOD, cuenta corriente 0116 0103 19 0003319430 " +
                                    " Correo: janez0266@gmail.com **";

                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            sendIntent.putExtra("address", contactUri);
                            sendIntent.putExtra("sms_body", strMessage);
                            startActivity(sendIntent);
                            //Toast.makeText(ClientesView.this, "Mensaje Enviado", Toast.LENGTH_LONG).show();
                        }else
                            Toast.makeText(ClientesView.this, "Selecciona un contacto primero", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("Cancelar", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "Mensaje SMS cancelado", Toast.LENGTH_LONG).show();
                    }
                })
                .create();
        alertDialog.show();

    }
    //todo **ojo** no funciona llamar
    public void llamarContacto(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Desea realizar la llamada al contacto?");
        alertDialog.setTitle("Llamar a contacto...");
        alertDialog.setIcon(R.drawable.ic_warning);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                try
                {
                    TextView num=(TextView)findViewById(R.id.txtTelefono);
                    String number = "tel:" + num.getText().toString().trim();
                    Toast.makeText(getApplicationContext(),
                            "Llamando al " + num.getText().toString().trim(), Toast.LENGTH_LONG).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    startActivity(callIntent);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),
                            "No se ha podido realizar la llamada", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getApplicationContext(),
                        "Llamada cancelada", Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

/*
        if (id == R.id.add) {
            Intent intent3 = new Intent(ClientesView.this, ClientesDetail.class);
            intent3.putExtra("cliente_id", 0);
            startActivity(intent3);
            return true;
        }
*/

        if (id == R.id.ingreso) {
            Intent intent4 = new Intent(ClientesView.this, MainReparaciones.class);
            intent4.putExtra("cliente_id", _Cliente_Id);
            startActivity(intent4);
            return true;
        }
        if (id == R.id.edit) {
            Intent intent2 = new Intent(ClientesView.this, ClientesDetail.class);
            intent2.putExtra("cliente_id", _Cliente_Id);
            startActivity(intent2);
            return true;
        }
        if (id == R.id.delete) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Pop-up/Eliminar Registro: " + _Cliente_Id)
                    .setMessage("Esta seguro de Eliminar el registro?")
                    .setIcon(R.drawable.papelera)
                    .setPositiveButton("si", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            DBHelper dbHelper = new DBHelper(ClientesView.this);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.delete("clientes", "key_id = ?",
                                    new String[] { String.valueOf(_Cliente_Id) });

                            db.delete("reparaciones", "key_id= ?",
                                    new String[] { String.valueOf(_Cliente_Id) });
                            //TODO PARA BORRAR LAS FOTOS DE DEBE CREAR UN PROCEDIMIENTO PARA RECORRER
                            // LA TABLA REGISTRO A REGISTRO DEL CLIENTE Y BORRAR UNA A UNA LA FOTO, LUEGO SE ELIMINA DE LA bd

                            db.delete("fotos", "id= ?",
                                    new String[] { String.valueOf(_Cliente_Id) });
                            db.close();
                            Toast.makeText(getApplicationContext(),
                                    "Registro " + _Cliente_Id + " Eliminado", Toast.LENGTH_LONG).show();
                            finish();


                        }
                    })
                    .setNegativeButton("no", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //no hacer nada
                        }
                    })
                    .create();
            alertDialog.show();
            return true;
        }
        if (id == R.id.repara) {
            Intent intent2 = new Intent(ClientesView.this, ReparacionesList.class);
            intent2.putExtra("cliente_id", _Cliente_Id);
            startActivity(intent2);
            return true;
        }
        if (id == R.id.llamar) {
           llamarContacto();
            return true;
        }
        if (id == R.id.sms) {
            sendMessage();
            return true;
        }
        if (id == R.id.close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}