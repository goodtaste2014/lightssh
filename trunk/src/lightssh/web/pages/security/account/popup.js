
var popup_login_account = $("<div id='popup_party' title='登录帐户' style='display: none;'></div>");

//$(popup).html('') fixed IE bug
function popupLoginAccount( url ,param){
	var popup = $( popup_login_account );
	//$( popup ).html( '<div><img id=\'loading\' src=\'<%= request.getContextPath() %>/images/loading.gif\'/>' );
	$( popup ).dialog({
		resizable: true,modal: true,height:500,width: 700,
		close: function(event, ui) {$(this).dialog('destroy'); },				
		buttons: {				
			"关闭": function() {$(this).dialog('destroy');}
		}
	});
	
	$.post(url,param,function(data){$( popup ).html( data );});
}

/**
 * 弹出框回调
 */
function callbackSelectLoginAccount( param ){
	$( popup_login_account ).dialog('destroy').html('');
}