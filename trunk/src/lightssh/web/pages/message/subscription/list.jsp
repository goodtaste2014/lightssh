<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/util/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		
		<script type="text/javascript" src="<s:url value="/pages/party/popup.js" />"></script>
		
		<title>消息订阅</title>
		
		<script type="text/javascript">
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>基础管理</li>
			<li>消息管理</li>
			<li>消息订阅</li>
		</ul>
		
		<%@ include file="/pages/common/util/messages.jsp" %>
		
		<mys:table cssClass="list" value="page" dynamic="false" status="loop">
			<mys:column title="序号" width="40px">
				<s:property value="#loop.index + 1"/>
			</mys:column>
			<mys:column title="编号" value="identity" sortKey="id" sortable="false" width="80px"/>
			<mys:column title="消息类型" value="catalog.type" width="80px"/>
			<mys:column title="订阅类型" value="subType" sortable="true" width="60px"/>
			<mys:column title="订阅值" value="subValue" sortable="true" width="60px"/>
			<mys:column title="有效期(起)" value="period.start" width="90px"/>
			<mys:column title="有效期(止)" value="period.start" width="90px"/>
			<mys:column title="创建人" value="creator" width="100px"/>
			<mys:column title="创建时间" value="createdTime" width="150px"/>
			<mys:column title="描述" value="description" />
			<%-- 
			<mys:column title="操作" width="40px" cssClass="action">
				<span>&nbsp;</span>
				<div class="popup-menu-layer">
					<ul class="dropdown-menu">
						<li class="section"/>
						
						<li class="edit">
							<a href="#">编辑信息</a>
						</li>
					</ul>
				</div>
			</mys:column>
			--%>
		</mys:table>
		
		<mys:pagination value="page"/>
	</body>
</html>