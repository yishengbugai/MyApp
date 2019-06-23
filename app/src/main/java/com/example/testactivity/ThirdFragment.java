package com.example.testactivity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {
    private MyAdapter listItemAdapter; // 适配器
    private ArrayList<PictureInfo> listItems; // 存放文字、图片信息
    String TAG = "FirstFragment";
    private String mCategroey = "0";
    private String mSize = "0";
    private String mColor = "0";
    private String mPage = "1";
    private RecyclerView recyclerView;
    private Handler handler;

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listItems = new ArrayList<PictureInfo>();
        LoadData();   //通过子线程加载第一页的资源


        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView3);  //实例化rec
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {    //判断是否到底
                if ( recyclerView.canScrollVertically(1)==false) {
                    Log.i(TAG, "onScrollStateChanged: "+"到底了");
                    LoadData1();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });   //监听页面到底事件


        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    listItems = (ArrayList<PictureInfo>) msg.obj;
                    initView();

                } else if(msg.what == 12){
                    listItems = (ArrayList<PictureInfo>) msg.obj;
                    listItemAdapter.setData(listItems);
                    listItemAdapter.notifyDataSetChanged();
                }
                super.handleMessage(msg);
            }
        };
    }
    public void LoadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.win4000.com/wallpaper_" + mCategroey + "_" + mColor + "_" + mSize + "_" + mPage + ".html").get();
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

                        Message msg = handler.obtainMessage(11);
                        //msg.what=5;
                        // msg1.obj="hello for run";
                        msg.obj = listItems;
                        handler.sendMessage(msg);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "运行到了2222之后" + e);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG, "run: " + "提醒");

                        listItemAdapter.notifyDataSetChanged();
                    }
                });
            }

        }).start();

    }
    public void LoadData1(){  new Thread(new Runnable() {


        @Override
        public void run() {
            try {
                int page = Integer.valueOf(mPage)+1;
                mPage = String.valueOf(page);
                Log.i(TAG, "run: page== "+mPage);
                Document document = Jsoup.connect("http://www.win4000.com/wallpaper_" + mCategroey + "_" + mColor + "_" + mSize + "_" + mPage + ".html").get();
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

                    Message msg = handler.obtainMessage(7);
                    msg.obj = listItems;
                    handler.sendMessage(msg);


                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "运行到了2222之后" + e);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.i(TAG, "run: " + "提醒");

                    listItemAdapter.notifyDataSetChanged();
                }
            });
        }

    }).start();}

    public void initView(){
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        listItemAdapter = new MyAdapter(listItems,"横图");
        recyclerView.setAdapter(listItemAdapter);
    }

}
