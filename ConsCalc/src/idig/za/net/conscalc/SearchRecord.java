package idig.za.net.conscalc;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRecord extends Activity {

	private int goTo;
	private Button mPickDate;
	private TextView mDateDisplay;
	static final int DATE_DIALOG_ID = 0;
	private int mYear;
	private int mMonth;
	private int mDay;
	private long datePubStamp;
	private int odometer;
	private DatabaseInterface dbHelper;
	private FuelRecordList fuelRecordList;
	protected int rowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_record);
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		goTo = b.getInt("goTo");
		
		final Button selectOdometerButton = (Button) findViewById(R.id.buttonSelectOdometer);
		selectOdometerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText e_odometer = (EditText) findViewById(R.id.odometerEditText);
				odometer = Integer.parseInt(e_odometer.getText().toString());
				//search database for this record
				Cursor cursor = dbHelper.findRecord(null, odometer, 0, "odometer");
				//parse results
				switch (cursor.getCount()) {
				case 0:
					//no record found - try to find closest records
					Cursor c = dbHelper.findManyRecords("odometer", null, odometer);
					switch (c.getCount()) {
					case 0:
						//no records found - go back and try again
						Toast.makeText(SearchRecord.this, getResources().getString(R.string.noRecordFound), Toast.LENGTH_LONG).show();
						Intent menuIntent = new Intent(SearchRecord.this, Menu.class);
						startActivity(menuIntent);
						break;
					case 1:
						//one record found - display it
						fuelRecordList = new FuelRecordList();
						fuelRecordList = dbHelper.getRecordList(c);
						//new intent do display results
						Intent intent = new Intent(SearchRecord.this, DisplayRecord.class);
						//close cursor and db
						c.close();
						dbHelper.close();
						//put data into bundle
						Bundle theBundle = new Bundle();
						theBundle.putParcelable("fuelRecordList", fuelRecordList);
						theBundle.putInt("goTo", goTo);
						theBundle.putInt("rowId", rowId);
						intent.putExtras(theBundle);
						//start intent
						startActivity(intent);				
						break;
					default:
						//many found
						//get list
						fuelRecordList = new FuelRecordList(); 
						fuelRecordList = dbHelper.getRecordList(c);
						c.close();
						dbHelper.close();
						Intent i = new Intent(SearchRecord.this, SelectedList.class);
						//pack bundle
						Bundle bundle = new Bundle();
						bundle.putParcelable("fuelRecordList", fuelRecordList);
						bundle.putInt("goTo", goTo);
						bundle.putBoolean("isDate", false);
						i.putExtras(bundle);
						startActivity(i);				
						break;
					}
				case 1:
					//one found - display it
					fuelRecordList = new FuelRecordList();					
					cursor.close();
					dbHelper.close();
					//create intent
					Intent intent = new Intent(SearchRecord.this, DisplayRecord.class);
					//build bundle
					Bundle theBundle = new Bundle();
					theBundle.putParcelable("fuelRecordList", fuelRecordList);
					theBundle.putInt("goTo", goTo);
					theBundle.putInt("rowId", rowId);
					intent.putExtras(theBundle);
					startActivity(intent);
					break;
				default:
					//close cursor and db
					cursor.close();
					dbHelper.close();					
					break;
				}
				
			}
		});
		
		//capture View elements
		mDateDisplay = (TextView) findViewById(R.id.textViewDateDisplay);
		mPickDate = (Button) findViewById(R.id.buttonPickDate);
		
		//add a click listener
		mPickDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		//get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		

	}
	
	//callback from date picker dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
			
				try {
					updateDisplay();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}
	
	private void updateDisplay() throws Exception {
		mDateDisplay.setText(new StringBuilder().append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
		//convert chosen date to miliseconds format
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, mYear);
		cal.set(Calendar.MONTH, mMonth);
		cal.set(Calendar.DAY_OF_MONTH, mDay);
		datePubStamp = cal.getTimeInMillis();
		Cursor cursor = dbHelper.findRecord(null,0,datePubStamp, "date");
		handleDateSearch(cursor);		
	}
	
	
	public void handleDateSearch(Cursor c) {
		Cursor cursor = null;
		//no record found
		if (c.getCount() == 0) {
			Date curDate = new Date(datePubStamp);
			String stringDate = curDate.toString();
			cursor = dbHelper.findManyRecords("stringDate", stringDate, 0);
			//non similar not found
			if (cursor.getCount() == 0) {
				Toast.makeText(SearchRecord.this, getResources().getString(R.string.noRecordFound), Toast.LENGTH_LONG).show();
				cursor.close();
				dbHelper.close();
			//records found	
			} else {
				//record found - display them
				fuelRecordList = new FuelRecordList();
				fuelRecordList = dbHelper.getRecordList(c);
				//new intent do display results
				Intent intent = new Intent(SearchRecord.this, SelectedList.class);
				if (cursor.getCount() == 1) {
					intent = new Intent(SearchRecord.this, DisplayRecord.class);
				}
				//close cursor and db
				c.close();
				dbHelper.close();
				//put data into bundle
				Bundle theBundle = new Bundle();
				theBundle.putParcelable("fuelRecordList", fuelRecordList);
				theBundle.putInt("goTo", goTo);
				theBundle.putInt("rowId", rowId);
				theBundle.putBoolean("isDate", true);
				intent.putExtras(theBundle);
				//start intent
				startActivity(intent);										
			}
		//record found
		} else {
			fuelRecordList = new FuelRecordList();
			c.moveToFirst();
			if (c.getCount() == 1) {
				fuelRecordList = dbHelper.getRecordList(c);
				
				c.close();
				dbHelper.close();
				
				//create intent
				Intent intent = new Intent(SearchRecord.this, DisplayRecord.class);
				//build bundle
				Bundle theBundle = new Bundle();
				theBundle.putParcelable("fuelRecordList", fuelRecordList);
				theBundle.putInt("goTo", goTo);
				theBundle.putInt("rowId", rowId);
				intent.putExtras(theBundle);
				startActivity(intent);				
				
			} else {
				fuelRecordList = dbHelper.getRecordList(c);
				
				c.close();
				dbHelper.close();				
				//new intent do display results
				Intent intent = new Intent(SearchRecord.this, SelectedList.class);
				//put data into bundle
				Bundle theBundle = new Bundle();
				theBundle.putParcelable("fuelRecordList", fuelRecordList);
				theBundle.putInt("goTo", goTo);
				theBundle.putBoolean("isDate", true);
				intent.putExtras(theBundle);
				//start intent
				startActivity(intent);									
			}
		}
			
	}
}
