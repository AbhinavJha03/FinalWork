package com.example.newwork;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import pl.droidsonroids.gif.GifImageView;

public class WeatherActivity extends AppCompatActivity {

    String info;
    GifImageView gifImageView;
    ListView listView;
    TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        textview =findViewById(R.id.textView);

        gifImageView = findViewById(R.id.gifImageView);
        listView=findViewById(R.id.listView);

        // FOR TEXT VIEW AND VALUE UPDATE THROUGH INTENT FROM MAIN ACTIVITY CLASS

        Intent intent = getIntent();
        String name= intent.getStringExtra("name");
        info = intent.getStringExtra("Info");
        String infoView = intent.getStringExtra("InfoView");
        String temp =intent.getStringExtra("temp");
        String feelsTemp =intent.getStringExtra("feels_like");
        String humidity =intent.getStringExtra("humidity");
        String pressure =intent.getStringExtra("pressure");
        String speed =intent.getStringExtra("speed");
        String degree =intent.getStringExtra("degree");
        String cloudiness=intent.getStringExtra("all");
        String lat= intent.getStringExtra("lat");
        String lon= intent.getStringExtra("lon");
        String sunriseText= intent.getStringExtra("sunrise");
        String sunsetText= intent.getStringExtra("sunset");



        textview.setText(name);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(" Latitude : "+ lat);
        arrayList.add(" Longitude : "+ lon);
        arrayList.add(" Main: " +info);
        arrayList.add(" Description: "+ infoView);
        arrayList.add(" Temperature: " + temp + "\u2103");
        arrayList.add(" Feels Like : " + feelsTemp + "\u2103");
        arrayList.add(" Humidity: " + humidity + "%");
        arrayList.add(" Pressure: "+ pressure+ " hPa");
        arrayList.add(" Wind Speed: "+ speed + " meter/sec");
        arrayList.add(" Cloudiness : " + cloudiness + "%");
        arrayList.add(" Wind Degree: "+ degree+ "\u00B0");
        arrayList.add(" Sunrise "+ sunriseText+ "A.M."+ " GMT+5:30");
        arrayList.add(" Sunset: "+ sunsetText+ "P.M."+ " GMT+5:30");

        final ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this, R.layout.row, arrayList);
        listView.setAdapter(arrayAdapter);
        textview=findViewById(R.id.textView);
        textview.setBackgroundColor(Color.LTGRAY);
        textview.getBackground().setAlpha(40);






        // FOR BACKGROUND GIF CHANGE
        switch (info) {
            case "Clear":
                gifImageView.setBackgroundResource(R.drawable.clear);
                break;

            case "Thunderstorm":
                gifImageView.setBackgroundResource(R.drawable.thunder);
                break;
            case "Haze":
                gifImageView.setBackgroundResource(R.drawable.haze);
                break;
            case "Clouds":
                gifImageView.setBackgroundResource(R.drawable.cloud);

                break;
            case "Rain":
                gifImageView.setBackgroundResource(R.drawable.rain);
                break;

            case "Mist":
                gifImageView.setBackgroundResource(R.drawable.mist);
                break;
            case "Smoke":
                gifImageView.setBackgroundResource(R.drawable.smoke);
                break;
            case "Snow":
                gifImageView.setBackgroundResource(R.drawable.snow);
                break;
            default:
                gifImageView.setBackgroundResource(R.drawable.sky);
        }

    }

}
