package com.android.apin.utils;

import com.android.apin.CallNativeModule;
import com.android.apin.ShareModule;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apin on 2017/10/18.
 */

public class ParseJSonUtils {
    public static CallNativeModule parseModuleToJson(String str) throws JSONException {
        CallNativeModule module = new CallNativeModule();
        JSONObject jsonObject = new JSONObject(str);
        String code = jsonObject.optString("code","");
        String msg = jsonObject.optString("msg","");
        String data = jsonObject.optString("data","");
        module.setCode(code);
        module.setMsg(msg);
        module.setData(data);
        return module;
    }
    public static ShareModule parseStringToShareModule(String str) {
        ShareModule shareModule = null;
        try {
            JSONObject jsonObject = new JSONObject(str);
            String title = jsonObject.optString("title","");
            String description = jsonObject.optString("description","");
            String thumbImage = jsonObject.optString("thumbImage","");
            String webpageUrl = jsonObject.optString("webpageUrl","");
            shareModule = new ShareModule(title, description, thumbImage,webpageUrl);
            return shareModule;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shareModule;
    }
}
