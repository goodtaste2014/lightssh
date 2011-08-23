<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
	
		<title>定时任务</title>
		
	</head>
	
	<body>
		<ul class="path">
			<li>基础管理</li>
			<li>定时任务</li>
			<li>任务列表</li>
		</ul>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<table class="list">
			<colgroup>
				<col class="element" width="50px"/>
				<col class="element" width="160px"/>
				<col class="element" width="200px"/>
				<col class="element" width="30px"/>
				<col class="element" width="190px"/>
				<col class="element" />
				<col class="element" width="50px"/>
			</colgroup>
			<thead>
				<tr>
					<th>序号</th>
					<th>名称</th>
					<th>CRON表达式</th>
					<th>状态</th>
					<th>上/下次运行时间</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			
			<s:iterator value="#request.list" status="loop">
			<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
				<td><s:property value="#loop.index+1"/></td>
				<td><s:property value="name"/></td>
				<td><s:property value="cronExpression"/></td>
				<td><s:property value="pause?'暂停':'运行'"/></td>
				<td>
					[上次] <s:property value="@com.google.code.lightssh.common.util.TextFormater@format(previousFireTime,'yyyy-MM-dd HH:mm:ss')"/>
					<br/>[下次] <s:property value="@com.google.code.lightssh.common.util.TextFormater@format(nextFireTime,'yyyy-MM-dd HH:mm:ss')"/>
				</td>
				<td><s:property value="description"/></td>
				<td><a href="<s:url value="/settings/scheduler/toggle.do?name=%{name}"/>"><s:property value="pause?'启用':'停用'"/></a></td>
			</tr>
			</s:iterator>
			</table>
			
	</body>
</html>