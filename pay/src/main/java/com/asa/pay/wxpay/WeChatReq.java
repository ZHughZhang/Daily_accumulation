package com.asa.pay.wxpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import com.asa.pay.BuildConfig;
import com.asa.pay.Constant;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static android.support.constraint.Constraints.TAG;

public class WeChatReq {

    private Context context;

    private String appId;
    private String partnerId;
    private String prepayId;
    private String packageValue;
    private String nonceStr;
    private String timeStamp;
    private String sign;
    private static OnWeChatListener listener;

    private static PayBroadCastReceive receive;

    IWXAPI iwxapi;

    public WeChatReq() {
        super();
    }

    public void send() {
        iwxapi = WXAPIFactory.createWXAPI(context.getApplicationContext(),null);
        iwxapi.registerApp(BuildConfig.WX_KEY);

        if (iwxapi.isWXAppInstalled()){
            PayReq payReq = new PayReq();
            payReq.appId = this.appId;
            payReq.packageValue = this.packageValue== null || TextUtils.isEmpty(this.packageValue)? "Sign=WXPay":this.packageValue;
            payReq.nonceStr = this.nonceStr;
            payReq.partnerId = this.partnerId;
            payReq.sign = this.sign;
            payReq.timeStamp = this.timeStamp;
            payReq.prepayId = this.prepayId;
            iwxapi.sendReq(payReq);
        }else {
            Log.e(TAG, "send: 未安装微信");
        }
    }

    public static class Builder {
        private Context context;

        //微信支付AppId
        private String appId;
        //微信支付商号
        private String partnerId;
        //预支付码（重要）
        private String prepayId;
        //"Sign=WXPay"
        private String packageValue = "Sign=WXPay";
        //
        private String nonceStr;
        //时间戳
        private String timeStamp;
        //签名
        private String sign;

        public Builder() {
            super();
        }

        /**
         * 上下文
         * @param context
         * @return
         */
        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        /**
         * 微信支付AppId
         * @param appId
         * @return
         */
        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * 微信支付商号
         * @param partnerId
         * @return
         */
        public Builder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        /**
         * 预支付码（重要）
         * @param prepayId
         * @return
         */
        public Builder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }

        /**
         *"Sign=WXPay"
         * @param packageValue
         * @return
         */
        public Builder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        /**
         *设置
         * @param nonceStr
         * @return
         */
        public Builder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        /**
         *设置时间戳
         * @param timeStamp
         * @return
         */
        public Builder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        /**
         * 设置签名
         * @param sign
         * @return
         */
        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }


        public WeChatReq create(){
            WeChatReq req = new WeChatReq();
            req.appId = this.appId;
            req.context = this.context;
            req.nonceStr = this.nonceStr;
            req.packageValue = this.packageValue;
            req.partnerId = this.partnerId;
            req.sign = this.sign;
            req.timeStamp = this.timeStamp;
            req.prepayId = this.prepayId;
            return req;
        }
    }


    public WeChatReq setOnWeChatListener(OnWeChatListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnWeChatListener{
        void onPaySuccess(int errorCode,String errorStr);
        void onPayFailure(int errorCode, String errorStr);
    }

    public static class PayBroadCastReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constant.WX_ACTION){
                int errorCode = intent.getIntExtra(Constant.WX_ERRORCODE,-1);
                if (errorCode == BaseResp.ErrCode.ERR_OK){
                    if (listener != null){
                        listener.onPaySuccess(errorCode,intent.getStringExtra(Constant.WX_ERRORSTR));
                    }
                }else {
                    if (listener != null){
                        listener.onPayFailure(errorCode,intent.getStringExtra(Constant.WX_ERRORSTR));
                    }
                }
            }
        }
    }



    public static void registerBroadCast(Context context){
        receive = new PayBroadCastReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.WX_ACTION);
        context.getApplicationContext().registerReceiver(receive,intentFilter);
    }
    public static void unRegisterBroadCast(Context context){
        if (receive != null){
            context.getApplicationContext().unregisterReceiver(receive);
        }
    }

}
