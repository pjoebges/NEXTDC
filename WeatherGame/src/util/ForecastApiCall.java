package util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class ForecastApiCall {

	private static String callWebAPI(String latitude, String longitude)
			throws Exception {

		/**
		 * URL vorbereiten
		 */
		String urlString = "https://api.forecast.io/forecast/8c57dddc586ff0fcb356aed265960007/"
				+ latitude + "," + longitude;
		GenericUrl url = new GenericUrl(urlString);

		/**
		 * Vorbereitung der "google-hhtp-client"-Library
		 */
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport
				.createRequestFactory();

		/**
		 * Eigentliche Abfrage über HTTP durchführen
		 */
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse httpResponse = request.execute();

		return httpResponse.parseAsString();
	}

	public static String wetterAbfragen(String latitude, String longitude)
			throws Exception {

		/**
		 * Aufruf der Web-API
		 */
		String jsonString = callWebAPI(latitude, longitude);

		/**
		 * JSOn parsen mit "JSOn.simple"
		 */
		JSONParser parser = new JSONParser();
		JSONObject jsonObjectGanzesDokument = (JSONObject) parser
				.parse(jsonString);

		JSONObject jsonObjectCurrently = (JSONObject) jsonObjectGanzesDokument
				.get("currently");
		double temperatureD = 0.0;
		int temperatureI = 0;
		try {
			/**
			 * Die API liefert Double-Werte zurück.
			 */
			temperatureD = (Double) jsonObjectCurrently.get("temperature");
		} catch (Exception e) {
			Log.i("ForecastApiCall", "Keinen Wert für Temperatur gefunden.");
			try {
				temperatureI = (Integer) jsonObjectCurrently.get("temperature");
				return temperatureI + "";
			} catch (Exception e1) {
				return temperatureI + "";
			}
		}
		String temp = temperatureD + "";
		/**
		 * Um Kommawerte abzuschneiden, muss geschaut werden, wo der "." in dem
		 * Rückgabewert der API ist.
		 */
		temp = fahrenheitToCelsius(temp);
		int i = temp.indexOf(".");
		temp = temp.substring(0, i);
		return temp;
	}

	/**
	 * 
	 * @param temp
	 *            Temperatur in Fahrenheit
	 * @return Temperatur in Celsius
	 */
	private static String fahrenheitToCelsius(String temp) {
		/**
		 * Formel zur Umrechnung nach: http://www.celsius-fahrenheit.de/
		 */
		try {
			double d = Double.parseDouble(temp);
			d = (d - 32);
			d = d * 5;
			d = d / 9;
			return d + "";
		} catch (Exception e) {
			Log.e("ForecastApiCall",
					"Fehler beim Umrechnen von Fahrenheit nach Celsius");
			return "0";
		}
	}
}
