<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<title>角色列表</title>
		
		<script type="text/javascript">
			function doRemove( id,name ){
				var url = '<s:url value="/security/role/remove.do?role.id="/>' + id ;
				if( confirm('确认删除角色[' + name + ']'))
					location.href=url;
			}
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>系统管理</li>
			<li>角色管理</li>
			<li>角色列表</li>
		</ul>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<s:form name="list" namespace="/system/role" method="post">
			<table class="profile">
				<tbody>
					<tr>
						<th><label for="name">名称</label></th>
						<td><s:textfield id="name" name="role.name" size="40" maxlength="100"/></td>
						<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
					</tr>
				</tbody>
			</table>
		</s:form>
		
		<mys:table cssClass="list" value="page" status="loop">
			<mys:column title="序号" width="50px">
				<s:property value="#loop.index + 1"/>
			</mys:column>
			<mys:column title="名称" value="name" sortable="true" width="260px"/>
			<mys:column title="创建日期" value="createdTime" sortable="true" width="160px"/>
			<mys:column title="描述" value="description"/>
			<mys:column title="操作" width="40px" cssClass="action">
				<span>&nbsp;</span>
				<div class="popup-menu-layer">
					<ul class="dropdown-menu">
						<li><a href="<s:url value="/security/role/permission.do?role.id=%{id}"/>">设置权限</a></li>
						<li><a href="<s:url value="/security/role/edit.do?role.id=%{id}"/>">编辑角色</a></li>
						<li class="section"/>
						<li>
							<a href="#" onclick="javascript:doRemove('<s:property value="%{id}"/>','<s:property value="%{name}"/>')">删除角色</a>
						</li>
					</ul>
				</div>
			</mys:column>
		</mys:table>
	
		<mys:pagination value="page" />
	</body>
</html>