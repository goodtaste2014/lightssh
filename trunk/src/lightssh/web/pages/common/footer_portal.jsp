<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/pages/common/taglibs.jsp" %>

<div id="footer">
	<p>
		<a href="">关于我们</a>
		|
		<a href="">联系我们</a>
		|
		<a href="">法律声明</a>
		|
		<a href="">网站地图 </a>
		|
		<a href="">ICP备XXX-XXX</a>
		<br/>
		<s:set name="year" value="%{@com.ylink.common.util.TextFormater@format(new java.util.Date(),'yyyy')}"/>
		&copy;<s:property value="%{(#year!=\"2011\")?(\"2011-\"+#year):\"2011\"}"/> 重庆金融资产交易所  
		电话：(028)xxxxxxx 传真：(028)xxxxxxx 
		<br/>
		技术支持：深圳市雁联计算系统有限公司
	</p>
</div>