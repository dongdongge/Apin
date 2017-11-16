/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  TouchableOpacity,
  View
} from 'react-native';

import {StackNavigator,TabNavigator} from 'react-navigation';
import DemoPay from './DemoPay.js';
import DemoJpush from './DemoJpush.js';

import ToastView from '../view/ToastView.js';
const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

import HttpTools from '../http/HttpTools.js';
export default class IndexDmeo extends Component<{}> {

  render() {
      const { navigate } = this.props.navigation;
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit App.js
        </Text>
          <TouchableOpacity onPress={()=>{
              HttpTools.get('/users/queryUser',(res)=>{
                  this.showToastViewAction.showToast(res.code+res.msg);
              },(err)=>[
                  this.showToastViewAction.showToast(err.code+err.msg)
              ]);
          }}>
                <Text style={styles.instructions}>
                  请求联网测试
                </Text>
          </TouchableOpacity>
        <TouchableOpacity
            onPress={()=>{navigate('pay')}}
            style={{height:50}}>
          <Text>跳转Demo</Text>
        </TouchableOpacity>
          <ToastView ref={(ref)=>{this.showToastViewAction=ref}} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
