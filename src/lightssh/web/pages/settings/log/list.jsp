<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/ui/i18n/jquery.ui.datepicker_zh_CN.js"></script>
	
		<title>系统日志</title>
		
		<script type="text/javascript">
			$(document).ready(function(){
				$("#access_start_date").datepicker({dateFormat: 'yy-mm-dd',changeYear:true});
				$("#access_end_date").datepicker({dateFormat: 'yy-mm-dd',changeYear:true});
			});
			
		</script>
		
	</head>
	
	<body>
		<ul class="path">
			<li>基础管理</li>
			<li>系统日志</li>
			<li>日志列表</li>
		</ul>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<s:form name="list" namespace="/settings/log" method="post">
			<table class="profile">
				<tbody>
					<tr>
						<th><label for="type">类型</label></th>
						<td>
							<s:select id="type" list="@com.google.code.lightssh.project.log.entity.AccessType@values()"
							 listKey="name()" headerKey="" headerValue="" value="access.type.name()" name="access.type"/>
						</td>
						
						<th><label for="time">时间</label></th>
						<td>
							<s:textfield name="access._period.start" id="access_start_date" size="10" /> -
							<s:textfield name="access._period.end" id="access_end_date" size="10"/>
						</td>
						
						<th><label for="ip">IP</label></th>
						<td><s:textfield id="ip" name="access.ip"/></td>
						
						<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
					</tr>
				</tbody>
			</table>
		</s:form>
	
		<table class="list">
			<colgroup>
				<col class="element" width="50px"/>
				<col class="element" width="80px"/>
				<col class="element" width="150px"/>
				<col class="element" width="100px"/>
				<col class="element" width="100px"/>
				<col class="element" />
				<col class="element" width="50px"/>
			</colgroup>
			<thead>
				<tr>
					<th>序号</th>
					<th>类型</th>
					<th>时间</th>
					<th>IP</th>
					<th>操作者</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			
			<s:iterator value="page.list" status="loop">
			<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
				<td><s:property value="#loop.index+1"/></td>
				<td><s:property value="%{type}"/></td>
				<td><s:property value="@com.google.code.lightssh.common.util.TextFormater@format(time,'yyyy-MM-dd HH:mm:ss')"/></td>
				<td><s:property value="%{ip}"/></td>
				<td><s:property value="%{operator}"/></td>
				<td><s:property value="%{description}"/></td>
				<td>
					<s:if test="showHistory">
						<a href="<s:url value="/settings/log/compare.do?access.id=%{id}"/>">比较</a>
					</s:if>
				</td>
			</tr>
			</s:iterator>
			</table>
			
			<s:set name="pagination" value="%{page}"/>
			<jsp:include page="/pages/common/pagination.jsp"/>
	</body>
</html>