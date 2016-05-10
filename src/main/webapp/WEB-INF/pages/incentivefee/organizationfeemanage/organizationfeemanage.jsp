<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
	var payStatus = 0;
	var type = 0;
	$(document).ready(function(){
		jQuery("#unfeeList").jqGrid({
			url:'organizationfeemanage/grid.json',
			datatype: "json",
			colNames:['','','机构简称','累计返利成交规模（万）','成功系数','奖励分值（万）','返利分值（万）','居间费总额（元）','返利比例','返利金额（元）','操作'],
			colModel:[{name:'id', index:'o.id', align:'center', hidden:true, sortable:false},
			          {name:'incentiveFeeId', index:'o.incentiveFeeId', align:'center', hidden:true, sortable:false},
			          {name:'shortName', index:'o.shortName', align:'center', hidden:false, sortable:false},
			          {name:'sumInvestAmount', index:'o.sumInvestAmount', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  return (rowdata.sumInvestAmount).toFixed(2);
			          }},
			          {name:'successRate', index:'o.successRate', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  if(rowdata.successRate == '' || rowdata.successRate == null){
			        		  return 0;
			        	  }else{
			        		  return (rowdata.successRate*100).toFixed(2)+'%';
			        	  }
			          }},
			          {name:'rewardScore', index:'o.rewardScore', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  if(rowdata.rewardScore =='' || rowdata.rewardScore == null){
			        		  return 0;
			        	  }else{
			        		  return rowdata.rewardScore;
			        	  }
			          }},
			          {name:'rebateScore', index:'o.rebateScore', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  return (rowdata.rebateScore).toFixed(2);
			          }},
			          {name:'sumCommissionFee', index:'o.sumCommissionFee', align:'center', hidden:false, sortable:false},
			          {name:'rebateRate', index:'o.rebateRate', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  if(rowdata.rebateRate == '' || rowdata.rebateRate == null){
			        		  return 0;
			        	  }else{
			        		  return (rowdata.rebateRate * 100).toFixed(2)+'%';
			        	  }
			          }},
			          {name:'rebateAmount', index:'o.rebateAmount', align:'center', hidden:false, sortable:false,formatter:function(cellvalue, options, rowdata){
			        	if(rowdata.rebateAmount == '' || rowdata.rebateAmount == null){
			        		return 0;
			        	}else{
			        		return (rowdata.rebateAmount).toFixed(2);
			        	}
			          }},
			          {name:'act', index:'o.act', align:'left', hidden:false, sortable:false,
			        	   formatter:function(cellvalue, options, rowdata){
			        		   var id = rowdata.id;
			        		   var content = "";
			        		   if(rowdata.rebateAmount == null || rowdata.rebateAmount == ""){
			        			    content += "<a href='javascript:void(0);' style='color:#999;' title='发放'>";
			        			    content += "<img src='${base}/${skin}/${incentivefee_organizationfeemanage_pay.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_organizationfeemanage_pay.name}";
									content += "</a>";
									
			        		   }else{
			        			   payStatus = 1;
				        		   <c:if test="${!empty incentivefee_organizationfeemanage_pay}">
				        		   <security:authorize ifAnyGranted="${incentivefee_organizationfeemanage_pay.code}">
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_${incentivefee_organizationfeemanage_pay.indexCode}'";
				        		   		content += " title='${incentivefee_organizationfeemanage_pay.name}'>";
				        		   		content += "<img src='${base}/${skin}/${incentivefee_organizationfeemanage_pay.icon}' weight='18' height='18' border='0' align='absmiddle'/>${incentivefee_organizationfeemanage_pay.name}";
				        		   		content += "</a>";
				        		   </security:authorize>
				        		   </c:if>
			        		   }
			        		   return content;
			        	   }
			           }],
			postData: {c:"${incentiveFeeDetail.c}",isPay:"0",payStageTime:$("#stageTime").val(),companyID:$("#companyID").val(),type:type},
			height:<msg:message code='jqgrid.height.300'/>,
			autowidth: true,
			jsonReader: {
				repeatitems: false
			},
			subGrid : true,
			subGridRowExpanded: function(subgrid_id,row_id) {
				var auditingTime = $("#stageTime").val();
				var subgrid_table_id = subgrid_id+"_t";
				$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>");
				jQuery("#"+subgrid_table_id).jqGrid({
					url:'organizationfeemanage/subgrid.json',
					datatype: "json",
					colNames:['订单号码','产品简称', '客户名称','投资金额（万）','返利核算金额（万）','期限数据类型','产品类型','存续止期','居间费金额（元）','理财顾问'],
					colModel:[{name:'orderNumber',index:'o.orderNumber'},
					          {name:'proShortName',index:'o.proShortName'},
					          {name:'clientName',index:'o.clientName'},
					          {name:'payAmount',index:'o.payAmount'},
					          {name:'payAmount1',index:'payAmount1',formatter:function(cellvalue, options, rowObject){
					        	  if(rowObject.productType == 2){
					        		  return (rowObject.payAmount).toFixed(2);
					        	  }else{
					        		  if(rowObject.deadlineDataType == 1){
						        		  if(rowObject.deadline >= 12){
						        			  return (rowObject.payAmount).toFixed(2);
						        		  }else{
						        			  return (rowObject.payAmount*rowObject.deadline/12).toFixed(2) ;
						        		  }
					        		  }else{
					        			  return (rowObject.payAmount*rowObject.deadline/365).toFixed(2) ;
					        		  }
					        	  } 
					          }},
					          {name:'deadlineDataType',index:'deadlineDataType',hidden:true},
					          {name:'productType',index:'productType',hidden:true},
					          {name:'deadline',index:'deadline',hidden:true},
					          {name:'commission',index:'o.commission'},
					          {name:'memberName',index:'o.memberName',formatter:function(cellvalue, options, rowObject){
				       				return cellvalue+"（"+rowObject.memberNickName+"）";
				       			}}],
					postData: {c:"${incentiveFeeDetail.c}",teamID:row_id,auditingTime:auditingTime,orgID:$("#companyID").val(),isPay:"0",type:type},
					autowidth: true,
					jsonReader: {
						repeatitems: false
					}
				});
			},
			caption: "待发放返利费用列表"
		});
		//返利费用发放
		$(".shortcut_${incentivefee_organizationfeemanage_pay.indexCode}").live("click",function(){
			var orgId = $(this).attr("id");
			var rowdata = jQuery("#unfeeList").jqGrid('getRowData',orgId);
			var incentiveFeeId = rowdata.incentiveFeeId;
			var auditingTime = $("#stageTime").val();
			var url = "${base}${incentivefee_organizationfeemanage_pay.controller}.json?";
			var layers = top.layer.confirm("<msg:message code='incentive.ispay.comfirm' />",  function(){
						$.ajax({
							url:url,
							type:'post',
							timeout:'60000',
							dataType:'json',
							data:{c:'${incentivefee_organizationfeemanage_pay.code}',orgId:orgId,incentiveFeeId:incentiveFeeId,auditingTime:auditingTime,companyID:$("#companyID").val(),type:type,isPay:"0"},
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										top.layer.close(layers);
										payStatus = 0;
										$("#unfeeList").trigger("reloadGrid");
									}
								}
							}
						});
					});
		});
		
		 jQuery("#feedList").jqGrid({
			url:'organizationfeemanage/grid.json',
			datatype: "json",
			colNames:['','','机构简称','累计成交规模（万）','成功系数','奖励分值（万）','返利分值（万）','居间费总额（元）','返利比例','返利金额（元）'],
			colModel:[{name:'id', index:'o.id', align:'center', hidden:true, sortable:false},
			          {name:'incentiveFeeId', index:'o.incentiveFeeId', align:'center', hidden:true, sortable:false},
			          {name:'shortName', index:'o.shortName', align:'center', hidden:false, sortable:false},
			          {name:'sumInvestAmount', index:'o.sumInvestAmount', align:'center', hidden:false, sortable:false},
			          {name:'successRate', index:'o.successRate', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  if(rowdata.successRate == '' || rowdata.successRate == null){
			        		  return 0;
			        	  }else{
			        		  return (rowdata.successRate*100).toFixed(2)+'%';
			        	  }
			          }},
			          {name:'rewardScore', index:'o.rewardScore', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  if(rowdata.rewardScore =='' || rowdata.rewardScore == null){
			        		  return 0;
			        	  }else{
			        		  return rowdata.rewardScore;
			        	  }
			          }},
			          {name:'rebateScore', index:'o.rebateScore', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  return (rowdata.rebateScore).toFixed(2);
			          }},
			          {name:'sumCommissionFee', index:'o.sumCommissionFee', align:'center', hidden:false, sortable:false},
			          {name:'rebateRate', index:'o.rebateRate', align:'center', hidden:false, sortable:false,formatter:function(cellvalue,options,rowdata){
			        	  if(rowdata.rebateRate == '' || rowdata.rebateRate == null){
			        		  return 0;
			        	  }else{
			        		  return (rowdata.rebateRate * 100).toFixed(2)+'%';
			        	  }
			          }},
			          {name:'rebateAmount', index:'o.rebateAmount', align:'center', hidden:false, sortable:false,formatter:function(cellvalue, options, rowdata){
				        	if(rowdata.rebateAmount == '' || rowdata.rebateAmount == null){
				        		return 0;
				        	}else{
				        		return (rowdata.rebateAmount).toFixed(2);
				        	}
				          }}],
			postData: {c:"${incentiveFeeDetail.c}",isPay:"1",payStageTime:$("#stageTime1").val(),companyID:$("#companyID1").val(),type:type},
			height:<msg:message code='jqgrid.height.300'/>,
			autowidth: true,
			jsonReader: {
				repeatitems: false
			},
			subGrid : true,
			subGridRowExpanded: function(subgrid_id1,row_id) {
				var stageTime1 = $("#stageTime1").val();
				var subgrid_table_id1 = subgrid_id1+"_t";
				$("#"+subgrid_id1).html("<table id='"+subgrid_table_id1+"' class='scroll'></table>");
				jQuery("#"+subgrid_table_id1).jqGrid({
					url:'organizationfeemanage/subgrid.json',
					datatype: "json",
					colNames:['订单号码','产品简称', '客户名称','投资金额（万）','实际成交金额（万）','期限数据类型','产品类型','存续止期','居间费金额（元）','理财顾问'],
					colModel:[{name:'orderNumber',index:'o.orderNumber'},
					          {name:'proShortName',index:'o.proShortName'},
					          {name:'clientName',index:'o.clientName'},
					          {name:'payAmount',index:'o.payAmount'},
					          {name:'payAmount1',index:'payAmount1',formatter:function(cellvalue, options, rowObject){
					        	  if(rowObject.productType == 2){
					        		  return (rowObject.payAmount).toFixed(2);
					        	  }else{
					        		  if(rowObject.deadlineDataType == 1){
						        		  if(rowObject.deadline >= 12){
						        			  return (rowObject.payAmount).toFixed(2);
						        		  }else{
						        			  return (rowObject.payAmount*rowObject.deadline/12).toFixed(2) ;
						        		  }
					        		  }else{
					        			  return (rowObject.payAmount*rowObject.deadline/365).toFixed(2) ;
					        		  }
					        	  } 
					          }},
					          {name:'deadlineDataType',index:'deadlineDataType',hidden:true},
					          {name:'productType',index:'productType',hidden:true},
					          {name:'deadline',index:'deadline',hidden:true},
					          {name:'commission',index:'o.commission'},
					          {name:'memberName',index:'o.memberName',formatter:function(cellvalue, options, rowObject){
				       				return cellvalue+"（"+rowObject.memberNickName+"）";
				       			}}],
					postData: {c:"${incentiveFeeDetail.c}",teamID:row_id,auditingTime:stageTime1,orgID:$("#companyID1").val(),isPay:"1",type:type},
					autowidth: true,
					jsonReader: {
						repeatitems: false
					}
				});
			},
			caption: "已发放返利费用列表"
		}); 
	});
	//返利费用核算
	function check(){
		var teamIDs = jQuery("#unfeeList").jqGrid('getDataIDs');
		var payStageTime = $("#stageTime").val();
		if(payStageTime==null){
			top.$.layer({
				shade  : [0.5 , '#000' , true],
		  		offset : ['50px',''],
		  	 	title  : "<msg:message code='info.prompt'/>",
			    dialog : {msg:"请设置业绩返利阶段参数！",type : 3}	
			});
		}else{
			var url = "${base}${incentivefee_organizationfeemanage_check.controller}.json";
			$.ajax({
				url:url,
				type:'post',
				traditional: true,
				timeout:'60000',
				dataType:'json',
				data:{c:'${incentivefee_organizationfeemanage_check.code}',teamIDs:teamIDs,payStageTime:payStageTime,companyID:$("#companyID").val(),type:type},
				success:function(jsonData){
					if(jsonData.status == "success"){
						payStatus = 0;
						$("#unfeeList").trigger("reloadGrid");
					}
				}
			});
		}
	}
	//返利费用一键发放
	function oneKeyPay(){
		if(payStatus==0){
			top.layer.msg("没有可发放返利费用！");
		}else {
			var orgIds = jQuery("#unfeeList").jqGrid('getDataIDs');
			var incentiveFeeIds = new Array();
			var temp = 0;
			var sourceIds = new Array();
			for (var i in orgIds) {
				var rowdata = jQuery("#unfeeList").jqGrid('getRowData',orgIds[i]);
				if(rowdata.rebateAmount != 0 && rowdata.rebateAmount != null && rowdata.rebateAmount !=''){
					incentiveFeeIds[temp] = rowdata.incentiveFeeId;
					sourceIds[temp] = rowdata.id;
					temp++;
				}
			}
			var auditingTime = $("#stageTime").val();
			var url = "${base}${incentivefee_organizationfeemanage_onekeypay.controller}.json?";
			var layers = top.layer.confirm("<msg:message code='incentive.ispay.comfirm' />",  function(){
				$.ajax({
					url:url,
					type:'post',
					traditional: true,
					timeout:'60000',
					dataType:'json',
					data:{c:'${incentivefee_organizationfeemanage_onekeypay.code}',orgIds:sourceIds,incentiveFeeIds:incentiveFeeIds,auditingTime:auditingTime,companyID:$("#companyID").val(),type:type,isPay:"0"},
					success:function(jsonData,textStatus){
						if(textStatus == "success"){
							if(jsonData.status == "success"){
								top.layer.close(layers);
								$("#unfeeList").trigger("reloadGrid");
							}
						}
					}
				});
			});
		}
		
	}
	//标签切换
	function tab(tag){
		if(tag == 1){
			jQuery("#unfeeList").trigger("reloadGrid");	
		}else{
			jQuery("#feedList").trigger("reloadGrid");
		}
		for(var i = 1 ; i <= 2 ; i++){
			if(i == tag){
				$("#tab_"+i).addClass("selected");
				$("#div_"+i).css("visibility","visible");
			}else{
				$("#tab_"+i).removeClass("selected");
				$("#div_"+i).css("visibility","hidden");
			}
		}
	}
	
	function reloadUnfeeList(){
		payStatus = 0;
		jQuery("#unfeeList").jqGrid("setGridParam",{postData:{c:"${incentiveFeeDetail.c}",isPay:"0",payStageTime:$("#stageTime").val(),companyID:$("#companyID").val(),type:type}});		
		jQuery("#unfeeList").trigger("reloadGrid");	
	}
	
	function reloadFeeList(){
		jQuery("#feedList").jqGrid("setGridParam",{postData:{c:"${incentiveFeeDetail.c}",isPay:"1",payStageTime:$("#stageTime1").val(),companyID:$("#companyID1").val(),type:type}});		
		jQuery("#feedList").trigger("reloadGrid");	
	}
</script>
	</head>
	<body class="skinMain">
		<ul id="tabnav">
			<li class="tab_1" ><a href="#" id="tab_1" onclick="tab(1)" class="selected">待发放返利费用</a></li>
			<li class="tab_2" ><a href="#" id="tab_2" onclick="tab(2)">已发放返利费用</a></li>
		</ul>
		<div id="div_1" class="tab-div-show">
		<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
			<tr>
				<td align="right" width="10%" class="search_info">
					计划发放周期
				</td>
				<td align="left" width="25%" class="search_lable">
					<select name="stageTime" id="stageTime" onchange="reloadUnfeeList();">
						<c:forEach items="${stageTimeMap}" var="item">
							<option value="${item.stageKey}">${item.stageDate}</option>
						</c:forEach>
					</select>
				</td>
				<c:choose>
					<c:when test="${companyID == null}">
						<td align="right" width="10%" class="search_info">
							居间公司
						</td>
						<td align="left" width="25%" class="search_lable">
							<select name="companyID" id="companyID" onchange="reloadUnfeeList();">
								<c:forEach items="${orgList}" var="item">
									<option value="${item.id}">${item.name}</option>
								</c:forEach>
							</select>
						</td>
					</c:when>
					<c:otherwise>
						<input type="hidden" id="companyID" value="${companyID}" />
					</c:otherwise>
				</c:choose>
				<td width="25%" class="button_area" align="center">
					<input type="button" class="btn2" value="${incentivefee_organizationfeemanage_check.name}" onclick="check()"/>
				</td>
				<td width="25%" class="button_area" align="center">
					<input type="button" class="btn2" value="${incentivefee_organizationfeemanage_onekeypay.name}" onclick="oneKeyPay()"/>
				</td>
			</tr>
		</table>
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td width="85%" valign="top">
					
						<table id="unfeeList"><tr><td>&nbsp;</td></tr></table>
						<lable style="color:red;">累计返利成交规模 = 子表格中返利核算金额之和<br>
						居间费总额 = 子表格中居间费金额之和<br>
						返利分值 = 累计返利成交规模 * 成功系数 + 奖励分值<br>
						返利金额 = 居间费总额 * 返利比例<br>
						</lable>
					
				</td>
			</tr>
		</table>
	</div>	
		<div id="div_2" class="tab-div-hide">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr>
					<td align="right" width="10%" class="search_info" >
						计划发放周期
					</td>
					<td align="left" width="25%" class="search_lable" height="23px">
						<select name="stageTime1" id="stageTime1" onchange="reloadFeeList();" >
							<c:forEach items="${stageTimeMap}" var="item">
								<option value="${item.stageKey}">${item.stageDate}</option>
							</c:forEach>
						</select>
					</td>
					<c:choose>
						<c:when test="${companyID == null}">
							<td align="right" width="10%" class="search_info">
								居间公司
							</td>
							<td align="left" width="25%" class="search_lable">
								<select name="companyID1" id="companyID1" onchange="reloadFeeList();">
									<c:forEach items="${orgList}" var="item">
										<option value="${item.id}">${item.name}</option>
									</c:forEach>
								</select>
							</td>
						</c:when>
						<c:otherwise>
							<input type="hidden" id="companyID1" value="${companyID}" />
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td width="85%" valign="top">
							<table id="feedList"><tr><td>&nbsp;</td></tr></table>
						
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>