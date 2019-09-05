package com.example.daily.network.conver;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network.conver
 * ClassName: GSonRequestBodyConverter
 * Author: zlq
 * CreateDate: 2019-09-03 17:52
 * Version: 1.0
 * Desc: 处理请求体
 **/
public class GSonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private final static MediaType MEDIATYPE  = MediaType.parse("application/json;charset=UTF-8");
    private final static Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson mGson;
    private final TypeAdapter<T> adapter;


    public GSonRequestBodyConverter(Gson mGson, TypeAdapter<T> adapter) {
        this.mGson = mGson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {

        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(),UTF_8);

        JsonWriter jsonWriter = mGson.newJsonWriter(writer);
        adapter.write(jsonWriter,value);

        jsonWriter.close();

        return RequestBody.create(MEDIATYPE,buffer.readByteString());
    }
}
