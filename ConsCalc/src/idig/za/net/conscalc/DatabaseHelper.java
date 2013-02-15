package idig.za.net.conscalc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	/****************************
	 * created by clive anthony *
	 * www.101apps.co.za        *
	 * **************************
	 */

	private static final String DBname = "DB_fuelMaster";
	private static final String TableName = "T_fuelMaster";
	private static int versionNumber = 1;

	public DatabaseHelper(Context context) {
		super(context, DBname, null, versionNumber);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String columns = "(_id integer primary key autoincrement, date integer, stringDate text, odometer integer unique,litres text, cost text)";
		String sql = "CREATE TABLE " + TableName + columns;
		database.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS notes");
		onCreate(database);
	}

}