<%@ page import="java.util.Map" %>
<%@ page import="java.util.Enumeration" %>
<%@ include file="/pages/common/taglibs.jsp" %>
<%@ page pageEncoding="UTF-8" %>
	
	<s:if test="#PAGE_NUMBER_NAME == null">
		<s:set name="PAGE_NUMBER_NAME" value="\"page.number\""/>
	</s:if>
    
    <!-- query string -->    
    <s:url var="root_url"/> 
    <s:set name="queryString" value="%{root_url}" />
    <s:if test="#REPORT_URL != null">
		<s:set name="queryString" value="#REPORT_URL"/>
	</s:if>
    <s:set name="queryString" value="#queryString + '?'" />
    <s:set name="over_write_params" value="#request.over_write"/>
    <s:set name="first" value="true"/>
	<s:iterator value="#parameters" status="loop">
		<s:if test="key != #PAGE_NUMBER_NAME && key != \"_view_theme\"">
			<s:set name="paramValue" value="value[0]"/>	
			<s:if test="#over_write_params[key] != null">
				<s:set name="paramValue" value="%{#over_write_params[key]}"/>
			</s:if>			
			<s:set name="queryString" value="#queryString + (#first?\"\":\"&amp;\") + key + \"=\" + #paramValue "/>
			<s:set name="first" value="false"/>	  
		</s:if>  
	</s:iterator>
	
	<s:set name="queryString" value="#queryString + \"&amp;\" + #PAGE_NUMBER_NAME + \"=\" + #pagination.number"/>
	
	<div class="export">
		<%-- 
		<select>
			<option value="current">当前页</option>
			<option value="all">全部</option>
		</select>
		<a class="pdf" href="<s:property value="#queryString+ '&amp;'" escape="false"/>type=PDF">PDF</a>
		<a class="xls" href="<s:property value="#queryString+ '&amp;'" escape="false"/>type=XLS">XLS</a>
		<a class="csv" href="<s:property value="#queryString+ '&amp;'" escape="false"/>type=CSV">CSV</a>
		<a class="word" href="<s:property value="#queryString+ '&amp;'" escape="false"/>type=RTF">RTF</a>
		--%>  
		
		<input type="button" class="action pdf" value="PDF" onclick="location.href='<s:property value="#queryString+ '&amp;'" escape="false"/>type=PDF'"/>
		<input type="button" class="action xls" value="EXCEL" onclick="location.href='<s:property value="#queryString+ '&amp;'" escape="false"/>type=XLS'"/>
		<input type="button" class="action csv" value="CSV" onclick="location.href='<s:property value="#queryString+ '&amp;'" escape="false"/>type=CSV'"/>
		<input type="button" class="action rtf" value="WORD" onclick="location.href='<s:property value="#queryString+ '&amp;'" escape="false"/>type=RTF'"/>
		
	</div>
	
	<sc