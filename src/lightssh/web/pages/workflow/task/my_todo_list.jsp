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
					"确认":function(){ location.href='proxyclaim.do?taskId='+taskId+'&userId='+$("input[name='userId']").val();}
					,"关闭": function() {$(this).dialog('destroy');}
				}
			});
			
		}
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>消息中心</li>
		<li>待办事宜</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
	
	<mys:table cssClass="list" value="taskPage" status="loop" pageParamPrefix="task_page">
		<mys:column title="序号" width="30px">
			<s:property value="#loop.index + 1"/>
		</mys:column>
		<mys:column title="流程编号" value="processInstanceId" width="80px"/>
		<mys:column title="流程类型" width="100px">
			<s:set name="procInst" value="@com.google.code.lightssh.project.workflow.util.WorkflowHelper@getProcessInstance(processInstanceId)"/>
			<s:set name="procDef" value="@com.google.code.lightssh.project.workflow.util.WorkflowHelper@getProcessDefinition(processDefinitionId)"/>
			<s:property value="#procDef.name"/>
		</mys:column>
		<mys:column title="流程名称">
			<s:set name="procAttr" value="@com.google.code.lightssh.project.workflow.util.WorkflowHelper@getProcAttr(processInstanceId)"/>
			<s:property value="#procAttr.bizName"/>
		</mys:column>
		<mys:column title="流程节点" value="name" sortable="false" width="160px"/>
		<mys:column title="创建人" value="#procInst.startUserId" sortable="false" width="80px"/>
		<mys:column title="创建时间" value="@com.google.code.lightssh.common.util.TextFormater@format(#procInst.startTime,'yyyy-MM-dd HH:hh:ss')" 
			sortable="false" width="150px"/>
		<mys:column title="到达时间" value="@com.google.code.lightssh.common.util.TextFormater@format(createTime,'yyyy-MM-dd HH:hh:ss')" 
			sortKey="createTime" sortable="false" width="150px"/>
		<mys:column title="操作" width="40px" cssClass="action">
			<span>&nbsp;</span>
			<div class="popup-menu-layer">
				<ul class="dropdown-menu">
					<li><a href="prepare.do?taskId=<s:property value="id"/>">处理流程</a></li>
					<li class="section"/>
					<li class="disabled"><a href="#" onclick="popup('<s:property value="id"/>');">签转代办</a></li>
				</ul>
			</div>
		</mys:column>
	</mys:table>

	<mys:pagination value="taskPage"  pageParamPrefix="taskPage"/>
	
	<div id="popup" title="签转代办" style="display: none;">
		<table class="profile">
			<tr>
				<th>任务代办者</th>
				<td><input type="text" name="userId" size="40"/></td>
			</tr>
		</table>
	</div>
</body>
