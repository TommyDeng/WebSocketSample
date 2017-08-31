package com.tommy.wss.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tommy.wss.WebSocketSample;

/**
 * @author TommyDeng <250575979@qq.com>
 * @version 创建时间：2017年8月30日 下午3:08:45
 *
 */

@Service
public class WsService {
	@Autowired
	WebSocketSample webSocketSample;

	@PostConstruct
	public void pulseDetect() throws IOException {
		Thread job = new Thread(new WsServiceJob());
		job.start();
	}

	class WsServiceJob implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					webSocketSample.broadcastPulse();
					Thread.sleep(30000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
