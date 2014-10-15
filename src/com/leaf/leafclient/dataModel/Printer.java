package com.leaf.leafclient.dataModel;

import java.util.UUID;

import org.json.JSONObject;

import com.leaf.client.util.DataModelUtils;

public class Printer extends BaseModel {
	
	public static final String URLPATH = "/printers";
	private static final String DESCRIPTION = "description";
	private static final String IP = "ip";
	private static final String IS_ACTIVE = "is_active";
	private static final String NAME = "name";
	private static final String PURPOSE = "purpose";
	private static final String SITE_ID = "site_id";
	private static final String PRINTER_TYPE = "printer_type";
   
	public Printer() {
		super();
	}

	public Printer(byte[] json) {
		super(json);
	}

	public Printer(JSONObject jsonObj) {
		super(jsonObj);
	}

	@Override
	protected void init() {
		/***
		 * if the json comes from the server, we want to make sure
		 * we add a uuid to it. In the future, we will fix the server 
		 * so that it will includes the uuid. 
		 */
		if ( getUuid() == null ) {
			setUuid(UUID.randomUUID().toString());
		}
		
	}
	public String getDescription() {
		return DataModelUtils.getStringFromJson(jsonObject, DESCRIPTION);
	}

	public void setDescription(String description) {
		DataModelUtils.put(jsonObject, DESCRIPTION, description);
	}

	public String getIp() {
		return DataModelUtils.getStringFromJson(jsonObject, IP);
	}

	public void setIp(String ip) {
		DataModelUtils.put(jsonObject, IP, ip);
	}

	public Boolean getIs_active() {
		return DataModelUtils.getBooleanFromJson(jsonObject, IS_ACTIVE);
	}

	public void setIs_active(Boolean is_active) {
		DataModelUtils.put(jsonObject, IS_ACTIVE, is_active);
	}

	public String getName() {
		return DataModelUtils.getStringFromJson(jsonObject, NAME);
	}

	public void setName(String name) {
		DataModelUtils.put(jsonObject, NAME, name);
	}

	public Integer getPurpose() {
		return DataModelUtils.getIntegerFromJson(jsonObject, PURPOSE);
	}

	public void setPurpose(Integer purpose) {
		DataModelUtils.put(jsonObject, PURPOSE, purpose);
	}

	public Integer getSite_id() {
		return DataModelUtils.getIntegerFromJson(jsonObject, SITE_ID);
	}

	public void setSite_id(Integer site_id) {
		DataModelUtils.put(jsonObject, SITE_ID, site_id);
	}

	public Integer getPrinter_type() {
		return DataModelUtils.getIntegerFromJson(jsonObject, PRINTER_TYPE);
	}

	public void setPrinter_type(Integer printer_type) {
		DataModelUtils.put(jsonObject, PRINTER_TYPE, printer_type);
	}
	
}
