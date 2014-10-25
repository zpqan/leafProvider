package com.leaf.leafData.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

import com.leaf.client.util.DataModelUtils;
import com.leaf.leafData.provider.LeafDbHelper;



public abstract class BaseModel {
	private static final String ID = "id";
	private static final String DIRTY = "dirty";
	private static final String DELETED = "deleted";
	private static final String VERSION = "version";
	private static final String UUID = "uuid";
	private static final String SYNC = "sync";
	
	protected JSONObject jsonObject;
	private int syncFlag = LeafDbHelper.CLEAN;
 
	public BaseModel() {
		jsonObject = new JSONObject();
		init();
	}
	
	public BaseModel(byte[] json) {
		try {
			this.jsonObject = new JSONObject(new String(json));
		} catch (JSONException e) {
			throw new IllegalArgumentException("invalid json");
		}
		init();
	}
	
	public BaseModel(JSONObject jsonObj) {
		this.jsonObject = jsonObj;
		init();
	}
	
	protected void init() {
		if (!jsonObject.has("sync")) {
			JSONObject syncObject = new JSONObject();
			try {
				jsonObject.put("sync", syncObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		
		Boolean dirty = getDirty();
		if ( dirty == null) {
			setDirty(false);
		}
		Boolean deleted = getDeleted();
		if ( deleted == null) {
			setDeleted( false);
		}
	}
	
	public int getSyncflag() {
		return this.syncFlag;
	}
	
	public void setSyncFlag(int flag) {
		this.syncFlag = flag;
	}
	
	public Integer getId() {
		return DataModelUtils.getIntegerFromJson(jsonObject, ID);
	}
	
	public void setId(Integer id) {
		DataModelUtils.put(jsonObject, ID, id);
	}

	public Boolean getDirty() {
		return DataModelUtils.getBooleanFromJson(jsonObject, DIRTY);
	}

	public void setDirty(Boolean dirty) {
		DataModelUtils.put(jsonObject, DIRTY, dirty);
	}

	public Boolean getDeleted() {
		return DataModelUtils.getBooleanFromJson(jsonObject, DELETED);
	}

	public void setDeleted(Boolean deleted) {
		DataModelUtils.put(jsonObject, DELETED, deleted);
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public String toJson() {
		try {
			return jsonObject.toString(4);
		} catch (JSONException e) {
			return null;
		}
	}
	public Integer getVersion() {
		JSONObject sync = DataModelUtils.getJSONObjectFromJson(jsonObject,SYNC);
		return DataModelUtils.getIntegerFromJson(sync, VERSION);
	}
	public String getUuid() {
		JSONObject sync = DataModelUtils.getJSONObjectFromJson(jsonObject,SYNC);
		return DataModelUtils.getStringFromJson(sync, UUID);
	}
	public void setUuid(String uuid) {
		JSONObject sync = DataModelUtils.getJSONObjectFromJson(jsonObject,SYNC);
		if (sync == null) {
			sync = new JSONObject();
			DataModelUtils.put(jsonObject, SYNC, sync);
		}
		DataModelUtils.put(sync, UUID, uuid);
		DataModelUtils.put(jsonObject, UUID, uuid);
		
	}
	
}
