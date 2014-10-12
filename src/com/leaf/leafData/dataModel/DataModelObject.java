package com.leaf.leafData.dataModel;

import java.util.List;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Parcelable;

public interface DataModelObject extends Parcelable {
	public JSONObject toJson(Cursor cursor);
	public List<JSONObject> getChangedRecords(ContentResolver contentResolver);
}
