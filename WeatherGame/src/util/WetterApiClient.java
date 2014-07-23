package util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import util.IGlobaleKonstanten;
import util.URLBuilder;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class WetterApiClient implements IGlobaleKonstanten {

	private static String callWebAPI(String cityCode) throws Exception {

		/**
		 *  URL vorbereiten
		 */
		String urlString = URLBuilder.getUrlFuerWetterabfrage(cityCode);
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
	public static String wetterAbfragen(String cityCode) throws Exception {

		/**
		 * Aufruf der Web-API
		 */
		String jsonString = callWebAPI(cityCode);

		/**
		 * JSOn parsen mit "JSOn.simple"
		 */
		JSONParser parser = new JSONParser();
		JSONObject jsonObjectGanzesDokument = (JSONObject) parser
				.parse(jsonString);

		JSONObject jsonObjectCity = (JSONObject) jsonObjectGanzesDokument
				.get("city");

		JSONObject jsonObjectForecast = (JSONObject) jsonObjectCity
				.get("forecast");
		
		if (jsonObjectForecast.size() != 1) {
			throw new Exception("Anzahl Vorhersagetage != 1");
		}

		String datumsString = (String) jsonObjectForecast.keySet().toArray()[0];
		JSONObject jsonObjectEinTag = (JSONObject) jsonObjectForecast
				.get(datumsString);
		/**
		 * Um eine NumberFormatException im weiteren Programmverlauf auszuschließen,
		 * muss hier sichergestellt werden, dass nur ganzzalige Temperaturen
		 * zurück gegeben werden. Deshalb werden die Kommazahlen abgeschnitten.
		 */
		String temperature = (String) jsonObjectEinTag.get("tx");
		if(temperature.contains(".")){
			int pointIndex = temperature.indexOf(".");
			return temperature.substring(0, pointIndex-1);
		}else{
			return temperature;
		}

	}
};
