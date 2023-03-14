package com.gtappdevelopers.firmadocus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PdfActivity extends AppCompatActivity {

    //creating a variable for PDF view.
    PDFView pdfView;
    //url of our PDF file.
    String pdfurl;
    private String iddp;
    private String archivo;
    String urlParametros;
    private String firma64;
    WebView webView;
    private DocuementosClass docu;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing our pdf view.
        webView = findViewById(R.id.webView);
        Bundle extras = getIntent().getExtras();




         iddp = extras.getString("iddp");

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        urlParametros = preferences.getString("URL", "");




        descargarPDF();



         progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Firma Documento");
        progressDialog.setMessage("firmando el documento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);










        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent(PdfActivity.this, FirmaActivity.class);
                intent.putExtra("iddp", iddp);
                intent.putExtra("archivo", archivo);

                startActivityForResult(intent, 1);
            }
        });






    }

    private void descargarPDF() {


        String url ="https://"+urlParametros+"/getPDF/"+iddp;




        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                Log.d("respuesta",result);
                try {
                    JSONArray jsonArr = new JSONArray(result);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                         docu = new DocuementosClass();

                        docu.setIDDP(Long.parseLong(jsonObj.getString("iddp")));
                        docu.setArchivo(jsonObj.getString("archivo"));

                    }


                    webView.loadData(docu.getArchivo(), "text/html; charset=utf-8", "UTF-8");

                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("TAG", "Petó por -> " + volleyError.getLocalizedMessage());
            }
        });



        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

if (resultCode == -1) {
    firma64 = data.getStringExtra("firma");

    sendWorkPostRequest();
}

    }




    private void sendWorkPostRequest() {

        String requestUrl =  "https://"+urlParametros+"/createSignature/"+iddp;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(PdfActivity.this, "Documento Firmado", Toast.LENGTH_SHORT).show();
                descargarPDF();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Toast.makeText(PdfActivity.this, "Error al firmar Documento ", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("imgBase64", firma64);
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//make the request to your server as indicated in your request url

        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        Volley.newRequestQueue(this).add(stringRequest);
    }





}