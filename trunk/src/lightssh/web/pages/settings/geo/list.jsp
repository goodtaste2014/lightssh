<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>

	<title>地理区域列表</title>
	
</head>
	
<body>
	<ul class="path">
		<li>基础管理</li>
		<li>地理区域</li>
		<li>搜索列表</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
	
	<s:form name="list" namespace="/settings/uom" method="post">
		<table class="profile">
			<tbody>
				<tr>
					<th><label for="type">类型</label></th>
					<td>
						<s:select id="type" list="@com.google.code.lightssh.project.geo.entity.GeoType@frequentlyUsed()"
						 listKey="name()" headerKey="" headerValue="" value="geo.type.name()" name="geo.type"/>
					</td>
					
					<th><label for="name">名称</label></th>
					<td>
						<s:textfield id="name" name="geo.name" />
					</td>
					
					<th><label for="active">状态</label></th>
					<td>
						<s:select headerKey="" headerValue="" list="#{true:'活动的',false:'已冻结'}" 
							name="geo.active" value="geo.getActive()" id="active"/>
					</td>
					
					<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
				</tr>
			</tbody>
		</table>
	</s:form>

	<%-- 
	<mys:pagination value="page"/>
	--%>
	
	<mys:table cssClass="list" value="page" status="loop">
		<mys:column title="序号" width="50px">
			<s:property value="#loop.index + 1"/>
		</mys:column>
		<mys:column title="类型" value="type" sortable="true" sortKey="type" width="80px"/>
		<mys:column title="系统编号" value="code" sortable="true" width="120px"/>
		<mys:column title="缩写" value="abbreviation" sortable="false" width="80px"/>
		<mys:column title="名称" value="name" sortable="true" width="180px"/>
		<mys:column title="数字编码" value="numericCode" sortable="true" width="100px"/>
		<mys:column title="状态" width="50px" sortable="true" sortKey="active">
			<s:property value="%{active?'活动的':'已冻结'}"/>
		</mys:column>
		<mys:column title="描述" value="description"/>
		<mys:column title="操作" width="40px" cssClass="action">
			<span>&nbsp;</span>
			<div class="popup-menu-list">
				<ul class="dropdown-menu">
					<li>
						<a href="<s:url value="/settings/geo/toggle.do?geo.code=%{code}"/>">
							<s:property value="%{active?'冻结':'激活'}"/>
						</a>
					</li>
					<li><a href="#">编辑数据</a></li>
					<li><a href="#">删除数据</a></li>
					<li class="section"/>
					<li><a href="#">... ...</a></li>
				</ul>
			</div>
		</mys:column>
	</mys:table>
	
	<mys:pagination value="page" pageSizeArray="15,50,100,500"/>
	
	<%-- 
	<table class="list">
		<colgroup>
			<col class="element" width="50px"/>
			<col class="element" width="80px"/>
			<col class="element" width="120px"/>
			<col class="element" width="80px"/>
			<col class="element" width="180px"/>
			<col class="element" width="100px"/>
			<col class="element" width="50px"/>
			<col class="element" />
			<col class="element" width="50px"/>
		</colgroup>
		<thead>
			<tr>
				<th>序号</th>
				<th>类型</th>
				<th class="sortable descending active">系统编号</th>
				<th>缩写</th>
				<th>名称</th>
				<th>数字编码</th>
				<th class="sortable ascending active">状态</th>
				<th>描述</th>
				<th>操作</th>
			</tr>
		</thead>
		
		<s:iterator value="page.list" status="loop">
		<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/> <s:property value="#loop.index!=1?'':'focused'"/>">
			<td><s:property value="#loop.index+1"/></td>
			<td><s:property value="%{type}"/></td>
			<td><s:property value="%{code}"/></td>
			<td><s:property value="%{abbreviation}"/></td>
			<td><s:property value="%{name}"/></td>
			<td><s:property value="%{numericCode}"/></td>
			<td><s:property value="%{active?'活动的':'已冻结'}"/></td>
			<td><s:property value="%{description}"/></td>
			<td class="action">
				<a href="<s:url value="/settings/geo/toggle.do?geo.code=%{code}"/>">
					<s:property value="%{active?'冻结':'激活'}"/>
				</a>
			</td>
		</tr>
		</s:iterator>
		</table>
		
		<s:set name="pagination" value="%{page}"/>
		<jsp:include page="/pages/common/pagination.jsp"/>
		--%>
</body>
