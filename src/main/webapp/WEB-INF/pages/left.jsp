<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib prefix="page" uri="/WEB-INF/tlds/sitemesh-page.tld" %>
<%@ taglib prefix="security" uri="/WEB-INF/tlds/springframework-security.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/tlds/springframework-spring.tld" %>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/springframework-form.tld" %>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<title><msg:message code="system.name"></msg:message></title>
<div id="sideBar">
<c:forEach var="pms1" items="${pmsLevelList1}" varStatus="vs1">
<security:authorize ifAnyGranted="${pms1.code}">
<div id="tb_${vs1.index}">
	<a href="#" id="${pms1.code}"  class="menuLink"><img src="${base}/${skin}/${pms1.icon}" align="absmiddle"  class="icon"/>${pms1.name}</a>
</div>
<div id="startMenuUl${pms1.code}" class="start-menu-div" style="display:none;">
	<div class="start-menu-head-div"></div>
	<div class="start-menu-triangle-div" id="start-menu-triangle-div-${pms1.code}"></div>
	<div id="startMenuUl${pms1.code}" class="start-menu-middle-div">	
		<ul class="start-menu-ul">
		<c:forEach var="pms2" items="${pmsLevelList2}" varStatus="vs2">
		<c:if test="${pms2.parentCode == pms1.code&&pms2.code !='511'}">
			<security:authorize ifAnyGranted="${pms2.code}">
			<li class="start-menu-middle-li">
				<div>
				<a href="#" id="${pms2.code}">
				<img src="${base}/${skin}/${pms2.icon}" width="18" height="18" align="absmiddle">
				${pms2.name}
				</a>
				</div>
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
</div>