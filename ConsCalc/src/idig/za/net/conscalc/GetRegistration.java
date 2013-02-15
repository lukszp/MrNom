package idig.za.net.conscalc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetRegistration extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_registration);
		
		final Button saveRegistrationButton = (Button) findViewById(R.id.saveRegistration);
		saveRegistrationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText editTextRegistration = (EditText) findViewById(R.id.editTextRegistration);
				String registrationNumber = editTextRegistration.getText().toString();
				//create shared preferences object - settings
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				//create editor
				SharedPreferences.Editor editor = settings.edit();
				//put vars to the editor
				editor.putBoolean("registrationIsSet", true);
				editor.putString("registrationNumber", registrationNumber);
				editor.commit();
				//create menu intent and start Menu activity
				Intent intent = new Intent(GetRegistration.this, Menu.class);
				startActivity(intent);
				
			}
		});
	}	
	
	
	
}
