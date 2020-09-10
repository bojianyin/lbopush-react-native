import {
    DeviceEventEmitter,
    NativeModules,
    Platform
} from 'react-native'

const Mipush = NativeModules.MiPush;
const listeners = {};
const MessageArrived = 'MessageArrived'; //接受到通知
const MessageClicked = 'MessageClicked'; //点击了通知

export default class MyPush{
    static OnMessageArrived(callback) {
        listeners[callback] = DeviceEventEmitter.addListener(
            MessageArrived, result => {
                callback(result)
            })
    }
    static OnMessageClicked(callback) {
        listeners[callback] = DeviceEventEmitter.addListener(
            MessageClicked, result => {
                callback(result)
            })
    }
    static removeListener(callback) {
        if (!listeners[callback]) {
            return
        }
        listeners[callback].remove()
        listeners[callback] = null
    }

    static registerPush(appid,appkey){
        return Mipush.registerPush(appid,appkey);
    }

    static unregisterPush(){
        return Mipush.unregisterPush();
    }

    static enablePush(){
        return Mipush.enablePush();
    }

    static disablePush(){
        return Mipush.disablePush();
    }


    static setAlias(alias){
        return Mipush.setAlias(alias);
    }
    static unsetAlias(alias){
        return Mipush.unsetAlias(alias);
    }
    static pausePush(){
        return Mipush.pausePush();
    }
    static resumePush(){
        return Mipush.resumePush();
    }

    static getAllAlias(){
        return Mipush.getAllAlias();
    }

    static clearNotification(){
        return Mipush.clearNotification();
    }

    static getRegId(){
        return Mipush.getRegId();
    }
}
