<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
	
<head>
	<meta name="decorator" content="background"/>
	<title>查看人员</title>
</head>

<body>
	<ul class="path">
		<li>组织机构管理</li>
		<li>人员管理</li>
		<li>查看人员</li>
	</ul>
		
	<%@ include file="/pages/common/messages.jsp" %>
	<table class="profile">
		<colgroup>
			<col width="15%"/>
			<col width="30%"/>
			<col width="15%"/>
			<col />
		</colgroup>
		<tbody>
			<tr>
				<th>姓名</th>
				<td><s:property value="%{party.name}"/></td>
				<th rowspan="7">照片</th>
				<td rowspan="7"></td>
			</tr>
				 	 		 	  	 
			<tr>
				<th>性别</th>
				<td><s:property value="%{party.gender}"/></td>
			</tr>
			
			<tr>
				<th>出生日期</th>
				<td><s:property value="%{party.birthday}"/></td>
			</tr>
			
			<tr>
				<th>籍贯</th>
				<td>
					<s:property value="%{party.country.name}"/>
					<s:property value="%{party.secondaryGeo.name}"/>
					<s:property value="%{party.thirdGeo.name}"/>
					<s:property value="%{party.fourthGeo.name}"/>
				</td>
			</tr>
			
			<tr>
				<th>民族</th>
				<td><s:property value="%{party.ethnicGroup}"/></td>
			</tr>
			
			<tr>
				<th>政治面貌</th>
				<td><s:property value="%{party.partyAffiliation}"/></td>
			</tr>
			
			<tr>
				<th>婚况</th>
				<td><s:property value="%{party.maritalStatus}"/></td>
			</tr>
			
			<tr>
				<th>最高学历</th>
				<td><s:property value="%{party.degree}"/></td>
				<th>证件(<s:property value="%{party.credentialsType}"/>)</th>
				<td><s:property value="%{party.identityCardNumber}"/></td>
			</tr>
			<tr>
				<th>描述</th>
				<td colspan="3"><s:property value="%{party.description}"/></td>
			</tr>
		</tbody>
	</table>
</body>