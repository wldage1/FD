



Date.prototype.format = function(fmt) 
{ //author: meizz 
  var o = { 
    "M+" : this.getMonth()+1,                 //月份 
    "d+" : this.getDate(),                    //日 
    "h+" : this.getHours(),                   //小时 
    "m+" : this.getMinutes(),                 //分 
    "s+" : this.getSeconds(),                 //秒 
    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
    "S"  : this.getMilliseconds()             //毫秒 
  }; 
  if(/(y+)/.test(fmt)) 
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
  for(var k in o) 
    if(new RegExp("("+ k +")").test(fmt)) 
    	fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
  return fmt; 
};



function formatDate(value , format){
	if(undefined == format)
	{
		format ="yyyy-MM-dd";
	}
	if(null == value)
	{
		return "";
	}
	return new Date(value).format(format);
};


/**
 * 将字符串日期转化为日期型
 * @param str
 * @returns {Date}
 */
function convertStringToDate(str)
{
	if("" == str) 
	{
		return "";
	}
	str = Date.parse(str.replace(/-/,"/"));
	var date = new Date(str);
	return date;
}
