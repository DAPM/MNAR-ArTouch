package com.example.artouch;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {

	private Spinner languageSpinner, screenSpinner;
	private Button btnSubmit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addItemsOnSpinners();	
		addListenerOnButton();
	}
 
	/** 
	 * add items into spinners dynamically
	 * @author Razvan
	 */
	public void addItemsOnSpinners() 
	{
		screenSpinner = (Spinner) findViewById(R.id.screenSelection);
		List<String> list = new ArrayList<String>();
		list.add("Screen 1");
		list.add("Screen 2");
		list.add("Screen 3");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		screenSpinner.setAdapter(dataAdapter);

		languageSpinner = (Spinner) findViewById(R.id.languageSelection);
		List<String> list2 = new ArrayList<String>();
		list2.add("Romana");
		list2.add("English");
		list2.add("Francais");       
		
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list2);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		languageSpinner.setAdapter(dataAdapter2);
	}

	// get the selected dropdown list value
	public void addListenerOnButton() 
	{
		languageSpinner = (Spinner) findViewById(R.id.languageSelection);
		screenSpinner = (Spinner) findViewById(R.id.screenSelection);
		btnSubmit = (Button) findViewById(R.id.startButton);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String languageChosen = languageSpinner.getSelectedItem().toString();
				String screenChosen = screenSpinner.getSelectedItem().toString();

				Configuration c = new Configuration(getResources().getConfiguration());

				if(languageChosen.equals("English")) 
				{
					c.locale = Locale.ENGLISH;
				}
				else if(languageChosen.equals("Francais")) 
				{
					c.locale = Locale.FRENCH;
				}
				else if(languageChosen.equals("Romana")) 
				{
					c.locale = new Locale("ro", "RO");
				}
				getResources().updateConfiguration(c, getResources().getDisplayMetrics());	

				Intent intent;
				if(screenChosen.equals("Screen 1"))
				{
					intent = new Intent(MainActivity.this, SelectLanguageActivity.class);
					startActivity(intent);
				}
				else if(screenChosen.equals("Screen 2"))
				{
					intent = new Intent(MainActivity.this, TutorialActivity.class);
					startActivity(intent);
				}
				else if(screenChosen.equals("Screen 3"))
				{
					intent = new Intent(MainActivity.this, PaintingExplorerActivity.class);
					startActivity(intent);
				}

			}

		});
	}
}

