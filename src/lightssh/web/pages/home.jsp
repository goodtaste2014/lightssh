<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/pages/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title><s:text name="project.name" /></title>
		<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/styles/<mys:theme />/theme.css" />
	</head>
	
	<frameset rows="70px,*" frameborder="no" border="0" framespacing="0">
		<frame id="head_frame" scrolling="no" src="<s:url value="/header.do"/>"></frame>
		<frameset id="main_frameset" cols="20%,*">
			<frame id="navigation_frame" name="navigation_frame" scrolling="no" src="<s:url value="/navigation.do"/>"></frame>
			<frame id="main_frame" name="main_frame" scrolling="yes" src="<s:url value="/panel.do"/>"></frame>
		</frameset>		
		<noframes>
			<body>该浏览器不支持FRAMESET!</body>
		</noframes>
	</frameset>	
</html>