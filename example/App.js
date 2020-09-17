/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React,{Component} from 'react';
import {
    SafeAreaView,
    StyleSheet,
    ScrollView,
    View,
    Text,
    StatusBar,
    Button,
    NativeModules
} from 'react-native';
import MPush from "./android/lbopush-react-native";

class App extends Component {
    constructor(props) {
        super(props);
    }

    componentDidMount(): * {
        MPush.OnMessageArrived(this.messageArrived);
        MPush.OnMessageClicked(this.messageClicked);
        MPush.OnMessageLocal(this.messageLocal);
    }

    messageArrived(e){
        console.log(e);
    }

    messageClicked(e){
        console.log(e);
    }

    messageLocal(e){
        console.log(e);
    }

    render() {
        return (
            <>
                <StatusBar barStyle="dark-content"/>
                <SafeAreaView>
                    <ScrollView
                        contentInsetAdjustmentBehavior="automatic"
                    >
                        <Button title={"实例华为小米并且创建channel"} onPress={() => {
                            MPush.registerPush("xibo","dd","xiob2233");
                        }} />

                        <Button title={"获取regid"} onPress={async () => {
                            const regid = await MPush.getRegId();
                            console.log(regid);
                        }}/>
                        <Button title={"获取手机品牌"} onPress={async () => {
                            const phone = await MPush.getPhoneType();
                            console.log(phone);
                        }}/>
                        <Button title={"获取华为推送intent 值"} onPress={async () => {
                            NativeModules.MiPush.gethuaweiintentstr();
                        }} />

                        <Button title={"发送本地推"} onPress={async () => {
                            MPush.sendLocalNotification("title","text",{"xiaobo":"sb"});
                        }} />

                        <Button title={"获取是否开启推送"} onPress={async () => {
                            MPush.getisOpenNotification().then(e=>{
                                alert(e);
                            })
                        }} />

                        <Button title={"去app info setting"} onPress={async () => {
                            MPush.startSettingAppInfo();
                        }} />

                    </ScrollView>
                </SafeAreaView>
            </>
        );
    }
}

const styles = StyleSheet.create({});

export default App;
