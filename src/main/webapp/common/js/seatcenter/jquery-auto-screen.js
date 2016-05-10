//页面自动布满浏览器脚本
$(document).ready(function(){
 	var bodyWidth = $(window).width();
	var bodyHeight = $(window).height();
	// 获取浏览器类型
	var bro=$.browser; 
	// 根据不同浏览器类型定义内容区域高度宽度
	var contentHeight = bodyHeight - 29 - 75 - 25;
	if(bro.msie) {
		contentHeight = contentHeight - 6;
	}
	if(bro.chrome){
		contentHeight = contentHeight - 20;
	}
    if(bro.mozilla) {
    	contentHeight = contentHeight - 10;
    }
    if(bro.safari) {
    	bodyHeight = bodyHeight - 2 + 25;
    }
    if(bro.opera) {
    	bodyHeight = bodyHeight - 3;
    }
	// 设置网页内容区域高度宽度
	$("#main_page").height(bodyHeight);
	$("#main_page").width(bodyWidth);
	$("#top_navigation_bars").width(contentHeight);
	$("#top").width(contentHeight);
	$("#footer").width(contentHeight);
	$("#content").height(contentHeight);
});
