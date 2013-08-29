<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

	<%@ include file="/pages/common/messages.jsp" %>

	<s:action name="%{#request.bizActionName}" namespace="%{#request.bizNamespace}" executeResult="true">
		<s:param name="%{#request.bizParamName}" value="%{#request.bizKey}"/>
	</s:action>
	
