package com.google.code.lightssh.project.geo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.code.lightssh.common.entity.base.UUIDModel;

/**
 * GEO 关系
 * @author YangXiaojin
 *
 */
@Entity
@Table( name="T_GEO_ASSO")
public class GeoAssociation extends UUIDModel{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 地理边界(整体)
	 */
	@ManyToOne
	@JoinColumn( name="TO_GEO_ID")
	private GeographicBoundary toGeo;
	
	/**
	 * 地理边界(部分)
	 */
	@ManyToOne
	@JoinColumn( name="FROM_GEO_ID")
	private GeographicBoundary fromGeo;
	
	/**
	 * 序号
	 */
	@Column( name="SEQUENCE")
	private Integer sequence;

	public GeographicBoundary getToGeo() {
		return toGeo;
	}

	public void setToGeo(GeographicBoundary toGeo) {
		this.toGeo = toGeo;
	}

	public GeographicBoundary getFromGeo() {
		return fromGeo;
	}

	public void setFromGeo(GeographicBoundary fromGeo) {
		this.fromGeo = fromGeo;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
