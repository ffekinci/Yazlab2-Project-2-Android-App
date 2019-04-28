package com.example.yazlab2_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    ArrayList<NewsItem> items = new ArrayList<>();

    public static String address = "http://192.168.43.26:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        main = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Background service

        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("time", 10);
        startService(intent);


        listView = findViewById(R.id.listview);

        CustomAdaptor custom = new CustomAdaptor();
        listView.setAdapter(custom);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailedNews.class);
                intent.putExtra("id",items.get(position).Id);
                startActivity(intent);
            }
        });

        final Menu menu = navigationView.getMenu();
        menu.clear();
        DownloadData downloadData = new DownloadData(DataType.category, menu, null, null);
        String link = address + "/api/category";
        try {
            downloadData.execute(link).get();
        }catch (Exception e){
            e.printStackTrace();
        }


        DownloadData downloadData2 = new DownloadData(DataType.news, null, listView,null);
        downloadData2.items = items;
        String link2 = address + "/api/news/";
        try {
            downloadData2.execute(link2).get();

        }catch (Exception e){
            e.printStackTrace();
        }

        //System.out.println("hwid: " + Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public static MainActivity main;
    public static void ShowNotification(String title, String content, int version, String id, Context ctx){

        System.out.println("maine gelen id: "+ id);

        Intent notificationIntent = new Intent(ctx, DetailedNews.class);

        notificationIntent.putExtra("id", id);

        PendingIntent pending = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(pending);
        builder.setAutoCancel(true);


        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(version, builder.build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

//        menu.add(0,0,0,"Deneme");
//        menu.add(0,0,1,"Deneme 2");
//        menu.add(0,0,2,"Deneme 3");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        String clicked = item.getTitle().toString();
        //Toast.makeText(getApplicationContext(), clicked, Toast.LENGTH_LONG).show();

        DownloadData downloadData = new DownloadData(DataType.news, null, listView,null);
        downloadData.items = items;
        String link = address + "/api/news/cat/"+clicked;
        if (clicked.equals("Tum Haberler"))
            link = address+"/api/news/";
        try {
            downloadData.execute(link).get();

        }catch (Exception e){
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Custom adaptor

    class CustomAdaptor extends BaseAdapter{
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.customlayout,null);

            ImageView img = view.findViewById(R.id.imageView2);
            TextView txt = view.findViewById(R.id.textView2);

            NewsItem item = items.get(position);

            txt.setText(item.Title);
            item.GetImage(img);

            return view;
        }
    }

}
