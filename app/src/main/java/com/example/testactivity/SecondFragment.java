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
public class SecondFragment extends Fragment {
    private Main2Adapter listItemAdapter; // 适配器
    private ArrayList<PictureInfo> listItems; // 存放文字、图片信息
    String TAG = "SecondFragment";
    private String mCategroey = "2338";
    private String mSize = "0";
    private String mColor = "0";
    private String mPage = "1";
    private RecyclerView recyclerView;
    private Handler handler;
    private ArrayList uurl =new ArrayList();
    private ArrayList variety=new ArrayList();
    PictureInfo pictureInfo;

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView2);  //实例化rec

        listItems = new ArrayList<PictureInfo>();
        LoadData();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 9) {
                    listItems = (ArrayList<PictureInfo>) msg.obj;
                    initView();

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
                    Document document =Jsoup.connect("http://www.win4000.com/mobile_0_0_0_1.html").get();
                    Elements elements = document.select("div.product_query a");   //解释

                    int i =0;
                    for (Element element : elements) {
                        variety.add(element.select("a").text());
                        Log.d("标题", (String) variety.get(i));
                        uurl.add(element.select("a").attr("href"));
                        Log.d("链接", (String) uurl.get(i));
                        i++;
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();

                }


                try {
                    int ii =1;
                    for (int i = 0; i < 22; i++) {
                        Document document = Jsoup.connect((String) uurl.get(ii)).get();
                        Elements elements = document.select("div.Left_bar ul li");   //解释
                        Element element=elements.get(1);

                        String image = element.select("a").first().children().first().attr("data-original");
                        String url =  (String) uurl.get(ii);
                        Log.d("图片", image);
                        Log.d("连接", url);

                        pictureInfo = new PictureInfo();
                        pictureInfo.setImage(image);
                        pictureInfo.setUrl(url);
                        pictureInfo.setTitle((String) variety.get(ii));
                        Log.i(TAG, "run: " + "放入pictureInfo");
                        listItems.add(pictureInfo);
                        ii++;

                    }

                    Message msg = handler.obtainMessage(9);
                    msg.obj = listItems;
                    handler.sendMessage(msg);



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

    private void initView() {


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        listItemAdapter = new Main2Adapter(listItems,"竖图");
        recyclerView.setAdapter(listItemAdapter);
    }

}
