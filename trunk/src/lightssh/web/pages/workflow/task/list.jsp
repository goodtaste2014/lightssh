<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>

	<title>任务列表</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
		});
		
		/**
		 * 认领任务
		 */		
		function popup( taskId ){
			var popup = $("#popup");
			//$( popup ).html( '<div><img id=\'loading\' src=\'<%= request.getContextPath() %>/images/loading.gif\'/>' );
			$( popup ).dialog({
				resizable: true,modal: true,height:300,width: 600,
				close: function(event, ui) { $(this).dialog('destroy'); },				
				buttons: {				
					"确认":function(){ location.href='claim.do?taskId='+taskId+'&userId='+$("input[name='userId']").val();}
					,"关闭": function() {$(this).dialog('destroy');}
				}
			});
			
		}
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>基础管理</li>
		<li>工作流</li>
		<li>任务列表</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
	
	<mys:table cssClass="list" value="task_page" status="loop" pageParamPrefix="task_page">
		<mys:column title="序号" width="50px">
			<s:property value="#loop.index + 1"/>
		</mys:column>
		<mys:column title="编号" value="getId()" sortKey="id" sortable="true" width="40px"/>
		<mys:column title="优先级" value="priority" sortable="true" width="40px"/>
		<mys:column title="修订" value="revision" sortable="false" width="60px"/>
		<mys:column title="名称" value="name" sortable="true" width="220px"/>
		<mys:column title="创建时间" value="@com.google.code.lightssh.common.util.TextFormater@format(createTime,'yyyy-MM-dd HH:hh:ss')" 
			sortKey="createTime" sortable="true" width="150px"/>
		<mys:column title="代理人" value="assignee" sortable="true" width="80px"/>
		<mys:column title="描述" value="description" />
		<%-- 
		--%>
		<mys:column title="操作" width="40px" cssClass="action">
			<span>&nbsp;</span>
			<div class="popup-menu-layer">
				<div class="popup-menu-list">
					<ul>
						<li><a href="claim.do?taskId=<s:property value="id"/>">由我认领</a></li>
						<li><a href="#" onclick="popup('<s:property value="id"/>');">分配任务</a></li>
					</ul>
					<ul>
						<li><a href="prepare.do?taskId=<s:property value="id"/>">提交流程</a></li>
					</ul>
				</div>
			</div>
		</mys:column>
	</mys:table>

	<mys:pagination value="task_page"  pageParamPrefix="task_page"/>
	
	<div id="popup" title="认领任务" style="display: none;">
		<table class="profile">
			<tr>
				<th>任务认领者</th>
				<td><input type="text" name="userId" size="40"/></td>
			</tr>
		</table>
	</div>
</body>
