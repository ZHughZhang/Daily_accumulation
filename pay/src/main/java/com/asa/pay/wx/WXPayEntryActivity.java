package com.asa.pay.wx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.asa.pay.BuildConfig;
import com.asa.pay.Constant;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_KEY,true);
        api.handleIntent(getIntent(),this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

        Log.e(Constant.TAG, "onReq: "+baseReq.openId);

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Intent intent = new Intent();
        intent.setAction(Constant.WX_ACTION);
        intent.putExtra(Constant.WX_ERRORCODE,baseResp.errCode);
        intent.putExtra(Constant.WX_ERRORSTR,baseResp.errStr);
        sendBroadcast(intent);

    }
}
