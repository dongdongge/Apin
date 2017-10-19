/*
*
* 支付类的demo 1支付宝支付，2微信支付
*
*
* */
import React, {Component} from 'react';
import {
    Platform,
    TouchableOpacity,
    ScrollView,
    Text,
    View,
}from 'react-native';
import {WeChat} from 'react-native-wechat';
import PayUtils from '../utils/PayShareLoginUtils.js';
import ToastView from '../view/ToastView.js';
var DataJson=[
    {code:1,textContentMsg:'支付宝支付'},
    {code:2,textContentMsg:'微信支付'},
    {code:3,textContentMsg:'QQ登录'},
    {code:4,textContentMsg:'微信登录'},
    {code:5,textContentMsg:'新浪登录'},
    {code:110,textContentMsg:'QQ分享'},
    {code:111,textContentMsg:'QQ分享图片'},
    {code:220,textContentMsg:'微信分享-朋友圈'},
    {code:221,textContentMsg:'微信分享-好友'},
    {code:222,textContentMsg:'微信分享图片-朋友圈'},
    {code:223,textContentMsg:'微信好友图片-好友'},
    {code:330,textContentMsg:'新浪分享'},
    {code:331,textContentMsg:'新浪分享-图片'},
];

export default class DemoPay extends Component{
    constructor(props){
        super(props);
        this.PayUtils = new PayUtils();
    }
    static navigationOptions = {
        headerTitle:"支付-分享-登录",
        headerBackTitle: "返回",
        gesturesEnabled:false,
    };

    selectedOnClick(code){
        this.PayUtils.utils_method({code:code,signOrder:'支付订单签名字符串，数据源'},
            (successCallBack)=>{this.showToastViewAction.showToast(successCallBack.code+successCallBack.msg)},
            (failCallBack)=>{ this.showToastViewAction.showToast(failCallBack.code+failCallBack.msg)});
    }
    render(){
        return(
            <View style={{flex:1,}}>
                <ScrollView>
                    {DataJson.map((item,i)=>{
                        return(
                        <TouchableOpacity
                            key={i}
                            onPress={()=>{ this.selectedOnClick(item.code)}}
                            style={{height:50,backgroundColor:'#32c17c',alignItems:'center',
                                borderRadius:10,
                                justifyContent:'center',margin:10}}>
                            <Text style={{color:'#FFF'}}>{item.textContentMsg}</Text>
                        </TouchableOpacity>)
                    })}
                </ScrollView>
                <ToastView ref={(ref)=>{this.showToastViewAction=ref}} />
            </View>
        )
    }
}