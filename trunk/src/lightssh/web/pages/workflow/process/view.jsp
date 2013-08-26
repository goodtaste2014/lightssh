<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>
	
	<title>流程情况</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
		});
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>工作流管理</li>
		<li>流程管理</li>
		<li>流程详情</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
		
	<s:set name="proc" value="#request.process"/>
	<table class="profile">
		<caption>流程信息</caption>
		<colgroup>
			<col width="100px"/>
			<col width="50%"/>
			<col width="10%"/>
			<col />
		</colgroup>
		<tbody>
			<tr>
				<%--<th>流程编号</th>--%>
				<th>
					<s:property value="#proc.processInstanceId"/>
				</th>
				<%--<th><label >流程类型</label></th>--%>
				<td>
					<s:set name="proDef" value="@com.google.code.lightssh.project.workflow.util.WorkflowHelper@getProcessDefinition(#proc.processDefinitionId)"/>
					<s:property value="#proDef.name"/>
				</td>
				<%--<th><label >创建者</label></th>--%>
				<td>
					<s:property value="#proc.startUserId"/>
				</td>
				<%--<th><label >创建时间</label></th>--%>
				<td>
					<s:property value="@com.google.code.lightssh.common.util.TextFormater@format(#proc.startTime,'yyyy-MM-dd HH:mm:ss')"/>
				</td>
			</tr>
		</tbody>
	</table>
	
	<s:action name="tasksofproc" namespace="/workflow/process" executeResult="true" >
		<s:param name="process.processInstanceId" value="#proc.processInstanceId"/>
	</s:action>

</body>
