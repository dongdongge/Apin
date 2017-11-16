/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  View
} from 'react-native';
import Routers from './main/Routers.js';

export default class App extends Component<{}> {

  render() {
    return (
      <View style={{flex:1}}>
       <Routers/>
      </View>
    );
  }
}

