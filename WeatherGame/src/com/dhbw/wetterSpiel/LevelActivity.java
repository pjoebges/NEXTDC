package com.dhbw.wetterSpiel;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LevelActivity extends Activity {

	Locale myLocale;
	int language_number;

	SharedPreferences settings;
	private String language;
	private boolean sounds;
	public static Context context;
	public static Toast toast;
	
	Button bt_level1;
	Button bt_level2;
	Button bt_level3; 
	Button bt_level4 ;
	Button bt_level5 ;
	Button bt_level6 ;
	Button bt_level7 ;
	Button bt_level8 ;
	Button bt_level9;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Die benötigte XML-Layout Datei:
		 */
		LevelActivity.context = this;
		if (settings == null)
			settings = getSharedPreferences(MainActivity.prefs_name, 0);

		language = settings.getString("Language", Locale.getDefault()
				.getLanguage());
		sounds = settings.getBoolean("Sounds", true);
		
		Configuration conf = getResources().getConfiguration();
		conf.locale = new Locale(language);
		getResources().updateConfiguration(conf,
				getResources().getDisplayMetrics());

		setContentView(R.layout.levels);

		/**
		 * Buttons setzen
		 */
		 bt_level1 = (Button) findViewById(R.id.levelButton1);
		 bt_level2 = (Button) findViewById(R.id.levelButton2);
		 bt_level3 = (Button) findViewById(R.id.levelButton3);
		 bt_level4 = (Button) findViewById(R.id.levelButton4);
		 bt_level5 = (Button) findViewById(R.id.levelButton5);
		 bt_level6 = (Button) findViewById(R.id.levelButton6);
		 bt_level7 = (Button) findViewById(R.id.levelButton7);
		 bt_level8 = (Button) findViewById(R.id.levelButton8);
		 bt_level9 = (Button) findViewById(R.id.levelButton9);
		
		/**
		 * Nur den jeweiligen Button freigeben, wenn das vorige Level schon
		 * geschafft wurde.
		 */
		bt_level1.setEnabled(true);
		bt_level2.setEnabled(MainActivity.settings.getBoolean("1", false));
		bt_level3.setEnabled(MainActivity.settings.getBoolean("2", false));
		bt_level4.setEnabled(MainActivity.settings.getBoolean("3", false));
		bt_level5.setEnabled(MainActivity.settings.getBoolean("4", false));
		bt_level6.setEnabled(MainActivity.settings.getBoolean("5", false));
		bt_level7.setEnabled(MainActivity.settings.getBoolean("6", false));
		bt_level8.setEnabled(MainActivity.settings.getBoolean("7", false));
		bt_level9.setEnabled(MainActivity.settings.getBoolean("8", false));
		/**
		 * Die Implementierung der Listener folgt dem gleichen Muster. Zunächst
		 * wird das ausgewählte Level als Wert der SpielActivity übergeben und
		 * dadurch ein neuer Thread gestartet. Danach wird ein Intent erstellt
		 * und auf die SpielActivity gewechselt.
		 */
		bt_level1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 1;
				SpielActivity.nextLevel = 2;
				Intent nextScreen = new Intent(getApplicationContext(),
						SpielActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				
				/**
				 * Wegen längerer Ladezeit der Wetterdaten ein Toast anzeigen.
				 */
				startActivity(nextScreen);
			}
		});
		bt_level2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 2;
				SpielActivity.nextLevel = 3;
				Intent nextScreen = new Intent(getApplicationContext(),
						SpielActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});
		bt_level3.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 3;
				SpielActivity.nextLevel = 4;
				Intent nextScreen = new Intent(getApplicationContext(),
						SpielActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});
		bt_level4.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 4;
				SpielActivity.nextLevel = 5;
				Intent nextScreen = new Intent(getApplicationContext(),
						SpielActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});
		bt_level5.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 5;
				SpielActivity.nextLevel = 6;
				Intent nextScreen = new Intent(getApplicationContext(),
						SpielActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});
		bt_level6.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 6;
				SpielActivity.nextLevel = 7;
				Intent nextScreen = new Intent(getApplicationContext(),
						SpielActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});
		bt_level7.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 7;
				SpielActivity.nextLevel = 8;
				Intent nextScreen = new Intent(getApplicationContext(),
						SpielActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});
		bt_level8.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 8;
				SpielActivity.nextLevel = 9;
				Intent nextScreen = new Intent(getApplicationContext(),
						LevelActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});
		bt_level9.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SpielActivity.currentLevel = 9;
				SpielActivity.nextLevel = 1;
				Intent nextScreen = new Intent(getApplicationContext(),
						LevelActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Nur den jeweiligen Button freigeben, wenn das vorige Level schon geschafft wurde.
		bt_level1.setEnabled(true);
		bt_level2.setEnabled(MainActivity.settings.getBoolean("1", false));
		bt_level3.setEnabled(MainActivity.settings.getBoolean("2", false));
		bt_level4.setEnabled(MainActivity.settings.getBoolean("3", false));
		bt_level5.setEnabled(MainActivity.settings.getBoolean("4", false));
		bt_level6.setEnabled(MainActivity.settings.getBoolean("5", false));
		bt_level7.setEnabled(MainActivity.settings.getBoolean("6", false));
		bt_level8.setEnabled(MainActivity.settings.getBoolean("7", false));
		bt_level9.setEnabled(MainActivity.settings.getBoolean("8", false));
	}
}