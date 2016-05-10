<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib prefix="page" uri="/WEB-INF/tlds/sitemesh-page.tld" %>
<%@ taglib prefix="security" uri="/WEB-INF/tlds/springframework-security.tld" %>
<%@ taglib prefix="spring" uri="/WEB-INF/tlds/springframework-spring.tld" %>
<%@ taglib prefix="form" uri="/WEB-INF/tlds/springframework-form.tld" %>
<%@ taglib prefix="msg" uri="/WEB-INF/tlds/springframework-message.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="${base}/${skin}/css/index.css" rel="stylesheet" type="text/css"/>
<link href="${base}/${skin}/css/login_select.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="${base}/${skin}/css/jquery.jscrollpane.codrops1.css">
<script type="text/javascript" src="${base}/common/js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="${base}/common/js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript" src="${base}/common/js/scroll-startstop.events.jquery.js"></script>
<script type="text/javascript">
function formSubmit(){
	var businessTypeId = $('input[name="businessTypeId"]:checked').val();
	if(businessTypeId == null || businessTypeId == ""){
		window.parent.Boxy.alert("请选择业务种类！", null, {title: "<msg:message code='info.prompt'/>"});
		return false;
	}else{
		window.location.href = "${base}/seatcenter/main?c=901&businessTypeId="+businessTypeId;
	}
}

</script>
</head>
<body>
<table class="centre_login" cellpadding="0" cellspacing="0">
    <tr>
        <td class="login_left">
        </td>
        <td valign="top" class="login_centre">
            <table cellpadding="0" cellspacing="0" class="centre_login_centre">
                <tr>
                    <td class="login_select_tu">
                    </td>
                </tr>
                <tr>
                    <td valign="top" height="140">
                        <div class="select_container">
                            <div id="" class="jp-container">
                                <ul>
                                	<c:forEach var="item"  items="${businessTypeList}" varStatus="vs1">
                                		<li><label for="businessTypeId${item.id}"><input type="radio" id="businessTypeId${item.id}" name="businessTypeId" value="${item.id}">${item.name}</label></li><br/>
									</c:forEach>
                                </ul>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="button" value="" class="inputbutton" onclick="formSubmit()" />
                    </td>
                </tr>
            </table>
    </td>
        <td class="login_right">
        </td>
    </tr>
</table>

<table class="bottom">
  <tr>
        <td>光大银行版权所有
        </td>
    </tr>
</table>
<script type="text/javascript">
			$(function() {
			
				// the element we want to apply the jScrollPane
				var $el					= $('#jp-container').jScrollPane({
					verticalGutter 	: -16
				}),
						
				// the extension functions and options 	
					extensionPlugin 	= {
						
						extPluginOpts	: {
							// speed for the fadeOut animation
							mouseLeaveFadeSpeed	: 500,
							// scrollbar fades out after hovertimeout_t milliseconds
							hovertimeout_t		: 1000,
							// if set to false, the scrollbar will be shown on mouseenter and hidden on mouseleave
							// if set to true, the same will happen, but the scrollbar will be also hidden on mouseenter after "hovertimeout_t" ms
							// also, it will be shown when we start to scroll and hidden when stopping
							useTimeout			: true,
							// the extension only applies for devices with width > deviceWidth
							deviceWidth			: 980
						},
						hovertimeout	: null, // timeout to hide the scrollbar
						isScrollbarHover: false,// true if the mouse is over the scrollbar
						elementtimeout	: null,	// avoids showing the scrollbar when moving from inside the element to outside, passing over the scrollbar
						isScrolling		: false,// true if scrolling
						addHoverFunc	: function() {
							
							// run only if the window has a width bigger than deviceWidth
							if( $(window).width() <= this.extPluginOpts.deviceWidth ) return false;
							
							var instance		= this;
							
							// functions to show / hide the scrollbar
							$.fn.jspmouseenter 	= $.fn.show;
							$.fn.jspmouseleave 	= $.fn.fadeOut;
							
							// hide the jScrollPane vertical bar
							var $vBar			= this.getContentPane().siblings('.jspVerticalBar').hide();
							
							/*
							 * mouseenter / mouseleave events on the main element
							 * also scrollstart / scrollstop - @James Padolsey : http://james.padolsey.com/javascript/special-scroll-events-for-jquery/
							 */
							$el.bind('mouseenter.jsp',function() {
								
								// show the scrollbar
								$vBar.stop( true, true ).jspmouseenter();
								
								if( !instance.extPluginOpts.useTimeout ) return false;
								
								// hide the scrollbar after hovertimeout_t ms
								clearTimeout( instance.hovertimeout );
								instance.hovertimeout 	= setTimeout(function() {
									// if scrolling at the moment don't hide it
									if( !instance.isScrolling )
										$vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
								}, instance.extPluginOpts.hovertimeout_t );
								
								
							}).bind('mouseleave.jsp',function() {
								
								// hide the scrollbar
								if( !instance.extPluginOpts.useTimeout )
									$vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
								else {
								clearTimeout( instance.elementtimeout );
								if( !instance.isScrolling )
										$vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
								}
								
							});
							
							if( this.extPluginOpts.useTimeout ) {
								
								$el.bind('scrollstart.jsp', function() {
								
									// when scrolling show the scrollbar
								clearTimeout( instance.hovertimeout );
								instance.isScrolling	= true;
								$vBar.stop( true, true ).jspmouseenter();
								
							}).bind('scrollstop.jsp', function() {
								
									// when stop scrolling hide the scrollbar (if not hovering it at the moment)
								clearTimeout( instance.hovertimeout );
								instance.isScrolling	= false;
								instance.hovertimeout 	= setTimeout(function() {
									if( !instance.isScrollbarHover )
											$vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
									}, instance.extPluginOpts.hovertimeout_t );
								
							});
							
								// wrap the scrollbar
								// we need this to be able to add the mouseenter / mouseleave events to the scrollbar
							var $vBarWrapper	= $('<div/>').css({
								position	: 'absolute',
								left		: $vBar.css('left'),
								top			: $vBar.css('top'),
								right		: $vBar.css('right'),
								bottom		: $vBar.css('bottom'),
								width		: $vBar.width(),
								height		: $vBar.height()
							}).bind('mouseenter.jsp',function() {
								
								clearTimeout( instance.hovertimeout );
								clearTimeout( instance.elementtimeout );
								
								instance.isScrollbarHover	= true;
								
									// show the scrollbar after 100 ms.
									// avoids showing the scrollbar when moving from inside the element to outside, passing over the scrollbar								
								instance.elementtimeout	= setTimeout(function() {
									$vBar.stop( true, true ).jspmouseenter();
								}, 100 );	
								
							}).bind('mouseleave.jsp',function() {
								
									// hide the scrollbar after hovertimeout_t
								clearTimeout( instance.hovertimeout );
								instance.isScrollbarHover	= false;
								instance.hovertimeout = setTimeout(function() {
										// if scrolling at the moment don't hide it
									if( !instance.isScrolling )
											$vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
									}, instance.extPluginOpts.hovertimeout_t );
								
							});
							
							$vBar.wrap( $vBarWrapper );
							
						}
						
						}
						
					},
					
					// the jScrollPane instance
					jspapi 			= $el.data('jsp');
					
				// extend the jScollPane by merging	
				$.extend( true, jspapi, extensionPlugin );
				if (typeof(jspapi) != "undefined") { 
					jspapi.addHoverFunc();
				}
			
			});
		</script>
</body>
</html>