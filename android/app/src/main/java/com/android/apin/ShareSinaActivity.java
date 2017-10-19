package com.android.apin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.apin.utils.DownloadImgUtils;
import com.android.apin.utils.ParseJSonUtils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apin on 2017/10/19.
 */

public class ShareSinaActivity extends Activity implements WbShareCallback {
    private WbShareHandler shareHandler;
    ShareSinaActivity context;
    CallNativeModule module;
    String code;
    String shareMsg;
    /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String APP_KEY      = "3448287776";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     *
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        WbSdk.install(this, new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE));
        shareHandler = new WbShareHandler(this);
        module= new CallNativeModule();
        shareHandler.registerApp();
//        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        Bundle bundle = getIntent().getExtras();
        shareMsg = bundle.getString("shareMsg");
        code = bundle.getString("code");
        mSsoHandler = new SsoHandler(this);
        if (code.equals("5")) {
            mSsoHandler.authorize(wbAuthListener);
        } else if (code.equals("330")||code.equals("331")){
            Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(this);
            if (mAccessToken.isSessionValid()){
                weiboShare(shareMsg,code);
            }else {
                mSsoHandler.authorize(wbAuthListener);
            }
        }else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void weiboShare(String msg,String code) {
        ShareModule shareModule = ParseJSonUtils.parseStringToShareModule(msg);
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if(code.equals("331")){
            weiboShareImg(msg);
        }else {
            weiboMessage.textObject = getTextObj(shareModule);
            weiboMessage.imageObject = getImageObj(context);
            weiboMessage.mediaObject = getWebpageObj(context,shareModule);
            shareHandler.shareMessage(weiboMessage, false);
        }
    }


    public void weiboShareImg(String msg){
        ShareModule shareModule = ParseJSonUtils.parseStringToShareModule(msg);
        DownloadImgUtils.getNetworkImgUrlToSaveLocalFile(shareModule.getThumbImage(), DownloadImgUtils.getSDCardPath() + "/ApinFile/DownLoadImg/", "2.jpg", new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                        weiboMessage.imageObject = getSinaShareImageObj(context, DownloadImgUtils.getSDCardPath() + "/ApinFile/DownLoadImg/"+ "2.jpg");
                        shareHandler.shareMessage(weiboMessage, false);
                    }

                });
                return false;
            }
        }));


    }


    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    public static ImageObject getSinaShareImageObj(Context context, String path) {

        ImageObject imageObject = new ImageObject();
        Bitmap bitmap1 = DownloadImgUtils.decodeImage(path);
        imageObject.setImageObject(bitmap1);
        return imageObject;
    }
    //微博登录
    SsoHandler mSsoHandler;
    WbAuthListener wbAuthListener = new WbAuthListener() {
        @Override
        public void onSuccess(final Oauth2AccessToken oauth2AccessToken) {

            Log.e("====","===新浪微博授权成功");
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = oauth2AccessToken;
                    // 显示 Token
                    // 保存 Token 到 SharedPreferences
//                        updateTokenView(mAccessToken, mTokenText, true);
                    AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                    if (code.equals("5")) {
                        module.setCode("1");
                        module.setMsg("新浪登录成功");
                        module.setData(getSina(mAccessToken));
                        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
                        finish();
                    } else {
                        weiboShare(shareMsg,code);
                    }
                }
            });
        }

        @Override
        public void cancel() {
            module.setCode("-1");
            module.setMsg("新浪登录失败，原因用户取消");
            module.setData("用户取消");
            MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
            finish();
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            module.setCode("-1");
            module.setMsg("新浪登录失败");
            module.setData("授权失败");
            MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
            finish();
        }
    };
    /**
     * 新浪请求接口
     *
     * @author Administrator
     */
    private Oauth2AccessToken mAccessToken;

    @Override
    public void onWbShareSuccess() {
        module.setCode("1");
        module.setMsg("新浪分享成功");
        module.setData("新浪分享成功");
        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
        finish();
    }

    @Override
    public void onWbShareCancel() {
        module.setCode("-1");
        module.setMsg("新浪分享失败"+"失败原因用户取消");
        module.setData("用户取消");
        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
        finish();
    }

    @Override
    public void onWbShareFail() {
        module.setCode("-1");
        module.setMsg("新浪分享失败");
        module.setData("分享失败");
        MainApplication.getMyReactPackage().myNativeModule.nativeCallRnMethod(module.toString());
        finish();
    }

    public String getSina(Oauth2AccessToken mAccessToken) {
        JSONObject jsonObject = new JSONObject();
        String str = "";
        try {
            jsonObject.put("name", "SINA");
            jsonObject.put("AccessToken", mAccessToken.getToken());
            jsonObject.put("OpenId", mAccessToken.getUid());
            str = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    public static TextObject getTextObj(ShareModule shareModule ) {
        TextObject textObject = new TextObject();
        textObject.text = shareModule.getDescription();
        textObject.title = shareModule.getTitle();
        textObject.actionUrl = shareModule.getWebpageUrl();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    public static ImageObject getImageObj(Context context) {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    public static WebpageObject getWebpageObj(Context context, ShareModule shareModule) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "爱拼机分享";
        mediaObject.description = "分享消息";
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = shareModule.getWebpageUrl();
        mediaObject.defaultText = "爱拼机分享";
        return mediaObject;
    }
}
