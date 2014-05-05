# Sensoro Beacon 配置教程

===============================

  为 Sensoro Beacon 配置参数分为两步
> 使用 ItApp 为 Sensoro Beacon 配置基本参数

> 下载地址 :

> 使用后台配置系统为 Sensoro Beacon 配置开发者参数

> 后台地址 :

# ItApp 使用方法
===============================

# Step 1：登录 ItApp


![结构](http://image.baidu.com/channel?c=%E7%9C%9F%E4%BA%BA%E7%A7%80%E5%9C%BA&fr=index)


使用 ItApp 之前,首先需要向 Sensoro 申请开发者账户

# Step 2：选择需要配置参数的 Sensoro Beacon

``插图``

登录后ItApp会自动检测周边的 Sensoro Beacon 并以列表形式展示

开发者根据 Major 与 Minor 选择相应 Sensoro Beacon 开始配置参数

```
注: 1. Sensoro Beacon 的 Major 与 Minor 图中红线标注的 Major = 129 Minor = 2 
       Major 与 Minor 是 Sensoro Beacon 的唯一标识符 不可更改
    2. Sensoro Beacon 与 ItApp 的距离
```

# Step 3：配置店面信息
``插图``

选择需要配置的 Sensoro Beacon 后点击开始配置

``插图``

依次填写 所在城市、区域、店面、分店信息

位置为 Sensoro Beacon 所在店内的位置，例如“门口”，“前台”，“卫生间”等等

地图选点点击后进入电子地图，选择您店铺的位置，如图

``插图``

选择店铺位置后，系统会记录经纬度。

# Step 4：确认信号范围

开发者手持 ItApp 在店内走动,以确保 Sensoro Beacon 信号覆盖所需区域

``插图``

# Step 4：拍照记录

为 Sensoro Beacon 设置一张记录图片

``插图``

# Step 5：确认提交

点击确认提交按钮,完成 Sensoro Beacon 的基本信息配置

``插图``
# Step 6：配置硬件参数
在 Sensoro Beacon 主界面选择配置硬件 

``插图``
进入以下界面

``插图``

在本界面可以修改 Sensoro Beacon 的发射功率与发射频率

这两个属性影响 Sensoro Beacon 的耗电量,与检测范围

如果将数值设置比较大,Sensoro Beacon 的耗电量也将随之增减

请开发这谨慎选择


# 后台配置系统使用方法
后台配置系统地址:

# Step 1：登录
使用 Sensoro 提供的开发者账户登录配置系统

``插图``

开发者需使用与 ItApp 同样的账户才能 查询到 ItApp 配置过的 Sensoro Beacon

# Step 2：选择所需配置的 Sensoro Beacon

登陆管理服务器后,服务器会将您配置过的 Sensoro Beacon 全部显示出来 
根据配置的数据选择所需 Sensoro Beacon 配置开发者参数

``插图``

我们选择 Major = 4 Minor = 13 这个 Beacon 来配置开发者参数

# Step 3：配置开发者参数

``插图``

> 蓝色区域

蓝色区域参数来源于 ItApp 的设置,在此无法修改,这些参数在回调方法时全部存储在 Spot 对象中


> 橙色区域

橙色区域中设置的参数为 Action 层参数 Remote 要勾选 

进店,停留,离开的 Action 内容分别为"enter_spot","stay_spot","leave_spot" 

逻辑参数为键值对,可以随意存储一些开发者需要的数据 例如 {"地址":"望京SOHO"}

> 红色区域

红色区域中设置的参数为逻辑层参数,同样是开发者自定义的键值对,数据存储在 Spot 对象中.





