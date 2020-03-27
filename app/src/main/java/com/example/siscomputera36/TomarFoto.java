package com.example.siscomputera36;

//import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TomarFoto extends AppCompatActivity implements View.OnClickListener {
    private ImageView mPhotoImageView;
    private ImageView imageCamara;
    private ImageView help;
    private Button btn_tomarFoto;
    public static final int REQUEST_CODE_TAKE_PHOTO = 0 /*1*/;
    public Uri photoURI;
    private TextView textView;
    private TextView txt_cliente;
    private EditText editText_nota;
    int _id;
    String cliente;
    ListView fotos_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);
        setTitle("Fotos del equipo Recibido");

        Bundle b = getIntent().getExtras(); //traer el key_id del cliente para relacionar la BD
        assert b != null;
        cliente= b.getString("nombre");
        _id = b.getInt("rep_id");
        // Views
        mPhotoImageView = (ImageView) findViewById(R.id.imgPhotoUser);
        imageCamara = findViewById(R.id.imageCamara);
        help = findViewById(R.id.help);

        btn_tomarFoto = findViewById(R.id.btn_tomarFoto);
        fotos_list = findViewById(R.id.fotos_list);

//        textView =  findViewById(R.id.editText);
        editText_nota = findViewById(R.id.editText_nota);

        txt_cliente = findViewById(R.id.text_cliente);
        txt_cliente.setText(cliente);
        // Listeners
//        mPhotoImageView.setOnClickListener(TomarFoto.this);
        btn_tomarFoto.setOnClickListener(TomarFoto.this);
        help.setOnClickListener(TomarFoto.this);
        mostrarLista();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_tomarFoto) {
            tomarfoto();
        }
        if (v == help){
            AlertDialog alertDialog = new AlertDialog.Builder(TomarFoto.this)
                    .setTitle("FileExplorer de Fotos " + _id)
                    .setMessage("1.- para tomar una foto, coloque la camara en posicion Horizontal.\n" +
                            "2.- Si quiere colocar una nota, escribala antes de tomar la foto \n" +
                            "3.- Para ver las fotos del listado, presione sobre ellas.\n" +
                            "4.- para eliminar una foto del listado, deje presionado el item hasta que salga el dialogo de eliminar")
                    .setIcon(R.drawable.ic_menu_camera)

                    .setNegativeButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //no hacer nada
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }

    public void tomarfoto(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        225);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        226);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                ContentValues values = new ContentValues();
                String filename = "julio_";
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.TITLE, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on ");

                photoURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                editText.setText(photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        // Create an image file name
        String imageFileName = cliente.trim().replaceAll(" ", "") + "_" + timeStamp + ".jpg";
        File storageDir = new File (getExternalFilesDir(Environment.DIRECTORY_PICTURES ), "reparaciones");
        storageDir.mkdirs();
        File image;
        image = new File(storageDir, imageFileName);
//        image.createNewFile();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //solo para mostrar la foto en el imageview
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmapImagen;

            try {

                bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                mPhotoImageView.setImageBitmap(bitmapImagen);

                String selecteadImage = getRealPathFromURI(this, photoURI); //RUTA ORIGINAL DE LA IMAGEN TOMADA
                //ahora se copia al directorio destino con elnombre escogido "DESTINO". este se debe pasar a la BD
                File fuente = new File(selecteadImage); //este el el archivo original sin comprimir
                File destino = new File(String.valueOf(createImageFile()));//este es el nombre del archivo que sera el destino
                FileOutputStream fos = null;
                try{
                    fos = new FileOutputStream(destino); //archivo destino
                    bitmapImagen.compress(Bitmap.CompressFormat.JPEG, 20, fos);//comprime la imagen real y la pasa a fos
                    fos.flush();// se graba en la sd
                }catch (FileNotFoundException ex){
                    ex.printStackTrace();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                fuente.delete();//borramos la foto original

                //GUARDAMOS LA RUTA DE LA FOTO (DESTINO) EN LA TABLA, JUNTO CON EL ID DE LA REPARACION
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                final ContentValues registro = new ContentValues();
                registro.put("id", _id);
                registro.put("ruta", destino.toString());
                registro.put("nota", editText_nota.getText().toString());
                db.insert("fotos", null, registro);
                db.close();
                Toast.makeText(getApplicationContext(), "Registro agregado:  " , Toast.LENGTH_LONG).show();
                //refrescar el listview
                mostrarLista();
                editText_nota.setText(""); //setear el campo nota


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void mostrarLista(){
        //mostramos el listview

        final ArrayList<String> datos_fotos = new ArrayList<String>();
        DBHelper dbHelper2 = new DBHelper(this);
        SQLiteDatabase db2 = dbHelper2.getReadableDatabase();

        Cursor c = db2.rawQuery("Select * from fotos where id = " + String.valueOf(_id), null);
        if (c.moveToFirst()) {
            do {
                String linea1 = " " + c.getInt(0)
                        + " " + c.getInt(1)
                        + " " + c.getString(2);
                datos_fotos.add(linea1);
            } while (c.moveToNext());
        }
        c.close();
        db2.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,     // itemlist del sistema
                datos_fotos);          //DATOS DEL ARREGLO QUE SE MOSTRARA
        fotos_list.setAdapter(adapter);
        fotos_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                String foto = datos_fotos.get(position).split(" ")[3];
                //tomar la foto de su ubicacion original y ponerla en el imageview
                File imgFile = new File(String.valueOf(foto));
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mPhotoImageView.setImageBitmap(myBitmap);

                }

//            Toast.makeText(getApplicationContext(), "foto:  " + foto, Toast.LENGTH_SHORT).show();
            }
        });
        fotos_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // eliminar la foto seleccionada de la sd y de la bd
                final String foto = datos_fotos.get(position).split(" ")[3]; //para borrar el archivo de la sd
                final int _id = Integer.parseInt(datos_fotos.get(position).split(" ")[1]); //para borrar el registro de la BD

                AlertDialog alertDialog = new AlertDialog.Builder(TomarFoto.this)
                        .setTitle("Pop-up/Eliminar Registro: " + _id)
                        .setMessage("Esta seguro de Eliminar la foto?")
                        .setIcon(R.drawable.papelera)
                        .setPositiveButton("si", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                DBHelper dbHelper = new DBHelper(TomarFoto.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete("fotos", "reg_id= ?",
                                        new String[] { String.valueOf(_id) });
                                db.close();
                                File archivoBorrar = new File(foto);
                                archivoBorrar.delete();
                                Toast.makeText(getApplicationContext(),
                                        "Registro " + _id + " Eliminado", Toast.LENGTH_LONG).show();
                                mostrarLista();
                            }
                        })
                        .setNegativeButton("no", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //no hacer nada
                            }
                        })
                        .create();
                alertDialog.show();
                // Devolvemos el valor true para evitar que se dispare
                // también el evento onListItemClick
                return true;
            }
        });

    }


}