package com.dhbw.wetterSpiel;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import util.ForecastApiThread;
import util.IGlobaleKonstanten;
import util.MyDragListener;
import util.MyDropListener;
import util.ObserverRunnable;
import util.WetterApiThread;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dhbw.wetterSpiel.R;

public class SpielActivity extends Activity implements IGlobaleKonstanten {

	public static Context context;
	Locale myLocale;
	int language_number;
	SharedPreferences settings;
	private String language;
	private boolean music;
	public static boolean wetter;
	public static int currentLevel;
	public static int nextLevel;
	MediaPlayer mp;
	/**
	 * Deklarierung aller TextViews, die in der onCreate-Methode initialisiert
	 * werden.
	 */
	public static TextView city1;
	public static TextView city2;
	public static TextView city3;
	public static TextView city4;
	public static TextView temp1;
	public static TextView temp2;
	public static TextView temp3;
	public static TextView temp4;

	/**
	 * Dieses Array speichert die korrekte Zuordnung von Temperatur zur Stadt
	 * und wird zur Überprüfung der Benutzereingaben verwendet.
	 */
	public static String[][] correctValues;
	/**
	 * Dieses Array speichert die Werte für das nächste Level, dessen Werte
	 * bereits vorher abgerufen werden.
	 */
	public static String[][] nextCorrectValues = new String[4][2];
	/**
	 * Anzahl der korrekten Zuweisungen vom Benutzer. Wird vom Runnable-Objekt
	 * verwendet.
	 */
	public static int correctAssignments = 1;
	/**
	 * Handler-Objekt für das Observer-Runnable-Objekt
	 */
	public static Handler handler = new Handler();

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
		music = settings.getBoolean("Music", true);
		wetter = settings.getBoolean("Wetter", true);

		Configuration conf = getResources().getConfiguration();
		conf.locale = new Locale(language);
		getResources().updateConfiguration(conf,
				getResources().getDisplayMetrics());

		/**
		 * Setze den Context für die Anzeige von Toasts aus der Runnable-Klasse
		 */
		SpielActivity.context = this;
		setContentView(R.layout.spiel);

		/**
		 * Initialisierung der TextViews
		 */
		city1 = (TextView) findViewById(R.id.textViewStadt1);
		city2 = (TextView) findViewById(R.id.textViewStadt2);
		city3 = (TextView) findViewById(R.id.textViewStadt3);
		city4 = (TextView) findViewById(R.id.textViewStadt4);
		temp1 = (TextView) findViewById(R.id.textViewTemparatur1);
		temp2 = (TextView) findViewById(R.id.textViewTemparatur2);
		temp3 = (TextView) findViewById(R.id.textViewTemparatur3);
		temp4 = (TextView) findViewById(R.id.textViewTemparatur4);

		/**
		 * Drag&Drop-Handler für die TextViews setzen
		 */
		city1.setOnTouchListener(new MyDragListener());
		city2.setOnTouchListener(new MyDragListener());
		city3.setOnTouchListener(new MyDragListener());
		city4.setOnTouchListener(new MyDragListener());

		temp1.setOnDragListener(new MyDropListener());
		temp2.setOnDragListener(new MyDropListener());
		temp3.setOnDragListener(new MyDropListener());
		temp4.setOnDragListener(new MyDropListener());

		/**
		 * Spiel starten
		 */
		if (music) {
			mp = MediaPlayer.create(SpielActivity.context, R.raw.gamesound);
			mp.setLooping(true);
			mp.start();
		}

		handler.post(new ObserverRunnable());
		startGame(currentLevel);
	}

	/**
	 * @param args
	 *            Ein Array mit den Namen der Städte, mit denen im aktuellen
	 *            Level gespielt wird.
	 */
	public static void setNames(String[] args) {
		/**
		 * TextViews mit den aktuellen Städtenamen befüllen
		 */
		city1.setText(args[0]);
		city2.setText(args[1]);
		city3.setText(args[2]);
		city4.setText(args[3]);

		/**
		 * Kontroll-Array mit den Städtenamen befüllen
		 */
		correctValues = new String[4][2];

		for (int i = 0; i < correctValues.length; i++) {
			correctValues[i][0] = args[i];
		}
	}

	/**
	 * @param args
	 *            Die Citycodes der Städte, mit denen im aktuellen Level
	 *            gespielt wird.
	 */
	public static void setTemperaturesWetter(String[] args) {
		/**
		 * TextViews mit den Temperaturen befüllen. Jede Abfrage erfolg in einem
		 * eigenen asynchronen Thread.
		 */
		try {
			/**
			 * Fülle das Kontroll-Array mit den Temperaturwerten, setze den
			 * Fortschrittsbalken.
			 */
			for (int i = 0; i < correctValues.length; i++) {
				correctValues[i][1] = getNextTemp_Wetter(args[i]);
			}
			/**
			 * Setzen der Werte für die Temperatur-TextViews in zufällige
			 * Positionen
			 */
			int[] newMixedTemperatures = mixTemperatures();
			temp1.setText(Integer.toString(newMixedTemperatures[0]));
			temp2.setText(Integer.toString(newMixedTemperatures[1]));
			temp3.setText(Integer.toString(newMixedTemperatures[2]));
			temp4.setText(Integer.toString(newMixedTemperatures[3]));

		} catch (InterruptedException e) {
			/**
			 * Im Falle eines Fehlers beim ersten Feld anzeigen, dass der Aufruf
			 * nicht geklappt hat.
			 */
			temp1.setText("Fehler beim API-Aufruf");
			e.printStackTrace();
		} catch (ExecutionException e) {
			temp1.setText("Fehler beim API-Aufruf");
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            Zweidimensionales Array mit den Längen-und Breitengraden der
	 *            Städte für das aktuelle Level
	 */
	public static void setTemperaturesForecast(String[][] args) {
		try {
			/**
			 * Holen der Temperaturen
			 */
			for (int i = 0; i < correctValues.length; i++) {
				correctValues[i][1] = getNextTemp_Forecast(args[i][0],
						args[i][1]);
			}
			/**
			 * Setzen der Werte für die Temperatur-TextViews in zufällige
			 * Positionen
			 */
			int[] newMixedTemperatures = mixTemperatures();
			temp1.setText(Integer.toString(newMixedTemperatures[0]));
			temp2.setText(Integer.toString(newMixedTemperatures[1]));
			temp3.setText(Integer.toString(newMixedTemperatures[2]));
			temp4.setText(Integer.toString(newMixedTemperatures[3]));
		} catch (InterruptedException e) {
			/**
			 * Im Falle eines Fehlers beim ersten Feld anzeigen, dass der Aufruf
			 * nicht geklappt hat.
			 */
			temp1.setText("Fehler beim API-Aufruf");
			e.printStackTrace();
		} catch (ExecutionException e) {
			temp1.setText("Fehler beim API-Aufruf");
			e.printStackTrace();
		}
	}

	/**
	 * @return Das Array indem die Temperaturen in zufälliger Reihenfolge
	 *         gemischt sind.
	 */
	private static int[] mixTemperatures() {
		/**
		 * Erstellen eines neuen Arrays, in das zunächst die Original-Daten
		 * gespeichert und dann gemixt werden.
		 */
		int[] mixedTemperatures = new int[correctValues.length];
		int random;
		int storeData;
		Random r = new Random();

		for (int i = 0; i < correctValues.length; i++)
			mixedTemperatures[i] = Integer.parseInt(correctValues[i][1]);

		/**
		 * 20x: Erzeugen einer Zufallszahl im Indexbereich 0 - 3 und Tausch der
		 * Werte.
		 */
		for (int i = 0; i < correctValues.length; i++) {
			random = r.nextInt(correctValues.length);
			storeData = mixedTemperatures[i];
			mixedTemperatures[i] = mixedTemperatures[random];
			mixedTemperatures[random] = storeData;
		}
		return mixedTemperatures;
	}

	/**
	 * @param city
	 *            Der Name der Stadt
	 * @param temp
	 *            Die Temperatur, auf die der Benutzer die Stadt gezogen hat.
	 * @return true, wenn die Zuordnung stimmt; false wenn nicht.
	 */
	public static boolean checkValues(String city, int temp) {
		/**
		 * Prüfe, ob das übergebene City-Temperatur-Paar im Array vorkommt.
		 */
		for (int i = 0; i < 4; i++) {
			if (city.equals(correctValues[i][0])) {
				if (temp == Integer.parseInt(correctValues[i][1])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param lvl
	 *            Das Level, das der Benutzer als erstes ausgewählt hat. Weiter
	 *            Level werden automatisch erzeugt und müssen nicht extra
	 *            gewählt werden.
	 */
	public static void startGame(int lvl) {
		Log.i("SpielActivity", "Start Game. currentLevel: " + currentLevel
				+ " nextLevel " + nextLevel);
		switch (lvl) {
		case 1:
			setNames(firstLevelNames);
			if (wetter) {
				setTemperaturesWetter(firstLevelCodes);
			} else {
				setTemperaturesForecast(firstLevelCodesF);
			}

			break;
		case 2:
			setNames(secondLevelNames);
			if (wetter) {
				setTemperaturesWetter(secondLevelCodes);
			} else {
				setTemperaturesForecast(secondLevelCodesF);
			}
			break;
		case 3:
			setNames(thirdLevelNames);
			if (wetter) {
				setTemperaturesWetter(thirdLevelCodes);
			} else {
				setTemperaturesForecast(thirdLevelCodesF);
			}
			break;
		case 4:
			setNames(fourthLevelNames);
			if (wetter) {
				setTemperaturesWetter(fourthLevelCodes);
			} else {
				setTemperaturesForecast(fourthLevelCodesF);
			}
			break;
		case 5:
			setNames(fifthLevelNames);
			if (wetter) {
				setTemperaturesWetter(fifthLevelCodes);
			} else {
				setTemperaturesForecast(fifthLevelCodesF);
			}
			break;
		case 6:
			setNames(sixthLevelNames);
			if (wetter) {
				setTemperaturesWetter(sixthLevelCodes);
			} else {
				setTemperaturesForecast(sixthLevelCodesF);
			}
			break;
		case 7:
			setNames(seventhLevelNames);
			if (wetter) {
				setTemperaturesWetter(seventhLevelCodes);
			} else {
				setTemperaturesForecast(seventhLevelCodesF);
			}
			break;
		case 8:
			setNames(eighthLevelNames);
			if (wetter) {
				setTemperaturesWetter(eighthLevelCodes);
			} else {
				setTemperaturesForecast(eighthLevelCodesF);
			}
			break;
		case 9:
			setNames(ninthLevelNames);
			if (wetter) {
				setTemperaturesWetter(ninthLevelCodes);
			} else {
				setTemperaturesForecast(ninthLevelCodesF);
			}
			break;
		default:
			break;
		}

	}

	public static void restartGame() {
		Log.i("SpielActivity", "Restart Game. Current Level: " + currentLevel);
		/**
		 * Mische erneut die Temperaturen und setze die Felder.
		 */
		int[] mixedTemperatures = mixTemperatures();
		temp1.setText(Integer.toString(mixedTemperatures[0]));
		temp2.setText(Integer.toString(mixedTemperatures[1]));
		temp3.setText(Integer.toString(mixedTemperatures[2]));
		temp4.setText(Integer.toString(mixedTemperatures[3]));

		/**
		 * Setze die Städte-Namen und mache die Felder wieder sichtbar (wurden
		 * im Drop-Handler unsichtbar gemacht!).
		 */
		city1.setText(correctValues[0][0]);
		city1.setVisibility(View.VISIBLE);
		city2.setText(correctValues[1][0]);
		city2.setVisibility(View.VISIBLE);
		city3.setText(correctValues[2][0]);
		city3.setVisibility(View.VISIBLE);
		city4.setText(correctValues[3][0]);
		city4.setVisibility(View.VISIBLE);
	}

	/**
	 * Die Methode wird verwendet, um die neuen Temperaturen von wetter.com
	 * zwischenzuspeichern. Die Operation wurde ausgelagert, um auch vom
	 * Runnable-Objekt aus darauf zugreifen zu können. Sie muss im UI Thread
	 * durchgeführt werden, da vom Runnable-Objekt aus kein Async Thread
	 * gestartet werden kann.
	 * 
	 * @param cityCode
	 *            CityCode, für den die neue Temperatur geholt werden soll.
	 * @return ganzzahlige Temperatur als String
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static String getNextTemp_Wetter(String cityCode)
			throws InterruptedException, ExecutionException {
		return new WetterApiThread().execute(cityCode).get();
	}

	/**
	 * Die Methode wird verwendet, um die neuen Temperaturen von forecast.io
	 * zwischenzuspeichern. Die Operation wurde ausgelagert, um auch vom
	 * Runnable-Objekt aus darauf zugreifen zu können. Sie muss im UI Thread
	 * durchgeführt werden, da vom Runnable-Objekt aus kein Async Thread
	 * gestartet werden kann.
	 * 
	 * @param latitude
	 *            Breitengrad der Stadt
	 * @param longitude
	 *            Längengrad der Stadt
	 * @return ganzzahlige Temperatur als String
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static String getNextTemp_Forecast(String latitude, String longitude)
			throws InterruptedException, ExecutionException {
		return new ForecastApiThread().execute(latitude, longitude).get();
	}
	
	/**
	 * Wird von der SpielActivity vom Benutzer durch Drücken der Hardware-Zurück-Taste zurück zur Level-Activity oder auf den 
	 */
	@Override
	protected void onPause(){
		if(this.isFinishing()){
			mp.stop();
		}
		super.onPause();
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	}
}