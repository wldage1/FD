<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<script type="text/javascript">
$(window).resize(function(){ 　　
	$("#sendMessageList").setGridWidth($(window).width()*0.999);　
});
$(document).ready(function(){
    jQuery("#sendMessageList").jqGrid({
        url: 'send/selectMember.json',
        datatype: 'json',
        colNames: [
                "<msg:message code='member.name'/>",
                "登陆账号",
       	        "",
       	        "<msg:message code='member.organization'/>",
       	     	"<msg:message code='member.status'/>",
       	        "组织类别",
       	        "性别",
       	        "省",
       	        "市"
       	        ],
               colModel: [
                {name: 'name',index: 'name',width:'20%',align:'center',hidden: false,sortable: false,formatter:formatMemberName},
                {name: 'account',index: 'account',width:'20%',align:'center',hidden: false,sortable: false},
                {name: 'nickName',index: 'nickName',width:'20%',align:'center',hidden: true,sortable: false},
                {name: 'teamName',index: 'teamName',width:'20%',align:'center',hidden: false,sortable: false},
                {name: 'status',index: 'status',width:'10%',align:'center',hidden: false,sortable: false,formatter:formatStatus},
       			{name: 'orgType',index:'orgType', width:'10%',align:'center',hidden:false,sortable:false,formatter:formatOrgType},
       			{name: 'gender',index:'gender', width:'10%',align:'center',hidden:false,sortable:false,formatter:formatGender},
       			{name: 'provinceName',index:'provinceName', width:'10%',align:'center',hidden:false,sortable:false},
       			{name: 'cityName',index:'cityName', width:'10%',align:'center',hidden:false,sortable:false}
                ],
        mtype:"POST",
        postData:{c:"${code}"},
        rowNum:10,     
        page:"${sendMessage.page}",
        rowList: [10, 20, 30],
        pager: '#pagered',
        height:	230,
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        rownumbers: false,
        jsonReader: {
        	repeatitems: false
        },
        caption:'<msg:message code="member.list"/>',
        toolbar: [true,"top"]
    });
	
    function formatStatus(cellvalue, options, rowObject){
    	var status = '';
    	if(cellvalue ==1){
    		status = '已注册';
    	}else if (cellvalue ==2){
    		status = '已认证';
    	}
		return status;   
	 }
    function formatOrgType(cellvalue){
    	var org = '';
    	if(cellvalue ==0){
    		org = '机构';
    	}else if (cellvalue ==1){
    		org = '团队';
    	}
    	return org;
    }
    function formatGender(cellvalue){
    	var gender = '';
    	if(cellvalue ==1){
    		gender = '男	';
    	}else if(cellvalue == 2){
    		gender = '女';
    	}else if(cellvalue == 3){
    		gender = '保密';
    	}
    	return $.trim(gender);
    }
    function formatMemberName(cellvalue, options, rowdata){
    	var nickname = rowdata.nickName;
    	if(nickname!=null && nickname != '' && nickname != 'undefined'){
    		cellvalue += "(" + nickname + ")"; 
    	}
    	if(cellvalue == null){
    		cellvalue = '';
    	}
    	return cellvalue;
    }
     
    jQuery("#sendMessageList").closest(".ui-jqgrid-bdiv").css("overflow-x","hidden");
    
  	//隐藏所以模板标签
	$("#system").hide();
	$("#note").hide();
	$("#email").hide();
	$("#im").hide();
    
  	//模板标签点击事件
    $(".templateContent").click(function(){
    	var id = this.id;
    	$("#"+id).addClass("templateTab");
    	$("div[id!="+id+"]").removeClass("templateTab");
    	var content,title;
    	if(id == "system"){
    		//显示模板内容
    		content=$("#systemMessage").val();
    		//更改模板标题
    		title="<msg:message code='messageTemplate.systemContent'/><msg:message code='system.common.sign.colon'/>"
    	}else if(id == "note"){
    		content=$("#noteMessage").val();
    		title="<msg:message code='messageTemplate.noteContent'/><msg:message code='system.common.sign.colon'/>"
    	}else if(id == "email"){
    		content=$("#emailMessage").val();
    		title="<msg:message code='messageTemplate.emailContent'/><msg:message code='system.common.sign.colon'/>"
    	}else{
    		content=$("#imMessage").val();
    		title="<msg:message code='messageTemplate.imContent'/><msg:message code='system.common.sign.colon'/>"
    	}
    	$("#contentTitle").html(title);
    	var textarea = $("#messageContent");
    	textarea.removeAttr('readonly');
    	textarea.css('color','#333333');
    	textarea.val(content);
    });
    
  	//模板类型多选框单击事件
    $("input:checkbox[name=sendWay]").click(function(){
    	templateTypeToggle(this);
    	var checkboxvalue = parseInt(this.value);
    	//都没选中
    	if(!$("input:checkbox[name=sendWay]").is(":checked")){
    		$("#contentTitle").html('消息内容：');
    		var textarea = $("#messageContent");
    		textarea.val("请您选择发送方式之后再填写消息内容");
    		textarea.attr('readonly','readonly');
    		textarea.css('color','#c0c0c0');
    		return;
    	}else if(!$(this).is(":checked")){
    		checkboxvalue =  parseInt($("input:checkbox[name=sendWay][checked]").eq(0).val());
    	}
    	   	switch (checkboxvalue){
        	case 1:
        		$("#system").click();
        		break;
        	case 2:
        		$("#note").click();
        		break;
        	case 3:
        		$("#email").click();
        		break;
        	case 4:
        		$("#im").click();
        		break;
        	}
    
    });
    
});

//通用关闭
function closeTopLayer(){
	top.layer.close(i);
}
	
//模板类型显示隐藏切换
function templateTypeToggle(obj){
	switch (parseInt(obj.value)){
	case 1:
		$("#system").toggle();
		break;
	case 2:
		$("#note").toggle();
		break;
	case 3:
		$("#email").toggle();
		break;
	case 4:
		$("#im").toggle();
		break;
	}
}
	
//保存消息内容
function textChange(){
	var textarea =  $("#messageContent");
	//如果为textarea为只读，不进行复制操作
	if(textarea.attr('readonly') == 'readonly'){
		return ;
	}
	var content =textarea.val();
	var id = $(".templateTab").attr("id");
	
	if(id == "system"){
		$("#systemMessage").val(content);
	}else if(id == "note"){
		$("#noteMessage").val(content);
	}else if(id == "email"){
		$("#emailMessage").val(content);
	}else {
		$("#imMessage").val(content);
	}
}

//获取所选城市的
var _message_send_message_area_temp = {};
function selectCity(select){
	var id = $(select).val();
	var url = "${base}/message/send/selectCity.json?id=" + id;
	if(id==''||id=='undefined' || id==null){
		return false;
	}
	var optiongJson ;
	if(_message_send_message_area_temp[id] != null && _message_send_message_area_temp[id] != ''){
		optiongJson = _message_send_message_area_temp[id] ;
		printOptions(select,optiongJson);
	}else{
		$.ajax({
			url:url,
			type:'post',
			timeout:'60000',
			dataType:'json',
			success:function(jsonData,textStatus){
				if (textStatus == "success"){
					if(jsonData.status == "success"){
						//查询结果缓存到页面
						optiongJson  = jsonData.cityList;
						_message_send_message_area_temp[id] = jsonData.cityList;
						printOptions(select,optiongJson);
					}else if(jsonData.status=="error"){
						top.$.layer({
						shade : [0.5 , '#000' , true],
				  		offset : ['50px',''],
				  	 	title : "提示信息",
					    dialog : {msg:"获取城市失败！",type : 3}	
						});
					}
				}
			}
		});
	}

}
function printOptions(el,jsonData){
	var s = '<option value="">请选择</option>';
	$.each(jsonData, function(key, val) {
		s += "<option value='" + val.id + "' >" + val.name + "</option>"; 
 　　});
    $("#city").html(s);
}
</script>

<style type="text/css">

input[type="radio"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:-1px; margin-bottom:-2px\9;
}
input[type="checkbox"] {
	width  : 14px;
	height : 14px;
	vertical-align:text-bottom; margin-bottom:-1px; margin-bottom:-2px\9;
}
.message_content_readonly{
   width: 710px; 
   height: 230px;
   color: #c0c0c0;
}
</style>
	<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<form:form method="post" action="send/saveSendMessage" id="sendMessage" commandName="sendMessage" >
			
			<input type="hidden" name="code" id="code" value="${code }"/>
			<input type="hidden" name="systemMessage" id="systemMessage" />
			<input type="hidden" name="noteMessage" id="noteMessage" />
			<input type="hidden" name="emailMessage" id="emailMessage" />
			<input type="hidden" name="imMessage" id="imMessage" />
		<!-- 	<input type="hidden" name="messageTaskID" id="messageTaskID"/> -->
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="skinLayout">
				<tr>
					<td width="15%" align="right" class="skinLayout_info"><msg:message code="messageTask.name"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="85%" class="skinLayout_lable">
					    <select name="messageTaskID" id="messageTaskID">
					        <c:forEach items="${messageTaskList }" var="messageTask">
					        <option value="${messageTask.id }">${messageTask.name}(代码:${messageTask.code})</option>
					        </c:forEach>
					    </select>
					    <font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info"><msg:message code="sendMessage.sendWay"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="85%" class="skinLayout_lable">
						<form:checkbox path="sendWay" value="1" /><msg:message code="messageTemplate.system"/>
						<form:checkbox path="sendWay" value="2" /><msg:message code="messageTemplate.note"/>
						<form:checkbox path="sendWay" value="3" /><msg:message code="messageTemplate.email"/>
						<form:checkbox path="sendWay" value="4" /><msg:message code="messageTemplate.IM"/>
						<font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right" class="skinLayout_info" id="contentTitle">
						<msg:message code="sendMessage.messageContent"/><msg:message code="system.common.sign.colon"/></td>
					<td width="85%" class="skinLayout_lable">
						<div style="width:750px;height:260px;">
							<div style="width:750px;height:230px;">
								<textarea id="messageContent" onblur="textChange()" readonly class="message_content_readonly" >请您选择发送方式之后再填写消息内容</textarea>
								<font color="red">&nbsp;*&nbsp;</font>
							</div>
							<div style="width:710px;height:25px;">
								<div id="system" style="border-right-width:0px;" class="templateContent">
									<msg:message code="sendMessage.systemMessage"/></div>
								<div id="note" style="border-right-width:0px;" class="templateContent">
									<msg:message code="sendMessage.noteMessage"/></div>
								<div id="email" style="border-right-width:0px;" class="templateContent">
									<msg:message code="sendMessage.emailMessage"/></div>
								<div id="im" class="templateContent"><msg:message code="sendMessage.imMessage"/></div>
							</div>
						</div>
					</td>
				</tr>
			</table>
			</form:form>
		</tr>
		
		<tr>
			<form:form method="post" action="selectMember" id="member" commandName="member" >
			<table width="100%" border="0"  cellspacing="1" cellpadding="0" class="search">
				<tr>
					<td width="10%" align="right" class="search_info"><msg:message code="member.name"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
						<form:input path="name" />
					</td>
					<td width="10%" align="right" class="search_info"><msg:message code="member.status"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
						<form:select path="status" style="width:150px;">
							<form:option value=""><msg:message code="messageTask.select"/></form:option>
							<form:option value="1"><msg:message code="member.register"/></form:option>
							<form:option value="2"><msg:message code="member.authentication"/></form:option>
						</form:select>
					</td>
					<td rowspan="3" width="15%" class="button_area" align="center">
						<input type="button" class="btn2" id="select" onclick="searchSendMembers()" value="<msg:message code='button.search'/>">&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="reset" class="btn2" value="<msg:message code='button.reset'/>">
					</td>
				</tr>
				<tr>
					<td width="10%" align="right" class="search_info"><msg:message code="member.org"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
						<form:select path="orgID" style="width:150px;">
							<form:option value="0"><msg:message code="messageTask.select"/></form:option>
							<c:forEach items="${organizationList}" var="item">
								<c:if test="${organizationList != null}">
									<option value="${item.id}">${item.name}</option>
								</c:if>
							</c:forEach>
						</form:select>
					</td>
					<td width="10%" align="right" class="search_info"><msg:message code="member.team"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
						<form:select path="teamID" style="width:150px;">
							<form:option value=""><msg:message code="messageTask.select"/></form:option>
							<c:forEach items="${teamList}" var="item">
								<c:if test="${teamList != null}">
									<option value="${item.id}">${item.name}</option>
								</c:if>
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td width="10%" align="right" class="search_info"><msg:message code="member.area"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
						<form:select path="province" style="width:150px;" onchange="selectCity(this)">
							<form:option value=""><msg:message code="messageTask.select"/></form:option>
							<c:forEach items="${provinceList}" var="item">
								<c:if test="${provinceList != null}">
									<option value="${item.id}">${item.name}</option>
								</c:if>
							</c:forEach>
						</form:select>
						<form:select path="city" style="width:150px;" >
							<form:option value=""><msg:message code="messageTask.select"/></form:option>
							<c:forEach items="${cityList}" var="item">
								<c:if test="${cityList != null}">
									<option value="${item.id}">${item.name}</option>
								</c:if>
							</c:forEach>
						</form:select>
					</td>
					<td width="10%" align="right" class="search_info"><msg:message code="member.gender"/>
						<msg:message code="system.common.sign.colon"/></td>
					<td width="30%" class="search_lable">
					    <select id="gender" style="width:150px;">  
					    <option value=''><msg:message code="messageTask.select"/></option>
					    <option value="1"><msg:message code="member.nan"/></option>
					    <option value="2"><msg:message code="member.nv"/></option>
					    <option value="3">保密</option>
					    </select>
					</td>
				</tr>
			</table>
			</form:form>
		</tr>
		
		<tr>
			<td width="100%" valign="top">
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td>
							<table id="sendMessageList"><tr><td>&nbsp;</td></tr></table>
							<div id="pagered"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	    <div style="height: 43px;"></div>
		<div class="toolbar">
		<div class="left"></div>
		<div class="center">
		    <input type="button" value="发送给选择" onclick="sendMessageBySelect()"/>
		    <input type="button" value="发送给检索条件" onclick="sendMessageBySearch()"/>
		    <input type="button" value="发送给所有" onclick="sendMessageAll()"/>
		</div>
		<div class="right"></div>
	</div>
	<script type="text/javascript">
	function searchSendMembers(){
		var searchValues =getSearchValues();
		jQuery("#sendMessageList").jqGrid('setGridParam',{page:1,postData:searchValues})
		$("#sendMessageList").trigger("reloadGrid");
	}
	function validSelect(){
		//var messageTaskSelect = $('#messageTaskID').val();
		if($('#messageTaskID').val() == '' || $('#messageTaskID').val()==null){
			return 1;
		}else if (!$("input:checkbox[name=sendWay]").is(":checked")){
			return 2;
		}
		return 0;
	}
	
	function valiTemplate(){
		var tip = "";
		if ($("#sendWay1").is(":checked") && ($('#systemMessage').val()==""||$('#systemMessage').val() == null)){
			tip +=  "," + "站内模板";
		} 
		if  ($("#sendWay2").is(":checked") && ($('#noteMessage').val()==""||$('#noteMessage').val() == null)){
			tip +=  "," + "短信模板";
		} 
		if  ($("#sendWay3").is(":checked") && ($('#emailMessage').val()==""||$('#emailMessage').val() == null)){
			tip +=  "," + "邮件模板";
		} 
		if  ($("#sendWay4").is(":checked") && ($('#imMessage').val()==""||$('#imMessage').val() == null)){
			tip +=  "," + "IM模板";
		}
		if(tip != ""){
			tip = tip.substring(1);
		}
		return tip;
	}
	
	 function sendMessageBySelect(){
		    if(validSelect() == 1){
		    	top.layer.msg("您没有选择任何任务！");
		    	return false;
		    }else if(validSelect() == 2){
		    	top.layer.msg("请您至少选择一种发送方式！");
		    	return false;
		    }
		    var temptip = valiTemplate();
		    if( temptip != ""){
		    	top.layer.msg(temptip + "内容不能为空！");
		    	return false;
		    }
	    	var ids = jQuery("#sendMessageList").jqGrid("getGridParam","selarrrow");
 			if (ids && ids !=''){
				var layers = top.layer.confirm("您确认发送消息到选择的理财顾问？",  function(){
					top.layer.close(layers);
					//var shadelayer =  top.layer.load(0);
					var shadelayer = $.layer({
						shade : [0.5 , '#000' , true],
						type : 3
						});
				 	var messageTaskID = $("#messageTaskID").val();
					var url = "send/saveSendMessage.json";
					var submitObj = {};
					submitObj["content"] = getMessage();
					submitObj["ids"] = $.toJSON(ids);
					submitObj["messageTaskID"] = messageTaskID;
					$.ajax({
						url:url,
						type:'post',
						timeout:'300000',
						data:submitObj,
						dataType:'json',
						success:function(jsonData,textStatus){
							if (textStatus == "success"){
								layer.close(shadelayer);
								if (jsonData.status == "success"){
									layer.msg("发送成功！",2,1);
								}else{
									layer.msg("发送失败！");
								}
							}
						}
					}); 
				});
			}
			else{
	    		top.layer.msg("您没有选择任何理财顾问！");
	   		} 

	 }
	 function getMessage(){
		 var jsonObj = {};
		 if ($("#sendWay1").is(":checked")) 
		 jsonObj["1"] = $('#systemMessage').val();
		 if ($("#sendWay2").is(":checked")) 
		 jsonObj["2"] = $('#noteMessage').val();
		 if ($("#sendWay3").is(":checked")) 
		 jsonObj["3"] = $('#emailMessage').val();
		 if ($("#sendWay4").is(":checked")) 
		 jsonObj["4"] = $('#imMessage').val();
		 return $.toJSON(jsonObj);
		
	 }
	 function getSearchValues(){
		 var jsonObj = {};
		 var memberName = $("#name").val();
		 var memberStatus = $("#status").val();
		 var orgID = $("#orgID").val();
		 var teamID = $("#teamID").val();
		 var province = $("#province").val();
		 var city = $("#city").val();
		 var gender = $("#gender").val();
			 jsonObj["name"] = memberName;
			 jsonObj["status"] = memberStatus;
			 jsonObj["orgID"] = orgID;
			 jsonObj["teamID"] = teamID;
			 jsonObj["province"] = province;
			 jsonObj["city"] = city;
			 jsonObj["gender"] = gender;
		 return jsonObj;
	 }
	 function sendMessageBySearch(){
		   if(validSelect() == 1){
		    	top.layer.msg("您没有选择任何任务！");
		    	return false;
		    }else if(validSelect() == 2){
		    	top.layer.msg("请您至少选择一种发送方式！");
		    	return false;
		    }
		    var temptip = valiTemplate();
		    if( temptip != ""){
		    	top.layer.msg(temptip + "内容不能为空！");
		    	return false;
		    }
		 var searchValues = getSearchValues();
		 if(searchValues != {}){
				var layers = top.layer.confirm("您确认发送消息到检索条件的理财顾问？",  function(){
					top.layer.close(layers);
					var shadelayer = $.layer({
						shade : [0.5 , '#000' , true],
						type : 3
						});
					// =  top.layer.load(0);
					var messageTaskID = $("#messageTaskID").val();
					var url = "send/saveSendMessageBySearch.json";
					var submitObj = {};
					submitObj["content"] = getMessage();
					submitObj["searchCondition"] = $.toJSON(searchValues);
					submitObj["messageTaskID"] = messageTaskID;
					$.ajax({
						url:url,
						type:'post',
						timeout:'300000',
						data:submitObj,
						dataType:'json',
						success:function(jsonData,textStatus){
							if (textStatus == "success"){
								if (jsonData.status == "success"){
									layer.close(shadelayer);
									layer.msg("发送成功！",2,1);
								}else{
									layer.msg("发送失败！");
								}
							}
						}
					}); 
				});
		 }else{
			 top.layer.msg("您没有输入任何查询条件！");
		 }
		 
	 }
	 function sendMessageAll(){
		   if(validSelect() ==1){
		    	top.layer.msg("您没有选择任何任务！");
		    	return false;
		    }else if(validSelect() ==2) {
		    	top.layer.msg('请您至少选择一种发送方式！');
		    	return false;
		    }
		    var temptip = valiTemplate();
		    if( temptip != ""){
		    	top.layer.msg(temptip + "内容不能为空！");
		    	return false;
		    }
			var layers = top.layer.confirm("您确认发送消息到所有的理财顾问？",  function(){
				top.layer.close(layers);
				var shadelayer = $.layer({
					shade : [0.5 , '#000' , true],
					type : 3
					});
				var messageTaskID = $("#messageTaskID").val();
				var url = "send/saveSendMessageAllMemer.json";
				var submitObj = {};
				submitObj["content"] = getMessage();
				submitObj["messageTaskID"] = messageTaskID;
				$.ajax({
					url:url,
					type:'post',
					timeout:'300000',
					data:submitObj,
					dataType:'json',
					success:function(jsonData,textStatus){
						if (textStatus == "success"){
							if (jsonData.status == "success"){
								layer.close(shadelayer);
								layer.msg("发送成功！",2,1);
							}else{
								layer.msg("发送失败！");
							}
						}
					}
				}); 
			});
	 }
	</script>
