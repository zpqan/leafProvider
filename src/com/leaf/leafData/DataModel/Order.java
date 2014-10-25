package com.leaf.leafData.DataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.leaf.client.util.DataModelUtils;


public class Order extends BaseModel {
	private static final String TAG = Order.class.getSimpleName();
	public static final String URLPATH = "/orders";
	
	private static final String _UUID = "uuid";
	//private static final String SYNC = "sync";
	private static final String NUMBER = "number";
	private static final String SERVE_TIME = "serve_time";
	private static final String SITE_ID = "site_id";
	private static final String USER_ID = "user_id";
	private static final String ORDER_TYPE = "order_type";
	private static final String NUMGUESTS = "numGuests";
	private static final String NUM_GUESTS = "num_guests";
	private static final String OPEN = "open";
	private static final String TOTALS = "totals";
	private static final String TAXES = "taxes";
	private static final String DISCOUNTS = "discounts";
	private static final String DISCOUNT = "discount";
	private static final String LINE_ITEMS = "line_items";
	private static final String LINE_ITEM_MODIFIERS = "line_item_modifiers";
//	private static final String PAYMENTS = "payments";
//	private static final String TRANSACTIONS = "transactions";
	
//	private static final String CREATEREQUEST = "create_request";
//	private static final String PATCHREQUESTS = "patch_requests";

	
	private List<LineItem>lineItems;
	private List<LineItemModifier> lineItemModifiers;
	private Discount discount;
	private TaxRate[] taxes; 
	
	public Order(JSONObject jsonObj) {
		super(jsonObj);
	}
	
	public Order(byte[] json) {
		super(json);
	}
	
	public Order() {
		super();
		init();
	}

	@Override
	protected void init() {
		super.init();
		fixDiscount();
		fixLineItem(DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEMS));
		fixLineItemModify(DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEM_MODIFIERS));
		fixTaxes(DataModelUtils.getArrayFromJson(jsonObject, TAXES));
		if ( getUuid() == null) {
			/***
			 * if the json comes from the server, we want to make sure
			 * we add a uuid to it. In the future, we will fix the server 
			 * so that it will includes the uuid. 
			 */
			setUuid(UUID.randomUUID().toString());
		}
	}
	
	@Override
	public void setId(Integer id) {
		super.setId(id);
		for ( LineItem lineItem : getLine_items()) {
			lineItem.setOrder_id(id);
		}
		Discount discount = getDiscount();
		if ( discount != null) {
			discount.setOrder_id(id);
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
			setDiscount(discount);
		}
		setDiscounts(null);
	}
	
	private void fixLineItem(JSONArray jsonArray) {
		if ( jsonArray == null) {
			jsonArray = new JSONArray();
		}
		lineItems = new ArrayList<LineItem>();
		int arraySize = jsonArray.length();
		for ( int i=0; i< arraySize; i++) {
			JSONObject jsonObj;
			try {
				jsonObj = jsonArray.getJSONObject(i);
				lineItems.add(new LineItem(jsonObj));	
			} catch (JSONException e) {
				continue;
			}
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
				lineItemModifiers.add( new LineItemModifier(jsonObj));
			} catch (JSONException e) {
				continue;
			}
		}	
	}
	
	private void fixTaxes(JSONArray jsonArray) {
		int arraySize = jsonArray.length();
		taxes = new TaxRate[arraySize];
		for ( int i=0; i< arraySize; i++) {
			JSONObject jsonObj;
			try {
				jsonObj = jsonArray.getJSONObject(i);
				taxes[i] = new TaxRate(jsonObj);
			} catch (JSONException e) {
				continue;
			}
		}	
	}
	
//	public String getCreateRequest() {
//		return DataModelUtils.getStringFromJson(jsonObject, CREATEREQUEST);
//	}
	
	public String getNumber() {
		return DataModelUtils.getStringFromJson(jsonObject, NUMBER);
	}

	public void setNumber(Integer value) {
		DataModelUtils.put(jsonObject, NUMBER, value);
	}

	public String getServe_time() {
		return DataModelUtils.getStringFromJson(jsonObject, SERVE_TIME);
	}

	public void setServe_time(String serve_time) {
		DataModelUtils.put(jsonObject, SERVE_TIME, serve_time);
	}

	public Integer getSite_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, SITE_ID);
	}

	public void setSite_id(Integer site_id) {
		DataModelUtils.put(jsonObject, SITE_ID, site_id);
	}

	public Integer getUser_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, USER_ID);
	}

	public void setUser_id(Integer user_id) {
		DataModelUtils.put(jsonObject, USER_ID, user_id);
	}

	public String getOrder_type() {
		return DataModelUtils.getStringFromJson(jsonObject, ORDER_TYPE);
	}

	public void setOrder_type(String order_type) {
		DataModelUtils.put(jsonObject, ORDER_TYPE, order_type);
	}

	public Integer getNumGuests() {
		return DataModelUtils.getIntegerFromJson(jsonObject, NUMGUESTS);
	}

	public void setNumGuests(Integer numGuests) {
		DataModelUtils.put(jsonObject, NUMGUESTS, numGuests);
	}

	public Boolean getOpen() {
		return DataModelUtils.getBooleanFromJson(jsonObject, OPEN);
	}

	public void setOpen(Boolean open) {
		DataModelUtils.put(jsonObject, OPEN, open);
	}

	public OrderTotals getTotals() {
		JSONObject ordertotalsJsonObj = DataModelUtils.getJSONObjectFromJson(jsonObject, TOTALS);
		return new OrderTotals(ordertotalsJsonObj);
	}

	public void setTotals(OrderTotals totals) {
		DataModelUtils.put(jsonObject, TOTALS, getJsonObject());
	}

	public TaxRate[] getTaxes() {
		JSONArray taxesJsonArray = DataModelUtils.getArrayFromJson(jsonObject, TAXES);
		TaxRate[] taxes = new TaxRate[taxesJsonArray.length()];
		for (int i=0; i<taxes.length; i++) {
			try {
				taxes[i] = new TaxRate(taxesJsonArray.getJSONObject(i));
			} catch (JSONException e) {
			
			}
		}
		return taxes;
	}

	public void setTaxes(TaxRate[] taxes) {
		JSONArray taxesJsonArray = new JSONArray();
		for( TaxRate taxRate : taxes) {
			taxesJsonArray.put(taxRate.getJsonObject());
		}
		DataModelUtils.put(jsonObject, TAXES, taxesJsonArray);
		
	}

	public JSONArray getDiscounts() {
		return DataModelUtils.getArrayFromJson(jsonObject, DISCOUNTS);
	}

	public void setDiscounts(JSONArray discounts) {
		if ( discounts == null) {
			discounts = new JSONArray();
		}
		DataModelUtils.put(jsonObject, DISCOUNTS, discounts);
	}

	public Discount getDiscount() {
		return discount;
		//return DataModelUtils.getJSONObjectFromJson(jsonObject, DISCOUNTS);
	}
	
	public void setDiscount(Discount value) {
		JSONObject discountJson = null;		
		discount = value;
		if ( discount != null) {
			discount.setSite_id(getSite_id());
			discount.setOrder_id(getId());
			discountJson = discount.getJsonObject();
		}
		DataModelUtils.put(jsonObject, DISCOUNT, discountJson);
	}
	
	public LineItem[] getLine_items() {
		LineItem[] result = new LineItem[lineItems.size()];
		return lineItems.toArray(result);	
	}

	public void setLine_items(LineItem[] lineItems) {
		JSONArray jsonArray = DataModelUtils.modelListToJsonArray(lineItems);
		DataModelUtils.put(jsonObject, LINE_ITEMS, jsonArray);
	}

	public LineItemModifier[]  getLine_item_modifiers() {
		LineItemModifier[] result = new LineItemModifier[lineItemModifiers.size()];
		return lineItemModifiers.toArray(result);
	}

	public void setLine_item_modifiers(LineItemModifier[] line_item_modifiers) {
		JSONArray jsonArray = DataModelUtils.modelListToJsonArray(line_item_modifiers);
		DataModelUtils.put(jsonObject, LINE_ITEM_MODIFIERS, jsonArray);
	}

	public JSONObject getCreateRequest() {
		JSONObject createRequest = new JSONObject();
		
		try {
			createRequest.putOpt(SITE_ID, getSite_id());
			createRequest.putOpt(USER_ID, getUser_id());
			createRequest.putOpt(ORDER_TYPE, getOrder_type());
			createRequest.putOpt(SERVE_TIME, this.getServe_time());
			createRequest.putOpt(NUM_GUESTS, this.getNumGuests());
			createRequest.putOpt(_UUID, getUuid());
		} catch (JSONException e) {
			Log.e(TAG, "getCreateRequest failed", e);
		}
		
		return createRequest;
	}
	
	
//	public static Order fromCreateRequest(JSONObject createRequest) {
//
//		JSONObject orderObject = new JSONObject();
//		JSONObject syncObject = new JSONObject();
//
//		try {
//			syncObject.put(_UUID, createRequest.get(_UUID));
//
//			orderObject.put(SITE_ID, createRequest.getInt(SITE_ID));
//			orderObject.put(ORDER_TYPE, createRequest.getString(ORDER_TYPE));
//
//			if (createRequest.has(SERVE_TIME)) orderObject.put(SERVE_TIME, createRequest.getString(SERVE_TIME));
//			if (createRequest.has(NUM_GUESTS)) orderObject.put(NUMGUESTS, createRequest.getInt(NUM_GUESTS));
//			if (createRequest.has(USER_ID)) orderObject.put(USER_ID, createRequest.getInt(USER_ID));
//
//			JSONArray emptyArray = new JSONArray();
//			orderObject.put(LINE_ITEMS, emptyArray);
//			orderObject.put(LINE_ITEM_MODIFIERS, emptyArray);
//			orderObject.put(TAXES, emptyArray);
//			orderObject.put(DISCOUNTS, emptyArray);
//			orderObject.put(CREATEREQUEST, createRequest);
//			orderObject.put(PATCHREQUESTS, emptyArray);
//			
//			orderObject.put("sync", syncObject);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		Order order = new Order(orderObject);
//		return order;
//	}

//	private void addNonNull(JSONObject jsonObject, String name, Object value) {
//		if (value != null)
//			try {
//				jsonObject.putOpt(name, value);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//	}

	public void addLineItem(LineItem lineItem) {
		lineItem.setOrder_id(getId());
		lineItem.setDeleted(false);
		lineItem.setDirty(false);
		lineItem.setId(null);
		lineItem.setSite_id(getSite_id());
		this.lineItems.add(lineItem);
		JSONArray jsonArray = DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEMS);
		jsonArray.put(lineItem.getJsonObject());
	}
	
//	public void addLineItemFromPatch(JSONObject lineItemPatch) {
//		LineItem lineItem = LineItem.fromLineItemPatch(lineItemPatch);
//		lineItem.setOrder_id(getId());
//		lineItem.setDeleted(false);
//		lineItem.setDirty(false);
//		lineItem.setId(null);
//		lineItem.setSite_id(getSite_id());
//		this.lineItems.add(lineItem);
//		JSONArray jsonArray = DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEMS);
//		jsonArray.put(lineItem.getJsonObject());
//	}


//	public void addLineItemModifier(LineItemModifier modifier) {
//		modifier.setDeleted(false);
//		modifier.setDirty(false);
//		modifier.setId(null);
//		this.lineItemModifiers.add(modifier);
//		JSONArray jsonArray = DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEM_MODIFIERS);
//		jsonArray.put(modifier.getJsonObject());
//	}
//	
//	public void addLineItemModifierFromPatch(JSONObject patch) {
//		LineItemModifier modifier = LineItemModifier.fromLineItemModifierPatch(patch);
//
//		this.lineItemModifiers.add(modifier);
//		JSONArray jsonArray = DataModelUtils.getArrayFromJson(jsonObject, LINE_ITEMS);
//		jsonArray.put(modifier.getJsonObject());
//	}

	
	public void addTaxRate(TaxRate taxRate) {
		JSONArray taxesJsonArray = DataModelUtils.getArrayFromJson(jsonObject, TAXES);
		taxesJsonArray.put(taxRate.getJsonObject());
		
	}	
	public String trimForRestCall() {
		setLine_item_modifiers(null);
		if ( getId() == null) {
			setLine_items(null);			
			setDiscounts(null);
			return jsonObject.toString();
		}
		trimLineItems();
		Discount discount = getDiscount();
		if ( discount != null && discount.getOrder_id() == null) {
			setDiscount(null);
		}
		return jsonObject.toString();
	}
	
	private void trimLineItems() {
		List<LineItem> list = new ArrayList<LineItem>();
		for (LineItem lineItem : getLine_items()) {
			if ( lineItem.isIncludedInRestCall() ){
				lineItem.trimForRestCall();
				list.add(lineItem);
			}
		}
		setLine_items(list.toArray(new LineItem[list.size()]));
	}
	
	public void addLineItemModifier(LineItemModifier modifier) {
		Integer lineItemId = modifier.getLine_item_id();
		LineItem lineItem = null;
		if ( lineItemId != null) {
			lineItem = findLineItemById(lineItemId);
		} else {
			lineItem = findLineItemByUuid(modifier.getLine_item_uuid());
		}
		lineItem.addLineItemModifier(modifier);
	}
	
	private LineItem findLineItemById(Integer id) {
		if ( id != null) {
			for (LineItem lineItem : getLine_items() ) {
				if (id.equals(lineItem.getId())) {
					return lineItem;
				}
			}
		}
		throw new IllegalArgumentException("can not find line item with id: " + id);
	}
	
	private LineItem findLineItemByUuid(String uuid) {
		if ( uuid != null) {
			for (LineItem lineItem : getLine_items() ) {
				if (uuid.equals(lineItem.getUuid())) {
					return lineItem;
				}
			}
		}
		throw new IllegalArgumentException("can not find line item with id: " + uuid);
	}

	public Order copySubItems(Order src) {
		Integer orderId = this.getId();
		LineItem[] line_items = src.getLine_items();
		for (LineItem line_item : line_items) {
			line_item.setOrder_id(orderId);
		}
		setLine_items(line_items);
		
		setLine_item_modifiers(src.getLine_item_modifiers());
		setDiscounts(src.getDiscounts());
		return this;
	}

//	public int getSyncflag() {
//		int result = LeafDbHelper.CLEAN;
//		Discount discount = getDiscount();
//		if ( discount != null) {
//			if ( discount.getOrder_id() == null) {
//				return LeafDbHelper.NEW;
//			}
//		}
//		for ( LineItem lineItem : getLine_items()) {
//			if ( lineItem.getSyncflag() == LeafDbHelper.NEW) {
//				return LeafDbHelper.NEW;
//			}
//			if ( lineItem.getSyncflag() == LeafDbHelper.MODIFIED) {
//				result = LeafDbHelper.MODIFIED;
//			}
//		}
//		return result;
//	}

	
//	public Payment[] getPayments() {
//		return payments;
//	}
//
//	public void setPayments(Payment[] payments) {
//		this.payments = payments;
//	}

//	public String getTransactions() {
//		return transactions;
//	}
//
//	public void setTransactions(String transactions) {
//		this.transactions = transactions;
//	}
	
}
