package com.android.apin;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apin on 2017/10/18.
 */

public class CallNativeModule {
    //可扩展

    String code;//代表了RN侧调用Native 的判别码
    String msg;//表示是信息例如：支付宝支付；QQ分享，QQ分享图片；新浪分享，新浪分享图片
    String data;//传递过来的值；

    @Override
    public String toString() {
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("code",code);
            jsonObject.put("msg",msg);
            jsonObject.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
