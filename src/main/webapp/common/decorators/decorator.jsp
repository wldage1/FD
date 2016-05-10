<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="decorator" uri="/WEB-INF/tlds/sitemesh-decorator.tld" %>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<title><decorator:title /></title>
<link  href="${base}/${skin}/css/skin.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" media="screen" href="${base}/${skin}/css/jquery.ui.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${base}/${skin}/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${base}/${skin}/css/uploadify.css">
<link href="${base}/${skin}/css/ztree.css" rel="stylesheet" type="text/css">
<link href="${base}/${skin}/css/demo.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/boxy.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/poshytip/tip-violet.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/poshytip/tip-yellowsimple.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/top.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/seek.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/repository.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/login_select.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/jquery.jscrollpane.codrops1.css" rel="stylesheet" type="text/css" >
<link href="${base}/${skin}/css/ui.multiselect.css" rel="stylesheet" type="text/css" >
<link  href="${base}/${skin}/css/tab.css" rel="stylesheet" type="text/css" />
<link href="${base}/${skin}/css/layer.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${base}/common/js/jquery-1.8.min.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery.json-2.2.min.js"></script>
<script src="${base}/common/js/jqgrid/grid.locale-cn.js" type="text/javascript"></script>
<script src="${base}/common/js/jqgrid/jquery.jqGrid.src.js" type="text/javascript"></script>
<script src="${base}/common/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script>
<script src="${base}/common/js/jqgrid/jquery.jqGrid.tree.plugin.js" type="text/javascript"></script>
<script type="text/javascript" src="${base}/common/js/tree/jquery.ztree.core-3.0.min.js"></script>
<script type="text/javascript" src="${base}/common/js/tree/jquery.ztree.exedit-3.0.min.js"></script>
<script type="text/javascript" src="${base}/common/js/tree/jquery.ztree.excheck-3.0.min.js"></script>
<%-- <script type="text/javascript" src="${base}/common/js/boxy/jquery.boxy.js"></script> --%>
<script  type="text/javascript" language="javascript" src="${base}/common/js/uploadify/jquery.uploadify-3.1.min.js"></script>
<script src="${base}/common/js/datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${base}/common/js/searchToolBar.js" type="text/javascript"></script>
<script src="${base}/common/js/json2.js" type="text/javascript"></script>
<script src="${base}/common/js/global.js" type="text/javascript"></script>
<script src="${base}/common/js/poshytip/jquery.poshytip.js" type="text/javascript"></script>
<script src="${base}/common/js/poshytip/jquery.poshytip.plugin.js" type="text/javascript"></script>
<script src="${base}/common/js/jquery.layout.js" type="text/javascript"></script>
<script src="${base}/common/js/layer/layer.min.js" type="text/javascript"></script>
<decorator:head />
</head>
<body class="skinMain" >
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="decorator-table">
	<c:if test="${navigateOrNot == 0}">
	<tr>
		<td width="100%" height="25">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="skinNav">
				<tr>
					<th width="2%"><img src="${base}/${skin}/images/nav/bg_07.gif" width="19" height="19" />
					</th>
					<th width="98%" align="left"><msg:message code="navigation.title"/>&nbsp;${navigation}</th>
				</tr>
			</table>
		</td>
	</tr>	
	</c:if>
	<tr>
		<td colspan="2">
			<decorator:body/>  
		</td>
	</tr>
	<c:if test="${!empty helpInfo}">
	<tr>
		<td height="5" colspan="2">&nbsp;</td>
	</tr>		
	<tr>
		<td colspan="2">
			<div style="display:block;" id="help_info">
				<table width="100%" class="skinLayout" border="0" height="<msg:message code="info.prompt.help.height"/>" cellspacing="0" cellpadding="0">
					<tr>
						<td width="80" align="right"><msg:message code="info.prompt.help"/></td>
						<td>
							&nbsp;${helpInfo}
						</td>
					</tr>
				</table>
			</div>		
		</td>
	</tr>
	</c:if>	
</table>
</body>
</html>
