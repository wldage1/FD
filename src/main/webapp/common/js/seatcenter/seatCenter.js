/**
 * 座席中心
 * 
 */

/**
 * 座席中心页面导航
 */
function tab(tag) {
	for ( var i = 1; i <= link.length; i++) {
		$("#tab_" + i).removeClass("selected");
		var src = $("#img_" + i).attr("src") ;
		var start = src.length - 5;
		var end = src.length - 4;
		if (i == tag) {
			if (src.substring(start, end) == '1') {
				src = src.substring(0, start) + 2 + src.substring(end, src.length);
			}
		} else {
			if (src.substring(start, end) == '2') {
				src = src.substring(0, start) + 1 + src.substring(end, src.length);
			}
		}
		$("#img_" + i).attr("src", src);
	}
	$("#iframeMain").attr("src", base + "/seatcenter?functionId=" + link[tag - 1]);
}

/**
 * 座席中心页面导航跳转
 */
function navigation(functionID){
	for ( var i = 1; i <= link.length; i++) {
		$("#tab_" + i).removeClass("selected");
		var src = $("#img_" + i).attr("src") ;
		var start = src.length - 5;
		var end = src.length - 4;
		if (src.substring(start, end) == '2') {
			src = src.substring(0, start) + 1 + src.substring(end, src.length);
		}
		$("#img_" + i).attr("src", src);
	}
	$("#iframeMain").attr("src", base + "/seatcenter?functionId=" + functionID);
}