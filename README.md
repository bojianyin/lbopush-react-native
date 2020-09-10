# lbopush-react-native

##集成了小米厂商推送,（ios暂不支持），通过小米厂商提供的api集成的客户端rn-sdk,
#暂时不支持华为,oppo,vivo等推送,后续会慢慢迭代集成5大厂商推送，包括ios会接通apn推送,因为本人对oc还是一直半解，所以还是希望有oc大佬来参与,非常欢迎加入!
###issue多多提问题，多多指教，欢迎star,感谢🙏！

#

###手动集成
    settings.gradle
        include ':lbopush-react-native'
        project(':lbopush-react-native').projectDir = new File(rootProject.projectDir, '../node_modules/lbopush-react-native/android')

    app/build.gradle
        implementation project(':lbopush-react-native')
        
    src/main/MainApplication
        packages.add(new MiPushPackage());       

###使用方法
####app唤醒必须首先调用registerPush方法,实例推送服务!!!
###使用必须
*必须首先在小米开放平台注册好推送账号.拿到appkey及appid


```javascript
var err = new VError('missing file: "%s"', '/etc/passwd');
console.log(err.message);
```


##api:
```javascript
static registerPush(String appid,String appkey)
注册推送
```
```javascript
static unregisterPush()
关闭MiPush推送服务
```

```javascript
static enablePush()
启用MiPush推送服务
```
```javascript
static disablePush()
禁用MiPush推送服务
```
```javascript
static setAlias(String alia)
设置alias
```
```javascript
static unsetAlias()
取消alias
```
```javascript
static pausePush()
暂停接收MiPush服务推送的消息
```

```javascript
static resumePush()
恢复接收MiPush服务推送的消息
```

```javascript
static getAllAlias():promise
获取设备所有别名
```
```javascript
static clearNotification()
清除米推送通知
```
```javascript
static getRegId()
获取注册的设备id
```
   
```javascript
static OnMessageArrived()
收到推送回调
```

```javascript
static OnMessageClicked()
点击推送回调
```
```javascript
static removeListener()
删除监听
```

##我会陆续吧华为,vivo,oppo,魅族等5大厂商 原厂推送集成进来 , 期待有能力的伙伴加入进来一起完善提交pr！！！
