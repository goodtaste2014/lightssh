<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
<head>
	<meta name="decorator" content="background"/>
	
	<title>我的账号</title>
</head>
	
<body>

	<ul class="path">
		<li>系统管理</li>
		<li>登录账号</li>
		<li>我的账号</li>
	</ul>
		
	<%@ include file="/pages/common/messages.jsp" %>
	
	<table class="profile">
		<tbody>
			<tr>
				<th><label for="name" class="required">登录账号</label></th>
				<td>
					<s:property value="account.loginName"/>
				</td>
			</tr>
			<tr>
				<th><label for="account_party">会员</label></th>
				<td>
					<span id="span_party_name"><s:property value="%{account.party.name}"/></span>
				</td>
			</tr>
			<tr>
				<th><label for="account_enabled">是否可用</label></th>
				<td>
					<s:property value="account.enabled?'是':'否'"/>
				</td>
			</tr>
			<tr>
				<th><label for="account_start_date">有效期</label></th>
				<td>
					<s:property value="account.period.start"/> -
					<s:property value="account.period.end"/>
				</td>
			</tr>
			<tr>
				<th><label for="desc">描述</label></th>
				<td><s:property value="account.description"/></td>
			</tr>
		</tbody>
	</table>
</body>