<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/util/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>
</head>

<body>
	<ul class="path">
		<li>系统首页</li>
	</ul>
	
	<%@ include file="/pages/common/util/messages.jsp" %>
	
	System Panel!
	<br/>
	Hello, <shiro:principal/>, how are you today?
	
</body>