package idig.za.net.conscalc;

import java.sql.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseInterface {
	
	/****************************
	 * created by clive anthony *
	 * www.101apps.co.za        *
	 * **************************
	 */

	private static final String TableName = "T_fuelMaster";
	private static final String RowId = "_id";
	private static final String Col_date = "date";
	private static final String Col_stringDate = "stringDate";
	private static final String Col_odometer = "odometer";
	private static final String Col_litres = "litres";
	private static final String Col_cost = "cost";
	private Context context;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;

	private FuelRecord aFuelRecord;
	private FuelRecordList fuelRecordList;

	public DatabaseInterface(Context context) {
		this.context = context;
	}

	public DatabaseInterface openWritable() {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public DatabaseInterface openReadable() {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getReadableDatabase();
		return this;
	}

	public DatabaseInterface close() {
		dbHelper.close();
		return this;
	}

	public int getOdometer(long longDate) {
		openReadable();
		String sql = "SELECT odometer FROM T_fuelMaster WHERE date = '"
				+ longDate + "'";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		int odometer = cursor.getInt(0);
		cursor.close();
		close();
		return odometer;
	}

	public String getStringDate(int odometer) {
		openReadable();
		String sql = "SELECT stringDate FROM T_fuelMaster WHERE odometer = "
				+ odometer;
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		String stringDate = cursor.getString(0);
		cursor.close();
		close();
		return stringDate;
	}

	public long getLongDate(int odometer) {
		openReadable();
		String sql = "SELECT date FROM T_fuelMaster WHERE odometer = "
				+ odometer;
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		long aLongDate = cursor.getLong(0);
		cursor.close();
		close();
		return aLongDate;
	}

	public int[] getConsumptionResult(int startOdometer, int endOdometer) {
		openReadable();
		Cursor cursor = database.rawQuery("SELECT SUM(litres) as sum FROM "
				+ "T_fuelMaster where odometer >= " + startOdometer
				+ " and odometer <= " + endOdometer, null);
		cursor.moveToFirst();
		int totalLitres = cursor.getInt(0);
		cursor.close();

		Cursor c = database.rawQuery("SELECT SUM(cost) as sum FROM "
				+ "T_fuelMaster where odometer >= " + startOdometer
				+ " and odometer <= " + endOdometer, null);
		c.moveToFirst();
		int totalCost = c.getInt(0);
		c.close();
		int[] resultArray = { totalLitres, totalCost };

		dbHelper.close();

		return resultArray;
	}

	public FuelRecordList getRecordList(Cursor cursor) {

		fuelRecordList = new FuelRecordList();
		cursor.moveToFirst();
		do {
			aFuelRecord = new FuelRecord();

			int rowId = cursor.getInt(0);
			long date = cursor.getLong(1);
			int odometer = cursor.getInt(3);
			String litres = cursor.getString(4);
			String cost = cursor.getString(5);

			aFuelRecord.setRowId(rowId);
			aFuelRecord.setDate(date);
			aFuelRecord.setOdometer(odometer);
			aFuelRecord.setLitres(litres);
			aFuelRecord.setCost(cost);

			fuelRecordList.add(aFuelRecord);

		} while (cursor.moveToNext());
		cursor.close();
		dbHelper.close();
		return fuelRecordList;
	}

	public int getNumberOfRecords() {
		openReadable();
		String sql = "SELECT " + RowId + " FROM " + TableName;
		Cursor cursor = database.rawQuery(sql, null);
		int numberOfRecords = cursor.getCount();
		cursor.close();
		dbHelper.close();
		return numberOfRecords;
	}

	public Cursor findManyRecords(String columnName, String stringWhereClause,
			int intWhereClause) {
		Cursor c;
		String sql = null;
		openReadable();
		if (columnName == "stringDate") {
			String yearString = stringWhereClause.substring(0, 4);
			sql = "SELECT * FROM " + TableName + " WHERE " + columnName
					+ " LIKE '" + yearString + "%' ORDER BY date DESC";
		} else {
			String intString = Integer.toString(intWhereClause);
			if (intString.length() < 2)
				intString = intString + "0";
			String odometerSearch = intString.substring(0, 1);
			int searchInt = Integer.parseInt(odometerSearch);
			sql = "SELECT * FROM " + TableName + " WHERE " + columnName
					+ " LIKE '" + searchInt + "%' ORDER BY date DESC";
		}
		c = database.rawQuery(sql, null);
		c.moveToFirst();
		return c;
	}

	public long addRecord(FuelRecord afuelRecord) {
		openWritable();

		ContentValues values = new ContentValues();
		values.put(Col_date, afuelRecord.getDate());
		Date longDate = new Date(afuelRecord.getDate());
		String stringDate = longDate.toString();
		values.put(Col_stringDate, stringDate);
		values.put(Col_odometer, afuelRecord.getOdometer());
		values.put(Col_litres, afuelRecord.getLitres());
		values.put(Col_cost, afuelRecord.getCost());
		long result = database.insert(TableName, RowId, values);
		close();
		return result;
	}

	public int deleteRecord(int rowId) {
		openWritable();
		int result = database.delete(TableName, RowId + " = " + rowId, null);
		close();
		return result;
	}

	public Cursor findRecord(String stringValue, int intValue, long longValue,
			String columnName) {
		String stringDate = null;
		if (columnName == "date") {
			Date longDate = new Date(longValue);
			stringDate = longDate.toString();
		}
		openReadable();
		String sql = null;
		if (stringValue != null)
			sql = "SELECT * FROM " + TableName + " WHERE " + columnName + " = "
					+ stringValue;
		if (columnName == "odometer")
			sql = "SELECT * FROM " + TableName + " WHERE odometer = "
					+ intValue;
		if (columnName == "date")
			sql = "SELECT * FROM " + TableName + " WHERE stringDate = '"
					+ stringDate + "'";
		Cursor cursor = database.rawQuery(sql, null);
		return cursor;
	}

	public int upDateRecord(FuelRecord aFuelRecord) {
		openWritable();
		int rowId = aFuelRecord.getRowId();
		Date curDate = new Date(aFuelRecord.getDate());
		String stringDate = curDate.toString();

		ContentValues values = new ContentValues();
		values.put(Col_date, aFuelRecord.getDate());
		values.put(Col_stringDate, stringDate);
		values.put(Col_odometer, aFuelRecord.getOdometer());
		values.put(Col_litres, aFuelRecord.getLitres());
		values.put(Col_cost, aFuelRecord.getCost());
		int result = database.update(TableName, values, RowId + " = " + rowId,
				null);
		close();
		return result;
	}

}
