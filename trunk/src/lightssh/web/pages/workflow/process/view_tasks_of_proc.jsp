<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

	<mys:table cssClass="list" value="#request.tasksOfProc" status="loop">
		<mys:column title="序号" width="50px">
			<s:property value="#loop.index + 1"/>
		</mys:column>
		<mys:column title="时间" width="150px">
			<s:property value="@com.google.code.lightssh.common.util.TextFormater@format(startTime,'yyyy-MM-dd HH:mm:ss')"/>
		</mys:column>
		<mys:column title="操作者" value="assignee" width="120px"/>
		<mys:column title="操作类型" value="" width="60px"/>
		<mys:column title="流转意见" value="description"/>
	</mys:table>
