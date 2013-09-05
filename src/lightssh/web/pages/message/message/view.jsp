<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/util/taglibs.jsp" %>
	
<head>
	<meta name="decorator" content="background"/>
	<title>查看消息</title>
</head>

<body>
	<ul class="path">
		<li>基础管理</li>
		<li>消息管理</li>
		<li>查看消息</li>
	</ul>
		
	<%@ include file="/pages/common/util/messages.jsp" %>
	
	<table class="profile">
		<colgroup>
			<col width="15%"/>
			<col width="30%"/>
			<col width="15%"/>
			<col />
		</colgroup>
		<tbody>
			<tr>
				<th>标题</th>
				<td colspan="3"><s:property value="%{message.title}"/></td>
			</tr>
			<tr>
				<th>内容</th>
				<td colspan="3">
					<pre><s:property value="%{message.content}"/></pre>
				</td>
			</tr>
			<tr>
				<th>优先级</th>
				<td><s:property value="%{message.priority}"/></td>
				<th>消息类型</th>
				<td><s:property value="%{message.catalog.type}"/></td>
			</tr>
			
			<tr>
				<th>是否可链接</th>
				<td><s:property value="%{message.linkable?'是':'否'}"/></td>
				<th>链接地址</th>
				<td><s:property value="%{message.catalog.type}"/></td>
			</tr>
			<tr>
				<th>是否可转发</th>
				<td><s:property value="%{message.forward?'是':'否'}"/></td>
				<th>发布条数</th>
				<td><s:property value="%{message.publishedCount}"/></td>
			</tr>
			<tr>
				<th>点击次数</th>
				<td><s:property value="%{message.hitCount}"/></td>
				<th>阅读者数</th>
				<td><s:property value="%{message.readerCount}"/></td>
			</tr>
			<tr>
				<th>计划清理标记</th>
				<td><s:property value="%{message.todoClean?'是':'否'}"/></td>
				<th>计划清理时间</th>
				<td><s:property value="%{message.todoCleanTime}"/></td>
			</tr>
			<tr>
				<th>创建人</th>
				<td><s:property value="%{message.creator}"/></td>
				<th>创建时间</th>
				<td><s:property value="%{message.createdTime}"/></td>
			</tr>
			
		</tbody>
	</table>
</body>