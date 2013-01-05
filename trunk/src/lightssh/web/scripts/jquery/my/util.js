
jQuery.lightssh={
	/**
	 * 弹出菜单
	 */
	popupMenu:function(evt){
		if(evt==null) 
			evt = window.event;
	    var target = (typeof evt.target != 'undefined')?evt.target:evt.srcElement;
		//var target = evt.target || evt.srcElement;
		var popupMenu = $(target).children('div.popup-menu-layer')[0];
		if( popupMenu == null )
			popupMenu = $($(target).parent()).children('.popup-menu-layer')[0];
	
		$.each($('.popup-menu-layer'),function(index,item) { 
			if( item != popupMenu ){
				$( item ).hide();
			}
		});
	
		if( popupMenu == null )
			return;
		
		var posx = 0;
		var posy = 0;
		var e = evt;
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
	
	/**
	 * 删除提示消息
	 */
	,removeMessages:function( ){
		var eTarget = null;
		if( event != null )
			eTarget = event.currentTarget?event.currentTarget:event.srcElement;
		
		if( eTarget == null )
			return;
		
		$(eTarget).parent().hide('slow');
	}
	
	/**
	 * 显示提示信息
	 */
	,showActionError:function( msg ){
		if($("div.messages").length == 0 ){
			$('table').before("<div class='messages'></div>")
		}
		
		if( $('.messages > .error').length == 0 ){
			$('.messages').append("<div class='error'>"+msg+"</div>") 
		}else{
			$('.error').text( msg );
		}
	}
	
	/**
	 * 校验用户密码
	 */
	,checkPassword:function( opts,callback ){
		var dialog = 'dialog-check-password';
		var url = '/security/account/validatepassword.do';
		var title = '登录密码';
		if(opts != null ){
			if( opts.url != null )
				url = opts.url;
			if( opts.title != null )
				title = opts.title;
		}
		
		var password = '';
		var options = {
			title: '验证登录密码',
			resizable: false,
			width: 300,
			height: 160,
			modal: true,
			zIndex: 9999,
			buttons: {
				'确定': function() {
					password = $("#password").val();
					//$( this ).remove();
					$.lightssh.callbackCheckPassword(
							{'dialog':dialog,'result':true,'password':$.md5(password),'url':url},callback );
				},
				'取消': function() {
					$( this ).remove();
					$.lightssh.callbackCheckPassword({'result':false});
				}
			},
			close: function() {
				$( this ).remove();
				$.lightssh.callbackCheckPassword({'result':false});
			}
		};
		
		var el = "<div id='"+dialog+"' style='display:none;'>"
			+"<div class='error'></div><p><label>"+title+"：</label>"
			+"<input type='password' id='password' size='30'/>"
			+"</p></div>";
		$( 'body' ).append( el );

		$( 'div#dialog-check-password' ).dialog( options );
	}

	,callbackCheckPassword:function( opts ,callback){
		var result = false;
		if( opts.result && opts.url != null ){
			var param =  {'password':opts.password,'rand':Math.random()};
			$.post(opts.url,param,function(json){
				result = json.passed;
				if( !result ){
					$('.error').html('密码错误！');
					//alert('密码错误！');
					$('.error').animate({
	                    backgroundColor: "#FCF8E3",
	                    color: "#F30"
	                }, 3000 );
				}else{
					$('#'+opts.dialog ).remove();
				}
				callback( result );
			});
		}

		return result;
	}
};