<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/pages/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.role/TR/html4/loose.dtd">
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
	<script type="text/javascript">
	var minRat = '${minRat}';
	var maxRat = '${maxRat}';
	var increaseRat = '${increaseRat}';
	var stageDate = '${stageDate}';
	var recommandRewardScore = '${recommandRewardScore}';
	var beginTime = '${beginTime}';

	// 当前行记数
	var currStage = 1;
	var currScore = 1;
	var currStage2 = 1;
	var currScore2 = 1;
	// 当前行累计记数
	var currStageSeri = 0;
	var currScoreSeri = 0;	
	var currStageSeri2 = 0;
	var currScoreSeri2 = 0;
	$(document).ready(function(){
		//最大年为当前年
		var MaxYear = (new Date()).getFullYear();
		//设置最小年
		var MinYear = 2000;
		//循环年赋值给下拉框
		 for(var i = MaxYear; i >= MinYear; i--){
	        var op = window.document.createElement("OPTION");
	        op.value = i;
	        op.innerHTML = i;
	        $("#selYear").append(op);
	    }
		//循环月赋值给下拉框
		 for(var i = 1; i < 13; i++){
	        var op = window.document.createElement("OPTION");
	        if( i < 10){
	        	i = "0"+i;
	        }
	        op.value = i;
	        op.innerHTML = i;
	        $("#selMonth").append(op);
	    }
		
		// init start
		initForm('0',true,0);
		initList('0',true,1);
		//--- init end

		//	del talbe -1  start
		$("#stageList .del-stage,#stageList .del-stage-ori").live("click",function(){
			var id = $(this).attr('id');
			$(this).parent().parent().remove();
			if(id >0 ){	
				$("#stageList .del-stage-ori").eq(-1).html("-");
			}
			$("#stageList .seri-n").each(function(index){
				$(this).html(index+1);
			});
			currStage = currStage -1;
		});	
		//--- del talbe -1  end
		
		//	del talbe -1  start
		$("#scoreList .del-score,#scoreList .del-score-ori").live("click",function(){
			var id = $(this).attr('id');
			$(this).parent().parent().remove();
			if(id >0 ){	
				$("#scoreList .del-score-ori").eq(-1).html("-");
			}
			$("#scoreList .seri-n").each(function(index){
				$(this).html(index+1);
			});
			currScore = currScore -1;
		});	
		//--- del talbe -1  end
	

		//	del talbe -1  start
		$("#stageList2 .del-stage,#stageList2 .del-stage-ori").live("click",function(){
			var id = $(this).attr('id');
			$(this).parent().parent().remove();
			if(id >0 ){	
				$("#stageList2 .del-stage-ori").eq(-1).html("-");
			}
			$("#stageList2 .seri-n").each(function(index){
				$(this).html(index+1);
			});
			currStage2 = currStage2 -1;
		});	
		//--- del talbe -1  end
		
		//	del talbe -1  start
		$("#scoreList2 .del-score,#scoreList2 .del-score-ori").live("click",function(){
			var id = $(this).attr('id');
			$(this).parent().parent().remove();
			if(id >0 ){	
				$("#scoreList2 .del-score-ori").eq(-1).html("-");
			}
			$("#scoreList2 .seri-n").each(function(index){
				$(this).html(index+1);
			});
			currScore2 = currScore2 -1;
		});	
		//--- del talbe -1  end
		
		//	overview talbe start
		$("#overview").click(function(){
			var status = true;
			var form = $("#form_3").serializeArray();
			var max = form[0].value;
			var min = form[1].value;
			var rat = form[2].value;
			$.each(form, function(i, item) {
				if(form[i].value == ""){
					$("#tip_error_3").css("display","block");
					$("#tip_error_3").animate({height:40},'fast');
					var title = $("#form_3 input").eq(i).attr("title");
					$("#tip_error_3_v").text(title+"不能为空");
					status = false;
					$("#list_table_2").html("");
					return false;
				}
			});
			if(status){
				$("#tip_error_3").css("display","none");
				var loop = 1;
				var subLoop = 13;
				var substart = 1;
				var curValue = 1;
				var co = min-(-12*rat);
				
				if(co < max){
					loop = 2;
				}
				var html  = "<tr><td colspan='13' class='table-title-2'><center>活跃度系数配置预览</center></td></tr>";
				for (var j=0;j<loop;j++){
					html += "<tr>";
					if(j ==0){
						html += "<td width='7.6%'><div class='t'>首月</div><div class='v'>1</div></td>";
					}
					if(j == 1){
						subLoop = Number(loop*12)+2;
						substart = 13;
					}
					for (var i=substart;i<subLoop;i++){
						var curValue = (curValue-(-rat)).toFixed(2);
						if(curValue >= max){
							curValue = max;
						}
						if(i == 25){
							html += "<td width='7.6%'><div class='t'>"+i+"</div><div class='v'>----</div></td>";
						}else{
							html += "<td width='7.6%'><div class='t'>"+i+"</div><div class='v'>"+curValue+"</div></td>";
						}
					}
					html += "</tr>";
				}
				$("#list_table_2").html(html);
			}
		});
		//--- overview talbe end
	});
	
	// 数字验证
	function checkNumber(evt,object,flag){
		var key = window.event?event.keyCode:evt.which;
		var re = /^[0-9]*(\.\d*)?$|^-?d^(\.\d*)?$/;
		if(key != 8){
			var value = $(object).val();
			if(!re.test(value)){
				if("paste" == flag){
					$(object).val("");
				}else{
					if(value != null && value != ""){
						$(object).val(value.substring(0,value.length-1));
					}
				}
			}
		}
		
	}
	
	function initForm(seri,status,lasy){
		if(seri == '1' || seri == '0'){
			$("input[name='stageDate']").attr("disabled",status);
			$("input[name='stageDate']").val(stageDate);
			$("input[name='recommandRewardScore']").attr("disabled",status);
			$("input[name='recommandRewardScore']").val(recommandRewardScore);
			$("#selYear").attr("disabled",status);
			$("#selYear").val(beginTime.substring(0,4));
			$("#selMonth").attr("disabled",status);
			$("#selMonth").val(beginTime.substring(5,7));
			setTimeout(function(){
				$("#tip_error_1").css("display","none");
			},lasy);
			initButtom('1',status);
		}
		if(seri == '3' || seri == '0'){
			$("#list_table_2").html("");
			$("input[name='minRat']").attr("disabled",status);
			$("input[name='maxRat']").attr("disabled",status);
			$("input[name='increaseRat']").attr("disabled",status);
		
			$("input[name='minRat']").val(minRat);
			$("input[name='maxRat']").val(maxRat);
			$("input[name='increaseRat']").val(increaseRat);
			setTimeout(function(){
				$("#tip_error_3").css("display","none");
			},lasy);
			initButtom('3',status);
		}
	}
	
	function initList(seri,status,type,lasy){
		var url;
		var rows
		if(seri == '1' || seri == '0'){
			$("#stageList").html("<tr><td colspan=\"6\">数据加载中</td></tr>");
			url = "incentive/rebateGrid.json?c=${c}&type=1";
			$.ajax({  
				type:"POST",  
				url:url,  
				dataType:"json",  
				success:function(data){
					var rows = data.rows; 
					intStageList(rows,type); 
				}  
			}); 
			setTimeout(function(){
				$("#tip_error_2").css("display","none");
			},lasy);
			initButtom('2',status);
		}
		
		if(seri == '2' || seri == '0'){
			$("#scoreList").html("<tr><td colspan=\"5\">数据加载中</td></tr>");
			url = "incentive/rankGrid.json?c=${c}&type=1";
			$.ajax({  
				type:"POST",  
				url:url,  
				dataType:"json",  
				success:function(data){
					var rows = data.rows; 
					intScoreList(rows,type); 
				}  
			}); 
			setTimeout(function(){
				$("#tip_error_4").css("display","none");
			},lasy);
			initButtom('4',status);
		}
		
		if(seri == '3' || seri == '0'){
			$("#stageList2").html("<tr><td colspan=\"6\">数据加载中</td></tr>");
			url = "incentive/rebateGrid.json?c=${c}&type=2";
			$.ajax({  
				type:"POST",  
				url:url,  
				dataType:"json",  
				success:function(data){
					var rows = data.rows; 
					intStageList2(rows,type); 
				}  
			}); 
			setTimeout(function(){
				$("#tip_error_5").css("display","none");
			},lasy);
			initButtom('5',status);
		}
		if(seri == '4' || seri == '0'){
			$("#scoreList2").html("<tr><td colspan=\"5\">数据加载中</td></tr>");
			url = "incentive/rankGrid.json?c=${c}&type=2";
			$.ajax({  
				type:"POST",  
				url:url,  
				dataType:"json",  
				success:function(data){
					var rows = data.rows; 
					intScoreList2(rows,type); 
				}  
			}); 
			setTimeout(function(){
				$("#tip_error_6").css("display","none");
			},lasy);
			initButtom('6',status);
		}
	}
	
	// create stage list
	function intStageList(rows,type){
		currStage = 1;
		currStageSeri = 0;
		$(".add-stage").html("");
		var html = "";
		var currStageName = "";
		
		$.each(rows,function(i,v){
			var levelName = v['levelName'] == null ? "" : v['levelName'];
			var levelTag = v['levelTag'] == null ? "" : v['levelTag'];
			var stageScore = v['stageScore'] == null ? "" : v['stageScore'];
			var serviceFeeRat = v['serviceFeeRat'] == null ? "" : v['serviceFeeRat']*1000000/10000;
           	html += "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currStage+"</span><input type=\"hidden\" name=\"stageList["+currStageSeri+"].type\" value=\"1\"/></td>";
			html +=	"<td class=\"m-v\" val='"+levelName+"' title=\"等级名称\" code=\"levelName\" curn="+currStageSeri+">"+levelName+"</td>";
			html +=	"<td class=\"m-v\" val='"+levelTag+"' title=\"等级标识\" code=\"levelTag\" curn="+currStageSeri+">"+levelTag+"</td>";
			html +=	"<td class=\"m-v\" val='"+stageScore+"' title=\"阶段返利分值要求\" code=\"stageScore\" curn="+currStageSeri+">"+stageScore+"</td>";
			html +=	"<td class=\"m-v\" val='"+serviceFeeRat+"' title=\"返利占服务费用比例\" code=\"serviceFeeRat\" curn="+currStageSeri+">"+serviceFeeRat+"</td>";
			html +=	"<td><a class=\"del-stage-ori\" href=\"javascript:void(0);\" id="+v['id']+"></a></td>";
			html +=	"</tr>";
			currStageSeri++;
			currStage++;
        }); 
		$("#stageList").html(html);
	}
	function intScoreList(rows,type){
		currScore = 1;
		currScoreSeri = 0;
		$(".add-score").html("");
		var html = "";
		var currStageName = "";
		$.each(rows,function(i,v){
			var rankName = v['rankName'] == null ? "" : v['rankName'];
			var rankTag = v['rankTag'] == null ? "" : v['rankTag'];
			var rewardScore = v['rewardScore'] == null ? "" : v['rewardScore'];
           	html += "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currScore+"</span><input type=\"hidden\" name=\"scoreList["+currScoreSeri+"].type\" value=\"1\"/></td>";
			html +=	"<td class=\"m-v\" val='"+rankName+"' title=\"排名名称\" code=\"rankName\" curn="+currScoreSeri+">"+rankName+"</td>";
			html +=	"<td class=\"m-v\" val='"+rankTag+"' title=\"排名标识\" code=\"rankTag\" curn="+currScoreSeri+">"+rankTag+"</td>";
			html +=	"<td class=\"m-v\" val='"+rewardScore+"' title=\"排名奖励分值\" code=\"rewardScore\" curn="+currScoreSeri+">"+rewardScore+"</td>";
			html +=	"<td><a class=\"del-score-ori\" href=\"javascript:void(0);\" id="+v['id']+"></a></td>";
			html +=	"</tr>";
			currScoreSeri++;
			currScore++;
        }); 
		$("#scoreList").html(html);
	}
	
	function createStageList(){
		var linc = currStageSeri;
		var	html  = "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currStage+"</span><input type=\"hidden\" name=\"stageList["+linc+"].type\" value=\"1\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].levelName\" title=\"等级名称\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].levelTag\" title=\"等级标识\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].stageScore\" title=\"阶段返利分值要求\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].serviceFeeRat\" title=\"返利占服务费用比例\"/></td>";
			html +=	"<td><a class=\"del-stage\" href=\"javascript:void(0);\" id="+(-currStage)+">-</a></td>";
			html +=	"</tr>";
		currStage = currStage + 1;
		currStageSeri = currStageSeri + 1;
		$("#stageList").append(html);
	}
	
	function createScoreList(){
		var linc = currScoreSeri;
		var html  = "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currScore+"</span><input type=\"hidden\" name=\"scoreList["+linc+"].type\" value=\"1\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"scoreList["+linc+"].rankName\" title=\"排名名称\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"scoreList["+linc+"].rankTag\" title=\"排名标识\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"scoreList["+linc+"].rewardScore\" title=\"排名奖励分值\"/></td>";
			html +=	"<td><a class=\"del-score\" href=\"javascript:void(0);\" id="+(-currScore)+">-</a></td>";
			html +=	"</tr>";
		currScore = currScore + 1;
		currScoreSeri = currScoreSeri + 1;
		$("#scoreList").append(html);
	}

	//	edit talbe -1  start
	function editStageList(){
		$("#stageList .m-v").each(function(index,item){
			var thisvalue = $(this).attr("val");
			var code = $(this).attr("code");
			var curn = $(this).attr("curn");
			var title = $(this).attr("title");
			$(this).html("<input type='text' name='stageList["+curn+"]."+code+"' value='"+thisvalue+"' title='"+title+"'/>");
		});
		$("#stageList .del-stage-ori").eq(-1).html("-");
		$(".add-stage").html("+");
		initButtom('2',false);
	};
	//--- edit talbe -1  end		
	
	//	edit talbe -2  start
	function editScoreList(){
		$("#scoreList .m-v").each(function(index,item){
			var thisvalue = $(this).attr("val");
			var code = $(this).attr("code");
			var curn = $(this).attr("curn");
			var title = $(this).attr("title");
			$(this).html("<input type='text' name='scoreList["+curn+"]."+code+"' value='"+thisvalue+"' title='"+title+"'/>");
		});
		$("#scoreList .del-score-ori").eq(-1).html("-");
		$(".add-score").html("+");
		initButtom('4',false);
	};
	//--- edit talbe -2  end

	
	// create stage list
	function intStageList2(rows,type){
		currStage2 = 1;
		currStageSeri2 = 0;
		$(".add-stage-2").html("");
		var html = "";
		var currStageName = "";
		$.each(rows,function(i,v){
			var levelName = v['levelName'] == null ? "" : v['levelName'];
			var levelTag = v['levelTag'] == null ? "" : v['levelTag'];
			var stageScore = v['stageScore'] == null ? "" : v['stageScore'];
			var serviceFeeRat = v['serviceFeeRat'] == null ? "" : v['serviceFeeRat']*1000000/10000;
           	html += "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currStage2+"</span><input type=\"hidden\" name=\"stageList["+currStageSeri2+"].type\" value=\"2\"/></td>";
			html +=	"<td class=\"m-v\" val='"+levelName+"' title=\"等级名称\" code=\"levelName\" curn="+currStageSeri2+">"+levelName+"</td>";
			html +=	"<td class=\"m-v\" val='"+levelTag+"' title=\"等级标识\" code=\"levelTag\" curn="+currStageSeri2+">"+levelTag+"</td>";
			html +=	"<td class=\"m-v\" val='"+stageScore+"' title=\"阶段返利分值要求\" code=\"stageScore\" curn="+currStageSeri2+">"+stageScore+"</td>";
			html +=	"<td class=\"m-v\" val='"+serviceFeeRat+"' title=\"返利占服务费用比例\" code=\"serviceFeeRat\" curn="+currStageSeri2+">"+serviceFeeRat+"</td>";
			html +=	"<td><a class=\"del-stage-ori\" href=\"javascript:void(0);\" id="+v['id']+"></a></td>";
			html +=	"</tr>";
			currStageSeri2++;
			currStage2++;
        }); 
		$("#stageList2").html(html);
	}
	function intScoreList2(rows,type){
		currScore2 = 1;
		currScoreSeri2 = 0;
		$(".add-score-2").html("");
		var html = "";
		var currStageName = "";
		$.each(rows,function(i,v){
			var rankName = v['rankName'] == null ? "" : v['rankName'];
			var rankTag = v['rankTag'] == null ? "" : v['rankTag'];
			var rewardScore = v['rewardScore'] == null ? "" : v['rewardScore'];
           	html += "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currScore2+"</span><input type=\"hidden\" name=\"scoreList["+currScoreSeri2+"].type\" value=\"2\"/></td>";
			html +=	"<td class=\"m-v\" val='"+rankName+"' title=\"排名名称\" code=\"rankName\" curn="+currScoreSeri2+">"+rankName+"</td>";
			html +=	"<td class=\"m-v\" val='"+rankTag+"' title=\"排名标识\" code=\"rankTag\" curn="+currScoreSeri2+">"+rankTag+"</td>";
			html +=	"<td class=\"m-v\" val='"+rewardScore+"' title=\"排名奖励分值\" code=\"rewardScore\" curn="+currScoreSeri2+">"+rewardScore+"</td>";
			html +=	"<td><a class=\"del-score-ori\" href=\"javascript:void(0);\" id="+v['id']+"></a></td>";
			html +=	"</tr>";
			currScoreSeri2++;
			currScore2++;
        }); 
		$("#scoreList2").html(html);
	}
	
	function createStageList2(){
		var linc = currStageSeri2;
		var	html  = "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currStage2+"</span><input type=\"hidden\" name=\"stageList["+linc+"].type\" value=\"2\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].levelName\" title=\"等级名称\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].levelTag\" title=\"等级标识\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].stageScore\" title=\"阶段返利分值要求\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"stageList["+linc+"].serviceFeeRat\" title=\"返利占服务费用比例\"/></td>";
			html +=	"<td><a class=\"del-stage\" href=\"javascript:void(0);\" id="+(-currStage2)+">-</a></td>";
			html +=	"</tr>";
		currStage2 = currStage2 + 1;
		currStageSeri2 = currStageSeri2 + 1;
		$("#stageList2").append(html);
	}
	
	function createScoreList2(){
		var linc = currScoreSeri2;
		var html  = "<tr>";
			html +=	"<td class=\"table-n\"><span class=\"seri-n\">"+currScore2+"</span><input type=\"hidden\" name=\"scoreList["+linc+"].type\" value=\"2\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"scoreList["+linc+"].rankName\" title=\"排名名称\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"scoreList["+linc+"].rankTag\" title=\"排名标识\"/></td>";
			html +=	"<td class=\"m-v\" val=\"\"><input type=\"text\" name=\"scoreList["+linc+"].rewardScore\" title=\"排名奖励分值\"/></td>";
			html +=	"<td><a class=\"del-score\" href=\"javascript:void(0);\" id="+(-currScore2)+">-</a></td>";
			html +=	"</tr>";
		currScore2 = currScore2 + 1;
		currScoreSeri2 = currScoreSeri2 + 1;
		$("#scoreList2").append(html);
	}

	//	edit talbe -1  start
	function editStageList2(){
		$("#stageList2 .m-v").each(function(index,item){
			var thisvalue = $(this).attr("val");
			var code = $(this).attr("code");
			var curn = $(this).attr("curn");
			var title = $(this).attr("title");
			$(this).html("<input type='text' name='stageList["+curn+"]."+code+"' value='"+thisvalue+"' title='"+title+"'/>");
		});
		$("#stageList2 .del-stage-ori").eq(-1).html("-");
		$(".add-stage-2").html("+");
		initButtom('5',false);
	};
	//--- edit talbe -1  end		
	
	//	edit talbe -2  start
	function editScoreList2(){
		$("#scoreList2 .m-v").each(function(index,item){
			var thisvalue = $(this).attr("val");
			var code = $(this).attr("code");
			var curn = $(this).attr("curn");
			var title = $(this).attr("title");
			$(this).html("<input type='text' name='scoreList["+curn+"]."+code+"' value='"+thisvalue+"' title='"+title+"'/>");
		});
		$("#scoreList2 .del-score-ori").eq(-1).html("-");
		$(".add-score-2").html("+");
		initButtom('6',false);
	};
	//--- edit talbe -2  end


	
	function saveTextForm(seri){
		var tdata;
		var status = true;
		var t_minRat = $("input[name='minRat']").val();
		var t_maxRat = $("input[name='maxRat']").val();
		var t_increaseRat = $("input[name='increaseRat']").val();
		var t_stageDate = $("input[name='stageDate']").val();
		var t_recommandRewardScore = $("input[name='recommandRewardScore']").val();
		var t_beginTime = $("#selYear").val()+"-"+$("#selMonth").val();
		
		if(seri == "1"){
			var form = $("#form_1").serializeArray();
			$.each(form, function(i, item) {
				var val = form[i].value;
				if($.trim(val) == ""){
					$("#tip_error_1").css("display","block");
					$("#tip_error_1").animate({height:40},'fast');
					var title = $("#form_1 input").eq(i).attr("title");
					$("#tip_error_1_v").text(title+"不能为空");
					status = false;
					return false;
				}
			});
			tdata = {c:"${c}",code:"stageDate,recommandRewardScore,beginTime",value:""+t_stageDate+","+t_recommandRewardScore+","+t_beginTime+""};
		}
		
		if(seri == "3"){
			var form = $("#form_3").serializeArray();
			$.each(form, function(i, item) {
				var val = form[i].value;
				if($.trim(val) == ""){
					$("#tip_error_3").css("display","block");
					$("#tip_error_3").animate({height:40},'fast');
					var title = $("#form_3 input").eq(i).attr("title");
					$("#tip_error_3_v").text(title+"不能为空");
					status = false;
					$("#list_table_2").html("");
					return false;
				}
			});
			tdata = {c:"${c}",code:"maxRat,minRat,increaseRat",value:""+t_maxRat+","+t_minRat+","+t_increaseRat+""};
		}
		
		if(status){
			jQuery.ajax({
				url:"incentive/updateStage.json",type: "post",contentType : 'application/x-www-form-urlencoded',
				data:tdata,dataType : 'json',
				success:function(data){
					if(data.status == 'success'){
						if(seri == '1'){
							stageDate = t_stageDate;
							recommandRewardScore = t_recommandRewardScore;
							beginTime = t_beginTime;
							$("#selYear").val($("#selYear").val());
							$("#tip_error_1").css("display","block");
							$("#tip_error_1_v").text("保存成功");
							$("#tip_error_1").animate({height:40},'fast');
							initForm('1',true,3000);
	
						}else if(seri == '3'){
							minRat = t_minRat;
							maxRat = t_maxRat;
							increaseRat = t_increaseRat;
							$("#tip_error_3").css("display","block");
							$("#tip_error_3_v").text("保存成功");
							$("#tip_error_3").animate({height:40},'fast');
							initForm('3',true,3000);
						}
					}else{
						if(seri == '1'){
							$("#tip_error_1").css("display","block");
							$("#tip_error_1_v").text("保存失败");
							$("#tip_error_1").animate({height:40},'fast');
							setTimeout(function(){
								$("#tip_error_1").css("display","none");
							},3000);
						}else if(seri == '3'){
							$("#tip_error_3").css("display","block");
							$("#tip_error_3_v").text("保存失败");
							$("#tip_error_3").animate({height:40},'fast');
							setTimeout(function(){
								$("#tip_error_3").css("display","none");
							},3000);
						}
					}
				}
			});
		}
	}
	
	function saveListForm(seri){
		var status = true;
		var tdata;
		if(seri == "1"){
			var url = "incentive/saveRebate.json";
			var form = $("#form_2").serializeArray();
			$.each(form, function(i, item) {
				var val = form[i].value;
				if($.trim(val) == ""){
					$("#tip_error_2").css("display","block");
					$("#tip_error_2").animate({height:40},'fast');
					var title = $("#form_2 input").eq(i).attr("title");
					$("#tip_error_2_v").text(title+"不能为空");
					status = false;
					return false;
				}
			});
			tdata = form;
		}else if(seri == "2"){
			var url = "incentive/saveRank.json";
			var form = $("#form_4").serializeArray();
			$.each(form, function(i, item) {
				var val = form[i].value;
				if($.trim(val) == ""){
					$("#tip_error_4").css("display","block");
					$("#tip_error_4").animate({height:40},'fast');
					var title = $("#form_4 input").eq(i).attr("title");
					$("#tip_error_4_v").text(title+"不能为空");
					status = false;
					return false;
				}
			});
			tdata = form;
		}else if(seri == "3"){
			var url = "incentive/saveRebate.json";
			var form = $("#form_5").serializeArray();
			$.each(form, function(i, item) {
				var val = form[i].value;
				if($.trim(val) == ""){
					$("#tip_error_5").css("display","block");
					$("#tip_error_5").animate({height:40},'fast');
					var title = $("#form_5 input").eq(i).attr("title");
					$("#tip_error_5_v").text(title+"不能为空");
					status = false;
					return false;
				}
			});
			tdata = form;
		}else if(seri == "4"){
			var url = "incentive/saveRank.json";
			var form = $("#form_6").serializeArray();
			$.each(form, function(i, item) {
				var val = form[i].value;
				if($.trim(val) == ""){
					$("#tip_error_6").css("display","block");
					$("#tip_error_6").animate({height:40},'fast');
					var title = $("#form_6 input").eq(i).attr("title");
					$("#tip_error_6_v").text(title+"不能为空");
					status = false;
					return false;
				}
			});
			tdata = form;
		}
		if(status){
			jQuery.ajax({
				url:url,type: "post",contentType : 'application/x-www-form-urlencoded',
				data:tdata,dataType : 'json',
				success:function(data){
					if(data.status == 'success'){
						if(seri == '1'){
							$("#tip_error_2").css("display","block");
							$("#tip_error_2_v").text("保存成功");
							$("#tip_error_2").animate({height:40},'fast');
							initList('1',true,1,3000);
						}else if(seri == '2'){
							$("#tip_error_4").css("display","block");
							$("#tip_error_4_v").text("保存成功");
							$("#tip_error_4").animate({height:40},'fast');
							initList('2',true,1,3000);
						}else if(seri == '3'){
							$("#tip_error_5").css("display","block");
							$("#tip_error_5_v").text("保存成功");
							$("#tip_error_5").animate({height:40},'fast');
							initList('3',true,1,3000);
						}else if(seri == '4'){
							$("#tip_error_6").css("display","block");
							$("#tip_error_6_v").text("保存成功");
							$("#tip_error_6").animate({height:40},'fast');
							initList('4',true,1,3000);
						}
					}else{
						if(seri == '1'){
							$("#tip_error_2").css("display","block");
							$("#tip_error_2_v").text("保存失败");
							$("#tip_error_2").animate({height:40},'fast');
							setTimeout(function(){
								$("#tip_error_2").css("display","none");
							},3000);
						}else if(seri == '2'){
							$("#tip_error_4").css("display","block");
							$("#tip_error_4_v").text("保存失败");
							$("#tip_error_4").animate({height:40},'fast');
							setTimeout(function(){
								$("#tip_error_4").css("display","none");
							},3000);
						}else if(seri == '3'){
							$("#tip_error_5").css("display","block");
							$("#tip_error_5_v").text("保存失败");
							$("#tip_error_5").animate({height:40},'fast');
							setTimeout(function(){
								$("#tip_error_5").css("display","none");
							},3000);
						}else if(seri == '4'){
							$("#tip_error_6").css("display","block");
							$("#tip_error_6_v").text("保存失败");
							$("#tip_error_6").animate({height:40},'fast');
							setTimeout(function(){
								$("#tip_error_6").css("display","none");
							},3000);
						}
					}
				}
			});
		}
	}
	
	function turnTab(tab,ctab){
		$("#t_"+tab).addClass("selected");
		$("#t_"+ctab).removeClass("selected");
		$("#"+tab).css("display","block");
		$("#"+ctab).css("display","none");
	}
	
	function initButtom(seri,isNotEdit){

		var href_n = "javascript:void(0)";
		var href_e = $("#e_"+seri).attr("method");
		var href_c = $("#c_"+seri).attr("method");
		var href_s = $("#s_"+seri).attr("method");
		
		var normal = "normal";
		var active = "active";
		var A = normal;
		var B = "";
		var	H_S = href_n;
		var	H_C = href_n;
		var H_E = href_e;
		if(isNotEdit == false){
			A = active;
			B = normal;
			H_E = href_n;
			H_C = href_c;
			H_S = href_s;
		}
		$("#e_"+seri).removeClass().addClass(A);
		$("#e_"+seri).attr("href",H_E);
		$("#c_"+seri).removeClass().addClass(B);
		$("#c_"+seri).attr("href",H_C);
		$("#s_"+seri).removeClass().addClass(B);
		$("#s_"+seri).attr("href",H_S);
		
	}
	
	</script>
	<style>
		h3{font-size:1.2em;}
		.tip-error{float:left;width:100%;}
		.tip-error li{float:left;height:25px;line-height:25px;}
		.tip-error .t{width:43px;background-color:#000;color:#FFF;padding-left:8px;}
		.tip-error .v{background-color:red;color:#FFF;padding:0px 8px;}
		.content a{color:#FFF;}
		.content a.del-score{color:#000;}
		.content a.del-stage{color:#000;}
		.content a.del-stage-ori{color:#000;}
		.content a.del-score-ori{color:#000;}
		
		
		.content{padding-left:5px;width:890px;}
		.tip-box{margin-top:10px;padding:8px;border: 1px solid #66CCFF;margin-bottom:15px;}
		.tip-box li{height:23px;line-height:23px;}
		.ti-nav{width:100%;float:left;height:25px;}
		.ti-nav li{float:left;}
		.ti-title{width:80px;text-align:center;font-size:1.2em;font-weight:bold;} 
		.ti-line-left{width:10px;height:8px; border-bottom:1px dashed #000;}
		.ti-line-right{width:800px;height:8px; border-bottom:1px dashed #000;}
		.ti-line-right a{width:40px;height:22px;line-height:22px;border-right:1px solid #FFF;text-align:center;float:right;cursor: pointer;font-weight:bold;background-color:#DADADA;color:#FFF;}
		.ti-line-right a.active{background-color:#3399FF;color:#FFF;}
		.ti-line-right a.normal:hover{background-color:#3399FF;color:#FFF;}

		.tip-text{float:left;width:100%;color:red;height:40px;line-height:30px;}
		.text-form{float:left;width:100%;height:40px;line-height:30px;}
		.text-form input{width:60px;height:22px;line-height:22px;}
		.text-form a{width:50px;height:22px;line-height:22px;background-color:#3399FF;color:#FFF;padding:5px;}
		.list-table,.list-table-sort{float:left;border-left:1px solid #66CCFF;border-bottom:1px solid #66CCFF;margin-bottom:15px;}
		.list-table td,.list-table-sort td{border-top:1px solid #66CCFF;border-right:1px solid #66CCFF;height:22px;line-height:22px;text-align:center;}
		.list-table th,.list-table-sort th{border-top:1px solid #66CCFF;border-right:1px solid #66CCFF;background-color:#66CCFF;color:#FFF;height:22px;line-height:22px;}
		.list-table .table-title,.list-table-sort .table-title{height:25px;line-height:25px;font-weight:bold;}
		.table-n{font-weight:bold;text-align:center;background-color:#66CCFF;color:#FFF;}
		
		.list-table-2{float:left;border-left:1px solid #66CCFF;border-bottom:1px solid #66CCFF;margin-bottom:15px;}
		.list-table-2 .table-title-2{height:25px;line-height:25px;font-weight:bold;}
		.list-table-2 td{border-top:1px solid #66CCFF;border-right:1px solid #66CCFF;height:45px;text-align:center;}
		.list-table-2 .t{height:25px;line-height:25px;font-weight:bold;background-color:#66CCFF;color:#FFF;}
		.list-table-2 .v{height:20px;line-height:20px;}

	</style>
	</head>
	<body>
		<ul id="tabnav">
			<li><a href="javascript:turnTab('tab_1','tab_2');" id="t_tab_1" class="selected">个人理财顾问返利</a></li>
			<li><a href="javascript:turnTab('tab_2','tab_1');" id="t_tab_2" class="">团队、机构返利</a></li>
		</ul>
		<div class="content" id="tab_1">
			<h3>个人理财顾问返利</h3>
			<div class="tip-box">
				<ul>
				<li>返利 = 理财顾问服务费 X [分值阶梯]返利占服务费比例</li>
				<li>返利分值 = 阶段累计规模 X 活跃度系数 X 现阶段推荐成功率系数 + 奖励分值</li>
				</ul>
			</div>
			<ul class="ti-nav" id="ti_nav_1">
				<li class="ti-line-left"></li>
				<li class="ti-title">阶段时长</li>
				<li class="ti-line-right">
					<a id="s_1" method="javascript:saveTextForm('1');">保存</a>
					<a id="c_1" method="javascript:initForm('1',true,0);">取消</a>
					<a class="normal" id="e_1" method="javascript:initForm('1',false,0);">编辑</a>
				</li>
			</ul>
			<ul class="tip-error" id="tip_error_1" style="display:none;"><li class="v" id="tip_error_1_v"></li></ul>
			<form id="form_1">	
				<div class="text-form">
					<lable>阶段统计时长(月):&nbsp;<lable><input type="text" title="阶段统计时长" name="stageDate" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"/>
					&nbsp;&nbsp;<lable>推荐人奖励分值:&nbsp;<lable><input type="text" name="recommandRewardScore" title="推荐人奖励分值" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"/>
					&nbsp;&nbsp;<lable>起始日期设置:&nbsp;<lable><select id="selYear"></select>&nbsp;年&nbsp;<select id="selMonth"></select>&nbsp;月
				</div>
			</form>
			<ul class="ti-nav">
				<li class="ti-line-left"></li>
				<li class="ti-title">分值阶梯</li>
				<li class="ti-line-right">
					<a id="s_2" method="javascript:saveListForm('1');">保存</a>
					<a id="c_2" method="javascript:initList('1',true,1,0);">取消</a>
					<a class="normal" id="e_2" method="javascript:editStageList();">编辑</a>
				</li>
			</ul>
			<div class="tip-text">* 返利分值阶梯，达到不同级别客户，追加相应比例返利。</div>
			<ul class="tip-error" id="tip_error_2" style="display:none;"><li class="v" id="tip_error_2_v"></li></ul>
			<form name="" id="form_2">
			<input type="hidden" name="type" value="1"/>
			<table width="890" border="0" cellspacing="0" cellpadding="0" align="left" class="list-table">
				<tr><td colspan="6" class="table-title"><center>返利分值阶梯</center></td></tr>
				<tr>
					<th width="22">N</th>
					<th width="211.5">等级名称</th>
					<th width="211.5">等级标识</th>
					<th width="211.5">阶段返利分值要求(万)</th>
					<th width="211.5">返利占服务费用比例(%)</th>
					<th width="22"><a class="add-stage" href="javascript:createStageList();" style="color:#FFF;"></a></th>
				</tr>
				<tbody id="stageList"></tbody>
			</table>
			</form>
			
			<ul class="ti-nav">
				<li class="ti-line-left"></li>
				<li class="ti-title">活跃度系数</li>
				<li class="ti-line-right">
					<a id="s_3" method="javascript:saveTextForm('3');">保存</a>
					<a id="c_3" method="javascript:initForm('3',true,0);">取消</a>
					<a class="normal" id="e_3" method="javascript:initForm('3',false,0);">编辑</a>
				</li>
			</ul>
			<div class="tip-text">* 根据客户连月开单的情况设定活跃度系数。</div>
			<ul class="tip-error" id="tip_error_3" style="display:none;"><li class="v" id="tip_error_3_v"></li></ul>
			<form name="" id="form_3">
				<div class="text-form">
					<lable>最大值:&nbsp;<lable><input type="text" name="maxRat" title="最大值" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"/>&nbsp;&nbsp;
					<lable>最小值:&nbsp;<lable><input type="text" name="minRat" title="最小值" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"/>&nbsp;&nbsp;
					<lable>阶梯系数:&nbsp;<lable><input type="text" name="increaseRat" title="阶梯系数" onkeyup="checkNumber(event,this,'edit');"  onpropertychange="checkNumber(event,this,'edit');" oninput="checkNumber(event,this,'edit');"/>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" id="overview">预览</a>
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="left" class="list-table-2" id="list_table_2">
				</table>
			</form>
			<ul class="ti-nav">
				<li class="ti-line-left"></li>
				<li class="ti-title">奖励分值</li>
				<li class="ti-line-right">
					<a id="s_4" method="javascript:saveListForm('2');">保存</a>
					<a id="c_4" method="javascript:initList('2',true,1,0);">取消</a>
					<a class="normal" id="e_4" method="javascript:editScoreList();">编辑</a>
				</li>
			</ul>
			<div class="tip-text">* 推荐及排名奖励分值。</div>
			<ul class="tip-error" id="tip_error_4" style="display:none;"><li class="v" id="tip_error_4_v"></li></ul>
			<form name="" id="form_4">
			<input type="hidden" name="type" value="1"/>
			<table width="544" border="0" cellspacing="0" cellpadding="0" align="left" class="list-table-sort">
				<tr><td colspan="5" class="table-title"><center>阶段排名奖励分值</center></td></tr>
				<tr>
					<th width="22">N</th>
					<th width="163">排名名称</th>
					<th width="163">排名标识</th>
					<th width="163">排名奖励分值</th>
					<th width="22"><a class="add-score" href="javascript:createScoreList();" style="color:#FFF;"></a></th>
				</tr>
				<tbody id="scoreList"></tbody>
			</table>
			</form>
		</div>
		<div class="content" id="tab_2" style="display:none">
			<h3>机构、团队返利</h3>
			<div class="tip-box">
				<ul>
				<li>返利 = 理财顾问服务费 X [分值阶梯]返利占服务费比例</li>
				<li>返利分值 = 阶段累计规模 X 现阶段推荐成功率系数 + 奖励分值</li>
				</ul>
			</div>
			<ul class="ti-nav">
				<li class="ti-line-left"></li>
				<li class="ti-title">分值阶梯</li>
				<li class="ti-line-right">
					<a id="s_5" method="javascript:saveListForm('3');">保存</a>
					<a id="c_5" method="javascript:initList('3',true,2,0);">取消</a>
					<a class="normal" id="e_5" method="javascript:editStageList2();">编辑</a>
				</li>
			</ul>
			<div class="tip-text">* 返利分值阶梯，达到不同级别客户，追加相应比例返利。</div>
			<ul class="tip-error" id="tip_error_5" style="display:none;"><li class="v" id="tip_error_5_v"></li></ul>
			<form name="" id="form_5">
			<input type="hidden" name="type" value="2"/>
			<table width="890" border="0" cellspacing="0" cellpadding="0" align="left" class="list-table">
				<tr><td colspan="6" class="table-title"><center>返利分值阶梯</center></td></tr>
				<tr>
					<th width="22">N</th>
					<th width="211.5">等级名称</th>
					<th width="211.5">等级标识</th>
					<th width="211.5">阶段返利分值要求(万)</th>
					<th width="211.5">返利占服务费用比例(%)</th>
					<th width="22"><a class="add-stage-2" href="javascript:createStageList2();" style="color:#FFF;"></a></th>
				</tr>
				<tbody id="stageList2"></tbody>
			</table>
			</form>
			
			<ul class="ti-nav">
				<li class="ti-line-left"></li>
				<li class="ti-title">奖励分值</li>
				<li class="ti-line-right">
					<a id="s_6" method="javascript:saveListForm('4');">保存</a>
					<a id="c_6" method="javascript:initList('4',true,2,0);">取消</a>
					<a class="normal" id="e_6" method="javascript:editScoreList2();">编辑</a>
				</li>
			</ul>
			<div class="tip-text">* 排名奖励分值。</div>
			<ul class="tip-error" id="tip_error_6" style="display:none;"><li class="v" id="tip_error_6_v"></li></ul>
			<form name="" id="form_6">
			<input type="hidden" name="type" value="2"/>
			<table width="544" border="0" cellspacing="0" cellpadding="0" align="left" class="list-table-sort">
				<tr><td colspan="5" class="table-title"><center>阶段排名奖励分值</center></td></tr>
				<tr>
					<th width="22">N</th>
					<th width="163">排名名称</th>
					<th width="163">排名标识</th>
					<th width="163">排名奖励分值</th>
					<th width="22"><a class="add-score-2" href="javascript:createScoreList2();" style="color:#FFF;"></a></th>
				</tr>
				<tbody id="scoreList2"></tbody>
			</table>
			</form>
			
		</div>
	</body>
</html>