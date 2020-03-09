package com.example.siscomputera36;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class ClientesView extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;
    Button btnLlamar;
    Button btnRepara;
    Button btnSms;
    Button btnClose;
    TextView txtNombre;
    TextView txtTelefono;
    TextView txtEmail;
    TextView txtNota;
    TextView txtId;

    int _Cliente_Id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_view);

        btnClose = (Button) findViewById(R.id.btnClose);
        btnRepara = (Button) findViewById(R.id.btnRepara);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtTelefono = (TextView) findViewById(R.id.txtTelefono);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtNota = (TextView) findViewById(R.id.txtNota);
        txtId = (TextView) findViewById(R.id.txtId);

// para mostrar los datos que viene de clientesmain y editarlos
        _Cliente_Id =0;
        Bundle b=getIntent().getExtras();
        _Cliente_Id=b.getInt("cliente_id");

//leer al cliente de la tabla
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


        btnRepara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //todo **ojo** no funciona enviar mensaje y llamar
    public void sendMessage(View v){

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Enviar SMS")//Título del diálogo
                .setMessage("Esta seguro de enviar un Mensaje de Texto " +
                        "con la informacion bancaria al Cliente?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Enviar SMS", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SmsManager smsManager = SmsManager.getDefault();
                        String contactUri = txtTelefono.getText().toString();
                        if(contactUri!=null) {
                            smsManager.sendTextMessage(
                                    contactUri,
                                    null,
                                    "** Mi banco es BOD, cuenta corriente 0116 0103 19 0003319430 " +
                                            "a nombre de: Julio Añez, CI: 7.774.094. " +
                                            "Correo: janez0266@gmail.com **",
                                    null,
                                    null);

                            Toast.makeText(ClientesView.this, "Mensaje Enviado", Toast.LENGTH_LONG).show();
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
    public void llamarContacto(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Desea realizar la llamada al contacto?");
        alertDialog.setTitle("Llamar a contacto...");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
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
        if (id == R.id.add) {
            Intent intent3 = new Intent(ClientesView.this, ClientesDetail.class);
            intent3.putExtra("cliente_id", 0);
            startActivity(intent3);
            return true;
        }
        if (id == R.id.search) {

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

            return true;
        }
        if (id == R.id.llamar) {
            llamarContacto();
            return true;
        }
        if (id == R.id.close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}