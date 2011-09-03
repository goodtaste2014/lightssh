<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<s:form id="profile_form" action="save" namespace="/party/person" method="post">
	<table class="profile">
		<tbody>
			<input type="hidden" name="party" value="person"/>
			<s:hidden name="party.id"/>
			<%-- 
			<tr>
				<th><label for="lastName" class="required">姓</label></th>
				<td>
					<s:set name="isInsert" value="%{(person==null||person.id==null)}"/>
					
					<s:textfield id="lastName" name="person.lastName" size="20"/>
				</td>
			</tr>
			
			<tr>
				<th><label for="name" class="required">名字</label></th>
				<td>
					<s:textfield id="firstName" name="person.firstName" size="20"/>
				</td>
			</tr>
			--%>
			
			<tr>
				<th><label for="name" class="required">姓名</label></th>
				<td>
					<s:textfield id="name" name="party.name" size="20"/>
				</td>
			</tr>
			
			<tr>
				<th><label for="gendar">性别</label></th>
				<td>
					<s:select name="party.gender" value="%{party.gender.name()}" listKey="name()" 
						list="@com.google.code.lightssh.project.party.entity.Person$Gender@values()"></s:select>
				</td>
			</tr>
			
			<tr>
				<th><label for="enabled">是否有效</label></th>
				<td>
					<s:select name="party.enabled" list="#{true:'是',false:'否'}"></s:select>
				</td>
			</tr>
			
			<tr>
				<th><label for="credentialsType">证件类型</label></th>
				<td>
					<s:select list="@com.google.code.lightssh.project.party.entity.CredentialsType@values()" 
						name="party.credentialsType" listKey="name()" headerKey="" headerValue=""
						value="party.credentialsType==null?party.credentialsType.name()"/>
				</td>
			</tr>
			
			<tr>
				<th><label for="identityCardNumber">证件号码</label></th>
				<td>
					<s:textfield name="party.identityCardNumber" size="30"/>
				</td>
			</tr>
			
			<tr>
				<th><label for="birthday">生日</label></th>
				<td>
					<s:textfield name="party.birthday" id="person_birthday" size="10"/>
				</td>
			</tr>
			
			<tr>
				<th><label for="degree">最高学历</label></th>
				<td>
					<s:select list="@com.google.code.lightssh.project.party.entity.Person$EducationLevel@values()" 
						name="party.degree" listKey="name()" headerKey="" headerValue=""
						value="party.degree.name()"/>
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
		<s:set name="isInsert" value="%{(party==null||party.id==null)}"/>
		<input type="submit" class="action save" name="Submit" 
			value="<s:property value="%{#isInsert?\"新增会员\":\"修改会员\"}"/>"/>
	</p>
</s:form>