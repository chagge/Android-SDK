package com.sensoro.sdkdemo;

import com.sensoro.beacon.core.SensoroSense;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;

public class SensoroSDKDemoApplication extends Application {
	private static final String APP_ID = "3";
	private static final String APP_KEY = null;
	// 蓝牙适配器
	private BluetoothAdapter bluetoothAdapter = null;
	// 是否支持BLE标志位
	public boolean isSupportBLE = false;
	// 是否开启蓝牙
	public boolean isBTOpen = false;
	// 整体的事件触发类
	public SensoroSense sensoroSense = null;

	@Override
	public void onCreate() {
		initBluetooth();
		startFsmService();
		super.onCreate();
	}

	private void startFsmService() {
		/**
		 * 只有设备支持BLE并且蓝牙开启,才能保证SDK正常使用.
		 * 用户可以自行处理.
		 */
		if (isSupportBLE && isBTOpen) {
			/**
			 * 获取SensoroSense的实例. 第一个参数为context;
			 * 第二个参数是APP_ID,不同APP需要向Sensoro咨询申请不同的APP_ID;
			 * 第三个参数是APP_KEY,不同APP需要不同的APP_KEY.该参数暂未使用.
			 * eg:	demo中设置测试APP_ID为1,APP_KEY为null.
			 * 
			 */
			sensoroSense = SensoroSense.getIntance(this, APP_ID, APP_KEY);
			/**
			 * 启动用户自定义Service,获取Beacon触发的回调函数. 第一个参数为Context;
			 * 第二个参数intent表示启动用户自定义service的intent; 
			 * 第三个参数表示服务进程关闭以后是否允许重新启动
			 * 第四个参数为Map构成的Log可选项,默认log参数均为true,具体描述如下：
			 * 	options { 
			 * 		// 可选启动参数 
			 * 		bool local_log,表示本地log存储
			 * 		bool remote_log,表示向SDK后台发送log信息
			 * 	}
			 * eg:	demo中设置允许重新启动,log默认均开启.
			 * 
			 */
			Intent intent = new Intent();
			intent.setClass(this, SensoroFsmService.class);
			sensoroSense.startService(this, intent, false, null);
			
			/**
			 * 绑定UID.
			 * 该操作为可选操作,只有当APP需要与用户唯一绑定的时候使用.
			 * eg:	摇一摇可以给用户产生一张优惠券,但是这张优惠券只能由摇到的该用户使用,其他用户查询不到且不能使用.
			 * 		因此,需要将service绑定到特定用户上,通过该函数进行绑定.
			 * 		该函数可以在任何时候进行调用,bind不同的用户.
			 */
//			sensoroSense.bindUid("4");
		} else {
			//TODO 用户自行处理,demo中禁止启动APP
			System.exit(0);
		}
	}

	/**
	 * @description 初始化BLE,判断设备是否支持BLE和蓝牙是否打开 建议第三方开发者必须进行判断,否则可能导致SDK不能正常使用.
	 * @date 2014-4-21
	 * @author Sensoro
	 */
	private void initBluetooth() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter != null) {
			if (getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_BLUETOOTH_LE)) {
				isSupportBLE = true;
			} else {
				isSupportBLE = false;
			}
			if (bluetoothAdapter.isEnabled()) {
				isBTOpen = true;
			} else {
				isBTOpen = false;
			}
		}
	}
}
