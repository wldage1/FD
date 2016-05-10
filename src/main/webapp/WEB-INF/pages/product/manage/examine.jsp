<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	$(document).ready(function(){
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/product/manage/validExamine.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#productApplication").submit();
					}
				}
			};
		$("form #btn_save,#unbtn_save").valid(options);
		//批准按钮
		$("#btn_save").click(function(){
			$("#status").val(1);
		});
		//不批准
		$("#unbtn_save").click(function(){
			$("#status").val(0);
		});
	});
</script>

</head>
<body class="skinMain">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<form method="post" action="saveProductApplication"  id="productApplication">
				<input type="hidden" name="turnCode" value="${product_manage.code}" />
				<input type="hidden" name="coverParam" value="true" />
				<input type="hidden" name="c" value="${product.c}"/>
				<input type="hidden" name="prompt" value="name"/>
				<input type="hidden" name=productID value="${product.id}"/>
<%-- 				<input type="hidden" name=providerUserID value="${product.creatorUserId}"/> --%>
				<input type="hidden" name="status" id="status"/>
				<input type="hidden" name="sellStatusTemp" value="${product.sellStatus}"/>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
					<tr>
						<td width="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
								<tr>
									<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.name"/><msg:message code="system.common.sign.colon"/></td>
									<td width="85%" class="skinLayout_lable">${product.name}</td>
								</tr>
								<tr>
									<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.code"/><msg:message code="system.common.sign.colon"/></td>
									<td width="85%" class="skinLayout_lable">${product.code}</td>
								</tr>
								<tr>
									<td width="15%" align="right" class="skinLayout_info"><msg:message code="product.application.feedback"/><msg:message code="system.common.sign.colon"/></td>
									<td width="85%" class="skinLayout_lable">
										<textarea name="feedback" id="feedback" class="valid-control" ></textarea>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			<div class="toolbar">
				<div class="left"></div>
					<div class="center">
						<c:if test="${product.status == 1}">
							<input type="button" class="btn2" id="btn_save" value="<msg:message code="product.product.examineratify" />" />
							<input type="button" class="btn2" id="unbtn_save" value="<msg:message code="product.product.unexamineratify" />" />
						</c:if>
						<input type="button" class="btn2" onclick="window.location.href='${base}${product_manage.controller}?c=${product_manage.code}&back=1'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
					</div>
				<div class="right"></div>
			</div>
			</form>
			</td>
		</tr>
	</table>
</body>
</html>
