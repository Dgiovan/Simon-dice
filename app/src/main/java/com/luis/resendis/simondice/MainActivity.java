package com.luis.resendis.simondice;

import com.luis.resendis.simondice.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity implements ColorFragment.PushListener {


    private int sequenceIndex = 0;
    private ArrayList<ColorFragment> sequence;

    private ColorFragment[] colors;

    private TextView indicator;
    private TextView Score;
    private TextView usertv;

    private boolean challenging;
    private int challengeIndex = 0;
    private int pointScore=0;

    private MediaPlayer one,two,thre,four;
    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        SharedPreferences preferences = getSharedPreferences("name", Context.MODE_PRIVATE);
        String user= preferences.getString("nameUser","userError");


        indicator = findViewById(R.id.indicator);
        Score = findViewById(R.id.Score);
        usertv =  findViewById(R.id.Tvuser);
        usertv.setText(user);

        one = MediaPlayer.create(this,R.raw.sound1);
        two = MediaPlayer.create(this,R.raw.sound2);
        thre = MediaPlayer.create(this,R.raw.sound3);
        four = MediaPlayer.create(this,R.raw.sound4);

        colors = new ColorFragment[4];
        colors[0] = (ColorFragment)findViewById(R.id.topleft);
        colors[1] = (ColorFragment)findViewById(R.id.topright);
        colors[2] = (ColorFragment)findViewById(R.id.bottomleft);
        colors[3] = (ColorFragment)findViewById(R.id.bottomright);

        colors[0].setPushListener(this);
        colors[1].setPushListener(this);
        colors[2].setPushListener(this);
        colors[3].setPushListener(this);

        initSequence();
        doSequence();
    }

    private void initSequence() {
        sequence = new ArrayList<ColorFragment>();
        incSequence();
    }

    private void doSequence() {
        indicator.setText("" + (sequenceIndex + 1));

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sequence.get(sequenceIndex).on();

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sequence.get(sequenceIndex).off();

                        Log.i("app","sequence " + sequenceIndex);
                        sequenceIndex++;
                        if(sequenceIndex < sequence.size()) {

                            doSequence();
                        } else {
                            Log.i("app","chanlenge ?");
                            indicator.setText("?");
                            sequenceIndex = 0;
                            challenging = true;
                        }
                    }
                }, 300);
            }
        }, 1000);
    }

    private void incSequence() {
        Score.setText(String.valueOf(pointScore));
        sequence.add(colors[(int) (Math.random() * colors.length)]);
        doSequence();

    }

    @Override
    public void onPush(View v) {
        if (challenging) {
            if (v == sequence.get(challengeIndex)) {
                indicator.setText("" + (challengeIndex + 1));
                Log.i("app", "good " + challengeIndex);
                challengeIndex++;

                if (challengeIndex >= sequence.size()) {
                    Log.i("app", "Yahouuuuuuu");
                    challenging = false;
                    challengeIndex = 0;
                    indicator.setText("\u2714");// HEAVY CHECK MARK
                    pointScore+=1;

                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            incSequence();
                        }
                    }, 1000);
                }
            } else {
                Log.i("app", "Bouhouhou");
                challenging = false;
                challengeIndex = 0;
                indicator.setText("\u2718");// HEAVY BALLOT X
                showAlertDialog();
            }
        }
    }

    private void showAlertDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this ).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Fallaste");
            alertDialog.setMessage("¿Deseas intentarlo de nuevo?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            pointScore = 0;
                            initSequence();
                            dialog.dismiss();
                            alertDialog = null;
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(MainActivity.this,Splash.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                            alertDialog = null;
                        }
                    });
            alertDialog.show();
        }
    }


}
