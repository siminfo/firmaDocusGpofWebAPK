package com.gtappdevelopers.firmadocus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUrl ;
    private Button btnLog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        txtUrl = findViewById(R.id.txtUrl);
        btnLog = findViewById(R.id.btnLog);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);



        String url = preferences.getString("URL", "");

        if (url != ""){
            txtUrl.setText(url);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);

        }


        btnLog.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("URL",txtUrl.getText().toString());

            editor.apply();

            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
    });




    }
}