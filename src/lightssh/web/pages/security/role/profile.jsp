<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
	<head>
		<meta name="decorator" content="background"/>
		
		<title>角色信息</title>
		
		<script type="text/javascript">
			$(document).ready(function(){
				/**
				 * 数据校验
				 */
				$("#role_form").validate({
					rules:{
						"role.name":{required:true,maxlength:100}
						,"role.description":{maxlength:200}
					}
				});
			});
		</script>
	</head>
	
<body>
	<ul class="path">
		<li>系统管理</li>
		<li>角色管理</li>
		<li><s:property value="%{(role==null||role.id==null)?\"新增角色\":\"修改角色\"}"/></li>
	</ul>
		
	<%@ include file="/pages/common/messages.jsp" %>
	
	<s:form id="role_form" action="save" namespace="/security/role" method="post">
		<table class="profile">
			<tbody>
				<tr>
					<th><label for="name" class="required">名称</label></th>
					<td>
						<s:hidden name="role.id"/>
						<s:textfield id="name" name="role.name" size="40" maxlength="300"/>
					</td>
				</tr>
				<tr>
					<th><label for="desc">描述</label></th>
					<td><s:textarea id="desc" name="role.description" cols="60" rows="5"/></td>
				</tr>
			</tbody>
		</table>
	
		<s:token/>

		<p class="submit">
			<input type="submit" class="action save" name="Submit" 
				value="<s:property value="%{(role==null||role.id==null)?\"新增角色\":\"修改角色\"}"/>"/>
		</p>
	</s:form>

</body>