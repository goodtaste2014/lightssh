<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>

	<title>计量单位列表</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
		});
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>基础管理</li>
		<li>计量单位</li>
		<li>搜索列表</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
	
	<s:form name="list" namespace="/settings/uom" method="post">
		<table class="profile">
			<tbody>
				<tr>
					<th><label for="type">类型</label></th>
					<td>
						<s:select id="type" list="@com.google.code.lightssh.project.uom.entity.UnitOfMeasure$UomType@values()"
						 listKey="name()" headerKey="" headerValue="" value="uom.type.name()" name="uom.type"/>
					</td>
					
					<th><label for="isocode">ISO编码</label></th>
					<td>
						<s:textfield name="uom.isoCode" />
					</td>
					
					<th><label for="active">状态</label></th>
					<td>
						<s:select headerKey="" headerValue="" list="#{true:'活动的',false:'已冻结'}" 
							name="uom.active" value="uom.getActive()"/>
					</td>
					
					<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
				</tr>
			</tbody>
		</table>
	</s:form>

	<table class="list">
		<colgroup>
			<col class="element" width="50px"/>
			<col class="element" width="80px"/>
			<col class="element" width="100px"/>
			<col class="element" width="100px"/>
			<col class="element" width="50px"/>
			<col class="element" />
			<col class="element" width="50px"/>
		</colgroup>
		<thead>
			<tr>
				<th>序号</th>
				<th>类型</th>
				<th>系统编号</th>
				<th>ISO编码</th>
				<th>状态</th>
				<th>描述</th>
				<th>操作</th>
			</tr>
		</thead>
		
		<s:iterator value="page.list" status="loop">
		<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
			<td><s:property value="#loop.index+1"/></td>
			<td><s:property value="%{type}"/></td>
			<td><s:property value="%{code}"/></td>
			<td><s:property value="%{isoCode}"/></td>
			<td><s:property value="%{active?'活动的':'已冻结'}"/></td>
			<td><s:property value="%{description}"/></td>
			<td>
				<a href="<s:url value="/settings/uom/toggle.do?uom.code=%{code}"/>">
					<s:property value="%{active?'冻结':'激活'}"/>
				</a>
			</td>
		</tr>
		</s:iterator>
		</table>
		
		<s:set name="pagination" value="%{page}"/>
		<jsp:include page="/pages/common/pagination.jsp"/>
</body>
