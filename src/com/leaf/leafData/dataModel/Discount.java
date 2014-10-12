package com.leaf.leafData.dataModel;



import java.math.BigDecimal;

import org.json.JSONObject;

import com.leaf.leafData.util.DataModelUtils;

public class Discount extends BaseModel {
	private static final String SITE_ID = "site_id";
	private static final String LINE_ITEM_ID = "line_item_id";
	private static final String ORDER_ID = "order_id";
	private static final String AMOUNT = "amount";
	private static final String PERCENTAGE = "percentage";
	private static final String NAME = "name";
	
	public Discount() {
		super();
	}
	
    public Discount (JSONObject jsonObj) {
        super(jsonObj);
        
    }
	public Integer getSite_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, SITE_ID);
	}

	public void setSite_id(Integer site_id) {
		DataModelUtils.put(jsonObject, SITE_ID, site_id);
	}

	public Integer getLine_item_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, LINE_ITEM_ID);
	}
	public void setLine_item_id(Integer line_item_id) {
		DataModelUtils.put(jsonObject, LINE_ITEM_ID, line_item_id);
	}
	
	public Integer getOrder_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, ORDER_ID);
	}
	public void setOrder_id(Integer order_id) {
		DataModelUtils.put(jsonObject, ORDER_ID, order_id);
	}
	
	public BigDecimal getAmount() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, AMOUNT);
	}
	public void setAmount(BigDecimal amount) {
		DataModelUtils.put(jsonObject, AMOUNT, amount);
	}
	public BigDecimal getPercentage() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, PERCENTAGE);
	}
	public void setPercentage(BigDecimal percentage) {
		DataModelUtils.put(jsonObject, PERCENTAGE, percentage); 
	}
	public String getName() {
		return DataModelUtils.getStringFromJson(jsonObject, NAME);
	}
	public void setName(String name) {
		DataModelUtils.put(jsonObject, NAME, name); 
	}
}
