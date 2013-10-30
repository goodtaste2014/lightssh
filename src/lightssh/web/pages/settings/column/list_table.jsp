<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/util/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		
		<title>定制化表</title>
		
		<script type="text/javascript">
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>基础管理</li>
			<li>定制化列</li>
			<li>定制化表</li>
		</ul>
		
		<%@ include file="/pages/common/util/messages.jsp" %>
		
		<mys:table cssClass="list" value="page" dynamic="false" status="loop">
			<mys:column title="序号" width="40px" value="#loop.index + 1"/>
			<mys:column title="编号" value="identity" width="200px"/>
			<mys:column title="名称" value="name" width="280px"/>
			<mys:column title="描述" value="description" />
			<mys:column title="操作" width="40px" cssClass="action">
				<span>&nbsp;</span>
				<div class="popup-menu-layer">
					<ul class="dropdown-menu">
						<%--<li class="section"/>--%>
						
						<li class="view">
							<a href="#">查看明细</a>
						</li>
					</ul>
				</div>
			</mys:column>
		</mys:table>
		
		<mys:pagination value="page"/>
	</body>
</html>