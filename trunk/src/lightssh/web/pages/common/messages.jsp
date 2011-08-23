<%@ include file="/pages/common/taglibs.jsp"%>
<%@ page pageEncoding="utf-8"%>

<div class="messages">
	<%-- ActionError Messages - usually set in Actions --%>
	<s:if test="hasActionErrors()">
		<s:iterator value="actionErrors">
			<div class="warning"><s:property escape="false" /></div>
		</s:iterator>
	</s:if>
	
	<%-- Action Messages - usually set in Actions --%>
	<s:if test="hasActionMessages()">
		<s:iterator value="actionMessages">
			<div class="ok"><s:property escape="false" /></div>
		</s:iterator>
	</s:if>
	
	<%-- FieldError Messages - usually set by validation rules --%>
	<s:if test="hasFieldErrors()">
		<s:iterator value="fieldErrors">
			<div class="warning" id="<s:property  value="key" />"><s:property  value="value[0]" escape="false"/></div>		
		</s:iterator>
	</s:if>
	
	<%-- Success Messages --%>
	<c:if test="${not empty success_messages}">
		<c:forEach var="msg" items="${success_messages}">
			<div class="success"><c:out value="${msg}" escapeXml="false"/></div>
		</c:forEach>
		<c:remove var="success_messages" scope="session" />
	</c:if>
	
	<%-- error Messages --%>
	<c:if test="${not empty error_messages}">
		<c:forEach var="msg" items="${error_messages}">
			<div class="error"><c:out value="${msg}" escapeXml="false"/></div>
		</c:forEach>
		<c:remove var="error_messages" scope="session" />
	</c:if>
</div>