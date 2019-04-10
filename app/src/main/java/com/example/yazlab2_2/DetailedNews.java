package com.example.yazlab2_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedNews extends AppCompatActivity {

     ImageView imgView;
    TextView titleView;
    TextView contentView;
    TextView countView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        imgView = findViewById(R.id.imageView4);
        titleView = findViewById(R.id.titleId);
        contentView = findViewById(R.id.contextId);
        countView = findViewById(R.id.newsViewCount);

        Intent intent = getIntent();

        DownloadData downloadData = new DownloadData(DataType.newsdetail, null, null, this);
        String link = "http://192.168.1.102:3000/api/news/"+intent.getStringExtra("id");
        try {
            downloadData.execute(link).get();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
