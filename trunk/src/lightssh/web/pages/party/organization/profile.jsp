<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
	<head>
		<meta name="decorator" content="background"/>
		
		<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.cookie.js"></script>
		<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/treeview/jquery.treeview.js"></script>
		<link rel="stylesheet" href="<%= request.getContextPath() %>/scripts/jquery/plugins/treeview/jquery.treeview.css" type="text/css">
		
		
		<title>编辑组织机构</title>
		
		<script type="text/javascript">
			$(document).ready(function(){
				/**
				 * 数据校验
				 */
				$("#profile_form").validate({
					rules:{
						"organization.name":{required:true,maxlength:100}
						,"organization.sequence":{digits:true}
						,"organization.description":{maxlength:200}
					}
					,submitHandler: function(form) {
						if( !ajaxCheck( ) )
							this.showErrors({"organization.name": "菜单名称已存在,请改用其它名称！"});
						else
							form.submit();
					}
				});
				
			});
			
			/**
			 * 检查名称是否重复
			 */
			function ajaxCheck( ){
				var result = false;
				$.ajax({
					url: "<s:url value="/party/organization/unique.do"/>"
					,dataType: "json" 
					,type:"post"
					,async: false
					,data: {
			        	"organization.id": function(){
							return $("input[name='organization.id']").val()
				        }
				        ,"organization.name": function(){
							return $.trim( $("input[name='organization.name']").val() );
				        },
				        
			        }
			        ,error: function(){ alert("检查重名出现异常!") }
			        ,success: function(json){
			        	result = json.unique;
			        }
				});
				
				return result;
			}
		
			/**
			 * 选择父节点弹出框
			 */		
			function popup(){
				var popup = $("#popup");
				$( popup ).html( '<div><img id=\'loading\' src=\'<%= request.getContextPath() %>/images/loading.gif\'/>' );
				$( popup ).dialog({
					resizable: true,modal: true,height:300,width: 600,
					close: function(event, ui) { $(this).dialog('destroy'); },				
					buttons: {				
						"关闭": function() {$(this).dialog('destroy');}
					}
				});
				
				var req_url = '<s:url value="/party/organization/popup.do"/>';
				$.post(req_url,{},function(data){$( popup ).html( data );});
			}
			
			/**
			 * 选择父节点
			 */		
			function callbackSelectOrganization( org ){
				$("#span_parent_name").html( org.name );
				$("#parent_id").val( org.id );
				$("#popup").dialog('destroy');
			}
		</script>
	</head>
	
<body>
	<ul class="path">
		<li>组织机构管理</li>
		<li>组织机构</li>
		<li>编辑账号</li>
	</ul>
		
	<%@ include file="/pages/common/messages.jsp" %>
	
	<s:form id="profile_form" action="save" namespace="/party/organization" method="post">
		<table class="profile">
			<tbody>
				<tr>
					<th><label for="name" class="required">名称</label></th>
					<td>
						<s:set name="isInsert" value="%{(organization==null||organization.id==null)}"/>
						<s:hidden name="organization.id"/>
						<s:hidden name="organization.enabled" value="%{#isInsert?true:organization.enabled}"/>
						
						<s:textfield id="name" name="organization.name" size="40" maxlength="300"/>
					</td>
				</tr>
				<tr>
					<th><label for="sequence">显示顺序</label></th>
					<td>
						<s:textfield id="sequence" name="organization.sequence"/>
					</td>
				</tr>
				<tr>
					<th><label for="parent">上级组织</label></th>
					<td>
						<span class="popup" onclick="popup();">&nbsp;</span>
						<s:hidden name="organization.parent.id" id="parent_id"/>
						<span id="span_parent_name"><s:property value="organization.parent.name"/></span>
					</td>
				</tr>
				<tr>
					<th><label for="desc">描述</label></th>
					<td><s:textarea id="desc" name="organization.description" cols="60" rows="5"/></td>
				</tr>
			</tbody>
		</table>
	
		<s:token/>
	
		<p class="submit">
			<input type="submit" class="action save" name="Submit" 
				value="<s:property value="%{#isInsert?\"新增组织机构\":\"修改组织机构\"}"/>"/>
		</p>
	</s:form>

	<div id="popup" title="上级组织" style="display: none;">&nbsp;</div>
</body>