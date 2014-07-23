package com.dhbw.wetterSpiel;

import java.util.Locale;

import com.dhbw.wetterSpiel.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

public class AnleitungActivity extends Activity {

	Locale myLocale;
	int language_number;

	SharedPreferences settings;
	private String language;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Die benötigte XML-Layout Datei:
		 */
		if (settings == null)
			settings = getSharedPreferences(MainActivity.prefs_name, 0);

		language = settings.getString("Language", Locale.getDefault()
				.getLanguage());

		Configuration conf = getResources().getConfiguration();
		conf.locale = new Locale(language);
		getResources().updateConfiguration(conf,
				getResources().getDisplayMetrics());

		setContentView(R.layout.anleitung);

	}
}
