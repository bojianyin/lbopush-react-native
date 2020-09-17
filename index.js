import {
    DeviceEventEmitter,
    NativeModules,
    Platform
} from 'react-native'

const Mipush = NativeModules.MiPush;
const listeners = {};
const MessageArrived = 'MessageArrived'; //接受到通知
const MessageClicked = 'MessageClicked'; //点击了通知
const MessageLocal   = 'MessageLocal';   //本地通知

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
    static OnMessageLocal(callback){
        listeners[callback] = DeviceEventEmitter.addListener(
            MessageLocal, result => {
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

    //xiaomi -> regiter | huawei->init getToken()
    static registerPush(channelname,channeldec,channelid){
        return Mipush.registerPush(channelname,channeldec,channelid);
    }

    //xiaomi -> unregisterPush | huawei-> deltoken
    static unregisterPush(){
        return Mipush.unregisterPush();
    }

    //xiaomi
    static enablePush(){
        return Mipush.enablePush();
    }

    //xiaomi
    static disablePush(){
        return Mipush.disablePush();
    }

    //xiaomi
    static setAlias(alias){
        return Mipush.setAlias(alias);
    }

    //xiaomi
    static unsetAlias(alias){
        return Mipush.unsetAlias(alias);
    }

    //xiaomi
    static pausePush(){
        return Mipush.pausePush();
    }

    //xiaomi
    static resumePush(){
        return Mipush.resumePush();
    }

    //xiaomi
    static getAllAlias(){
        return Mipush.getAllAlias();
    }

    //xiaomi
    static clearNotification(){
        return Mipush.clearNotification();
    }

    //xiaomi
    static getRegId(){
        return Mipush.getRegId();
    }

    //获取手机厂商品牌
    static getPhoneType(){
        return Mipush.getPhoneType();
    }

    //获取华为token 需要上传给服务器
    static getHuaweitoken(){
        return Mipush.getHuaweitoken();
    }

    //发送本地推送
    static sendLocalNotification(title,text,param){
        return Mipush.sendLocalNotification(title,text,JSON.stringify(param));
    }

    //获取是否开启推送
    static getisOpenNotification(){
        return Mipush.isopenNotification();
    }

    //去设置app权限
    static startSettingAppInfo(){
        return Mipush.startSettingAppInfo();
    }


}
