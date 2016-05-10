<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script>
	$(document).ready(function() {
	
		// 初始产品期限默认值
		initLimitDom("deadline","${subProduct.deadline}");
		// 初始小额投资人数上限默认值
		initLimitDom("maxLowAmountClientCount","${subProduct.maxLowAmountClientCount}");
		// 初始收益类型
		changeProfitType("${subProduct.profitType}");
		<c:if test="${product.isTotalShare == 0}">
		// 最大募集金额默认值
		initLimitDom("maxTotalShare","${subProduct.maxTotalShare}");
		</c:if>
		// 页面验证
		var options = {
			event:'focus',
			ajax:{
				url:'${base}/market/salemanage/saleconfig/split/valid.json'
			},
			validStatus:function(status){
				if (status){
					$("#subProduct").submit();
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
	
	// 设置收益类型
	function changeProfitType(type){
		if(type && type == 1){
			$("#tr_profitRatioDesc").show();
		}else{
			$("#tr_profitRatioDesc").hide();
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
			if(dev != '' && (dev == '-1' || dev == '-1.0' || dev == '-1.000000')){
				limit.val(dev);
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
</script>
</head>
<body class="skinMain">

	<table id="subProduct_table" width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain" align="left">
		<tr>
			<td id="subProduct_table_right" valign="top" >
				<form:form method="post" action="split/save" commandName="subProduct" >
					<input type="hidden" name="c" value="${c}"/>
					<input type="hidden" name="prompt" value="productName" />
					<input type="hidden" name="turnCode" value="${market_salemanage_saleconfig.code}" />
					<input type="hidden" name="coverParam" value="1" />
					<input type="hidden" name="productId" value="${product.id}" />
					<input type="hidden" name="productType" value="${product.type}" />
					<input type="hidden" name="productName" value="${product.shortName}" />
					<input type="hidden" name="id" value="${subProduct.id}" />
					<input type="hidden" name="isTotalShare" value="${product.isTotalShare}" />
					<input type="hidden" name="isTotalLowAmountClientCount" value="${product.isTotalLowAmountClientCount}" />
					<input type="hidden" name="productSaleStatus" value="${product.sellStatus}" />
					<div id="rate_hidden"></div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.name"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								${product.name}
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.type"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="type" onchange="this.value=$.trim(this.value)" value="${subProduct.type}" class="valid-control" /><font color="red">&nbsp;&nbsp;可以按照受益权类型或者期限进行拆分，例：18个月期或者优先级。  如果不填写默认为原有产品的名称。</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.deadline"/>(<c:if test="${product.deadlineDataType == 1}">月</c:if><c:if test="${product.deadlineDataType == 2}">日</c:if>)</span><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="deadline" id="in_deadline" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" />&nbsp;&nbsp;<input  name="deadline" id="un_deadline" onclick="limitDom('deadline','${subProduct.deadline}');" type="checkbox" value="-1" style="width:15px;height:15px;" align="absmiddle">&nbsp;&nbsp;不限<font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>
						<c:if test="${product.isTotalShare == 0}">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.minTotalShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="minTotalShare" maxlength="20" value="${subProduct.minTotalShare}" class="valid-control" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red" class="sy-lmt">&nbsp;*&nbsp;</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.maxTotalShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="maxTotalShare" class="valid-control" id="in_maxTotalShare" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"  maxlength="30" />&nbsp;&nbsp;<input name="maxTotalShare" id="un_maxTotalShare" onclick="limitDom('maxTotalShare','${product.maxTotalShare}');" type="checkbox" value="-1" style="width:15px;height:15px;">&nbsp;&nbsp;不限<font color="red" class="sy-lmt">&nbsp;*</font>
							</td>
						</tr>
						</c:if>

						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.profitType"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="profitType" onChange="changeProfitType(this.value);">
									<form:option value="2"><msg:message code="product.profitType.fix"/></form:option>
									<form:option value="1"><msg:message code="product.profitType.unfix"/></form:option>										
								</form:select>
								<font color="red">&nbsp;</font>
							</td>
						</tr>
						<tr id="tr_profitRatioDesc">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.profitRatioDesc"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:textarea path="profitRatioDesc" id="profitRatioDesc"  cssStyle="height:150px;width:50%;"/>
							</td>
						</tr>	
			
						<!--<tr id="subProduct_6">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.salesFactor"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="salesFactor" maxlength="20"  class="valid-control"/><font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>-->
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.warningShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="warningShare" maxlength="20"  value="${subProduct.warningShare}" class="valid-control" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" /><font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>
						<c:if test="${product.isTotalLowAmountClientCount == 0}">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.maxLowAmountClientCount"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="maxLowAmountClientCount" class="valid-control" id="in_maxLowAmountClientCount" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" />&nbsp;&nbsp;<input name="maxLowAmountClientCount" id="un_maxLowAmountClientCount" onclick="limitDom('maxLowAmountClientCount','${subProduct.maxLowAmountClientCount}');" type="checkbox" value="-1" style="width:15px;height:15px;">&nbsp;&nbsp;不限<font color="red">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.warningLowAmountClientCount"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<input name="warningLowAmountClientCount" value="${subProduct.warningLowAmountClientCount}" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');" />&nbsp;&nbsp;<font color="red">&nbsp;</font>
							</td>
						</tr>
						</c:if>
						<!--<tr id="subProduct_8">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.isFavorites"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="isFavorites" >
									<form:option value="1" ><msg:message code='product.operate.yes'/></form:option>
									<form:option value="0" ><msg:message code='product.operate.no'/></form:option>
								</form:select>
								<font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>-->
						<!--<tr id="subProduct_9">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.isOrder"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="isOrder" >
									<form:option value="0" ><msg:message code='product.operate.no'/></form:option>
									<form:option value="1" ><msg:message code='product.operate.yes'/></form:option>
								</form:select>
								<font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>-->
						<!--<tr id="subProduct_14">
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.payWay"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="payWay" >
									<form:option value="" ><msg:message code='please.select'/></form:option>
									<form:options items="${payWay}"/>
								</form:select>
								<font color="red">&nbsp;&nbsp;</font>
							</td>
						</tr>-->
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="subProduct.beginningShare"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="beginningShare" class="valid-control" maxlength="30" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"/>&nbsp;&nbsp;<font color="red" class="sy-lmt-2">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">&nbsp;</td>
							<td width="85%" colspan="2" align="left">
								<input type="button" class="btn2" name="btnok" value="<msg:message code="button.save"/>" id="btnok"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" class="btn2" onclick="window.location.href='${base}${market_salemanage_saleconfig.controller}?c=${market_salemanage_saleconfig.code}&id=${product.id}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</td>
						</tr>
					</table>
				</form:form>
			</td>
		</tr>
	</table>

</body>
</html>
