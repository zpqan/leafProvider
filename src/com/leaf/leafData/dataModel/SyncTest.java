package com.leaf.leafData.dataModel;

import java.math.BigDecimal;

public class SyncTest {
	public static Order createOrder() {
		Order order = new Order();
		order.setSite_id(1096);
		order.setUser_id(4154);
		order.setOrder_type("tender");
		order.setNumGuests(2);
		order.setTaxes(createTaxes());
		order.addLineItem(createLineItem());
		return order;
	}
	
	public static TaxRate[] createTaxes() {
		TaxRate[] taxes = new TaxRate[3];
		TaxRate taxRate = new TaxRate();
		taxes[0] = createTaxRate("food", 0.05);
		taxes[1] = createTaxRate("sales", 0.051);
		taxes[2] = createTaxRate("beverage", 0.08);
		return taxes;
	}
	
	public static TaxRate createTaxRate(String name, double rate) {
		TaxRate taxRate = new TaxRate();
		taxRate.setName(name);
		taxRate.setRate(new BigDecimal(rate));
		return taxRate;
	}
	

	public static LineItem createLineItem() {
		LineItem lineItem = new LineItem();
		lineItem.setSeat_position(-1);
		lineItem.setSite_id(1096);
		lineItem.setIs_taxable(true);
		lineItem.setSubtotal(new BigDecimal(6.12));
		lineItem.setCatalog_item_id(45938);
		lineItem.setPrice(new BigDecimal(88.12));
		lineItem.setName("newly created line item");
		lineItem.setTax_reason("new reson");
		lineItem.setPrice_override(new BigDecimal(3.14));
		lineItem.setNotes("newly create line item notes");
		return lineItem;	
	}
	
	public static void testModifyLineItem(Order order) {
		LineItem lineItem = order.getLine_items()[0];
		lineItem.setNotes("***modified by sync test");
		lineItem.setDirty(true);
	}
	
}
