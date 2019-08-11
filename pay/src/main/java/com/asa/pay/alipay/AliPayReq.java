package com.asa.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.alipay.sdk.app.PayTask;

import java.lang.ref.WeakReference;
import java.util.Map;

public class AliPayReq {

    private static final int SDK_PAY_FLAG = 1;
    private Activity activity;
    private static OnALiPayListener listener;

    //支付宝支付配置
    private String orderInfo;

    //用户返回支付结果的handler
    private Handler mHandler;

    public AliPayReq() {
        super();
        mHandler = new WeakHandle(activity);

    }


    /**
     * 发送支付结果
     */
    public void send() {
        final  String orderInfo = this.orderInfo;
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                PayTask payTask = new PayTask(activity);
                Map<String,String> result = payTask.payV2(orderInfo,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }


    public static  class Builder{
        private Activity activity;
        private String orderInfo;

        public Builder() {
            super();
        }
        public Builder with(Activity activity){
            this.activity = activity;
            return this;
        }
        public Builder setOrderInfo(String orderInfo){
            this.orderInfo = orderInfo;
            return this;
        }

        public AliPayReq create(){
            AliPayReq req = new AliPayReq();
            req.activity = this.activity;
            req.orderInfo = this.orderInfo;
            return req;
        }
    }

    public AliPayReq setOnALiPayListener(OnALiPayListener listener){
        this.listener = listener;
        return this;
    }

    public interface OnALiPayListener{
        void onPaySuccess(String resultInfo);
        void onPayFailure(String resultInfo);
        void onPayConfirming(String resultInfo);
    }

    private static class WeakHandle extends Handler {
        private WeakReference<Activity> weakReference;

        public WeakHandle(Activity activity) {
            this.weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference.get() == null) return;
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    String resultStatus = map.get("resultStatus");
                    String resultInfo = map.get("resultInfo");
                    String memo = map.get("memo");
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (listener != null) listener.onPaySuccess(resultInfo);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        if (listener != null) listener.onPayConfirming(resultInfo);
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        if (listener != null) listener.onPayFailure(memo);
                    } else {
                        if (listener != null) listener.onPayFailure(resultInfo);
                    }
                }
                break;
                default:
                    break;
            }
        }
    }
}
