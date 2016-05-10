$(document).ready(function(){
	//系统菜单隐藏通用函数
/*	$(document).click(function() {
		$(window.parent.document).find('.start-menu-div').each(function(){
			var dis = $(this).css("display");
			if (dis == 'block'){
				$(this).hide(200);
			}
		});
		//设置选中功能为不选中状态
		$(window.parent.document).find('.menu-items-div').each(function(){
			$(this).css("background","");
		});
	});	*/
	
	//帮助效果控制方法
	$("#help_btn").toggle(function(){
		$("#help_info").show(500);
	},function(){
		$("#help_info").hide(500);
	});
	
	//删除mongo文件
	$.delMongoFile = function(base, id){
		$.post(base + "/storage/delete", {id :id},function(data){});
	};
	
	//下载mongo文件
	$.downloadMongoFile = function(base, id){
		window.location.href = base + "/storage/download?id=" + id;
	};
	
	//删除指定目录下文件
	$.delFile = function(base, p, n) {
		$.post(base + "/storage/deleteFile", {p :p, n: n},function(data){});
	};
	
	//下载本地文件
	$.downloadFile = function(base, p, n){
		window.location.href = base + "/storage/downloadFile?p=" + p + "&n=" + n;
	};
});