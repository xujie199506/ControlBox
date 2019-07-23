<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<title>Insert title here</title>
<script type="text/javascript">
	var global_rootPath = "${ctx}";
</script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/jquery/easyui/themes/default/easyui.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/jquery/easyui/themes/icon.css"
	type="text/css"></link>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery/ajaxSetupExtension.js"></script>
</head>
<body>
	

<select id="sel"></select>
<input type="button" value="刷新" onclick="oncli()" />


<input type="button" value="清零" onclick="clear1()" />
<input type="button" value="对比" onclick="check()" />

<input type="button" value="统计" onclick="statistics()" />
<input type="button" value="断开" onclick="kill()" />
<input type="button" value="刷新" onclick="getnum()" />
<input type="text" id="inoutnum"   style="width:611px;height:30px;display: block;"/>

<br>
<input type="text" id="sl"/>
<br>
参数类型:<input type="text" id="cmd"/>
<br>
 参数值：
	 <input type="text" id="ta"/>

</textarea>

<br>
<input type="button" value="发送" onclick="sendData()" />
<hr>
升级:<br>
<input type="text" id="version"  placeholder="这里输入升级版本"/>
<input type="text" id="FeilName"  placeholder="升级文件名"/>
<input type="button" value="加载升级文件" onclick="LoadUpgradeFile()" />
<input type="button" value="升级" onclick="updata()" />
<hr>
状态获取<br>
<select id="zthq">
<option value='00'>信号强度</option>

<option value='01'>网络状态</option>
</select>
<input type="button" value="获取" onclick="getstate()" />
<hr>

</body>

<script type="text/javascript">
	$(oncli());
	function oncli() {

		$.ajax({
			type : "post",
			traditional : true,
			url : "${pageContext.request.contextPath}/test",
			data : 'username=111',
			success : function(data, textStatus) {
				$("#sel").empty();
				var strs = new Array(); //定义一数组 
				strs = data.split(","); //字符分割 
				var a=0;
				for (i = 0; i < strs.length; i++) {
					$("#sel").append(
							"<option value='5'>" + strs[i] + "</option>");
				
				}
				
				$("#sl").val(strs.length);

			},
			error : function() {
			}
		});
		
		var t2 = window.setInterval("getnum()",1000);//使用字符串执行方法 
		
		
	
		 
		
		
		
	}
	
	
	function getnum(){
		 
		$.ajax({
			type : "post",
			traditional : true,
			url : "${pageContext.request.contextPath}/getnum",
			data : 'username=111',
			success : function(data, textStatus) {
				$("#inoutnum").val(data);
				console.log(data);
			},
			error : function() {
			}
		}); 
		
	}
	
	function getstate(){
		var zthq=$("#zthq").find("option:selected").text();
		var checkText=$("#sel").find("option:selected").text();
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/getstate",
			data :  'dz='+checkText+'&zthq='+zthq,
			success : function(data, textStatus) {
				//alert(data)
			},
			error : function() {
			}
		});
	}
	
	function kill(){ 
		var checkText=$("#sel").find("option:selected").text();
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/kill",
			data :  'dz='+checkText,
			success : function(data, textStatus) {
				//alert(data)
			},
			error : function() {
			}
		});
	}
	
	
	function statistics(){
		var checkText=$("#sel").find("option:selected").text();
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/statistics",
			data :  'dz='+checkText,
			success : function(data, textStatus) {
				//alert(data)
			},
			error : function() {
			}
		});
	}
	
	
	function clear1(){
		$("#inoutnum").val("");
		var checkText='';
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/clear",
			data :  'dz='+checkText,
			success : function(data, textStatus) {
			
			},
			error : function() {
			}
		});
	}
	
	
	function check(){
		var checkText='';
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/check",
			data :  'dz='+checkText,
			success : function(data, textStatus) {
			
			},
			error : function() {
			}
		});
	}
	
	
	function sendData() {
		var checkText=$("#sel").find("option:selected").text();
		var ta=	$("#ta").val()
		var cmd=	$("#cmd").val()
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/Redata",
			data : 'dz='+checkText+'&con='+ta+'&type='+cmd,
			success : function(data, textStatus) {
				alert(data)
			},
			error : function() {
			}
		});
	}
	
	function updata() {

		var checkText=$("#sel").find("option:selected").text();
		var version=	$("#version").val()
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/updata",
			data :  'dz='+checkText+'&version='+version,
			success : function(data, textStatus) {
				alert(data)
			},
			error : function() {
			}
		});
	}
	
	function LoadUpgradeFile() {

		var FeilName=$("#FeilName").val();;
		var version=	$("#version").val();
		$.ajax({
			type : "post",
			traditional : true,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			url : "${pageContext.request.contextPath}/LoadUpgradeFile",
			data :  'FeilName='+FeilName+'&version='+version,
			success : function(data, textStatus) {
				alert(data)
			},
			error : function() {
			}
		});
	}
</script>

</html>