package com.tommy.wss.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tommy.wss.bo.Event;
import com.tommy.wss.bo.UserInfo;

/**
 * @author TommyDeng <250575979@qq.com>
 * @version 创建时间：2017年8月28日 下午5:08:11
 *
 */

public class WsUtils {
	public static String getValueFromJsonStr(String jsonStr, String key) {
		if (StringUtils.isEmpty(jsonStr) || StringUtils.isEmpty(key)) {
			return null;
		}
		JsonElement element = new JsonParser().parse(jsonStr).getAsJsonObject().get(key);
		return element == null ? null : element.getAsString();
	}

	public static String buildEvent(String eventType, UserInfo eventUser, Object eventData) {
		return new Gson().toJson(new Event(eventType, eventUser, eventData));
	}

	public static String getCurrentTimeStr() {
		return new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime());
	}

	public static void sendText(Session session, String text) throws IOException {

		System.out.println(session.getUserProperties().get("userTag") + " >> ");
		System.out.println(text);

		if (session.isOpen()) {
			session.getBasicRemote().sendText(text);
		} else {
			System.out.println("sendText failed by session closed...");
		}
	}

}
