package com.example.usuario.datosejercicios1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Main2Activity extends AppCompatActivity {

    EditText edtDireccion;
    Button btnBuscar;
    RadioGroup rgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtDireccion = (EditText)findViewById(R.id.edtDireccion);
        rgURL = (RadioGroup)findViewById(R.id.rgURL);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "";
                switch (rgURL.getCheckedRadioButtonId()) {
                    case R.id.rbtnHTTP:
                        URL = "http://";
                        break;
                    case R.id.rbtnHTTPS:
                        URL = "https://";
                        break;
                }
                URL += edtDireccion.getText().toString();
                Intent intent = new Intent(Main2Activity.this, Main2WebActivity.class);
                intent.putExtra("URL", URL);
                startActivity(intent);
            }
        });
    }
}