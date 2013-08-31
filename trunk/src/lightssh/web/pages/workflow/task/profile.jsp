<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/util/taglibs.jsp" %>
	
	
	<script type="text/javascript">
		
		$(document).ready(function(){
			/**
			 * 数据校验
			 */
			$("#profile_form").validate({
				rules:{
					"task.message":{required:true,maxlength:800}
				},
				messages:{
					"account.loginName": {}
				}
			});
			
		});
		
		/**
		 * 完成任务
		 */
		function doTask(type){
			//$("#task_id").val(taskId);
			$("#task_type").val(type);
			//$("#task_message").val(msg);
			
			$("#profile_form").submit();
		}
		
		/**
		 * 提交
		 */
		function doTaskSubmit(){
			doTask('SUBMIT');
		}
		
		/**
		 * 退回
		 */
		function doTaskRevoke(){
			doTask('REVOKE');
		}
	</script>
	
	<s:action name="viewproc" namespace="/workflow/process" executeResult="true">
		<s:param name="process.processInstanceId" value="#request.procInstId"></s:param>
	</s:action>
	
	<s:form id="profile_form" action="complete" namespace="/workflow/task" method="post">
		<table class="profile">
			<caption>流程操作</caption>
			<tbody>
				<tr>
					<th><label for="desc">流转意见</label></th>
					<td>
						<s:hidden id="task_id" name="task.id" value="%{taskId==null?task.id:taskId}"/>
						<s:hidden id="task_type" name="task.type" value="SUBMIT"/>
						<s:textarea id="task_message" theme="simple" name="task.message" cols="80" rows="5"/>
					</td>
				</tr>
			</tbody>
		</table>
	
		<s:token />
		
		<p class="submit">
			<input type="button" class="action save" value="提交流程" onclick="doTaskSubmit()" />
			<input type="button" class="action save" value="退回流程" onclick="doTaskRevoke()" />
		</p>
	</s:form>
	
