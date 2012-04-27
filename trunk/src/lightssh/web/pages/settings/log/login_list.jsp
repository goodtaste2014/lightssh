<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<meta name="decorator" content="background"/>
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/ui/i18n/jquery.ui.datepicker_zh_CN.js"></script>
	
		<title>用户登录日志</title>
		
		<script type="text/javascript">
			$(document).ready(function(){
				$("#access_start_date").datepicker({dateFormat: 'yy-mm-dd',changeYear:true});
				$("#access_end_date").datepicker({dateFormat: 'yy-mm-dd',changeYear:true});
			});

			/**
			 * 查看会员详情
			 */
			function popupView( name ){
				var url = '<s:url value="/security/account/view.do"/>?account.loginName=' + name;
				var popup = $("#account_popup");
				//$( popup ).html( '<div><img id=\'loading\' src=\'<%= request.getContextPath() %>/images/loading.gif\'/>' );
				$( popup ).dialog({
					resizable:false,modal: true,height:400,width: 700,
					close: function(event, ui) {$(this).dialog('destroy').html(''); },				
					buttons: {				
						"关闭": function() {$(this).dialog('destroy').html('');}
					}
				});
				
				$.post(url,{},function(data){$( popup ).html( data );});
			}
		</script>
		
	</head>
	
	<body>
	<ul class="path">
		<li>系统管理</li>
		<li>系统日志</li>
		<li>用户登录日志</li>
	</ul>
 		
		<%@ include file="/pages/common/messages.jsp" %>
		
		<s:form name="loginlist" namespace="/settings/log" method="post">
			<table class="profile">
				<tbody>
					<tr>
						<td><label for="time">时间</label></td>
						<td>
							<s:textfield name="loginLog._period.start" id="access_start_date" size="10" /> -
							<s:textfield name="loginLog._period.end" id="access_end_date" size="10"/>
						</td>
						
						<td><label for="ip">IP</label></td>
						<td><s:textfield id="ip" name="loginLog.ip"  size="10"/></td>
						
						<td><label for="operator">登录用户</label></td>
						<td><s:textfield id="operator" name="loginLog.operator"  size="10"/></td>
						
						<td colspan="2"><input type="submit" class="action search" value="查询"/></td>
					</tr>
				</tbody>
			</table>
		</s:form>
	
		<table class="list">
			<colgroup>
				<col class="element" width="50px"/>
				<col class="element" width="200px"/>
				<col class="element" width="150px"/>
				<col class="element" width="200px"/>
				<col class="element" />
			</colgroup>
			<thead>
				<tr>
					<th>序号</th>
					<th>时间</th>
					<th>IP</th>
					<th>登录用户</th>
					<th>描述</th>
				</tr>
			</thead>
			
			<s:iterator value="page.list" status="loop">
			<tr class="<s:property value="#loop.odd?\"odd\":\"even\""/>">
				<td><s:property value="#loop.index+1"/></td>
				<td><s:property value="%{createdTime}"/></td>
				<td><s:property value="%{ip}"/></td>
				<td>
					<a href="#" onclick="javascript:popupView('<s:property value="%{operator}"/>')">
						<s:property value="%{operator}"/>
					</a>
				</td>
				<td><s:property value="%{description}"/></td>
			</tr>
			</s:iterator>
			</table>
			
			<s:set name="pagination" value="%{page}"/>
			<jsp:include page="/pages/common/pagination.jsp"/>
			
			<div id="account_popup" title="登录账户信息" style="display: none;">&nbsp;</div>
	</body>
</html>