package com.example.mirek.diabetapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    String text="Pomagamy funkcjonować";

    TextView splashText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        splashText = findViewById(R.id.splashText);
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            splashText.setText(text);
                            splashText.startAnimation(AnimationUtils.loadAnimation(SplashScreenActivity.this, android.R.anim.fade_in));
                        }
                    });
                    sleep(5000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            splashText.setText("");
                        }
                    });
                    sleep(1000);
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        mThread.start();
    }
}
