package com.example.usuario.datosejercicios2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usuario.datosejercicios1.R;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Ejercicio5 extends AppCompatActivity {

    EditText ruta;
    ImageView image_view;

    Button boton_descargar, boton_abajo, boton_arriba;

    String[] galeria_de_imagenes;
    int imagen_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio5);

        imagen_actual = 0;
        galeria_de_imagenes = new String[1];

        ruta = (EditText)findViewById(R.id.ruta);
        image_view = (ImageView)findViewById(R.id.image_view);
        boton_descargar = (Button)findViewById(R.id.boton_descargar);
        boton_descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ruta.getText().toString().isEmpty()){
                    Toast.makeText(Ejercicio5.this, "Ruta no válida", Toast.LENGTH_SHORT).show();
                }
                else {
                    descargar();
                }
            }
        });
        boton_abajo = (Button)findViewById(R.id.boton_abajo);
        boton_abajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagen_actual == 0) imagen_actual = galeria_de_imagenes.length-1;
                else imagen_actual = (imagen_actual-1)% galeria_de_imagenes.length;
                try{
                    Picasso.with(Ejercicio5.this).load(galeria_de_imagenes[imagen_actual]).into(image_view);
                }
                catch(Exception e){
                    Toast.makeText(Ejercicio5.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        boton_arriba = (Button)findViewById(R.id.boton_arriba);
        boton_arriba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen_actual = (imagen_actual+1)% galeria_de_imagenes.length;
                try{
                    Picasso.with(Ejercicio5.this).load(galeria_de_imagenes[imagen_actual]).into(image_view);
                }
                catch(Exception e){
                    Toast.makeText(Ejercicio5.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        habilitarBotones(false);
    }

    private void descargar(){
        final ProgressDialog progress = new ProgressDialog(Ejercicio5.this);

        RestClient.get(ruta.getText().toString(), new FileAsyncHttpResponseHandler(Ejercicio5.this) {
            private List<String> urls = new ArrayList<String>();

            @Override
            public void onStart() {
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progress.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response){
                progress.dismiss();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while((line = reader.readLine()) != null) urls.add(line);
                } catch (IOException e) {
                    Toast.makeText(Ejercicio5.this, "Fallo en la lectura", Toast.LENGTH_SHORT);
                }

                if (urls.size() != 0) galeria_de_imagenes = new String[urls.size()];

                for(int i = 0; i < urls.size(); i++) galeria_de_imagenes[i] = urls.get(i);

                if(comprobarGaleria()){
                    habilitarBotones(true);
                    try{
                        Picasso.with(Ejercicio5.this).load(galeria_de_imagenes[imagen_actual]).into(image_view);
                    }
                    catch(Exception e){
                        Toast.makeText(Ejercicio5.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    habilitarBotones(false);
                    Toast.makeText(Ejercicio5.this, "Error en el fichero", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progress.dismiss();
                habilitarBotones(false);
                Toast.makeText(Ejercicio5.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void habilitarBotones(boolean b){
        boton_abajo.setEnabled(b);
        boton_arriba.setEnabled(b);
    }

    private boolean comprobarGaleria(){
        for (int i = 0; i < galeria_de_imagenes.length; i++){
            if(!Patterns.WEB_URL.matcher(galeria_de_imagenes[i]).matches()) return false;
        }
        return true;
    }
}
