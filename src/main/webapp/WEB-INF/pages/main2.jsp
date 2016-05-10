<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib prefix="decorator" uri="/WEB-INF/tlds/sitemesh-decorator.tld" %>
<%@ taglib prefix="page" uri="/WEB-INF/tlds/sitemesh-page.tld" %>
<%@ taglib prefix="security" uri="/WEB-INF/tlds/springframework-security.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/tlds/springframework-spring.tld" %>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/springframework-form.tld" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<script type="text/javascript">
 $(document).ready(function(){
	 $.autoScreen(); 
	 $("html").click(function(event){
	 	if($(event.target).attr("id") != "moreTabList" && $(event.target).attr("id") != "moreTabBtn"){
			$("#moreTabList").hide();
			$("#moreTabBtn").removeClass("onMore");
			$("#moreTabBtn").removeClass("downMore");
		}
	 });
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
		$.menuTab(tid);
	});
 });
//计算tab标签存放的最大个数
var maxCount = parseInt((screen.width - 210)/105) - 1;
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
$.menuTab = function(code){
	//第一次进页面第一个iframe显示，其他的隐藏
	if (load){
		//隐藏所有iframe
		$("#zleftmenu iframe").hide();
		//设置第一个iframe 显示
		$("#zleftmenu iframe").first().show();
		//获取左侧标签的第一个
		var dname = $("#verticalTab li").first().attr("title");
		//设置系统名称
		$("#systemName").text(dname);
		//默认获取第一个code
		code = $("#verticalTab li").first().attr("id");
		//显示iframe
		$("#content_"+code).show();
		load = false;
	}
	
	//初始化全局对象
	$showTabContainer = $("#showTabContainer");
	$moreTabBtn = $("#moreTabBtn");
	$moreTabList = $("#moreTabList");
	$hideTabContainer = $("#hideTabContainer");
	
	//隐藏显示标签控制事件
	$moreTabBtn.unbind("hover").hover(function(){
		$(this).addClass("onMore");
	},function(){
		if($moreTabList.css("display") == "none"){
			$(this).removeClass("onMore");
		}
	}).unbind("click").click(function(){
		if($moreTabList.css("display") == "none"){
			$moreTabList.show();
			$(this).addClass("downMore");
		}else{
			$moreTabList.hide();
			$(this).removeClass("downMore");
		}
	});	
	
 	$("ul li a",window.frames["leftMenuFrame_"+code].document).unbind("click").bind("click",function(){
 		var txt = $(this).text();
 		var code = $(this).attr("id");
 		var ifid = $("#content_"+code).attr("id");
 		//显示的tab标签计数
 		var showTabCount = $("li",$showTabContainer).size();
 		//显示的tab标签计数
 		var hideTabCount = $("li",$hideTabContainer).size();
 		//所有标签的数量
 		var tempCount = showTabCount + hideTabCount;
 		//如果此id已经存在值直接显示，不在画新的页面
 		if (ifid){
 			//获取隐藏标签id
 			var compareid = $("#" + code,$hideTabContainer).attr("id");
 			//如果此id存在说面本次点击的为隐藏区域内的标签
 			if (compareid){
				//显示标签和隐藏标签调换
				$.changeTab(code,txt); 		
				//显示iframe
				$("#content_"+code).show();		
				//调用显示标签事件处理函数
				$.showTabEvent();		
				//调用隐藏标签事件处理函数
				$.hideTabEvent();					
 			}
 			else{
	 			//清楚其它标签选中样式
	 			$("li",$showTabContainer).each(function(){
	 				$(this).removeClass();
	 			});
	 			//设置当前 标签被选中
	 			$("#"+code).parent().addClass("nav_1");
	 			//动态话显示主操作区域ifram
	 			//隐藏显示的iframe
	 			$("#content iframe").hide(); 			
				//显示iframe
				$("#content_"+code).show();
 			}
		}
 		else{
			if (showTabCount < maxCount){
				//清除其它显示标签样式
				$("li",$showTabContainer).each(function(){
					$(this).removeClass();
				});
				//最佳新的显示标签在最后
				$showTabContainer.append($("<li></li>").append(
						$("<div></div>").attr("id",code).addClass("mainFont").append(txt)
					).append(
						$("<div></div>").addClass("mainClose")
					).addClass("nav_1")
				);
				//创建或显示iframe主操作界面
				$.createIframe(code);
				//调用显示标签事件处理函数
				$.showTabEvent();
			}
			else if(maxCount <= showTabCount && tempCount < limitSize){
				//显示标签和隐藏标签调换
				$.changeTab(code,txt);
				//调用显示标签事件处理函数
				$.showTabEvent();
				//调用隐藏标签事件处理函数
				$.hideTabEvent();		
				//创建或显示iframe主操作界面
				$.createIframe(code);
				//如果标签的计数个数达到显示最大值，则显示 ">>" 按钮
				if(showTabCount == maxCount){
					$moreTabBtn.show();
				}
			}
			else{
				alert("已经到达标签数量极限，请关闭其它标签，在打开新的。");
			}
 		}
	});	 
 };
//创建或显示iframe主操作界面
$.createIframe = function(code){
	//隐藏显示的iframe
	$("#content iframe").hide();
	//获取iframe的id判断是否存在，若存在直接显示此iframe
	var ifid = $("#content_"+code).attr("id");
	if (!ifid){
		//如果此id不存在，则创建一个新的iframe
		$("#content").append(
			$("<iframe></iframe>").attr("id","content_"+code).attr("name","content_"+code)
			.attr("width","100%").attr("height","100%").attr("frameborder","0")
			.attr("scrolling","auto").attr("src","content?ddd="+code)
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
		$(this).removeClass();
	});
	//追加新的li到显示标签的最后面，并处于选中状态
	$showTabContainer.append(
		$("<li></li>").append(
			$("<div></div>").attr("id",code).addClass("mainFont").append(txt)
		).append(
			$("<div></div>").addClass("mainClose")
		).addClass("nav_1")
	);
	//显示tab文本内容
	var showTabContainerText = $("li",$showTabContainer).first().find(".mainFont").text();
	//显示tab的id
	var showTabContainerID = $("li",$showTabContainer).first().find(".mainFont").attr("id");
	//删除显示标签的第一个
	$("li",$showTabContainer).first().remove();
	//删除隐藏标签中做交换的标签
	$("#"+code,$hideTabContainer).parent().remove();
	//把显示标签的第一个放到下拉层里面（删除在创建信息的元素）
	$hideTabContainer.append(
		$("<li></li>").append(
			$("<a></a>").attr("id",showTabContainerID).append("href","#").text(showTabContainerText).addClass("moreListOut")
		).append(
			$("<div></div>").addClass("tabClose")
		)
	);	
};
//关闭显示标签处理函数
$.closeShowTab = function($cshowTab){
	//删除此tab标签元素
	$cshowTab.parent().remove();
	//删除图片，前面一个元素的id
	var rmid = $cshowTab.prev().attr("id");	
	//删除iframe
	$("#content_"+rmid).remove();	
	//隐藏标签的数量
    var hideTabSize = $("li",$hideTabContainer).size();

	//如果隐藏标签大于0，将隐藏标签第一个从隐藏容器内删除，并追加到显示标签的最后显示
	if (hideTabSize > 0){
		//隐藏元素第一个
		var $hideFirst = $("li",$hideTabContainer).first();
		//隐藏标签对应的id
		var hideCode = $("a",$hideFirst).attr("id");
		//隐藏标签对应的文本内容
		var hideTxt = $("a",$hideFirst).text();
		
		//清除其它显示标签样式
		$("li",$showTabContainer).each(function(){
			$(this).removeClass();
		});
		//追加新的li到显示标签的最后面，并处于选中状态
		$showTabContainer.append(
			$("<li></li>").append(
				$("<div></div>").attr("id",hideCode).addClass("mainFont").append(hideTxt)
			).append(
				$("<div></div>").addClass("mainClose")
			).addClass("nav_1")
		);		
		//显示iframe
		$("#content_"+hideCode).show();
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
			//每删除一个，默认最后一个处于被选中状态
			$("li",$showTabContainer).last().addClass("nav_1");			
		}
		//获取最后一个显示标签id
		var defaultCode = $("li",$showTabContainer).last().find(".mainFont").attr("id");
		//设置最后一个标签对应的iframe显示
		$("#content_"+defaultCode).show();	
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
	$(".mainFont",$showTabContainer).unbind("click").bind("click",function(){
		var id = $(this).attr("id");
		//清楚其它标签选中样式
		$("li",$showTabContainer).each(function(){
			$(this).removeClass();
		});
		//设置当前 标签被选中
		$(this).parent().addClass("nav_1");
		//动态话显示主操作区域ifram
		//隐藏显示的iframe
		$("#content iframe").hide();
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
			$(this).removeClass();
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
				$("<div></div>").attr("id",hideTabContainerTextID).addClass("mainFont").append(hideTabContainerText)
			).append(
				$("<div></div>").addClass("mainClose")
			).addClass("nav_1")
		);
		//显示tab文本内容
		var showTabContainerText = $("li",$showTabContainer).first().find(".mainFont").text();
		//显示tab的id
		var showTabContainerID = $("li",$showTabContainer).first().find(".mainFont").attr("id");
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
		$("#content iframe").hide();
		//显示iframe
		$("#content_"+hideTabContainerTextID).show();
	});	
};
</script>
</head>
<body onload="$.menuTab()">
<table id="windowTable" align="center" width="100%" height="500" border="0" cellspacing="0" cellpadding="0" >
  <tr>
	 <td id="windowTop">
	 	<table align="center" border="0" cellspacing="0" cellpadding="0" width="100%">
	 		<tr>
	 			<td width="30%" align="center">logo</td>
	 			<td width="40%" align="center"><img src="${base}/common/skin/images/main/top_04.jpg" width="427" height="46"></td>
	 			<td width="30%" align="center">
			      	<ul class="topOperate">
			            <li><a href="#">修改密码</a></li>
			            <li class="topExit"><a href="#">退出系统</a></li>
			        </ul> 			
	 			</td>
	 		</tr>
	 	</table>
	 </td>
  </tr>
  <tr>
	 <td id="windowTab">
		 <table align="center" border="0" cellspacing="0" cellpadding="0" width="100%">
			  <tr>
			     <td height="30" width="210" align="center">
					Admin,欢迎您回来！
				 </td>
				 <td height="30" colspan="2">
					<div class="mainNav">
						<ul id="showTabContainer">
						</ul>
						<div class="more">
			                <div class="navMore" id="moreTabBtn"></div>
			                <div class="moreList" id="moreTabList">
			                	<ul id="hideTabContainer">
			                    </ul>
			                </div>
			            </div>
					</div> 
				 </td>
			  </tr>		
		  </table>
	 </td>
  </tr>  
  <tr>
	  <td id="windowMain">
	    <table border="0" align="center" cellspacing="0" cellpadding="0" width="100%" height="100%" class="leftMenu">
		    <tr>
               <td width="34" align="center" valign="top" height="100%" class="leftMenuTab">
                  <table border="0" cellspacing="0" cellpadding="0" class="leftTab1">
                    <tr>
                      <td valign="top" height="100%">
                        <ul id="verticalTab">
                          <c:forEach var="pms" items="${pmslist}" varStatus="vs">
                            <c:choose>
							<c:when test="${vs.count == 1}">
	                            <li class="z${vs.count} leftNav" id="${pms.code}" title="${pms.name}"></li>
	                        </c:when>
	                        <c:otherwise>
	                            <li class="z${vs.count}" id="${pms.code}" title="${pms.name}"></li>
	                        </c:otherwise>    
	                        </c:choose>                      
                          </c:forEach>
                        </ul>                     
					  </td>
                    </tr>
                  </table>
				</td>
			    <td width="168" align="center" class="leftSideBar">                  
                  	<table border="0" cellspacing="0" cellpadding="0" width="160">
                      <tr>
                        <td height="30" id="systemName" align="center" valign="middle"></td>
                      </tr>
                      <tr>
                        <td id="zleftmenu">
                          	<c:forEach var="pms" items="${pmslist}" varStatus="vs">
                          		<iframe id="leftMenuFrame_${pms.code}" width="100%" height="100%" name="leftMenuFrame_${pms.code}" src="left?c=${pms.code}" frameborder="0" scrolling="no"></iframe>
                          	</c:forEach>
						</td>
                      </tr>
                    </table>            
				</td>
			    <td align="center" valign="top" id="content" class="rightContent">
			    	<iframe id="mainFrameWelcome" width="100%" height="100%" name="mainFrameWelcome" src="welcome" frameborder="0" scrolling="no"></iframe>
			    </td>
			</tr>
		</table>	  
	  </td>
  </tr>    
  <tr>
	  <td id="windowBottom">	  
	  </td>
  </tr>  
</table>
</body>
</html>