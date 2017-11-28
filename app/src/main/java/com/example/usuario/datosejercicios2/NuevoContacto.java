package com.example.usuario.datosejercicios2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.example.usuario.datosejercicios1.R;

public class NuevoContacto extends AppCompatActivity implements View.OnClickListener {

    Button btn_anadir, btn_cancelar;
    EditText edt_nombre, edt_telefono, edt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_contacto);

        edt_nombre = (EditText)findViewById(R.id.edt_nombre);
        edt_telefono = (EditText)findViewById(R.id.edt_telefono);
        edt_email = (EditText)findViewById(R.id.edt_email);

        btn_anadir = (Button)findViewById(R.id.btn_anadir);
        btn_anadir.setOnClickListener(this);
        btn_cancelar = (Button)findViewById(R.id.btn_cancelar);
        btn_cancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btn_anadir){
            if(edt_nombre.getText().toString().isEmpty() || edt_telefono.getText().toString().isEmpty() || edt_email.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
            }
            else anadirContacto();
        }
        if(view == btn_cancelar){
            finish();
        }
    }

    public void anadirContacto() {
        try {
            File fichero = new File(this.getApplicationContext().getFilesDir(),"agenda.txt");
            if(!fichero.exists()) fichero.createNewFile();

            BufferedWriter escritor = new BufferedWriter(new FileWriter(fichero,true));
            escritor.write(edt_nombre.getText().toString() + "," + edt_telefono.getText().toString() + "," + edt_email.getText().toString());
            escritor.newLine();
            escritor.close();

            Toast.makeText(this, "Contacto añadido", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "No se ha podido añadir el contacto", Toast.LENGTH_SHORT).show();
        }
    }
}
