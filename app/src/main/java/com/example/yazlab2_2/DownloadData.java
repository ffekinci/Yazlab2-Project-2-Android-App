package com.example.yazlab2_2;

import android.os.AsyncTask;
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

enum DataType {
    category,
    news,
    newsdetail,
    vote
}

public class DownloadData extends AsyncTask<String,Void,String> {
    public DataType dataType;
    Menu menu;
    ListView listView;
    DetailedNews detailed;
    ArrayList<NewsItem> items = new ArrayList<>();

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
                    for (int i=0; i<array.length(); i++){
                        JSONObject obje = array.getJSONObject(i);

                        menu.add(obje.getString("title"));

                    }

                    break;
                }
                case news: {
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
                    detailed.countView.setText(item.Count + " Görüntünlenme");
                    break;
                }
//                case vote:
//                    JSONObject obj = new JSONObject(s);
//                    int stat = obj.getInt("status");
//                    boolean status;
//
//                    if(stat == 1){
//                        status = true;
//                    }
//                    else
//                        status = false;
//
//                    System.out.println("statüs: "+ status);
//                    detailed.stats = status;

            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }


}
