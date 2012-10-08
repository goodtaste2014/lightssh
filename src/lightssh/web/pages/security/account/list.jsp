<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<title>账号列表</title>
		
		<script type="text/javascript">
			function doRemove( id,name ){
				var url = '<s:url value="/security/account/remove.do?account.id="/>' + id ;
				if( confirm('确认删除用户账号[' + name + ']'))
					location.href=url;
			}
		</script>
	</head>
	
	<body>
		<ul class="path">
			<li>系统管理</li>
			<li>登录账号</li>
			<li>账号列表</li>
		</ul>
		
		<input type="button" class="action new" value="新增账号" onclick="location.href='<s:url value="edit.do"/>'"/>
		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<s:form name="list" namespace="/system/account" method="post">
			<table class="profile">
				<tbody>
					<tr>
						<th><label for="name">登录账号</label></th>
						<td><s:textfield id="name" name="account.loginName" value="%{cachedParams['account.loginName']}" size="40" maxlength="100"/></td>
						<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
					</tr>
				</tbody>
			</table>
		</s:form>
		
		<mys:table cssClass="list" value="page" status="loop">
			<mys:column title="序号" width="50px">
				<s:property value="#loop.index + 1"/>
			</mys:column>
			<mys:column title="登录账号" value="loginName" sortable="true" width="150px"/>
			<mys:column title="会员" value="party.name" sortable="false" width="150px"/>
			<mys:column title="有效" sortable="true" width="50px" sortKey="enabled">
				<s:property value="%{enabled?'是':'否'}"/>
			</mys:column>
			<mys:column title="创建日期" value="createDate" sortable="true" width="90px"/>
			<mys:column title="有效期(起)" value="period.start" sortable="true" width="90px" />
			<mys:column title="有效期(止)" value="period.end" sortable="true" width="90px"/>
			<mys:column title="角色" width="200px">
				<s:iterator value="roles">
					<s:property value="%{name}"/>
				</s:iterator>
			</mys:column>
			<mys:column title="描述" value="description"/>
			<mys:column title="操作" width="40px" cssClass="action">
				<span>&nbsp;</span>
				<div class="popup-menu-layer box-shadow">
					<div class="popup-menu-list" style="white-space: nowrap; width: auto; ">
						<ul class="section">
							<li>
								<a href="<s:url value="/security/account/edit.do?account.id=%{id}"/>">编辑帐号</a>
							</li>
							<li>
								<a href="<s:url value="/security/account/edit.do?account.id=%{id}&role=update"/>">编辑角色</a>
							</li>
						</ul>
						
						<ul class="section">
							<li>
								<a href="#">禁用帐号</a>
							</li>
							<li>
								<a href="<s:url value="/security/account/prereset.do?account.loginName=%{loginName}"/>">重设密码</a>
							</li>
						</ul>
						
						<ul class="section">
							<li>
								<s:if test="loginName != @com.google.code.lightssh.project.security.service.LoginAccountManagerImpl@ROOT_LOGIN_NAME">
									<a href="#" onclick="javascript:doRemove('<s:property value="%{id}"/>','<s:property value="%{loginName}"/>')">删除帐号</a>
								</s:if>
							</li>
						</ul>
					</div>
				</div>
			</mys:column>
		</mys:table>
	
		<mys:pagination value="page" />
	
		<s:url var="report_url_param" value="/security/account/report.do"/>
		<s:set name="REPORT_URL" value="%{report_url_param}"/>
		<jsp:include page="/pages/common/report.jsp"/>
			
	</body>
</html>