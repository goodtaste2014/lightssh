<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>
	
	<title>流程情况</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
			//$( "#tabs" ).tabs();
			$( "#tabs" ).tabs({
				beforeLoad: function( event, ui ) {
					ui.jqXHR.error(function() {
						alert('error');
					});
				}
			});
			
		});
	</script>
	
</head>
	
<body>
	<ul class="path">
		<li>工作流管理</li>
		<li>流程管理</li>
		<li>流程详情</li>
	</ul>
	
	<%@ include file="/pages/common/messages.jsp" %>
	
	<div id="tabs"> 
		<ul> 	
			<li><a href="#tabs-1">流程详情</a></li> 	
			<li><a href="<s:url value="/workflow/process/image.do?process.processInstanceId=%{process.processInstanceId}"/>">流程图</a></li>
		</ul>
		<div id="tabs-1" style="padding:5px;">
			<%@ include file="view_profile.jsp" %>
		</div>
	</div>
	
</body>
