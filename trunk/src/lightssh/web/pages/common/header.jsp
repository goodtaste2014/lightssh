<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<title>index page</title>
		<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/styles/<mys:theme />/theme.css" />
	
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.cookie.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/timers/jquery.timers-1.2.js"></script>
	
		<script type="text/javascript">
			
			$(document).ready(function(){
				$(document).everyTime(2000, function() {
			    	if( $.cookie( "REFRESH-HEADER" ) == 'TRUE' ){
			    		//alert($.cookie( "REFRESH-HEADER") );
			    		$.cookie( "REFRESH-HEADER",'',{path:'<%= request.getContextPath() %>'});
			    		location.reload();
			    	}
				});
			});
			
			function theme( link ){
				var select = $("<select></select>");
				$("<option value=''></option>").appendTo(select);
				$("<option value='default'>经典</option>").appendTo(select);
				$("<option value='vigor'>热血</option>").appendTo(select);
				
				$(link).hide();
				select.insertAfter(link);
				select.focus();
				
				$(select).blur(function(){
					$(this).hide()
					$(link).show()
				});
				
				$(select).change(function(){
					var date = new Date();
					date.setTime(date.getTime() + (365 * 24 * 60 * 60 * 1000)); //cookie 超期时间
					$.cookie( "theme", this.value ,{expires: date});
					
					var url = "<s:url value="/welcome.do?theme="/>" + this.value;
					top.location.href=url;
					//$(this).hide()
					//$(link).show()
				});
			}
			
			function locale( link ){
				var select = $("<select></select>");
				$("<option value=''></option>").appendTo(select);
				$("<option value='en'>ENGLISH</option>").appendTo(select);
				$("<option value='cn_ZH'>中文</option>").appendTo(select);
				
				$(link).hide();
				select.insertAfter(link);
				select.focus();
				
				$(select).blur(function(){
					$(this).hide()
					$(link).show()
				});
				
				$(select).change(function(){
					var url = "<s:url value="/welcome.do?locale="/>" + this.value;
					top.location.href=url;
				});
			}
		</script>
	</head>
	
	<body id="header">
		<div id="header">
			<div class="logo">
				<img alt="logo" src="<s:url value="/images/logo.png"/>" />
				<a href="http://code.google.com/p/lightssh/" target="_blank">Light SSH</a>
			</div>
			
			<div class="status" id="div_status">
				<div class="right">
					<s:set name="principal" value="@com.google.code.lightssh.project.security.service.SecurityUtil@getPrincipal()"/>
					<s:text name="project.header.welcome"/>：<a href="<s:url value="/security/account/my.do"/>" class="icon user" target="main_frame"><s:property value="#principal"/></a>
					
					|&nbsp;<a href="<s:url value="/welcome.do"/>" class="icon home" target="_top"><s:text name="project.header.home"/></a>
					|&nbsp;<a href="<s:url value="/security/account/edit.do?password=update"/>" class="icon password" target="main_frame"><s:text name="project.header.changepassword"/></a>
					
					|&nbsp;<a href="#" class="icon theme" onclick="theme(this)"><s:text name="project.header.theme"/></a>
					|&nbsp;<a href="#" class="icon language" onclick="locale(this)"><s:text name="project.header.language"/></a>
					
					|&nbsp;<a href="<s:url value="/logout.do"/>" class="icon exit" target="_top"><s:text name="project.header.exit"/></a>
				
				</div>
				<div class="left">&nbsp;</div>
			</div>
		</div>
	</body>
</html>