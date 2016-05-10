<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="${base}/common/js/datasource/ds.js"></script>
<script type="text/javascript">
//名称提示信息
var namePrompt = "<msg:message code='attribute.name.input'/>";
//code提示信息
var codePrompt = "<msg:message code='attribute.code.input'/>";
var count = 0;
//展示类型
var pageShowType = "";
<c:forEach var="pageShowType" items="${pageShowTypeList}">
	pageShowType += "<option value=\"${pageShowType.key}\">${pageShowType.value}</option>";
</c:forEach>
//验证规则	
var validationRuleId = "<option value=\"0\"><msg:message code='please.select'/></option>";
	<c:forEach var="validationRule" items="${validationRuleList}">
		validationRuleId += "<option value=\"${validationRule.id}\">${validationRule.name}</option>";
	</c:forEach>
$(document).ready(function(){
	$("input").live("focus",function(){
		$(this).poshytip('hide');
	});
	//验证
	$("#name").poshytip({
			content: "<msg:message code='productAttribute.name.input'/>",
			className: 'tip-violet',
			showOn: 'focus',
			alignTo: 'target',
			alignX: 'right',
			alignY: 'center',
			offsetX: 5,
			hideAniDuration:100
		});
		$("#code").poshytip({
			content:"<msg:message code='productAttribute.code.input'/>",
			className: 'tip-violet',
			showOn: 'focus',
			alignTo: 'target',
			alignX: 'right',
			alignY: 'center',
			offsetX: 5,
			hideAniDuration:100
		});
		
    jQuery("#attributeList").jqGrid({
    	datatype: "local",
        colNames: ["<msg:message code='attribute.name'/><font color=\"red\">&nbsp;*&nbsp;</font>", 
                   "<msg:message code='attribute.code'/><font color=\"red\">&nbsp;*&nbsp;</font>",
                   "<msg:message code='attribute.isNotNull'/>",
                   "<msg:message code='attribute.isEnabled'/>",
                   "<msg:message code='attribute.isForQuery'/>",
                   "<msg:message code='attribute.pageShowType'/>",
                   "<msg:message code='attribute.validationRuleId'/>",
                   "<msg:message code='attribute.sortNumber'/>",
                   "<msg:message code='attribute.defaultValue'/>",
                   "<img id='addRow' src='${base}/${skin}/images/icon/Add.png' width='20' height='20' style='cursor:pointer;'/>"
                   ],
        colModel: [
                   {name: 'name',index:'name',width:'12%',align:'center',hidden: false,sortable:false},
                   {name: 'code',index:'code',width:'12%',align:'center',hidden: false,sortable:false},
                   {name: 'isNotNull',index:'isNotNull',width:'6%',align:'center',hidden: false,sortable:false},
                   {name: 'isEnabled',index:'isEnabled',width:'6%',align:'center',hidden: false,sortable:false},
                   {name: 'isForQuery',index:'isForQuery',width:'6%',align:'center',hidden: false,sortable:false},
                   {name: 'pageShowType',index:'pageShowType',width:'8%',align:'center',hidden: false,sortable:false},
                   {name: 'validationRuleId',index:'validationRuleId',width:'8%',align:'center',hidden: false,sortable:false},
                   {name: 'sortNumber',index:'sortNumber',width:'8%',align:'center',hidden: false,sortable:false},
                   {name: 'defaultValue',index:'defaultValue',width:'6%',align:'center',hidden: true,sortable:false},
                   {name: 'operate',index:'operate',width:'2%',align:'center',hidden: false,sortable:false}
                   ],
        height:<msg:message code='jqgrid.height.250'/>,
        autowidth: true,
        shrinkToFit:true,
        toolbar: [true,"bottom"]
    });
    
    //返回按钮部分
    /*
    var $save = $("<a></a>").attr("href","javascript:void();")
	  .attr("id","save")
	  .append($("<img/>").attr("src","${base}/${skin}/images/icon/save.png")
	  .attr("width","18").attr("height","18").attr("border","0")
	  .attr("border","0").attr("align","absmiddle"))
	  .append("<msg:message code="button.save"/>");    
    var $back = $("<a></a>").attr("href","${base}${elf:parentcontroller(requestScope,sessionScope)}")
	  .attr("id","back")
	  .append($("<img/>").attr("src","${base}/${skin}/images/icon/back.png")
	  .attr("width","18").attr("height","18").attr("border","0")
	  .attr("border","0").attr("align","absmiddle"))
	  .append("<msg:message code="button.back"/>");        
	$("#t_attributeList").append("&nbsp;&nbsp;").append(
			$("<span></span>")
			.attr("class","jqgridContainer")
			.append($save)
			.append("&nbsp;&nbsp;")
			.append($back)
		);
	$("#t_attributeList #save").click(function(){
		$("form#attribute").submit();
	});
	*/
    $('#addRow').click(function(){
    	//排序
    	var sortNumber ="";
	    sortNumber += "<a href='javascript:void(0);' id='" + (parseInt(count)+1)+ "' onclick='moveUp(this);'><img src='${base}/common/skin/images/icon/up.png' width='20' height='20'/></a>";
		sortNumber += "<a href='javascript:void(0);' id='" + (parseInt(count)+1)+ "' onclick='moveDown(this);'><img src='${base}/common/skin/images/icon/down.png' width='20' height='20'/></a>";
		sortNumber += "<input type='hidden' id='sortNumber"+(parseInt(count)+1)+"' maxlength='25' style='width:90%' value='"+(parseInt(count)+1)+"' />";
    	var data={
    			name:"<input type='text' id='name"+(parseInt(count)+1)+"' maxlength='25' onclick=\"changeStyle('name"+(parseInt(count)+1)+"');\" onkeyup=\"checkname(event,this,'edit');\" onfocusout=\"checkname(event,this,'paste');\" onpropertychange=\"checkname(event,this,'edit');\" oninput=\"checkname(event,this,'edit');\" style='width:80%;'/>",
    			code:"<input type='text' id='code"+(parseInt(count)+1)+"' maxlength='30' onclick=\"changeStyle('code"+(parseInt(count)+1)+"');\" onkeyup=\"checkcode(event,this,'edit');\" onfocusout=\"checkcode(event,this,'paste');\" onpropertychange=\"checkcode(event,this,'edit');\" oninput=\"checkcode(event,this,'edit');\" style='width:80%;'/>",
    			isNotNull:"<input type='checkbox' id='isNotNull"+(parseInt(count)+1)+"' checked='true' />",
    			isEnabled:"<input type='checkbox' id='isEnabled"+(parseInt(count)+1)+"' checked='true'/>",
    			isForQuery:"<input type='checkbox' id='isForQuery"+(parseInt(count)+1)+"' checked='true'/>",
    			pageShowType:"<select id='pageShowType"+(parseInt(count)+1)+"' onchange=\"changeShowType('"+(parseInt(count)+1)+"');\">"+pageShowType+"</select><input type='button' id='img"+(parseInt(count)+1)+"' value='..' disabled='disabled' onclick='setValue("+(parseInt(count)+1)+")'/>",
    			validationRuleId:"<select id='validationRuleId"+(parseInt(count)+1)+"'>"+validationRuleId+"</select>",
    			sortNumber:sortNumber,
    			defaultValue:"<input type='hidden' id='defaultValue"+(parseInt(count)+1)+"' />",
    			operate:"<a href='javascript:void(0);' id='" + (parseInt(count)+1) + "' onclick='delRow(this);'><img src='${base}/${skin}/images/icon/reduce.png' width='20' height='20'/></a>"
				};
		jQuery("#attributeList").jqGrid('addRowData',(parseInt(count)+1),data);
		count++;
		$("#name"+count).poshytip({
			content:namePrompt,
			className: 'tip-violet',
			showOn: 'focus',
			alignTo: 'target',
			alignX: 'right',
			alignY: 'center',
			offsetX: 5,
			hideAniDuration:100
		});
		$("#code"+count).poshytip({
			content:codePrompt,
			className: 'tip-violet',
			showOn: 'focus',
			alignTo: 'target',
			alignX: 'right',
			alignY: 'center',
			offsetX: 5,
			hideAniDuration:100
		});
		
	});
	
	 //数据回显
      <c:forEach var="attribute" items="${attributeList}" varStatus='st'>
    	//行号
		attrId = parseInt('${attribute.sortNumber}');
    	var isNotNull = "";
    	if("${attribute.isNotNull}" == 1){
    		isNotNull = "<input type='checkbox' id='isNotNull"+attrId+"' checked='true' />";
    	}else{
    		isNotNull = "<input type='checkbox' id='isNotNull"+attrId+"'  />";
    	}
    	var isEnabled = "";
    	if("${attribute.isEnabled}" == 1){
    		isEnabled = "<input type='checkbox' id='isEnabled"+attrId+"' checked='true'/>";
    	}else{
    		isEnabled = "<input type='checkbox' id='isEnabled"+attrId+"' />";
    	}
    	var isForQuery = "";
    	if("${attribute.isForQuery}" == 1){
    		isForQuery = "<input type='checkbox' id='isForQuery"+attrId+"' checked='true'/>";
    	}else{
    		isForQuery = "<input type='checkbox' id='isForQuery"+attrId+"' />";
    	}
    	//var sortNumber = "<input type='text' id='sortNumber"+attrId+"' value='"+"${attribute.sortNumber}"+"' maxlength='5' style='width:35px;' onkeyup=\"checkNumber(event,this,'edit');\" onfocusout=\"checkNumber(event,this,'paste');\" onpropertychange=\"checkNumber(event,this,'edit');\" oninput=\"checkNumber(event,this,'edit');\"/>";
    	
    	 //排序
		var number ="";
	    number += "<a href='javascript:void(0);' id='" + attrId+ "' onclick='moveUp(this);'><img src='${base}/common/skin/images/icon/up.png' width='20' height='20'/></a>";
		number += "<a href='javascript:void(0);' id='" + attrId+ "' onclick='moveDown(this);'><img src='${base}/common/skin/images/icon/down.png' width='20' height='20'/></a>";
		number += "<input type='hidden' id='sortNumber"+attrId+"' maxlength='25' style='width:90%' value='"+attrId+"' />";
    	
    	
    	var pageShowType_db = "";
    	<c:forEach var="showType" items="${pageShowTypeList}">
	    	if("${attribute.pageShowType == showType.key}" == "true"){
	    		pageShowType_db += "<option value=\"${showType.key}\" selected='selected'>${showType.value}</option>";
	    	}else{
	    		pageShowType_db += "<option value=\"${showType.key}\" >${showType.value}</option>";
	    	}
    	</c:forEach>
    	var defaultValue_db = "";
    	if("${attribute.pageShowType== 1}" == "true" || "${attribute.pageShowType== 5}" == "true"  || "${attribute.pageShowType== 6}" == "true" ){
    		defaultValue_db = "<img id='img"+attrId+"' src='${base}/${skin}/images/icon/detail.png'  width='20' height='20' style='cursor:pointer;'/><input type='hidden' id='defaultValue"+attrId+"' value=\"${attribute.defaultValue}\" />";
    	}else{
    		defaultValue_db = "<img id='img"+attrId+"' src='${base}/${skin}/images/icon/detail.png' onclick=\"setValue('"+attrId+"')\"  width='20' height='20' style='cursor:pointer;'/><input type='hidden' id='defaultValue"+attrId+"' value=\"${attribute.defaultValue}\" />";
    	}
    	
    	var validationRuleId_db = "";
    	<c:forEach var="validationRule" items="${validationRuleList}">
	    	if("${attribute.validationRuleId== validationRule.id}" == "true"){
	    		validationRuleId_db += "<option value=\"${validationRule.id}\" selected='selected'>${validationRule.name}</option>";
			}else{
				validationRuleId_db += "<option value=\"${validationRule.id}\">${validationRule.name}</option>";
			}
   		</c:forEach>
    
		var operate = "<a href='javascript:void(0);' id='"+attrId+"' onclick='delRow(this);'><img src='${base}/${skin}/images/icon/reduce.png' width='20' height='20'/></a>";

	  	var data_db={
				name:"<input type='text' id='name"+attrId+"' maxlength='25' onclick=\"changeStyle('name"+attrId+"');\" value=\"${attribute.name}\" onkeyup=\"checkname(event,this,'edit');\" onfocusout=\"checkname(event,this,'paste');\" onpropertychange=\"checkname(event,this,'edit');\" oninput=\"checkname(event,this,'edit');\" style='width:80%;' />",
				code:"<input type='text' id='code"+attrId+"' maxlength='30' onclick=\"changeStyle('code"+attrId+"');\" value=\"${attribute.code}\" onkeyup=\"checkcode(event,this,'edit');\" onfocusout=\"checkcode(event,this,'paste');\" onpropertychange=\"checkcode(event,this,'edit');\" oninput=\"checkcode(event,this,'edit');\" style='width:80%;' />",
				isNotNull:isNotNull,
				isEnabled:isEnabled,
				pageShowType:"<select id='pageShowType"+attrId+"' onchange=\"changeShowType('"+attrId+"');\">"+pageShowType_db+"</select><input type='button' id='img"+attrId+"' value='..' disabled='disabled'/ onclick='setValue("+attrId+")'>",
				defaultValue:defaultValue_db,
				validationRuleId:"<select id='validationRuleId"+attrId+"'>"+validationRuleId_db+"</select>",
				isForQuery:isForQuery,
				sortNumber: number,
				operate:operate
			};
    	jQuery("#attributeList").jqGrid('addRowData',attrId,data_db);
    	count = attrId;
		if("${attribute.pageShowType}" == "2" || "${attribute.pageShowType}" == '3'  || "${attribute.pageShowType}" == '4'|| "${attribute.pageShowType}" == '7'|| "${attribute.pageShowType}" == '8' ){
			$("#validationRuleId"+attrId).attr({"disabled":"disabled"});
			$("#img"+attrId).removeAttr("disabled");
			//$("#img"+attrId).onclick =function onclick(){ setValue(''+attrId);};
			//document.getElementById('img'+index).onclick =function onclick(){ setValue(''+index);};
		}
    		
   		$("#name"+attrId).poshytip({
   			content:namePrompt,
   			className: 'tip-violet',
   			showOn: 'focus',
   			alignTo: 'target',
   			alignX: 'right',
   			alignY: 'center',
   			offsetX: 5,
   			hideAniDuration:100
   		});
   		$("#code"+attrId).poshytip({
   			content:codePrompt,
   			className: 'tip-violet',
   			showOn: 'focus',
   			alignTo: 'target',
   			alignX: 'right',
   			alignY: 'center',
   			offsetX: 5,
   			hideAniDuration:100
   		});
	</c:forEach>
    //数据回显结束
	
	//创建失败提示信息显示
	<c:forEach var="errorMessage" items="${result}">
		var message = '${errorMessage.msg}';
		var field = '${errorMessage.field}';
		$("#"+field).poshytip('update',message);
		$("#"+field).poshytip('show');
	</c:forEach>
	
});

function checkNumber(evt,object,flag){
	var key = window.event?event.keyCode:evt.which;
	var re = /^[0-9]*[1-9][0-9]*$/;
	if(key != 8){
		var value = $(object).val();
		if(!re.test(value)){
			if("paste" == flag){
				$(object).val("");
			}else{
				if(value != null && value != ""){
					$(object).val(value.substring(0,value.length-1));
				}
			}
		}
	}
}

//行上移
function moveUp(obj){
	//获取当前对象
	var current=$(obj).parent().parent();
    var prev=current.prev(); 
    var currentId = $(current).attr("id");
    var prevId = $(prev).attr("id");
    var currSort = $('#sortNumber'+currentId).val();
    var prevSort = $('#sortNumber'+prevId).val();
	if(prev && prev.attr("class") != 'jqgfirstrow'){
		current.insertBefore(prev);
		$('#sortNumber'+currentId).val(prevSort);
		$('#sortNumber'+prevId).val(currSort);
	}	
}

//行下移
function moveDown(obj){
	var current=$(obj).parent().parent();
	var next=current.next();
	var currentId = $(current).attr("id");
    var nextId = $(next).attr("id");
    var currSort = $('#sortNumber'+currentId).val();
    var nextSort = $('#sortNumber'+nextId).val();
	if(next && next.attr("role") == 'row'){
		current.insertAfter(next);
		$('#sortNumber'+currentId).val(nextSort);
		$('#sortNumber'+nextId).val(currSort);
	}
}


//删除行
function delRow(o){
  	var rowid = $(o).attr("id");
	jQuery("#attributeList").jqGrid('delRowData',rowid);  
}
function getArrayCount(temp,array){
	for(var i=0; i < array.length; i++){
		if(temp == array[i]){
			return true;
		}
	}
}

function formSubmit(){
	var attributeString = "";
	var errorCount = 0;
	var ids = jQuery("#attributeList").jqGrid('getDataIDs');
	if(ids.length == 0){
		window.parent.Boxy.alert("<msg:message code='attribute.isNotExist'/>", null, {title: "<msg:message code='info.prompt'/>"});
		return false;
	}
	var names = new Array();
	var codes = new Array();
	for(var i=0;i < ids.length;i++){
		var id = ids[i];
		var name = $("#name"+id).val();
		var code = $("#code"+id).val();
		var isNotNull = $("#isNotNull"+id).is(':checked')==true?1:0;
		var isEnabled = $("#isEnabled"+id).is(':checked')==true?1:0;
		var pageShowType = $("#pageShowType"+id).val();
		var defaultValue="";
		if($("#pageShowType"+id).val()==1 || $("#pageShowType"+id).val()==5 || $("#pageShowType"+id).val()==6){
		}else{
			defaultValue = $("#defaultValue"+id).val();
		}
		var validationRuleId = "";
		if($("#pageShowType"+id).val()==1 || $("#pageShowType"+id).val()==5){
			validationRuleId = $("#validationRuleId"+id).val();
		}
		if(validationRuleId==null||typeof(validationRuleId)=='undefined'){
			validationRuleId = '';
		}
		var isForQuery = $("#isForQuery"+id).is(':checked')==true?1:0;
		var sortNumber = $("#sortNumber"+id).val();
		if(name == ""){
			$("#name"+id).poshytip('show');
			return false;
		}
		if(code == ""){
			$("#code"+id).poshytip('show');
			return false;
		}else{
			if(getArrayCount(name,names)){
				namePrompt = "<msg:message code='attribute.name.already'/>";
				$("#name"+id).poshytip('update',namePrompt);
				$("#name"+id).poshytip('show');
				return false;
			}else{
				names.push(name);
			}
			if(getArrayCount(code,codes)){
				codePrompt = "<msg:message code='attribute.code.already'/>";
				$("#code"+id).poshytip('update',codePrompt);
				$("#code"+id).poshytip('show');
				return false;
			}else{
				codes.push(code);
			}
		}
		attributeString += name+"#";
		attributeString += code+"#";
		attributeString += isNotNull+"#";
		attributeString += isEnabled+"#";
		attributeString += isForQuery+"#";
		attributeString += pageShowType+"#";
		attributeString += defaultValue+"#";
		attributeString += validationRuleId+"#";
		attributeString += sortNumber;
		if(i != ids.length-1)
			attributeString += "@";
	}
	$("#attributeString").val(attributeString);
	if(errorCount>0){
		return false;
	}
	return true;
 }
function changeStyle(object){
	if(object.indexOf("code")<0){
		namePrompt = "<msg:message code='attribute.name.input'/>";
		$("#"+object).poshytip('update',namePrompt);
	}else{
		codePrompt = "<msg:message code='attribute.code.input'/>";
		$("#"+object).poshytip('update',codePrompt);
	}
}
function changeShowType(index){
	if($("#pageShowType"+index).val()==1 || $("#pageShowType"+index).val()==5 || $("#pageShowType"+index).val()==6){
		$("#img"+index).attr("disabled","disabled");
	}else{
		$("#validationRuleId"+index).val("0");
		$("#img"+index).removeAttr("disabled");
	}
	$("#defaultValue"+index).val("");
	if($("#pageShowType"+index).val()==1 || $("#pageShowType"+index).val()==5 ){
		$('#validationRuleId'+index).removeAttr("disabled"); 
	}else{
		$('#validationRuleId'+index).attr("disabled","disabled"); 
	}
}

//调用数据来源方法
var attributeValueBoxy;
var ds;
function setValue(index){ 
	var valueString = $("#defaultValue"+index).val();
	var type = "1";
	var value = "";
	if(valueString!= null && valueString != ""){
		type = valueString.split("_")[0];
		value = valueString.split("_")[1];
	}
	//var url = "${base}/product/attribute/setValue?c=${productAttributeGroup.c}&id="+index+"&type="+type+"&value="+value;
	//ds = new DataSource(index,url,"","<msg:message code='add.option.value'/>","<msg:message code='button.colse'/>");
	
	//var html = "<table cellSpacing=1 cellPadding=0 border=0 style='padding:0;'><tr><td><iframe width='450px' height='100px' id='valueFrame' src='${base}/config/reportconfig/setValue?c=${report.c}&id="+index+"&type="+type+"&value="+value+"' frameborder='0'  style='padding:0px;'></iframe></td></tr></table>";
	var html = "<table cellSpacing=1 cellPadding=0 border=0 style='padding:0;'><tr><td><iframe width='450px' height='100px' id='valueFrame' src='${base}/product/attribute/setValue?c=${productAttributeGroup.c}&id="+index+"&type="+type+"&value="+value+"' frameborder='0'  style='padding:0px;'></iframe></td></tr></table>";
	try{	
		if(top.attributeValueBoxy == null){
			top.attributeValueBoxy = new window.Boxy(html,{title:"<msg:message code='add.option.value'/>",closeText:"[<msg:message code='button.colse'/>]"});
			boxyLocation(index);
		}else{
				top.attributeValueBoxy.setContent(html);
				top.attributeValueBoxy.show();
				boxyLocation(index);
			}
	}catch(err){
		top.attributeValueBoxy = new window.Boxy(html,{title:"<msg:message code='add.option.value'/>",closeText:"[<msg:message code='button.colse'/>]"});
		boxyLocation(index);
	}
	$(".boxy-wrapper").css("width","480px");
	boxyLocation(index);
}
function boxyLocation(index){
	var offset = $('#img'+index).offset();
	var boxyX = offset.left-top.attributeValueBoxy.getSize()[0];
	var boxyY;
	if((offset.top+25+top.attributeValueBoxy.getSize()[1])> $(window).height()){
		boxyY = $(window).height() - top.attributeValueBoxy.getSize()[1];
	}else{
		boxyY = offset.top+25;
	}
	top.attributeValueBoxy.moveTo(boxyX,boxyY);
}
function attributeValueBoxySubmit(id,valueString){
	$("#defaultValue"+id).val(valueString);
}
//控制中文
function checkname(evt,object,flag){
	var key = window.event?event.keyCode:evt.which;
	var re = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
	if(key != 8){
		var value = $(object).val();
		if(!re.test(value)){
			if("paste" == flag){
				$(object).val("");
			}else{
				if(value != null && value != ""){
					$(object).val(value.substring(0,value.length-1));
				}
			}
		}
	}
}
//控制英文
function checkcode(evt,object,flag){
	var key = window.event?event.keyCode:evt.which;
	var re = /^\w+$/;
	if(key != 8 && $(object).val() != "<msg:message code='please.input'/>" && $(object).val() != "<msg:message code='engilshname.already'/>"){
		var value = $(object).val();
		if(!re.test(value)){
			if("paste" == flag){
				$(object).val("");
			}else{
				if(value != null && value != ""){
					$(object).val(value.substring(0,value.length-1));
				}
			}
		}
	}
}
</script>
<style type="text/css">
	.prompt{
		color:red;
	}
	.normal{
		color:black;
	}
</style>
</head>
<body class="skinMain">
<form:form method="post" action="save" commandName="productAttributeGroup" id="attribute" onsubmit="return formSubmit();">
<input type="hidden" name="c" value="${productAttributeGroup.c}"/>
<input type="hidden" name="page" value="1"/>
<input type="hidden" name="prompt" value="name" />
<form:hidden path="attributeString"/>
<table cellpadding="0" cellspacing="1" border="0" width="100%" class="skinMain">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" class="skinLayout">
				<tr>
					<td align="right" width="25%" class="skinLayout_info"><msg:message code='attributegroup.name'/><msg:message code="system.common.sign.colon"/></td>
					<td align="left" width="75%" class="skinLayout_lable">
						<form:input path="name" maxlength="20" onkeyup="checkname(event,this,'edit');" onfocusout="checkname(event,this,'paste');" onpropertychange="checkname(event,this,'edit');" oninput="checkname(event,this,'edit');"/><font color="red">&nbsp;*&nbsp;</font>
					</td>
					
				</tr>	
				<tr>
					<td align="right" width="25%" class="skinLayout_info"><msg:message code='attributegroup.code'/><msg:message code="system.common.sign.colon"/></td>
					<td align="left" width="75%" class="skinLayout_lable">
						<form:input path="code" maxlength="20" onkeyup="checkcode(event,this,'edit');" onfocusout="checkcode(event,this,'paste');" onpropertychange="checkcode(event,this,'edit');" oninput="checkcode(event,this,'edit');"/><font color="red">&nbsp;*&nbsp;</font>
					</td>
				</tr>	
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="attributeList"><tr><td>&nbsp;</td></tr></table>
			<div id="pagered"></div>
		</td>
	</tr>
	<tr>
		<td>
			<div class="toolbar">
				<div class="left"></div>
				<div class="center">
					<input type="submit" class="btn2" id="btnok" value="<msg:message code='button.save'/>"  />
					<input type="button" class="btn2" onclick="window.location.href='${base}${elf:parentcontroller(requestScope,sessionScope)}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
				</div>
				<div class="right"></div>
			</div>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>