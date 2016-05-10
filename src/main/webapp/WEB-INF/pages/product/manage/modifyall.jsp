<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">

var ids = "";
var verifyString ="";
var attributeList;
var attributeSize;
var noticeDelete = "<msg:message code='uploadify.delete'/>";
var noticeDetail = "<msg:message code='uploadify.detail'/>";
var pleaseSelect = "<msg:message code='product.attribute.please.select'/>";
var oldAttrId = '${product.investCategoryId}';//上次提交属性组id
var editorNUM = 0;
var textareaArray = new Array();
var loadPIMG = false;// 是否有产品图片

var aa;

//气泡验证
var valid;

function changeType(id){
	var layers =  top.layer.load(0,2);
	//如果产品类型不为空，加载对应属性
	if(id !="" || id!=null){
		// 设置属性组ID
		$("#attributeGroupId").val(id);
		//根据类型值查询所包含属性 并生成页面代码
		$.post("getAttributeListValue.json", { c: "${c}", id: "${product.id}",investCategoryId :id },
			function(data) {

			//清除当前已加载的属性
			$("#prdTable").empty();
			// 当前产品属性所包含的属性List
			var attribute = data.attributeList;
			attributeList = data.attributeList;
			//数组长度
			attributeSize = data.listSize;
			for(var i=0;i<attributeSize;i++){
				var showName ="";
				var id = attributeList[i].id;  //属性ID
				var name = attributeList[i].name;  //中文名
				var showType = attributeList[i].pageShowType; //显示类型
				var isRequired = attributeList[i].isNotNull; //是否必填
				var isEnabled = attributeList[i].isEnabled; //是否启用
				var checkValue = attributeList[i].defaultValue; //下拉框、单选、多选框值显示
				var attributeValue = attributeList[i].attributeValue; //属性对应的值
				var verify = attribute[i].validationRuleValue; //验证规则
				if(isEnabled != 0){
					showName += "<tr><td width='15%' align='right' class='skinLayout_info' style='border-top:1;'>"+name+"<msg:message code='system.common.sign.colon'/></td>";
					showName +="<td width='85%'  style='border-top:1;' class='skinLayout_lable'>";
					var show =showTypeChoose(showType,isRequired,id,checkValue,attributeValue,i);
					
					showName +=show;
					showName +="</td></tr>";
					if(i != 0)
						ids+=",";
					ids +=id;
					if(isRequired==1){
						verifyString +=id+"#"+isRequired+"#"+verify+"#"+name+"@";
					}	
					$(showName).appendTo($("#prdTable"));
					
				}	
			} 
		}).success(function() {
			initAttributeEditor();
			initUploader();
		});
	}
	top.layer.close(layers);
	// 执行完成释放保存按键
	$("#btnok").attr("disabled",false);
}
function showTypeChoose(showType,isRequired,id,checkValue,prdValue,rowCount){
	var show = "";
	if(showType){
		//文本框
		if(showType == 1){
			show +="<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/><input type =text id= 'dt"+id+"'  name='productAttributeValueList["+rowCount+"].value'  value='"+prdValue+"'/>";
		}
		//下拉框
		if(showType == 2){
			show= "<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/>";
			show+= "<select  id= 'dt"+id+"' name='productAttributeValueList["+rowCount+"].value'>"; 
			show+="<option value=''>"+pleaseSelect+"</option>";
			if(checkValue!=null && checkValue !=''){
				var chkValue = checkValue.split("_")[1].split("*");
				for(var i=0;i<chkValue.length;i++){
					var value = chkValue[i].split(":");
					if(prdValue==value[1]){
						show+="<option value='"+value[1]+"' selected >"+value[0]+"</option>";	     
					}else{
						show+="<option value='"+value[1]+"'>"+value[0]+"</option>";	    
					}
				}
			}
			show+="</select>"  						
		}
		//单选框
		if(showType == 3){
			show ="<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/>";
			if(checkValue!=null && checkValue !=''){
			var chkValue = checkValue.split("_")[1].split("*");
				for(var i=0;i<chkValue.length;i++){
					var value = chkValue[i].split(":");
					if(prdValue==value[1]){
						show += " <input type='radio' value='"+value[1]+"'id= 'dt"+id+"' name='productAttributeValueList["+rowCount+"].value' checked>"+value[0]+"";
					}else{
						show += " <input type='radio' value='"+value[1]+"'id= 'dt"+id+"' name='productAttributeValueList["+rowCount+"].value'>"+value[0]+"";
					}
				}
			}
		}
		//复选框
		if(showType == 4){
			show ="<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/>";
			if(checkValue!=null && checkValue !=''){
				var chkValue = checkValue.split("_")[1].split("*"); //属性中所包含的所有选项
				var prdCkb = prdValue.split("_");	//产品值中保存的选项
				for(var i=0;i<chkValue.length;i++){
					var value = chkValue[i].split(":");
					var str = "";
					for(var j=0;j<prdCkb.length;j++){
						if(prdCkb[j]==value[1]){
							show += " <input type='checkbox' value='"+value[1]+"' id= 'dt"+id+"'  name='productAttributeValueList["+rowCount+"].value' checked='checked'>"+value[0]+"";
							str = prdCkb[j];
						}
					}
					if(str==""){
						show += " <input type='checkbox' value='"+value[1]+"' id= 'dt"+id+"'  name='productAttributeValueList["+rowCount+"].value'>"+value[0]+"";
					}
				}		
			}
		}
		//文本域
		if(showType==5){
			show ="<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/>";
			show +="<textarea  id= 'dt"+id+"' name='productAttributeValueList["+rowCount+"].value' style='font-size:12px;width:600px;height='450px;'>"+prdValue+"</textarea>";
		}
		//日期
		if(showType == 6){
			show ="<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/>";
			show+="<input id= 'dt"+id+"' name='productAttributeValueList["+rowCount+"].value' type='text' onclick='WdatePicker()' class='Wdate' readonly='readonly' value='"+prdValue+"' >"
		}
		//隐藏域
		if(showType == 8){
			show ="<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/>";
			show+="<input id= 'dt"+id+"'   name='productAttributeValueList["+rowCount+"].value' type='hidden'  value='"+prdValue+"' >"
		}
		//富文本
		if(showType == 9){
			show ="<input type='hidden' name='productAttributeValueList["+rowCount+"].attributeId' value='"+id+"'/>";
			show +="<textarea id= 'dt"+id+"' name='productAttributeValueList["+rowCount+"].value' style='width:825px;height:250px;'>"+$("<div></div>").html(prdValue).text()+"</textarea>";
			textareaArray.push(id);
			editorNUM++;
		}
		//是否必填 0为不验证
		if(isRequired !=0 && isRequired=="1"&&showType!=7&&showType!=8){
			show += "<font color='red'>&nbsp;*&nbsp;</font>";
		}
		show += "&nbsp;<span id='s_"+id+"' style='color:red;'></span>";
	}
	return show;
}

	//页面编辑器
	var editor = "";
	var editor_2 = "";
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="description"]',{
		uploadJson : '${base}/product/manage/kindeditor/uploadimg',
		fileManagerJson : '${base}/product/manage/kindeditor/uploadimg',
		allowFileManager: true
		});
		//editor_2 = K.create('textarea[name="profile"]',{
		//uploadJson : '${base}/product/manage/kindeditor/uploadimg',
		//fileManagerJson : '${base}/product/manage/kindeditor/uploadimg',
		//allowFileManager: true
		//});
	});
	
	$(document).ready(function() {

		// 属性未加载完成前禁用保存
		$("#btnok").attr("disabled",true);
		// 页面验证
		var options = {
			event:'focus',
			ajax:{
				url:'${base}/product/manage/create_valid.json'
			},
			validStatus:function(status){
				if (status){
					var textarea = $("<div></div>").text($("#description").val()).html();
					$("#description").val(textarea);
					formSubmit();
				}
			}
		};
		$("form #btnok").valid(options);
		valid = new $.Valid(options);
		/**
		$("form #btnok").click(function(){
			var flag = formvalid();
			if(flag == true){
				formSubmit();
			}
		});*/
		// 居间公司
		$("#providerId").val("${product.providerId}");
		// 切换产品属性
		changeType("${product.investCategoryId}");
		// 切换产品销售状态
		changeProductType('${product.type}');
		// 最大募集金额默认值
		initLimitDom("maxTotalShare","${product.maxTotalShare}");
		changeDeadlineType("${product.deadlineType}");
		changeProfitType("${product.profitType}");
		// 删除图片
		$("#delImg").click(function(){
			if(!loadPIMG){
				return false;
			}
			$("#fileDel").html("");
			$("#productImage").val("");
		});
		
	});
	
	// 初始不限输入控件
	function initLimitDom(obj,dev){
		var limit = $("#in_"+obj);
		var unlimit = $("#un_"+obj);
		if(dev != '' && (dev == '-1' || dev == '-1.0' || dev == '-1.000000')){
			unlimit.attr("checked",true);
			limit.attr("disabled",true);
		}else{
			limit.val(dev);
			unlimit.attr("name","");
		}	
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
	
	function tab(tag){
		if(tag!=3){
			$(".tip-violet").remove();
		}
		for(var i = 1 ; i <= 4 ; i++){
			if(i == tag){
				$("#tab_"+i).addClass("selected");
				$("#div_"+i).css("display","block");
			}else{
				$("#tab_"+i).removeClass("selected");
				$("#div_"+i).css("display","none");
			}
		}
	}

	
	var editorArray = new Array();
	
	function initAttributeEditor(){
		for(w=0;w<textareaArray.length;w++){
			var id = textareaArray[w];
			if(typeof(id) != 'undefined'){
				var editor_w = KindEditor.create('#dt'+id,{
					uploadJson : '${base}/product/manage/kindeditor/uploadimg',
					fileManagerJson : '${base}/product/manage/kindeditor/uploadimg',
					allowFileManager: true
				});
				editorArray[w] = editor_w;
			}	
		}
	}
	
	function initUploader(){
		$("#uploadify").uploadify({
			'swf'            : '${base}/common/flash/uploadify/uploadify.swf',//指定uploadify.swf路径   
			'uploader'       : '${base}/product/manage/uploadfile;jsessionid=<%=session.getId()%>',  
			'buttonClass'    : 'btn2',
			'queueID'        : 'fileQueue',
			'sizeLimit'		 : 500000 ,     //这里是文件最大值 byte
			'auto'           : false,//是否允许同时上传多文件，默认false   
			'multi'          : false,//是否自动上传，即选择了文件即刻上传。  
			'fileTypeDesc'   : "图片文件",               
			'fileTypeExts'   : "*.gif; *.jpg; *.png;*.jpeg;*.bmp;*.tga;*.pcx;*.tiff", //控制可上传文件的扩展名，启用本项时需同时声明fileDesc           		
			'buttonText'     : '<msg:message code='info.prompt.select'/>',   
			'width'          : 60,
			'height'         : 24,
			'queueSizeLimit' : 1,
			'onInit'      	 : function() {
				var pi = "${product.productImage}";
				if(pi != ""){
					$("#delImg").css("color","#000");
					$("#fileDel").html("<img height='100' weight='100' src='${ftpUrl}${product.productImage}'>");
					loadPIMG = true;
				}else{
					$("#delImg").css("color","#CCC");
				}
				
			},
			'onUploadSuccess': function (file,data,response) {	
				if(response==true){
					$("#fileDel").html("<img height='100' weight='100' src='${ftpUrl}" + data + "'>");
					$("#productImage").val(data);
					loadPIMG = true;
					$("#delImg").css("color","#000");
				}else{
					   top.layer.msg("<msg:message code='uploadify.fileFail'/>");
				}
			} 
		});
	}
	
	function formvalid(){
		var flag = false;
		var form = $("#product").serializeArray();
		console.log(form);
		
		var pname = $('#name').val();
		var sname = $('#shortName').val();
		var type = $('#type').val();
		var closeType = $('#closeType').val();
		var orgTypeId = $('#orgTypeId').val();
		var raiseStartTime = $('#raiseStartTime').val();
		var raiseFinishTime = $('#raiseFinishTime').val();
		var beginningShare = $('#beginningShare').val();
		var incrementalShare = $('#incrementalShare').val();
		var minTotalShare = $('input[name="maxTotalShare"]').val();
		
		var profitType = $('#profitType').val();
		var minProfitRatio = $('#minProfitRatio').val();
		var maxProfitRatio = $('#maxProfitRatio').val();
		
		var deadlineType = $('#deadlineType').val();
		var minDeadline = $('#minDeadline').val();
		var maxDeadline = $('#maxDeadline').val();
		
		
	}
			
	function formSubmit(){
		editor.sync();
		//editor_2.sync();
		
		for(m=0;m<editorArray.length;m++){
			var eobj = editorArray[m];
			if(typeof(eobj) != 'undefined'){
				eobj.sync();
			}
		}

		var errorCount = 0;
		var attributeGroupId = $("#attributeGroupId").val();

		if(attributeGroupId!=null && attributeGroupId != 0 && attributeGroupId !=''){
			if(attributeList != null){
				for(var i=0;i<attributeSize;i++){
					var id = attributeList[i].id;
					var isEnabled = attributeList[i].isEnabled;
					if(isEnabled != 0){
						var pageShowType = attributeList[i].pageShowType;
						var isNotNull = attributeList[i].isNotNull;
						var verifyValue = attributeList[i].validationRuleValue;
						//提示内容
						var promptMessage = attributeList[i].promptMessage;
						if(typeof(verifyValue)=='undefined'){
							verifyValue ="";
							promptMessage = "";
						}
						var value = "";
						if(pageShowType == 3 || pageShowType == 4){
							$("input[name='"+id+"']:checked").each(function(){
									value += $(this).val()+"_";
							});
						   if(value.indexOf('_')>0)
							{
								value = value.substring(0,value.length-1);
							}else{
								value = "";
							}
						}else if(pageShowType == 9){
							if($("#dt"+id).val()==null||$("#dt"+id).val()==''){
								value = "";
							}else{
								value = $("#dt"+id).val();
								var tvalue = $("<div></div>").text(value).html();
								$("#dt"+id).val(tvalue);
							}
						}else{
							if($("#dt"+id).val()==null||$("#dt"+id).val()==''){
								value = " ";
							}else{
								value = $("#dt"+id).val();
							}
						}
						if(isNotNull == 1&&pageShowType!=7&&pageShowType!=8){
							
							value = value==null?"":$.trim(value);
							var re = new RegExp(verifyValue);
							//提示信息内容
							var message ="";
							if(value==""){
								tab(3);
								message = "<msg:message code='product.attribute.isNotNull'/>";
							}
							if(value!=''&&!re.test(value)){
								message = promptMessage;
							}
							if(value==""&&verifyValue!=''&&verifyValue!=0){
								message += ","+promptMessage;
							}
							//错误提示
							if(message!=''){
								tab(3);
								//console.log("---value---"+value+"|id="+id);
								$("#dt"+id).poshytip({
									content: message,
									className: 'tip-violet',
									showOn: 'focus',
									alignTo: 'target',
									alignX: 'right',
									alignY: 'center',
									offsetX: 5,
									hideAniDuration:100
								}).poshytip('show');
								errorCount++;
							}
						}
					}
				}
			}
		}
		if(errorCount>0){
			return false;
		}
		$("form#product").submit();
		return true;
	}

	
	// 修改产品类型
	function changeProductType(productType){
		// 跨市场货币基金启用产品销售状态
		if(productType == 2 || productType == 5){
			$('#sellStatus').attr("disabled",false);
		}else{
			$('#sellStatus').attr("disabled",true);
			$("#sellStatus option[value='1']").attr("selected",true);    
		}
		validOrNot();
	}
	
	// 设置不限输入控件
	function limitDom(obj,dev){
		var cklimit = $("#un_"+obj).attr("checked");
		var limit = $("#in_"+obj);
		var unlimit = $("#un_"+obj);
		
		if(cklimit){
			$("#maxTotalShareTip").hide();
			limit.attr("disabled",true);
			limit.val('');
			unlimit.attr("name",obj);
		}else{
			if(!(($("#type").val()==2||$("#type").val()==5) && $("#sellStatus").val()==5)){
				$("#maxTotalShareTip").show();
			}
			limit.attr("disabled",false);
			unlimit.attr("name","");
			if(dev != '-1' && dev != '-1.0' && dev != '-1.000000'){
				limit.val(dev);
			}
		}
	}
	
	// 设置收益类型
	function changeProfitType(type){
		if(type && type == 2){
			$("#profitType_min").show();
			$("#profitType_max").show();
		}else{
			$("#profitType_min").hide();
			$("#profitType_max").hide();
		}
		validOrNot();
	}
	
	// 设置期限类型
	function changeDeadlineType(type){
		if(type && type == 1){
			$("#deadlineDataType").attr("disabled",false);
			$("#deadlineDataType").show();
			
			$("#deadlineType_min").show();
			$("#deadlineType_max").show();
		}else{
			$("#deadlineDataType").attr("disabled",true);
			$("#deadlineDataType").hide();
			
			$("#deadlineType_min").hide();
			$("#deadlineType_max").hide();
		}
		validOrNot();
	}
	
	// 设置期限时间类型
	function changeDeadlineDataType(type){
		if(type && type == 1){
			$(".dl-tip").html("(月)");
		}else{
			$(".dl-tip").html("(日)");
		}
	}
	
	//判断是否验证
	function validOrNot(){
		$(".tip-common").prev().attr("class","valid-control");
		$(".tip-time").prev().attr("class","Wdate valid-control");
		$("#beginningShare").attr("class","valid-control");
		$("input[name='maxTotalShare']").attr("class","valid-control");
		valid.reset();
		$(".tip-common").show();
		$(".tip-time").show();
		$("#beginningShareTip").show();
		var cklimit = $("#un_maxTotalShare").attr("checked");
		if(!cklimit){
			$("#maxTotalShareTip").show();
		}
		if(($("#type").val()==2||$("#type").val()==5) && $("#sellStatus").val()==5){
			valid.remove($(".tip-common").prev());
			valid.remove($(".tip-time").prev());
			valid.remove($("input[name='maxTotalShare']"));
			$(".tip-common").prev().attr("class","");
			$(".tip-time").prev().attr("class","Wdate");
			$("input[name='maxTotalShare']").attr("class","");
			$(".tip-common").hide();
			$(".tip-time").hide();
			$("#maxTotalShareTip").hide();
			if($("#type").val()==2){
				valid.remove($("#beginningShare"));
				$("#beginningShare").attr("class","");
				$("#beginningShareTip").hide();
			}
		}
		if($("#profitType").val()==1){
			$("#minProfitRatio").attr("class","");
			$("#maxProfitRatio").attr("class","");
		}
		if($("#deadlineType").val()==2){
			$("#minDeadline").attr("class","");
			$("#maxDeadline").attr("class","");
		}
	}

	// 数字验证
	function checkNumber(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
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
	
	//设置机构类型
	function orgType(){
		var orgTypeId = $("select[name=providerId]").find("option:selected").attr("class");
		$("#orgTypeId").val(orgTypeId);
	}
</script>

</head>
<body class="skinMain">
	<ul id="tabnav">
		<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected"><msg:message code="product.tab.base"/></a></li>
		<li class="tab_3" ><a href="#" id="tab_3" onclick="tab(3)"><msg:message code="product.tab.attribute"/></a></li>
		<!--<li class="tab_4" ><a href="#" id="tab_4" onclick="tab(4)"><msg:message code="product.profile"/></a></li>-->
		<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)"><msg:message code="product.description"/></a></li>
	</ul>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<form:form method="post" action="save" commandName="product" id="product" >
					<input type="hidden" name="c" value="${c}"/>
					<input type="hidden" name="prompt" value="name"/>
					<input type="hidden" name="turnCode" value="${product_manage.code}"/>
					<input type="hidden" name="coverParam" value="1"/>
					<input type="hidden" name="id" value="${product.id}"/>
					<input type="hidden" name="productImage" id="productImage" value="${product.productImage}" />
					<input type="hidden" name="orgTypeId" id="orgTypeId" value="${product.orgTypeId}">
					
			<div id="div_1" >
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td width="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.provider.name"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<select name="providerId" id="providerId" class="valid-control" onchange="orgType()">
										<option value="">请选择</option>
										<c:forEach items="${providerList}" var="item">
											<option value="${item.id}" class="${item.companyType}">${item.name}</option>
										</c:forEach>
									</select><font color="red">&nbsp;*&nbsp;</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.name"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:input path="name" maxlength="100" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
								</td>
							</tr>
								<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.shortName"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:input path="shortName" maxlength="50" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.code"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:input path="code" maxlength="20" class="valid-control"/><font color="red">&nbsp;*&nbsp;</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.type"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:select path="type" htmlEscape="true" onChange="changeProductType($(this).val());" class="valid-control">
										<option value="">请选择</option>
										<form:options items="${productType}" /> 										
									</form:select><font color="red">&nbsp;*&nbsp;</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.sellStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:select path="sellStatus" onchange="validOrNot()">
										<form:option value="1">首次募集</form:option>	
										<form:option value="5">已成立</form:option>										
									</form:select><font color="red">&nbsp;</font>
								</td>
							</tr>
							<c:if test="${!empty product_manage_salertype}">
								<security:authorize ifAnyGranted="${product_manage_salertype.code}">
								<tr>
									<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.salerType"/><msg:message code="system.common.sign.colon"/></td>
									<td width="85%" class="skinLayout_lable">
										<form:select path="salerType">
											<form:option value="1">优财富</form:option>	
											<form:option value="2">非优财富</form:option>										
										</form:select><font color="red">&nbsp;</font>
									</td>
								</tr>
								</security:authorize>
							</c:if>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.closeType"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:select path="closeType" htmlEscape="true" items="${closeType}" />
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.raiseArea"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:select path="raiseArea">
										<option value="">请选择</option>
										<c:forEach items="${raiseAreaList}" var="item">
											<option value="${item.key}" <c:if test="${product.raiseArea==item.key}">selected</c:if>>${item.value}</option>
										</c:forEach>										
									</form:select><font color="red">&nbsp;&nbsp;</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.raiseStartTime"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<input type="text" id="raiseStartTime" name="raiseStartTime" value="${product.raiseStartTime}" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'raiseFinishTime\')||\'2050-01-01\'}'})" class="Wdate valid-control">&nbsp;<font color="red" class="tip-time">*</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.raiseFinishTime"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<input type="text" id="raiseFinishTime" name="raiseFinishTime" value="${product.raiseFinishTime}" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'raiseStartTime\')}',maxDate:'2050-01-01'})" class="Wdate valid-control">&nbsp;<font color="red" class="tip-time">*</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.beginningShare"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:input path="beginningShare" maxlength="30"  class="valid-control"/><font id="beginningShareTip" color="red">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.incrementalShare"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:input path="incrementalShare" maxlength="30" class="valid-control" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.minTotalShare"/><msg:message code="system.common.sign.colon"/></td>
									<td width="85%" class="skinLayout_lable">
									<form:input path="minTotalShare" class="valid-control" maxlength="30"  onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="tip-common">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxTotalShare"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<input name="maxTotalShare" class="valid-control"  id="in_maxTotalShare" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"  maxlength="30" />&nbsp;&nbsp;<input name="maxTotalShare" id="un_maxTotalShare" onclick="limitDom('maxTotalShare','${product.maxTotalShare}');" type="checkbox" value="-1" style="width:15px;height:15px;" class="valid-control">&nbsp;&nbsp;不限<font color="red" id="maxTotalShareTip">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.profitType"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:select path="profitType" onChange="changeProfitType(this.value);" >
										<form:option value="2"><msg:message code="product.profitType.fix"/></form:option>
										<form:option value="1"><msg:message code="product.profitType.unfix"/></form:option>										
									</form:select>
									<font color="red">&nbsp;</font>
								</td>
							</tr>
							<tr id="profitType_min">
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.minProfitRatio"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:input path="minProfitRatio" class="valid-control" maxlength="30"  onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="tip-common">&nbsp;*</font>
								</td>
							</tr>
							<tr id="profitType_max">
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxProfitRatio"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
								<form:input path="maxProfitRatio" class="valid-control" maxlength="30"  onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="tip-common">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.deadlineType"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:select path="deadlineType" onChange="changeDeadlineType(this.value);">
										<form:option value="1">固定</form:option>	
										<form:option value="2">不固定</form:option>										
									</form:select>
									<select name="deadlineDataType" id="deadlineDataType" onChange="changeDeadlineDataType(this.value);" style="width:55px;">
										<option value="1">按月</option>	
										<option value="2">按日</option>										
									</select>
									<font color="red">&nbsp;</font>
								</td>
							</tr>
							<tr id="deadlineType_min">
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.minDeadline"/><span class="dl-tip">(月)</span><msg:message code="system.common.sign.colon"/></td>
									<td width="85%" class="skinLayout_lable">
									<form:input path="minDeadline" class="valid-control" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="tip-common">&nbsp;*</font>
								</td>
							</tr>
							<tr id="deadlineType_max">
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxDeadline"/><span class="dl-tip">(月)</span><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
									<form:input path="maxDeadline" class="valid-control" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="tip-common">&nbsp;*</font>
								</td>
							</tr>
							
							<tr>
								<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.picture"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
								
								
									<div id="fileQueue"></div>
									<input type="file" name="uploadify" id="uploadify" />
									<p>
										<div id="fileOperateInfo">
											<a href="javascript:$('#uploadify').uploadify('cancel', '*')">
											<msg:message code='uploadify.cancelUpload'/></a> | 
											<a href="javascript:$('#uploadify').uploadify('upload', '*')">
											<msg:message code='uploadify.startUpload'/></a> | 
											<a id="delImg" href="javascript:void(0);">删除</a>
										</div>
									</p>
									<div id="fileDel" ></div>
								</td>
							</tr>
							
							<tr height="35">
								<td width="15%" align="right" class="skinLayout_info"></td>
								<td width="85%" class="skinLayout_lable"></td>
							</tr>
						</table>
				</td>
			</tr>
		</table>
	</div>
	<div id="div_2" style="display:none;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" >
			<tr><td height="100%" style="padding-left:3px;padding-right:6px;"><textarea name="description" id="descrption" style="height:450px;width:100%;">${product.description }</textarea> </td></tr>
		</table>
		
	</div>
	<div id="div_3" style="display:none;">
		<table  width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
			
			<tr>
				<td width="100%" style="border-top:1px">
					<input type="hidden" name="attributeGroupId" id="attributeGroupId" />
					<input type="hidden" name="attributeValue" id="attributeValue" />
					<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.investCateName"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="investCategoryId" htmlEscape="true" onChange="changeType(this.value);">	
									<form:options items="${investCategoryList}" itemLabel="name" itemValue="id"/> 										
								</form:select>
							</td>
						</tr>
					</table>
					<table id="prdTable"  width="100%" border="0" cellspacing="0" cellpadding="0"  class="skinLayout">
					</table> 
				</td>
			</tr>
		</table>
	</div>
	<!--
	<div id="div_4" style="display:none;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" >
			<tr><td height="100%" style="padding-left:3px;padding-right:6px;"><textarea name="profile" style="height:450px;width:100%;">${product.profile}</textarea> </td></tr>
		</table>
		
	</div>
	-->
	<div id="menuContent" class="menuContent" style="display: none; position: absolute;">
		<ul id="treeDemo" class="ztree" style="margin-top: 0; width: 160px;"></ul>
	</div>
	<div style="float:left;width:100%;height:40px;"></div>
	<div class="toolbar">
		<div class="left"></div>
		<div class="center">
			<input type="button" class="btn2" id="btnok"  value="<msg:message code="button.save"/>" />
			<input type="button" class="btn2" onclick="window.location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
		</div>
		<div class="right"></div>
	</div>
			</form:form>
			</td>
		</tr>
	</table>
</body>
</html>
