<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>

	<title>任务列表</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
		});
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
		<mys:column title="编号" value="id" sortable="true" width="40px"/>
		<mys:column title="优先级" value="priority" sortable="true" width="40px"/>
		<mys:column title="修订" value="revision" sortable="true" width="60px"/>
		<mys:column title="名称" value="name" sortable="true" width="260px"/>
		<mys:column title="创建时间" value="createTime" sortable="true"/>
		<mys:column title="描述" value="description" />
		<%-- 
		--%>
		<mys:column title="操作" width="40px" cssClass="action">
			<span>&nbsp;</span>
			<div class="popup-menu-layer">
				<div class="popup-menu-list">
					<ul>
						<li><a href="#">新建流程</a></li>
					</ul>
					<ul>
						<li>
							<a href="#">详情</a>
						</li>
						<li>
							<a href="#">流程图</a>
						</li>
					</ul>
					<ul>
						<li>
							<a href="#">历史版本</a>
						</li>
					</ul>
				</div>
			</div>
		</mys:column>
	</mys:table>

	<mys:pagination value="task_page" />
</body>
