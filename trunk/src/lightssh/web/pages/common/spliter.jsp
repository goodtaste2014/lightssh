<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/pages/common/taglibs.jsp" %>
<html>
	<head>
		<title>index page</title>
		<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/styles/<mys:theme />/theme.css" />
		
		<style type="text/css"><!--
			a.spliter{
				text-decoration: none;
				font-size: 30px;
				color: black;
				text-align: center;
				margin-left:1px;
			}
			
			a.spliter:hover {
				color: red;
			}
		</style>
		
		<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery/jquery.min.js"></script>
		
		<script type="text/javascript">
			var MENU_DISPLAY_VALUE = '0';
			function toggleMenu(){
				var version = '';
				$.each($.browser, function(i, val) {
				      if( i == 'version')
				    	  version = val;
				})
				
				var main_frameset = $('#main_frameset',window.parent.document);
				var cols_arr =  $(main_frameset).attr('cols').split(',');
				var disply_cols = '';
				for( var i=0;i<cols_arr.length;i++ ){
					if( i== 0 ){
						if( cols_arr[0] != "0")
							MENU_DISPLAY_VALUE = cols_arr[0];
						disply_cols += ( cols_arr[0] == "0"?MENU_DISPLAY_VALUE:"0");
						
						$('a.spliter').html( cols_arr[0] == "0"?'&laquo;':'&raquo;');
					}else{ disply_cols += ","+cols_arr[i] }
				}
				
				$(main_frameset).attr('cols',disply_cols );
				
			}
		</script>
	</head>
	
	<body id="spliter" style="background-color:#FDF9FD;">
		<a href="#" class="spliter" onclick="javascript:toggleMenu();">&laquo;</a>
	</body>
</html>