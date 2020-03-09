package com.example.siscomputera36;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class ClientesDetail extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;
    Button btnSave;
    Button btnClose;
    EditText txtNombre;
    EditText txtTelefono;
    EditText txtEmail;
    EditText txtNota;
    TextView txtId;

    int _Cliente_Id=0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_detail);

        btnSave = (Button) findViewById(R.id.btnSms);
        btnClose = (Button) findViewById(R.id.btnClose);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtNota = (EditText) findViewById(R.id.txtNota);
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
                //txtId.setText(cursor.getInt(cursor.getColumnIndex("key_id")));
                txtId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("key_id"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNombre.getText().toString().trim().equalsIgnoreCase(""))
                    txtNombre.setError("This field can not be blank");
                else{
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
// se leen las variables del layout
                    String telefono = txtTelefono.getText().toString();
                    String nombre = txtNombre.getText().toString();
                    String email = txtEmail.getText().toString();
                    String nota = txtNota.getText().toString();

                    Cursor fila = db.rawQuery("select nombre, telefono, email, nota from clientes where key_id=" + String.valueOf(_Cliente_Id), null);
//                Cursor fila = db.rawQuery("select nombre, telefono, email, nota from clientes where key_id=" + _Cliente_Id, null);
                    if (!fila.moveToFirst()) {  //devuelve true o false
                        ContentValues registro = new ContentValues();  //es una clase para guardar datos
// se copian los valores a la tabla
                        registro.put("telefono", telefono);
                        registro.put("nombre", nombre);
                        registro.put("email", email);
                        registro.put("nota", nota);
                        db.insert("clientes", null, registro);    // clientes: nombre de la tabla
                        Toast.makeText(getApplicationContext(), "Se cargaron los datos del cliente",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues registro = new ContentValues();  //es una clase para guardar datos
                        registro.put("telefono", telefono);
                        registro.put("nombre", nombre);
                        registro.put("email", email);
                        registro.put("nota", nota);
                        db.update("clientes", registro, "key_id = ?",
                                new String[]{String.valueOf(_Cliente_Id)});
                        //       new String[] { String.valueOf(_Cliente_Id) });
                        Toast.makeText(getApplicationContext(), "Cliente Actualizado", Toast.LENGTH_SHORT).show();

                    }
                    db.close();
                    _Cliente_Id = 0;
//                    limpiarCampos();
                    Toast.makeText(getApplicationContext(), "** Registro Agregado/Modificado con exito **", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
    public void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtNota.setText("");
        txtId.setText("");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_ventana2, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add:
                    //metodoAdd()
                    //info.setText("Se presionó Añadir");
                    Toast.makeText(getApplicationContext(), "Se presiono agregar ", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.search:
                    //metodoSearch()
                    //info.setText("Se presionó Buscar");
                    Toast.makeText(getApplicationContext(), "Se presionó Buscar", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.edit:
                    //metodoEdit()
                    //info.setText("Se presionó Editar");
                    Toast.makeText(getApplicationContext(), "Se presionó Editar", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.delete:
                    //metodoDelete()
                    // info.setText("Se presionó Eliminar");
                    Toast.makeText(getApplicationContext(), "Se presionó Eliminar", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_settings:
                    //metodoSettings()
                    //info.setText("Se presionó Ajustes");
                    Toast.makeText(getApplicationContext(), "Se presionó Ajustes", Toast.LENGTH_LONG).show();
                    return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */
//todo **ojo** no funcionan contactos
    public void initPickContacts(View v){
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }

    private void renderContact(Uri uri) {
        txtNombre.setText(getName(uri));
        txtTelefono.setText(getPhone(uri));
//        contactPic.setImageBitmap(getPhoto(uri));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                contactUri = intent.getData();
                renderContact(contactUri);
            }
        }
    }

    private String getPhone(Uri uri) {
        String id = null;
        String phone = null;
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);
        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();
        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE+"= " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                selectionArgs,
                new String[] { id },
                null
        );
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();
        return phone;
    }

    private String getName(Uri uri) {
        String name = null;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if(c.moveToFirst()){
            name = c.getString(0);
        }
        c.close();
        return name;
    }
}