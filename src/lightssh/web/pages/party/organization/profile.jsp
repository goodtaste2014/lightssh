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
						"party.name":{required:true,maxlength:100}
						,"party.description":{maxlength:200}
					}
					,submitHandler: function(form) {
						if( flase && !ajaxCheck( ) ) //TODO AJAX 提交存在问题！！！
							this.showErrors({"party.name": "名称已存在,请改用其它名称！"});
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
					,async: true
					,data: {
						"party":"organization"
			        	,"party.id": function(){
							return $("input[name='party.id']").val()
				        }
				        ,"party.name": function(){
							return $.trim( $("input[name='party.name']").val() );
				        }
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
						<s:set name="isInsert" value="%{(party==null||party.id==null)}"/>
						<input type="hidden" name="party" value="organization"/>
						<s:hidden name="party.id"/>
						<s:hidden name="party.enabled" value="%{#isInsert?true:party.enabled}"/>
						
						<s:textfield id="name" name="party.name" size="40" maxlength="300"/>
					</td>
				</tr>
				<tr>
					<th><label for="desc">描述</label></th>
					<td><s:textarea id="desc" name="party.description" cols="60" rows="5"/></td>
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