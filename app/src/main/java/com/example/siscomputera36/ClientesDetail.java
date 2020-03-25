package com.example.siscomputera36;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Toast;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;

public class ClientesDetail extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 99;
    private Uri contactUri;
    Button btnSave;
    Button btnClose;
    EditText txtNombre;
    EditText txtTelefono;
    EditText txtEmail;
    EditText txtNota;
    TextView txtId;
    ImageView imageView3;

    int _Cliente_Id=0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);  //se salen los botones de abajo
        //setTheme(android.R.style.ThemeOverlay_Material_Dialog);  //ventana muy angosta
        //setTheme(android.R.style.Theme_Material_Dialog_Alert); //fondo gris oscuro. se salen los botones de abajo

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_detail);
        setTitle(" ");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ClientesDetail.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnSave = (Button) findViewById(R.id.btnSave);
        btnClose = (Button) findViewById(R.id.btnClose);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtNota = (EditText) findViewById(R.id.txtNota);
        txtId = (TextView) findViewById(R.id.txtId);
        imageView3 = findViewById(R.id.imageView);


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
                txtId.setText("Modificar Cliente: " + String.valueOf(cursor.getInt(cursor.getColumnIndex("key_id"))));
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
                    fila.close();
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
//todo **ojo** no funcionan contactos... El error esta al leer el nro telefonico y la imagen. el nombre lo lee bien
    public void initPickContacts(View v){
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                contactUri = intent.getData();
//                Toast.makeText(getApplicationContext(), "onactivityresult:-- " + contactUri, Toast.LENGTH_LONG).show();

                renderContact(contactUri);
            }
        }
    }

    private void renderContact(Uri uri) {
        txtNombre.setText(getName(uri));
//        txtTelefono.setText(getPhone(uri));
//        imageView3.setImageBitmap(getPhoto(uri));
    }

    private String getName(Uri uri) {
        String name = null;
        String phone = null;
        ContentResolver contentResolver = getContentResolver();

        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if(c.moveToFirst()){
//            name = c.getString(0);
            name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        }
        c.close();
        return name;
    }
    private String getPhone(Uri uri) {
        String id = null;
        String phone = null;
        //se busca el id del contacto, para luego buscar en la tabla de los nros telefonicos (tablas separadas)
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
        Toast.makeText(getApplicationContext(), "Contact ID:-- " + id, Toast.LENGTH_LONG).show();
//todo aqui esta el problema de los contactos
        //con el id del contacto, se busca en la tabla telefonos, y se selecciona el nro mobile (type_mobile)

        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE+" = " +
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




//        phone = "123456789";

        return phone;
    }


    private Bitmap getPhoto(Uri uri) {
        /*
        Foto del contacto y su id
         */
        Bitmap photo = null;
        String id = null;

        /************* CONSULTA ************/
        Cursor contactCursor = getContentResolver().query(
                uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /*
        Usar el método de clase openContactPhotoInputStream()
         */
        try {
            InputStream input =
                    ContactsContract.Contacts.openContactPhotoInputStream(
                            getContentResolver(),
                            ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI,
                                    Long.parseLong(id))
                    );
            if (input != null) {
                /*
                Dar formato tipo Bitmap a los bytes del BLOB
                correspondiente a la foto
                 */
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }

        } catch (IOException iox) { /* Manejo de errores */ }

        return photo;
    }
}