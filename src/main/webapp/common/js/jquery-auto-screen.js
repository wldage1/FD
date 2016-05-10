//页面自动布满浏览器脚本
 $(document).ready(function(){
 	var bodyWidth = document.body.clientWidth > 1000 ? document.body.clientWidth :1024;
	var bodyHeight = document.body.clientHeight > 560 ? document.body.clientHeight :570;
	var mainTableWidth;
	var mainTableHeight;
	var menuHeight;
	// 获取浏览器类型
	var bro=$.browser; 
	// 根据不同浏览器类型定义内容区域高度宽度
	mainTableWidth = bodyWidth;
	mainTableHeight = bodyHeight - 66;
	if(bro.msie) {mainTableHeight = mainTableHeight-9;};
    if(bro.mozilla) {mainTableHeight = mainTableHeight-3;};
    if(bro.safari) {mainTableHeight = mainTableHeight-2;};
    if(bro.opera) {mainTableHeight = mainTableHeight-3;};
	
	// 设置网页内容区域高度宽度
	$("#windowTable").width(bodyWidth);
	$("#windowTable").height(bodyHeight);
	$("#windowMain").height(mainTableHeight);
	$("#zleftmenu").height(mainTableHeight-38);
 });
 
 
