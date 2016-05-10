
function DataSource(index,url,names,_title,_closeText){
	this.index = index;
	this.url = url;
	this.names = names;
	var html= "<table cellSpacing=1 cellPadding=0 border=0 style='padding:0;'>"
	         +"    <tr><td><iframe width='450px' height='100px' name='valueFrame' id='valueFrame' src='"+url+"' frameborder='0'  style='padding:0px;'></iframe></td></tr>"
	         +"</table>";
	if(	attributeValueBoxy != null){
		attributeValueBoxy.setContent(html);
		attributeValueBoxy.show();
	}else{
		attributeValueBoxy = new Boxy(html, { title:_title, closeText:"["+_closeText+"]",modal: true }); 
		$(".boxy-wrapper").css("width","480px");
	}
	attributeValueBoxy.center();
}
DataSource.prototype = {
	saveDataSource:function(o){
	    this.clearValue();
		var temp;
		for(var i=0;i<this.names.length;i++){
			switch (i)
			{
			    case 0:
			    	temp = o.type;
			    	break;
			    case 1:
			    	temp = o.id;
			    	break;
			    case 2:
			    	temp = o.keyId;
			    	break;
			    case 3:
			    	temp = o.valueId;
			    	break;
			    case 4:
			    	temp = o.strObjArray;
			    	break;
			}
			$("#"+this.names[i]).val(temp);
		}
	},
	test:function (){alert(this.index)},
	clearValue:function(){
		for(var i=0;i<this.names.length;i++){
			$("#"+this.names[i]).val("");
		}
		//$("#ruleParameter\\["+this.index+"\\]\\.dataSourceType").val("");
		//$("#ruleParameter\\["+this.index+"\\]\\.dataSourceID").val("");
		//$("#ruleParameter\\["+this.index+"\\]\\.keyFieldID").val("");
		//$("#ruleParameter\\["+this.index+"\\]\\.valueFieldID").val("");
		//$("#ruleParameter\\["+this.index+"\\]\\.ruleParameterDataItem").val("");
	}
}
