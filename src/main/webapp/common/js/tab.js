/**
 * 切换Tab标签
 * @param tab 要切换的Tab标签
 * @param count Tab标签总数
 */
function changeTab(tag, count){
	for(var i = 1 ; i <= count ; i++){
		if(i == tag){
			$("#tab_"+i).addClass("selected");
			$("#div_"+i).css("display","block");
		}else{
			$("#tab_"+i).removeClass("selected");
			$("#div_"+i).css("display","none");
		}
	}
}


