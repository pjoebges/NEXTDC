package util;

import android.os.AsyncTask;
import android.util.Log;

public class ForecastApiThread extends AsyncTask<String, Integer, String>
		implements IGlobaleKonstanten {

	@Override
	protected String doInBackground(String... arg0) {
		try {
			/**
			 * Da beim Testen keine Exception von forecast.io aufgetreten sind,
			 * reicht hier das einmalige Aufrufe der API.
			 */
			return ForecastApiCall.wetterAbfragen(arg0[0], arg0[1]);
		} catch (Exception e) {
			return "0";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i("ForecastApiThread", "ApiCall erfolgreich. Temperatur: " + result);
	}

}
