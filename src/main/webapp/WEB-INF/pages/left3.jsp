<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib prefix="page" uri="/WEB-INF/tlds/sitemesh-page.tld" %>
<%@ taglib prefix="security" uri="/WEB-INF/tlds/springframework-security.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/tlds/springframework-spring.tld" %>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/springframework-form.tld" %>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<div class="menu_bg_top"></div>
<div id="sideBar">
<table width="69" border="0" cellpadding="0" cellspacing="0" >
<c:forEach var="pms1" items="${pmsLevelList1}" varStatus="vs1">
<security:authorize ifAnyGranted="${pms1.code}">
<tbody id="tb_${vs1.index}">
	<tr>
		<th width="68" height="68" valign="top" align="center">
			<div style="margin: 3px 3px 3px 3px" class="menu-items-div" id="index_${pms1.code}" align="center">
				<div style="width:60px;height:35px;padding-top:3px;">
					<a href="#" id="${pms1.code}"><img src="${base}/${skin}/${pms1.icon}" align="absmiddle"/></a>
				</div>
				<div style="width:60px;height:20px;">${pms1.name}</div>
			</div>
		</th>
	</tr>
</tbody>
<div id="startMenuUl${pms1.code}" class="start-menu-div" style="display:none">
	<div class="start-menu-head-div"></div>
	<div class="start-menu-triangle-div" id="start-menu-triangle-div-${pms1.code}"></div>
	<div id="startMenuUl${pms1.code}" class="start-menu-middle-div">	
		<ul class="start-menu-ul">
		<c:forEach var="pms2" items="${pmsLevelList2}" varStatus="vs2">
		<c:if test="${pms2.parentCode == pms1.code}">
			<security:authorize ifAnyGranted="${pms2.code}">
			<li class="start-menu-middle-li">
				<a href="#" id="${pms2.code}"><img src="${base}/${skin}/${pms2.icon}" align="absmiddle"/>${pms2.name}</a>
			</li>
			</security:authorize>
		</c:if>
		</c:forEach> 				
		</ul>
	</div>
	<div class="start-menu-bottom-div"></div>
</div>              
</security:authorize>                 
</c:forEach>
</table>
</div>


