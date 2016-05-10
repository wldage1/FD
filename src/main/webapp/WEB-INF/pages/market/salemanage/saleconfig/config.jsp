<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript" charset="utf-8"></script>
<title></title>
<script>
	var editor;
	$(document).ready(function() {
	
		// 初始产品募集额度及收益类型表单
		changeTotalShare("${product.isTotalShare}");
		changeProfitType("${product.profitType}");
		changeDeadlineType("${product.deadlineType}");
		changeLowClientCount("${product.isTotalLowAmountClientCount}");
		changeCloseType("${product.closeType}");
		// 最大募集金额默认值
		initLimitDom("maxTotalShare","${product.maxTotalShare}");
		// 初始受托人默认值
		initLimitDom("maxClientCount","${product.maxClientCount}");
		// 初始小额投资人数量上限默认值
		initLimitDom("maxLowAmountClientCount","${product.maxLowAmountClientCount}");
		// 初始小额投资上限默认值
		initLimitDom("lowAmountThreshold","${product.lowAmountThreshold}");
		
		// 表单验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/market/salemanage/saleconfig/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						editor.sync();
						$("form#product").submit();
					}
				}
			};
		$("form #btnok").valid(options);
		
		// 初始产品验证项
		var productType = "${product.type}";
		var productSellStatus = "${product.sellStatus}";
		if(productType == "5" && (productSellStatus == "5" || productSellStatus =="6")){
			$(".sy-lmt").text("");
		}else if(productType == "2" && (productSellStatus == "5" || productSellStatus =="6")){
			$(".sy-lmt").text("");
			$(".sy-lmt-2").text("");
		}
		
	});

	// 设置募集额度
	function changeTotalShare(totalShare){
		if(totalShare && totalShare == 1){
			$('#totalShare_min').show();
			$('#totalShare_max').show();
			
		}else{
			$('#totalShare_min').hide();
			$('#totalShare_max').hide();
		}
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
	}
	
	// 设置期限时间类型
	function changeDeadlineDataType(type){
		if(type && type == 1){
			$(".dl-tip").html("(月)");
		}else{
			$(".dl-tip").html("(日)");
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
	}
	
	
	// 修改封闭类型
	function changeCloseType(ct){
		// 全程封闭
		if(ct == 1){
			$('#isRedeem').attr("disabled",true);
			$('#isAdd').attr("disabled",true);
			$('#hide_isRedeem').attr("disabled",false);
			$('#hide_isAdd').attr("disabled",false);
		}else{
			$('#isRedeem').attr("disabled",false);
			$('#isAdd').attr("disabled",false);
			$('#hide_isRedeem').attr("disabled",true);
			$('#hide_isAdd').attr("disabled",true);
		}
	}
	
	// 修改小额投资人数是否共享
	function changeLowClientCount(lv){
		if(lv && lv == 0){
			$("#maxLowAmountClientCount_nor").hide();
			$("#maxLowAmountClientCount_warn").hide();
		}else{
			$("#maxLowAmountClientCount_nor").show();
			$("#maxLowAmountClientCount_warn").show();
		}
	}

	
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
	
	// 设置不限输入控件
	function limitDom(obj,dev){
		var cklimit = $("#un_"+obj).attr("checked");
		var limit = $("#in_"+obj);
		var unlimit = $("#un_"+obj);
		
		if(cklimit){
			limit.attr("disabled",true);
			limit.val('');
			unlimit.attr("name",obj);
		}else{
			limit.attr("disabled",false);
			unlimit.attr("name","");
			if(dev != '-1' && dev != '-1.0' && dev != '-1.000000'){
				limit.val(dev);
			}
		}
	}
	
	
	// 验证产品止期
	function validStopdate(){
		var foundDate = $("#foundDate").val();
		var stopDate = $("#stopDate").val();
		if(foundDate != ''){
			var arr = foundDate.split("-");
			var starttime = new Date(arr[0], arr[1], arr[2]);
			var starttimes = starttime.getTime();

			var arrs = stopDate.split("-");
			var lktime = new Date(arrs[0], arrs[1], arrs[2]);
			var lktimes = lktime.getTime();

			if (starttimes > lktimes) {
				$('#stopDate').poshytip('show');
				return false;
			}else{
				return true;
			}
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
	
	//页面编辑器
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="accountInfo"]',{
		uploadJson : '${base}/product/manage/kindeditor/uploadimg',
		fileManagerJson : '${base}/product/manage/kindeditor/uploadimg',
		allowFileManager: true
		});
	});
	
</script>
</head>
<body class="skinMain">

	<table id="subProduct_table" width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain" align="left">
		<tr>
			<td id="subProduct_table_right" valign="top" >
				<form:form method="post" action="${base}/market/salemanage/saleconfig/config/save" commandName="product" >
					<input type="hidden" name="c" value="${c}"/>
					<input type="hidden" name="type" value="${product.type}" />
					<input type="hidden" name="sellStatus" value="${product.sellStatus}" />
					<input type="hidden" name="prompt" value="productName" />
					<input type="hidden" name="turnCode" value="${market_salemanage.code}" />
					<input type="hidden" name="coverParam" value="1" />
					<input type="hidden" name="id" value="${product.id}" />
					<input type="hidden" name="productName" value="${product.shortName}" />
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
						
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.shortName"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								${product.shortName}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.isTotalShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="isTotalShare" onChange="changeTotalShare(this.value);" class="valid-control">
									<option value="">--请选择--</option>
									<form:option value="1"><msg:message code="product.operate.yes"/></form:option>	
									<form:option value="0"><msg:message code="product.operate.no"/></form:option>										
								</form:select>&nbsp;<font color="red">*</font>
							</td>
						</tr>
						<tr id="totalShare_min">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.minTotalShare"/><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
							<form:input path="minTotalShare" class="valid-control" maxlength="30" value="${product.minTotalShare}" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="sy-lmt">&nbsp;*</font>
							</td>
						</tr>
						<tr id="totalShare_max">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxTotalShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="maxTotalShare" class="valid-control" id="in_maxTotalShare" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"  maxlength="30" />&nbsp;&nbsp;<input name="maxTotalShare" id="un_maxTotalShare" onclick="limitDom('maxTotalShare','${product.maxTotalShare}');" type="checkbox" value="-1" style="width:15px;height:15px;">&nbsp;&nbsp;不限<font color="red" class="sy-lmt">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.deadlineType"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="deadlineType" onChange="changeDeadlineType(this.value);">
									<form:option value="1">固定</form:option>	
									<form:option value="2">不固定</form:option>										
								</form:select>
								<form:select path="deadlineDataType" onChange="changeDeadlineDataType(this.value);" style="width:55px;">
									<form:option value="1">按月</form:option>	
									<form:option value="2">按日</form:option>										
								</form:select>
								<font color="red" class="sy-lmt">&nbsp;</font>
							</td>
						</tr>
						<tr id="deadlineType_min">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.minDeadline"/><span class="dl-tip">(月)</span><msg:message code="system.common.sign.colon"/></td>
								<td width="85%" class="skinLayout_lable">
								<form:input path="minDeadline" class="valid-control" maxlength="30" value="${product.minDeadline}" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="sy-lmt">&nbsp;*</font>
							</td>
						</tr>
						<tr id="deadlineType_max">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxDeadline"/><span class="dl-tip">(月)</span><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="maxDeadline" class="valid-control" maxlength="30" value="${product.maxDeadline}" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="sy-lmt">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.profitType"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="profitType" onChange="changeProfitType(this.value);">
									<form:option value="2"><msg:message code="product.profitType.fix"/></form:option>
									<form:option value="1"><msg:message code="product.profitType.unfix"/></form:option>										
								</form:select>
								<font color="red" class="sy-lmt">&nbsp;</font>
							</td>
						</tr>
						<tr id="profitType_min">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.minProfitRatio"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="minProfitRatio" class="valid-control" maxlength="30" value="${product.minProfitRatio}" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="sy-lmt">&nbsp;*</font>
							</td>
						</tr>
						<tr id="profitType_max">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxProfitRatio"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
							<form:input path="maxProfitRatio" class="valid-control" maxlength="30" value="${product.maxProfitRatio}" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="sy-lmt">&nbsp;*</font>
							</td>
						</tr>
						
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.raiseStartTime"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="raiseStartTime" maxlength="30" value="${product.raiseStartTime}" onclick='WdatePicker()' class='Wdate valid-control' readonly='readonly'/>&nbsp;<font color="red" class="sy-lmt">*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.raiseFinishTime"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="raiseFinishTime" maxlength="30" value="${product.raiseFinishTime}" onclick='WdatePicker()' class='Wdate valid-control' readonly='readonly'/>&nbsp;<font color="red" class="sy-lmt">*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.beginningShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="beginningShare" maxlength="30" value="${product.beginningShare}"  class="valid-control"/><font color="red" class="sy-lmt-2">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.incrementalShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="incrementalShare" maxlength="30" value="${product.incrementalShare}" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="sy-lmt-2">&nbsp;*</font>
							</td>
						</tr>
							
							
							
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.foundDate"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="foundDate" maxlength="30" value="${product.foundDate}" onclick='WdatePicker()' class='Wdate' readonly='readonly'/><font color="red">&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.stopDate"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="stopDate"  maxlength="30" value="${product.stopDate}" onclick='WdatePicker()' class='Wdate valid-control' readonly='readonly'/><font color="red">&nbsp;</font>
							</td>
						</tr>

						
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxClientCount"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="maxClientCount" id="in_maxClientCount" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" />&nbsp;&nbsp;<input name="maxClientCount" id="un_maxClientCount" onclick="limitDom('maxClientCount','${product.maxClientCount}');" type="checkbox" value="-1" style="width:15px;height:15px;">&nbsp;&nbsp;不限<font color="red">&nbsp;</font>
							</td>
						</tr>
						
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.isTotalLowAmountClientCount"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="isTotalLowAmountClientCount" onChange="changeLowClientCount(this.value);">
									<form:option value="1"><msg:message code="product.operate.yes"/></form:option>	
									<form:option value="0"><msg:message code="product.operate.no"/></form:option>										
								</form:select>
								<font color="red">&nbsp;</font>
							</td>
						</tr>
						<tr id="maxLowAmountClientCount_nor">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxLowAmountClientCount"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="maxLowAmountClientCount" class="valid-control" id="in_maxLowAmountClientCount" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"  maxlength="30" />&nbsp;&nbsp;<input name="maxLowAmountClientCount" id="un_maxLowAmountClientCount" onclick="limitDom('maxLowAmountClientCount','${product.maxLowAmountClientCount}');" type="checkbox" value="-1" style="width:15px;height:15px;">&nbsp;&nbsp;不限<font color="red">&nbsp;*</font>
							</td>
						</tr>
						<tr id="maxLowAmountClientCount_warn">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.warningLowAmountClientCount"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="warningLowAmountClientCount" value="${product.warningLowAmountClientCount}" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"  maxlength="30" />&nbsp;&nbsp;<font color="red">&nbsp;</font>
							</td>
						</tr>
						
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.lowAmountThreshold"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="lowAmountThreshold" class="valid-control" id="in_lowAmountThreshold" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"  />&nbsp;&nbsp;<input name="lowAmountThreshold" id="un_lowAmountThreshold" onclick="limitDom('lowAmountThreshold','${product.lowAmountThreshold}');" type="checkbox" value="-1" style="width:15px;height:15px;">&nbsp;&nbsp;不限<font color="red">&nbsp;*</font>
							</td>
						</tr>
						<!--<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.sellType"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="sellType">
									<form:options items="${sellTypeMap}"/> 										
								</form:select>
								<font color="red">&nbsp;</font>
							</td>
						</tr>-->
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.isNoTice"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="isNoTice">
									<form:option value="0" ><msg:message code='product.operate.no'/></form:option>
									<form:option value="1" ><msg:message code='product.operate.yes'/></form:option>
								</form:select>
								<font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>
						
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.closeType"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="closeType" htmlEscape="true" items="${closeType}" onchange="changeCloseType(this.value);"/>
								<font color="red">&nbsp;</font>
							</td>
						</tr>

						<input type="hidden" name="isRedeem" id="hide_isRedeem" disabled="disabled" value="0"/>
						<input type="hidden" name="isAdd" id="hide_isAdd"  disabled="disabled" value="0"/>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.isRedeem"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="isRedeem">	
									<form:option value="0" ><msg:message code='product.operate.no'/></form:option>
									<form:option value="1" ><msg:message code='product.operate.yes'/></form:option>
								</form:select>
								<font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.isAdd"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="isAdd">
									<form:option value="0" ><msg:message code='product.operate.no'/></form:option>
									<form:option value="1" ><msg:message code='product.operate.yes'/></form:option>
								</form:select>
								<font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.contractPrefix"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="contractPrefix" maxlength="30" value="${product.contractPrefix}" />&nbsp;<font color="red"></font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.accountInfo"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<textarea name="accountInfo" style="height:150px;width:635px;">${product.accountInfo}</textarea> 
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">&nbsp;</td>
							<td width="85%" colspan="2" align="left">
								<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" class="btn2" onclick="history.go(-1);" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</td>
						</tr>
					</table>
				</form:form>
			</td>
		</tr>
	</table>
	
</body>
</html>
