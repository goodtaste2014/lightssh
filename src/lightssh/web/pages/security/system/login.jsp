<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language ="java" pageEncoding = "UTF-8" contentType="text/html;charset=utf-8" %> 
<%@ page import ="org.springframework.security.authentication.*"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>	
	<title>系统登录</title>
	<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/styles/<mys:theme />/theme.css" />
	<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.min.js"></script>
	<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/cryptography/jquery.md5.js"></script>

	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	
	<script type="text/javascript">
		$(document).ready(function(){
			$("#js_warning").remove();
			$("#password").attr("value",'');
		});
		
		function dosubmit(){
			if( $("#username").attr('value') == ''){
				showMessage("'用户帐号'不能为空！");
				$("#username").focus();
				return false;
			}else if( $("#password").attr('value') == ''){
				showMessage("'登录密码'不能为空！");
				$("#password").focus();
				return false;
			}else if( $("#captcha").attr('value') == ''){
				showMessage("'验证码'不能为空！");
				$("#password").focus();
				return false;
			}
			
			$("#password").attr("value",$.md5($("#password").attr("value")));
			return true;				
		}
		
		/**
		 * 显示提示信息
		 */
		function showMessage( msg ){
			if( $('.error').length == 0 ){
				$('.messages').append("<div class='error'>"+msg+"</div>") 
			}else{
				$('.error').text( msg );
			}
		}
		
		function refresh(){
			var src = "<%= request.getContextPath() %>/images/kaptcha.jpg?rnd=" + Math.random();
			$("#jcaptcha").attr("src",src);
		}
	</script>
</head>

<body style="text-align: center;">
	<br/>
	<br/>
	
	<div id="login_panel">
	
	<div id="login">
		
		<div class="login_frame">
			<div class="login_form">
				<h3>用户登录</h3>
					
				<div class="messages">
					<div class="warning" id="js_warning">您的浏览器不支持JAVASCRIPT，无法登录系统！</div>
					<%@ include file="/pages/security/system/login_error.jsp" %>
				</div>
				
				<s:form id="login_form" action="login" namespace="/" method="post" onsubmit="return dosubmit();">
					<ul>
						<li>
							<label for="username">账号&nbsp;&nbsp;&nbsp;</label>
							<input type='text' id="username" class="user" name='j_username' size="30" autocomplete="off"/>
						</li>
						<li>
							<label for="password">密码&nbsp;&nbsp;&nbsp;</label>
							<input type='password' id="password" class="password" name='j_password' size="30"/>
						</li>
						
						<s:if test="showCaptcha">
						<li>
							<label for="captcha">验证码</label>
							<input type='text' class="captcha" id="captcha" name='j_captcha' size="15"/>
							<span class="refresh" onclick="refresh();">看不清楚?</span><br/>
							<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<!--<img id="jcaptcha" src="<%= request.getContextPath() %>/images/jcaptcha" />-->
							<img id="jcaptcha" alt="captcha" style="margin-left:0 !important;margin:4px 0 0 2px;" 
								src="<%= request.getContextPath() %>/images/kaptcha.jpg">
						</li>
						</s:if>
						
						<li>
							<label for="login_submit">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input type="submit" value="登录" id="login_submit" class="action login"/>
							<a href="#">忘记密码？</a>
						</li>
					</ul>
				</s:form>
			</div>
		</div>
	</div>	
	
	</div>
</body>
</html>
