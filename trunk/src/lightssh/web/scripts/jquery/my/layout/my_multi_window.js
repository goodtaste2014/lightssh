
/**
 * 多窗口
 */
$.fn.mymultiwindows = function( options ) {
    var opts = $.extend( {}, $.fn.mymultiwindows.defaults, options );
    
    var menuHandler = null;//变量共享访问
    
    this.bind('click',function(){
		var linkUrl = $(this).attr('href');
		var iframeId = linkUrl;
		if( iframeId.indexOf('?') > 0 )
			iframeId = iframeId.substring(0,iframeId.indexOf('?'));
		
		//菜单选项
    	try{
    		menuHandler = top.frames[ opts.menu_frame ][ opts.menu_tab_handler ];
    		menuHandler.addTab({"id":iframeId,"title":$(this).text(),"href":linkUrl}) 
     	}catch(exp){
     	}
			
		var mainBody = $(top.frames[ opts.window_frame ].window.document.body);
		
		var currIframe = $(mainBody).find("iframe."+opts.iframe_class+"[id='"+iframeId+"']");
		
		$(mainBody).find("iframe."+opts.iframe_class).hide();
		if( $(currIframe).length == 0){ //不存在
			currIframe = $("<iframe class='"+opts.iframe_class+"' id='"+iframeId +"'"
					+" src='"+linkUrl+"' "
					+" width='100%' height='100%' "
					+" marginwidth='0' marginheight='0' frameborder='0' framespacing='0' border='0' />");
			$(mainBody).append( currIframe );
		}
		$(currIframe).show(); //显示当前iframe
		
		opts.link();
		
		return false;
	});
}

$.fn.mymultiwindows.defaults = {
	menu_tab_handler:"myMenuTabs"
	,window_frame:"main_frame"
	,menu_frame:"head_frame"
	,iframe_class:"tab"
	,link:function(){}
};