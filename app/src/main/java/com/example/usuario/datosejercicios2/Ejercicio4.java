package com.example.usuario.datosejercicios2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.usuario.datosejercicios1.R;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Ejercicio4 extends AppCompatActivity {

    private EditText edt_url, edt_save;
    private RadioGroup rg_type;
    private Button btn_connect, btn_save;
    private WebView webview;

    String web = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio4);

        RequestQueue requestQueue = MySingleton.getInstance(Ejercicio4.this).getRequestQueue();

        edt_url = (EditText)findViewById(R.id.edt_url);
        edt_save = (EditText)findViewById(R.id.edt_save);
        rg_type = (RadioGroup)findViewById(R.id.rg_type);
        webview = (WebView)findViewById(R.id.webview);

        btn_connect = (Button)findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectar();
            }
        });

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

    }

    private void conectar(){
        if(isNetworkAvailable()) {
            switch (rg_type.getCheckedRadioButtonId()){
                case R.id.rbtn_java:
                    new JavaConnection(Ejercicio4.this).execute(edt_url.getText().toString());
                    break;
                case R.id.rbtn_aahc:
                    aahcRequest(edt_url.getText().toString());
                    break;
                case R.id.rbtn_volley:
                    makeRequest(edt_url.getText().toString());
                    break;
            }
        }
        else {
            Toast.makeText(Ejercicio4.this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    class JavaConnection extends AsyncTask<String, String, Result> {
        ProgressDialog progress;
        Context context;

        public JavaConnection(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage("Conectando...");
            progress.setCancelable(true);
            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    JavaConnection.this.cancel(true);
                }
            });
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected Result doInBackground(String... params) {
            return connectJava(params[0]);
        }

        @Override
        protected void onPostExecute(Result result) {
            if(result.isCode()) {
                web = result.getContent();
                webview.loadDataWithBaseURL(null, web, "text/html", "UTF-8", null);
            }
            else {
                webview.loadDataWithBaseURL(null, result.getMessage(), "text/plain", "UTF-8", null);
            }
            super.onPostExecute(result);
            progress.dismiss();
        }

        @Override
        protected void onCancelled() {
            webview.loadDataWithBaseURL(null, "Cancelado", "text/plain", "UTF-8", null);
            super.onCancelled();
            progress.dismiss();
        }
    }

    public Result connectJava(String texto) {
        URL url;
        HttpURLConnection urlConnection = null;
        int response;
        Result result = new Result();
        try {
            url = new URL(texto);
            urlConnection = (HttpURLConnection) url.openConnection();
            response = urlConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                result.setCode(true);
                result.setContent(read(urlConnection.getInputStream()));
            }
            else {
                result.setCode(false);
                result.setMessage("Error en el acceso a la web: " + String.valueOf(response));
            }
        } catch (IOException e) {
            result.setCode(false);
            result.setMessage("Excepción: " + e.getMessage());
        } finally {
            try {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                result.setCode(false);
                result.setMessage("Excepción: " + e.getMessage());
            }
            return result;
        }
    }

    private void aahcRequest(String url){
        final ProgressDialog progress = new ProgressDialog(Ejercicio4.this);
        RestClient.get(url, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage("Conectando...");
                progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progress.show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(Ejercicio4.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                web = responseString;
                webview.loadDataWithBaseURL(null, web, "text/html", "UTF-8", null);
                progress.dismiss();
            }
        });
    }

    public void makeRequest(String url) {
        final String path = url;
        final ProgressDialog progress = new ProgressDialog(Ejercicio4.this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Conectando...");
        progress.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        web  = response;
                        webview.loadDataWithBaseURL(path, web, "text/html", "utf-8", null);
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        String message = "";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Timeout Error " + error.getMessage();
                        } else if (error instanceof AuthFailureError) {
                            message = "AuthFailure Error " + error.getMessage();
                        } else if (error instanceof ServerError) {
                            message = "Server Error " + error.getMessage();
                        } else if (error instanceof NetworkError) {
                            message = "Network Error " + error.getMessage();
                        } else if (error instanceof ParseError) {
                            message = "Parse Error " + error.getMessage();
                        }
                        webview.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
                    }
                });
    }

    private void guardar(){
        if(!edt_save.getText().toString().isEmpty()){
            String name = edt_save.getText().toString();

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(Environment.getExternalStorageDirectory(), name);

                try {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                    writer.write(web);
                    writer.close();
                    Toast.makeText(Ejercicio4.this, "Página guardada", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String read(InputStream in) throws IOException{
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while((line = reader.readLine()) != null){
            result.append(line);
        }
        reader.close();
        return result.toString();
    }
}
