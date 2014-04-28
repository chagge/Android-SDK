SDK 介绍
==================================
> 版本号 alpha


# 功能
该SDK提供了主要三个功能.第一个是基于Beacon信号产生的交互结果;第二个是进入,离开,停留某个Beacon的区域或由Beacon组成的区域;第三个是Beacon的出现和消失.

# 交互层

在这一层,APP只关心交互产生的结果，并不关心交互如何发生。比如，交互的结果是获得了积分，而产生积分的交互有可能是进入、离开或停留，任何一种交互最终都会导致“获得积分”的结果，规则和参数可在服务端配置。这个层次的APP只关心现在积分被触发了，需要如何处理，并不关心是什么触发了这个结果。

# 逻辑层

在这一层，当事件发生时，SDK会把交互发生的场景信息（类似POI）通知给APP，APP可直接处理。发生的事件包括进入点,离开点,停留点,进入区域,离开区域,停留区域.区域是有多个Beacon组成的逻辑区域.点或者区域信息可在服务器端配置.这个层次APP可以获取到在服务器端自行配置的点或者区域的信息.

# 物理层
在这一层,当有新的Beacon出现或者Beacon消失时,SDK会把Beacon的uuid,major和minor通知给APP,APP可直接处理.这个层次APP获取的都是底层的硬件信息号.

# Github目录结构说明

## Document
SDK开发文档

## Example
Android SDK开发示例，提供该第三方开发者使用

## CHANGELOG.md
SDK更新说明

## README.md
README

