<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="org.springframework.security.authentication.*"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>	
	<title>系统登录</title>
	<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/styles/<mys:theme />/theme.css" />
	<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.min.js"></script>
	<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/cryptography/jquery.md5.js"></script>

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
			if( $('.error').html() == null )
				$('.messages').child("<div class='error'>"+msg+"</div>") 
			else 
				$('.error').text( msg );
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
		<div class="login_form_top">
			<span class="left">&nbsp;</span>
			<span class="right">&nbsp;</span>
		</div> 
		
		<div class="login_frame">
			<div class="login_form">
				<div class="right">
					<h3>系统登录</h3>
					
					<div class="messages" style="float: none;">
						<div class="warning" id="js_warning">您的浏览器不支持JAVASCRIPT，无法登录系统！</div>
						<s:set name="isShiro" value="#request.secuirtyFramework=='shiro'"/>
						<s:if test="%{#isShiro}">
							<%@ include file="/pages/security/system/login_error_shiro.jsp" %>
						</s:if>
						<s:else>
							<%@ include file="/pages/security/system/login_error.jsp" %>
						</s:else>
					</div>
					
					<s:form id="login_form" action="%{#isShiro?'login':'j_spring_security_check'}" namespace="/" method="post" onsubmit="return dosubmit();">
						<ul>
							<li>
								<label for="username">账号：</label>
								<input type='text' id="username" class="user" name='j_username' size="30" autocomplete="off"/>
							</li>
							<li>
								<label for="password">密码：</label>
								<input type='password' id="password" class="password" name='j_password' size="30"/>
							</li>
							
							<s:if test="#session.login_failure_count >= 3">
							<li>
								<label for="captcha">验证码：</label>
								<input type='text' class="captcha" id="captcha" name='j_captcha' size="15"/>
								<span class="refresh" onclick="refresh();">看不清楚?</span><br/>
								<label>&nbsp;</label>
								<!--<img id="jcaptcha" src="<%= request.getContextPath() %>/images/jcaptcha" />-->
								<img id="jcaptcha" alt="captcha" style="margin-left:0 !important;margin:4px 0 0 2px;" src="<%= request.getContextPath() %>/images/kaptcha.jpg">
							</li>
							</s:if>
							
							<li>
								<label for="login_submit">&nbsp;</label>
								<input type="submit" value="登录" id="login_submit" class="action login"/>
							</li>
						</ul>
					</s:form>
				
				</div>
			</div>
		</div>
		
		<div class="login_form_bottom">
			<span class="left">&nbsp;</span>
			<span class="right">&nbsp;</span>
		</div>
	</div>	
	
	</div>
</body>

