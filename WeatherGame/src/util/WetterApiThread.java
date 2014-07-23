package util;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

public class WetterApiThread extends AsyncTask<String, Integer, String> implements
		IGlobaleKonstanten {

	boolean firstCall = true;

	@Override
	protected String doInBackground(String... arg0) {
		/**
		 * Versuche, die API aufzurufen. Fliegt eine Exception, warte 250ms und
		 * versuche es 2x erneut.
		 */
		try {
			return WetterApiClient.wetterAbfragen(arg0[0]);
		} catch (Exception e1) {
			Log.w("ApiThread", "1. API-Aufruf fehlgeschlagen");
			/**
			 * Der Vorteil von SystemClock.sleep(long ms) gegenüber
			 * Thread.sleep(long ms) ist, dass die SystemClock nicht frühzeitig
			 * durch eine InterruptedException unterbrochen werden kann. Info
			 * von:
			 * http://developer.android.com/reference/android/os/SystemClock.html
			 */
			SystemClock.sleep(250);
			try {
				return WetterApiClient.wetterAbfragen(arg0[0]);
			} catch (Exception e2) {
				Log.w("ApiThread", "2. API-Aufruf fehlgeschlagen");
				SystemClock.sleep(250);
				try {
					return WetterApiClient.wetterAbfragen(arg0[0]);
				} catch (Exception e3) {
					Log.w("ApiThread", "3. API-Aufruf fehlgeschlagen");
				}
			}
			return "0";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i("WetterApiThread", "ApiCall erfolgreich. Temperatur: " + result);
	}

}
