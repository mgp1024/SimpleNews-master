package com.lauren.simplenews.news.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lauren.simplenews.news.getbeans.News;
import com.lauren.simplenews.R;
import com.lauren.simplenews.news.getbeans.Root;
import com.lauren.simplenews.okhttp.Info;
import com.lauren.simplenews.okhttp.okhttp;
import com.lauren.simplenews.utils.AsyncImageLoader;
import com.lauren.simplenews.utils.FileCache;
import com.lauren.simplenews.utils.MemoryCache;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GetActivity extends AppCompatActivity {
    okhttp okhttp=new okhttp();
    TextView tv_get,tv_getting;
    ImageView img_get;
    String url;
    int num;
    private AsyncImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        initview();
        MemoryCache mcache = new MemoryCache();//内存缓存
        File sdCard = android.os.Environment.getExternalStorageDirectory();//获得SD卡
        File cacheDir = new File(sdCard, "jereh_cache");//缓存根目录
        FileCache fcache = new FileCache(this, cacheDir, "news_img");//文件缓存
        imageLoader = new AsyncImageLoader(this, mcache, fcache);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String strs= (String) msg.obj;
                    JsonData(strs);
                    break;
            }
        }
    };

    private void initview() {
        tv_get= (TextView) findViewById(R.id.tv_get);
        tv_getting= (TextView) findViewById(R.id.tv_getting);
        img_get= (ImageView) findViewById(R.id.img_get);
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        num=intent.getIntExtra("num",0);
        getpost(url);
    }
    private void getpost(String url) {
        File sdcache = this.getExternalCacheDir();
        okhttp.initOkHttpClient(sdcache);
        try {
            RequestBody body = new FormBody.Builder().add("JSON", "{flag:object}").build();
            okhttp.postAsynHttp(url, body, "news");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String  str= Info.map.get("news");
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = str;
                    handler.sendMessage(msg);
                }
            },500);
        }catch (Exception o){

        };
    }
    public void JsonData(String aa) {
        Gson gson = new Gson();
        Root rt = gson.fromJson(aa, Root.class);
            News data = rt.getNews().get(num);
            String tv_title = data.getTitle();
            String img_titlt = data.getImage();
            String tv_content = data.getContent();
        tv_get.setText(tv_title);
        Bitmap Bitmap = imageLoader.loadBitmap(img_get,img_titlt);
        img_get.setImageBitmap(Bitmap);
        tv_getting.setText(tv_content);

    }
}
