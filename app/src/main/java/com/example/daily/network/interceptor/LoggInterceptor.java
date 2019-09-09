package com.example.daily.network.interceptor;

import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network.interceptor
 * ClassName: LoggInterceptor
 * Author: zlq
 * CreateDate: 2019-09-05 09:18
 * Version: 1.0
 * Desc: 日志拦截器
 **/
public class LoggInterceptor implements Interceptor {
    private static final String TAG = LoggInterceptor.class.getName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.e(TAG,"request:"+request.toString());
        long t1 = System.nanoTime();
        Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        Log.e(TAG, String.format(Locale.getDefault(),"Recevied response for %s in %.1fms%n%s",response,request.url(),(t2-t1)/1e6d,response.headers()));
        MediaType mediaType = response.body().contentType();
        String conntent = response.body().string();
        Log.e(TAG, "response body :"+conntent);
        return response.newBuilder().body(ResponseBody.create(mediaType, conntent)).build();

    }


}
