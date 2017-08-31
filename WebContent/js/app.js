var LOGIN = "LOGIN";
var LOGOUT = "LOGOUT";
var SENDMSG = "SENDMSG";
var PULSE = "PULSE";
function user_login() {

	// 登录按钮loading
	$('#loginBtn').addClass('loading');

	// 隐藏登录页面
	$('#loginDiv').hide();
	// $('#loginDiv').transition('slide right');

	// 弹出聊天主界面
	$('#mainDiv').transition('slide up');

	// 发送登录请求
	var event = new Event(LOGIN);
	Event.prototype.userName;

	event.userName = $('#loginNameInput').val();
	sendEvent(event);

}

function send_message() {
	var event = new Event(SENDMSG);
	Event.prototype.message;

	event.message = $('#messageInput').val();
	sendEvent(event);
}

function processMessage(message) {
	if (message.eventType == LOGIN || message.eventType == LOGOUT) {
		reloadUserList(message);
		appendTips(message);
	} else if (message.eventType == SENDMSG) {
		appendChat(message);
	} else if (message.eventType == PULSE) {
		$('#wsStatus').transition('pulse');
	}
}

// 刷新用户列表
function reloadUserList(message) {
	console.info('reloading user list');
	var userListHtml = '<h3 class="ui dividing header">用户列表</h3>';
	$(message.eventData)
			.each(
					function(index, element) {
						userListHtml += '<div class="item"><img class="ui avatar image" src="img/male.png"><div class="content"><div class="header">'
								+ element.userName + '</div></div></div>'
					});
	$('#userListDiv').empty();
	$('#userListDiv').html(userListHtml);
}

// 打印消息
function appendChat(message) {
	var commentHtml = '<div class="comment">';
	commentHtml += '<a class="avatar"><img src="img/male.png"></a>';
	commentHtml += '<div class="content">';
	commentHtml += '<a class="author">' + message.eventUser.userName + '</a>';
	commentHtml += '<div class="metadata">';
	commentHtml += '<span class="date">' + message.eventTime + '</span>';
	commentHtml += '</div>';
	commentHtml += '<div class="text">' + message.eventData + '</div>';
	commentHtml += '<div class="actions">';
	commentHtml += '<a class="reply">Reply</a>';
	commentHtml += '</div>';
	commentHtml += '</div>';
	commentHtml += '</div>';

	$('#chatOutputDiv').append(commentHtml);
	$('#messageInput').val('');

	var divHeight = $("#chatOutputDiv")[0].scrollHeight;
	$("#chatOutputDiv").scrollTop(divHeight);
}

// 打印提示
function appendTips(message) {
	var tipColor = 'warning';
	var tips = '';
	if (message.eventType == LOGIN) {
		tipColor = 'green';
		tips = '欢迎  <b>' + message.eventUser.userName + '</b> 进入.';
	} else if (message.eventType == LOGOUT) {
		tipColor = 'grey';
		tips = '<b>' + message.eventUser.userName + '</b> 已离开.';
	}

	var commentHtml = '<div class="comment">';
	commentHtml += '<div class="message ui compact ' + tipColor + '">';
	commentHtml += '<p>' + tips + '</p>';
	commentHtml += '</div>';
	$('#chatOutputDiv').append(commentHtml);
}