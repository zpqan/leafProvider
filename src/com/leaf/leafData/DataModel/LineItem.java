package com.leaf.leafData.DataModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.leaf.client.util.DataModelUtils;


public class LineItem extends BaseModel{
	private static final String TAG = LineItem.class.getSimpleName();
	private static final String ORDER_ID = "order_id";
	private static final String SITE_ID = "site_id";
	private static final String CATALOG_ITEM_ID = "catalog_item_id";
	private static final String ITEM_ID = "item_id";
	private static final String QUANTITY = "quantity";
	private static final String PRICE = "price";
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String IS_TAXABLE = "is_taxable";
	private static final String TAX_REASON = "tax_reason";
	private static final String SUBTOTAL = "subtotal";
	private static final String SENT_TO_KITCHEN = "sent_to_kitchen";
	private static final String PRICE_OVERRIDE = "price_override";
	private static final String UNIT_TYPE = "unit_type";
	private static final String COURSE = "course";
	private static final String SEAT_POSITION = "seat_position";
	private static final String LINE_ITEM_MODIFIERS = "line_item_modifiers";
	private static final String DISCOUNTS = "discounts";
	private static final String DISCOUNT = "discount";
	private static final String NOTES = "notes";
	private static final String ORDER_UUID = "order_uuid";

	private List<LineItemModifier> lineItemModifiers ;
	private Discount discount;
	
	public LineItem() {
		super();
	}
	
	public LineItem(byte[] json) {
		super(json);
	}
	
	public LineItem(JSONObject jsonObj) {
		super(jsonObj);
		
	}	
	
//	public LineItem(LineItemPatch lineItemPatch) {
//		super(lineItemPatch.json);
//		init();
//		
//		JSONObject patch = lineItemPatch.json;
//		this.jsonObject = patch;
//		
//		setCatalog_item_id(patch.optInt(CATALOG_ITEM_ID));
//		this.setUuid(patch.optString(_UUID));
//		this.setDirty(false);
//		this.setDeleted(false);
//	}

	@Override
	protected void init() {
		super.init();		
		try {
			if (!jsonObject.has(LINE_ITEM_MODIFIERS))  jsonObject.put(LINE_ITEM_MODIFIERS, new JSONArray());
			if (!jsonObject.has(DISCOUNTS))  jsonObject.put(DISCOUNTS, new JSONArray());
		} catch (JSONException e) {
			
		}
		
		/***
		 * change the item_id to catalog_item_id
		 */
		Integer itemId = getItem_id();
		if ( itemId != null) {
			setCatalog_item_id(itemId);
			jsonObject.remove(ITEM_ID);
		}
		fixDiscount();
		fixLineItemModify(DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEM_MODIFIERS));
	}

	@Override
	public void setId(Integer id) {
		super.setId(id);
		for ( LineItemModifier modifier : getLine_item_modifiers()) {
			modifier.setLine_item_id(id);
		}
		Discount discount = getDiscount();
		if ( discount != null) {
			discount.setLine_item_id(id);
		}
	}
	
	private void fixLineItemModify(JSONArray jsonArray) {
		if ( jsonArray == null) {
			jsonArray = new JSONArray();
		}
		
		lineItemModifiers = new ArrayList<LineItemModifier>();
		int arraySize = jsonArray.length();
		for ( int i=0; i< arraySize; i++) {
			JSONObject jsonObj;
			try {
				jsonObj = jsonArray.getJSONObject(i);
				lineItemModifiers.add (new LineItemModifier(jsonObj));
			} catch (JSONException e) {
				continue;
			}
		}
	}
	
	/***
	 * this is to convert the discount object from array to a singleton 
	 */
	private void fixDiscount() {
		JSONArray discounts = getDiscounts();
		JSONObject discountJson = DataModelUtils.getJSONObjectFromArray(discounts, 0);
		if ( discountJson != null) {
			discount = new Discount(discountJson);
		}
		setDiscounts(null);
	}
	public Integer getOrder_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, ORDER_ID);
	}

	public void setOrder_id(Integer order_id) {
		DataModelUtils.put(jsonObject, ORDER_ID, order_id);
	}

	public String getOrder_uuid() {
		return DataModelUtils.getStringFromJson(jsonObject, ORDER_UUID);
	}

	public void setOrder_uuid(String uuid) {
		DataModelUtils.put(jsonObject, ORDER_UUID, uuid);
	}

	public Integer getSite_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, SITE_ID);
	}

	public void setSite_id(Integer site_id) {
		DataModelUtils.put(jsonObject, SITE_ID, site_id);
	}

	public Integer getCatalog_item_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, CATALOG_ITEM_ID);
	}

	public void setCatalog_item_id(Integer catalog_item_id) {
		DataModelUtils.put(jsonObject, CATALOG_ITEM_ID, catalog_item_id);
	}

	public Integer getItem_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, ITEM_ID);
	}

//	public void setItem_id(Integer item_id) {
//		DataModelUtils.put(jsonObject, ITEM_ID, item_id);
//	}

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

	public String getNumber() {
		return DataModelUtils.getStringFromJson(jsonObject, NUMBER);
	}

	public void setNumber(String number) {
		DataModelUtils.put(jsonObject, NUMBER, number);
	}

	public String getName() {
		return DataModelUtils.getStringFromJson(jsonObject, NAME);
	}

	public void setName(String name) {
		DataModelUtils.put(jsonObject, NAME, name);
	}

	public Boolean getIs_taxable() {
		return DataModelUtils.getBooleanFromJson(jsonObject, IS_TAXABLE);
	}

	public void setIs_taxable(Boolean is_taxable) {
		DataModelUtils.put(jsonObject, IS_TAXABLE, is_taxable);
	}

	public String getTax_reason() {
		return DataModelUtils.getStringFromJson(jsonObject, TAX_REASON);
	}

	public void setTax_reason(String tax_reason) {
		DataModelUtils.put(jsonObject, TAX_REASON, tax_reason);
	}

	public BigDecimal getSubtotal() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, SUBTOTAL);
	}

	public void setSubtotal(BigDecimal subtotal) {
		DataModelUtils.put(jsonObject, SUBTOTAL, subtotal);
	}

	public String getNotes() {
		return DataModelUtils.getStringFromJson(jsonObject, NOTES);
	}

	public void setNotes(String notes) {
		DataModelUtils.put(jsonObject, NOTES, notes);
	}

	public Boolean getSent_to_kitchen() {
		return DataModelUtils.getBooleanFromJson(jsonObject, SENT_TO_KITCHEN);
	}

	public void setSent_to_kitchenl(Boolean sent_to_kitchenl) {
		DataModelUtils.put(jsonObject, SENT_TO_KITCHEN, sent_to_kitchenl);
	}

	public BigDecimal getPrice_override() {
		return DataModelUtils.getBigDecimalFromJson(jsonObject, PRICE_OVERRIDE);
	}

	public void setPrice_override(BigDecimal price_override) {
		DataModelUtils.put(jsonObject, PRICE_OVERRIDE, price_override);
	}

	public String getUnit_type() {
		return DataModelUtils.getStringFromJson(jsonObject, UNIT_TYPE);
	}

	public void setUnit_type(String unit_type) {
		DataModelUtils.put(jsonObject, UNIT_TYPE, unit_type);
	}

	public String getCourse() {
		return DataModelUtils.getStringFromJson(jsonObject, COURSE);
	}

	public void setCourse(String course) {
		DataModelUtils.put(jsonObject, COURSE, course);
	}

	public Integer getSeat_position() {
		return DataModelUtils.getIntegerFromJson(jsonObject, SEAT_POSITION);
	}

	public void setSeat_position(Integer seat_position) {
		DataModelUtils.put(jsonObject, SEAT_POSITION, seat_position);
	}

	public LineItemModifier[]  getLine_item_modifiers() {
		LineItemModifier[] result = new LineItemModifier[lineItemModifiers.size()];
		return lineItemModifiers.toArray(result);
	}

	public void setLine_item_modifiers(LineItemModifier[] line_item_modifiers) {
		JSONArray jsonArray = new JSONArray();
		
		if (line_item_modifiers != null) {
			jsonArray = DataModelUtils.modelListToJsonArray(line_item_modifiers);
		}
		DataModelUtils.put(jsonObject, LINE_ITEM_MODIFIERS, jsonArray);
	}

	public void addLineItemModifier(LineItemModifier lineItemModifier) {
		lineItemModifier.setLine_item_id(getId());
		lineItemModifier.setDeleted(false);
		lineItemModifier.setDirty(false);
		lineItemModifier.setId(null);
		lineItemModifier.setSite_id(getSite_id());
		this.lineItemModifiers.add(lineItemModifier);
		JSONArray jsonArray = DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEM_MODIFIERS);
		jsonArray.put(lineItemModifier.getJsonObject());	
		if (getId() != null) {
			this.setDirty(true);
		}
	}
	
	public void deleteLineItemModifier(LineItemModifier lineItemModifier) {
		if (getId() != null) {
			this.setDirty(true);
			lineItemModifier.setDeleted(true);
		} else {
			
		}
	}
	
	
	public JSONArray getDiscounts() {
		return DataModelUtils.getArrayFromJson(jsonObject, DISCOUNTS);
	}

	public void setDiscounts(JSONArray discounts) {
		DataModelUtils.put(jsonObject, DISCOUNTS, discounts);
	}

	public Discount getDiscount() {
		return discount;
	}
	
	public void setDiscount(Discount value) {
		setDirty(true);
		JSONObject discountJson = null;		
		discount = value;
		if ( discount != null) {
			discount.setSite_id(getSite_id());
			discount.setLine_item_id(getId());
			discountJson = discount.getJsonObject();
		}
		DataModelUtils.put(jsonObject, DISCOUNT, discountJson);
	}

	
	public void trimForRestCall() {
		jsonObject.remove(ITEM_ID);
		remveEmptyStringValue(NOTES);
		remveEmptyStringValue(TAX_REASON);
		jsonObject.remove(ORDER_UUID);

		boolean hasId = getId() != null;
		
		List<LineItemModifier> list = new ArrayList<LineItemModifier>();
		for (LineItemModifier modifier : getLine_item_modifiers() ) {
			if ( modifier.isIncludedInRestCall()) {
				modifier.trimForRestCall();
				list.add(modifier);
				setDirty(true);
			}
		}
		if (!hasId) setDirty(false);
		setLine_item_modifiers(list.toArray(new LineItemModifier[list.size()]));
	}
	
	private void remveEmptyStringValue(String name) {
		String value;
		try {
			value = jsonObject.getString(name);
			if ( value != null && value.length() == 0) {
				jsonObject.remove(name);
			}
		} catch (JSONException e) {
		}
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

	
}
