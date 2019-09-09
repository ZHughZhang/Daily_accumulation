package com.example.daily.network.retrofit;

import android.text.TextUtils;

import com.example.daily.network.Api;
import com.example.daily.network.Constant;
import com.example.daily.network.conver.MineGsonCoverterFactory;
import com.example.daily.network.interceptor.LoggInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network.retrofit
 * ClassName: RetrofitApi
 * Author: zlq
 * CreateDate: 2019-09-03 17:16
 * Version: 1.0
 * Desc: retrofit实现类
 **/
public class RetrofitApi {
    private static Object object = new Object();

    private static RetrofitApi instance;

    private String baseUrl;

    public static RetrofitApi getInstance() {

        return getInstance(null);
    }

    private RetrofitApi(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl) || TextUtils.equals(baseUrl, "null")) {
            baseUrl = Constant.baseUrl;
        }
        this.baseUrl = baseUrl;
    }

    public static RetrofitApi getInstance(String baseUrl) {
        if (instance == null) {
            synchronized (object) {
                if (instance == null) {
                    instance = new RetrofitApi(baseUrl);
                }
            }
        }
        return instance;
    }


    public Api create() {

        Api api = initRetrofit().create(Api.class);
        return api;
    }

    private Retrofit initRetrofit() {
       return initRetrofit(null);
    }

    private Retrofit initRetrofit(OkHttpClient okHttpClient) {

        Retrofit retrofit;

        Retrofit.Builder builder = new Retrofit.Builder();

        builder.baseUrl(baseUrl);
        retrofit = builder.baseUrl(baseUrl)
                .client(okHttpClient == null? initOkhttp():okHttpClient)
                .addConverterFactory(MineGsonCoverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        return retrofit;
    }


    private OkHttpClient initOkhttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(Constant.DEFAULT_TIME, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIME,TimeUnit.SECONDS)
                .connectTimeout(Constant.DEFAULT_TIME+10,TimeUnit.SECONDS)
                //超时重新连
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggInterceptor());
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }


}
