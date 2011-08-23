<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
	<head>
		<meta name="decorator" content="background"/>
		
		<title>添加用户角色</title>
		
		<script type="text/javascript">
			$(function() {	
				$("#version_list").dialog("destroy");						
			});
			
			/**
			 * 选择角色
			 */		
			function popup(){
				$("#version_list").dialog({
					resizable: true,
					height:300,
					width: 600,
					modal: true,
					close: function(event, ui) { $(this).dialog('destroy'); },				
					buttons: {					
						"关闭": function() {
							$(this).dialog('destroy');
						}
						,"选择": selectRoles
					}
				});
				
				var req_url = '<s:url value="/security/role/popup.do?page.size=1024"/>';
				$.ajax({
					type:"POST",
					url: req_url,
					dataType: 'json',
					timeout: 10000,
					error: function(){alert('list role error');$("#version_list").dialog('destroy');},
					success: function( json ) {
						showRoleList( json.page.list );
					}
				});
			}
			
			/**
			 * 显示角色列表
			 */
			function showRoleList( list ){
				$.each( list , function( index,role ){
					var item = "<input type='checkbox' value='"+role.id+"' rolename='"+role.name+"'/>"
					item += "<strong>"+role.name+"</strong>";
					item += ' - ' + (role.description==null?'':role.description);
					$("#version_list").append( "<div class='line'>"+ item + "</div>"  );
				});
				
				$("#loading").fadeOut();
			}
			
			/**
			 * 选择角色
			 */
			function selectRoles( ){
				$("#selected_roles").html('');
				var ids = '';
				$.each( $("input:checked"), function( index, checkbox ){
					var rolename = $(checkbox).attr('rolename');
					$("#selected_roles").append( (index==0?'':' ,')+ rolename );
					ids += (index==0?'':',') + $(checkbox).attr('value');
				});
				$("#selected_role_ids").attr('value',ids);
				$(this).dialog('destroy');
				$("input[type=submit]").removeAttr("disabled");
			}
		</script>
	</head>
	
<body>
	<ul class="path">
		<li>系统管理</li>
		<li>用户账号</li>
		<li>账号列表</li>
		<li>修改角色</li>
	</ul>
		
	<%@ include file="/pages/common/messages.jsp" %>
	
	<s:form action="role" namespace="/security/account" method="post">
		<table class="profile">
			<tbody>
				<tr>
					<th><label for="name">登录账号</label></th>
					<td>
						<s:hidden name="account.id"/>
						<s:hidden name="account.loginName"/>
						<s:property value="%{account.loginName}"/>
					</td>
				</tr>
				<tr>
					<th><label for="period">有效期</label></th>
					<td>
						<s:if test="account.period == null || (account.period.start == null && account.period.end == null)">
							永不过期
						</s:if>
						<s:elseif test="account.period.start==null">
							截止至<s:property value="account.period.end"/>
						</s:elseif>
						<s:elseif test="account.period.end == null">
							从<s:property value="account.period.start"/>开始有效
						</s:elseif>
						<s:else>
							<s:property value="account.period.start"/> 到
							<s:property value="account.period.end"/>
						</s:else>
					</td>
				</tr>
				<tr>
					<th><label for="desc">描述</label></th>
					<td><s:property value="account.description"/></td>
				</tr>
				
				<tr>
					<th><label for="role">角色</label></th>
					<td>
						<span class="popup" onclick="popup();">&nbsp;</span>
						<span id="selected_roles">
							<s:set name="ids" value="''"/>
							<s:iterator value="account.roles" status="loop">
								<s:property value="(#loop.first?'':',')+ name"/>
								<s:set name="ids" value="#ids + (#loop.first?'':',') + id"/>
							</s:iterator>
						</span>
						<s:hidden name="ids" id="selected_role_ids" value="%{#ids}"/>
					</td>
				</tr>
				
			</tbody>
		</table>
	
		<p class="submit">
			<input type="submit" class="action save" disabled="disabled" name="Submit" value="修改角色"/>
		</p>
	</s:form>

	<div id="version_list" title="角色列表" style="display: none;">&nbsp;</div>
</body>