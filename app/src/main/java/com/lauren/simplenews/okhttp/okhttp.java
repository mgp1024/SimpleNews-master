package com.lauren.simplenews.okhttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okhttp {


    public OkHttpClient mOkHttpClient;
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public String str;
    private Bitmap bitmap;

    public void initOkHttpClient(File sdcache) {

        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }

    /**
     * post异步请求
     */
    public void postAsynHttp(final String url, final RequestBody formBody, final String key) {
//        final Context context = MusicApplication.getContext();
        Log.d("cookie.url", Info.cookie + "-----str------");
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("cookie", Info.cookie)
                //.addHeader("jsonp", "callback")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("e", e + "-------");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String phoneString = (String) ShareData.getParam(context, "phoneString", "");
//                String passwordString = (String) ShareData.getParam(context, "passwordString", "");
                if (response.isSuccessful()) {
                    str = response.body().string();
                    if (!TextUtils.isEmpty(str)) {
//                        if (url == Url.login) {
//                            List<String> cookieList = response.headers("Set-Cookie");
//                            String session = cookieList.get(0);
//                            session = session.substring(0, session.indexOf(";"));
//                            Info.cookie = session;
//                        }
                        Info.map.put(key, str);
                        Log.d("str", str + "-----str------");
                    }
                } else {
                    Log.d("response", response.isSuccessful() + "-------------");
                    Boolean flog = response.isSuccessful();
                    Info.map.put(key, flog + "");
                }
            }
        });

    }

    /**
     * 异步上传文件
     */
//    public void postAsynFile(String url, File file) {
//        final Info info = new Info();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
//                .build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("wangshu", response.body().string());
//            }
//        });
//    }

    /**
     * 异步下载文件
     */
//    public void downAsynFile(String url) {
//        final Info info = new Info();
//        Request request = new Request.Builder().url(url).build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                InputStream inputStream = response.body().byteStream();
//                FileOutputStream fileOutputStream = null;
//                try {
//                    String icon_path = "/mnt/sdcard/Video" + System.currentTimeMillis()
//                            + ".mp3";
//                    fileOutputStream = new FileOutputStream(icon_path);
//                    byte[] buffer = new byte[2048 * 2048];
//                    int len = 0;
//                    while ((len = inputStream.read(buffer)) != -1) {
//                        fileOutputStream.write(buffer, 0, len);
//                    }
//                    fileOutputStream.flush();
//                } catch (IOException e) {
//                    Log.i("wangshu", "IOException");
//                    e.printStackTrace();
//                }
//
//                Log.d("wangshu", "文件下载成功");
//            }
//        });
//    }
//
//    public void sendMultipart() {
//        final Info info = new Info();
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("title", "wangshu")
//                .addFormDataPart("image", "wangshu.jpg",
//                        RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
//                .build();
//
//        Request request = new Request.Builder()
//                .header("Authorization", "Client-ID " + "...")
//                .url("https://api.imgur.com/3/image")
//                .post(requestBody)
//                .build();
//
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("wangshu", response.body().string());
//            }
//        });
//    }

}
