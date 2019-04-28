package com.example.yazlab2_2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

enum LikeStatus {
    none,
    liked,
    disliked
}

public class DetailedNews extends AppCompatActivity {

     ImageView imgView;
    TextView titleView;
    TextView contentView;
    TextView countView;
    Button likeButton;
    Button unlikeButton;
    String newsId;
    String hwid;
    public LikeStatus status = LikeStatus.none;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        Intent intent = getIntent();
        newsId = intent.getStringExtra("id");
        //System.out.println("Detaile gelen id = "+ newsId + "abc: "+ savedInstanceState.getInt("abc"));
//
//        if(savedInstanceState.getInt("abc",0) == 1){
//            DownloadData downloadData = new DownloadData(DataType.latest, null,null,null);
//            String link = MainActivity.address+"/api/news/lastNews";
//            try {
//                downloadData.execute(link).get();
//                newsId = downloadData.lastt;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        hwid = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        imgView = findViewById(R.id.imageView4);
        titleView = findViewById(R.id.titleId);
        contentView = findViewById(R.id.contextId);
        countView = findViewById(R.id.newsViewCount);
        unlikeButton = findViewById(R.id.unlike);
        likeButton = findViewById(R.id.like);

        DownloadData downloadDatafirst = new DownloadData(DataType.vote, null, null, this);
        String linkfirst = MainActivity.address + "/api/vote/"+ hwid + "/"+newsId;
        try {
            downloadDatafirst.execute(linkfirst).get();
        }catch (Exception e){
            e.printStackTrace();
        }




        DownloadData downloadData = new DownloadData(DataType.newsdetail, null, null, this);
        String link = MainActivity.address + "/api/news/"+newsId;
        try {
            downloadData.execute(link).get();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void vote(View view) {
        int id = view.getId();
        DownloadData downloadData = new DownloadData(DataType.empty, null, null, this);
        String link = MainActivity.address + "/api/vote/" + hwid + "/" + newsId;

        if (id == R.id.like) {
            link += "/true";
            switch (status)
            {
                case disliked:
                    unlikeButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                    likeButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    status = LikeStatus.liked;
                    break;
                case liked:
                    likeButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                    status = LikeStatus.none;
                    break;
                default:
                    likeButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    status = LikeStatus.liked;
                    break;
            }
        } else {
            switch (status)
            {
                case liked:
                    likeButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                    unlikeButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    status = LikeStatus.disliked;
                    //if(Build.VERSION.SDK_INT >=23){
                    //Toast.makeText(getApplicationContext(), "Unliked", Toast.LENGTH_LONG).show();
                    break;
                case disliked:
                    unlikeButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                    status = LikeStatus.none;
                    break;
                default:
                    unlikeButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    status = LikeStatus.disliked;
                    break;
            }
            link += "/false";
        }

        try {
            downloadData.execute(link);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
