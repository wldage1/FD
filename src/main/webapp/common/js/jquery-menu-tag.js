var _my_sys_messsge_ids = [];
$(document).ready(function(){
	// 加载左侧菜单
	$("#menu").html($.ajax({url:"left?c=1",async:false,cache:false}).responseText);
    //
	//设置屏幕的高度和宽度
	var mBodyHeight = document.body.clientHeight > 570 ? document.body.clientHeight :570;
	//展开第一个菜单
	$("#sideBar").find(".start-menu-div:first").attr("nowDisplay","TRUE").show();
	
	//绑定一级菜单点击事件
	$("#sideBar").find("[class='menuLink']").live("click",function(){
		//隐藏当前显示的二级菜单
		$("#sideBar").find("div[nowDisplay='TRUE']").attr("nowDisplay","FALSE").hide();
		var code = $(this).attr("id");
	    $("#startMenuUl"+code).slideToggle(400);
	    //标记当前显示的二级菜单
	    $("#startMenuUl"+code).attr("nowDisplay","TRUE");
	});
	//绑定二级菜单点击事件
	$("#sideBar li").live("click",function(){
		var code = $(this).find("a").attr("id");
		var txt = $(this).find("a").text();
		//changeSecondMenu(code)
		menuTab(code,txt);
	});	
	$("#show_or_hide_menu").live("click",function(){
		var menu = $('#fd_menu_container');
		menu.toggle();
		var img = $('#show_or_hide_menu :first-child');
		if (img.attr("class") == 'show'){
			img.attr("class",'hide'); 
		}else{
			img.attr("class",'show');
		}
	});	
	/*站内信提示*/
	var myMessageTip = document.getElementById("my_sys_message_tip") ;
	if(myMessageTip != null && myMessageTip != 'undefined'){
		showMySysMessage();
	    setInterval(showMySysMessage, "600000");   
	    $("#my_sys_message_tip").live("click",function(){
	    	menuTab("501","我的消息");
	    });
	    $("#my_sys_message_new_arrived_count").live("click",function(){
	    	menuTab("501","我的消息");
	    });
	}

});

function showMySysMessage(){
	  _my_sys_messsge_ids = getMySysMessageIDs();
    setSysMessageClass();
}

function changeSecondMenu(id){
	$('#' + id).parent().attr("class","start-menu-middle-li-selected");
}

function getMySysMessageIDs(){
	return  eval($.ajax({url:"message/manage/getmymessagelist",async:false,cache:false}).responseText);
}
function setSysMessageClass(){
	if(_my_sys_messsge_ids.length>0){
    	$('#my_sys_message_tip').attr('class','mySysMessage_newArrived');
    	$('#my_sys_message_new_arrived_count').html("(" + _my_sys_messsge_ids.length +")");
    }else{
    	$('#my_sys_message_tip').attr('class','mySysMessage_no_newArrived');
    	$('#my_sys_message_new_arrived_count').html("");
    }
}

function readOneSysMessage(messageID){
	 var inArrayPosition = $.inArray(messageID,_my_sys_messsge_ids);
	 if(inArrayPosition != -1){
	 	//删除未读id
	     _my_sys_messsge_ids.splice(inArrayPosition,1);
	 }
	 setSysMessageClass();
}