/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, NativeModules, TouchableOpacity} from 'react-native';

const UpgradeNative = NativeModules.Upgrade;

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.title}>
          版本 1
        </Text>
        <TouchableOpacity
          style={{
            height: 90,
            backgroundColor: 'red',
          }}
          onPress={() => {
            const url = 'http://106.75.233.162/index.android.bundle'
            UpgradeNative.startHotUpgrade(url)
            // UpgradeNative.show("xxxxx", 300)
          }}
        >
          <Text style={{
            fontSize: 30,
            color: '#FFF'
          }}>
            点我升级
          </Text>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  btn: {
    width: 200,
      height: 200,
      backgroundColor: 'red',
  },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'green',
  },
  title: {
    color: '#FFF',
    fontSize: 90,
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
