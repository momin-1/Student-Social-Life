package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity {

    String url1 = "Unlock opportunities WSSL helps you unlock exclusive internship opportunities through our network. Find internships that best match your skillset and interests, so that you can take the next step in your career.\n";
    String url2 = "Unlock opportunities WSSL helps you unlock exclusive internship opportunities through our network. Find internships that best match your skillset and interests, so that you can take the next step in your career.\n";
    String url3 = "Unlock opportunities WSSL helps you unlock exclusive internship opportunities through our network. Find internships that best match your skillset and interests, so that you can take the next step in your career.\n";

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Welcome.this, SignUpLogin.class);

                startActivity(intent);

            }
        });



    }
}