<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
<head>
	<meta name="decorator" content="background"/>
</head>

<body>
	<ul class="path">
		<li>基础管理</li>
		<li>企业资料</li>
		<li>查看信息</li>
	</ul>
	
	<input type="button" class="action settings" value="设置企业信息" onclick="location.href='<s:url value="viewparent.do?action=edit"/>'"/>
		
	<%@ include file="/pages/common/messages.jsp" %>
	
	<s:set name="org" value="party"/>
	
	<table class="profile">
		<tbody>
			<tr>
				<th>编号</th>
				<td><s:property value="#org.id"/></td>
			</tr>
			<tr>
				<th>名称</th>
				<td><s:property value="#org.name"/></td>
			</tr>
			<tr>
				<th>类型</th>
				<td>
					<s:property value="party_role_type"/>
				</td>
			</tr>
			<tr>
				<th>描述</th>
				<td><s:property value="#org.description"/></td>
			</tr>
		</tbody>
	</table>
</body>