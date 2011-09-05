package com.google.code.lightssh.project.geo.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.geo.entity.GeographicBoundary;
import com.google.code.lightssh.project.geo.service.GeographicBoundaryManager;

/**
 * 
 * @author YangXiaojin
 *
 */
@Component("geoAction")
@Scope("prototype")
public class GeographicBoundaryAction extends CrudAction<GeographicBoundary>{

	private static final long serialVersionUID = 8080328735431672890L;
	
	private GeographicBoundary geo;
	
	@Resource(name="geoManager")
	public void setGeoManager( GeographicBoundaryManager geoManager ){
		super.manager = geoManager;
	}
	
	public GeographicBoundaryManager getManager(){
		return (GeographicBoundaryManager)super.manager;
	}

	public GeographicBoundary getGeo() {
		geo = super.model;
		return geo;
	}

	public void setGeo(GeographicBoundary geo) {
		this.geo = geo;
		super.model = this.geo;
	}
	
	public String toggle( ){
		try{
			getManager().toggleActive(geo);
		}catch( Exception e ){
			this.saveErrorMessage(e.getMessage());
		}
		
		return SUCCESS;
	}
}
