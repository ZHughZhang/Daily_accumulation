package com.example.daily.network.conver;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network.conver
 * ClassName: MineGsonCoverterFactory
 * Author: zlq
 * CreateDate: 2019-09-03 18:15
 * Version: 1.0
 * Desc: java 文件描述
 **/
public class MineGsonCoverterFactory extends Converter.Factory {

    private final Gson mGson;

    private MineGsonCoverterFactory(Gson mGson) {
        if (mGson == null) throw new NullPointerException("Gson == null");
        this.mGson = mGson;
    }

    public static MineGsonCoverterFactory create(){
        return create(new Gson());
    }

    public static MineGsonCoverterFactory create(Gson gson){
        return new MineGsonCoverterFactory(gson);
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GSonResponseBodyConverter<>(type,mGson);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));

        return new GSonRequestBodyConverter<>(mGson,adapter);
    }
}

