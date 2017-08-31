function newGuid() {
	return guids4() + guids4() + '-' + guids4() + '-' + guids4() + '-'
			+ guids4() + '-' + guids4() + guids4() + guids4();
}
function guids4() {
	return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
}

function isHtml5Support() {
	if (typeof (Worker) == 'undefined') {
		return false;
	}
	return true;
}

function wsConnect(wsUrl) {
	if (wsocket != null
			&& (wsocket.readyState == wsocket.OPEN || wsocket.readyState == wsocket.CONNECTING)) {
		// wsocket.close();
		return;
	}
	
	wsocket = new WebSocket(wsUrl);
	wsocket.onopen = function(evt) {

		console.info('WS onopen...');
		readyStateDisplay();
	};
	wsocket.onclose = function(evt) {

		console.info('WS onclose...');
		console.info(evt.reason);
		readyStateDisplay();
	};
	wsocket.onmessage = function(evt) {
		// 处理业务
		processMessage(JSON.parse(evt.data));

		console.info('WS onmessage...');
		console.info(evt.data);
		readyStateDisplay();
	};

	wsocket.onerror = function(evt) {

		console.error('WS onerror... ');
		console.error(evt.message);
		readyStateDisplay();
	};
}

function readyStateDisplay() {
	// CONNECTING 0 The connection is not yet open.
	// OPEN 1 The connection is open and ready to communicate.
	// CLOSING 2 The connection is in the process of closing.
	// CLOSED 3 The connection is closed or couldn't be opened.
	if (wsocket.readyState == wsocket.CONNECTING) {
		$('#wsStatus').addClass('blue');
		$('#wsStatus').html('CONNECTING');
	} else if (wsocket.readyState == wsocket.OPEN) {
		$('#wsStatus').addClass('green');
		$('#wsStatus').html('OPEN');
	} else if (wsocket.readyState == wsocket.CLOSING) {
		$('#wsStatus').addClass('red');
		$('#wsStatus').html('CLOSING');
	} else if (wsocket.readyState == wsocket.CLOSED) {
		$('#wsStatus').addClass('grey');
		$('#wsStatus').html('CLOSED');
	}
}

function Event(eventType) {
	this.eventType = eventType;
}

function sendEvent(event) {
	if (wsocket.readyState == wsocket.OPEN) {
		// 转换为json字符串
		var outJsonStr = JSON.stringify(event);
		console.info('client send msg==>:' + outJsonStr);
		wsocket.send(outJsonStr);

	} else {
		console.info('WebSocket Not Valiad...');
	}
}