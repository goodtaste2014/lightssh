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
				<th><label for="name" >登录账号</label></th>
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
					<s:if test="account.period == null ">
						永不过期
					</s:if>
					<s:else>
						<s:property value="account.period.start"/> -
						<s:property value="account.period.end"/>
					</s:else>
				</td>
			</tr>
			<tr>
				<th><label>拥有的角色</label></th>
				<td>
					<s:iterator value="%{account.roles}" status="loop">
						<span style="white-space:nowrap;"><s:property value="name"/></span><br/>
					</s:iterator>
				</td>
			</tr>
			
			<tr>
				<th><label >邮箱地址</label></th>
				<td>
					<s:property value="account.email"/>
				</td>
			</tr>
			
			<tr>
				<th><label>上次更新密码时间</label></th>
				<td>
					<s:property value="account.lastUpdatePasswordTime"/>
				</td>
			</tr>
			
			<tr>
				<th><label for="desc">创建时间</label></th>
				<td>
					<s:property value="@com.google.code.lightssh.common.util.TextFormater@format(account.createDate,'yyyy-MM-dd HH:mm:ss')"/>
				</td>
			</tr>
			
			<tr>
				<th><label for="desc">描述</label></th>
				<td>
					<s:property value="account.description"/>
					<br/>
				</td>
			</tr>
			
		</tbody>
	</table>
</body>