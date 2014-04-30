Sensoro Android SDK 入门教程 
===============================
重要提示:此Sdk运行需满足两个条件,Android设备支持4.0蓝牙,Android设备系统不低于4.3


> SDK和Example下载地址

```
https://github.com/sensoro/Android-SDK
```

# Step 1:检测环境

开发者需检测需检测当前硬件环境是否能运行SDK

> 示例代码


```
private void check() {
	BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	if (bluetoothAdapter != null) {
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			isSupportBLE = true;//是否支持蓝牙低功耗(蓝牙4.0功能)
		} else {
			isSupportBLE = false;//是否支持蓝牙低功耗(蓝牙4.0功能)
		}
		if (bluetoothAdapter.isEnabled()) {
			isBlueToothOpen = true;//蓝牙已经开启
		} else {
			isBlueToothOpen = false;//蓝牙未开启
		}
	}
}
```

# Step 2:将SDK需要的jar包导入工程

SDK需要如下四个jar包,开发者可从Example工程中得到

```
android-logging-log4j-1.0.3.jar
concurrentlinkedhashmap-lru-1.4.jar
log4j-1.2.17.jar
sensorobeacon.jar
```

# Step 3:构建自己的FsmService

## 使用Sdk必不可少FsmService
开发者需要写一个类继承自FsmService

```
public class ExampleFsmService extends FsmService 
```

## 启动Service

> 示例代码


```
public void startFsmService() {
    String APP_ID = "3";
	String APP_KEY = null;
	SensoroSense sensoroSense = null;

	// 只有设备支持BLE并且蓝牙开启,才能保证SDK正常使用. 用户可以自行处理.

	if (isSupportBLE && isBlueToothOpen) {
		/**
		 * 获取SensoroSense的实例. 第一个参数为context;
		 * 第二个参数是APP_ID,不同APP需要向Sensoro咨询申请不同的APP_ID;
		 * 第三个参数是APP_KEY,不同APP需要不同的APP_KEY.该参数暂未使用. eg:
		 * demo中设置测试APP_ID为1,APP_KEY为null.
		 * 
		 */
		if (sensoroSense == null)
			sensoroSense = SensoroSense.getIntance(this, APP_ID, APP_KEY);
		/**
		 * 启动用户自定义Service,获取Beacon触发的回调函数. 第一个参数为Context;
		 * 第二个参数intent表示启动用户自定义service的intent; 
		 * local_log,表示本地log存储 bool remote_log,表示向SDK后台发送log信息 } eg:
		 * demo中设置允许重新启动,log默认均开启.
		 * 
		 */
		Intent intent = new Intent();
		intent.setClass(this, ExampleFsmService.class);
		sensoroSense.startService(this, intent);
	} else {
		// TODO 用户自行处理,demo中禁止启动APP
		System.exit(0);
	}
}
```
## 停止Service

> 示例代码


```
public void stopFsmService() {
	sensoroSense = SensoroSense.getIntance(this, APP_ID, APP_KEY);
	Intent intent = new Intent();
	intent.setClass(this, ExampleFsmService.class);
	sensoroSense.stopService(intent);
}
```

# Step 4:实现FsmService中的回调函数

FsmService中的回调函数分为三层，开发者根据业务需求，选择合适的层次，并这个层次的回调函数中实现自己的业务逻辑即可。


> 交互层回调方法:

若在交互层实现业务，可以直接利用服务端的界面来定义交互的触发条件。当后台预定义的交互发生时，APP 会获得回调， APP 只需要关心交互的界面实现。

```
onAction(Action action)
```

> 逻辑层回调方法:

```
onEnterSpot(Spot spot, Zone zone)
onLeaveSpot(Spot spot, Zone zone)
onStaySpot(Spot spot, Zone zone, long seconds)
onEnterZone(Zone zone, Spot spot)
onLeaveZone(Zone zone, Spot spo)
onStayZone(Zone zone, Spot spot, long seconds)
```

> 物理层回调方法:

```
onNew(Beacon)
onGone(Beacon)
```


经过如上的4步,开发者已经能够使用该SDK了,如果需要和业务逻辑进行结合,参见Step 5

# Step 5:配置Beacon

有两种方法可以配置Beacon

> 使用后台配置系统配置Beacon的拓展参数

> 使用ItApp为Beacon配置基本参数(可选)

**注:
1. 使用SDK之前要配置您的Beacon.如果开发者没有配置,回调函数中获取到的数据就没有自行配置的内容,也就是说不能够完成相关的业务逻辑.
2. 如果开发者不配置Beacon,将只触发物理层回调,不会触发逻辑层和交互层回调.**


如果开发者想进一步了解如何使用SDK,请参见 Android SDK doc.md
