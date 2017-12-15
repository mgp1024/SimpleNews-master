package com.lauren.simplenews.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lauren.simplenews.R;
import com.lauren.simplenews.utils.AsyncImageLoader;
import com.lauren.simplenews.utils.FileCache;
import com.lauren.simplenews.utils.MemoryCache;

import java.io.File;
import java.util.List;

/**
 * Created by sun on 2017/11/17.
 */

public class CarAdapter extends BaseAdapter {
    Context context;
    List<String> l_title;
    List<String> l_img;
    List<String> l_content;

    private AsyncImageLoader imageLoader;

    public CarAdapter(Context context, List<String> l_title, List<String> l_img, List<String> l_content) {
        this.context = context;
        this.l_title = l_title;
        this.l_img = l_img;
        this.l_content = l_content;
        MemoryCache mcache = new MemoryCache();//内存缓存
        File sdCard = android.os.Environment.getExternalStorageDirectory();//获得SD卡
        File cacheDir = new File(sdCard, "jereh_cache");//缓存根目录
        FileCache fcache = new FileCache(context, cacheDir, "news_img");//文件缓存
        imageLoader = new AsyncImageLoader(context, mcache, fcache);
    }



    @Override
    public int getCount() {
        return l_title.size();
    }

    @Override
    public Object getItem(int i) {
        return l_title.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list,null);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
        ImageView img_url=(ImageView)view.findViewById(R.id.img_url);
        TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
        tv_title.setText(l_title.get(i));
        img_url.setTag(l_img.get(i));
        tv_content.setText(l_content.get(i));
        Bitmap Bitmap = imageLoader.loadBitmap(img_url,l_img.get(i));
        if(Bitmap == null) {
//            img_bill.setImageResource(R.drawable.backimage);
        } else {
            img_url.setImageBitmap(Bitmap);
        }

    return view;
    }

}
