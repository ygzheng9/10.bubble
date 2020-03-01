package com.metis.bubble.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRegionSalesStats<M extends BaseRegionSalesStats<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setYear(java.lang.String year) {
		set("year", year);
	}
	
	public java.lang.String getYear() {
		return getStr("year");
	}

	public void setRegion(java.lang.String region) {
		set("region", region);
	}
	
	public java.lang.String getRegion() {
		return getStr("region");
	}

	public void setQuantity1(java.math.BigDecimal quantity1) {
		set("quantity1", quantity1);
	}
	
	public java.math.BigDecimal getQuantity1() {
		return get("quantity1");
	}

	public void setQuantity2(java.math.BigDecimal quantity2) {
		set("quantity2", quantity2);
	}
	
	public java.math.BigDecimal getQuantity2() {
		return get("quantity2");
	}

	public void setQuantity3(java.math.BigDecimal quantity3) {
		set("quantity3", quantity3);
	}
	
	public java.math.BigDecimal getQuantity3() {
		return get("quantity3");
	}

	public void setBatch(java.lang.String batch) {
		set("batch", batch);
	}
	
	public java.lang.String getBatch() {
		return getStr("batch");
	}

}
