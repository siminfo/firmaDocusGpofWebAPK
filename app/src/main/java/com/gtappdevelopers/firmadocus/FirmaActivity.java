package com.gtappdevelopers.firmadocus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class FirmaActivity extends AppCompatActivity {



 private Button btnGuardar;
 private ImageButton btnBorrar;
    private String firma64;
    private LinearLayout contenedorFirma;
    private PaintClass fondo;
    String urlParametros;
    private String iddp;
    private String archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnGuardar = findViewById(R.id.buttonGuardar);
        contenedorFirma = findViewById(R.id.contenedorFirma);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        urlParametros = preferences.getString("URL", "");

         fondo = new PaintClass(this);
        contenedorFirma.addView(fondo);
    }

    public void saveImage(View view) {

        int canvasWidth = fondo.getWidth();
        int canvasHeight = fondo.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        fondo.draw(canvas);
        firma64 = getBase64String(bitmap);
        Bundle extras = getIntent().getExtras();




        iddp = extras.getString("iddp");
        archivo = extras.getString("archivo");

        Log.d("Firma",firma64);


        Intent resultIntent = new Intent();
        resultIntent.putExtra("firma", firma64);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();





    }




    public void clearCanvas(View view) {

    contenedorFirma.removeAllViews();
        fondo = new PaintClass(this);
        contenedorFirma.addView(fondo);


    }


    private String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        return base64String;


    }

}