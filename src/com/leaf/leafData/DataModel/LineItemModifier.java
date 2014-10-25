package com.leaf.leafData.DataModel;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.leaf.client.util.DataModelUtils;

public class LineItemModifier extends BaseModel {
	private static final String CATALOG_ITEM_MODIFIER_ID = "catalog_item_modifier_id";
	private static final String LINE_ITEM_ID = "line_item_id";
	private static final String LINE_ITEM_UUID = "line_item_uuid";
	private static final String NAME = "name";
	private static final String NOTES = "notes";
	private static final String PRICE = "price";
	private static final String QUANTITY = "quantity";
	private static final String SITE_ID = "site_id";

	public LineItemModifier() {
		super();
	}
	public LineItemModifier(JSONObject jsonObj) {
		super(jsonObj);
	}
	
	public Integer getCatalog_item_modifier_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject,
				CATALOG_ITEM_MODIFIER_ID);
	}

	public void setCatalog_item_modifier_id(Integer catalog_item_modifier_id) {
		DataModelUtils.put(jsonObject, CATALOG_ITEM_MODIFIER_ID,
				catalog_item_modifier_id);
	}

	public Integer getLine_item_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, LINE_ITEM_ID);
	}

	public void setLine_item_id(Integer line_item_id) {
		DataModelUtils.put(jsonObject, LINE_ITEM_ID, line_item_id);
	}
	
	public String getLine_item_uuid() {
		return DataModelUtils.getStringFromJson(jsonObject, LINE_ITEM_UUID);
	}

	public void setLine_item_uuid(String line_item_uuid) {
		DataModelUtils.put(jsonObject, LINE_ITEM_UUID, line_item_uuid);
	}


	public String getName() {
		return DataModelUtils.getStringFromJson(jsonObject, NAME);
	}

	public void setName(String name) {
		DataModelUtils.put(jsonObject, NAME, name);
	}

	public String getNotes() {
		return DataModelUtils.getStringFromJson(jsonObject, NOTES);
	}

	public void setNotes(String notes) {
		DataModelUtils.put(jsonObject, NOTES, notes);
	}

	public BigDecimal getPrice() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, PRICE);
	}

	public void setPrice(BigDecimal price) {
		DataModelUtils.put(jsonObject, PRICE, price);
	}

	public BigDecimal getQuantity() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, QUANTITY);
	}

	public void setQuantity(BigDecimal quantity) {
		DataModelUtils.put(jsonObject, QUANTITY, quantity);
	}

	public Integer getSite_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, SITE_ID);
	}

	public void setSite_id(Integer site_id) {
		DataModelUtils.put(jsonObject, SITE_ID, site_id);
	}

	public boolean isIncludedInRestCall() {
		if ( !getDirty() && !getDeleted() && getId() == null) {
			return true;
		}
		if ( getDirty() || getDeleted()) {
			return true;
		}
		return false;
	}

	public static LineItemModifier fromLineItemModifierPatch(
			JSONObject patch) {
		LineItemModifier lim = new LineItemModifier();
//		LineItemModifierPatch {
//			line_item_id (integer, optional),
//			catalog_item_modifier_id (integer, optional),
//			quantity (number, optional),
//			notes (string, optional),
//			deleted (boolean, optional),
//			dirty (boolean, optional)
//			}
//		
//		LineItemModifier {
//			id (integer, optional),
//			site_id (integer, optional),
//			line_item_id (integer, optional),
//			catalog_item_modifier_id (integer, optional),
//			name (string, optional),
//			price (number, optional),
//			quantity (number, optional),
//			notes (string, optional)
//			}

		lim.setCatalog_item_modifier_id(DataModelUtils.getIntegerFromJson(patch, LINE_ITEM_ID));

		lim.setCatalog_item_modifier_id(DataModelUtils.getIntegerFromJson(patch, CATALOG_ITEM_MODIFIER_ID));
		lim.setQuantity(DataModelUtils.getBigDecimalFromJson(patch, QUANTITY));
		lim.setNotes(DataModelUtils.getStringFromJson(patch, NOTES));
		return lim;
	}
}
