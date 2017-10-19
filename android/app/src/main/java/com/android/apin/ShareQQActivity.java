package com.android.apin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.apin.utils.DownloadImgUtils;
import com.android.apin.utils.ParseJSonUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class ShareQQActivity extends Activity implements IUiListener {

    public static final String Scope = "all";//授权类型
    String APP_ID = "1105694910";//ID
    ShareQQActivity activity;
    Tencent mTencent;
    CallNativeModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        activity = this;
        module = new CallNativeModule();
        Bundle bundle = getIntent().getExtras();
        String code = bundle.getString("code");
        if (code.equals("3")) {
            mTencent.login(this, Scope, this);
        } else if (code.equals("110")) {
            qqShare(bundle.getString("shareMsg"));
        } else if (code.equals("111")) {
            qqShareImg(bundle.getString("shareMsg"));
        }
    }

    public void qqGetUserInfor() {
        mTencent.setOpenId(mTencent.getOpenId());
        UserInfo mInfo = new UserInfo(this, mTencent.getQQToken());
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                module.setCode("1");
                module.setMsg("qq登录成功");
                module.setData(getTencent(mTencent));
                MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                finish();
            }

            @Override
            public void onError(UiError uiError) {
                module.setCode("-1");
                module.setMsg("qq登录失败");
                module.setData("" + uiError.errorDetail);
                MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                finish();
            }

            @Override
            public void onCancel() {
                module.setCode("-1");
                module.setMsg("qq登录失败");
                module.setData("用户取消");
                MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                finish();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    public void qqShareImg(String msg) {
        ShareModule shareModule = ParseJSonUtils.parseStringToShareModule(msg);
        final Bundle bundle = new Bundle();
        DownloadImgUtils.getNetworkImgUrlToSaveLocalFile(shareModule.getThumbImage(), DownloadImgUtils.getSDCardPath() + "/ApinFile/DownLoadImg/", "2.jpg", new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.e("===", "选用默认的数值===下载成功");
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);// 设置分享类型为纯图片分享
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, DownloadImgUtils.getSDCardPath() + "/ApinFile/DownLoadImg/" + "2.jpg");
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "爱拼机");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTencent.shareToQQ(activity, bundle, new IUiListener() {
                            @Override
                            public void onCancel() {
                                module.setCode("-1");
                                module.setMsg("qq分享图片失败");
                                module.setData("用户取消");
                                MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                                finish();
                            }

                            @Override
                            public void onComplete(Object response) {
                                // TODO Auto-generated method stub
                                module.setCode("1");
                                module.setMsg("qq分享图片成功");
                                module.setData("qq分享图片成功");
                                MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                                finish();
                            }

                            @Override
                            public void onError(UiError e) {
                                module.setCode("-1");
                                module.setMsg("qq分享图片失败");
                                module.setData("qq分享图片失败");
                                MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                                finish();
                            }
                        });
                    }
                });

                return false;
            }
        }));
        return;

    }

    public void qqShare(String msg) {
        ShareModule shareModule = ParseJSonUtils.parseStringToShareModule(msg);
        final Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
//        bundle.putString(QQConstants.PARAM_TARGET_URL, "http://connect.qq.com/");
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareModule.getTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareModule.getDescription());
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareModule.getWebpageUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareModule.getThumbImage());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(activity, bundle, new IUiListener() {
                    @Override
                    public void onCancel() {
                        module.setCode("-1");
                        module.setMsg("qq分享失败");
                        module.setData("用户取消");
                        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                        finish();
                    }

                    @Override
                    public void onComplete(Object response) {
                        // TODO Auto-generated method stub
                        module.setCode("1");
                        module.setMsg("qq分享成功");
                        module.setData("qq分享成功");
                        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                        finish();
                    }

                    @Override
                    public void onError(UiError e) {
                        module.setCode("-1");
                        module.setMsg("qq分享失败");
                        module.setData("" + e.errorDetail);
                        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onComplete(Object o) {
        initOpenidAndToken((JSONObject) o);
        qqGetUserInfor();
    }

    @Override
    public void onError(UiError uiError) {
        module.setCode("-1");
        module.setMsg("qq分享失败");
        module.setData("" + uiError.errorDetail);
        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
        finish();
    }

    @Override
    public void onCancel() {
        module.setCode("-1");
        module.setMsg("qq登录失败");
        module.setData("用户取消");
        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
        finish();
    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    public String getTencent(Tencent mTencent) {
        JSONObject jsonObject = new JSONObject();
        String str = "";
        try {
            jsonObject.put("name", "QQ");
            jsonObject.put("AccessToken", mTencent.getAccessToken());
            jsonObject.put("OpenId", mTencent.getOpenId());
            str = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }
}
