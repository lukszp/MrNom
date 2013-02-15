package idig.za.net.conscalc;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddRecord extends Activity {
    
	private static final String TAG = "ConsCalc";
	private TextView mDateDisplay;
	private Button mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	long datePubStamp;
	static final int DATE_DIALOG_ID = 0;
	private boolean dateChanged = false;
	private long date;
	private String stringDate;
	public static final String PREFS_NAME = "MyPrefsFile";
	
	FuelRecord aFuelRecord;
	private DatabaseInterface dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_record);
		
		dbHelper = new DatabaseInterface(this);
		
		final Calendar c = Calendar.getInstance();
		date = c.getTimeInMillis();
		
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		Date curDate = new Date(date);
		stringDate = curDate.toString();
		Log.i(TAG, "the long date is " + date);
		Log.i(TAG, "the stringDate = " + stringDate);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String registrationNumber = settings.getString("registrationNumber", "no reg# set");
		
		TextView tvDate = (TextView) findViewById(R.id.chosenDateTextView);
		tvDate.setText(stringDate);
		
		TextView tvRegNumber = (TextView) findViewById(R.id.registrationNumberTextView);
		tvRegNumber.setText(registrationNumber);
		
		final Button saveRecordButton = (Button) findViewById(R.id.addRecordButton);
		saveRecordButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final EditText editTextOdometer = (EditText) findViewById(R.id.odometerEditText);
				final EditText editTextLitres = (EditText) findViewById(R.id.litresEditText);
				final EditText editTextCost = (EditText) findViewById(R.id.costEditText);
				
				if ((editTextOdometer.getText().toString().trim().length() == 0) || (editTextLitres.getText().toString().trim().length() == 0) || (editTextCost.getText().toString().trim().length() == 0)) {
					Toast.makeText(AddRecord.this, getResources().getString(R.string.addRecordToast), Toast.LENGTH_LONG).show();
				}
				else {
					int odometer = Integer.parseInt(editTextOdometer.getText().toString());
					String litres = editTextLitres.getText().toString();
					String cost = editTextLitres.getText().toString();
					
					long savedDate;
					
					if (dateChanged) {
						savedDate = datePubStamp;
					} else {
						savedDate = date;
					}
					
					//store data
					aFuelRecord = new FuelRecord();
					//put fields into record
					aFuelRecord.setDate(savedDate);
					aFuelRecord.setOdometer(odometer);
					aFuelRecord.setLitres(litres);
					aFuelRecord.setCost(cost);
					
					//add record
					if (dbHelper.addRecord(aFuelRecord) != -1) {
						Toast.makeText(AddRecord.this, getResources().getString(R.string.addRecordOK), Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(AddRecord.this, getResources().getString(R.string.addRecordError), Toast.LENGTH_LONG).show();
					}
					
					Intent menuIntent = new Intent(AddRecord.this, Menu.class);
					startActivity(menuIntent);
					
				}
			}
		});
		
		mDateDisplay = (TextView) findViewById(R.id.chosenDateTextView);
		mPickDate = (Button) findViewById(R.id.addDateButton);
		
		mPickDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
				
			}
			
		});
				
	}	
	private void updateDisplay() {
		dateChanged = true;
		mDateDisplay.setText(new StringBuilder().append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, mYear);
		cal.set(Calendar.MONTH, mMonth);
		cal.set(Calendar.DAY_OF_MONTH, mDay);
		datePubStamp = cal.getTimeInMillis();
		
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
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
	
}
