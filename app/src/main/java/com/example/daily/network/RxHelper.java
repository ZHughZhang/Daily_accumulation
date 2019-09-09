package com.example.daily.network;

import android.content.Context;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network
 * ClassName: RxHelper
 * Author: zlq
 * CreateDate: 2019-09-05 15:12
 * Version: 1.0
 * Desc: Transformer
 **/
public class RxHelper {

    public static <T> ObservableTransformer<T,T> observableIO(final Context context){
        return upstream -> {
            Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            return observable;
        };
    }

    public static <T> ObservableTransformer<T,T> observableIO(RxFragment fragment){
        return upstream -> {
            Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(fragment.<T>bindToLifecycle());

            return observable;
        };
    }

    public static <T> FlowableTransformer<T,T> flowableIO(){
       return  upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public <T>ObservableSource<T> composrContext(Context context,Observable<T> observable){

        if (context instanceof RxActivity){
            return observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxFragmentActivity) {
            return observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxAppCompatActivity) {
            return observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        }else {
            return observable;
        }

    }




}
