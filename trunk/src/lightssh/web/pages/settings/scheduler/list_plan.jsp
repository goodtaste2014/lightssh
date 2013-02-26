<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
	
		<title>执行计划</title>
		
		<script type="text/javascript">
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>基础管理</li>
			<li>计划任务</li>
			<li>执行计划</li>
		</ul>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<mys:table cssClass="list" value="page" status="loop">
			<mys:column title="序号"  width="50px">
				<s:property value="#loop.index+1"/>
			</mys:column>
			
			<mys:column title="编号" value="identity" sortable="true" sortKey="id" width="120px"/>
			<mys:column title="计划执行时间" value="planFireTime" sortable="true" width="150px"/>
			<mys:column title="实际执行时间" value="fireTime" sortable="false" width="150px"/>
			<mys:column title="执行完成时间" value="finishTime" sortable="false" width="150px"/>
			<mys:column title="状态" width="50px">
				<s:property value="finished?'完成':'未完成'"/>
			</mys:column>
			<%-- 
			--%>
			<mys:column title="创建时间" value="createdTime" width="150px"/>
			<mys:column title="描述" value="description"/>
			<mys:column title="操作" width="40px" cssClass="action">
				<span>&nbsp;</span>
				<div class="popup-menu-layer">
					<ul class="dropdown-menu">
						<li class="list">
							<a href="<s:url value="listdetail.do?plan.id=%{id}"/>">执行计划明细</a>
						</li>
						<li class="section"/>
					</ul>
				</div>
			</mys:column>
		</mys:table>
			
		<mys:pagination value="page"/>	
	</body>
</html>