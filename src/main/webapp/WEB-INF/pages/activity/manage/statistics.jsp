<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
<script type="text/javascript">
	var k;
	$(window).resize(function(){
		$("#statisticsList").setGridWidth($(window).width()*0.999);
	});
	$(document).ready(function(){
		jQuery("#statisticsList").jqGrid({
			url: 'statistics/gridN5.json',
			datatype: 'json',
			colNames: ["理财顾问ID",
			           "理财顾问",
			           "手机号",
			           "理财顾问状态",
			           "微信关注",
			           "微信号",
			           "基数",
			           "N1状态",
			           "N2状态",
			           "N3状态",
			           "N4状态",
			           "N5状态",
			           "N1奖金",
			           "N2奖金",
			           "N3奖金",
			           "N4奖金",
			           "N5奖金",
			           "N1",
			           "N2",
			           "N3",
			           "N4",
			           "N5",
			           "已发放奖金(元)",
			           "未发放奖金(元)",
			           "身份验证",
			           "<msg:message code='info.operate'/>"],
			colModel: [{name:'joinId', index:'a.joinId', align:'center', sortable:false, hidden:true},
			           {name:'memberName', index:'a.memberName', align:'center', sortable:false, width:'8%', formatter:function(cellvalue, options, rowObject){
							if(cellvalue!=null && rowObject.memberNickName!=null){
				          	  	return cellvalue+"("+rowObject.memberNickName+")";
							}else if(cellvalue!=null && rowObject.memberNickName==null){
				          	  	return cellvalue;
							}else if(cellvalue==null && rowObject.memberNickName!=null){
				          	  	return rowObject.memberNickName;
							}else{
				          	  	return "";
							}
			           }},
			           {name:'memberPhone', index:'a.memberPhone', align:'center', sortable:false, width:'8%'},
			           {name:'memberStatus', index:'a.memberStatus', align:'center', sortable:false, hidden:true},
			           {name:'reserved41', index:'a.reserved41', align:'center', sortable:false, hidden:true},
			           {name:'reserved6', index:'a.reserved6', align:'center', sortable:false, width:'8%'},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'5%'},
			           {name:'reserved36', index:'a.reserved36', align:'center', sortable:false, hidden:true},
			           {name:'reserved37', index:'a.reserved37', align:'center', sortable:false, hidden:true},
			           {name:'reserved38', index:'a.reserved38', align:'center', sortable:false, hidden:true},
			           {name:'reserved39', index:'a.reserved39', align:'center', sortable:false, hidden:true},
			           {name:'reserved40', index:'a.reserved40', align:'center', sortable:false, hidden:true},
			           {name:'reserved1', index:'a.reserved1', align:'center', sortable:false, hidden:true},
			           {name:'reserved2', index:'a.reserved2', align:'center', sortable:false, hidden:true},
			           {name:'reserved3', index:'a.reserved3', align:'center', sortable:false, hidden:true},
			           {name:'reserved4', index:'a.reserved4', align:'center', sortable:false, hidden:true},
			           {name:'reserved5', index:'a.reserved5', align:'center', sortable:false, hidden:true},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'6%',formatter:function(cellvalue, options, rowObject){
							var content = "";
			        	    if(rowObject.reserved36==0){
			        		   content = "<img src='${base}/${skin}/images/activity/notobtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved36==1){
			        		   content = "<img src='${base}/${skin}/images/activity/obtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved36==2){
			        		   content = "<img src='${base}/${skin}/images/activity/paid.png' height='12' width='12'>";
			        	    }
			        	    return content;
			           }},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'6%',formatter:function(cellvalue, options, rowObject){
			        	    var content = "";
			        	    if(rowObject.reserved37==0){
			        		   content = "<img src='${base}/${skin}/images/activity/notobtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved37==1){
			        		   content = "<img src='${base}/${skin}/images/activity/obtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved37==2){
			        		   content = "<img src='${base}/${skin}/images/activity/paid.png' height='12' width='12'>";
			        	    }
			        	    return content;
			           }},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'6%',formatter:function(cellvalue, options, rowObject){
			        	    var content = "";
			        	    if(rowObject.reserved38==0){
			        		   content = "<img src='${base}/${skin}/images/activity/notobtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved38==1){
			        		   content = "<img src='${base}/${skin}/images/activity/obtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved38==2){
			        		   content = "<img src='${base}/${skin}/images/activity/paid.png' height='12' width='12'>";
			        	    }
			        	    return content;
			           }},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'6%',formatter:function(cellvalue, options, rowObject){
			        	    var content = "";
			        	    if(rowObject.reserved39==0){
			        		   content = "<img src='${base}/${skin}/images/activity/notobtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved39==1){
			        		   content = "<img src='${base}/${skin}/images/activity/obtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved39==2){
			        		   content = "<img src='${base}/${skin}/images/activity/paid.png' height='12' width='12'>";
			        	    }
			        	    return content;
			           }},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'6%',formatter:function(cellvalue, options, rowObject){
			        	    var content = "";
			        	    if(rowObject.reserved40==0){
			        		   content = "<img src='${base}/${skin}/images/activity/notobtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved40==1){
			        		   content = "<img src='${base}/${skin}/images/activity/obtain.png' height='12' width='12'>";
			        	    }else if(rowObject.reserved40==2){
			        		   content = "<img src='${base}/${skin}/images/activity/paid.png' height='12' width='12'>";
			        	    }
			        	    return content;
			           }},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'8%',formatter:function(cellvalue, options, rowObject){
			        	    var content = 0;
			        	    if(rowObject.reserved36==2){
			        		   content += parseInt(rowObject.reserved1);
			        	    }
			        	    if(rowObject.reserved37==2){
			        		   content += parseInt(rowObject.reserved2);
			        	    }
			        	    if(rowObject.reserved38==2){
			        		   content += parseInt(rowObject.reserved3);
			        	    }
			        	    if(rowObject.reserved39==2){
			        		   content += parseInt(rowObject.reserved4);
			        	    }
			        	    if(rowObject.reserved40==2){
			        		   content += parseInt(rowObject.reserved5);
			        	    }
			        	    return content;
			           }},
			           {name:'reserved42', index:'a.reserved42', align:'center', sortable:false, width:'8%',formatter:function(cellvalue, options, rowObject){
			        	    var content = 0;
			        	    if(rowObject.reserved36==1){
			        		   content += parseInt(rowObject.reserved1);
			        	    }
			        	    if(rowObject.reserved37==1){
			        		   content += parseInt(rowObject.reserved2);
			        	    }
			        	    if(rowObject.reserved38==1){
			        		   content += parseInt(rowObject.reserved3);
			        	    }
			        	    if(rowObject.reserved39==1){
			        		   content += parseInt(rowObject.reserved4);
			        	    }
			        	    if(rowObject.reserved40==1){
			        		   content += parseInt(rowObject.reserved5);
			        	    }
			        	    return content;
			           }},
			           {name:'reserved44', index:'a.reserved44', align:'center', sortable:false, width:'5%', formatter:function(cellvalue, options, rowObject){
			        	    if(cellvalue==0){
			        		   return "未通过";
			        	    }else if(cellvalue==1){
		        	    	   return "通过";
			        	    }
			           }},
			           {name:'act', index:'i.act', align:'left', sortable:false, width:'20%',
			        	   formatter:function(cellvalue, options, rowdata){
			        			var id = rowdata.id;
			        			var content = "";
		        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_issue'";
		        		   		content += " title='发放奖金'>";
		        		   		content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>发放奖金";
		        		   		content += "</a>";
		        		   		if(rowdata.reserved41!=1){
			        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_follow'";
			        		   		content += " title='已关注'>";
			        		   		content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>已关注";
			        		   		content += "</a>";
		        		   		}
		        		   		if(rowdata.reserved44==0){
		        		   			if(rowdata.memberStatus==2){
				        		   		content += "<a href='javascript:void(0);' id='" + id + "' class='shortcut_approve'";
				        		   		content += " title='通过验证'>";
				        		   		content += "<img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>通过验证";
				        		   		content += "</a>";
		        		   			}else{
		        		   				content += "<span style='color:#CFC09F;' title='通过验证'><img src='${base}/${skin}/images/icon/detail.png' weight='18' height='18' border='0' align='absmiddle'/>通过验证</span>";
		        		   			}
		        		   		}
			        			return content;
			        	   }
			           }],
			mtype: "POST",
			postData: {c:"${activityRecord.c}",activityId:"${activityRecord.activityId}",activityStartTime:"${activityRecord.activityStartTime}",activityEndTime:"${activityRecord.activityEndTime}"},
			rowNum: 10,
			page: "${activity.page}",
			rowList: [<msg:message code='jqgrid.row.list.s10.10'/>],
			pager: '#pagered',
			height: '245',
			autowidth: true,
			viewrecords: true,
			jsonReader: {
				repeatitems: false
			},
			caption: '${activityRecord.activityName}-活动统计列表',
			toolbar: [true,"bottom"]
		});
		
		$("#select").click(function(){
			var reserved36 = $("#reserved36").val();
			if(reserved36 == null){
				reserved36 = '';
			}
			var reserved37 = $("#reserved37").val();
			if(reserved37 == null){
				reserved37 = '';
			}
			var reserved38 = $("#reserved38").val();
			if(reserved38 == null){
				reserved38 = '';
			}
			var reserved39 = $("#reserved39").val();
			if(reserved39 == null){
				reserved39 = '';
			}
			var reserved40 = $("#reserved40").val();
			if(reserved40 == null){
				reserved40 = '';
			}
			jQuery("#statisticsList").jqGrid(
				"setGridParam",
				{postData:{
					c:"${activityRecord.c}",
					activityId:"${activityRecord.activityId}",
					activityStartTime:"${activityRecord.activityStartTime}",
					activityEndTime:"${activityRecord.activityEndTime}",
					memberPhone:$("#memberPhone").val(),
					reserved41:$("#reserved41").val(),
					reserved44:$("#reserved44").val(),
					reserved36:reserved36,
					reserved37:reserved37,
					reserved38:reserved38,
					reserved39:reserved39,
					reserved40:reserved40,
					reserved34:$("#reserved34").val(),
					reserved35:$("#reserved35").val(),
				}}
			).trigger("reloadGrid");
	    });
		
		//返回按钮
		var $back = $("<a></a>").attr("href","javascript:void();")
		  .attr("id","back")
		  .append($("<img/>").attr("src","${base}/${skin}/images/icon/back.png")
		  .attr("width","18").attr("height","18").attr("border","0")
		  .attr("border","0").attr("align","absmiddle"))
		  .append("<msg:message code="button.back"/>");    
		$("#t_statisticsList").append("&nbsp;&nbsp;").append(
			$("<span></span>")
			.attr("class","jqgridContainer")
			.append($back)
		);
		$("#t_statisticsList #back").click(function(){
			window.location.href='${base}${activity_manage.controller}?c=${activity_manage.code}&back=1';
		});
		
		//关注微信
		$(".shortcut_follow").live("click",function(){
			var rowid = $(this).attr("id");
		   	var data = jQuery("#statisticsList").jqGrid("getRowData",rowid);
		   	var memberName = data.memberName;
		   	k = top.$.layer({
				type : 1,
				title : "理财顾问已关注微信",
				border : [0],
				area : ['350px','200px'],
				page : {
					html :  "<table height='165px' width='350px' bgcolor='#EAFBFF'>"+
								"<tr>"+
									"<td align='right' width='40%'>"+
										"理财顾问<msg:message code='system.common.sign.colon'/>"+
									"</td>"+
									"<td>"+
										memberName+
									"</td>"+
								"</tr>"+
								"<tr>"+
									"<td align='right'>"+
										"微&nbsp;&nbsp;信&nbsp;&nbsp;号<msg:message code='system.common.sign.colon'/>"+
									"</td>"+
									"<td>"+
										"<input type='text' id='weixin'>" +
									"</td>"+
								"</tr>"+
								"<tr bgcolor='#CCDFED' height='40px'>"+
									"<td colspan='2' align='center'>"+
										"<input type='button' class='btn2' value='<msg:message code='button.confirm'/>' onclick='follow("+rowid+")'>"+
										"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
										"<input type='button' class='btn2' value='<msg:message code='button.cancel'/>' onclick='closeLayer()'>"+
									"</td>"+
								"</tr>"+
							"</table>"
				}
			});
		});
		
		//活动身份验证
		$(".shortcut_approve").live("click",function(){
			var rowid = $(this).attr("id");
			var index = $.layer({
				shade    : false,
				dialog   : {
					msg  : "确定该理财顾问通过活动身份验证？",
					btns : 2,
					type : 4,
					btn  : ["<msg:message code='button.confirm'/>", "<msg:message code='button.cancel'/>"],
					yes  : function(){
						$.ajax({
							url:"${base}/activity/manage/statistics/approve.json",
							type:'post',
							timeout:'60000',
							dataType:'json',
							data:{id:rowid,reserved44:1},
							success:function(jsonData,textStatus){
								if(textStatus == "success"){
									if(jsonData.status == "success"){
										layer.close(index);
										$("#statisticsList").trigger("reloadGrid");
									}
								}
							}
						});
					},
					no   : function(){
						layer.close(index);
					}
				}
			});
		});
		
		//确认关注微信
		window.parent.follow = function(rowid){
			var weixin = $("#weixin",window.parent.document).val();
			if(weixin==''){
				top.layer.msg("微信号不能为空！",1);
				return;
			}
			$.ajax({
				url: "${base}/activity/manage/statistics/follow.json",
				type:'post',
				timeout:'60000',
				dataType:'json',
				data: {id:rowid,reserved41:1,reserved6:weixin,reserved36:1},
				success:function(jsonData){
					top.layer.close(k);
					if(jsonData.status=='success'){
						$("#statisticsList").trigger("reloadGrid");
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg: "已关注！" ,type : 9}
						});
					}else{
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg: "关注失败！" ,type : 8}	
						});
					}
				}
			});
		}
		
		//关闭弹出层
		window.parent.closeLayer = function(){
			top.layer.close(k);
		}
		
		//奖金发放
		$(".shortcut_issue").live("click",function(){
			var rowid = $(this).attr("id");
		   	var data = jQuery("#statisticsList").jqGrid("getRowData",rowid);
		   	if(data.reserved41==0){
		   		top.layer.msg("该理财顾问暂未关注微信！",2);
		   		return;
		   	}
		   	if(data.reserved36!=1 && data.reserved37!=1 && data.reserved38!=1 && data.reserved39!=1 && data.reserved40!=1){
		   		top.layer.msg("该理财顾问没有奖金可发放！",2);
		   		return;
		   	}
		   	var memberName = data.memberName;
		   	var memberPhone = data.memberPhone;
		   	var reserved6 = data.reserved6;
		   	var content = "";
		   	if(data.reserved36 == 1){
		   		content += "<tr><td align='right'><input type='checkbox' id='" + data.reserved1 + "' name='reserved36' value='2' onclick='count()'>&nbsp;&nbsp;N1</td><td align='center'>"+data.reserved1+"元</td></tr>";
		   	}
		   	if(data.reserved37 == 1){
		   		content += "<tr><td align='right'><input type='checkbox' id='" + data.reserved2 + "' name='reserved37' value='2' onclick='count()'>&nbsp;&nbsp;N2</td><td align='center'>"+data.reserved2+"元</td></tr>";
		   	}
		   	if(data.reserved38 == 1){
		   		content += "<tr><td align='right'><input type='checkbox' id='" + data.reserved3 + "' name='reserved38' value='2' onclick='count()'>&nbsp;&nbsp;N3</td><td align='center'>"+data.reserved3+"元</td></tr>";
		   	}
		   	if(data.reserved39 == 1){
		   		content += "<tr><td align='right'><input type='checkbox' id='" + data.reserved4 + "' name='reserved39' value='2' onclick='count()'>&nbsp;&nbsp;N4</td><td align='center'>"+data.reserved4+"元</td></tr>";
		   	}
		   	if(data.reserved40 == 1){
		   		content += "<tr><td align='right'><input type='checkbox' id='" + data.reserved5 + "' name='reserved40' value='2' onclick='count()'>&nbsp;&nbsp;N5</td><td align='center'>"+data.reserved5+"元</td></tr>";
		   	}
	   		content += "<tr height='30px'><td align='right'>发放总奖金：</td><td align='center' id='issueAmount'></td></tr>";
		   	k = top.$.layer({
				type : 1,
				title : "请选择奖金发放",
				border : [0],
				area : ['500px','200px'],
				page : {
					html :  "<form id='issueForm'>"+
								"<table height='300px' width='500px' bgcolor='#EAFBFF'>"+
									"<tr height='25px'>"+
										"<td align='right' width='35%'>"+
											"理财顾问："+
										"</td>"+
										"<td align='center'>"+
											memberName+
										"</td>"+
									"</tr>"+
									"<tr height='25px'>"+
										"<td align='right'>"+
											"手&nbsp;&nbsp;机&nbsp;&nbsp;号："+
										"</td>"+
										"<td align='center'>"+
											memberPhone+
										"</td>"+
									"</tr>"+
									"<tr height='25px' border='1'>"+
										"<td align='right'>"+
											"微&nbsp;&nbsp;信&nbsp;&nbsp;号："+
										"</td>"+
										"<td align='center'>"+
											reserved6+
										"</td>"+
									"</tr>"+
									content+
									"<tr bgcolor='#CCDFED' height='40px'>"+
										"<td colspan='2' align='center'>"+
											"<input type='button' class='btn2' value='<msg:message code='button.confirm'/>' onclick='issue("+rowid+")'>"+
											"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
											"<input type='button' class='btn2' value='<msg:message code='button.cancel'/>' onclick='closeLayer()'>"+
										"</td>"+
									"</tr>"+
								"</table>"+
							"</form>"
				}
			});
		});
		
		window.parent.count = function(){
			var issueAmount = 0;
			for (var i=36; i<=40; i++) {
				if($("input[name='reserved"+i+"']:checked",window.parent.document).val()==2){
					issueAmount += parseInt($("input[name='reserved"+i+"']:checked",window.parent.document).attr("id"));
				}
			}
			if(issueAmount != 0){
				$("#issueAmount",window.parent.document).text(issueAmount+"元");
			}else{
				$("#issueAmount",window.parent.document).text("");
			}
		}
		
		//确认发放奖金
		window.parent.issue = function(rowid){
			var data = $("#issueForm",window.parent.document).serialize();
			if(data==''){
				top.layer.msg("未选择任何奖金！",1);
				return;
			}
			var reserved43;
			for (var i=36; i<=40; i++) {
				if($("input[name='reserved"+i+"']:checked",window.parent.document).val()==2){
					reserved43 = i - 35;
				}
			}
		   	var rowdata = jQuery("#statisticsList").jqGrid("getRowData",rowid);
		   	var joinId = rowdata.joinId;
		   	var memberName = rowdata.memberName;
			$.ajax({
				url: "${base}/activity/manage/statistics/issue.json?id="+rowid+"&reserved43="+reserved43+"&joinId="+joinId+"&memberName="+memberName,
				type:'post',
				timeout:'60000',
				dataType:'json',
				data: data,
				success:function(jsonData){
					top.layer.close(k);
					if(jsonData.status=='success'){
						$("#statisticsList").trigger("reloadGrid");
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg: "奖金发放成功！" ,type : 9}
						});
					}else{
						top.$.layer({
							area : ['auto','auto'],
							dialog : {msg: "奖金发放失败！" ,type : 8}	
						});
					}
				}
			});
		}
	});
	
	function changeN(n){
		if(n==0){
			$("select[name='status']").attr("disabled","disabled");
			$("#reserved34").attr("disabled","disabled");
			$("#reserved35").attr("disabled","disabled");
			$("select[name='status']").attr("id","");
		}else{
			$("select[name='status']").removeAttr("disabled");
			$("#reserved34").removeAttr("disabled");
			$("#reserved35").removeAttr("disabled");
			$("select[name='status']").attr("id","reserved"+(35+parseInt(n)));
		}
	}
</script>
	</head>
	<body class="skinMain">
		<form:form action="statistics" method="post" commandName="activityRecord">
			<input type="hidden" name="c" value="${activityRecord.c}"/>
			<input type="hidden" name="activityId" value="${activityRecord.activityId}"/>
			<table cellpadding="0" cellspacing="1" border="0" width="100%" class="search">
				<tr height="25px">
					<td align="right" width="10%" class="search_info">
						手机号：
					</td>
					<td align="left" width="20%" class="search_lable">
						<form:input path="memberPhone" htmlEscape="true"/>
					</td>
					<td align="right" width="10%" class="search_info">
						关注微信：
					</td>
					<td align="left" width="20%" class="search_lable">
						<select name="reserved41" id="reserved41">
							<option value="">全部</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
					</td>
					<td align="right" width="10%" class="search_info">
						身份验证：
					</td>
					<td align="left" width="20%" class="search_lable">
						<select name="reserved44" id="reserved44">
							<option value="">全部</option>
							<option value="1">通过</option>
							<option value="0">未通过</option>
						</select>
					</td>
					<td width="10%" class="button_area" rowspan="2" align="center">
						<input type="button" class="btn2" id="select" value="<msg:message code='button.search' />"/>
					</td>
				</tr>
				<tr height="25px">
					<td align="right" width="10%" class="search_info">
						奖金等级：
					</td>
					<td align="left" width="20%" class="search_lable">
						<select onchange="changeN(this.value)">
							<option value="0">全部</option>
							<option value="1">N1</option>
							<option value="2">N2</option>
							<option value="3">N3</option>
							<option value="4">N4</option>
							<option value="5">N5</option>
						</select>
					</td>
					<td align="right" width="10%" class="search_info">
						奖金状态：
					</td>
					<td align="left" width="20%" class="search_lable">
						<select id="" name="status" disabled="disabled">
							<option value="0">未获得</option>
							<option value="1">已获得</option>
							<option value="2">已发放</option>
						</select>
					</td>
					<td align="right" width="10%" class="search_info">
						获得时间：
					</td>
					<td align="left" width="20%" class="search_lable">
						<input type="text" style="width: 100px;" disabled="disabled" name="reserved34" id="reserved34" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});">
						 -- 
						<input type="text" style="width: 100px;" disabled="disabled" name="reserved35" id="reserved35" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});">
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="skinMain">
				<tr>
					<td>
						<table id="statisticsList" ><tr><td>&nbsp;</td></tr></table>
						<div id="pagered"></div>
					</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>