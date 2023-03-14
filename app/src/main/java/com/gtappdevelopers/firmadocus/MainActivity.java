package com.gtappdevelopers.firmadocus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    ProgressDialog pd;
    String txtJson;
    String urlParametros;
    boolean archivo;
    boolean llamadaRealizada = false;
    SharedPreferences preferences;
    RecyclerView recyDocumentos;
    private DocusAdapter recyAdapter;
    private List<DocuementosClass> documentos =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

     preferences = PreferenceManager.getDefaultSharedPreferences(this);



        urlParametros = preferences.getString("URL", "");


    }

    private void inicializarAdapter() {

       // documentos = descargarPDFs();


        recyDocumentos = findViewById(R.id.recyDocus);
        recyDocumentos.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyDocumentos.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        this.recyAdapter = new DocusAdapter(MainActivity.this, documentos);
        recyDocumentos.setAdapter(recyAdapter);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_descargar:
                descargarPDFs();
                break;
            case R.id.menu_logout:
                onClickLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.atencion));
        builder.setMessage(getString(R.string.desconectar));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("URL","");

                    editor.apply();

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

    private void descargarPDFs() {
        Log.d("TAG", "LLAMADA" + urlParametros);
        String url ="https://"+urlParametros+"/getPDFS";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                Log.d("respuesta",result);
                documentos.removeAll(documentos);
                try {
                    JSONArray jsonArr = new JSONArray(result);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                    DocuementosClass docu = new DocuementosClass();

                            docu.setIDDP(Long.parseLong(jsonObj.getString("IDDP")));
                            docu.setArchivo(jsonObj.getString("Descripcion"));
                            docu.setRuta(jsonObj.getString("Archivo"));
                            docu.setFecha(jsonObj.getString("Fecha"));
                            docu.setNif(jsonObj.getString("NIF"));
                            docu.setPaciente(jsonObj.getString("NC"));


                            documentos.add(docu);





                    }

                    if (recyAdapter == null){

                        inicializarAdapter();

                    }else{
                        recyAdapter.notifyDataSetChanged();
                    }


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



        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        descargarPDFs();

    }
}


