package com.example.usuario.datosejercicios1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Main3Activity extends AppCompatActivity {

    private MyCountDownTimer miContador;
    private TextView txvCafes, txvTiempo;
    private RadioButton rbtnAscendente, rbtnDescendente;
    private Button btnResta, btnSuma, btnComenzar, btnReiniciar;
    private int contadorTiempo = 1, contadorCafes = 0;
    private MediaPlayer mp;
    private AlertDialog.Builder popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        popup = new AlertDialog.Builder(this);

        //MediaPlayer.create(this, R.raw.audio);

        txvCafes = (TextView)findViewById(R.id.txvCafes);
        txvTiempo = (TextView)findViewById(R.id.txvTiempo);
        rbtnAscendente = (RadioButton)findViewById(R.id.rbtnAscendente);
        rbtnDescendente = (RadioButton)findViewById(R.id.rbtnDescendente);
        btnResta = (Button)findViewById(R.id.btnResta);
        btnSuma = (Button)findViewById(R.id.btnSuma);
        btnComenzar = (Button)findViewById(R.id.btnComenzar);
        btnReiniciar = (Button)findViewById(R.id.btnReiniciar);

        btnResta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (miContador != null) miContador.cancel();
                //Lo suyo sería evitar que se puedan poner 0 minutos
                //pero lo permito para poder comprobar lo que ocurre al llegar al décimo café sin tener que esperar 10 minutos.
                if(contadorTiempo >= 1) {
                    contadorTiempo--;
                }
                txvTiempo.setText(String.format("%02d", contadorTiempo) + ":00");
            }
        });

        btnSuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (miContador != null) miContador.cancel();
                if(contadorTiempo < 60) {
                    contadorTiempo++;
                }
                txvTiempo.setText(String.format("%02d", contadorTiempo) + ":00");
            }
        });

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contadorCafes < 10) {
                    if (miContador != null) miContador.cancel();
                    miContador = new MyCountDownTimer(contadorTiempo * 60000, 1000);
                    miContador.start();
                }
            }
        });

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contadorCafes = 0;
                txvCafes.setText(contadorCafes+"");
            }
        });
    }

    private class MyCountDownTimer extends CountDownTimer {

        long startTime, minutos, segundos;

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
            this.startTime = startTime;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(rbtnDescendente.isChecked()) {
                minutos = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                segundos = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutos);
            }
            else {
                minutos = TimeUnit.MILLISECONDS.toMinutes(startTime - millisUntilFinished);
                segundos = TimeUnit.MILLISECONDS.toSeconds(startTime - millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutos);
            }
            txvTiempo.setText(String.format("%02d:%02d", minutos, segundos));
        }

        @Override
        public void onFinish() {
            txvTiempo.setText("FIN");
            //mp.start();
            contadorCafes++;
            txvCafes.setText(contadorCafes+"");
            if(contadorCafes >= 10) {
                popup.setTitle("FIN");
                popup.setMessage("Se alcanzó el máximo de cafés");
                popup.setPositiveButton("También OK", null);
                popup.setNegativeButton("OK", null);
                popup.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (miContador != null) miContador.cancel();
        super.onBackPressed();
    }
}
