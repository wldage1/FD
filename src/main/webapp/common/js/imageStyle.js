//改变图片的显示是否为灰色   newid  要设置的图片id属性，style要设置是否可用  不可用为 0 可用 1
    function setNewAttr(newid,style){
    	var obj = $("#"+newid);
    	var osrc = $("#"+newid).attr("src");
    	var ind = osrc.lastIndexOf('_');
    	if(style == '1'){
    	var newsrc = osrc.slice(0,ind+1)+"02.png";
    	obj.removeAttr("src").attr("src",newsrc);
    	obj.removeAttr("disabled");
    	}
    	if(style == '0'){
    		var newsrc = osrc.slice(0,ind+1)+"01.png";
    		obj.removeAttr("src").attr("src",newsrc);
    		obj.attr("disabled","disabled");
    	}
    }
    
  //参数顺序为:实现-free、示忙-busy、休息-free、呼出-dial、挂断-hungUp、签入-logon、签出-logout、录音-record
    function setImgStyle(i1,i2,i3,i4,i5,i6,i7,i8){
		setNewAttr("free",i1);
		setNewAttr("busy",i2);
		setNewAttr("rest",i3);
		setNewAttr("dial",i4);
		setNewAttr("hungUp",i5);
		setNewAttr("logon",i6);
		setNewAttr("logout",i7);
		setNewAttr("record",i8);
	}
    
    /**
     * 将整数秒转换成时分秒格式
     */
    function FormatTime(intTime){
    	var TimeStr = "";
    	if(intTime >= 3600){
    		var Hours = parseInt(intTime/3600);
    		var Minutes = parseInt((intTime - 3600*Hours)/60);
    		var Seconds = intTime - Hours*3600-Minutes*60;
    		if(Hours < 10){
  	    	TimeStr = "0"+Hours;
  	  	}else{
  	  		TimeStr = ""+Hours;
  	  	}
  	  	if(Minutes < 10 ){
  	    	TimeStr += ":0"+Minutes;
  	  	}else{
  	    	TimeStr += ":"+Minutes;
  	  	}
  	  	if(Seconds < 10){
  	    	TimeStr += ":0"+Seconds;
  	  	}else{
  	  		TimeStr += ":"+Seconds;
  	  	} 
    	}
    	else if(intTime >= 60 && intTime < 3600){
    		var Minutes = parseInt(intTime/60);
    		var Seconds = intTime - Minutes*60;
      
  	  	if(Minutes < 10 ){
  	    	TimeStr = "00:0"+Minutes;
  	  	}else{
  	    	TimeStr = "00:"+Minutes;
  	  	}
  	  	if(Seconds < 10){
  	    	TimeStr += ":0"+Seconds;
  	  	}else{
  	  		TimeStr += ":"+Seconds;
  	  	} 
    	}
    	else {
    		var Seconds = intTime;
    		if(Seconds < 10){
  	    	TimeStr = "00:00:0"+Seconds;
  	  	}else{
  	  		TimeStr = "00:00:"+Seconds;
  	  	} 
    	}
    	return(TimeStr);
    }
    
    /**
     * 统计时间
     */
    function clockTime(state){
    	try{
    		timeCount++;
    		if(state){
    			OverTalkTimeCount++;
    			$("#time").html(FormatTime(timeCount));
    			//$("#ontime").html(FormatTime(OverTalkTimeCount));
    		}
  		
  	    ClockTimer = window.setTimeout("clockTime("+state+");", 1000);
      }catch(e){
      	alert("〖时间提示显示错误〗"+e);
      }
    }
    
    /**
	 * 开始计时
	 */
  function startClock(state){
  	if(ClockTimer != null){
  		window.clearTimeout(ClockTimer);
      ClockTimer = null;
  	}
    clockTime(state);
   // document.all.ClockIcon.src="images/hour_m.gif";
  }
    
    /**
	 * 结束计时
	 */
  function stopClock(){
	/********sinowel date 2013-01-24 呼叫时间和通话时间初始化**************************************************/
	timeCount = 0;
    OverTalkTimeCount = 0;
	/*********************************************************************************/
    if(ClockTimer != null){
      window.clearTimeout(ClockTimer);
      ClockTimer = null;
    }
  }
  
  /**
   * 重置计时
   */
  function clearClock(){
  	timeCount = 0;
    OverTalkTimeCount = 0;
	$("#time").html("00:00:00");
  }