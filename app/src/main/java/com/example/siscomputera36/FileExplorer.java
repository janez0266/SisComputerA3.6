package com.example.siscomputera36;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileExplorer extends AppCompatActivity {


    Button btn_close;
    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    private TextView carpetaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        setTitle("File Explorer/copiar o Mover");
        btn_close = findViewById(R.id.close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        carpetaActual = findViewById(R.id.textTitulo);

        directorioRaiz = Environment.getExternalStorageDirectory().getPath(); //directorio que se mostrara en el listview

        verArchivosDirectorio(directorioRaiz);

    }

    private void verArchivosDirectorio(String rutaDirectorio) {
        carpetaActual.setText("Estas en: " + rutaDirectorio);
        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();
        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;

        // Si no es nuestro directorio raiz creamos un elemento que nos
        // permita volver al directorio padre del directorio actual
        if (!rutaDirectorio.equals(directorioRaiz)) {
            listaNombresArchivos.add("../");
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        // Almacenamos las rutas de todos los archivos y carpetas del directorio
        for (File archivo : listaArchivos) {
            listaRutasArchivos.add(archivo.getPath());
        }

        // Ordenamos la lista de archivos para que se muestren en orden alfabetico
        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);


        // Recorredos la lista de archivos ordenada para crear la lista de los nombres
        // de los archivos que mostraremos en el listView
        for (int i = x; i < listaRutasArchivos.size(); i++){
            File archivo = new File(listaRutasArchivos.get(i));
            if (archivo.isFile()) {
                listaNombresArchivos.add(archivo.getName());
            } else {
                listaNombresArchivos.add("/" + archivo.getName());
            }
        }

        // Si no hay ningun archivo en el directorio lo indicamos
        if (listaArchivos.length < 1) {
            listaNombresArchivos.add("No hay ningun archivo");
            listaRutasArchivos.add(rutaDirectorio);
        }


        // Creamos el adaptador y le asignamos la lista de los nombres de los
        // archivos y el layout para los elementos de la lista
        adaptador = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listaNombresArchivos);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                // Obtenemos la ruta del archivo en el que hemos hecho click en el
                // listView
                final File filePath = new File(listaRutasArchivos.get(position)); //filepath comleto con nombre del archivo seleccionado
                final String nombreArchivo = filePath.getName();  //nombre del archivo seleccionado

                // Si es un archivo se muestra un Toast con su nombre y si es un directorio
                // se cargan los archivos que contiene en el listView
                if (filePath.isFile()) {


                    //todo aqui copiamos el archivo seleccionado a la ruta interna de la aplicacion
                    File pathDestino = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //ruta al directorio de la aplicacion
                    final File destination = new File( pathDestino
                            + "/../"     //se retrocede una carpeta
                            + nombreArchivo);
                    //AlertDialog.Builder builder = new AlertDialog.Builder(FileExplorer.this);
                    //builder.setMessage("Ruta destino: " + destination)
                    //        .show();

                    //funciona bien con mp3, pdf, jpg, docx, apk, exe, rar,
                    AlertDialog alertDialog = new AlertDialog.Builder(FileExplorer.this)
                            .setTitle("Mover/Copiar")
                            .setMessage("Desea Mover o Copiar el Archivo al directorio del programa?")
                            .setIcon(R.drawable.folder_open)
                            .setPositiveButton("Copiar", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    try {
                                        copy(filePath, destination); // se copia el archivo
                                        Toast.makeText(FileExplorer.this,
                                                "Archivo: -" + nombreArchivo + "- copiado con éxito.. ",
                                                Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // Devuelvo los datos a la activity principal
                                //Intent data = new Intent();
                                //data.putExtra("filename", item.get(position));
                                //setResult(RESULT_OK, data);
                            })
                            .setNegativeButton("Mover", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        copy(filePath, destination); // se copia el archivo
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    filePath.delete();//borramos el archivo original
                                    Toast.makeText(FileExplorer.this,
                                            "Archivo: -" + nombreArchivo + "- movido con éxito.. ",
                                            Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNeutralButton("Ver", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Aquí irá lo que se desee hacer cuando hagamos click en el botón

                                    File file= filePath;
                                    Uri uri = Uri.parse(file.toString());

                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    // Check what kind of file you are trying to open, by comparing the url with extensions.
                                    // When the if condition is matched, plugin sets the correct intent (mime) type,
                                    // so Android knew what application to use to open the file
                                    if (filePath.toString().contains(".doc") || filePath.toString().contains(".docx")) {
                                        // Word document
                                        intent.setDataAndType(uri, "application/msword");
                                    } else if(filePath.toString().contains(".pdf")) {
                                        // PDF file
                                        intent.setDataAndType(uri, "application/pdf");
                                    } else if(filePath.toString().contains(".ppt") || filePath.toString().contains(".pptx")) {
                                        // Powerpoint file
                                        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                                    } else if(filePath.toString().contains(".xls") || filePath.toString().contains(".xlsx")) {
                                        // Excel file
                                        intent.setDataAndType(uri, "application/vnd.ms-excel");
                                    } else if(filePath.toString().contains(".zip") || filePath.toString().contains(".rar")) {
                                        // WAV audio file
                                        intent.setDataAndType(uri, "application/x-wav");
                                    } else if(filePath.toString().contains(".rtf")) {
                                        // RTF file
                                        intent.setDataAndType(uri, "application/rtf");
                                    } else if(filePath.toString().contains(".wav") || filePath.toString().contains(".mp3")) {
                                        // WAV audio file
                                        intent.setDataAndType(uri, "audio/x-wav");
                                    } else if(filePath.toString().contains(".gif")) {
                                        // GIF file
                                        intent.setDataAndType(uri, "image/gif");
                                    } else if(filePath.toString().contains(".jpg") || filePath.toString().contains(".jpeg") || filePath.toString().contains(".png")) {
                                        // JPG file
                                        intent.setDataAndType(uri, "image/jpeg");
                                    } else if(filePath.toString().contains(".txt")) {
                                        // Text file
                                        intent.setDataAndType(uri, "text/plain");
                                    } else if(filePath.toString().contains(".3gp") || filePath.toString().contains(".mpg") || filePath.toString().contains(".mpeg") || filePath.toString().contains(".mpe") || filePath.toString().contains(".mp4") || filePath.toString().contains(".avi")) {
                                        // Video files
                                        intent.setDataAndType(uri, "video/*");
                                    } else {
                                        //if you want you can also define the intent type for any other file

                                        //additionally use else clause below, to manage other unknown extensions
                                        //in this case, Android will show all applications installed on the device
                                        //so you can choose which application to use
                                        intent.setDataAndType(uri, "*/*");
                                    }

                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .create();
                    alertDialog.show();
                    //verArchivosDirectorio(directorioRaiz); //regresa al listview al directorio raiz

                } else {
                    // Si es un directorio mostramos todos los archivos que contiene
                    verArchivosDirectorio(listaRutasArchivos.get(position));
                }


            }
        });
    }

    private void copy(File source, File destination) throws IOException {
        FileChannel in = new FileInputStream(source).getChannel();
        FileChannel out = new FileOutputStream(destination).getChannel();
        try { in.transferTo(0, in.size(), out);

        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }


}
