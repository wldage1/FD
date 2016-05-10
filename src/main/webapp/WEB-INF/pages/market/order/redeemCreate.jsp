<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">
	var datas;
	var temp = 0;//判断是否选择产品
	$(document).ready(function(){
		//兼容IE和火狐
		if(window.ActiveXObject){
			$("#netValue").attr("onpropertychange","Amount()");
			$("#share").attr("onpropertychange","Amount()");
		}else{
			$("#netValue").attr("oninput","Amount()");
			$("#share").attr("oninput","Amount()");
		}
		
		jQuery("#productList_select").jqGrid({
	        url: '#',
	        datatype: 'json',
	        colNames: ["",
	            "<msg:message code='order.product.shortName'/>",
		        "<msg:message code='order.clientName'/>",
		        "<msg:message code='order.contractNumber'/>",
		        "<msg:message code='product.operate'/>",
		        "","","","","","","",""
		        ],
	        colModel: [
	            {name: 'id',index: 'id',width:'1%',align:'center',hidden: true,sortable: false},
	            {name: 'productName',index: 'productName',width:'25%',align:'center',hidden: false,sortable: false}, 
	            {name: 'clientName',index: 'clientName',width:'25%',align:'center',hidden: false,sortable: false}, 
				{name: 'contractNumber',index: 'contractNumber',width:'25%',align:'center',hidden: false,sortable: false},
				{name:'act',index:'act', width:'25%',align:'center',hidden:false,sortable:false,formatter:initAct},
				{name: 'provider.shortName',index: 'provider.shortName',width:'1%',align:'center',hidden: true,sortable: false},
				{name: 'provider.id',index: 'provider.id',width:'1%',align:'center',hidden: true,sortable: false},
				{name:'teamID',index:'teamID',width:'1%',align:'center',hidden: true,sortable: false},
				{name:'team.shortName',index:'team.shortName',width:'1%',align:'center',hidden: true,sortable: false},
				{name:'orgID',index:'orgID',width:'1%',align:'center',hidden: true,sortable: false},
				{name:'productID',index:'productID',width:'1%',align:'center',hidden: true,sortable: false},
				{name:'memberID',index:'memberID',width:'1%',align:'center',hidden: true,sortable: false},
				{name:'member.name',index:'member.name',width:'1%',align:'center',hidden: true,sortable: false}
				],
	        mtype:"POST",
	        height:80,
	        rowNum:1000,
	        autowidth: true,
	        jsonReader: {
	        	repeatitems: false
	        },
	        caption:'<msg:message code="holdingproduct.list"/>',
	        toolbar: [false,"top"]
		});

		//查询按钮
		$("#productSelect").click(function(){
			jQuery("#productList_select").jqGrid("setGridParam",{url:'${base}/market/order/selectHoldingProduct.json',postData:{productName:$("#productShortName").val(),clientName:$("#selectClientName").val()}}).trigger("reloadGrid");	
	    });
		//投资金额  绑定到到账金额
		$("#investAmount").change(function(){
			$("#payAmount").val(this.value);
		});
		
	var options = {
			event:'focus',
			ajax:{
				url:'${base}/market/redeemorder/valid.json'
			},
			validStatus:function(status){
				//status 返回结果为true 代表验证全部通过
				if (status){
					//提交表单
					if(temp == 1){
						$("form#redeemOrder").submit();
					}else{
						top.$.layer({
							shade : [0.5 , '#000' , true],
					  		offset : ['50px',''],
					  	 	title : "<msg:message code='info.prompt'/>",
						    dialog : {msg:"请选择合适的产品",type : 3}	
						});
					}
				}
			}
		};
	$("form #btn_save").valid(options);
	
	//客户类型改变事件
	$("#clientType").change(function(){
		if(this.value==1){
			$("#cardTypeMs").show();
			$("#cardTypeVl").show();
		}else{
			//证件类型
			$("#iDCardType").val("");
			$("#cardTypeMs").hide();
			$("#cardTypeVl").hide();
		}
	});
		
	});
	//当前页iframe ID
	var iframeID = window.frameElement.id;
	//弹出层的编号
	var k;
	
	//选择按钮
	function selectedProduct(v){
			temp = 1;
			datas=v;
			//产品名称
			$("#productName").val(datas.productName);
			//产品ID
			$("#productID").val(datas.productID);
			//自产品ID 
			$("#subProductID").val(datas.subProductID);
			//发行机构简称
			$("#providerName").val(datas.provider.shortName);
			$("#providerID").val(datas.provider.id);
			//合同号
			$("#contractNumber").val(datas.contractNumber);
			//客户名称
			$("#clientName").val(datas.clientName);
			//客户类型
			$("#clientType").val(datas.clientType);
			if(datas.clientType==1){
				//证件类型
				$("#iDCardType").val(datas.iDCardType);	
				$("#cardTypeMs").show();
				$("#cardTypeVl").show();
			}else{
				//证件类型
				$("#iDCardType").val("");
				$("#cardTypeMs").hide();
				$("#cardTypeVl").hide();
			}
			//证件号码
			$("#iDCard").val(datas.iDCard);
			//理财顾问新姓名
			$("#memberName").val(datas.member.name);
			$("#memberID").val(datas.memberID);
			$("#teamID").val(datas.teamID);
			$("#teamName").val(datas.team.shortName);
			$("#holdingProductID").val(datas.id);
			$("#orgID").val(datas.orgID);
		}
	
	//通用关闭
	function closeTopLayer(){
		top.layer.close(k);
	}
	
	//初始化列表后的操作
	function initAct(cellvalue, options, rowObject){
			var id = rowObject.id;
			var rowdata = rowObject;
			var content = "";
			<!-- 确定 -->
	    	content += "<a href='javascript:void(0);' id='" + id + "' onclick='selectedProduct("+$.toJSON(rowdata)+")' title='<msg:message code='order.true.button'/>'>";
	    	content += "<img src='${base}/${skin}/images/icon/order processing.png'  weight='18' height='18' border='0' align='absmiddle'><font color='#0000FF' title='<msg:message code='order.true.button'/>'><msg:message code='select.button'/></font>";
	    	content += "</a>";
	    	return content;
	}
	
function Amount(){
	$("#temp").val("1");
	var share = $("#share").val();
	var netValue = $("#netValue").val();
	if(share!=null && netValue!=null ){
		$("#amount").val((share * netValue/10000).toFixed(4));
		if($("#amount").val() == "NaN"){
			$("#amount").val(0)
			$("#temp").val("");
		}
	}
}

function clearNoNum(obj)	{		
	//先把非数字的都替换掉，除了数字和.		
	obj.value = obj.value.replace(/[^\d.]/g,"");		
	//必须保证第一个为数字而不是.		
	obj.value = obj.value.replace(/^\./g,"");		
	//保证只有出现一个.而没有多个.		
	obj.value = obj.value.replace(/\.{2,}/g,".");		
	//保证.只出现一次，而不能出现两次以上		
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");	
	}

</script>
</head>
<body class="skinMain">
<form:form method="post" id="redeemOrder" action="saveredeem" commandName="redeemOrder">
<form:hidden path="c"/>
<form:hidden path="id"/>
<form:hidden path="temp" value="1"/>
<form:hidden path="holdingProductID"/>
<input type="hidden" name="turnCode" value="${market_order.code}"/>
<input type="hidden" name="coverParam" value="1"/>
<input type="hidden" name="prompt" value="orderNumber" />
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr>
					<td align="right" width="10%" class="search_info" height="25"><msg:message code='product.shortName'/><msg:message code="system.common.sign.colon"/></td>
					<td align="left" width="30%"  class="search_lable"><input type="text" id="productShortName" /></td>
					<td align="right" width="10%" class="search_info" height="25"><msg:message code='order.clientName'/><msg:message code="system.common.sign.colon"/></td>
					<td align="left" width="30%"  class="search_lable"><input type="text" id="selectClientName" /></td>
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
		<form:hidden path="productID" id="productID"/>
		<form:hidden path="providerID" id="providerID"/>
		<form:hidden path="memberID" id="memberID"/>
		<form:hidden path="teamID" id="teamID"/>
		<form:hidden path="orgID" id="orgID" />
		<form:hidden path="subProductID" id="subProductID" />
		<tr>
			<td width="100%">
					<table align="center" width="100%" border="" cellspacing="1" cellpadding="0" class="skinLayout" >
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.contractNumber"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""><form:input path="contractNumber" readonly="true"/></td>
								<td align="right" class="skinLayout_info"  ><msg:message code="order.tradeStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:select path="tradeStatus" cssStyle="width:150px;"><c:forEach items="${tradeStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.product.shortName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""><form:input path="productName" readonly="true"/><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="product.org.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%"><form:input path="providerName" readonly="true"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.member.name"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="memberName" readonly="true"/></td>
								<td align="right" class="skinLayout_info"><msg:message code="order.teamID"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="teamName" readonly="true"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.clientTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:select path="clientType" cssStyle="width:150px;"><c:forEach items="${clientTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
								<td align="right" class="skinLayout_info" id="cardTypeMs" ><msg:message code="order.iDCardTypeName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" id="cardTypeVl"><form:select path="iDCardType" cssStyle="width:150px;"><form:option value=""><msg:message code="redeemorder.idcardtype.select"/></form:option><c:forEach items="${iDCardTypeMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.clientName"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="clientName" readonly="true"/></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.iDCard"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="iDCard" class="valid-control"/><font color="red">&nbsp;&nbsp;*</font></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="order.netValue"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""><form:input path="netValue" class="valid-control" onkeyup="clearNoNum(this)"/><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" ><msg:message code="order.documentStatus"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:select path="documentStatus" cssStyle="width:150px;"><c:forEach items="${documentStatusMap}" var="it"><form:option value="${it.key}">${it.value}</form:option></c:forEach></form:select></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"><msg:message code="redeemorder.shares"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class=""><form:input path="share" class="valid-control" onkeyup="clearNoNum(this)" /><font color="red">&nbsp;&nbsp;*</font></td>
								<td align="right" class="skinLayout_info" ><msg:message code="redeemorder.tradeTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="tradeTime" maxlength="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate" readonly="readonly"/></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info"  ><msg:message code="redeemorder.amount"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" ><form:input path="amount" readonly="true" class="valid-control"/></td>
								<td align="right" class="skinLayout_info" width="20%"><msg:message code="redeemorder.openTime"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" width="30%"><span id="code" name="code"><form:input path="openTime" maxlength="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="Wdate" readonly="readonly"/></span></td>
							</tr>
							<tr>
								<td align="right" class="skinLayout_info" ><msg:message code="order.remark"/><msg:message code="system.common.sign.colon"/></td>
								<td align="left" class="" colspan="3"><form:textarea path="remark" style="width:280px;height:80px;"/></td>
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