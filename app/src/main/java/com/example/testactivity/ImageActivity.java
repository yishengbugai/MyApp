package com.example.testactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageActivity extends AppCompatActivity {
    private RecyclerView recy1;
    private String url;
    private String type;
    private ArrayList<ImageList> iList = new ArrayList<>();
    private ImageList moreImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        recy1 = (RecyclerView) findViewById(R.id.recy1);
        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        type = intent.getStringExtra("Type");

        ParseHtml();

    }



    private void ParseHtml(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
//                    Elements tables=document.getElementsByTag("dd");
                    Elements elements = document.select("ul[id=scroll] li");
                    for (Element element : elements) {
                        String imagelist = element.select("a").first().children().first().attr("data-original");
                        String imageurl0 = imagelist.substring(0, imagelist.indexOf("_"));
                        String imageurl = imageurl0 + ".jpg";
                        moreImage = new ImageList();
                        moreImage.setUrl(imageurl);
                        Log.d("更多图片", imageurl);
                        iList.add(moreImage);

                        Message msg = handler.obtainMessage(8);
                        msg.obj = iList;
                        handler.sendMessage(msg);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();;

    }



    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if (msg.what == 8) {
                    initImage();
            }
        }
    };

    private void initImage(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recy1.setLayoutManager(linearLayoutManager);
        MoreAdapter adapter = new MoreAdapter(iList,type);
        recy1.setAdapter(adapter);
    }
}


