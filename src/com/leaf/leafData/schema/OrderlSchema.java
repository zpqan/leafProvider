package com.leaf.leafData.schema;

import java.util.HashMap;
import java.util.Map;

public class OrderlSchema implements Schema {
	public static String[] indexedFields = {"user_id", "open"};
	private static Map<String, String> typeMap = new HashMap<String,String>();
	static {
		typeMap.put("user_id", INTEGER_TYPE);
		typeMap.put("open", BOOLEAN_TYPE);
	}

	@Override
	public String[] getIndexedFieldNames() {
		return indexedFields;
	}
	
	@Override
	public String getFeildType (String fieldName) {
		String type = typeMap.get(fieldName);
		if ( type == null) {
			throw new IllegalArgumentException("field name: " + fieldName + " not found");
		}
		return type;
	}
}
