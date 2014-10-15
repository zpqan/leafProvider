package com.leaf.client.schema;

public interface Schema {
	public static String STRING_TYPE ="STRING_TYPE";
	public static String INTEGER_TYPE ="INTEGER_TYPE";
	public static String BOOLEAN_TYPE ="BOOLEAN_TYPE";

	String[] getIndexedFieldNames();
	String getFeildType (String name);
}
