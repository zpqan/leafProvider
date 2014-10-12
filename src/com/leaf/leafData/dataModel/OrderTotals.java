package com.leaf.leafData.dataModel;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.leaf.leafData.util.DataModelUtils;

public class OrderTotals {
	private static final String ORDERS = "orders";
	private static final String FOOD = "food";
	private static final String TAX = "tax";
	private static final String BAR = "bar";
	private static final String TIP = "tip";
	
	private JSONObject jsonObject;
	public OrderTotals(JSONObject jsonObj) {
		this.jsonObject = jsonObj;
	}
	
	public JSONObject getJsonObject() {
		return this.jsonObject;
	}
	public BigDecimal getOrders() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, ORDERS);
	}
	public void setOrders(BigDecimal orders) {
		DataModelUtils.put(jsonObject, ORDERS, orders);
	}
	public BigDecimal getFood() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, FOOD);
	}
	public void setFood(BigDecimal food) {
		DataModelUtils.put(jsonObject, FOOD, food);
	}
	public BigDecimal getTax() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, TAX);
	}
	public void setTax(BigDecimal tax) {
		DataModelUtils.put(jsonObject, TAX, tax);
	}
	public BigDecimal getBar() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, BAR);
	}
	public void setBar(BigDecimal bar) {
		DataModelUtils.put(jsonObject, BAR, bar);
	}
	public BigDecimal getTip() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, TIP);
	}
	public void setTip(BigDecimal tip) {
		DataModelUtils.put(jsonObject, TIP, tip);
	}
}
