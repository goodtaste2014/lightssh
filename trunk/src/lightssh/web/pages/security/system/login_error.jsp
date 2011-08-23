<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="org.springframework.security.authentication.*"%>
<%@ page import="com.google.code.lightssh.project.security.service.BadCaptchaException"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<%
	org.springframework.security.core.AuthenticationException authExp = (org.springframework.security.core.AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
	if( authExp != null ){
		String expMsg = "登录异常:" + authExp.getMessage();
		if( authExp instanceof DisabledException ){
			expMsg = "用户账号已被禁用!";
		}else if( authExp instanceof AccountExpiredException ){
			expMsg = "用户账号已过期!";
		}else if( authExp instanceof BadCredentialsException ){
			expMsg = "错误的用户账号或密码！";
		}else if( authExp instanceof BadCaptchaException ){
			expMsg = "验证码错误!";
		}else if( authExp instanceof AuthenticationServiceException ){
			expMsg = "错误的用户账号!";
		}
		out.print("<div class=\"error\">" + expMsg + "</div>");
	}
	
%>