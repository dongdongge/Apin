/*
*
* 极光推送
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
export default class DemoJpush extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return(
            <ScrollView style={{flex:1}}>
                <TouchableOpacity
                 onPress={()=>{alert('极光推送测试')}}
                 style={{height:50,justifyContent:'center',alignItems:'center'}}>
                    <Text>极光推送测试</Text>
                </TouchableOpacity>
            </ScrollView>
        )
    }
}