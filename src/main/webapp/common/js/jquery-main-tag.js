 $(document).ready(function(){ 
	 $("html").click(function(event){
	 	if($(event.target).attr("id") != "moreTabList" && $(event.target).attr("id") != "moreTabBtn"){
			$("#moreTabList").hide();
			$("#moreTabBtn").removeClass("onMore");
			$("#moreTabBtn").removeClass("downMore");
		}
	 });
	 /**
 	$("#verticalTab li").unbind("click").bind("click",function(){
		var tid = $(this).attr("id");
		var name = $(this).attr("title");
		var cls = $(this).attr("class");
		$("#verticalTab li").each(function(){
			$(this).removeClass("leftNav");
		});
		$(this).addClass("leftNav");
		$("#systemName").text(name);
		$("#leftmenu iframe").hide();
		$("#leftMenuFrame_"+tid).show();
		menuTab(tid);
	});	 
	 **/
	 var onindex = 0;
	 $("#verticalTab a").mouseover(function(){
		$(this,"tr").each(function(){
			var index = $(this).attr("id");
			var clsname = $("#left_"+index).attr("class");
			if (clsname.indexOf("on") < 0){
				$("#left_"+index).removeClass().addClass("lefton");
				$("#center_"+index).removeClass().addClass("centeron");
				$("#right_"+index).removeClass().addClass("righton");
			}
			else{
				onindex = index;
			}
		});
	});
	$("#verticalTab a").mouseout(function(){
		$(this,"tr").each(function(){
			var index = $(this).attr("id");
			if (index != onindex){
				$("#left_"+index).removeClass().addClass("leftout");
				$("#center_"+index).removeClass().addClass("centerout");
				$("#right_"+index).removeClass().addClass("rightout");
			}			
		});			
	});	 
 	$("#verticalTab tr").unbind("click").bind("click",function(){
		var index = $("a",$(this)).attr("id");
		var icon = $("a",$(this)).attr("icon");
		//删除图片，改为从a标签的code属性取值
		//var code = $("a img",$(this)).attr("id");
		var code = $("a",$(this)).attr("code");
		var name = $("a",$(this)).text();
		onindex = index;
		//删除菜单的图片
		/*var imgsrc = $("a img",$(this)).attr("src");
		imgsrc = imgsrc.replace(".gif","_temp.gif");
		$("#systemName").css("background","url("+imgsrc+") repeat-x");*/
		$("#zleftmenu iframe").hide();
		$("#leftMenuFrame_"+code).css("display","block");
		var boo = $.browser.msie? $.browser.msie : false;
		if (!boo){
			var ifsrc = $("#leftMenuFrame_"+code).attr("src") + "&" + new Date();
			$("#leftMenuFrame_"+code).attr("src",ifsrc);
		}
		$(".lefton").removeClass().addClass("leftout");
		$(".centeron").removeClass().addClass("centerout");
		$(".righton").removeClass().addClass("rightout");
		var lid = $("#left_"+index).attr("id");
		if (lid){
			$("#left_"+index).removeClass().addClass("lefton");
		}
		var prtid = $("#righttop_"+(index-1)).attr("id");
		if (prtid){
			$("#righttop_"+(index-1)).removeClass().addClass("lefton");
			$("#right_"+(index-1)).removeClass().addClass("rightout");
		}
		$("#center_"+index).removeClass().addClass("centeron");
		var rtid = $("#righttop_"+index).attr("id");
		if (rtid){
			$("#right_"+index).removeClass().addClass("leftout");
			$("#righttop_"+index).removeClass().addClass("righton");
		}
		else{
			$("#right_"+index).removeClass().addClass("righton");
		}
	}); 	
 });
//计算tab标签存放的最大个数(弃用，tab不是定长)
//var maxCount = parseInt((screen.width - 450)/130) - 1;
//tab 极限个数
var limitSize = 50;
//显示标签页面元素容器
var $showTabContainer;
//更多按钮页面元素对象
var $moreTabBtn;
//隐藏标签存放层对象
var $moreTabList;
//隐藏标签页面元素容器
var $hideTabContainer;
//是否是第一次加载 解决火狐浏览器iframe 隐藏后不加载的问题
var load = true;
function menuTab(code,txt,customSrc){
	//始初化全局对象
	$showTabContainer = $("#showTabContainer");
	$moreTabBtn = $("#moreTabBtn");
	$moreTabList = $("#moreTabList");
	$hideTabContainer = $("#hideTabContainer");
	
	//第一次进页面第一个iframe显示，其他的隐藏
	//if (load){
		//var imgsrc = $("#verticalTab a img").first().attr("src");
		//imgsrc = imgsrc.replace(".gif","_temp.gif");
		//$("#systemName").css("background","url("+imgsrc+") repeat-x");
		//load = false;
	//}
	//如果code 有效执行
	if (code){
		//隐藏显示标签控制事件
		$moreTabBtn.unbind("hover").hover(function(){
			$(this).addClass("onMore");
		},function(){
			if($moreTabList.css("display") == "none"){
				$(this).removeClass("onMore");
			}
		}).unbind("click").click(function(event){
			var offset = $(event.target).offset();
			//var top = offset.top ;
			var left = offset.left - 20;
			if($moreTabList.css("display") == "none"){
				$(this).addClass("downMore");
				$(this).addClass("downMore");
				$moreTabList.css("margin-top",20).css("margin-right",left);
				$moreTabList.show();
			}else{
				$moreTabList.hide();
				$(this).removeClass("downMore");
			}
		});	
		//标签函数
		var ifid = $("#content_"+code).attr("id");
		//显示的tab标签计数
		var showTabCount = $("li",$showTabContainer).size();
		//隐藏的tab标签计数
		var hideTabCount = $("li",$hideTabContainer).size();
		//所有标签的数量
		var tempCount = showTabCount + hideTabCount;
		//如果此id已经存在值直接显示，不在画新的页面
		if (ifid){
			//获取隐藏标签id
			var compareid = $("#" + code,$hideTabContainer).attr("id");
			//如果此id存在说面本次点击的为隐藏区域内的标签
			if (compareid){
				//隐藏显示的iframe
	 			$("#content iframe").each(function(){
	 				$(this).hide();
	 			}); 
				//显示标签和隐藏标签调换
				$.changeTab(code,txt); 	
	 			//获取iframe src
	 			var src = $("#content_"+code).attr("src");
	 			//从新请求src 加载页面
	 			$("#content_"+code).attr("src",src);				
				//显示iframe
				$("#content_"+code).show();		
				//调用显示标签事件处理函数
				$.showTabEvent();		
				//调用隐藏标签事件处理函数
				$.hideTabEvent();					
			}
			else{
	 			//清除其它标签选中样式
	 			$("li",$showTabContainer).each(function(){
	 				$(this).find(".mainFontLeft").removeClass().addClass("mainFontLeftChange");
					$(this).find(".mainFont").removeClass().addClass("mainFontChange");
					$(this).find(".mainFontRight").removeClass().addClass("mainFontRightChange");
					//删除选中标记nav_1
					$(this).removeClass();
	 			});
	 			//设置当前 标签被选中
	 			$("#"+code).parent().find(".mainFontLeftChange").removeClass().addClass("mainFontLeft");
	 			$("#"+code).parent().find(".mainFontChange").removeClass().addClass("mainFont");
	 			$("#"+code).parent().find(".mainFontRightChange").removeClass().addClass("mainFontRight");
	 			//增加选中标记
	 			$("#"+code).parent().addClass("nav_1");
	 			//动态话显示主操作区域ifram
	 			//隐藏显示的iframe
	 			$("#content iframe").each(function(){
	 				$(this).hide();
	 			}); 
	 			//获取iframe src
	 			var src = $("#content_"+code).attr("src");
	 			//从新请求src 加载页面
	 			$("#content_"+code).attr("src",src);
				//显示iframe
				$("#content_"+code).show();
			}
		}
		else{
	       	//清除其它显示标签样式
			$("li", $showTabContainer).each(function() {
				$(this).find(".mainFontLeft").removeClass().addClass("mainFontLeftChange");
				$(this).find(".mainFont").removeClass().addClass("mainFontChange");
				$(this).find(".mainFontRight").removeClass().addClass("mainFontRightChange");
				// 删除选中标记nav_1
				$(this).removeClass();
			});
			// 最佳新的显示标签在最后
			$showTabContainer.append($("<li style='cursor: pointer;'></li>")
					.append($("<div></div>").addClass("mainFontLeft"))
					.append($("<div></div>").attr("id", code)
									.addClass("mainFont").append(txt))
					.append($("<div></div>").addClass("mainFontRight")
									.append($("<div></div>").addClass("mainClose"))
					// 增加选中标记
					).addClass("nav_1"));
			//判断tab的总长度是否超过容器长度
			if($.isTabsWidthTooLong() && tempCount < limitSize){
				//删除上一步增加的li元素（不增li无法判断容器是否能够容下tab）
				$("li",$showTabContainer).last().remove();
			 	//显示标签和隐藏标签调换
				$.changeTab(code,txt);
				//调用显示标签事件处理函数
				$.showTabEvent();
				//调用隐藏标签事件处理函数
				$.hideTabEvent();		
				//创建或显示iframe主操作界面
				$.createIframe(code);
				//显示 ">>" 按钮
				$moreTabBtn.show();
			 //超过tab极限数
			 }else if (tempCount >=  limitSize){
			    alert("已经到达标签数量极限，请关闭其它标签，在打开新的。");
			 //正常增加tab
			 }else{
			 	//创建或显示iframe主操作界面
				$.createIframe(code);
				//调用显示标签事件处理函数
				$.showTabEvent();
			 }
		}
	}
 };
//创建或显示iframe主操作界面
$.createIframe = function(code,customSrc){
	//隐藏显示的iframe
	$("#content iframe").each(function(){
		$(this).hide();
	}); 
	//获取iframe的id判断是否存在，若存在直接显示此iframe
	var ifid = $("#content_"+code).attr("id");
	//增加自定义iframe的src功能
	var src = "";
	if( customSrc != 'undefined' && customSrc != null && customSrc != '' ){
		src = customSrc;
	}else{
		src = "content?c="+code;
	}
	if (!ifid){
		//如果此id不存在，则创建一个新的iframe
		$("#content").append(
			$("<iframe></iframe>").attr("id","content_"+code).attr("name","content_"+code)
			.attr("width","100%").attr("height","100%").attr("frameborder","0")
			.attr("scrolling","auto").attr("class","contentIframe").attr("src",src)
		);
	}
	else{
		//显示iframe
		$("#content_"+code).show();
	}	
};
//显示标签和隐藏标签调换
$.changeTab = function(code,txt){
	//清除其它显示标签样式
	$("li",$showTabContainer).each(function(){
	$(this).find(".mainFontLeft").removeClass().addClass("mainFontLeftChange");
		$(this).find(".mainFont").removeClass().addClass("mainFontChange");
		$(this).find(".mainFontRight").removeClass().addClass("mainFontRightChange");
		$(this).removeClass();
	});
	//追加新的li到显示标签的最后面，并处于选中状态
	$showTabContainer.append(
		$("<li></li>").append(
				$("<div></div>").addClass("mainFontLeft")
		).append(
			$("<div></div>").attr("id",code).addClass("mainFont").append(txt)
		).append(
			$("<div></div>").addClass("mainFontRight").append(
				$("<div></div>").addClass("mainClose")
			)
		).addClass("nav_1")
	);
	//删除隐藏标签中做交换的标签
	$("#"+code,$hideTabContainer).parent().remove();
	

    $.moveTabToHideContainer();
};
//移动标签到隐藏容器 
$.moveTabToHideContainer = function (){
	//显示tab文本内容
	var showTabContainerText = $("li",$showTabContainer).first().find(".mainFont").text();
	if (!showTabContainerText){
		showTabContainerText = $("li",$showTabContainer).first().find(".mainFontChange").text();
	}
	//显示tab的id
	var showTabContainerID = $("li",$showTabContainer).first().find(".mainFont").attr("id");
	if (!showTabContainerID){
		showTabContainerID = $("li",$showTabContainer).first().find(".mainFontChange").attr("id");
	}
	//删除显示标签的第一个
	$("li",$showTabContainer).first().remove();
	//把显示标签的第一个放到下拉层里面（删除在创建信息的元素）
	$hideTabContainer.append(
		$("<li></li>").append(
			$("<a></a>").attr("id",showTabContainerID).append("href","#").text(showTabContainerText+"").addClass("moreListOut")
		).append(
			$("<div></div>").addClass("tabClose")
		)
	);	
	//如果标签过长，则继续删除tab
	if($.isTabsWidthTooLong()){
		$.moveTabToHideContainer();
	}
}



//关闭显示标签处理函数
$.closeShowTab = function($cshowTab){
	//删除此tab标签元素
	$cshowTab.parent().parent().remove();
	//删除图片，前面一个元素的id
	var rmid = $cshowTab.parent().prev().attr("id");	
	//删除iframe
	$("#content_"+rmid).remove();	
	//隐藏标签的数量
    var hideTabSize = $("li",$hideTabContainer).size();
	//如果隐藏标签大于0，将隐藏标签第一个从隐藏容器内删除，并追加到显示标签的最后
	if (hideTabSize > 0){
		//隐藏元素第一个
		var $hideFirst = $("li",$hideTabContainer).first();
		//隐藏标签对应的id
		var hideCode = $("a",$hideFirst).attr("id");
		//隐藏标签对应的文本内容
		var hideTxt = $("a",$hideFirst).text();
		//追加新的li到显示标签的最后面，并处于选中状态
		var showSize = $(".nav_1",$showTabContainer).size();
		var tcss = "";
		//关闭已经选中的tab
		if (showSize == 0){
			//显示iframe
			$("#content_"+hideCode).show();
			tcss = "nav_1";
			$showTabContainer.append(
				$("<li></li>").append(
					$("<div></div>").addClass("mainFontLeft")
				).append(
					$("<div></div>").attr("id",hideCode).addClass("mainFont").append(hideTxt)
				).append(
					$("<div></div>").addClass("mainFontRight").append(
						$("<div></div>").addClass("mainClose")
					)
				).addClass(tcss)
		    );
		//关闭没有选中的tab
		}else{
				$showTabContainer.append(
				$("<li></li>").append(
					$("<div></div>").addClass("mainFontLeftChange")
				).append(
					$("<div></div>").attr("id",hideCode).addClass("mainFontChange").append(hideTxt)
				).append(
					$("<div></div>").addClass("mainFontRightChange").append(
						$("<div></div>").addClass("mainClose")
					)
				)
		    );
		}
		
		//删除本隐藏标签
		$hideFirst.remove();
		if (hideTabSize == 1){
			//关闭隐藏按钮
			$moreTabBtn.hide();		
			//隐藏下拉层
			$moreTabList.hide();
		}
	}	
	else{	
		//已经被选中的标签数量
		var showSize = $(".nav_1",$showTabContainer).size();
		//只有为0时代表没有其它标签被选中，并设置最后一个为选中状态
		if (showSize == 0){
			//获取最后一个显示标签id
			var defaultCode = $("li",$showTabContainer).last().find(".mainFontChange").attr("id");
			//每删除一个，默认最后一个处于被选中状态
			$("li",$showTabContainer).last().addClass("nav_1");		
			$("li",$showTabContainer).last().find(".mainFontLeftChange").removeClass().addClass("mainFontLeft");
		    $("li",$showTabContainer).last().find(".mainFontChange").removeClass().addClass("mainFont");
		    $("li",$showTabContainer).last().find(".mainFontRightChange").removeClass().addClass("mainFontRight");
			//设置最后一个标签对应的iframe显示
			$("#content_"+defaultCode).show();
		}
		//如入显示标签个数为0，显示欢迎页面
		var ssize = $("li",$showTabContainer).size();
		if (ssize == 0){
			$("#mainFrameWelcome").show();
		}
	}
	//调用显示标签事件处理函数
	$.showTabEvent();	
};
//显示标签事件处理函数
$.showTabEvent = function (){
	//显示标签关闭事件和鼠标移入，移出事件
	$(".mainClose",$showTabContainer).unbind("click").click(function(){
		//关闭显示标签处理函数
		$.closeShowTab($(this));
	}).unbind("mouseover").mouseover(function(){
		//鼠标悬停事件
		$(this).removeClass().addClass("mainShut");
	}).unbind("mouseout").mouseout(function(){
		//鼠标离开事件
		$(this).removeClass().addClass("mainClose");
	});	
	//显示标签显示主操作区域内容事件
	$("div[class^=mainFont]",$showTabContainer).unbind("click").bind("click",function(){
		var id = $(this).attr("id");
		//清楚其它标签选中样式
		$("li",$showTabContainer).each(function(){
			$(this).find(".mainFontLeft").removeClass().addClass("mainFontLeftChange");
			$(this).find(".mainFont").removeClass().addClass("mainFontChange");
			$(this).find(".mainFontRight").removeClass().addClass("mainFontRightChange");
			//删除选中标记nav_1
			$(this).removeClass();
		});
		//设置当前 标签被选中
		$(this).parent().addClass("nav_1");
		$(this).parent().find(".mainFontLeftChange").removeClass().addClass("mainFontLeft");
		$(this).parent().find(".mainFontChange").removeClass().addClass("mainFont");
		$(this).parent().find(".mainFontRightChange").removeClass().addClass("mainFontRight");
		//动态话显示主操作区域ifram
		//隐藏显示的iframe
		$("#content iframe").each(function(){
			$(this).hide();
		}); 
		//显示iframe
		$("#content_"+id).show();
	});		
};
//隐藏标签处理事件
$.hideTabEvent = function(){
	//隐藏标签鼠标关闭，悬停，移出事件
	$("div",$hideTabContainer).unbind("mouseover").mouseover(function(){
		$(this).addClass("tabShut");
	}).unbind("mouseout").mouseout(function(){
		$(this).removeClass("tabShut");
	}).unbind("click").click(function(){
		$(this).parent().remove();
		var rmid = $(this).prev().attr("id");
		//删除iframe
		$("#content_"+rmid).remove();
		var hsize = $("li",$hideTabContainer).size();
		if (hsize == 0){
			//关闭隐藏按钮
			$moreTabBtn.hide();	
			//隐藏下拉层
			$moreTabList.hide();
		}
	});	
	//隐藏标签鼠标点击事件，悬停，移出事件
	$("a",$hideTabContainer).unbind("mouseover").mouseover(function(){
		$(this).addClass("moreListOver");
	}).unbind("mouseout").mouseout(function(){
		$(this).removeClass("moreListOver");
	}).unbind("click").click(function(){
		//清除其它显示标签样式
		$("li",$showTabContainer).each(function(){
			$(this).find(".mainFontLeft").removeClass().addClass("mainFontLeftChange");
			$(this).find(".mainFont").removeClass().addClass("mainFontChange");
			$(this).find(".mainFontRight").removeClass().addClass("mainFontRightChange");
		});
		//隐藏显示出来的标签
		$moreTabList.hide();
		//设置更多按钮为正常样式
		$moreTabBtn.removeClass("onMore").removeClass("downMore");
		//隐藏Tab文本内容
		var hideTabContainerText = $(this).text();
		//隐藏Tab的id
		var hideTabContainerTextID = $(this).attr("id");
		//删除所选的li
		$(this).parent().remove();
		//追加新的li到显示标签的最后面，并处于选中状态
		$showTabContainer.append(
			$("<li></li>").append(
				$("<div></div>").addClass("mainFontLeft")
			).append(
				$("<div></div>").attr("id",hideTabContainerTextID).addClass("mainFont").append(hideTabContainerText)
			).append(
				$("<div></div>").addClass("mainFontRight").append(
					$("<div></div>").addClass("mainClose")
				)
			).addClass("nav_1")
		);
		//显示tab文本内容
		var showTabContainerText = $("li",$showTabContainer).first().find(".mainFont").text();
		if (!showTabContainerText){
			showTabContainerText = $("li",$showTabContainer).first().find(".mainFontChange").text();
		}
		//显示tab的id
		var showTabContainerID = $("li",$showTabContainer).first().find(".mainFont").attr("id");
		if (!showTabContainerID){
			showTabContainerID = $("li",$showTabContainer).first().find(".mainFontChange").attr("id");
		}
		//删除显示标签的第一个
		$("li",$showTabContainer).first().remove();
		//把显示标签的第一个放到下拉层里面（删除在创建信息的元素）
		$hideTabContainer.append(
			$("<li></li>").append(
				$("<a></a>").attr("id",showTabContainerID).append("href","#").text(showTabContainerText).addClass("moreListOut")
			).append(
				$("<div></div>").addClass("tabClose")
			)
		);
		//调用显示标签事件处理函数
		$.showTabEvent();
		//调用隐藏标签事件处理函数
		$.hideTabEvent();		
		//隐藏显示的iframe
		$("#content iframe").each(function(){
			$(this).hide();
		}); 
		//显示iframe
		$("#content_"+hideTabContainerTextID).show();
	});	
};
//判断tab是否超过页面宽度
$.isTabsWidthTooLong= function(){
	var tabwithsum = 0;
	$('#showTabContainer li').each(function(){
	   tabwithsum += $(this).width();
	});
	//40像素为显示隐藏菜单按钮
 	return  parseInt(tabwithsum) >= parseInt($('#showTabContainer').width() - 40) ;
};