<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
<head>
	<meta name="decorator" content="background"/>
	
	<title>工作流</title>
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/ui/i18n/jquery.ui.datepicker_zh_CN.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			//$("input[type='text'][class='date']").datepicker(
			//		{dateFormat: 'yy-mm-dd',changeYear:true,yearRange:'-60,60'});
		});
	</script>
</head>
	
<body>
	<ul class="path">
		<li>基础管理</li>
		<li>工作流</li>
		<li>提交流程</li>
	</ul>
		
	<%@ include file="/pages/common/messages.jsp" %>
	<s:set name="task" value="#request.task_form_data.task"/>
	<s:if test="#task != null ">
		任务名称：<s:property value="#task.name"/>
	</s:if>
	
	<s:form action="submit">
		<s:hidden name="taskId" value="%{#task.id}"/>
		<table class="profile">
		<s:iterator value="#request.task_form_data.formProperties">
			<tr>
				<th><s:property value="name"/></th>
				<td><input type="text" name="<s:property value="id"/>" class="<s:property value="type.name"/>"/></td>
			</tr>
		</s:iterator>
		</table>
		
		<input type="submit" class="action save" value="提交流程"/>
	</s:form>
</body>