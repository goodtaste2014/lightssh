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
	
	<input type="button" class="action settings" value="设置企业信息" 
		onclick="location.href='<s:url value="viewparent.do?action=edit"/>'"/>
		
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
	
	<table class="list">
		<colgroup>
			<col class="element" width="50px"/>
			<col class="element" width="80px"/>
			<col class="element" width="400px"/>
			<col class="element" />
			<col class="element" width="50px"/>
		</colgroup>
		<thead>
			<tr>
				<th>&nbsp;</th>
				<th>类型</th>
				<th>联系方式</th>
				<th>描述</th>
				<th>操作</th>
			</tr>
		</thead>
		<s:iterator value="#request.party_contacts" status="loop">
			<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
				<td><s:property value="#loop.index+1"/></td>
				<td><s:property value="type"/></td>
				<td><s:property value="format()"/></td>
				<td><s:property value="description" escape="true"/></td>
				<td><a href="<s:url value="/settings/organization/removecontact.do?contact.id=%{id}"/>">删除</a></td>
			</tr>
		</s:iterator>
	</table>
	
</body>