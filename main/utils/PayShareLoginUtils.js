/*
*
* 支付的主要文件
*
*
* */
var WeChat = require('react-native-wechat');//微信 支付，分享，登录
import {
    Platform,
    NativeModules,
    NativeAppEventEmitter,
    DeviceEventEmitter,
} from 'react-native';
import fs from 'react-native-fs';
var shareMsg={
    title: '分享朋测试',
    description: '这仅仅是一个测试',
    thumbImage: 'http://pic28.nipic.com/20130419/11696883_093006176353_2.jpg',
    type: 'news',
    webpageUrl: 'http://pic28.nipic.com/20130419/11696883_093006176353_2.jpg',
}
export default class PayShareLoginUtils{
    constructor(){
        this.initSetUp();
    }
    //微信有关的需要初始化  注册商户ID
    initSetUp(){
        WeChat.registerApp('wx3783eec7a89a70d5');
    }

    utils_method(dataParams,successCallBack,failCallBack){
        switch (dataParams.code){
            case 1://支付宝
                if(Platform.OS=='ios'){
                    successCallBack({code:1,msg:'暂未开发'});
                }else {
                    let obj ={code:1,msg:'支付宝支付',data:dataParams.signOrder};
                    NativeModules.MyNativeModule.rnCallNativeMethod(JSON.stringify(obj));
                    DeviceEventEmitter.addListener('nativeCallRnMethodListener',(msg)=>{
                        let data = JSON.parse(msg);
                        console.log('lxd-wb'+msg);
                        if(data.code==1){
                            successCallBack({code:1,msg:'支付成功'});
                        }else {
                            failCallBack({code:-1,msg:'支付失败'});
                        }
                        DeviceEventEmitter.removeAllListeners('nativeCallRnMethodListener');
                    });
                }

                break;
            case 2://微信
                this.pay_method_WXPay(dataParams.signOrder,successCallBack,failCallBack);
                break;
            case 3:
                this.share_login_method_QQ_Sina(dataParams.code,successCallBack,failCallBack);
                break;
            case 4:
                this.login_method_wx(successCallBack,failCallBack);
                break;
            case 5:
                this.share_login_method_QQ_Sina(dataParams.code,successCallBack,failCallBack);
                break;
            case 330://新浪分享
                this.share_login_method_QQ_Sina(dataParams.code,successCallBack,failCallBack);
                break;
            case 331://新浪分享 图片
                this.share_login_method_QQ_Sina(dataParams.code,successCallBack,failCallBack);
                break;
            case 110://qq分享
                this.share_login_method_QQ_Sina(dataParams.code,successCallBack,failCallBack);
                break;
            case 111://qq分享 图片
                this.share_login_method_QQ_Sina(dataParams.code,successCallBack,failCallBack);
                break;

            case 220://微信分享 朋友圈
                this.share_method_WXShareTimeline(dataParams.code,successCallBack,failCallBack);
                break;
            case 221://微信分享 好友
                this.share_method_WXShareToSession(dataParams.code,successCallBack,failCallBack);
                break;
            case 222://微信分享图片 朋友圈
                this.share_method_WXshareToTimeline_ImgFiles(dataParams.code,successCallBack,failCallBack);
                break;
            case 223://微信分享图片  好友
                this.share_method_WXshareToSession_ImgFile(dataParams.code,successCallBack,failCallBack);
                break;
        }


    }
    /**
     * 安卓和Ios 共同使用微信，系统自动判断,
     *
     * */
    pay_method_WXPay(signOrder,successCallBack,failCallBack) {
        WeChat.isWXAppInstalled()
            .then((isInstalled) => {
                if (isInstalled) {
                    WeChat.pay({
                        partnerId: 'bb22f07fcf744a69a3499f21e56c374b',
                        prepayId: 'WX1217752501201407033233368018',
                        nonceStr: '5K8264ILTKCH16CQ2502SI8ZNMTM67VS',
                        timeStamp: '1412000000',
                        package: 'Sign=WXPay',
                        sign: 'C380BEC2BFD727A4B6845133519F3AD6'
                    }).then((response)=>{//回调，支付成功
                        successCallBack({code:1,msg:'支付成功'});
                    }).catch((error) => {//回调，支付失败 异常，
                        failCallBack({code:-1,msg:JSON.stringify(error)});
                    });
                } else {//异常
                    failCallBack({code:-1,msg:'没有安装微信软件，请您安装微信之后再试'})
                }
            });
    }
    /*
    *
    * 微信分享 分享朋友圈 分享的是链接，文字描述，网络图片
    *
    * */
    share_method_WXShareTimeline(shareData,successCallBack,failCallBack){
        WeChat.isWXAppInstalled().then((isInstalled)=>{
            if(isInstalled){
                WeChat.shareToTimeline(shareMsg).then(response => {
                    successCallBack({code:1,msg:'分享成功'});
                }).catch(err => {
                    if(err.code==-2){
                        failCallBack({code:-1,msg:"取消分享"});
                    }else {
                        failCallBack({code:-1,msg:JSON.stringify(err)});
                    }
                });
            }else {
                failCallBack({code:-1,msg:'没有安装微信软件，请您安装微信之后再试'})
            }
        })
    }
    /*
   *
   * 微信分享 分享好友 分享的是链接，文字描述，网络图片
   *
   * */
    share_method_WXShareToSession(shareData,successCallBack,failCallBack){
        WeChat.isWXAppInstalled().then((isInstalled)=>{
            if(isInstalled){
                WeChat.shareToSession(shareMsg).then(response => {
                    successCallBack({code:1,msg:'分享成功'});
                }).catch(err => {
                    if(err.code==-2){
                        failCallBack({code:-1,msg:"取消分享"});
                    }else {
                        failCallBack({code:-1,msg:JSON.stringify(err)});
                    }
                });
            }else {
                failCallBack({code:-1,msg:'没有安装微信软件，请您安装微信之后再试'})
            }
        })
    }

    /**
     *
     * 微信分享 微信分享好友 仅分享图片
     *
     *
     * */
    share_method_WXshareToSession_ImgFile(formUrls,successCallBack,failCallBack){
        formUrls = 'http://y0.ifengimg.com/b2677775d3fcae2d/2015/0819/ori_55d436019e5f1.jpeg';
        const downloadDest = fs.DocumentDirectoryPath+'/apinShare.jpg';
        let wxShareMsgImg =
            Platform.OS == "ios"?
                {
                    type: 'imageFile',
                    title: '分享探索图片',
                    description: '分享图片到好友',
                    mediaTagName: 'email signature',
                    messageAction: undefined,
                    messageExt: undefined,
                    thumbImage:formUrls,
                    imageUrl: 'file://' + downloadDest // require the prefix on both iOS and Android platform
                }
                :
                {
                    type: 'imageFile',
                    title: '分享探索图片',
                    description: '分享图片到好友',
                    mediaTagName: 'email signature',
                    messageAction: undefined,
                    messageExt: undefined,
                    imageUrl: 'file://' + downloadDest // require the prefix on both iOS and Android platform
                };
        const options = {
            fromUrl: formUrls,
            toFile: downloadDest,
            background: true,
        };
        WeChat.isWXAppInstalled().then((isInstalled)=>{
                if(isInstalled){
                    fs.downloadFile(options).promise.then(res => {
                        WeChat.shareToSession(wxShareMsgImg).then(response => {
                            successCallBack({code:1,msg:'分享成功'});
                        }).catch(err=>{
                            if(err.code==-2){
                                failCallBack({code:-1,msg:"取消分享"});
                            }else {
                                failCallBack({code:-1,msg:JSON.stringify(err)});
                            }
                        })
                    }).catch(err => {
                        failCallBack({code:-1,msg:'下载图片异常'+JSON.stringify(err)});
                    })
                }else {
                    failCallBack({code:-1,msg:'没有安装微信软件，请您安装微信之后再试'})
                }
            }
        ).catch((err)=>{
            failCallBack({code:-1,msg:'没有安装微信软件，请您安装微信之后再试'})
        });
    }

    /**
     *
     * 微信分享朋友圈  仅分享图片
     *
     *
     * */
    share_method_WXshareToTimeline_ImgFiles(formUrls,successCallBack,failCallBack){
        formUrls = 'http://y0.ifengimg.com/b2677775d3fcae2d/2015/0819/ori_55d436019e5f1.jpeg';
        const downloadDest = fs.DocumentDirectoryPath+'/apinShare.jpg';
        let wxShareMsgImg =
            Platform.OS == "ios"?
                {
                    type: 'imageFile',
                    title: '分享探索图片',
                    description: '分享图片到好友',
                    mediaTagName: 'email signature',
                    messageAction: undefined,
                    messageExt: undefined,
                    thumbImage:formUrls,
                    imageUrl: 'file://' + downloadDest // require the prefix on both iOS and Android platform
                }
                :
                {
                    type: 'imageFile',
                    title: '分享探索图片',
                    description: '分享图片到好友',
                    mediaTagName: 'email signature',
                    messageAction: undefined,
                    messageExt: undefined,
                    imageUrl: 'file://' + downloadDest // require the prefix on both iOS and Android platform
                };
        const options = {
            fromUrl: formUrls,
            toFile: downloadDest,
            background: true,
        };
        WeChat.isWXAppInstalled().then((isInstalled)=>{
                if(isInstalled){
                    fs.downloadFile(options).promise.then(res => {
                          WeChat.shareToTimeline(wxShareMsgImg).
                          then(response => {successCallBack({code:1,msg:'分享成功'});})
                              .catch(err=>{
                                  if(err.code==-2){
                                      failCallBack({code:-1,msg:"取消分享"});
                                  }else {
                                      failCallBack({code:-1,msg:JSON.stringify(err)});
                                  }})
                    }).catch(err => {
                        failCallBack({code:-1,msg:'下载图片异常'+JSON.stringify(err)});
                    })
                }else {
                    failCallBack({code:-1,msg:'没有安装微信软件，请您安装微信之后再试'})
                }
            }
        ).catch((err)=>{
            failCallBack({code:-1,msg:'没有安装微信软件，请您安装微信之后再试'})
        });
    }
    login_method_wx(successCallBack,failCallBack){
        let scope = 'snsapi_userinfo';
        let state = 'wechat_sdk_demo';
        //发送授权请求
        WeChat.isWXAppInstalled().then((isInstalled) => {
            if (isInstalled) {
                WeChat.sendAuthRequest(scope, state).then(response => {
                    //返回code码，通过code获取access_token
                    return this.getAccessToken(response.code)
                }).then((res) => {
                    successCallBack({code: 1,msg:{'AccessToken': res.access_token, 'OpenId': res.unionid}})
                }).catch(err => {
                    if (err.code===-2){
                        failCallBack({code: -1, msg: '用户取消',})
                    }else {
                        failCallBack({code: -1, msg: '登录失败' + JSON.stringify(err),})
                    }
                })
            } else {
                failCallBack({code: -1, msg: '请安装微信客户端'})
            }
        }).catch((err)=>{
            failCallBack({code:-1,msg:'请安装微信客户端'})
        });
    }
    getAccessToken(str) {
        return fetch(`https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx3783eec7a89a70d5&secret=9e12874b19023223e474890b4d0845d2&code=${str}&grant_type=authorization_code`, {
            method: 'GET',
        }).then((response) => response.json())
            .then(
                (responseData) => { // 上面的转好的json
                    return responseData
                });
    }
    share_login_method_QQ_Sina(code,successCallBack,failCallBack){
        if(Platform.OS=='ios'){
            successCallBack({code:1,msg:'暂未开发'});
        }else {
            let obj ={code:code,msg:'qq登录 新浪登录',data:shareMsg};
            NativeModules.MyNativeModule.rnCallNativeMethod(JSON.stringify(obj));
            DeviceEventEmitter.addListener('nativeCallRnMethodListener',(msg)=>{
                let data = JSON.parse(msg);
                if(data.code==1){
                    successCallBack({code:1,msg:'操作成功'+data.data});
                }else {
                    failCallBack({code:-1,msg:'操作失败'+data.msg});
                }
                DeviceEventEmitter.removeAllListeners('nativeCallRnMethodListener');
            });
        }
    }
}