package com.dhbw.wetterSpiel;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import util.ForecastApiThread;
import util.WetterApiThread;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EinstellungenActivity extends Activity {

	Locale myLocale;
	int language_number;

	SharedPreferences settings;
	static Context context;
	private String language;
	private boolean music;
	private boolean sounds;
	private boolean wetter;
	private String temp1W;
	private String temp2W;
	private String temp3W;
	private String temp4W;
	private String temp1F;
	private String temp2F;
	private String temp3F;
	private String temp4F;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		/**
		 * Die benötigte XML-Layout Datei:
		 */
		if (settings == null)
			settings = getSharedPreferences(MainActivity.prefs_name, 0);

		language = settings.getString("Language", Locale.getDefault()
				.getLanguage());
		music = settings.getBoolean("Music", true);
		sounds = settings.getBoolean("Sounds", true);
		wetter = settings.getBoolean("Wetter", true);

		Configuration conf = getResources().getConfiguration();
		conf.locale = new Locale(language);
		getResources().updateConfiguration(conf,
				getResources().getDisplayMetrics());

		setContentView(R.layout.einstellungen);

		/**
		 * Button erstellen und Button Listener
		 */
		Button bt_about_game = (Button) findViewById(R.id.buttonUeberdasSpiel);
		Button bt_language = (Button) findViewById(R.id.buttonSprache);
		ToggleButton bt_sounds = (ToggleButton) findViewById(R.id.buttonTon);
		ToggleButton bt_music = (ToggleButton) findViewById(R.id.buttonMusik);
		Button bt_reset_game = (Button) findViewById(R.id.buttonSpielZuruecksetzen);
		ToggleButton bt_wetter = (ToggleButton) findViewById(R.id.wetterdienst);
		Button help = (Button) findViewById(R.id.help);

		bt_sounds.setChecked(sounds);
		bt_music.setChecked(music);
		bt_wetter.setChecked(wetter);

		bt_about_game.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				click_about_game();
			}
		});
		bt_language.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				click_language();
			}
		});
		bt_sounds.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				click_sounds(isChecked);
			}
		});
		bt_music.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				click_music(isChecked);
			}
		});
		bt_reset_game.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				click_reset_game();
			}
		});
		bt_wetter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (sounds) {
					MainActivity.mediaplayer.start();
				}
				click_wetter(isChecked);
			}
		});
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/**
				 * Lade die Wetterdaten zum Vergleich.
				 */
				try {
					temp1W = new WetterApiThread().execute("US0MA0046").get();
					temp2W = new WetterApiThread().execute("AU0VI0027").get();
					temp3W = new WetterApiThread().execute("IN0DL0002").get();
					temp4W = new WetterApiThread().execute("KPXXX0018").get();
					
					temp1F = new ForecastApiThread().execute("42.3587","-71.0567").get();
					temp2F = new ForecastApiThread().execute("-37.8175","144.9671").get();
					temp3F = new ForecastApiThread().execute("28.6138","77.2107").get();
					temp4F = new ForecastApiThread().execute("39.0142","125.7763").get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				/**
				 * Setze die Message und baue den Dialog.
				 */
				String sub1 = getString(R.string.help_message_1) + "\t\t\t" + temp1W + "\t"
						+ temp1F + "\n";
				String sub2 = getString(R.string.help_message_2) + "\t" + temp2W + "\t"
						+ temp2F + "\n";
				String sub3 = getString(R.string.help_message_3) + "\t" + temp3W + "\t"
						+ temp3F + "\n";
				String sub4 = getString(R.string.help_message_4) + "\t" + temp4W + "\t"
						+ temp4F;
				
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
						context);
				dialogBuilder.setTitle(R.string.help_title);
				dialogBuilder.setMessage(sub1 + sub2 + sub3 + sub4);
				dialogBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				AlertDialog dialog = dialogBuilder.create();
				dialog.show();
			}
		});
	}

	/**
	 * Implementierungen der Click-Events
	 */
	private void click_language() {
		/**
		 * Erstellen einer Dialogbox zur Auswahl der Sprache
		 */
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.sprache);
		dialogBuilder.setNegativeButton(R.string.abbrechen, null);
		dialogBuilder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						/**
						 * Sprache ändern.
						 */
						if (language_number == 0)
							myLocale = new Locale("en");
						else if (language_number == 1)
							myLocale = new Locale("de");
						Resources res = getResources();
						DisplayMetrics dm = res.getDisplayMetrics();
						Configuration conf = res.getConfiguration();
						conf.locale = myLocale;
						res.updateConfiguration(conf, dm);
						/**
						 * Konfiguration speichern
						 */
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("Language", myLocale.getLanguage());
						editor.commit();
						/**
						 * Aktivity neustarten
						 */
						Intent refresh = new Intent(getApplicationContext(),
								EinstellungenActivity.class);
						finish();
						startActivity(refresh);

					}
				});
		/**
		 * Richtige Sprache setzen (Man kan auch rumparsen, aber bei 2 Sprachen
		 * kein Verhältnis)
		 */
		String phone_language = getResources().getConfiguration().locale
				.getLanguage();
		language_number = 0;
		if (phone_language.equals("en"))
			language_number = 0;
		else if (phone_language.equals("de"))
			language_number = 1;
		dialogBuilder.setSingleChoiceItems(R.array.languages, language_number,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						/**
						 * Sprache ändern.
						 */
						language_number = which;
					}
				});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/**
	 * Zurücksetzen aller Levels
	 */
	private void click_reset_game() {
		SharedPreferences.Editor editor = settings.edit();
		for (int i = 1; i < 10; i++) {
			editor.putBoolean(i + "", false);
		}
		editor.commit();
		Toast toast = Toast.makeText(this, R.string.spielZurueckgesetzt,
				Toast.LENGTH_LONG);
		toast.show();
	}

	/**
	 * @param isChecked
	 *            Wert, der angibt ob Musik gespielt werden soll.
	 */
	private void click_music(boolean isChecked) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("Music", isChecked);
		music = isChecked;
		editor.commit();
	}

	/**
	 * @param isChecked
	 *            Wert, der angibt ob Sounds abgespielt werden sollen.
	 */
	private void click_sounds(boolean isChecked) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("Sounds", isChecked);
		sounds = isChecked;
		editor.commit();
	}

	/**
	 * @param isChecked
	 *            Wert, der angibt welcher Wetterdienst verwendet wird.
	 */
	private void click_wetter(boolean isChecked) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("Wetter", isChecked);
		editor.commit();
	}

	private void click_about_game() {
		/**
		 * Erstellen einer Dialogbox zur Anzeige des About_game_strings
		 */
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.ueberDasSpiel);
		dialogBuilder.setMessage(R.string.about_game_string);
		dialogBuilder.setNeutralButton(R.string.ok, null);

		/**
		 * Starten des Dialoges
		 */
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();

	}

}
