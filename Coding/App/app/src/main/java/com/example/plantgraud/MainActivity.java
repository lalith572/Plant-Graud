package com.example.plantgraud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation agri2Anim,titleAnim;
    ImageView agricycle;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        agri2Anim = AnimationUtils.loadAnimation(this,R.anim.agri2);
        titleAnim = AnimationUtils.loadAnimation(this,R.anim.title_anim);
        agricycle = findViewById(R.id.agricycle);

        title = findViewById(R.id.title);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                agricycle.startAnimation(agri2Anim);
                title.startAnimation(titleAnim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this,OptionPage.class));
                        finish();
                    }
                },4000);
            }
        },2000);
    }
}