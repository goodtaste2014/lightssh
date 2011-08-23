<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<title>用户列表</title>
		<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/scripts/jquery/plugins/treetable/jquery.treetable.css" />
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/treetable/jquery.treetable.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/livefilter/jquery.livefilter.js"></script>
		
		<script type="text/javascript">
			function doRemove( id,name ){
				var url = '<s:url value="/party/organization/remove.do?organization.id="/>' + id ;
				if( confirm('确认删除组织机构[' + name + ']'))
					location.href=url;
			}
			
			/**
			 * init treeTable
			 */
			function initTreeTable( ){
				$("#orgnization_list").treeTable(
				  	 //{expandable: false}
				);
			}
			
			$(document).ready(function(){
				initTreeTable();
				$('#live_filter').liveFilter('table');
			});
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>组织机构管理</li>
			<li>组织机构</li>
			<li>组织列表</li>
		</ul>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<div id="live_filter">
		
		<label for="name">名称/描述</label>
		<input id="name" class="filter" name="livefilter" type="text" value="" size="40" onchange="if(this.value=='')initTreeTable();"/>
		
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
	
		<table id="orgnization_list" class="list">
			<colgroup>
				<col class="element" width="50px"/>
				<col class="element" width="100px"/>
				<col class="element" width="200px"/>
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
					<th>上级组织</th>
					<th>有效</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			
			<s:iterator value="#request.list" status="loop1">
				<s:set name="parent" value="null"/>
				<s:set name="loop" value="#loop1"/>
				<%@include file="table_tr.jsp" %>
			
				<s:iterator value="%{sortedChildren}" status="loop2">
					<s:set name="parent" value="parent.id"/>
					<s:set name="loop" value="#loop2"/>
					<%@include file="table_tr.jsp" %>
					
					<s:iterator value="%{sortedChildren}" status="loop3">
						<s:set name="parent" value="parent.id"/>
						<s:set name="loop" value="#loop3"/>
						<%@include file="table_tr.jsp" %>
					</s:iterator>
				</s:iterator>
			</s:iterator>
			</table>
		</div>
	</body>
</html>