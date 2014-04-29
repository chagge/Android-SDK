package com.sensoro.sdkdemo;

import java.util.Map;

import android.content.Intent;
import android.util.Log;

import com.sensoro.beacon.core.Action;
import com.sensoro.beacon.core.Beacon;
import com.sensoro.beacon.core.FsmService;
import com.sensoro.beacon.core.Spot;
import com.sensoro.beacon.core.Zone;

/**
 * @description APP开发者自定义类,继承FsmService.在FsmService各种回调方法中,处理不同的层次的beacon触发事件.
 * @date 2014-04-21
 * @author Sensoro
 */

public class SensoroFsmService extends FsmService {
	private static final String TAG = "SensoroFsmService";
	public static final String ENTER_SPOT = "enter_spot"; // SDK
															// 回调action交互动作.详见SPOT定义.

	public static final String TYPE = "type"; // 自定义SDK server spot参数param的key
	public static final String FIXEDCORNER = "fixedcorner"; // 自定义SDK Server
															// spot参数param中,key为type的值
	public static final String MESSAGE = "message"; // 自定义SDK Server
													// action参数param的key

	public static final String SENSORO_ACTION = "android.intent.action.SENSORO_ACTION"; // 自定义广播action名称
	public static final String BROADCAST_NAME = "BROADCAST_NAME"; // 自定义广播类型名称
	public static final String ENTER_MESSAGE = "ENTER_MESSAGE"; // 自定义广播传递的进店消息
	public static final String ENTER_FIXCORNER = "ENTER_FIXCORNER"; // 自定义广播传递的进入摇一摇区域
	public static final String LEAVE_FIXCORNER = "LEAVE_FIXCORNER"; // 自定义广播传递的离开摇一摇区域

	public static final String STRING_LINE_FEED = "\n";
	public static final String STRING_NEW_BEACON = "new_beacon:";
	public static final String STRING_GONE_BEACON = "gone_beacon:";
	public static final String STRING_STAY_SPOT = "stay_spot:";
	public static final String STRING_STAY_ZONE = "stay_zone:";
	public static final String STRING_ENTER_SPOT = "enter_spot:";
	public static final String STRING_LEAVE_SPOT = "leave_spot:";
	public static final String STRING_ENTER_ZONE = "enter_zone:";
	public static final String STRING_LEAVE_ZONE = "leave_zone:";
	public static final String STRING_ATION = "ation:";

	public static final String MAJOR = "  major=";
	public static final String MINOR = "  minor=";
	public static final String ZONE = "  zone=";
	public static final String SPOT = "  spot=";
	public static final String SECONDS = "  seconds=";
	public static final String NAME = "  name=";
	public static final String PARAM = "  param=";

	/**
	 * Description: 当sdk检测到某个Beacon时回调该方法.
	 * 
	 * @param Beacon
	 *            beacon {@link Spot}
	 * @return null
	 * 
	 * @example 当我们的sdk检测到某个正在工作的beacon时,讲回调此方法返回我们检测到的Beacon对象
	 *          该方法不必联网,属于硬件层的回调方法.其中的Beacon对象是由首次布置Beacon时设置好的
	 *          无法更改.在onGone这个Beacon之前不会再次回调
	 * 
	 * */
	@Override
	public void onNew(Beacon beacon) {
		// TODO 以下为开发者自行处理,demo中通过广播发送给activity显示
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_NEW_BEACON);
		parameterStringBuilder.append(MAJOR);
		parameterStringBuilder.append(beacon.getMajor());
		parameterStringBuilder.append(MINOR);
		parameterStringBuilder.append(beacon.getMinor());
		parameterStringBuilder.append(STRING_LINE_FEED);
		Intent intent = new Intent();
		intent.putExtra(BROADCAST_NAME, parameterStringBuilder.toString());
		intent.setAction(SENSORO_ACTION);
		sendBroadcast(intent);
	}

	/**
	 * Description: 当sdk数秒内没有再接收到某个活动中的Beacon信号时,回调此方法.
	 * 
	 * @param Beacon
	 *            beacon {@link Spot}
	 * @return null
	 * 
	 * @example 当SDK检测到某个Beacon回调onNew方法后,数秒内没有再收到此Beacon的信号 将视为
	 * 
	 * */
	@Override
	public void onGone(Beacon beacon) {
		// TODO 以下为开发者自行处理,demo中通过广播发送给activity显示
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_GONE_BEACON);
		parameterStringBuilder.append(MAJOR);
		parameterStringBuilder.append(beacon.getMajor());
		parameterStringBuilder.append(MINOR);
		parameterStringBuilder.append(beacon.getMinor());
		parameterStringBuilder.append(STRING_LINE_FEED);
		Intent intent = new Intent();
		intent.putExtra(BROADCAST_NAME, parameterStringBuilder.toString());
		intent.setAction(SENSORO_ACTION);
		sendBroadcast(intent);
	}

	/**
	 * Description: 进入一个预定义的点时回调该方法.
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * @example demo中Spot的param属性,自定义参数key为type,type返回值为fixedcorner表示进入摇一摇区域.
	 *          满足上述条件,则构成进入摇一摇区域的业务. demo中配置摇一摇区域为一个spot触发,开发者也可以自定义由一个Zone来触发.
	 * 
	 * */
	@Override
	public void onEnterSpot(Spot spot, Zone zone) {
		// TODO 以下为开发者自行处理,demo中通过广播发送给activity显示
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_ENTER_SPOT);
		parameterStringBuilder.append(NAME);
		parameterStringBuilder.append(spot.getName());
		parameterStringBuilder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // 开发者在SDK server中没配置可能为空
			// String type = param.get(TYPE);
			// if (type != null && type.equals(FIXEDCORNER)) {
			// 进入点参数type为fixcorner --> 定义为进入摇一摇区域
			// parameterStringBuilder.append(FIXEDCORNER);
			parameterStringBuilder.append(PARAM);
			parameterStringBuilder.append(param.toString());
			parameterStringBuilder.append(STRING_LINE_FEED);
		}
		Intent intent = new Intent();
		intent.putExtra(BROADCAST_NAME, parameterStringBuilder.toString());
		intent.setAction(SENSORO_ACTION);
		sendBroadcast(intent);
	}

	/**
	 * Description: 离开一个预定义的点时回调该方法.
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * @example demo中Spot的param属性,自定义参数key为type,type返回值为fixedcorner表示离开摇一摇区域.
	 *          满足上述条件,则构成离开摇一摇区域的业务. demo中配置摇一摇区域为一个spot触发,开发者也可以自定义由一个Zone来触发.
	 * 
	 * */
	@Override
	public void onLeaveSpot(Spot spot, Zone zone) {
		// TODO 以下为开发者自行处理,demo中通过广播发送给activity显示
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_LEAVE_SPOT);
		parameterStringBuilder.append(NAME);
		parameterStringBuilder.append(spot.getName());
		parameterStringBuilder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // 开发者在SDK server中没配置可能为空
			// String type = param.get(TYPE);
			// if (type != null && type.equals(FIXEDCORNER)) {
			// 进入点参数type为fixcorner --> 定义为进入摇一摇区域
			// parameterStringBuilder.append(FIXEDCORNER);
			parameterStringBuilder.append(PARAM);
			parameterStringBuilder.append(param.toString());
			parameterStringBuilder.append(STRING_LINE_FEED);
		}
		Intent intent = new Intent();
		intent.putExtra(BROADCAST_NAME, parameterStringBuilder.toString());
		intent.setAction(SENSORO_ACTION);
		sendBroadcast(intent);
	}
	
	/**
	 * Description: 在一个预定点停留时回调该方法.
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * @example demo中Spot的param属性,自定义参数key为type,type返回值为fixedcorner表示离开摇一摇区域.
	 *          满足上述条件,则构成离开摇一摇区域的业务. demo中配置摇一摇区域为一个spot触发,开发者也可以自定义由一个Zone来触发.
	 * 
	 * */
	@Override
	public void onStaySpot(Spot spot, Zone zone, long seconds) {
		// TODO 以下为开发者自行处理,demo中通过广播发送给activity显示
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_LEAVE_SPOT);
		parameterStringBuilder.append(NAME);
		parameterStringBuilder.append(spot.getName());
		parameterStringBuilder.append(STRING_LINE_FEED);
		Map<String, String> param = spot.getParam();
		if (param != null) { // 开发者在SDK server中没配置可能为空
			// String type = param.get(TYPE);
			// if (type != null && type.equals(FIXEDCORNER)) {
			// 进入点参数type为fixcorner --> 定义为进入摇一摇区域
			// parameterStringBuilder.append(FIXEDCORNER);
			parameterStringBuilder.append(PARAM);
			parameterStringBuilder.append(param.toString());
			parameterStringBuilder.append(STRING_LINE_FEED);
		}
		Intent intent = new Intent();
		intent.putExtra(BROADCAST_NAME, parameterStringBuilder.toString());
		intent.setAction(SENSORO_ACTION);
		sendBroadcast(intent);
	}

	@Override
	public void onEnterZone(Zone zone, Spot spot) {
		Log.v(TAG, "onEnterZone--" + zone.toString());
	}

	@Override
	public void onLeaveZone(Zone zone, Spot spot) {
		Log.v(TAG, "onLeaveZone--" + zone.toString());
	}

	@Override
	public void onStayZone(Zone zone, Spot spot, long seconds) {
		Log.v(TAG, "onStayZone--" + zone.toString());
	}

	/**
	 * Description: 发生预定义的交互并获得结果
	 * 
	 * @param Action
	 *            {@link Action}
	 * @return null
	 * 
	 * @example demo中Action的param属性,自定义参数的key为message,非空表示有进店消息;
	 *          action的值为enter_spot,表示进入某个点.满足上述两个条件,则构成用户进店业务.
	 * 
	 */
	@Override
	public void onAction(Action action) {
		// TODO onAction方法用来控制
		Map<String, String> param = action.getParam();
		// param是一个拓展参数,可以存储很多有用的数据,比如下面的例子,key是type,value是fixedcorner
		// 证明这个beacon是一个淘金角区域,还可以设置更多,例如车站,马路名字等等与拓展信息
		String message = null;
		if (param != null) { // 开发者在SDK server中没配置可能为空
			message = param.get(MESSAGE);
		}
		String act = action.getAction();
		if (act.equals(ENTER_SPOT)) {
			// 进入某个点
			if (message != null) {
				StringBuilder parameterStringBuilder = new StringBuilder();
				parameterStringBuilder.append(STRING_ATION);
				parameterStringBuilder.append(ENTER_MESSAGE);
				parameterStringBuilder.append(STRING_LINE_FEED);
				// 进入点的消息非空 --> 定义为进店消息
				Intent intent = new Intent();
				intent.putExtra(BROADCAST_NAME, parameterStringBuilder.toString());
				intent.setAction(SENSORO_ACTION);
				sendBroadcast(intent);
			}
		}
	}
}
