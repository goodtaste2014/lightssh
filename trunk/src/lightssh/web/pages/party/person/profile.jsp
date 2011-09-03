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
				
				/**
				 * 数据校验
				 */
				$("#profile_form").validate({
					rules:{
						"party.name":{required:true}
					}
				});
				
				$("input[type='text'][name='party.birthday']").datepicker({dateFormat: 'yy-mm-dd',changeYear:true,yearRange:'-60,60'});
			});
			
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
				<li><a href="<s:url value="?profile=family"/>">家庭成员</a></li>
				--%>
			</ul> 
		
			<div id="tabs-1">
				<%@ include file="profile_base.jsp" %>
			</div>
	</div>
	
	<div id="popup" title="所属组织机构" style="display: none;">&nbsp;</div>
</body>