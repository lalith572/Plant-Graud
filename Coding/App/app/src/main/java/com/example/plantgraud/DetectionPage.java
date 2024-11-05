package com.example.plantgraud;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantgraud.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DetectionPage extends AppCompatActivity {


    TextView result;
    ImageView picture;
    ImageButton camera;
    int imageSize = 128;
    Button ResultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_page);

        result = findViewById(R.id.textView5);
        picture = findViewById(R.id.imageView);
        camera = findViewById(R.id.imageButton4);
        ResultButton = findViewById(R.id.button);

        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String diseaseName;
            String[] classes = { "Tomato Bacteria Spot",
                    "Tomato Early Blight", "Tomato Healthy",
                    "Tomato Late Blight", "Tomato Leaf Mold", "Tomato Mosaic Virus",
                    "Tomato Septoria Leaf Spot", "Tomato Target Spot", "Tomato Two Spotted Spider Mite",
                    "Tomato Yellow Leaf Curl Virus"};
            result.setText(classes[maxPos]);
            diseaseName = result.getText().toString();
            ResultButton.setOnClickListener(view -> {

                if(diseaseName.equals("Tomato Bacteria Spot")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-bacteria_spot")));
                }
                else if (diseaseName.equals("Tomato Early Blight")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-early_blight")));
                }
                else if (diseaseName.equals("Tomato Healthy")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-healthy")));
                }
                else if (diseaseName.equals("Tomato Late Blight")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-late_blight")));
                }
                else if (diseaseName.equals("Tomato Leaf Mold")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-leaf_mold")));
                }
                else if (diseaseName.equals("Tomato Mosaic Virus")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-mosaic_virus")));
                }
                else if (diseaseName.equals("Tomato Septoria Leaf Spot")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-septoria_leaf_spot")));
                }
                else if (diseaseName.equals("Tomato Target Spot")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-target_spot")));
                }
                else if (diseaseName.equals("Tomato Two Spotted Spider Mite")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-two_spotted_spider_mite")));
                }
                else if (diseaseName.equals("Tomato Yellow Leaf Curl Virus")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantgraud.netlify.app/tomato-yellow_leaf_curl_virus")));
                }


            });



            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            picture.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}