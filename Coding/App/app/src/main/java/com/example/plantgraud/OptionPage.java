package com.example.plantgraud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OptionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);

        ImageButton detectPlant = findViewById(R.id.imageButton);
        ImageButton monitorPlant = findViewById(R.id.imageButton2);
        ImageButton blynkApp = findViewById(R.id.imageButton3);
        TextView download = findViewById(R.id.textView3);

        blynkApp.setVisibility(View.GONE);
        download.setVisibility(View.GONE);

        detectPlant.setOnClickListener(view -> {
            Intent find = new Intent(getApplicationContext(),DetectionPage.class);
            startActivity(find);
        });


        monitorPlant.setOnClickListener(view -> {
            Intent search = getPackageManager().getLaunchIntentForPackage("cloud.blynk");
            if(search != null){
                startActivity(search);
            }
            else{
                Toast.makeText(this,"You don't have the app...Please download it!!!",Toast.LENGTH_SHORT).show();
                blynkApp.setVisibility(View.VISIBLE);
                download.setVisibility(View.VISIBLE);
            }
        });

        blynkApp.setOnClickListener(view -> {
            String blynkLink = "https://play.google.com/store/apps/details?id=cloud.blynk&hl=en_IN&gl=US";
            Uri webAddress = Uri.parse(blynkLink);

            Intent goTo = new Intent(Intent.ACTION_VIEW, webAddress);
            if(goTo.resolveActivity(getPackageManager()) != null){
                startActivity(goTo);
            }
            else {
                Toast.makeText(this, "NOT SUPPORTED" ,Toast.LENGTH_SHORT).show();
            }
        });

    }
}