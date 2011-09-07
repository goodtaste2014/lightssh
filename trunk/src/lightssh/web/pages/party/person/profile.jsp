<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
	<head>
		<meta name="decorator" content="background"/>
		
		<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.cookie.js"></script>
		<script language="javascript" src="<%= request.getContextPath() %>/scripts/jquery/plugins/treeview/jquery.treeview.js"></script>
		<link rel="stylesheet" href="<%= request.getContextPath() %>/scripts/jquery/plugins/treeview/jquery.treeview.css" type="text/css">
		
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/ui/i18n/jquery.ui.datepicker_zh_CN.js"></script>
	
		
		<title>编辑人员</title>
		
		<script type="text/javascript">
			$(document).ready(function(){
				$( "#tabs" ).tabs({ ajaxOptions: { async: false },cache: true });
				$( "#tabs" ).bind( "tabsload", function(event, ui) {
					showDatepicker();
				});
				
				/**
				 * 数据校验
				 */
				$("#profile_form").validate({
					rules:{
						"party.name":{required:true}
					}
				});
				
				showDatepicker();
			});
			
			function showDatepicker( ){
				$("input[type='text'][name='party.birthday']").datepicker(
					{dateFormat: 'yy-mm-dd',changeYear:true,yearRange:'-60,60'});
			}
			
			/**
			 * 自动填值
			 */
			function autofill( ele ){
				var p_identity = $("input[name='party.identityCardNumber']");
				if( $(ele).attr("name") == "party.identityCardNumber" 
					|| $(ele).attr("name") == "party.credentialsType"){
					if( $(p_identity).val().length == 18 
						&& 'P01' == $("select[name='party.credentialsType']").val() ){
						var ymd = $(p_identity).val().substring(6,14);
						ymd = ymd.substring(0,4)+'-'+ymd.substring(4,6)+'-'+ymd.substring(6,8)
						$("input[name='party.birthday']").val( ymd )
					}
				}
			}
		</script>
	</head>
	
<body>
	<ul class="path">
		<li>组织机构管理</li>
		<li>人员管理</li>
		<li>编辑人员</li>
	</ul>
		
	<%@ include file="/pages/common/messages.jsp" %>
	
	<div id="tabs"> 
			<ul> 	
				<li><a href="#tabs-1">基本信息</a></li>
				<li><a href="<s:url value="?profile=contact"/>">联系方式</a></li>
				<%-- 
				<li><a href="<s:url value="?profile=family"/>">人事信息</a></li>
				<li><a href="<s:url value="?profile=family"/>">工作经历</a></li>
				--%>
				<li><a href="<s:url value="?profile=family"/>">家庭成员</a></li>
			</ul> 
		
			<div id="tabs-1">
				<%@ include file="profile_base.jsp" %>
			</div>
	</div>
	
	<div id="popup" title="所属组织机构" style="display: none;">&nbsp;</div>
</body>