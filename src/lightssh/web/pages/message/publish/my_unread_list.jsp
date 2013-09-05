<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/util/taglibs.jsp" %>

<head>
	<meta name="decorator" content="background"/>

	<title>任务列表</title>
	
	<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/ui/i18n/jquery.ui.datepicker_zh_CN.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$(".calendar").datepicker({dateFormat: 'yy-mm-dd',changeYear:true});
			
			$(".message").bind("click",function(evt){
				if(evt==null) 
					evt = window.event;
			    var target = (typeof evt.target != 'undefined')?evt.target:evt.srcElement;
			    
				$( $(target).parent().find(".content") ).slideToggle();
				
				if( $(target).parent().attr("class").indexOf('unread') )
					ajaxRead( $(target).parent().attr("publishid"),target );
			});
		});
		
		/**
		 * 检查名称是否重复
		 */
		function ajaxRead( id ,target){
			var result = false;
			$.ajax({
				url: "<s:url value="/message/publish/read.do"/>"
				,dataType: "json" 
				,type:"post"
				,async:false
				,data: {
		        	"publish.id": id
		        }
		        ,error: function(){ 
		        	$.lightssh.showActionError("阅读消息异常!"); 
		        }
		        ,success: function(json){
		        	result = json.result.success;
		        	if( result ){
		        		$(target).parent().find(".flag").text('已读');
		        		$(target).parent().removeClass("unread");
		        	}
		        }
			});
			
			return result;
		}
	</script>
	
	<style type="text/css">
		tr.unread{
			font-weight: bold;
			color: green;
		}
	</style>
</head>
	
<body>
	<ul class="path">
		<li>消息中心</li>
		<li>未读消息</li>
	</ul>
	
	<%@ include file="/pages/common/util/messages.jsp" %>
	
	<s:form name="mytodolist" method="post">
		<input type="submit" class="action search" value="查询"/>
		
		<table class="profile">
			<colgroup>
				<col width="10%"/>
				<col width="23%"/>
				<col width="10%"/>
				<col width="23%"/>
				<col width="10%"/>
				<col />
			</colgroup>
			<tbody>
				<tr>
					<th><label for="title">消息标题</label></th>
					<td><s:textfield id="title" name="publish.message.title" size="20" maxlength="100"/></td>
					
					<th><label for="startTime">接收时间</label></th>
					<td>
						<s:textfield name="publish.period.start" cssClass="calendar" size="10" /> -
						<s:textfield name="publish.period.end" cssClass="calendar" size="10"/>
					</td>
					
					<th><label for="owner">消息创建者</label></th>
					<td>
						<s:textfield name="task.procInstStartUser" size="20" />
					</td>
				</tr>
			</tbody>
		</table>
	</s:form>
	
	<table class="list">
		<colgroup>
			<col width="50px"/>
			<col />
			<col width="50px"/>
			<col width="80px"/>
			<col width="150px"/>
		</colgroup>
		<thead>
			<tr>
				<th>序号</th>
				<th>标题</th>
				<th>阅读标志</th>
				<th>创建者</th>
				<th>接收时间</th>
			<tr/>
		</thead>
		<tbody>
		<s:iterator value="page.list" status="loop">
			<s:set name="unread" value="readTime == null"/>
			<tr class="message <s:property value="#unread?'unread':'read'"/>" publishid="<s:property value="id"/>">
				<td><s:property value="#loop.index + 1"/></td>
				<td>
					<s:property value="message.title"/>
					<div class="content summary" style="display: none;float: none;text-align: left;margin: 2px 5px 2px 5px;">
						<div class="line">
							<div class="left"></div>
							<div class="right"></div>
						</div>
						<s:property value="message.content"/>
					</div>
				</td>
				<td class="flag"><s:property value="#unread?'未读':'已读'"/></td>
				<td><s:property value="message.creator"/></td>
				<td><s:property value="createdTime"/></td>
			</tr>
		</s:iterator>
		</tbody>
	</table>

	<mys:pagination />
</body>
