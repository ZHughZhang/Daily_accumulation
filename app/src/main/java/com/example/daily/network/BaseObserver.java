package com.example.daily.network;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network
 * ClassName: BaseObserver
 * Author: zlq
 * CreateDate: 2019-09-05 11:18
 * Version: 1.0
 * Desc: 统一处理成功失败
 **/
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        if (tBaseResponse.getCode() ==200) {
            onSuccess(tBaseResponse.getData());
        }else {
            onFailure(null,tBaseResponse.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e,RxExceptionUtil.exceptionHandler(e));
    }

    @Override
    public void onComplete() {

    }


    public abstract void onSuccess(T data);

    public abstract void onFailure(Throwable throwable,String errorMsg);
}
