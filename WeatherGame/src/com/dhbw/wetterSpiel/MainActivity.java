package com.dhbw.wetterSpiel;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dhbw.wetterSpiel.R;

public class MainActivity extends Activity {
	private String language;
	private boolean sounds;
	public static SharedPreferences settings;
	public static final String prefs_name = "WetterQuizApp";
	public static Context context;
	public static MediaPlayer mediaplayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Konfiguration holen
		 */
		if (settings == null)
			settings = getSharedPreferences(prefs_name, 0);

		language = settings.getString("Language", Locale.getDefault()
				.getLanguage());
		sounds = settings.getBoolean("Sounds", true);

		Configuration conf = getResources().getConfiguration();
		conf.locale = new Locale(language);
		getResources().updateConfiguration(conf,
				getResources().getDisplayMetrics());

		setContentView(R.layout.activity_main);
		/**
		 * Kontext für den MediaPlayer setzen, MediaPlayer initialisieren
		 */
		MainActivity.context = getApplicationContext();
		MainActivity.setMediaPlayer();

		Button startButton = (Button) findViewById(R.id.buttonStart);
		Button beendenButton = (Button) findViewById(R.id.buttonBeenden);
		Button einstellungenButton = (Button) findViewById(R.id.buttonEinstellungen);
		Button anleitungButton = (Button) findViewById(R.id.buttonAnleitung);
		/**
		 * ClickListener implementieren für den Button zum Wechsel der Activity
		 */
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(getApplicationContext(),
						LevelActivity.class);
				if (sounds) {
					Log.i("Level", "click");
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);

			}
		});
		/**
		 * ClickListener implementieren für den Button zum Spiel beenden
		 */
		beendenButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				/**
				 * Schließe das Spiel Activity
				 */
				finish();

			}
		});
		/**
		 * ClickListener implementieren für den Button zu den Einstellungen
		 */
		einstellungenButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(getApplicationContext(),
						EinstellungenActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);

			}
		});
		/**
		 * ClickListener implementieren für den Button zu der Anleitung
		 */
		anleitungButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(getApplicationContext(),
						AnleitungActivity.class);
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				startActivity(nextScreen);

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		/**
		 * Prüfung ob sich Sprache geändert hat
		 */
		if (language != settings.getString("Language", Locale.getDefault()
				.getLanguage())) {
			Configuration conf = getResources().getConfiguration();
			conf.locale = new Locale(language);
			getResources().updateConfiguration(conf,
					getResources().getDisplayMetrics());
			sounds = settings.getBoolean("Sounds", true);
			Intent refresh = new Intent(getApplicationContext(),
					MainActivity.class);
			finish();
			startActivity(refresh);
		}

	}

	public static void setMediaPlayer() {
		mediaplayer = MediaPlayer.create(context, R.raw.buttonsound);
	}
}
