package com.example.daily.network.conver;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network.conver
 * ClassName: GSonResponseBodyConverter
 * Author: zlq
 * CreateDate: 2019-09-03 18:04
 * Version: 1.0
 * Desc: Response响应体类
 *  通过拦截Response进行初步数据解析
 *  比如剥离数据中状态返回相关实体
 **/
public class GSonResponseBodyConverter<T> implements Converter<ResponseBody,T> {

    private final Type mType;
    private final Gson mGson;

    private static final MediaType MEDIATYPE = MediaType.parse("application/json;charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private JSONObject jsonObject;


    public GSonResponseBodyConverter(Type mType, Gson mGson) {
        this.mType = mType;
        this.mGson = mGson;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String responsebody;
        try {
            responsebody = value.toString();
        }finally {
            value.close();
        }
        return mGson.fromJson(responsebody,mType);
    }
}
