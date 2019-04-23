package com.example.yazlab2_2;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedNews extends AppCompatActivity {

     ImageView imgView;
    TextView titleView;
    TextView contentView;
    TextView countView;
    Button likeButton;
    Button unlikeButton;
    String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        imgView = findViewById(R.id.imageView4);
        titleView = findViewById(R.id.titleId);
        contentView = findViewById(R.id.contextId);
        countView = findViewById(R.id.newsViewCount);
        unlikeButton = findViewById(R.id.unlike);
        likeButton = findViewById(R.id.like);


        Intent intent = getIntent();
        newsId = intent.getStringExtra("id");

        DownloadData downloadData = new DownloadData(DataType.newsdetail, null, null, this);
        String link = "http://192.168.0.18:3000/api/news/"+newsId;
        try {
            downloadData.execute(link).get();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void vote(View view){
        int id = view.getId();
        DownloadData downloadData = new DownloadData(DataType.vote, null, null, this);
        String hwid = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String link = "http://192.168.0.18:3000/api/vote/" + hwid + "/" + newsId;

        if(id == R.id.like){

            link+= "/true";
            try {
                downloadData.execute(link);
                Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_LONG);

                likeButton.setEnabled(false);
                likeButton.setBackgroundColor(Color.RED);
                //likeButton.getBackground().setAlpha(80);


            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{

            link += "/false";
            try {
                downloadData.execute(link);
                Toast.makeText(getApplicationContext(), "Unliked", Toast.LENGTH_LONG);

                unlikeButton.setEnabled(false);
                unlikeButton.setBackgroundColor(Color.RED);


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
