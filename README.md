Sensoro Android SDK 入门教程 
===============================
重要提示：此 Sdk 运行需满足两个条件，Android 设备支持 4.0 蓝牙，Android 设备系统不低于 4.3


> SDK 和 Example 下载地址

```
https://github.com/sensoro/Android-SDK
```

# Step 1：检测环境

开发者需检测需检测当前硬件环境是否能运行 SDK

> 示例代码


```
private void check() {
	BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	if (bluetoothAdapter != null) {
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			isSupportBLE = true;//是否支持蓝牙低功耗(蓝牙 4.0 功能)
		} else {
			isSupportBLE = false;//是否支持蓝牙低功耗(蓝牙 4.0 功能)
		}
		if (bluetoothAdapter.isEnabled()) {
			isBlueToothOpen = true;//蓝牙已经开启
		} else {
			isBlueToothOpen = false;//蓝牙未开启
		}
	}
}
```

# Step 2：将 SDK 需要的 jar 包导入工程

SDK 需要如下四个 Jar 包，开发者可从 Example 工程中得到

```
android-logging-log4j-1.0.3.jar
concurrentlinkedhashmap-lru-1.4.jar
log4j-1.2.17.jar
sensorobeacon.jar
```

# Step 3：构建自己的 FsmService

## 使用 Sdk 必不可少 FsmService
开发者需要写一个类继承自 FsmService

```
public class ExampleFsmService extends FsmService 
```

## 启动 Service

> 示例代码


```
public void startFsmService() {
    String APP_ID = "3";
	String APP_KEY = null;
	SensoroSense sensoroSense = null;

	// 只有设备支持 BLE 并且蓝牙开启，才能保证SDK正常使用。用户可以自行处理。

	if (isSupportBLE && isBlueToothOpen) {
		/**
		 * 获取 SensoroSense 的实例. 第一个参数为 context；
		 * 第二个参数是 APP_ID，不同 APP 需要向 Sensoro 咨询申请不同的 APP_ID；
		 * 第三个参数是 APP_KEY，不同 APP 需要不同的 APP_KEY。该参数暂未使用. eg：
		 * Demo 中设置测试 APP_ID 为 1，APP_KEY 为 Null.
		 * 
		 */
		if (sensoroSense == null)
			sensoroSense = SensoroSense.getIntance(this, APP_ID, APP_KEY);
		/**
		 * 启动用户自定义 Service，获取 Beacon 触发的回调函数。第一个参数为 Context;
		 * 第二个参数 Intent 表示启动用户自定义 Service 的 Intent； 
		 * Local_log，表示本地 Log 存储 Bool Remote_log，表示向 SDK 后台发送 Log 信息} eg：
		 * Demo 中设置允许重新启动，Log 默认均开启。
		 * 
		 */
		Intent intent = new Intent();
		intent.setClass(this, ExampleFsmService.class);
		sensoroSense.startService(this, intent);
	} else {
		// TODO 用户自行处理，Demo 中禁止启动 APP
		System.exit(0);
	}
}
```
## 停止 Service

> 示例代码


```
public void stopFsmService() {
	sensoroSense = SensoroSense.getIntance(this, APP_ID, APP_KEY);
	Intent intent = new Intent();
	intent.setClass(this, ExampleFsmService.class);
	sensoroSense.stopService(intent);
}
```

# Step 4:实现回调函数

FsmService 中的回调函数分为三层，完成业务并不需要实现三个层次所有的方法，开发者可根据自身的业务需求，选择合适的抽象层次，只在这个层次的回调函数中实现自己的业务逻辑即可。


> 交互层回调方法：

“(每天最多允许一次)在用户进入 Beacon 1281 的信号覆盖区域时，给用户发出提示，内容为‘欢迎光临 Costa 望京店’，当用户划开提示时，进入 APP 并打开 Costa 望京店的最新商品推介页’”

这段 Use Story 描述的是一个典型的交互定义(以及交互所需的参数)，系统后台已经按照这个逻辑实现了相关的设置功能，只需在后台设置好，在发生预定义的交互时(用户拿着手机进入区域，而且满足限制条件) APP 就会获得回调(包括所需的参数)。若在这个层次实现业务， APP 只需关心交互的实现细节，其他的事情 SDK 和系统后台已经做完，开发的工作量可以做到最小。

```
onAction(Action action)
```

> 逻辑层回调方法:

“(每一次)在用户进入 beacon 1281 的信号覆盖区域时，立即告知 APP 发生‘进入事件’(并带上在后台为 beacon 1281 预定义的信息)，由 APP 自行决定(是否以及如何)进行交互”

这是逻辑层的典型 use story。当交互层提供的功能没有必要时(比如，每次进入都会触发的简单逻辑)，可以进入逻辑层。在这个层次，APP 可依据在后台给 beacon 配置好的预定义信息进行处理。形成对照的是：在交互层，APP 只需要管‘发生了交互时要怎么办’；而在逻辑层，APP 除了这件事之外， 还需要管‘确定是否发生了交互，以及获取交互的内容’。如，APP 可能需要自己处理：该不该发提示、提示的内容是什么。

```
onEnterSpot(Spot spot, Zone zone)
onLeaveSpot(Spot spot, Zone zone)
onStaySpot(Spot spot, Zone zone, long seconds)
onEnterZone(Zone zone, Spot spot)
onLeaveZone(Zone zone, Spot spot)
onStayZone(Zone zone, Spot spot, long seconds)
```

> 物理层回调方法:

“(每一次)在用户进入 beacon 1281 的信号覆盖区域时，立即告知 APP 发生‘进入事件’(并带上 beacon 的硬件 id)”

物理层，顾名思义，就是对 beacon 硬件层事件的简单包装，若在物理层实现业务，不需要依赖服务端的配置，当有 beacon 出现或消失时，APP 就会获得回调。但 APP 需要做比逻辑层做更多的事情。除非是实现简单的概念性 DEMO 或者信号检测工具，一般情况下，不建议使用。

```
onNew(Beacon)
onGone(Beacon)
```


经过如上的 4 步,开发者已经能够使用该 SDK 了,如果需要和业务逻辑进行结合,参见 Step 5

# Step 5：配置 Beacon

有两种方法可以配置 Beacon

> 使用后台配置系统配置 Beacon 的拓展参数

> 使用 ITAPP 为 Beacon 配置基本参数

注:

1. 使用 SDK 之前要配置您的 Beacon。如果开发者没有配置，回调函数中获取到的数据就没有自行配置的内容，也就是说不能够完成相关的业务逻辑。

2. 如果开发者不配置 Beacon，将只触发物理层回调，不会触发逻辑层和交互层回调。



如果开发者想进一步了解如何使用SDK,请参见 Android SDK doc.md
