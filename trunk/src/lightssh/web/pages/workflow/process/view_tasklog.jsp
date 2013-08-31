<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/util/taglibs.jsp" %>

<s:if test="#request.tasklogOfProc==null">
	无数据！
</s:if>

<mys:table cssClass="list" value="#request.tasklogOfProc" status="loop">
	<mys:column title="序号" width="50px">
		<s:property value="#loop.index + 1"/>
	</mys:column>
	<mys:column title="时间" width="150px">
		<s:property value="createdTime"/>
	</mys:column>
	<mys:column title="操作者" value="operator" width="120px"/>
	<mys:column title="操作类型" value="type" width="60px"/>
	<mys:column title="流转意见" value="description"/>
</mys:table>
