package idig.za.net.conscalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);		
		
		//Add add button listener
		final Button buttonAdd = (Button) findViewById(R.id.buttonAddRecord);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// create intent object
				Intent intent = new Intent(Menu.this, AddRecord.class);
				startActivity(intent);				
			}
		});
		//Add edit button listener
		final Button buttonEdit = (Button) findViewById(R.id.buttonEditRecord);
		buttonEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Menu.this, SearchRecord.class);
				intent.putExtra("goTo", 1);
				startActivity(intent);				
			}
		});
		//Add delete button listener
		final Button buttonDelete = (Button) findViewById(R.id.buttonDeleteRecord);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		//Add calculate consumption button listener
		final Button buttonCalc = (Button) findViewById(R.id.buttonCalculateConsumption);
		buttonCalc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});


	}	
	

	
}
