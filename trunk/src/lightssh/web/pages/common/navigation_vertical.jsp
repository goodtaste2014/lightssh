<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<html>
	<head>
		<title>index page</title>
		<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/styles/<mys:theme />/theme.css" />
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.cookie.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/my/layout/vertical_menu.js"></script>
		<style type="text/css">
			body{
				padding:0 4px 0 2px;
				background-color: #FCFFEF;
			}
			
			#navigation{
			}
			
			.toggle{
				text-decoration: none;
				font-size: 24px;
				color: black;
				text-align: right;
				vertical-align:middle;
				margin-left:1px;
				background-color: #F7F7F7;
			}
			
			.toggle a{
				line-height:24px;
			}
			
			.toggle:hover {
				color: red;
			}
			
			ul#vertical_navigation{
				margin:0;/* fixed IE bug*/
				padding:0;/* fixed IE bug*/
			}
			
			ul#vertical_navigation,ul#vertical_navigation li a{
				width:100%;
			}
			
		</style>
		
		<script>
			/**
			 * init vertical menu
			 */
			$(document).ready(function(){
				initVerticalMenu( "vertical_navigation" );
			})
			
			var MIN_MENU_COL = "20px";
			var MAX_MENU_COL = "20%";
			function toggleMenu(){
				var main_frameset = $('#main_frameset',window.parent.document);
				var cols_arr =  $(main_frameset).attr('cols').split(',');
				
				if( cols_arr[0] != MIN_MENU_COL && cols_arr[0] != MAX_MENU_COL)
					cols_arr[0] = MAX_MENU_COL;
					
				var isHidden = (cols_arr[0]==MAX_MENU_COL);
				var disply_cols = isHidden?MIN_MENU_COL:MAX_MENU_COL;
				$('div.toggle>a').html( !isHidden?'&laquo;':'&raquo;');
				
				disply_cols += ",*";
				//alert( disply_cols );
				$("#vertical_navigation").css("display",isHidden?"none":"block");
				$(main_frameset).attr('cols',disply_cols );
			}
		</script>
	</head>
	
	<body>
	<div id="navigation">
		<div class="toggle">
			<a href="#" onclick="javascript:toggleMenu();">&laquo;</a>
		</div>
		
		<ul id="vertical_navigation" class="menu vertical-menu">
			<li>
				<%-- 系统管理--%>
				<a href="#"><s:text name="project.nav.sysmgr"/></a>
				<ul id="system_mgr">
					<li>
						<%-- 登录账号--%>
						<a href="#"><s:text name="project.nav.loginaccount"/></a>
						<ul id="login_account">
							<%-- 修改密码--%>
							<li><a href="<s:url value="/security/account/edit.do?password=update"/>" target="main_frame"><s:text name="project.nav.changepassword"/></a></li>
							<%-- 新增用户--%>
							<li><a href="<s:url value="/security/account/edit.do"/>" target="main_frame"><s:text name="project.nav.newaccount"/></a></li>
							<%-- 用户列表--%>
							<li><a href="<s:url value="/security/account/list.do"/>" target="main_frame"><s:text name="project.nav.listaccount"/></a></li>
						</ul>
					</li>
					
					<li>
						<%-- 角色管理--%>
						<a href="#"><s:text name="project.nav.rolemgr"/></a>
						<ul id="security">
							<%-- 新增角色--%>
							<li><a href="<s:url value="/security/role/edit.do"/>" target="main_frame"><s:text name="project.nav.newrole"/></a></li>
							<%-- 角色列表--%>
							<li><a href="<s:url value="/security/role/list.do"/>" target="main_frame"><s:text name="project.nav.listrole"/></a></li>
						</ul>
					</li>
					
				</ul>
			</li>
			
			<li>
				<%-- 人员机构管理--%>
				<a href="#"><s:text name="project.nav.partymgr"/></a>
				<ul id="party_mgr">
					<li>
						<%-- 组织机构管理--%>
						<a href="#"><s:text name="project.nav.org"/></a>
						<ul id="org_mgr">
							<li><a href="<s:url value="/party/organization/edit.do"/>" target="main_frame"><s:text name="project.nav.neworg"/></a></li>
							<%-- 组织机构列表 --%>
							<li><a href="<s:url value="/party/organization/list.do"/>" target="main_frame"><s:text name="project.nav.listorg"/></a></li>
						</ul>
					</li>
					<li>
						<%-- 人员管理--%>
						<a href="#"><s:text name="project.nav.person"/></a>
						<ul id="member_mgr">
							<%-- 新增人员--%>
							<li><a href="<s:url value="/party/person/edit.do"/>" target="main_frame"><s:text name="project.nav.newperson"/></a></li>
							<%-- 人员列表--%>
							<li><a href="<s:url value="/party/person/list.do"/>" target="main_frame"><s:text name="project.nav.listperson"/></a></li>
						</ul>
					</li>
				</ul>
			</li>
			
			<li>
				<%-- 基础管理--%>
				<a href="#"><s:text name="project.nav.settings"/></a>
				<ul id="settings_mgr">
					<li><a href="<s:url value="/settings/organization/viewparent.do"/>" target="main_frame">企业资料</a></li>
					<%-- 系统日志--%>
					<li><a href="<s:url value="/settings/log/list.do"/>" target="main_frame"><s:text name="project.nav.syslog"/></a></li>
					<%-- 定时任务 --%>
					<li><a href="<s:url value="/settings/scheduler/list.do"/>" target="main_frame">定时任务</a></li>
					<%-- 计量单位 --%>
					<li><a href="<s:url value="/settings/uom/list.do"/>" target="main_frame">计量单位</a></li>
				</ul>
			</li>
		</ul>
		
		<div class="spliter">
		</div>
	</div>
	</body>
</html>