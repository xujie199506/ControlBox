//全局ajax控制，用于session超时 无权限时 提示
$.ajaxSetup({
	cache : false, // close AJAX cache
	contentType : "application/x-www-form-urlencoded;charset=utf-8",
	complete : function(XHR, textStatus) {
		var sessionstatus = XHR.getResponseHeader("sessionstatus");
		var loginPath = "login.jsp";//XHR.getResponseHeader("loginPath");
		if (911 == XHR.status && "timeout" == sessionstatus) {
			// 此处使用了开源的消息确认框
			alert('您的会话已经过期，请重新登陆后继续操作！');
			window.location.replace(loginPath);
			return;
		}
	}
});
