package com.google.code.lightssh.project.webservice;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

/**
 * 文件上传 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class FileUpload {
	@XmlAttribute
	private String title;

	@XmlElement
	@XmlMimeType("application/octet-stream")
	private DataHandler data;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DataHandler getData() {
		return data;
	}

	public void setData(DataHandler data) {
		this.data = data;
	}
	
}
