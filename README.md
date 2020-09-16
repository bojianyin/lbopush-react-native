# lbopush-react-native  

## 集成了小米&华为厂商推送,（ios暂不支持），通过小米｜华为厂商提供的api集成的客户端rn-sdk,
###### 暂时不支持oppo,vivo等推送,后续会慢慢迭代集成5大厂商推送，包括ios会接通apn推送,因为本人对oc还是一知半解，所以还是希望有oc大佬来参与,非常欢迎加入!
### issue多多提问题，多多指教，欢迎star,感谢🙏！

## 版本历史
##### `V1.2.1` 删除了registerPush的config参数, app/manifests添加了meta-data，修改了OnMessageArrived｜messageClicked的回调参数结构
##### `V1.2.0` 华为正式接入 支持华为透传推送及普通消息推送（暂时华为推送点击回调 只支持*原生activity* 跳转）--
##### `V1.0.1` 支持小米厂商推送,首次提交

### `透传须知`

###### hms透传即 透明传递，不会有通知显示，但会有消息接收，后面我会写一个本地推送给rn，如果透传有需要显示通知，客户端在rn里自己可调用本地推送从而达到透传推送；
###### hms普通推送 须知：这个目前只支持intent跳转 即 android 的 activity，如果有小伙伴有什么问题 可以私信我，我看能否给你提供一个比较好的方案.

## 安装
    npm install lbopush-react-native
## 包配置
settings.gradle
```javascript
    include ':lbopush-react-native'
    project(':lbopush-react-native').projectDir = new File(rootProject.projectDir, '../node_modules/lbopush-react-native/android')
````

app/build.gradle
```javascript
    implementation project(':lbopush-react-native')
```

src/main/MainApplication
```javascript
    packages.add(new MiPushPackage());
```
app/manifests *在application节点下添加*
```javascript
    <meta-data android:name="xiaomiappkey" android:value="${XM_APPKEY}" />
    <meta-data android:name="xiaomiappid" android:value="${XM_APPID}" />
 ```

app/build.gradle
```javascript
    defaultConfig{
        ...
        manifestPlaceholders=[
                XM_APPID:"xm"+你的appid,
                XM_APPKEY:"xm"+你的appkey,
        ]
    }
```
权限申请(android6.0以后 动态申请电话和存储权限)
```javascript

<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />​

<uses-permission android:name="android.permission.INTERNET" /> 

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> 

<uses-permission android:name="android.permission.READ_PHONE_STATE" /> 

````

## 小米厂商集成步骤
### 手动集成
      暂无需要手动添加任何配置
             
## 华为厂商集成步骤
### 手动集成
    1.首先确保华为开发者联盟正确申请 推送app 并且按照 华为推送文档 正确填写 SHA256指纹.
    2.在华为开发者联盟 正确下载agconnect-services.json 并且放置在 主模块工程目录下 且于src同一级目录
    3.添加HUAWEI agcp插件以及Maven代码库。
      在“allprojects > repositories”中配置HMS Core SDK的Maven仓地址。
      在“buildscript > repositories”中配置HMS Core SDK的Maven仓地址。
      如果App中添加了“agconnect-services.json”文件则需要在“buildscript > dependencies”中增加agcp配置。
      
      buildscript {
          repositories {
              google()
              jcenter()
              maven {url 'https://developer.huawei.com/repo/'}
          }
          dependencies {
              ...
              classpath 'com.huawei.agconnect:agcp:1.3.1.300'
          }
      }
       
      allprojects {
          repositories {
              google()
              jcenter()
              maven {url 'https://developer.huawei.com/repo/'}
          }
      }
    4.华为透传模版
        exp:{
            title:"", 通知栏标题 key不可更改
            body:"",  通知内容   key不可更改
            ... 自定义参数
        }  
    5.华为集成完毕

## 华为推送点击回调须知
    1.普通推送 
      主工程清单文件 android配置一个自定义activity 用于点击普通推送后跳转
      exp:
               <activity android:name=".HuaweiActivity">
                  <intent-filter>
      
                      <action android:name="android.intent.action.VIEW" />
      
                      <category android:name="android.intent.category.DEFAULT" />
                      <category android:name="android.intent.category.BROWSABLE" />
     
                      <data
                          android:host="com.huawei.codelabpush"
                          android:path="/deeplink"
                          android:scheme="pushscheme" />
      
                  </intent-filter>
              </activity>
      切记 必须要配置scheme 支持外部传参至activity
      服务端推送 时 intent地址 由客户端提供获取的intent字符串 开发者可以调用 NativeModules.MiPush.gethuaweiintentstr(); 修改后获取自己所需的string 提供后台填写
      
      activity获取参数
        exp：
              Intent intent = getIntent();
              if (null != intent) {
                  // 方法1设置的数据通过如下方式获取
                  String name1 = intent.getData().getQueryParameter("name");
                  int age1 = Integer.parseInt(intent.getData().getQueryParameter("age"));
                  // 方法2设置的数据通过如下方式获取
                  String name2 = intent.getStringExtra("name");
                  int age2 = intent.getIntExtra("age", -1);
                  Toast.makeText(this, "name " + name1 + ",age " + age1, Toast.LENGTH_SHORT).show();
              }
     2.透传消息
      还在编写中,暂不支持点击推送回调       


### 使用方法
   #### app唤醒必须首先调用 **registerPush** 方法,实例推送服务!!!

### 使用必须

    *必须首先在小米开放平台注册好推送账号.拿到appkey及appid
    *必须首先在华为开发者联盟注册好推送.拿到agconnect-services.json文件


## api:
`已删除` ~~const conf={
    "xiaomi_appid":"你的小米appId",
    "xiaomi_appkey":"你的小米appKey",
};~~

```javascript
static registerPush(String channelname,String channeldec,String channelid):void
注册推送 app启动 首先调用这个方法 必须！！！
```
```javascript
static unregisterPush():void
关闭MiPush推送服务
```

```javascript
static enablePush():void
启用MiPush推送服务
```
```javascript
static disablePush():void
禁用MiPush推送服务
```
```javascript
static setAlias(String alia):void
设置alias
```
```javascript
static unsetAlias():void
取消alias
```
```javascript
static pausePush():void
暂停接收MiPush服务推送的消息
```

```javascript
static resumePush():void
恢复接收MiPush服务推送的消息
```

```javascript
static getAllAlias():promise<List<String>>
获取设备所有别名
```
```javascript
static clearNotification():void
清除米推送通知
```
```javascript
static getRegId():promise<String>
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

```javascript
static getPhoneType()
获取手机厂商品牌
```

```javascript
static getHuaweitoken()
获取华为token 需要上传给服务器 服务器通过token 推送
```

## 监听回调参数
```javascript
   import MPush from "./android/lbopush-react-native";
   MPush.OnMessageArrived(this.messageArrived);
   MPush.OnMessageClicked(this.messageClicked); 

   messageArrived(e){
         // 下面这段是lbopush里的一部分源码,仅供参考，不供rn使用
          WritableMap writableMap= Arguments.createMap();
          WritableMap writableMap= Arguments.createMap();
          writableMap.putString("extra",json.toString()); //自定义参数或华为透传自定义参数
          writableMap.putString("title",null); //小米会返回普通推送的title 华为返回null
          writableMap.putString("description",null);//小米会返回普通推送的description 华为返回null
          writableMap.putString("content",null);//小米会返回普通推送的content 华为返回null
          writableMap.putString("token",message.getToken());//小米null 华为返回华为成功后的token
          writableMap.putInt("messageid",msgId); //消息id
          writableMap.putString("messagetype","华为透传"); //消息类型 小米推送｜华为透传
        
   }

```

## 我会陆续吧vivo,oppo,魅族等5大厂商 原厂推送集成进来 , 期待有能力的伙伴加入进来一起完善提交pr！！！
