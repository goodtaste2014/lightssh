<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>
	
	<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.cookie.js"></script>
	<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/treeview/jquery.treeview.js"></script>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/scripts/jquery/plugins/treeview/jquery.treeview.css" type="text/css">
	
	<title>树列表</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
		});
		
		/**
		 * 显示树结点
		 */
		function viewnode( id ){
			var popup = $("#popup");
			$( popup ).html( '<div><img id=\'loading\' src=\'<%= request.getContextPath() %>/images/loading.gif\'/>' );
			$( popup ).dialog({
				resizable: true,modal: true,height:400,width: 700,
				close: function(event, ui) { $(this).dialog('destroy'); },				
				buttons: {				
					"关闭": function() {$(this).dialog('destroy');}
				}
			});
			
			var req_url = '<s:url value="/settings/tree/popup.do"/>';
			$.post(req_url,{"view":"true","level":4,"tree.id":id},function(data){$( popup ).html( data );});
		}
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>基础管理</li>
		<li>分类树</li>
		<li>搜索列表</li>
	</ul>
	
	<input type="button" class="action new" value="新增分类树" onclick="location.href='<s:url value="edit.do"/>'"/>
		
	<%@ include file="/pages/common/messages.jsp" %>
	
	<s:form name="list" namespace="/settings/tree" method="post">
		<table class="profile">
			<tbody>
				<tr>
					<th><label for="name">名称</label></th>
					<td>
						<%-- 
						<s:select id="type" list="@com.google.code.lightssh.project.uom.entity.UnitOfMeasure$UomType@values()"
						 listKey="name()" headerKey="" headerValue="" value="uom.type.name()" name="uom.type"/>
						--%>
						<s:textfield name="tree.name" id="name"/>
					</td>
					
					<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
				</tr>
			</tbody>
		</table>
	</s:form>

	<table class="list">
		<colgroup>
			<col class="element" width="50px"/>
			<col class="element" width="300px"/>
			<col class="element" />
			<col class="element" width="200px"/>
		</colgroup>
		<thead>
			<tr>
				<th>序号</th>
				<th>名称</th>
				<th>描述</th>
				<th>操作</th>
			</tr>
		</thead>
		
		<s:iterator value="page.list" status="loop">
		<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
			<td><s:property value="#loop.index+1"/></td>
			<td><a href="<s:url value="edit.do?tree.id=%{id}"/>"><s:property value="%{name}"/></a></td>
			<td><s:property value="%{description}"/></td>
			<td>
				<span class="view" onclick="viewnode('<s:property value="%{id}"/>')">查看结点</span>
				<a class="new" href="<s:url value="/settings/tree/editnode.do?tree.id=%{id}"/>">
					添加结点
				</a>
			</td>
		</tr>
		</s:iterator>
		</table>
		
		<s:set name="pagination" value="%{page}"/>
		<jsp:include page="/pages/common/pagination.jsp"/>
		
		<div id="popup" title="树结点" style="display: none;">&nbsp;</div>
</body>
