<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>

	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/ui/i18n/jquery.ui.datepicker_zh_CN.js"></script>
	
	<title>流程列表</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$(".calendar").datepicker({dateFormat: 'yy-mm-dd',changeYear:true});
		});
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>工作流管理</li>
		<li>流程管理</li>
		<li>我的流程</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
		
	<s:form name="list" method="post">
		<table class="profile">
			<tbody>
				<tr>
					<th><label for="proid">流程编号</label></th>
					<td><s:textfield id="proid" name="process.processInstanceId" size="20" maxlength="100"/></td>
					
					<th><label for="defid">流程类型</label></th>
					<td><s:textfield id="defid" name="process.processDefinitionId" size="20" maxlength="100"/></td>
					
				</tr>
				<tr>
					<th><label for="owner">开始时间</label></th>
					<td>
						<s:textfield name="process.startPeriod.start" cssClass="calendar" id="access_start_date" size="10" /> -
						<s:textfield name="process.startPeriod.end" cssClass="calendar" id="access_end_date" size="10"/>
					</td>
					
					<th><label for="status">流程状态</label></th>
					<td>
						<s:select name="process.finish" list="#{false:'活动',true:'结束' }" headerKey="" headerValue="" />
						
						<input type="submit" class="action search right" value="查询"/>
					</td>
					
				</tr>
			</tbody>
		</table>
	</s:form>
	
	<mys:table cssClass="list" value="hp_page" status="loop" pageParamPrefix="hp_page">
		<mys:column title="序号" width="30px">
			<s:property value="#loop.index + 1"/>
		</mys:column>
		<%-- 
		<mys:column title="ID" value="getId()" width="80px"/>
		<mys:column title="流程ID" value="processDefinitionId" sortable="true" width="160px"/>
		<mys:column title="业务关键字" value="businessKey" sortable="true"/>
		<mys:column title="结束任务ID" value="endActivityId" width="100px"/>
		<mys:column title="结束时间" sortable="true" sortKey="endTime" width="150px">
			<s:property value="@com.google.code.lightssh.common.util.TextFormater@format(endTime,'yyyy-MM-dd HH:mm:ss')"/>
		</mys:column>
		<mys:column title="执行时间" width="50px" sortable="true" sortKey="duration">
			<s:property value="@com.google.code.lightssh.common.util.TextFormater@timeFormat(durationInMillis)"/>
		</mys:column>
		--%>
		<mys:column title="流程编号" value="processInstanceId" sortable="true" sortKey="PROC_INST_ID_" width="80px"/>
		<mys:column title="流程类型" width="180px">
			<s:set name="proDef" value="@com.google.code.lightssh.project.workflow.util.WorkflowHelper@getProcessDefinition(processDefinitionId)"/>
			<s:property value="#proDef.name"/>
		</mys:column>
		<%--<mys:column title="流程名称" />--%>
		<mys:column title="流程节点">
			<s:if test="endTime==null">
				<s:set name="task" value="@com.google.code.lightssh.project.workflow.util.WorkflowHelper@listTaskByProcessId(getId())"/>
				<s:iterator value="#task" status="loop">
					<s:property value="#loop.first?'':','"/><s:property value="name"/>
				</s:iterator>
			</s:if>
			<s:else>
				流程已结束
			</s:else>
		</mys:column>
		<mys:column title="状态" sortable="false" width="50px">
			<font color="<s:property value="endTime==null?'green':'black'"/>"><s:property value="endTime==null?'活动':'结束'"/></font>
		</mys:column>
		<mys:column title="创建者" value="startUserId" sortable="false" width="100px"/>
		<mys:column title="开始时间" sortable="true" sortKey="START_TIME_" width="150px">
			<s:property value="@com.google.code.lightssh.common.util.TextFormater@format(startTime,'yyyy-MM-dd HH:mm:ss')"/>
		</mys:column>
		
		<mys:column title="操作" width="40px" cssClass="action">
			<span>&nbsp;</span>
			<div class="popup-menu-layer">
				<ul class="dropdown-menu">
					<li class="view">
						<a href="<s:url value="view.do?process.processInstanceId=%{processInstanceId}"/>">流程详情</a>
					</li>
				</ul>
			</div>
		</mys:column>
	</mys:table>

	<mys:pagination value="hp_page" pageParamPrefix="hp_page"/>
</body>
