/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, NativeModules, TouchableOpacity, NativeEventEmitter} from 'react-native';
import RNRestart from 'react-native-restart';

// for Android
// const UpgradeNative = NativeModules.Upgrade; 

// for IOS
const UpgradeNative = NativeModules.HotupdateModule;
const hotupdateManagerEmitter = new NativeEventEmitter(UpgradeNative);


const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

function debounce(func, wait, immediate) {
	var timeout;
	return function() {
		var context = this, args = arguments;
		var later = function() {
			timeout = null;
			if (!immediate) func.apply(context, args);
		};
		var callNow = immediate && !timeout;
		clearTimeout(timeout);
		timeout = setTimeout(later, wait);
		if (callNow) func.apply(context, args);
	};
};



type Props = {};
export default class App extends Component<Props> {
  state = {
    processPercent: 0,
  }

  componentDidMount = () => {
    // 监听 native 事件
    const subscription = hotupdateManagerEmitter.addListener(
      // 'EventReminder',
      'UpdateProcess',
      (data) => {
          this.setState({
            processPercent: data['process_percent']
          })
          /*
        debounce(() => {
          this.setState({
            processPercent: data['process_percent']
          })
        }, 300)
        */
        console.log('saul', data['process_percent'])
      }
    )
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.title}>
          版本 1
        </Text>
        <Text style={styles.title}>
          下载进度{(this.state.processPercent*100).toFixed(2) + '%'}
        </Text>
        <TouchableOpacity
          style={{
            height: 90,
            backgroundColor: 'red',
          }}
          onPress={() => {
            const url = 'http://106.75.233.162/ios.jsbundle'
            // UpgradeNative.startHotUpgrade(url)
            UpgradeNative.startHotupdate(url)
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
        <TouchableOpacity
          style={{
            height: 90,
            backgroundColor: 'red',
          }}
          onPress={() => {
            console.log('saul xxxx', UpgradeNative)
            // RNRestart.Restart();
            UpgradeNative.reloadBundle();
          }}
        >
          <Text style={{
            fontSize: 30,
            color: '#FFF'
          }}>
            重启加载 bundle
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={{
            height: 90,
            backgroundColor: 'red',
          }}
          onPress={() => {
            UpgradeNative.deleteJSBundle()
          }}
        >
          <Text style={{
            fontSize: 30,
            color: '#FFF'
          }}>
          删除更新
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
