<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<title>账号列表</title>
		
		<script type="text/javascript">
			function doRemove( id,name ){
				var url = '<s:url value="/security/account/remove.do?account.id="/>' + id ;
				if( confirm('确认删除用户账号[' + name + ']'))
					location.href=url;
			}
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>系统管理</li>
			<li>登录账号</li>
			<li>账号列表</li>
		</ul>
		
		<input type="button" class="action new" value="新增账号" onclick="location.href='<s:url value="edit.do"/>'"/>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<s:form name="list" namespace="/system/account" method="post">
			<table class="profile">
				<tbody>
					<tr>
						<th><label for="name">登录账号</label></th>
						<td><s:textfield id="name" name="account.loginName" size="40" maxlength="100"/></td>
						<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
					</tr>
				</tbody>
			</table>
		</s:form>
	
		<table class="list">
			<colgroup>
				<col class="element" width="50px"/>
				<col class="element" width="150px"/>
				<col class="element" width="150px"/>
				<col class="element" width="50px"/>
				<col class="element" width="90px"/>
				<col class="element" width="90px"/>
				<col class="element" width="90px"/>
				<col class="element" />
				<col class="element" width="100px"/>
			</colgroup>
			<thead>
				<tr>
					<th>序号</th>
					<th>登录账号</th>
					<th>会员</th>
					<th>有效</th>
					<th>创建日期</th>
					<th>有效期(起)</th>
					<th>有效期(止)</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			
			<s:iterator value="page.list" status="loop">
			<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
				<td><s:property value="#loop.index+1"/></td>
				<td><a href="<s:url value="/security/account/edit.do?account.id=%{id}"/>"><s:property value="%{loginName}"/></a></td>
				<td><s:property value="%{party.name}"/></td>
				<td><s:property value="%{enabled?'是':'否'}"/></td>
				<td><s:property value="%{createDate}"/></td>
				<td><s:property value="%{period.start}"/></td>
				<td><s:property value="%{period.end}"/></td>
				<td><s:property value="%{description}"/></td>
				<td>
					<a href="<s:url value="/security/account/edit.do?account.id=%{id}&role=update"/>">角色</a>
					<s:if test="loginName != @com.google.code.lightssh.project.security.service.LoginAccountManagerImpl@ROOT_LOGIN_NAME">
						<a href="#" onclick="javascript:doRemove('<s:property value="%{id}"/>','<s:property value="%{loginName}"/>')">删除</a>
					</s:if>
				</td>
			</tr>
			</s:iterator>
			</table>
			
			<s:set name="pagination" value="%{page}"/>
			<jsp:include page="/pages/common/pagination.jsp"/>
			
			<s:url var="report_url_param" value="/security/account/report.do"/>
			<s:set name="REPORT_URL" value="%{report_url_param}"/>
			<jsp:include page="/pages/common/report.jsp"/>
			
	</body>
</html>