<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<title>用户列表</title>
		
		<script type="text/javascript">
			function doRemove( id,name ){
				var url = '<s:url value="/party/organization/remove.do"/>?party=organization&party.id=' + id ;
				if( confirm('确认删除组织机构[' + name + ']'))
					location.href=url;
			}
			
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>组织机构管理</li>
			<li>组织机构</li>
			<li>组织列表</li>
		</ul>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<s:form name="list" namespace="/party/organization" method="post">
			<input type="hidden" name="party" value="organization"/>
			<table class="profile">
				<tbody>
					<tr>
						<th><label for="name">名称</label></th>
						<td><s:textfield id="name" name="party.name" size="40" maxlength="100"/></td>
						<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
					</tr>
				</tbody>
			</table>
		</s:form>
	
		<table class="list">
			<colgroup>
				<col class="element" width="50px"/>
				<col class="element" width="100px"/>
				<col class="element" width="200px"/>
				<col class="element" width="50px"/>
				<col class="element" />
				<col class="element" width="100px"/>
			</colgroup>
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>编号</th>
					<th>名称</th>
					<th>有效</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			
			<s:iterator value="page.list" status="loop">
			<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
				<td><s:property value="#loop.index+1"/></td>
				<td><s:property value="%{id}"/></td>
				<td><a href="<s:url value="/party/organization/edit.do?party=organization&party.id=%{id}"/>"><s:property value="%{name}"/></a></td>
				<td><s:property value="%{enabled?'是':'否'}"/></td>
				<td><s:property value="%{description}"/></td>
				<td>
					<a href="#" onclick="javascript:doRemove('<s:property value="%{id}"/>','<s:property value="%{name}"/>')">删除</a>
				</td>
			</tr>
			</s:iterator>
		</table>
		
		<s:set name="pagination" value="%{page}"/>
		<jsp:include page="/pages/common/pagination.jsp"/>
	</body>
</html>