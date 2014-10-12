package com.leaf.leafData.activity;

import android.app.Activity;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.leaf.leafData.R;
import com.leaf.leafData.provider.LeafDataType;
import com.leaf.leafData.provider.LeafDbHelper;
import com.leaf.leafData.provider.LeafDataType.DataTypes;
import com.leaf.leafData.provider.LeafDataType.LeafPath;




/**
 * Top-level activity for the test app
 *
 */
public class MainActivity extends Activity {
	static final String TAG = MainActivity.class.getSimpleName();

	private MainActivity thisMain = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisMain = this;
		
		setContentView(R.layout.activity_main);
		ListView listView = (ListView) findViewById(android.R.id.list);

		listView.setAdapter(new ListAdapter() {


			@Override
			public void registerDataSetObserver(DataSetObserver observer) {
			}

			@Override
			public void unregisterDataSetObserver(DataSetObserver observer) {
			}

			@Override
			public int getCount() {
				return LeafPath.values().length;
			}

			@Override
			public Object getItem(int position) {

				return LeafPath.values();
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				ViewGroup vg = (ViewGroup)View.inflate(thisMain, R.layout.main_activity_button_line, null);

				TextView label = (TextView)vg.findViewById(R.id.label_text);
				DataTypes dataType = LeafDataType.getDataType((LeafPath.values()[position]));

				String dataTypeString = dataType.name();
				label.setText(dataType.name());

				Button b = (Button)vg.findViewById(R.id.get_all);
				b.setTag(R.id.datatype_tag, dataTypeString);
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String type = (String) v.getTag(R.id.datatype_tag);
						Log.i(TAG, "click get_all for :" + type);
					}});

				b = (Button)vg.findViewById(R.id.get_by_id);
				b.setTag(R.id.datatype_tag, dataTypeString);
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String type = (String) v.getTag(R.id.datatype_tag);
						Log.d(TAG, "get by id for: " + type);


					}});

				b = (Button)vg.findViewById(R.id.sync);
				b.setTag(R.id.datatype_tag, dataTypeString);
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String typeString = (String)v.getTag(R.id.datatype_tag);
						Log.i(TAG, "calling sync");
					}
				});
				//				b.setVisibility(View.GONE);


				return vg;
			}

			@Override
			public int getItemViewType(int position) {
				return 0;
			}

			@Override
			public int getViewTypeCount() {
				return 1;
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isEnabled(int position) {
				return true;
			}});


				View reset = findViewById(R.id.reset_db);

				reset.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LeafDbHelper dbHelper = new LeafDbHelper(thisMain);
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						dbHelper.recreateDb(db);
					}
				}
						);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		return (id == R.id.action_settings) || super.onOptionsItemSelected(item);
	}


	
	

}
