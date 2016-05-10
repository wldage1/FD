<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script>
	
	$(document).ready(function() {
		// 初始产品验证项
		var type = '${docConfig.type}';
		if(type == '2'){
			$("#type").val('2');
		}
	
		// 表单验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/market/salemanage/saleconfig/docconfig/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#productDocConfig").submit();
					}
				}
			};
		$("form #btnok").valid(options);
		
	});
	
</script>
</head>
<body class="skinMain">

	<table id="config_table" width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain" align="left">
		<tr>
			<td id="subProduct_table_right" valign="top" >
				<form:form method="post" action="${base}/market/salemanage/saleconfig/docconfig/save" commandName="productDocConfig" >
					<input type="hidden" name="c" value="${c}"/>
					<input type="hidden" name="productId" value="${docConfig.productId}" />
					<input type="hidden" name="prompt" value="contact" />
					<input type="hidden" name="turnCode" value="${market_salemanage.code}" />
					<input type="hidden" name="coverParam" value="1" />
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="productDocConfig.type"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:select path="type" class="valid-control">
									<form:option value="1"><msg:message code="productDocConfig.type.a"/></form:option>	
									<form:option value="2"><msg:message code="productDocConfig.type.b"/></form:option>										
								</form:select>&nbsp;<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="productDocConfig.contact"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="contact" class="valid-control" maxlength="30" value="${docConfig.contact}"/>
								&nbsp;<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="productDocConfig.address"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="address" class="valid-control" maxlength="30" value="${docConfig.address}"/>
								&nbsp;<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="productDocConfig.telephone"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="telephone" class="valid-control" maxlength="30" value="${docConfig.telephone}"/>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="productDocConfig.cellphone"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="cellphone" class="valid-control" maxlength="30" value="${docConfig.cellphone}"/>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right" class="skinLayout_info"><msg:message code="productDocConfig.postcode"/><msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<form:input path="postcode" class="valid-control" maxlength="30" value="${docConfig.postcode}"/>
								&nbsp;<font color="red">*</font>
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
