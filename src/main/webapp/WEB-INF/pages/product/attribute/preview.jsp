<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">
	input[type="radio"],input[type="checkbox"]{
		height:13px;
		width:13px;
		vertical-align:middle;
		border:0;
	}
</style>
</head>
<body class="skinMain">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinLayout">
					<c:forEach var="attribute" items="${attributeList}">
						<tr>
							<td width="15%" align="right" class="skinLayout_info">${attribute.name}<msg:message code="system.common.sign.colon"/></td>
							<td width="85%" class="skinLayout_lable">
								<c:if test="${attribute.pageShowType == 1}">
									<input value="${attribute.defaultValue}"></input>
								</c:if>
								<c:if test="${attribute.pageShowType == 2}">
									<select>
										<c:forTokens items="${attribute.defaultValue}" delims="*" var="option">
											<option value="${fn:split(option,':')[1]}">${fn:split(option,':')[0]}</option>
										</c:forTokens>
									</select>
								</c:if>
								<c:if test="${attribute.pageShowType == 3}">
									<c:forTokens items="${attribute.defaultValue}" delims="*" var="option">
										<input type="radio" name="radioValue" value="${fn:split(option,':')[1]}"/>${fn:split(option,':')[0]}
									</c:forTokens>
								</c:if>
								<c:if test="${attribute.pageShowType == 4}">
									<c:forTokens items="${attribute.defaultValue}" delims="*" var="option">
										<input type="checkbox" name="checkboxValue" value="${fn:split(option,':')[1]}"/>${fn:split(option,':')[0]}
									</c:forTokens>
								</c:if>
								<c:if test="${attribute.pageShowType == 5}">
									<textarea>${attribute.defaultValue}</textarea>
								</c:if>
								<c:if test="${attribute.pageShowType == 6}">
									<input type="text"  class="Wdate" onclick="WdatePicker({isShowClear:false,readOnly:true})" value="${attribute.defaultValue}"/>
								</c:if>
								<c:if test="${attribute.pageShowType == 8}">
									<input type="hidden" value="${attribute.defaultValue}"/>
								</c:if>
								<c:if test="${attribute.isNotNull == 1}">
									<font color="red">&nbsp;*&nbsp;</font>
								</c:if>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td width="15%" align="right">&nbsp;</td>
						<td width="85%" colspan="2" align="left">
							<input type="button" class="btn2" onclick="window.location.href='${base}${elf:parentcontroller(requestScope,sessionScope)}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table> 
</body>
</html>