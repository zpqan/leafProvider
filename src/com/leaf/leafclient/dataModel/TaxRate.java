package com.leaf.leafclient.dataModel;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.leaf.client.util.DataModelUtils;

public class TaxRate {
	private static final String NAME = "name";
	private static final String RATE = "rate";
	private JSONObject jsonObject;
	
	public TaxRate() {
		this.jsonObject = new JSONObject();
	}

	public TaxRate(JSONObject jsonObj) {
		this.jsonObject = jsonObj;
	}
	
	public JSONObject getJsonObject() {
		return this.jsonObject;
	}
	public String getName() {
		return DataModelUtils.getStringFromJson(jsonObject, NAME);
	}

	public void setName(String name) {
		DataModelUtils.put(jsonObject, NAME, name);
	}
	
	public BigDecimal getRate() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, RATE);
	}

	public void setRate(BigDecimal rate) {
		DataModelUtils.put(jsonObject, RATE, rate);
	}


}
