package util;
import java.security.MessageDigest;
import java.util.Formatter;


/**
 * Klasse liefert URL für Wetter-Abfrage von openweather-API von wetter.com zurück.
 * Siehe auch Doku: http://www.wetter.com/apps_und_mehr/website/api/dokumentation/
 *
 * Beispiel-URL für Abfrage für Karlsruhe (kann direkt in Browser eingegeben werden):
 * http://api.wetter.com/forecast/weather/city/DE0005309/project/meinprivaterwettercomapizugriff/cs/37457c2fc86650e911604fc256cb7305
 */
public class URLBuilder implements IGlobaleKonstanten {

	/** String mit drei Platzhaltern für die Abfrage-URL. 
	 * Platzhalter 1: CITY_CODE; Platzhalter 2: Projektname; Plathalter 3: Prüfsumme. 
	 * Wenn das "/output/json" am Ende weggelassen wird, dann liefert die Anfrage XML statt JSon.
	 */
	protected static final String URL_TEMPLATE_STRING = 
			"http://api.wetter.com/forecast/weather/city/%s/project/%s/cs/%s/output/json";
	
	
	/**
	 * Berechnet MD5-Prüfsumme für bestimmten City-Code. In den Hash fließen auch der Name des Projektes
	 * und der API-Key ein.
	 * 
	 * @param cityCode Code der Stadt, für die die Abfrage sein soll
	 * @return MD5-Summe, die an URL als "Authentifizierung" angehängt werden muss.
	 *         Hex-Buchstaben dürfen nicht Großbuchstaben sein.
	 */ 
	protected static String calcMD5Sum(String cityCode) throws Exception {
		
		// *** String zusammenbauen, der verhasht werden soll ***
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(PROJEKT_NAME);
		stringBuffer.append(API_KEY);
		stringBuffer.append(cityCode);
		
		String stringToBeHashed = stringBuffer.toString();
				
		// *** Verhashung durchführen ***
		MessageDigest hashGenerator = MessageDigest.getInstance("MD5");
		hashGenerator.update(stringToBeHashed.getBytes());
		byte[] hashEingangsdatei = hashGenerator.digest();
		
		// *** Hash als String darstellen ***
		stringBuffer = new StringBuffer();
	    Formatter formatter = new Formatter(stringBuffer);
	    for (byte b : hashEingangsdatei)
	    	formatter.format("%02x", b); // x=Hexziffern mit Kleinbuchstaben, "02": zweistellig mit führender Null zum Auffüllen
	    formatter.close();
		
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * Liefert den String, der für die Wetterabfrage einer bestimmten
	 * Stadt notwendig ist. Beinhaltet auch Projektname und MD5-Summe.
	 * 
	 * @param cityCode  Code für eine bestimmte Stadt, z.B. "DE0005309"
     *                    für Karlsruhe. 
	 * @return URL, mit der API aufzurufen ist
	 */
	public static String getUrlFuerWetterabfrage(String cityCode) throws Exception {

		if (cityCode.trim().length() == 0)
			throw new Exception("Interner Fehler: City-Code darf nicht leer sein.");
		
		String pruefsumme = calcMD5Sum(cityCode);
		
		// *** Platzhalter im Format-String ausfüllen ***
		StringBuffer sb = new StringBuffer();
		Formatter formatter = new Formatter(sb);
		formatter.format(URL_TEMPLATE_STRING, cityCode, PROJEKT_NAME, pruefsumme);
		formatter.close();
		
		return sb.toString();
	}

}
