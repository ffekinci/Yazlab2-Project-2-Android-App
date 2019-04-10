package com.example.yazlab2_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class NewsItem {
    public String Id;
    public String Title;
    public String Url;
    public String Content;
    public String Count;

    public NewsItem(String url, String title, String id)
    {
        Url = url;
        Id = id;
        Title = title;
    }

    public void GetImage(ImageView view) {
        try {
            DownloadImageTask task = new DownloadImageTask(view);
            task.execute(Url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
