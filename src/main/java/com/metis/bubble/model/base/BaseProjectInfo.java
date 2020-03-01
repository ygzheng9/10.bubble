package com.metis.bubble.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseProjectInfo<M extends BaseProjectInfo<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setProject(java.lang.String project) {
		set("project", project);
	}
	
	public java.lang.String getProject() {
		return getStr("project");
	}

	public void setClient(java.lang.String client) {
		set("client", client);
	}
	
	public java.lang.String getClient() {
		return getStr("client");
	}

	public void setPlant(java.lang.String plant) {
		set("plant", plant);
	}
	
	public java.lang.String getPlant() {
		return getStr("plant");
	}

	public void setCity(java.lang.String city) {
		set("city", city);
	}
	
	public java.lang.String getCity() {
		return getStr("city");
	}

	public void setBomCount(java.lang.Integer bomCount) {
		set("bomCount", bomCount);
	}
	
	public java.lang.Integer getBomCount() {
		return getInt("bomCount");
	}

	public void setPartCount(java.lang.Integer partCount) {
		set("partCount", partCount);
	}
	
	public java.lang.Integer getPartCount() {
		return getInt("partCount");
	}

	public void setReusePartCount(java.lang.Integer reusePartCount) {
		set("reusePartCount", reusePartCount);
	}
	
	public java.lang.Integer getReusePartCount() {
		return getInt("reusePartCount");
	}

	public void setReuseRate(java.math.BigDecimal reuseRate) {
		set("reuseRate", reuseRate);
	}
	
	public java.math.BigDecimal getReuseRate() {
		return get("reuseRate");
	}

}
