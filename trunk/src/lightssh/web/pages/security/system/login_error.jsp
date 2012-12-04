<%@page language="java" pageEncoding ="UTF-8" contentType="text/html;charset=utf-8"%>

<%@page import="com.google.code.lightssh.project.security.service.BadCaptchaException"%>
<%@page import="com.google.code.lightssh.project.security.shiro.TimeLockedException"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="org.apache.shiro.subject.Subject"%>
<%@page import="org.apache.shiro.authc.*"%>

<%@include file="/pages/common/taglibs.jsp"%>

<%
	Object obj=request.getAttribute(org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	AuthenticationException authExp = (AuthenticationException)obj;
	if( authExp != null ){
		String expMsg="";
		
		if(authExp instanceof UnknownAccountException || authExp instanceof IncorrectCredentialsException){
			expMsg="错误的用户账号或密码！";
		}else if( authExp instanceof BadCaptchaException){
			expMsg="验证码错误！";
		}else if( obj instanceof LockedAccountException ){
			expMsg= "用户账号已禁用！";
		}else if( obj instanceof ExpiredCredentialsException ){
			expMsg= "用户账号已过期！";
		}else if( obj instanceof TimeLockedException ){
			expMsg= authExp.getMessage();
		}else{
			expMsg="登录异常:"+authExp.getMessage() ;
		}
		
		out.print("<div class=\"messages\">");
		out.print("<div class=\"error\">"+expMsg+"</div>");
		out.print("</div>");
	}
%>