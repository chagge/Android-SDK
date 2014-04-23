package com.sensoro.sdkdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.secsoro.sdkdemo.R;

public class SensoroSDKDemoActivity extends Activity implements OnClickListener {
	public static final String SENSORO_ACTION = "android.intent.action.SENSORO_ACTION"; // 自定义广播action名称
	public static final String STRING_LINE_FEED = "\n";
	public static final String STRING_ENTER_SPOT = "enter_spot:";
	public static final String STRING_ATION = "ation:";

	public static final String MAJOR = "  major=";
	public static final String MINOR = "  minor=";
	public static final String ZONE = "  zone=";
	public static final String SPOT = "  spot=";
	public static final String SECONDS = "  seconds=";

	public static final String ACTIVE_EVENT = "fixedcorner";
	public static final String TYPE = "type";
	/** sdk给我们返回的消息都打印在这里 */
	private EditText outputEt;
	/** 清空打印信息,信息有时候太多了就清除掉 */
	private Button clearBtn;
	/** 打印信息的字符串,当sdk回调方法时,将回调的信息添加进去,打印在EditText上 */
	private StringBuilder logStr = new StringBuilder();

	private SensorManager mSensorManager = null;
	private boolean isInFixCorner = false; // 是否进入淘金角

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdk_demo);
		init();
	}

	/** 初始化界面及Receiver等等 */
	private void init() {
		outputEt = (EditText) findViewById(R.id.output_et);
		outputEt.setKeyListener(null);
		outputEt.setMovementMethod(ScrollingMovementMethod.getInstance());
		clearBtn = (Button) findViewById(R.id.clear_edittext_button);
		clearBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				outputEt.setText("");
				logStr = new StringBuilder();
			}
		});
		logStr.append(outputEt.getText().toString());
		registerMyReceiver();// 注册MyReceiver
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	/** 注册MyReceiver */
	private void registerMyReceiver() {
		IntentFilter filter = new IntentFilter(SensoroFsmService.SENSORO_ACTION);
		registerReceiver(sensoroBroadcastReceiver, filter);// 注册MyReceiver
	}

	@Override
	public void onClick(View v) {
	}

	private BroadcastReceiver sensoroBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String stringExtra = intent
					.getStringExtra(SensoroFsmService.BROADCAST_NAME);
			if (stringExtra.equals(SensoroFsmService.ENTER_FIXCORNER)) {
				isInFixCorner = true;
				logStr.append(context.getString(R.string.enter_fixedcorner)
						+ STRING_LINE_FEED);
			} else if (stringExtra.equals(SensoroFsmService.LEAVE_FIXCORNER)) {
				isInFixCorner = false;
				logStr.append(context.getString(R.string.leave_fixedcorner)
						+ STRING_LINE_FEED);
			} else if (stringExtra.equals(SensoroFsmService.ENTER_MESSAGE)) {
				String message = intent
						.getStringExtra(SensoroFsmService.ENTER_MESSAGE);
				logStr.append(message + STRING_LINE_FEED);
			}

			outputEt.setText(logStr);
			outputEt.setSelection(outputEt.getText().length(), outputEt
					.getText().length());
		}
	};

	/**
	 * 重力感应监听
	 */
	private SensorEventListener mSensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0]; // x轴方向的重力加速度，向右为正
			float y = values[1]; // y轴方向的重力加速度，向前为正
			float z = values[2]; // z轴方向的重力加速度，向上为正
			// Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" +
			// z);
			// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
			int medumValue = 14;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了,小米更差改成14
			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
					|| Math.abs(z) > medumValue) {
				// 如果达到摇一摇的标准,判断是否正在进行获取优惠券操作
				if (isInFixCorner) {
					logStr.append(getString(R.string.shark_it_off)
							+ STRING_LINE_FEED);
					outputEt.setText(logStr);
					outputEt.setSelection(outputEt.getText().length(), outputEt
							.getText().length());
					// TODO 摇一摇后获取这个Spot开发者自定义的Params参数
					// 可以随意进行一些操作,例如打开Url,或者开启Activity
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	@Override
	protected void onResume() {
		if (mSensorManager != null) {// 注册监听器
			mSensorManager.registerListener(mSensorEventListener,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mSensorManager != null) {// 取消监听器
			mSensorManager.unregisterListener(mSensorEventListener);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(sensoroBroadcastReceiver);// 取消
	}
}
