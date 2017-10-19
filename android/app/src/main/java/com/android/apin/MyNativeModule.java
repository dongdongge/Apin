package com.android.apin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.apin.utils.ParseJSonUtils;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONException;

/**
 * Created by apin on 2017/10/18.
 */

public class MyNativeModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext mContext;
    //构造方法
    public MyNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }
    @Override
    public String getName() {
        //MyNativeModule 需要此名字来调用该类方法
        return "MyNativeModule";
    }
//    {code:1,textContentMsg:'支付宝支付'},
//    {code:3,textContentMsg:'QQ登录'},
//    {code:5,textContentMsg:'新浪登录'},
//    {code:110,textContentMsg:'QQ分享'},
//    {code:111,textContentMsg:'QQ分享图片'},
//    {code:330,textContentMsg:'新浪分享'},
//    {code:331,textContentMsg:'新浪分享图片'},
    /*
    * Rn侧调用本地的方法去 其中从RN、侧传过来的值只能是String类型的；
    *
    *
    *
    * */
    @ReactMethod
    public void rnCallNativeMethod(String str){
        CallNativeModule module=null;
        if(str.isEmpty()||str==null){
            module = new CallNativeModule();
            module.setCode("-1");
            module.setMsg("传过来数据为空，请检查");
            nativeCallRnMethod(module.toString());
        }
        try {
            module = ParseJSonUtils.parseModuleToJson(str);
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //bundle为需要传给PayActivity的加密签名
            Bundle bundle = new Bundle();
            if(module.getCode().equals("1")){
                bundle.putString("signOrder", module.getData());
                intent.setClass(mContext,AliPayActivity.class);
            }else if (module.getCode().equals("3")||module.getCode().equals("110")||module.getCode().equals("111")){
                bundle.putString("code",module.getCode());
                bundle.putString("shareMsg", module.getData());
                intent.setClass(mContext,ShareQQActivity.class);
            }else {
                bundle.putString("code",module.getCode());
                bundle.putString("shareMsg", module.getData());
                intent.setClass(mContext,ShareSinaActivity.class);
            }
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void nativeCallRnMethod(String str){
        //将消息msg发送给RN侧
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("nativeCallRnMethodListener", str);
    }
}
