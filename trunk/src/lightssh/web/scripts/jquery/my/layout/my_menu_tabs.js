(function($){
	
	$.fn.mymenutabs = function( options ) {
	    var opts = $.extend( {}, $.fn.mymenutabs.defaults, options );
	 
	    var windowHandler = null; //窗口操作句柄
	 	var ultabs = this;
	 	$(ultabs).find("li.tab a").bind('click',function(){
			$(ultabs).find("li.tab").removeClass("active");
			$(this).parent().addClass("active");
			
			opts.activeTab();
		});
	 	
	 	try{
	 		windowHandler = top.frames[ opts.window_frame ][ opts.multi_window_handler ];
	 	}catch(exp){
	 		//alert(exp)
	 	}
					
		//关闭按钮
		$(ultabs).find("li.tab span").bind('click',function(){
			$(this).parent().remove();
			
			opts.closeTab();
		});
		
		
		return new MyMenuTabs( $(this),opts ); //实例化
	};
	
	 /**
     * 构造函数
     */
    function MyMenuTabs(obj,settings){
        this._obj = obj;
        //this._color = settings.color;
    }
	
	/** 
	 * 添加菜单 
	 */
    MyMenuTabs.prototype.addTab = function ( opts ){
		alert( opts.title );
		return this;
	}
	 
	$.fn.mymenutabs.defaults = {
		multi_window_handler:"myMultiWindows"
		,window_frame:"main_frame"
		,closeTab:function(){}
		,activeTab:function(){}
	    ,max_tab_count: 5
	    ,can_refresh:true
	};

})(jQuery);