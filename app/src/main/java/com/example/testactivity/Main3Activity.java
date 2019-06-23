package com.example.testactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    private MyAdapter listItemAdapter; // 适配器
    private ArrayList<PictureInfo> listItems; // 存放文字、图片信息
    String TAG = "Main3Activity";
    private String mSize = "0";
    private String mColor = "0";
    private String mPage = "1";
    private RecyclerView recyclerView;
    private Handler handler;
    String url;
    String type;
    private ArrayList uurl =new ArrayList();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        type = intent.getStringExtra("Type");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);  //实例化rec
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if ( recyclerView.canScrollVertically(1)==false) {
                    Log.i(TAG, "onScrollStateChanged: "+"到底了");
                    LoadData1();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
        listItems = new ArrayList<PictureInfo>();
        LoadData();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 9) {
                    listItems = (ArrayList<PictureInfo>) msg.obj;
                    initView();

                } else if(msg.what == 10){
                    listItems = (ArrayList<PictureInfo>) msg.obj;
                    listItemAdapter.setData(listItems);
                    listItemAdapter.notifyDataSetChanged();
                }
                super.handleMessage(msg);
            }
        };

    }



    public void LoadData() {
        new Thread(new Runnable() {


            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.select("div.pages a");   //解释
                    int i =0;
                    for (Element element : elements) {
                        uurl.add(element.select("a").attr("href"));
                        Log.d("剩余页数链接",(String) uurl.get(i));
                        i++;
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();

                }

                try {
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.select("div.Left_bar ul li");

                    for (Element element : elements) {
                        String image = element.select("a").first().children().first().attr("data-original");
                        String url = element.select("a").first().attr("href");
                        String title = element.select("a").first().attr("title");
                        Log.d("图片", image);
                        Log.d("连接", url);
                        Log.d("标题", title);

                        PictureInfo pictureInfo = new PictureInfo();
                        pictureInfo.setImage(image);
                        pictureInfo.setUrl(url);
                        pictureInfo.setTitle(title);
                        Log.i(TAG, "run: " + "放入pictureInfo");
                        listItems.add(pictureInfo);

                        Message msg = handler.obtainMessage(9);
                        msg.obj = listItems;
                        handler.sendMessage(msg);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG, "run: " + "提醒");

                        listItemAdapter.notifyDataSetChanged();
                    }
                });
            }

        }).start();
    }
    public void LoadData1() {
        new Thread(new Runnable() {


            @Override
            public void run() {
                try {
                    int i=0;
//                    int page = Integer.valueOf(mPage)+1;
//                    mPage = String.valueOf(page);
//                    Log.i(TAG, "run: page== "+mPage);
                    Document document = Jsoup.connect((String) uurl.get(i)).get();
                    Elements elements = document.select("div.Left_bar ul li");

                    for (Element element : elements) {
                        String image = element.select("a").first().children().first().attr("data-original");
                        String url = element.select("a").first().attr("href");
                        String title = element.select("a").first().attr("title");
                        Log.d("图片", image);
                        Log.d("连接", url);
                        Log.d("标题", title);

                        PictureInfo pictureInfo = new PictureInfo();
                        pictureInfo.setImage(image);
                        pictureInfo.setUrl(url);
                        pictureInfo.setTitle(title);
                        Log.i(TAG, "run: " + "放入pictureInfo");
                        listItems.add(pictureInfo);

                        i++;  //翻页

                        Message msg = handler.obtainMessage(10);
                        msg.obj = listItems;
                        handler.sendMessage(msg);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "运行到了2222之后" + e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG, "run: " + "提醒");

                        listItemAdapter.notifyDataSetChanged();
                    }
                });
            }

        }).start();
    }
    private void initView() {


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        listItemAdapter = new MyAdapter(listItems,type);
        recyclerView.setAdapter(listItemAdapter);
    }

}