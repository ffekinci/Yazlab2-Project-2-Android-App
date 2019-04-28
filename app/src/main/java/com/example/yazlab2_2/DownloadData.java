package com.example.yazlab2_2;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

enum DataType {
    category,
    news,
    newsdetail,
    vote,
    latest,
    empty
}

public class DownloadData extends AsyncTask<String,Void,String> {
    public DataType dataType;
    Menu menu;
    ListView listView;
    DetailedNews detailed;
    ArrayList<NewsItem> items = new ArrayList<>();
    SharedPreferences pref;
    Context ctx;
    String lastt;

    public DownloadData(DataType dataType, Menu menu, ListView listView, DetailedNews det) {
        this.dataType = dataType;
        this.menu = menu;
        this.listView = listView;
        this.detailed = det;
    }


    @Override
    protected String doInBackground(String... strings) {

        String result = "";
        URL url;
        HttpURLConnection httpURLConnection;

        try {

            //System.out.println("url: " + strings[0]);
            url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();

            while (data > 0) {

                char character = (char) data;
                result += character;

                data = inputStreamReader.read();

            }


            return result;

        } catch (Exception e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);

        //System.out.println("123123123");

        //System.out.println("data: " + s);

        try {

            switch (dataType){
                case category: {
                    JSONArray array = new JSONArray(s);
                    menu.clear();
                    menu.add("Tum Haberler");
                    for (int i=0; i<array.length(); i++){
                        JSONObject obje = array.getJSONObject(i);

                        menu.add(obje.getString("title"));

                    }

                    break;
                }
                case news: {
                    items.clear();

                    JSONArray array = new JSONArray(s);
                    //System.out.println(array.length());
                    for (int i=0; i<array.length(); i++){
                        JSONObject obje = array.getJSONObject(i);
                        NewsItem item = new NewsItem(obje.getString("imageUrl"),obje.getString("title"),obje.getString("_id"));
                        items.add(item);
                    }
                    listView.invalidateViews();
                    break;
                }
                case newsdetail:{

                    JSONObject obj = new JSONObject(s);

                    NewsItem item = new NewsItem(obj.getString("imageUrl"), obj.getString("title"),obj.getString("_id"));
                    item.Content = obj.getString("content");
                    item.Count = obj.getString("view");
                    item.GetImage(detailed.imgView);
                    detailed.titleView.setText(item.Title);
                    detailed.contentView.setText(item.Content);
                    detailed.countView.setText(item.Count + " Görüntülenme");
                    break;
                }
                case vote:
                    JSONObject obj2 = new JSONObject(s);
                    boolean vote = obj2.getBoolean("vote");
                    if (vote){
                        detailed.likeButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                        detailed.status = LikeStatus.liked;
                    }
                    else{
                        detailed.unlikeButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                        detailed.status = LikeStatus.disliked;
                    }
                break;
                case latest:
                    JSONObject obj = new JSONObject(s);
                    String ver = obj.getString("version");
                    int version = Integer.parseInt(ver.substring(0, 8), 16);

                    lastt = ver;

                    System.out.println("GELEN - "+ ver + " - Kayıtlı : "+ pref.getInt("lastVersion",0));


                    if(pref.getInt("lastVersion",0) < version){
                        //pref.edit().clear();
                        pref.edit().putInt("lastVersion", version).apply();
                        //Notification
                    MainActivity.ShowNotification("Yeni haber!",obj.getString("title"),version, ver, ctx);

                    }
                break;
                case empty:
                    //Empty
                break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }


}
