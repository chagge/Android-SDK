APP 与 SDK 交互的对外交互接口
==================================
# 生命周期接口

## 开始：
```

void startService(String appid, String appkey, Map options) // 开始服务

options { // 可选启动参数
bool local_log,
bool remote_log
}
```

## 结束：

```

void stopService() // 结束服务

```

# 交互层

在这一层 APP 只关心交互导致的结果，并不关心交互如何发生。比如，交互的结果是获得了积分，而产生积分的交互有可能是进入、离开或停留，任何一种交互最终都会导致“获得积分”的结果，规则和参数可在服务端配置。这个层次的 APP 只关心现在积分被触发了，需要如何处理，并不关心是什么触发了这个结果。

```

Action { // 交互结果
String type, // 交互结果的类型：提示，积分，发券
Map params, // 开发者自行配置的信息，交互参数，积分URL，发券URL等
String action, // 交互，如：enter_spot(进入点)，leave_spot(离开点)，stay_spot(点停留)，enter_zone(进入区)，leave_zone(离开区)，stay_zone(区停留)
Spot spot, // 交互发生的点
Zone zone, // 交互发生的区
}
```
## 回调接口：
```
onAction(Action action) // 回调：发生预定义的交互并获得结果
```

# 逻辑层

在这一层，当事件发生时，SDK 会把交互发生的场景信息（类似 POI）通知给 APP，APP 可直接处理。 

点，逻辑上，一个 beacon 就对应着一个点。
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

## 查询接口：
```
Spot[] getSpots() // 查询：当前所在的点，有可能在多个点的交叉区
```

## 回调接口：

```
onEnterSpot(Spot spot, Zone zone) // 回调：进入点
onLeaveSpot(Spot spot, Zone zone) // 回调：离开点
onStaySpot(Spot spot, Zone zone, int seconds) // 回调：在点停留，若一直停留，则多次回调，间隔为最小停留时间单位
```

区，由多个点构成。区是为 APP 高度定制的概念.
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
注：onEnterZone(zone1, spot1) 和 onEnterSpot(spot1, zone1) 的区别在于，前者意味着“从 spot1 进入 zone1”，后者意味着“进入 spot1，而且 spot1 从属于 zone1”（zone 也可能为 null，以表达 spot 并不从属于任何的 zone）。若 zone1 包含 3 个 spot ，依次经过各个点，则后者可能会被调用 3 次，而前者只会被调用 1 次。

# 物理层

在这一层，基本就是 iBeacon 的接口包装。

## 查询接口：

```
Beacon[] getBeacons() // 查询：当前所在物理区域，有可能在多个物理区域的交叉区
```

##回调接口：

```
onNew(Beacon beacon) // 回调：进入物理区域
onGone(Beacon beacon) //回调：离开物理区域
```

```
Beacon { 
String uuid, // BLE 无线电信号内包含的唯一性 id
String major,
String minor
}
```
