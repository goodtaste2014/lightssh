<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<table class="profile">
	<tbody>
		<input type="hidden" name="party" value="person"/>
		<s:hidden name="party.id"/>
	
		<tr>
			<th><label for="name" class="required">姓名</label></th>
			<td>
				<s:textfield id="name" name="party.name" size="20"/>
			</td>
		</tr>
		
		<tr>
			<th><label for="desc">描述</label></th>
			<td><s:textarea id="desc" name="party.description" cols="60" rows="5"/></td>
		</tr>
	</tbody>
</table>