<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<title>人员列表</title>
		
		<script type="text/javascript">
			function doRemove( id,name ){
				var url = '<s:url value="/party/person/remove.do"/>?party=person&party.id=' + id ;
				if( confirm('确认删除人员[' + name + ']'))
					location.href=url;
			}
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>组织机构管理</li>
			<li>人员管理</li>
			<li>人员列表</li>
		</ul>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<%-- 
		<s:form name="list" namespace="/party/organization" method="post">
			<table class="profile">
				<tbody>
					<tr>
						<th><label for="name">名称</label></th>
						<td><s:textfield id="name" name="organization.name" size="40" maxlength="100"/></td>
						<td colspan="2"><input type="submit" value="查询"/></td>
					</tr>
				</tbody>
			</table>
		</s:form>
		--%>
		
		<mys:table cssClass="list" value="page" status="loop">
			<mys:column title="序号" width="40px">
				<s:property value="#loop.index + 1"/>
			</mys:column>
			<mys:column title="编号" value="identity" sortKey="id" sortable="true" width="50px"/>
			<mys:column title="姓名" value="name" sortable="true" width="100px"/>
			<mys:column title="性别" value="gender" sortable="true" width="20px"/>
			<mys:column title="婚况" value="maritalStatus" sortable="true" width="20px"/>
			<mys:column title="国籍" value="country.name" sortable="true" width="40px"/>
			<mys:column title="民族" value="ethnicGroup" sortable="false" width="80px"/>
			<mys:column title="政治面貌" value="partyAffiliation" sortable="false" width="50px"/>
			<mys:column title="证件类型" value="credentialsType" sortable="true" width="100px"/>
			<mys:column title="证件号码" value="identityCardNumber" sortable="true" width="130px"/>
			<%-- 
			<mys:column title="有效" value="enabled?'是':'否'" sortable="true" width="50px"/>
			--%>
			<mys:column title="描述" value="description" />
			<mys:column title="操作" width="40px" cssClass="action">
				<span>&nbsp;</span>
				<div class="popup-menu-layer">
					<ul class="dropdown-menu">
						<li>
							<a href="<s:url value="/party/person/edit.do?party=person&party.id=%{id}"/>">编辑信息</a>
						</li>
						<li>
							<a href="<s:url value="/party/person/view.do?party=person&party.id=%{id}"/>">查看人员</a>
						</li>
						
						<li class="section"/>
						
						<li>
							<a href="#" onclick="javascript:doRemove('<s:property value="%{id}"/>','<s:property value="%{name}"/>')">删除</a>
						</li>
					</ul>
				</div>
			</mys:column>
		</mys:table>
		
		<mys:pagination value="page"/>
	</body>
</html>