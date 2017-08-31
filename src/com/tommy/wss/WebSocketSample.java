package com.tommy.wss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.tommy.wss.bo.Event;
import com.tommy.wss.bo.UserInfo;
import com.tommy.wss.utils.WsUtils;

/**
 * @author TommyDeng <250575979@qq.com>
 * @version 创建时间：2017年7月14日 下午6:07:58
 *
 */
@Component
@ServerEndpoint(value = "/websocket/{userTag}", configurator = SpringConfigurator.class)
public class WebSocketSample {

	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	private static final Map<String, UserInfo> usersMap = Collections.synchronizedMap(new HashMap<>());

	@OnOpen
	public void onOpen(Session session, @PathParam(value = "userTag") String userTag) throws Exception {
		session.getUserProperties().put("userTag", userTag);
		System.out.println("client OnOpen...");
	}

	@OnMessage
	public void onMessage(Session session, String inMsgStr) throws Exception {
		System.out.println("OnMessage <" + inMsgStr + ">");
		String userTag = (String) session.getUserProperties().get("userTag");

		String eventType = WsUtils.getValueFromJsonStr(inMsgStr, "eventType");

		if (Event.LOGIN.equals(eventType)) {
			// 用户登录

			UserInfo userInfo = new UserInfo();
			userInfo.userTag = userTag;
			userInfo.userName = WsUtils.getValueFromJsonStr(inMsgStr, "userName");
			usersMap.put(userTag, userInfo);
			sessions.add(session);

			// 广播用户登录
			for (Session openSession : sessions) {
				WsUtils.sendText(openSession,
						WsUtils.buildEvent(Event.LOGIN, userInfo, new ArrayList<>(usersMap.values())));
			}
		} else if (Event.SENDMSG.equals(eventType)) {
			String message = WsUtils.getValueFromJsonStr(inMsgStr, "message");
			// 广播用户消息
			for (Session openSession : sessions) {
				WsUtils.sendText(openSession, WsUtils.buildEvent(Event.SENDMSG, usersMap.get(userTag), message));
			}
		}

	}

	@OnClose
	public void onClose(Session session, CloseReason reson) throws Exception {
		String userTag = (String) session.getUserProperties().get("userTag");
		// 用户登出
		UserInfo logoutUser = usersMap.get(userTag);
		usersMap.remove(userTag);
		sessions.remove(session);
		// 广播用户登出
		for (Session openSession : sessions) {

			WsUtils.sendText(openSession,
					WsUtils.buildEvent(Event.LOGOUT, logoutUser, new ArrayList<>(usersMap.values())));
		}

		session.close();
		System.out.println("OnClose <" + userTag + ">");
	}

	@OnError
	public void onError(Session session, Throwable e) {
		String userTag = (String) session.getUserProperties().get("userTag");
		System.err.println("OnError <" + userTag + ">");
		// e.printStackTrace();
	}

	public void broadcastPulse() throws IOException {
		for (Session openSession : sessions) {
			WsUtils.sendText(openSession,
					WsUtils.buildEvent(Event.PULSE, null, "----------- Server Pulse -------------------"));
		}
	}
}
