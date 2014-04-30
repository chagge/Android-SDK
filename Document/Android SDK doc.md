APP 与 SDK 交互的对外交互接口
==================================
> 版本号 alpha


# 版本要求
目前该sdk要求Android终端支持蓝牙4.0(低功耗蓝牙),并且Android系统版本不低于Android4.3(后续版本会支持低版本的android系统),所以建议您在开发APP时加入蓝牙版本和和系统版本的校验,校验的代码参见Example代码.
# 使用简介
在 Android 平台， SDK 和 APP 之间的交互通过继承FsmService类来实现。在回调函数中完成需要的业务操作。

# 生命周期接口
这里假设您继承自FsmService的类的名称是SensoroFsmService

## SensoroSense
该类用于启动和关闭sdk功能
> 获取SensoroSense实例的函数


```
public static SensoroSense getIntance(Context context, String appID, String appKey)
```
<table>
	<tr>
		<td>参数</td>
		<td>类型</td>		
		<td>说明</td>	
	<tr/> 
	
	<tr>
		<td>context</td>
		<td>Context</td>		
		<td>继承自Context的类,如Activity或Service</td>	
	<tr/>
	<tr>
		<td>appID</td>
		<td>String</td>		
		<td>由SDK提供商提供的appID,每个APP对应一个appID</td>	
	<tr/>
	<tr>
		<td>appKey</td>
		<td>String</td>		
		<td>备用,目前没有使用,传递null即可</td>	
	<tr/>
</table>

> 代码示例
```
SensoroSense sensoroSense = SensoroSense.getIntance(context, "1", null)
```

## 开始：

```
public void startService(Context context, Intent intent)
```

> 代码示例

```
Intent intent = new Intent();
intent.setClass(this, SensoroFsmService.class);
sensoroSense.startService(this, intent);
```

## 配置

```
public void setConfiguration(Configuration configuration)
```

> Configuration类用于对SDK进行配置,对该配置方法的调用要早于startService才会生效,Configuration配置函数说明如下:


### setLocalLog(boolean localLog)
配置本地调试信息是否开启,该方法主要为了方便开发者进行SDK的调试


> 配置代码如下:

```
Configuration.Builder builder = new Configuration.Builder();
builder.setLocalLog(true);
sensoroSense.setConfiguration(builder.create());
sensoroSense.startService(this, intent);
```

## 结束：

```
void stopService(Intent intent) // 结束服务

```
> 代码示例

```
Intent intent = new Intent();
intent.setClass(this, SensoroFsmService.class);
sensoroSense.stopService(intent);
```

# 层级说明
SDK分为交互层,逻辑层和物理层,开发者可以根据不同的业务需求,选择不同的层次进行开发

## 交互层

在这一层 APP 只关心交互导致的结果，并不关心交互如何发生。比如，交互的结果是获得了积分，而产生积分的交互有可能是进入、离开或停留，任何一种交互最终都会导致“获得积分”的结果，规则和参数可在服务端配置。这个层次的 APP 只关心现在积分被触发了，需要如何处理，并不关心是什么触发了这个结果。交互层的信息需要在web端进行配置,如果不配置交互层的数据,就不会又交互层的回调函数触发



### 回调接口：

```
onAction(Action action) // 回调：发生预定义的交互并获得结果
```

```
Action { // 交互结果
String type, // 交互结果的类型：提示，积分，发券
HashMap param, // 开发者自行配置的信息，交互参数，积分URL，发券URL等
String action, // 交互，如：enter_spot(进入点)，leave_spot(离开点)，stay_spot(点停留)，enter_zone(进入区)，leave_zone(离开区)，stay_zone(区停留)
Spot spot, // 交互发生的点
Zone zone, // 交互发生的区
}
```

> 代码示例

```
public void onAction(Action action) {
	Map<String, String> param = action.getParam();
	String message = null;
	if (param != null) { // 开发者在SDK server中没配置可能为空
		message = param.get(MESSAGE);
	}
	String act = action.getAction();
	if (act.equals(ENTER_SPOT)) {
		if (message != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(STRING_ATION);
			builder.append(ENTER_MESSAGE);
			builder.append(STRING_LINE_FEED);
			// 进入点的消息非空 --> 定义为进店消息
			Intent intent = new Intent();
			intent.putExtra(BROADCAST_NAME,
					builder.toString());
			intent.setAction(SENSORO_ACTION);
			sendBroadcast(intent);
			//这里将数据通过广播发送出去
			//开发者可以使用其他方法自行处理这些参数
		}
	}
}
```

## 逻辑层

在这一层，当事件发生时，SDK会把交互发生的场景信息（类似POI）通知给APP，APP可直接处理。发生的事件包括进入点,离开点,停留点,进入区域,离开区域,停留区域.区域是有多个Beacon组成的逻辑区域.点或者区域信息可在服务器端配置.这个层次APP可以获取到在服务器端自行配置的点或者区域的信息.逻辑层的信息需要使用ITApp或者web页面进行配置,如果没有配置,则不会触发逻辑层的回调函数.

点，逻辑上，一个 beacon 就对应着一个点。

> 点的数据结构


```
Spot: {
String name, // 名字
String type, // beacon 的 type，如，店铺，广告牌，
String address, // 地址：以 path 结构组织
float lat, // 经度
float lon, // 纬度
// -- 扩展，每个 app 可不同
String id, // 开发者自行定义的 id
String[] zids, // 点在这个 APP 里属于什么区,开发者自己定义的区域id的数组
Map params, // 开发者自行配置的信息
}
```

### 查询接口：

```
Spot[] getSpots() // 查询：当前所在的点，有可能在多个点的交叉区
```

### 回调接口：

```
onEnterSpot(Spot spot, Zone zone) // 回调：进入点
onLeaveSpot(Spot spot, Zone zone) // 回调：离开点
onStaySpot(Spot spot, Zone zone, int seconds) // 回调：在点停留，若一直停留，则多次回调，间隔为最小停留时间单位
```

区域，由多个点构成。区是为 APP 高度定制的概念.一点可能属于多个区域
> 区域的数据结构
> 
```
Zone: {
String id, // 开发者自行定义的区的 id,这个id对应spot.zids的数组的一个zid
Map params, // 开发者自行配置的信息
}
```

## 查询接口：
```
Zone[] getZones() // 查询：当前所在的区，有可能在多个区的交叉区
```

## 回调接口：
```
onEnterZone(Zone zone, Spot spot) // 回调：进入区
onLeaveZone(Zone zone, Spot spot) // 回调：离开区
onStayZone(Zone zone, Spot spot, int seconds) // 回调：在区停留，若一直停留，则多次回调，间隔为最小停留时间单位
```

> 代码示例

```
	public void onEnterSpot(Spot spot, Zone zone) {
		// TODO 以下为开发者自行处理,demo中通过广播发送给activity显示
		StringBuilder builder = new StringBuilder();
		builder.append(STRING_ENTER_SPOT);
		builder.append(NAME);
		builder.append(spot.getName());
		builder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // 开发者在SDK server中没配置可能为空
			builder.append(PARAM);
			builder.append(param.toString());
			builder.append(STRING_LINE_FEED);
		}
		Intent intent = new Intent();
		intent.putExtra(BROADCAST_NAME, builder.toString());
		intent.setAction(SENSORO_ACTION);
		sendBroadcast(intent);
		//这里将数据通过广播发送出去
		//开发者可以使用其他方法自行处理这些参数
	}
```

注：onEnterZone(zone1, spot1) 和 onEnterSpot(spot1, zone1) 的区别在于，前者意味着“从 spot1 进入 zone1”，后者意味着“进入 spot1，而且 spot1 从属于 zone1”（zone 也可能为 null，以表达 spot 并不从属于任何的 zone）。若 zone1 包含 3 个 spot ，依次经过各个点，则后者可能会被调用 3 次，而前者只会被调用 1 次。

# 物理层

在这一层,当有新的Beacon出现或者Beacon消失时,SDK会把Beacon的uuid,major和minor通知给APP,APP可直接处理.这个层次APP获取的都是底层的硬件信号.物理层不需要进行任何的配置就可以触发

## 查询接口：

```
Beacon[] getBeacons() // 查询：当前所在物理区域，有可能在多个物理区域的交叉区
```

##回调接口：

```
onNew(Beacon beacon) // 回调：进入物理区域
onGone(Beacon beacon) //回调：离开物理区域
```
> Beacon的数据结构

```
Beacon { 
String uuid, // BLE 无线电信号内包含的唯一性 id
String major,
String minor
}
```

> 代码示例

```
	public void onNew(Beacon beacon) {
		// TODO 以下为开发者自行处理,demo中通过广播发送给activity显示
		StringBuilder builder = new StringBuilder();
		builder.append(STRING_NEW_BEACON);
		builder.append(MAJOR);
		builder.append(beacon.getMajor());
		builder.append(MINOR);
		builder.append(beacon.getMinor());
		builder.append(STRING_LINE_FEED);
		Intent intent = new Intent();
		intent.putExtra(BROADCAST_NAME, builder.toString());
		intent.setAction(SENSORO_ACTION);
		sendBroadcast(intent);
		//这里将数据通过广播发送出去
		//开发者可以使用其他方法自行处理这些参数
	}
```
