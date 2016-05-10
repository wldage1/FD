<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	//产品数据
	var datas;
	//居间比例数组
	var comms;
	//当前页iframe ID
	var iframeID = window.frameElement.id;
	//弹出层的编号
	var k;
	//子产品ID
	var subID;
	
	$(document).ready(function(){
		jQuery("#productList_select").jqGrid({
	        url: '${base}/product/manage/gridTwo.json',
	        datatype: 'local',
	        colNames: [ "",
			            "<msg:message code='order.product.shortName'/>",
				        "<msg:message code='product.code'/>",
				        "<msg:message code='product.operate'/>",
				        "",
				        "",
				        ""
	        		  ],
	        colModel: [
			            {name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
			            {name: 'shortName',index: 'shortName',width:'35%',align:'center',hidden: false,sortable: false}, 
						{name: 'code',index: 'code',width:'30%',align:'center',hidden: false,sortable: false},
						{name: 'act',index:'act', width:'25%',align:'center',hidden:false,sortable:false,formatter:initAct},
						{name: 'provider.shortName',index: 'shortName',width:'0%',hidden: true},
						{name: 'providerId',index: 'providerId',width:'0%',hidden: true},
						{name: 'type',index: 'type',width:'0%',hidden: true}
					  ],
			mtype:"POST",
	        height:	80,
	        autowidth: true,
	        jsonReader: {
	        	repeatitems: false
	        },
	        caption:'<msg:message code="product.list"/>',
	        toolbar: [false,"top"]
		});
		//查询按钮
		$("#productSelect").click(function(){
			var tradeType='${order.tradeType}';
			if(tradeType==2){
				jQuery("#productList_select").jqGrid("setGridParam",{datatype:'json',postData:{shortName:$("#productShortName").val(),type:tradeType}}).trigger("reloadGrid");	
			}else{
				jQuery("#productList_select").jqGrid("setGridParam",{datatype:'json',postData:{shortName:$("#productShortName").val()}}).trigger("reloadGrid");
			}
			 
		});
		//投资金额  绑定到到账金额、已分配份额
		$("#investAmount").change(function(){
			$("#payAmount").val(this.value);
			$("#share").val(this.value);
			makePayProgress();
			//判断产品是否为空
			if(this.value){
				countExpectProfitRatio();
			}
		});
		
		//到账金额  绑定到打款状态
		$("#payAmount").change(function(){
			makePayProgress();
		});
		
		//已分配份额  绑定到打款状态
		$("#share").change(function(){
			makePayProgress();
		});
		
		function makePayProgress(){
			var payAmount = (Number)($("#payAmount").val());
			var share = (Number)($("#share").val());
			if(payAmount == 0){
				$("#payProgress").val(0);
				//$("#payProgressName").text("<msg:message code='payProgress.notyet'/>");
			}else if(payAmount < share){
				$("#payProgress").val(1);
				//$("#payProgressName").text("<msg:message code='payProgress.part'/>");
			}else if(payAmount == share){
				$("#payProgress").val(2);
				//$("#payProgressName").text("<msg:message code='payProgress.all'/>");
			}else if(payAmount > share){
				$("#payProgress").val(3);
				//$("#payProgressName").text("<msg:message code='payProgress.over'/>");
			}
		}
		
		//保存提交按钮
		$("#btn_save").click(function(){
			this.form.submit();
		});
		
		//验证
		var options = {
				event:'focus',
				ajax:{
					url:'${base}/market/order/valid.json'
				},
				validStatus:function(status){
					//status 返回结果为true 代表验证全部通过
					if (status){
						//提交表单
						$("form#order").submit();
					}
				}
			};
		$("form #btn_save").valid(options);
		
		//客户类型改变时事件
		$("#clientType").change(function(){
			//1为个人 2为企业
			if(this.value==1){
				$("#cardTypeMs").show();
				$("#cardTypeVl").show();
			}else{
				$("#cardTypeMs").hide();
				$("#cardTypeVl").hide();
			}
		});
    });
	
	//选择产品按钮
	function selectedProduct(v){
		datas=v;
		if(datas.type==1){//代表信托 
			$("#netValueTrIdTwo").hide();
			$("#netValue").removeClass("valid-control");
		}else if(datas.type==2){//阳光私募
			$("#netValueTrIdTwo").show();
			$("#netValue").addClass("valid-control");
		}else{
			$("#netValueTrIdTwo").hide();
			$("#netValue").removeClass("valid-control");
		}
		//收益权类型选择
		checkSubProduct();
		//产品名称
		$("#productName").val(datas.name);
		$("#productID").val(datas.id);
		//发行机构简称
		$("#providerShortName").val(datas.provider.shortName);
		$("#providerID").val(datas.providerId);
		//居间公司id
		$("#orgID").val(datas.orgId);
		$("#contractPrefix").val(datas.contractPrefix);
	}
	
	//获取理财顾问信息
	function getMembers(mID,mName,teamID,orgType,orgID,tName){
		$("#memberID").val(mID);
		$("#memberName").val(mName);
		if(tName==null || tName=='' || tName=='null' || tName=='undefined'){
			$("#teamName").val("");
		}else{
			$("#teamName").val(tName);			
		}
		$("#teamID").val(teamID);
	}
	
	//根据基本信息  初始化页面字段
	function selectBenefit(d){
		//获取选择的受益权类型数组下标
		var i=$("#subProductList",top.document).val();
		i = i == null || i == "" ? 0 : i;
		//子产品数据
		var data=datas.subProductList[i];
		//显示最大预期收益率
		if(data.profitType==1){//浮动
			if(data.profitRatioDesc!=null && data.profitRatioDesc!=''){
				$("#profitRatio").val(data.profitRatioDesc);		
			}else{
				$("#profitRatio").val("<msg:message code='profitRatio.float'/>");
			}
		}else{
			//计算预期收益率
			subID=data.id;
			countExpectProfitRatio();
		}
		//子产品ID
		$("#subProductID").val(data.id);
		//收益权类型
		if(data.type==null || data.type==''){
			$("#subproductType").html(datas.shortName);	
		}else{
			$("#subproductType").html(data.type);
		}
		//产品期限(月)
		if(data.deadline == -1){
			$("#deadline").val(data.deadline);
			$("#deadlineShow").empty().append('<msg:message code="order.deadline.unlimited"/>');
		}else{
			$("#deadline").val(data.deadline);
			$("#deadlineShow").empty().append(data.deadline);
		}
		//关闭层
		if(d==1){
			closeTopLayer();
		}
	}
	
	//计算 预期收益率
	function countExpectProfitRatio(){
		if(subID!=null && $("#investAmount").val()!=null && $("#investAmount").val()!=''){
			$.ajax({
				url:'${base}/market/order/countExpectProfitRatio.json?c=${order.c}&subProductID=' + subID + '&investAmount=' + $("#investAmount").val(),
				type:'post',
				timeout:'60000',
				dataType:'json',
				success:function(jsonData){
					$("#profitRatio").val(jsonData.profitRatio);
				}
			});	
		}
	}
	
	//查询子产品信息
	function checkSubProduct(){
		//判断信托是否有受益权类型    满足条件 代表有
		if(datas.subProductList.length>1){
			var htm='<select multiple="multiple" name="subProductList" id="subProductList" style="width: 250px;height: 150px;">';
			$.each(datas.subProductList,function(i,n){
				var tempSubName="";
				if(n.type==null || n.type==""){
					tempSubName=datas.shortName;
				}else{
					tempSubName=n.type;
				}
				htm += "<option value='"+i+"'>"+tempSubName+"</option>";
			});
			htm+='</select>';
			//页面层例二
			k = top.$.layer({
				type: 1,
				title: '<msg:message code="order.product.child.type"/>',
				shadeClose: true, 
				closeBtn: [0,true],
				border : [5, 0.5, '#666', true],
				offset: ['50px',''],
				move: ['.xubox_title', true],
				area: ['520px','auto'],
				page: {
					html:'<table width="520" border="0" cellspacing="0" cellpadding="0">'+
							'<tr>'+
								'<td width="100%" class="skinLayout_info" colspan="2">'+ 
									'<table width="518" border="0" height="200" cellspacing="1" cellpadding="0" class="skinLayout">'+
										'<tr>'+
											'<td align="right" width="40%" class="skinLayout_info">'+
												'<msg:message code="product.name" />：'+
											'</td>'+
											'<td align="left" width="60%" class="skinLayout_info">'+datas.name+'</td>'+
										'</tr>'+
										'<tr>'+
											'<td align="right" width="40%" class="skinLayout_info">'+
												'<msg:message code="redeemorder.subproduct.type"/>：'+
											'</td>'+
											'<td align="left" width="60%" class="skinLayout_info">'+htm+'</td>'+
										'</tr>'+
									'</table>'+
			    				'</td>'+
		    				'</tr>'+
							'<tr height="40" bgcolor="#CCDCFF">'+
			    				'<td align="right" width="50%"><input type="button" onclick="document.getElementById(\''+ iframeID +'\').contentWindow.selectBenefit(1);"  id="updateStatus" value="确定"/>&nbsp;&nbsp;</td>'+
			    				'<td align="left"  width="50%">&nbsp;&nbsp;<input type="button" onclick="document.getElementById(\''+ iframeID +'\').contentWindow.closeTopLayer();" value="<msg:message code="product.close.button" />"/></td>'+
							'</tr>'+
						'</table>'
				}
			});
		}else{//信托产品  对应无受益权类型
			selectBenefit(2);
		}
	}
	
	//初始化列表后的操作
	function initAct(cellvalue, options, rowObject){
			var id = rowObject.id;
			var rowdata = rowObject;
			var content = "";
			<!-- 确定 -->
	    	content += "<a href='javascript:void(0);' id='" + id + "' onclick='selectedProduct("+$.toJSON(rowdata)+")' title='<msg:message code='select.button'/>'>";
	    	content += "<font color='#0000FF' title='<msg:message code='select.button'/>'><msg:message code='select.button'/></font>";
	    	content += "</a>";
	    	return content;
	}
	
	//选择理财顾问
	function selectMemeber(){
		//iframe层例二
		var url='${base}/market/order/member?iframeID='+iframeID;
		k=top.$.layer({
			type: 2,
			title: "<msg:message code='member.list'/>",
			fix: false,
			closeBtn: true,
			shadeClose: true,
			shade: [0.5,'#B0B1B2', true],
			closeBtn:[0,true],
			border : [5, 0.3, '#666', true],
			offset: ['100px',''],
			area: ['660px','370px'],
			iframe: {src: url},
			success: function(){
				//layer.msg('点击层外任意处，可关闭该iframe层', 2, 9);
			}
		});
	}
	
	//通用关闭
	function closeTopLayer(){
		top.layer.close(k);
	}
	
	//计算 居间费比例
	function setJuJianBl(){
		//判断是否有挂靠
		if($("#isOrNotGk").val()!=null && $("#isOrNotGk").val()!=''){
			//为0 代表挂靠机构
			if($("#isOrnotOrg").val()==0){
				//按照机构居间比例计算
				$("#commissionPercent").val(comms[1]);
			}else{
				//按照个人居间比例计算
				$("#commissionPercent").val(comms[0]);
			}				
		}else{
			//按照个人居间比例计算
			$("#commissionPercent").val(comms[0]);
		}
	}
	// 数字
	function checkNumber(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		//var re = /^-?[1-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		var re = /^-?[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(key != 8){
			var value = $(object).val();
			if(!re.test(value)){
				$(object).val("");
			}
		}
	}
		
	function contractNum(){
		if($("#contractSuffix").val()==""){
			$("#contractNumber").val("");
		}else{
			$("#contractNumber").val(($("#contractPrefix").val()).concat($("#contractSuffix").val()));
		}
	}
</script>
</head>
<body class="skinMain">
<form:form method="post" id="order" action="saveorupdate" commandName="order">
<form:hidden path="c"/>
<input type="hidden" name="turnCode" value="${market_order.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<input type="hidden" name="prompt" value="orderNumber" />
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr>
					<td align="right" width="30%" class="search_info" height="25"><msg:message code='product.shortName'/><msg:message code="system.common.sign.colon"/></td>
					<td align="left" width="50%"  class="search_lable"><input type="text" id="productShortName" /></td>
					<td class="search_info"><input type="button" id="productSelect" value="<msg:message code='accurate.select.search'/>" /></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="productList_select"><tr><td>&nbsp;</td></tr></table>
		</td>
	</tr>
</table>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
		<form:hidden path="subProductID" id="subProductID"/>
		<form:hidden path="productID" id="productID"/>
		<form:hidden path="providerID" id="providerID"/>
		<form:hidden path="memberID" id="memberID"/>
		<form:hidden path="teamID" id="teamID"/>
		<form:hidden path="orgID" id="orgID"/>
		<tr>
			<td width="100%">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
							<tr>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="redeemorder.subproduct.type"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" id="subproductType"></td>
								<td align="right" class="skinLayout_info"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" >
									<input id="contractPrefix" name="contractPrefix" type="text" disabled="disabled">
									-
									<input id="contractSuffix" name="contractSuffix" type="text" onchange="contractNum()">
									<input type="text" id="contractNumber" name="contractNumber" class="valid-control" style="height: 0;width: 0;">
								</td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.investAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="investAmount" id="investAmount" class="valid-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.payAmount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"><form:input path="payAmount" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="product.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input id="productName" path="productName"  readonly="true" class="valid-control"/><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.org.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" width="30%"><input id="providerShortName" disabled="disabled"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.member.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><input id="memberName" name="memberName" readonly="readonly"  class="valid-control"/><a href="javascript:void(0);" onclick="selectMemeber();"><msg:message code="select.button" /></a><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><input id="teamName" disabled="disabled"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="clientType" cssStyle="width:150px;"><c:forEach items="${clientTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								<td align="right" class="skinLayout_info" id="cardTypeMs" ><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" id="cardTypeVl"><form:select path="iDCardType" cssStyle="width:150px;"><c:forEach items="${iDCardTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="clientName" class="valid-control"/><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="iDCard" class="valid-control"/><font color="red">&nbsp;&nbsp;*</font></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.tradeType"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="tradeType" cssStyle="width:150px;"><c:forEach items="${tradeTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.deadline"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:hidden path="deadline" id="deadline"/><span id="deadlineShow"></span></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.profitRatio"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="profitRatio" id="profitRatio" class="valid-control" readonly="true"/><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.orderTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="orderTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate valid-control" readonly="readonly"/><font color="red">&nbsp;&nbsp;*</font></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.share"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  ><form:input path="share" class="valid-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.payTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="payTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.payProgress"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="payProgress" cssStyle="width:150px;" class="valid-control"><c:forEach items="${payProgressMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.commissionPayTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:input path="commissionPayTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate valid-control" readonly="readonly" /><font color="red">&nbsp;&nbsp;*</font></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.documentStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="documentStatus" cssStyle="width:150px;"><c:forEach items="${documentStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" ><form:select path="tradeStatus" cssStyle="width:150px;"><c:forEach items="${tradeStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr id="netValueTrIdTwo" style="display:none;">
								<td align="right" class="skinLayout_info" ><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left"  colspan="3"><form:input path="netValue" class="valid-control" onkeyup="checkNumber(event,this,'edit');" onpropertychange="checkNumber(event,this,'edit');" onfocusout="checkNumber(event,this,'paste');" oninput="checkNumber(event,this,'edit');"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.docStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" colspan="3"><form:select path="docStatus"  cssStyle="width:150px;"><form:option value="0">未归集</form:option><form:option value="1">已归集</form:option></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" colspan="3"><form:textarea path="remark" /></td>
							</tr>
							<tr ><td height="30">&nbsp;</td></tr>
						</table>
						<div class="toolbar">
							<div class="left"></div>
								<div class="center">
									<input type="button" class="btn2" id="btn_save" value="<msg:message code="button.save"/>" />
									<input type="button" class="btn2" onclick="location.href='${base}${market_order.controller}?c=${market_order.code}'" name="btnback" value="<msg:message code="button.back"/>" id="btnback"/>
								</div>
							<div class="right"></div>
						</div>
					</td>
				</tr>
			</table>
	</form:form>
</html>