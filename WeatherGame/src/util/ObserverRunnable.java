package util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.dhbw.wetterSpiel.MainActivity;
import com.dhbw.wetterSpiel.SpielActivity;
import com.dhbw.wetterSpiel.R;

public class ObserverRunnable implements Runnable, IGlobaleKonstanten {

	boolean loadNewData = true;
	SharedPreferences settings;

	@Override
	public void run() {
		if ((SpielActivity.correctAssignments % 5) == 0) {
			settings = MainActivity.settings;
			SharedPreferences.Editor editor = settings.edit();
			Integer level = new Integer(SpielActivity.currentLevel);
			editor.putBoolean(level.toString(), true);
			editor.commit();
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
					SpielActivity.context);
			dialogBuilder.setMessage(R.string.level_beendet);
			dialogBuilder.setPositiveButton(R.string.level_naechstes,
					new OnClickListener() {

						/**
						 * Setze das nächste Level und starte erneut.
						 */
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							SpielActivity.currentLevel = SpielActivity.nextLevel;
							/**
							 * Wenn das Ende des Spiels erreicht ist, fange
							 * wieder bei Level 1 an.
							 */
							if (SpielActivity.nextLevel == 9) {
								SpielActivity.nextLevel = 1;
								Toast toast = Toast.makeText(
										SpielActivity.context,
										R.string.level_durchgespielt,
										Toast.LENGTH_LONG);
								toast.show();
							} else {
								/**
								 * Wenn nicht, zähle einen hoch.
								 */
								SpielActivity.nextLevel++;
							}
							/**
							 * Setze die neuen Werte in der neuen SpielActivity.
							 * Dann starte das Spiel erneut mit den neuen Daten.
							 * Setze außerdem loadNewData auf true, damit
							 * parallel zum Spiel bereits die nächsten Daten
							 * geladen werden.
							 */
							SpielActivity.correctValues = SpielActivity.nextCorrectValues;
							SpielActivity.nextCorrectValues = new String[4][2];
							loadNewData = true;
							SpielActivity.restartGame();
						}
					});

			/**
			 * Zeige den entworfenen Dialog an und zähle die correctAssignments
			 * hoch (wg. "% 5")
			 */
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
			SpielActivity.correctAssignments++;
		} else {
			/**
			 * Wenn neue Daten geladen werden sollen, schaue für welches Level.
			 */
			if (loadNewData) {
				Log.i("ObserverRunnable", "Lade neue Daten für Level "
						+ SpielActivity.nextLevel);
				String[] cityNames = new String[4];
				String[] cityCodes = new String[4];
				String[] cityCodesF[] = new String[4][2];

				switch (SpielActivity.nextLevel) {
				case 1:
					cityNames = firstLevelNames;
					cityCodes = firstLevelCodes;
					cityCodesF = firstLevelCodesF;
					break;
				case 2:
					cityNames = secondLevelNames;
					cityCodes = secondLevelCodes;
					cityCodesF = secondLevelCodesF;
					break;
				case 3:
					cityNames = thirdLevelNames;
					cityCodes = thirdLevelCodes;
					cityCodesF = thirdLevelCodesF;
					break;
				case 4:
					cityNames = fourthLevelNames;
					cityCodes = fourthLevelCodes;
					cityCodesF = fourthLevelCodesF;
					break;
				case 5:
					cityNames = fifthLevelNames;
					cityCodes = fifthLevelCodes;
					cityCodesF = fifthLevelCodesF;
					break;
				case 6:
					cityNames = sixthLevelNames;
					cityCodes = sixthLevelCodes;
					cityCodesF = sixthLevelCodesF;
					break;
				case 7:
					cityNames = seventhLevelNames;
					cityCodes = seventhLevelCodes;
					cityCodesF = seventhLevelCodesF;
					break;
				case 8:
					cityNames = eighthLevelNames;
					cityCodes = eighthLevelCodes;
					cityCodesF = eighthLevelCodesF;
					break;
				case 9:
					cityNames = ninthLevelNames;
					cityCodes = ninthLevelCodes;
					cityCodesF = ninthLevelCodesF;
					break;
				default:
					Log.e("ObserverRunnable",
							"Fehler - SpielActivity.nextLevel > 9");
					break;
				}
				/**
				 * Setze erst die nächsten Namen...
				 */
				for (int i = 0; i < SpielActivity.nextCorrectValues.length; i++)
					SpielActivity.nextCorrectValues[i][0] = cityNames[i];

				try {
					/**
					 * ... und dann hole dir die nächsten 4 Temperatur-Werte.
					 */
					for (int i = 0; i < SpielActivity.nextCorrectValues.length; i++) {
						if (SpielActivity.wetter) {
							SpielActivity.nextCorrectValues[i][1] = SpielActivity
									.getNextTemp_Wetter(cityCodes[i]);
						} else {
							SpielActivity.nextCorrectValues[i][1] = SpielActivity
									.getNextTemp_Forecast(cityCodesF[i][0],
											cityCodesF[i][1]);
						}
					}

					/**
					 * Beim API-Aufruf wird immer "0" zurück gegeben, wenn es
					 * Fehler gab. Suche hier nach einer "0" als Temperatur und
					 * falls vorhanden, prüfe mit der anderen API, ob der Wert
					 * stimmt oder durch eine Exception zustande gekommen ist.
					 */
					for (int i = 0; i < SpielActivity.nextCorrectValues.length; i++) {
						if (SpielActivity.nextCorrectValues[i][1] == (0 + "")) {
							if (SpielActivity.wetter) {
								SpielActivity.nextCorrectValues[i][1] = SpielActivity
										.getNextTemp_Forecast(cityCodesF[i][0],
												cityCodesF[i][0]);
							} else {
								SpielActivity.nextCorrectValues[i][1] = SpielActivity
										.getNextTemp_Wetter(cityCodes[i]);
							}
						}
					}
				} catch (Exception e) {
					Log.e("ObserverRunnable",
							"Exception beim Thread-Aufruf zum Speichern der nächsten Temperaturen");
					e.printStackTrace();
				}
				/**
				 * Damit der Vorgang auch nur einmal durchgeführt wird, wird der
				 * Boolean-Wert auf "false" gesetzt.
				 */
				loadNewData = false;
				Log.i("ObserverRunnable", "new Data stored");
			}
		}
		/**
		 * Führe die Prüfung 5x in der Sekunde durch. Beim Testen hat sich
		 * herausgestellt, dass dieser Wert eine angemessene
		 * Reaktionsgeschwindigkeit des Programms darstellt und dabei nicht zu
		 * viel CPU-Zeit verbraucht.
		 */
		SpielActivity.handler.postDelayed(this, 200);
	}
}
