$(function(){
	indexlayoutObj = $('#index_easyui-layout');
	index_initLayout();
});

//layout object
var indexlayoutObj;

//初始化layout
function index_initLayout() {
	indexlayoutObj.layout();
    setHeight();
}

//设置高度
function setHeight(){
    var p = indexlayoutObj.layout('panel','center');    // get the center panel
    var oldHeight = p.panel('panel').outerHeight();
    p.panel('resize', {height:'auto'});
    var newHeight = p.panel('panel').outerHeight();
    indexlayoutObj.layout('resize',{
        height: (indexlayoutObj.height() + newHeight - oldHeight)
    });
}

var iframeIndex;
//添加tab页
function index_addTab(title, url){
	var index_tabs = $('#index_tabs');
	/*if(isLogin()==false) {
		alert('请先登录');
		return;
	}*/
	
	iframeIndex = new Date().getTime();
	var content="<iframe id='iframe_" + iframeIndex + "' name='iframe_" 
		+ iframeIndex + "' scrolling='auto' frameborder='0' src='"
		+ global_rootPath + url + "' style='width:100%;height:100%;overflow:hidden;'></iframe>";
	var flag = index_tabs.tabs('exists', title);
	if(flag){
		index_tabs.tabs('select', title);
	}else{
		index_tabs.tabs('add',{
			title: title ,
			closable: true,
			content: content
		});
	}
}

/**
 * 获取选中的iframe名称
 * @return {selectedTabs} 
 */
function getSelectedTabs() {
	var ifm = $('#main_Tables',top.parent.document).tabs('getSelected').panel('options').content;
	var regx = new RegExp(/id='([^\']*)'/gi);
    var ifmid = regx.exec(ifm)[1];
    return eval(ifmid);
}

/**
 * 根据指定参数打开弹出框方法
 * @param {Object} title 标题
 * @param {Object} width 宽
 * @param {Object} height 高
 * @param {Object} url 打开页面
 * @param {Object} refresh 关闭时是否刷新打开者页面
 * @param {Object} maximize 窗口是否最大化
 */
function openDialog(title, width, height, url, refresh, maximize) {
	$("#iframe_1").attr("src", url);
	var dialog = $('#dialog_1');
	//添加关闭事件
	dialog.window( {
		shadow:false,// 窗口没有阴影
		minimizable:false,// 去掉最小化按钮
		onBeforeClose : function() {
			if(refresh) {
				var ifm = $('#index_tabs').tabs('getSelected').panel('options').content;
				var regx = new RegExp(/id='([^\']*)'/gi);
			    var ifmid = regx.exec(ifm)[1];
			    eval(ifmid).refPage();
			}
		}
	});
	dialog.dialog('open');
	//动态改变大小
	dialog.panel("resize", {
		width : width,
		height : height
	});
	//动态改变位置
	dialog.dialog('setTitle', title);
	var scrollTop = document.documentElement.scrollTop;
	//窗口最大化
	if(maximize) {
		$('.panel-tool-max').click();
		dialog.panel("move",{
			top:scrollTop+'px',
			left:document.documentElement.scrollLeft
		});
	} else {
		var top=(screen.height-height)/4;
		var left=(screen.width-width)/2;
		dialog.panel("move",{top:top+scrollTop,left:left});
	}
}
/**
 * 关闭打开的弹出框
 */
function dialogClose(){
	 $('#dialog_1').window('close');
}