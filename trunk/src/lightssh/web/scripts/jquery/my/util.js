
jQuery.lightssh={
	/**
	 * 弹出菜单
	 */
	popupMenu:function(){
		var popupMenu = $(event.srcElement).children('.popup-menu-layer')[0];
		if( popupMenu == null )
			popupMenu = $($(event.srcElement).parent()).children('.popup-menu-layer')[0];
	
		$.each($('.popup-menu-layer'),function(index,item) { 
			if( item != popupMenu ){
				$( item ).hide();
			}
		});
	
		if( popupMenu == null )
			return;
		
		var posx = 0;
		var posy = 0;
		if(!e) var e = window.event;
		if(e.pageX || e.pageY) {
			posx = e.pageX;
			posy = e.pageY;
		}else if(e.clientX || e.clientY) {
			posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
			posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
		}
		var left = posx - $(popupMenu).width();
		var top = posy;
		$( popupMenu ).css("left",left + "px");
		$( popupMenu ).css("top",top + "px");
		$( popupMenu ).show();
	
		//$("td").removeClass("action");
		//$(event).addClass("action");//事件目标
		
		$("tr").removeClass("focused");
		$(popupMenu).parent().parent().addClass("focused");//事件目标父结点
	}

	 /**
	  * 显示域错误信息
	  */
	,showFieldError:function(ele,val){
		$( ele ).focus();
		$( ele ).after( $("<label name='error' class='error'>"+val+"</label>") );
	}
};