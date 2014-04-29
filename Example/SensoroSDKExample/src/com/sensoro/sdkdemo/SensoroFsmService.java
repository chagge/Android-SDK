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
 * @description APP�������Զ�����,�̳�FsmService.��FsmService���ֻص�������,����ͬ�Ĳ�ε�beacon�����¼�.
 * @date 2014-04-21
 * @author Sensoro
 */

public class SensoroFsmService extends FsmService {
	public static final String TAG = "SensoroFsmService";
	public static final String ENTER_SPOT = "enter_spot"; // SDK
															// �ص�action��������.���SPOT����.

	public static final String TYPE = "type"; // �Զ���SDK server spot����param��key
	public static final String FIXEDCORNER = "fixedcorner"; // �Զ���SDK Server
															// spot����param��,keyΪtype��ֵ
	public static final String MESSAGE = "message"; // �Զ���SDK Server
													// action����param��key

	public static final String SENSORO_ACTION = "android.intent.action.SENSORO_ACTION"; // �Զ���㲥action����
	public static final String BROADCAST_NAME = "BROADCAST_NAME"; // �Զ���㲥��������
	public static final String ENTER_MESSAGE = "ENTER_MESSAGE"; // �Զ���㲥���ݵĽ�����Ϣ
	public static final String ENTER_FIXCORNER = "ENTER_FIXCORNER"; // �Զ���㲥���ݵĽ���ҡһҡ����
	public static final String LEAVE_FIXCORNER = "LEAVE_FIXCORNER"; // �Զ���㲥���ݵ��뿪ҡһҡ����

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
	public static final String ZID = "  zid=";
	public static final String PARAM = "  param=";

	/**
	 * Description: ������������,��sdk��⵽ĳ��Beaconʱ�ص��÷���.(Ӳ����ص�����)
	 *          �÷�����������,����Ӳ����Ļص�����.���е�Beacon���������״β���Beaconʱ���úõ�
	 *          �޷�����.��onGone���Beacon֮ǰ�����ٴλص�
	 * 
	 * @param Beacon
	 *            beacon {@link Spot}
	 * @return null
	 * 
	 *    		
	 * 
	 * */
	@Override
	public void onNew(Beacon beacon) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
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
	 * Description: �뿪��������,��sdk������û���ٽ��յ�ĳ����е�Beacon�ź�ʱ,�ص��˷���.(Ӳ����ص�����)
	 *          �÷�����������,����Ӳ����Ļص�����.���е�Beacon���������״β���Beaconʱ���úõ�
	 *          �޷�����.��onGone���Beacon֮ǰ�����ٴλص�
	 * @param Beacon
	 *            beacon {@link Spot}
	 * @return null
	 * 
	 * */
	@Override
	public void onGone(Beacon beacon) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
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
	 * Description: ����һ��Ԥ����ĵ�ʱ�ص��÷���.(�߼���ص�����)
	 * 
	 * @param Spot 
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * */
	@Override
	public void onEnterSpot(Spot spot, Zone zone) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_ENTER_SPOT);
		parameterStringBuilder.append(NAME);
		parameterStringBuilder.append(spot.getName());
		parameterStringBuilder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // ��������SDK server��û���ÿ���Ϊ��
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
	 * Description: �뿪һ��Ԥ����ĵ�ʱ�ص��÷���.(�߼���ص�����)
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * */
	@Override
	public void onLeaveSpot(Spot spot, Zone zone) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_LEAVE_SPOT);
		parameterStringBuilder.append(NAME);
		parameterStringBuilder.append(spot.getName());
		parameterStringBuilder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // ��������SDK server��û���ÿ���Ϊ��
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
	 * Description: ��һ��Ԥ����ͣ��ʱ�ص��÷���.(�߼���ص�����)
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * */
	@Override
	public void onStaySpot(Spot spot, Zone zone, long seconds) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_STAY_SPOT);
		parameterStringBuilder.append(NAME);
		parameterStringBuilder.append(spot.getName());
		parameterStringBuilder.append(STRING_LINE_FEED);
		Map<String, String> param = spot.getParam();
		if (param != null) { // ��������SDK server��û���ÿ���Ϊ��
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
	 * Description: ����һ��Ԥ���������ʱ�ص��÷���.(�߼���ص�����)
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * */
	@Override
	public void onEnterZone(Zone zone, Spot spot) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_ENTER_ZONE);
		parameterStringBuilder.append(ZID);
		parameterStringBuilder.append(zone.getZid());
		parameterStringBuilder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // ��������SDK server��û���ÿ���Ϊ��
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
	 * Description: �뿪һ��Ԥ��������ʱ�ص�����(�߼���ص�����)
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * */
	@Override
	public void onLeaveZone(Zone zone, Spot spot) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_LEAVE_ZONE);
		parameterStringBuilder.append(ZID);
		parameterStringBuilder.append(zone.getZid());
		parameterStringBuilder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // ��������SDK server��û���ÿ���Ϊ��
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
	 * Description: ��һ��Ԥ������ͣ��ʱ�ص��÷���.(�߼���ص�����)
	 * 
	 * @param Spot
	 *            spot {@link Spot}
	 * @param Zone
	 *            zone {@link Zone}
	 * @return null
	 * 
	 * */
	@Override
	public void onStayZone(Zone zone, Spot spot, long seconds) {
		// TODO ����Ϊ���������д���,demo��ͨ���㲥���͸�activity��ʾ
		StringBuilder parameterStringBuilder = new StringBuilder();
		parameterStringBuilder.append(STRING_STAY_ZONE);
		parameterStringBuilder.append(ZID);
		parameterStringBuilder.append(zone.getZid());
		parameterStringBuilder.append(STRING_LINE_FEED);

		Map<String, String> param = spot.getParam();
		if (param != null) { // ��������SDK server��û���ÿ���Ϊ��
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
	 * Description: ����Ԥ����Ľ�������ý�� (������ص�����)
	 * 
	 * @param Action
	 *            {@link Action}
	 * @return null
	 * 
	 * @example demo��Action��param����,�Զ��������keyΪmessage,�ǿձ�ʾ�н�����Ϣ;
	 *          action��ֵΪenter_spot,��ʾ����ĳ����.����������������,�򹹳��û�����ҵ��.
	 * 
	 */
	@Override
	public void onAction(Action action) {
		// TODO onAction������������
		Map<String, String> param = action.getParam();
		// param��һ����չ����,���Դ洢�ܶ����õ�����,�������������,key��type,value��fixedcorner
		// ֤�����beacon��һ���Խ������,���������ø���,���糵վ,��·���ֵȵ�����չ��Ϣ
		String message = null;
		if (param != null) { // ��������SDK server��û���ÿ���Ϊ��
			message = param.get(MESSAGE);
		}
		String act = action.getAction();
		if (act.equals(ENTER_SPOT)) {
			// ����ĳ����
			if (message != null) {
				StringBuilder parameterStringBuilder = new StringBuilder();
				parameterStringBuilder.append(STRING_ATION);
				parameterStringBuilder.append(ENTER_MESSAGE);
				parameterStringBuilder.append(STRING_LINE_FEED);
				// ��������Ϣ�ǿ� --> ����Ϊ������Ϣ
				Intent intent = new Intent();
				intent.putExtra(BROADCAST_NAME,
						parameterStringBuilder.toString());
				intent.setAction(SENSORO_ACTION);
				sendBroadcast(intent);
			}
		}
	}
}
