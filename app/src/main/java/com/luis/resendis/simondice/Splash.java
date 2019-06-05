package com.luis.resendis.simondice;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Splash extends AppCompatActivity {

    private final long SPLASH = 3000;
    private LinearLayout screensplash,getname;
    private EditText name;
    private Button  toContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        screensplash      = findViewById(R.id.splash);
        getname     = findViewById(R.id.getname);
        name        = findViewById(R.id.Edname);
        toContinue  = findViewById(R.id.btncontinue);

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             screensplash.setVisibility(View.GONE);
             getname.setVisibility(View.VISIBLE);
         }
     },SPLASH);

        toContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.getText().toString();
                if (name.getText().toString().equals(""))
                {
                    name.setError("Por favor ingresa tu nombre");
                }if (!name.getText().toString().equals(""))
                {

                    SharedPreferences preferences = getSharedPreferences("name", Context.MODE_PRIVATE);
                    String usertext= name.getText().toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("nameUser",usertext);
                    editor.commit();

                    Intent intent = new Intent(Splash.this , MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }
}
