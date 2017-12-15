package com.lauren.simplenews.news.widget;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lauren.simplenews.commons.Urls;
import com.lauren.simplenews.news.adapter.CarAdapter;
import com.lauren.simplenews.news.adapter.JokerAdapter;
import com.lauren.simplenews.news.adapter.NbaAdapter;
import com.lauren.simplenews.news.adapter.TopAdapter;
import com.lauren.simplenews.news.getbeans.News;
import com.lauren.simplenews.R;
import com.lauren.simplenews.news.postbeans.Cars;
import com.lauren.simplenews.news.postbeans.Jokes;
import com.lauren.simplenews.news.postbeans.NBA;
import com.lauren.simplenews.news.postbeans.Root;

import com.lauren.simplenews.news.NewsAdapter;
import com.lauren.simplenews.news.postbeans.Top;
import com.lauren.simplenews.okhttp.Info;
import com.lauren.simplenews.okhttp.okhttp;
import com.lauren.simplenews.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.media.CamcorderProfile.get;
import static com.lauren.simplenews.commons.Urls.url0;
import static com.lauren.simplenews.commons.Urls.url1;
import static com.lauren.simplenews.commons.Urls.url2;
import static com.lauren.simplenews.commons.Urls.url3;
import static com.lauren.simplenews.commons.Urls.url4;
import static com.lauren.simplenews.news.widget.NewsFragment.NEWS_TYPE_TOP;


/**
 * Description : 新闻Fragment
 */
public class NewsListFragment extends Fragment {
    ListView lv_titile;
    List<String> l_title = new ArrayList<>();
    List<String> l_img = new ArrayList<>();
    List<String> l_content = new ArrayList<>();
    okhttp okhttp=new okhttp();
    private static final String TAG = "NewsListFragment";
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private LinearLayoutManager mLayoutManager;
    private List<News> mData;
    private int mType = NEWS_TYPE_TOP;
    private int pageIndex = 0;
    private NewsAdapter mAdapter;
    View view;
  List<Integer> view_id = new ArrayList<Integer>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type");
        view_id.add(mType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_newslist,  container, false);
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mLayoutManager = new LinearLayoutManager(getActivity());
        onTakePhoto();
        initview();
        return view;
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.isShowFooter()) {
                //加载更多
                LogUtils.d(TAG, "loading more data");
            }
        }
    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String title=(String) msg.obj;
                    Log.d("title",title+"---------------");
                    if (title!=null) {
                        JsonData(title);
                    }
                    break;
            }
        }
    };
    public static NewsListFragment newInstance(int type) {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }
    private void initview(){
        lv_titile = (ListView) view.findViewById(R.id.recyclerView);
        getpost(Urls.url4);
    }

    private void getpost(String url) {
        File sdcache = getActivity().getExternalCacheDir();
        okhttp.initOkHttpClient(sdcache);
        try {
            RequestBody body = new FormBody.Builder().add("JSON", "{flag:object}").build();
            okhttp.postAsynHttp(url, body, "news");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String  str= Info.map.get("news");
                    Log.d("JSON数据串","************"+str);
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
        switch (view_id.get(Info.viewid)) {
            case 0:
                for (int i = 0; i < rt.getTop().size(); i++) {
                    Top data = rt.getTop().get(i);
                    String tv_title = data.getTitle();
                    String img_titlt = data.getImage();
                    String tv_content = data.getContent();
                    l_title.add(tv_title);
                    l_img.add(img_titlt);
                    l_content.add(tv_content);
                }
                TopAdapter adapter = new TopAdapter(getActivity(), l_title, l_img, l_content);
                lv_titile.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lv_titile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.putExtra("url", url0);
                        intent.putExtra("num", i);
                        intent.setClass(getActivity(), GetActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 1:
                for (int i = 0; i < rt.getNBA().size(); i++) {
                    NBA data = rt.getNBA().get(i);
                    String tv_title = data.getTitle();
                    String img_titlt = data.getImage();
                    String tv_content = data.getContent();
                    l_title.add(tv_title);
                    l_img.add(img_titlt);
                    l_content.add(tv_content);
                }
                NbaAdapter nadapter = new NbaAdapter(getActivity(), l_title, l_img, l_content);
                lv_titile.setAdapter(nadapter);
                nadapter.notifyDataSetChanged();
                lv_titile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.putExtra("url", url2);
                        intent.putExtra("num", i);
                        intent.setClass(getActivity(), GetActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 2:
                for (int i = 0; i < rt.getCars().size(); i++) {
                    Cars data = rt.getCars().get(i);
                    String tv_title = data.getTitle();
                    String img_titlt = data.getImage();
                    String tv_content = data.getContent();
                    l_title.add(tv_title);
                    l_img.add(img_titlt);
                    l_content.add(tv_content);
                }
                CarAdapter cadapter = new CarAdapter(getActivity(), l_title, l_img, l_content);
                lv_titile.setAdapter(cadapter);
                cadapter.notifyDataSetChanged();
                lv_titile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.putExtra("url", url3);
                        intent.putExtra("num", i);
                        intent.setClass(getActivity(), GetActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 3:
                for (int i = 0; i < rt.getJokes().size(); i++) {
                    Jokes data = rt.getJokes().get(i);
                    String tv_title = data.getTitle();
                    String img_titlt = data.getImage();
                    String tv_content = data.getContent();
                    l_title.add(tv_title);
                    l_img.add(img_titlt);
                    l_content.add(tv_content);
                }
                JokerAdapter jadapter = new JokerAdapter(getActivity(), l_title, l_img, l_content);
                lv_titile.setAdapter(jadapter);
                jadapter.notifyDataSetChanged();
                lv_titile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.putExtra("url", url1);
                        intent.putExtra("num", i);
                        intent.setClass(getActivity(), GetActivity.class);
                        startActivity(intent);
                    }
                });
                break;
        }

    }
    public   void   onTakePhoto()   {
        if (Build.VERSION.SDK_INT>=23)       {
            int request= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (request!= PackageManager.PERMISSION_GRANTED)    //缺少权限，进行权限申请
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
                return;
            }
            else
            {
                //权限同意，不需要处理
                Toast.makeText(getActivity(),"权限同意",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //低于23 不需要特殊处理
        }
    }



}
