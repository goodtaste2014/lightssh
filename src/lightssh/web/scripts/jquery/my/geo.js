
/**
	用法：
	initGeo({'geo_parent_url':'/settings/geo/listcountry.do',
	'geo_children_url':'/settings/geo/listchildren.do',
	'geo_active':'true',
	'geo_selectors':[
		{'name':'xxx.country.code','value':'xxx'},
		{'name':'xxx.xxx.code','value':'xxx'},
		{'name':'xxx.xxx.code','value':'xxx'},
		{'name':'xxx.xxx.code','value':'xxx'}]
	});
 */


	/**
	 * 初始化地理区域
	 */
	function initGeo( param ){
		var selectors = param['geo_selectors'];
		for( var i=0;i<selectors.length;i++){
			showGeo( param,i+1,i==0?null:selectors[i-1].value );
			$("select[name='"+selectors[i].name+"']").val( selectors[i].value );
		}
	}

	/**
	 * 显示地理区域
	 */
	function showGeo( param,level,geo_code ){
		if( param == null )
			return;

		//GEO数据获取URL地址
		var geo_url = param['geo_parent_url'];
		if( level != null && level > 1 )
			geo_url = param['geo_children_url'];

		//显示级别
		level = (level==null||level=='')?1:level;

		var selectors = param['geo_selectors'];
		removeChildren(selectors,level);
		
		//当前下拉元素名
		var curr_sel_name = selectors[level-1].name;
		if( curr_sel_name == null || curr_sel_name == '' ){
			return ;
		}
				
		//当前下拉元素
		var geo_select = $("select[name='"+curr_sel_name+"']");
		if( geo_select.length == 0 && ( level-2 >= 0) ){ //
			var parent_geo = $("select[name='"+selectors[level-2].name+"']");
			geo_select = $("<select name='"+curr_sel_name+"'></select>");
			$(parent_geo).after( geo_select );
		}

		if( (geo_code == null || geo_code == '')&& event != null ){
			var eTarget = event.currentTarget?event.currentTarget:event.srcElement;
			geo_code = $(eTarget).val();
		}
		
		$.ajax({
			url: geo_url
			,dataType:"json",type:"post",async:false
			,data:{'geo.active':param['geo_active'],'geo.code':geo_code }
	        ,error:function(){  }
	        ,success: function(json){
	        	buildGeoSelect( json.list,geo_select,param,level );
	        }
		});
	}

	/**
	 * 构造显示元素
	 */
	function buildGeoSelect( list,geo_select,param,level ){
		if( list == null ){
			$(geo_select).remove();
			return;
		}
		
		$(geo_select).empty();
		$(geo_select).append( "<option value=''></option>" );
		$.each(list,function(index,item){
				var opt = "<option value='"+item.code+"'>"+item.name+"</option>";
				$(geo_select).append( opt );
			});
		$( geo_select ).change(function(){showGeo(param,level+1);});
	}

	/**
	 * 删除孩子节点
	 */
	function removeChildren( selectors,level ){
		for( var i=level;i<selectors.length;i++ ){
			$("select[name='"+selectors[i].name+"']").remove();
		}
	}