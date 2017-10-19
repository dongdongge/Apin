import React, {Component} from 'react';
import {
    Platform,
    TouchableOpacity,
    ScrollView,
    Text,
    View,
}from 'react-native';
export default class ToastView extends Component{

    constructor(props){
        super(props);
        this.state={
            isShow:false,
            showText:'',
        }
    }
    showToast=(showText)=>{
        this.setState({
            showText:showText,
            isShow:true,
        })
        this.ShowTextTimeOut&&clearTimeout(this.ShowTextTimeOut);
        this.ShowTextTimeOut =setTimeout(()=>{
            this.setState({
                showText:'',
                isShow:false,
            });
        },3000);

    }
    render(){
        return(
            this.state.isShow? <View style={{flex:1,width:'100%',height:'100%',position:'absolute',top:0,left:0,justifyContent:'center',alignItems:'center'}}>
                <View style={{width:'50%',paddingTop:20,paddingBottom:20,backgroundColor:'#2b2b2b',borderRadius:10,justifyContent:'center',alignItems:'center'}}>
                    <Text style={{textAlign:'center',color:'#FFF',}}>
                        {this.state.showText}
                    </Text>
                </View>
            </View>:null
           )
    }


}