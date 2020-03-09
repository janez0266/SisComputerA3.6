package com.example.siscomputera36;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.provider.Settings.Secure;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    int requestCode;
    String[] permissions;
    int[] grantResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();

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
//                R.id.nav_acercade, R.id.nav_clientes)   //no funcionan porque no son fragmentos
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
