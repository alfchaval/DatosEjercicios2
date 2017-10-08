package com.example.usuario.datosejercicios1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {

    private EditText edtPeso, edtDias, edtCantidad, edtHuesos, edtCarnes, edtVegetales, edtCereales, edtVisceras;
    private TextView txvCantidad, txvHuesos, txvCarnes, txvVegetales, txvCereales, txvVisceras,
            canCantidad, canHuesos, canCarnes, canVegetales, canCereales, canVisceras;
    private double numCantidad, numHuesos, numCarnes, numVegetales, numCereales, numVisceras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        edtPeso = (EditText)findViewById(R.id.edtPeso);
        edtPeso.addTextChangedListener(new GenericTextWatcher(edtPeso));
        edtDias = (EditText)findViewById(R.id.edtDias);
        edtDias.addTextChangedListener(new GenericTextWatcher(edtDias));
        edtCantidad = (EditText)findViewById(R.id.edtCantidad);
        edtCantidad.addTextChangedListener(new GenericTextWatcher(edtCantidad));
        edtHuesos = (EditText)findViewById(R.id.edtHuesos);
        edtHuesos.addTextChangedListener(new GenericTextWatcher(edtHuesos));
        edtCarnes = (EditText)findViewById(R.id.edtCarnes);
        edtCarnes.addTextChangedListener(new GenericTextWatcher(edtCarnes));
        edtVegetales = (EditText)findViewById(R.id.edtVegetales);
        edtVegetales.addTextChangedListener(new GenericTextWatcher(edtVegetales));
        edtCereales = (EditText)findViewById(R.id.edtCereales);
        edtCereales.addTextChangedListener(new GenericTextWatcher(edtCereales));
        edtVisceras = (EditText)findViewById(R.id.edtVisceras);
        edtVisceras.addTextChangedListener(new GenericTextWatcher(edtVisceras));

        txvCantidad = (TextView)findViewById(R.id.txvCantidad);
        txvHuesos = (TextView)findViewById(R.id.txvHuesos);
        txvCarnes = (TextView)findViewById(R.id.txvCarnes);
        txvVegetales = (TextView)findViewById(R.id.txvVegetales);
        txvCereales = (TextView)findViewById(R.id.txvCereales);
        txvVisceras = (TextView)findViewById(R.id.txvVisceras);

        canCantidad = (TextView)findViewById(R.id.canCantidad);
        canHuesos = (TextView)findViewById(R.id.canHuesos);
        canCarnes = (TextView)findViewById(R.id.canCarnes);
        canVegetales = (TextView)findViewById(R.id.canVegetales);
        canCereales = (TextView)findViewById(R.id.canCereales);
        canVisceras = (TextView)findViewById(R.id.canVisceras);

        Actualizar();
    }

    private void Actualizar() {
        double peso = 0;
        int dias = 0;
        try {
            peso = Double.parseDouble(edtPeso.getText().toString());
            dias = Integer.parseInt(edtDias.getText().toString());
        } catch (Exception e) {}

        numCantidad = peso*25;
        txvCantidad.setText(dias*numCantidad+"");
        numHuesos = numCantidad*65/100;
        txvHuesos.setText(dias*numHuesos+"");
        numCarnes = numCantidad*20/100;
        txvCarnes.setText(dias*numCarnes+"");
        numVegetales = numCantidad*5/100;
        txvVegetales.setText(dias*numVegetales+"");
        numCereales = numCantidad*2/100;
        txvCereales.setText(dias*numCereales+"");
        numVisceras = numCantidad*8/100;
        txvVisceras.setText(dias*numVisceras+"");

        canCantidad.setText(Dias(numCantidad, edtCantidad.getText().toString()));
        canHuesos.setText(Dias(numHuesos, edtHuesos.getText().toString()));
        canCarnes.setText(Dias(numCarnes, edtCarnes.getText().toString()));
        canVegetales.setText(Dias(numVegetales, edtVegetales.getText().toString()));
        canCereales.setText(Dias(numCereales, edtCereales.getText().toString()));
        canVisceras.setText(Dias(numVisceras, edtVisceras.getText().toString()));
    }

    private String Dias(Double necesario, String total) {
        try {
            return String.format("%.2f", (Double.parseDouble(total)/necesario) )+" días";
        } catch (Exception e) {}
        return "";
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch(view.getId()){
                case R.id.edtPeso:
                case R.id.edtDias:
                    Actualizar();
                    break;
                case R.id.edtCantidad:
                    canCantidad.setText(Dias(numCantidad, edtCantidad.getText().toString()));
                    break;
                case R.id.edtHuesos:
                    canHuesos.setText(Dias(numHuesos, edtHuesos.getText().toString()));
                    break;
                case R.id.edtCarnes:
                    canCarnes.setText(Dias(numCarnes, edtCarnes.getText().toString()));
                    break;
                case R.id.edtVegetales:
                    canVegetales.setText(Dias(numVegetales, edtVegetales.getText().toString()));
                    break;
                case R.id.edtCereales:
                    canCereales.setText(Dias(numCereales, edtCereales.getText().toString()));
                    break;
                case R.id.edtVisceras:
                    canVisceras.setText(Dias(numVisceras, edtVisceras.getText().toString()));
                    break;
            }
        }
    }
}