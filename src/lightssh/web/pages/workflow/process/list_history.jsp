<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>

	<title>历史流程列表</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
		});
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>基础管理</li>
		<li>工作流</li>
		<li>历史流程实例</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
	
	<mys:table cssClass="list" value="hp_page" status="loop" pageParamPrefix="hp_page">
		<mys:column title="序号" width="50px">
			<s:property value="#loop.index + 1"/>
		</mys:column>
		<%-- 
		<mys:column title="ID" value="getId()" width="80px"/>
		--%>
		<mys:column title="流程ID" value="processDefinitionId" sortable="true" width="160px"/>
		<mys:column title="流程实例ID" value="processInstanceId" sortable="true" width="60px"/>
		<mys:column title="活动" sortable="false" width="50px">
			<s:property value="active?'是':'否'"/>
		</mys:column>
		<mys:column title="第一执行人" value="startUserId" sortable="false" width="50px"/>
		<mys:column title="结束任务ID" value="endActivityId" width="100px"/>
				<mys:column title="开始时间" sortable="true" sortKey="startTime" width="150px">
			<s:property value="@com.google.code.lightssh.common.util.TextFormater@format(startTime,'yyyy-MM-dd HH:hh:ss')"/>
		</mys:column>
		<mys:column title="结束时间" sortable="true" sortKey="endTime" width="150px">
			<s:property value="@com.google.code.lightssh.common.util.TextFormater@format(endTime,'yyyy-MM-dd HH:hh:ss')"/>
		</mys:column>
		<mys:column title="执行时间" width="50px" sortable="true" sortKey="duration">
			<s:property value="@com.google.code.lightssh.common.util.TextFormater@timeFormat(durationInMillis)"/>
		</mys:column>
		<mys:column title="业务关键字" value="businessKey" sortable="true"/>
		<mys:column title="操作" width="40px" cssClass="action">
			<span>&nbsp;</span>
			<div class="popup-menu-layer">
				<div class="popup-menu-list">
					<ul>
						<li>
							<a href="#">&nbsp;</a>
						</li>
					</ul>
				</div>
			</div>
		</mys:column>
	</mys:table>

	<mys:pagination value="hp_page" />
</body>
