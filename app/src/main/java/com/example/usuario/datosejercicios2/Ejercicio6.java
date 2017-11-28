package com.example.usuario.datosejercicios2;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.usuario.datosejercicios1.R;

public class Ejercicio6 extends AppCompatActivity {

    private EditText edtEuros, edtDolares, edtDaE;
    private RadioButton rbtnDaE, rbtnEaD;
    private Button btnConvertir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio6);

        edtEuros = (EditText)findViewById(R.id.edtEuros);
        edtDolares = (EditText)findViewById(R.id.edtDolares);
        rbtnDaE = (RadioButton)findViewById(R.id.rbtnDaE);
        rbtnEaD = (RadioButton)findViewById(R.id.rbtnEaD);
        btnConvertir = (Button)findViewById(R.id.btnConvertir);

        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double cambio;
                Double valor;
                try {
                    cambio = Double.parseDouble(edtDaE.getText().toString());
                    if(rbtnDaE.isChecked()) {
                        try {
                            valor = Double.parseDouble(edtDolares.getText().toString());
                            edtEuros.setText(String.format("%.2f", valor/cambio));
                        } catch (Exception e) {
                            Snackbar.make(view, "Valor erróneo en Dólares", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else if(rbtnEaD.isChecked()) {
                        try {
                            valor = Double.parseDouble(edtEuros.getText().toString());
                            edtDolares.setText(String.format("%.2f", valor*cambio));
                        } catch (Exception e) {

                        }Snackbar.make(view, "Valor erróneo en Euros", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(view, "Valor erróneo en el cambio", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
