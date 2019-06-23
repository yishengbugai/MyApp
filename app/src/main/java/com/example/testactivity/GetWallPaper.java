package com.example.testactivity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetWallPaper {
    private static final String TAG = "GetWallPaper";
    private static String judge1;
    private static String filePath;
    private static Bitmap mBitmap;
    private static Context context;
    private static String mSaveMessage;
    private static String mFileName="妹子图片";



    public static void setWallpaper(final Activity activity, final String imgUrl,final String judge) {

        judge1=judge;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL httpUrl = new URL(imgUrl);//获取传入进来的url地址  并捕获解析过程产生的异常
                    //使用是Http访问  所以用HttpURLConnection  同理如果使用的是https  则用HttpsURLConnection
                    try {
                        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();//通过httpUrl开启一个HttpURLConnection对象
                        conn.setReadTimeout(5000);//设置显示超市时间为5秒
                        conn.setRequestMethod("GET");//设置访问方式
                        conn.setDoInput(true);//设置可以获取输入流

                        InputStream in = conn.getInputStream();//获取输入流

                        //创建一个写入ID卡的文件对象
                        FileOutputStream out = null;
                        File download = null;
                        String filename = String.valueOf(System.currentTimeMillis());//获取系统时间
                        //判断文件是否存在   Environment.MEDIA_MOUNTEDID卡是否挂载  如果是则创建文件对象
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                            File parent = Environment.getExternalStorageDirectory();//获取ID卡目录
                            File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
                            if (!dirFile.exists()){
                                dirFile.mkdir();
                            }
                            download = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/" , System.currentTimeMillis()+ ".jpg");//在父类的目录下创建一个以当前下载的系统时间为文件名的文件

                            out = new FileOutputStream(download);
                        }

                        byte[] b = new byte[2 * 1024];
                        int len;
                        if (out != null) {//id卡如果存在  则写入
                            while ((len = in.read(b)) != -1) {
                                out.write(b, 0, len);
                            }
                        }

                        //读取该文件中的内容
                        final InputStream data=new FileInputStream(download.getAbsolutePath());
                        final Bitmap bitmap = BitmapFactory.decodeFile(download.getAbsolutePath());
                        activity.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                //设置图片为壁纸
                                //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.bg_user_top);//设置项目res中的图片
                                WallpaperManager manager = WallpaperManager.getInstance(activity);
                                try {
                                    if(judge1.equals("主屏幕")){
                                    manager.setStream(data,null,true,WallpaperManager.FLAG_SYSTEM);
//                                    manager.setBitmap(bitmap);
                                    Log.i(TAG, "run: "+"主屏幕设置成功");
                                        Toast.makeText(activity, "主屏幕设置成功", Toast.LENGTH_SHORT).show();}
                                    else if(judge1.equals("锁屏")){
                                        manager.setStream(data,null,true,WallpaperManager.FLAG_LOCK);
//                                    manager.setBitmap(bitmap);
                                        Toast.makeText(activity, "锁屏设置成功", Toast.LENGTH_SHORT).show();
                                        Log.i(TAG, "run: "+"锁屏设置成功");
                                    }

                                } catch (IOException e) {

                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
