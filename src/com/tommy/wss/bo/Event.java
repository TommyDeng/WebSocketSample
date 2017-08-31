package com.tommy.wss.bo;

import com.tommy.wss.utils.WsUtils;

/**
 * @author TommyDeng <250575979@qq.com>
 * @version 创建时间：2017年8月28日 下午3:27:36
 *
 */

public class Event {
	public static String LOGIN = "LOGIN";
	public static String LOGOUT = "LOGOUT";
	public static String SENDMSG = "SENDMSG";
	public static String PULSE = "PULSE";
	
	String eventType;
	UserInfo eventUser;
	Object eventData;
	String eventTime = WsUtils.getCurrentTimeStr();

	/**
	 * @param eventType
	 * @param eventData
	 */
	public Event(String eventType, UserInfo eventUser, Object eventData) {
		super();
		this.eventType = eventType;
		this.eventUser = eventUser;
		this.eventData = eventData;
	}

}