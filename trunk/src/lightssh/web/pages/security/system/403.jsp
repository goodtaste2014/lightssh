<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="org.springframework.security.authentication.*"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>	
	<title>403</title>
	<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/styles/<mys:theme />/theme.css" />
	<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.min.js"></script>
	
</head>

<body style="text-align: center;">
	<h3>403,您没有权限访问该资源！</h3>
</body>

