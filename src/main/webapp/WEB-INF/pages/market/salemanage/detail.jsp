<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="${base}/common/js/kindeditor/kindeditor.js" type="text/javascript"></script>
<title></title>
<script type="text/javascript">
//编辑器
var editor = "";
var editor2 = "";
var opti={
	width:'600px',
	height:'200px',
    items : [
			 
			],
	readonlyMode:true
};
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="description"]',opti);
	editor2 = K.create('textarea[name="profile"]',opti);
});

</script>
</head>
<body class="skinMain">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<tr>
			<td width="100%">
				<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
						<tr>
							<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.code" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  width="30%">${product.code}</td>
							<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.name" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  width="30%">${product.name}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.shortName" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.shortName}</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.type" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
								<c:forEach items="${productTypeMap}" var="it"><c:if test="${product.type==it.key}">${it.value}</c:if></c:forEach>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.organization.name" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left" >${organization.name}</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.dictionary.name" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left" >${dictionaryItem.itemName}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.provider" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left" >${provider.shortName}</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.isTotalShare" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left" >
								<c:if test="${product.isTotalShare==0}">否</c:if><c:if test="${product.isTotalShare==1}">是</c:if>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.raiseStartTime" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.raiseStartTime}</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.raiseFinishTime" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.raiseFinishTime}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.beginningShare" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.beginningShare}</td>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.incrementalShare" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.incrementalShare}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.minTotalShare" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.minTotalShare}</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.maxTotalShare" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
							<c:if test="${product.maxTotalShare==-1}">不限</c:if><c:if test="${product.maxTotalShare!=-1}">${product.maxTotalShare}</c:if>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.foundDate" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.foundDate}</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.stopDate" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.stopDate }</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.deadlineType" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left" colspan="3" >
								<c:forEach items="${deadlineTypeMap}" var="it"><c:if test="${product.deadlineType==it.key}">${it.value}</c:if></c:forEach>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.status" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
								<c:forEach items="${statusMap}" var="it"><c:if test="${product.status==it.key}">${it.value}</c:if></c:forEach>
							</td>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.sellStatus" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
								<c:forEach items="${sellStatusMap}" var="it"><c:if test="${product.sellStatus==it.key}">${it.value}</c:if></c:forEach>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.released" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
								<c:forEach items="${releasedMap}" var="it"><c:if test="${product.released==it.key}">${it.value}</c:if></c:forEach>
							</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.profitType" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
								<c:forEach items="${profitTypeMap}" var="it"><c:if test="${product.profitType==it.key}">${it.value}</c:if></c:forEach>
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.createTime" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.createTime}</td>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.modifyTime" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.modifyTime}</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.isRedeem" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
								<c:if test="${product.isRedeem==0}"><msg:message code="product.operate.no" /></c:if><c:if test="${product.isRedeem==1}"><msg:message code="product.operate.yes" /> </c:if>
							</td>
							<td align="right" class="skinLayout_info"  ><msg:message code="product.isAdd" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >
								<c:if test="${product.isAdd==0}"><msg:message code="product.operate.no" /></c:if><c:if test="${product.isAdd==1}"><msg:message code="product.operate.yes" /></c:if>
							</td>
						</tr>
						<c:if test="${product.deadlineType == 1}">
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.minDeadline" />
							<c:choose>
								<c:when test="${product.deadlineDataType == 2}">(日)</c:when>
								<c:otherwise>(月)</c:otherwise>
							</c:choose>
							<msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.minDeadline}</td>
							<td align="right" class="skinLayout_info" ><msg:message code="product.maxDeadline" />
							<c:choose>
								<c:when test="${product.deadlineDataType == 2}">(日)</c:when>
								<c:otherwise>(月)</c:otherwise>
							</c:choose>
							<msg:message code="system.common.sign.colon"/></td>
							<td align="left"  >${product.maxDeadline}</td>
						</tr>
						</c:if>
						<c:if test="${product.profitType==2}">
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="product.minProfitRatio" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left"   >${product.minProfitRatio}</td>
								<td align="right" class="skinLayout_info" ><msg:message code="product.maxProfitRatio" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left"   >${product.maxProfitRatio}</td>
							</tr>
						</c:if>
						<c:if test="${product.type==1}">
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="product.maxClientCount" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left"   >
									<c:choose>
										<c:when test="${product.maxClientCount==-1}"><msg:message code="product.unlimited" /></c:when>
										<c:otherwise>${product.maxClientCount}</c:otherwise>
									</c:choose>
								</td>
								<td align="right" class="skinLayout_info" ><msg:message code="product.maxLowAmountClientCount" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left"   >
									<c:choose>
										<c:when test="${product.maxLowAmountClientCount==-1}"><msg:message code="product.unlimited" /></c:when>
										<c:otherwise>${product.maxLowAmountClientCount}</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="product.lowAmountThreshold" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left" colspan="3">
									<c:choose>
										<c:when test="${product.lowAmountThreshold==-1}"><msg:message code="product.unlimited" /></c:when>
										<c:otherwise>${product.lowAmountThreshold}</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:if>
						<c:if test="${product.type==2}">
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="product.closeType" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left" colspan="3">
									<c:forEach items="${closeTypeMap}" var="it"><c:if test="${product.closeType==it.key}">${it.value}</c:if></c:forEach>
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="product.maxLowAmountClientCount" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left">
									<c:if test="${product.maxLowAmountClientCount == -1}">不限</c:if>
									<c:if test="${product.maxLowAmountClientCount != -1}">${product.maxLowAmountClientCount}</c:if>
								</td>
								<td align="right" class="skinLayout_info" ><msg:message code="product.lowAmountThreshold" /><msg:message code="system.common.sign.colon"/></td>
								<td align="left">
									<c:if test="${product.lowAmountThreshold == -1}">不限</c:if>
									<c:if test="${product.lowAmountThreshold != -1}">${product.lowAmountThreshold}</c:if>
								</td>
							</tr>
						</c:if>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.productCategory.name" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"  colspan="3">
								${productCategory.name}
							</td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.profile" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"   colspan="3" ><textarea name="profile" style="height:250px;width:80%;background-color: #FFF" disabled="disabled">${product.profile}</textarea></td>
						</tr>
						<tr>
							<td align="right" class="skinLayout_info" ><msg:message code="product.description" /><msg:message code="system.common.sign.colon"/></td>
							<td align="left"   colspan="3" ><textarea name="profile" style="height:250px;width:80%;background-color: #FFF" disabled="disabled">${product.description}</textarea></td>
						</tr>
						<c:forEach items="${productAttributeList}" var="pro" varStatus="stu">
							<tr>
								<td align="right" class="skinLayout_info" >${pro.productAttribute.name}:</td>
								<td align="left"   colspan="3" ><c:if test="${pro.productAttribute.pageShowType==9}"><textarea name="ext_${stu.index}" style="height:450px;width:100%;">${pro.value}</textarea></c:if><c:if test="${pro.productAttribute.pageShowType!=9}">${pro.value}</c:if></td>
							</tr>
							<script>
								var editor_${stu.index};
								KindEditor.ready(function(K) {
									editor_${stu.index} = K.create('textarea[name="ext_${stu.index}"]',opti);
								});
							</script>
						</c:forEach>
						<tr><td height="30" colspan="4">&nbsp;</td></tr>
					</table>
					<div class="toolbar">
						<div class="left"></div>
							<div class="center">
								<input type="button" class="btn2" onclick="location.href='${base}${backLink}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
							</div>
						<div class="right"></div>
					</div>
				</td>
			</tr>
		</table>
</html>