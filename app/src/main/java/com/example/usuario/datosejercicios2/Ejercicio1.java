package com.example.usuario.datosejercicios2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usuario.datosejercicios1.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Ejercicio1 extends AppCompatActivity {

    FloatingActionButton anadir;
    ListView lista_vista;
    ArrayList<String> lista_contactos;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio1);

        lista_contactos = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lista_contactos);
        lista_vista = (ListView)findViewById(R.id.lista);
        lista_vista.setAdapter(adapter);

        anadir = (FloatingActionButton) findViewById(R.id.btn_anadir);
        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Ejercicio1.this, NuevoContacto.class);
                startActivity(i);
            }
        });
        actualizarContactos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarContactos();
        adapter.notifyDataSetChanged();
    }

    public void actualizarContactos(){
        int errores = 0;
        try {
            File fichero = new File(getApplicationContext().getFilesDir(), "agenda.txt");
            if(fichero.exists())
            {
                BufferedReader lector = new BufferedReader(new FileReader(fichero));
                String linea;
                String[] informacionContacto;
                String contacto = "";
                while((linea = lector.readLine()) != null) {
                    informacionContacto = linea.split(",");
                    try {
                        contacto = "Nombre: " + informacionContacto[0] + "\nTeléfono: " + informacionContacto[1] + "\nEmail: " + informacionContacto[2];
                        if(!lista_contactos.contains(contacto)) lista_contactos.add(contacto);
                    }
                    catch (Exception e) {
                        errores++;
                    }
                }
                if(errores > 0) Toast.makeText(this, "Se produjo un error leyendo " + errores + " contactos", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Error leyendo el archivo", Toast.LENGTH_SHORT).show();
        }
    }
}
