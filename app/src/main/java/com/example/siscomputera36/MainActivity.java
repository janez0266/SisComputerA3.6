package com.example.siscomputera36;

//todo Agregar en un futuro una ventana de login para usar el programa,
// usando un generador de claves tomando el ANDROID_ID COMO BASE Y PODERLO COMERCIALIZAR

//reparaciones: el acceso al formulario para agregar reparaciones debe venir del listview de clientes  o del clientesview
//el acceso a reparaciones desde el menu debe mostrar las reparaciones que estan en taller

// agregado al github el 9/3/2020

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.provider.Settings.Secure;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    int requestCode;
    String[] permissions;
    int[] grantResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w("Exito","Mensaje de warning");
// extraer el ANDROID_ID del telefono para convertirlo en clave
        //se extrae el id. este esta compuesto por numeros y letras
        final String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);  // obtiene un unico identificador del telefono
        //ahora se extraen solo los numeros del device_id
//        final String cadena = deviceId;

        final StringBuilder myClave = new StringBuilder();
        for(int i = 0; i< deviceId.length(); i ++)
        {
            if(Character.isDigit(deviceId.charAt(i))) {
                myClave.append(deviceId.charAt(i));
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               messageFloat();
/*
                Snackbar.make(view, "ID del dispositivo:" + deviceId, Snackbar.LENGTH_LONG)
                        //.setActionTextColor(Color.CYAN)
                        //.setActionTextColor(getResources().getColor(R.color.snackbar_action))
                        .setAction("Acción", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //aqui acciones a ejecutar
                                Toast.makeText(getApplicationContext(),
                                        "** clave generada ** " + myClave, Toast.LENGTH_LONG).show();
                                Log.i("Snackbar", "Pulsada acción snackbar!"); //envia un mensaje a la salida RUN de Android Studio
                            }
                        })
                        .show();
 */

            }
        });
      DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void messageFloat(){

        LayoutInflater inflater = getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.message_float, null);

        final TextView txtTitulo = dialoglayout.findViewById(R.id.txtTitulo);
        final TextView txtSubtitulo = dialoglayout.findViewById(R.id.txtSubtitulo);
        final ImageView imageUp = dialoglayout.findViewById(R.id.imageUp);
        final EditText etAsunto = dialoglayout.findViewById(R.id.et_EmailAsunto);
        final EditText etMensaje = dialoglayout.findViewById(R.id.et_EmailMensaje);

        Button btnEnviarMail = (Button) dialoglayout.findViewById(R.id.btnEnviarMail);
        btnEnviarMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = etAsunto.getText().toString();
                String message = etMensaje.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] { "janez0266@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, " mensaje " + message);

                // need this to prompts email client only
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Seleciona un cliente de correo"));

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialoglayout);
        builder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.close) {
            finish();
            return true;
        }
        // TODO colocar los procedimientos de borrado de las tablas en una acivity aparte
        if (id == R.id.del_repara) {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("* ADVERTENCIA * Eliminar tabla REPARACIONES ")
                    .setMessage("La eliminaciónde la tabla REPARACIONES también eliminara la tabla FOTOS.." +
                            "\n \nEsta seguro de Eliminar ambas tablas?")
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("si", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            DBHelper dbHelper = new DBHelper(MainActivity.this);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.execSQL("DROP TABLE IF EXISTS reparaciones");
                            db.execSQL(DBHelper.CREATE_TABLE_REPARACIONES);
                            //todo, eliminar tambien las fotos en la SD
                            db.execSQL("DROP TABLE IF EXISTS fotos");
                            db.execSQL(DBHelper.CREATE_TABLE_FOTOS);
                            db.close();
                            Toast.makeText(getApplicationContext(),
                                    "Registros de ta tabla REPARACIONES Eliminado", Toast.LENGTH_LONG).show();
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
        if (id == R.id.del_cli) {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("* ADVERTENCIA * Eliminar tabla CLIENTES")
                    .setMessage("La eliminacion de la tabla CLIENTES tambien eliminarrá la tabla " +
                            "Reparaciones y FOTOS. \n \nEsta seguro de Eliminar las tablas?")
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("si", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            DBHelper dbHelper = new DBHelper(MainActivity.this);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.execSQL("DROP TABLE IF EXISTS clientes");
                            db.execSQL(DBHelper.CREATE_TABLE_CLIENTES);
                            db.execSQL("DROP TABLE IF EXISTS reparaciones");
                            db.execSQL(DBHelper.CREATE_TABLE_REPARACIONES);
                            //todo, eliminar tambien las fotos en la SD
                            db.execSQL("DROP TABLE IF EXISTS fotos");
                            db.execSQL(DBHelper.CREATE_TABLE_FOTOS);
                            db.close();

                            Toast.makeText(getApplicationContext(),
                                    "Registros de las tablas CLIENTES y REPARACIONES Eliminado", Toast.LENGTH_LONG).show();
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
        if (id == R.id.action_settings){
            //nada
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
