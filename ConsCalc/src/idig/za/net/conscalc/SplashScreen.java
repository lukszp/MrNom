package idig.za.net.conscalc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends Activity {

	private TimerTask delayTask;
	private Timer myTimer;
	//preferences file
	public static final String PREFS_NAME = "MyPrefsFile";
	
	private static final String TAG = "ConsCalc";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//splash screen handling
		myTimer = new Timer();
		delayTask = new TimerTask() {
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			boolean registrationIsSet = settings.getBoolean("registrationIsSet", false);
			
			@Override
			public void run() {
			
				Log.i(TAG, "is Registration set in splash screen = " + registrationIsSet);
				
				if (registrationIsSet) {
					myTimer.cancel();
					Intent intent = new Intent(SplashScreen.this, Menu.class);
					startActivity(intent);
				}
				else
				{
					myTimer.cancel();
					Intent intent = new Intent(SplashScreen.this, GetRegistration.class);
					startActivity(intent);
				}
				
			}
			
		};
		
		myTimer.schedule(delayTask,2000);
	}


}
