package util;
import java.security.MessageDigest;
import java.util.Formatter;


/**
 * Klasse liefert URL f�r Wetter-Abfrage von openweather-API von wetter.com zur�ck.
 * Siehe auch Doku: http://www.wetter.com/apps_und_mehr/website/api/dokumentation/
 *
 * Beispiel-URL f�r Abfrage f�r Karlsruhe (kann direkt in Browser eingegeben werden):
 * http://api.wetter.com/forecast/weather/city/DE0005309/project/meinprivaterwettercomapizugriff/cs/37457c2fc86650e911604fc256cb7305
 */
public class URLBuilder implements IGlobaleKonstanten {

	/** String mit drei Platzhaltern f�r die Abfrage-URL. 
	 * Platzhalter 1: CITY_CODE; Platzhalter 2: Projektname; Plathalter 3: Pr�fsumme. 
	 * Wenn das "/output/json" am Ende weggelassen wird, dann liefert die Anfrage XML statt JSon.
	 */
	protected static final String URL_TEMPLATE_STRING = 
			"http://api.wetter.com/forecast/weather/city/%s/project/%s/cs/%s/output/json";
	
	
	/**
	 * Berechnet MD5-Pr�fsumme f�r bestimmten City-Code. In den Hash flie�en auch der Name des Projektes
	 * und der API-Key ein.
	 * 
	 * @param cityCode Code der Stadt, f�r die die Abfrage sein soll
	 * @return MD5-Summe, die an URL als "Authentifizierung" angeh�ngt werden muss.
	 *         Hex-Buchstaben d�rfen nicht Gro�buchstaben sein.
	 */ 
	protected static String calcMD5Sum(String cityCode) throws Exception {
		
		// *** String zusammenbauen, der verhasht werden soll ***
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(PROJEKT_NAME);
		stringBuffer.append(API_KEY);
		stringBuffer.append(cityCode);
		
		String stringToBeHashed = stringBuffer.toString();
				
		// *** Verhashung durchf�hren ***
		MessageDigest hashGenerator = MessageDigest.getInstance("MD5");
		hashGenerator.update(stringToBeHashed.getBytes());
		byte[] hashEingangsdatei = hashGenerator.digest();
		
		// *** Hash als String darstellen ***
		stringBuffer = new StringBuffer();
	    Formatter formatter = new Formatter(stringBuffer);
	    for (byte b : hashEingangsdatei)
	    	formatter.format("%02x", b); // x=Hexziffern mit Kleinbuchstaben, "02": zweistellig mit f�hrender Null zum Auff�llen
	    formatter.close();
		
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * Liefert den String, der f�r die Wetterabfrage einer bestimmten
	 * Stadt notwendig ist. Beinhaltet auch Projektname und MD5-Summe.
	 * 
	 * @param cityCode  Code f�r eine bestimmte Stadt, z.B. "DE0005309"
     *                    f�r Karlsruhe. 
	 * @return URL, mit der API aufzurufen ist
	 */
	public static String getUrlFuerWetterabfrage(String cityCode) throws Exception {

		if (cityCode.trim().length() == 0)
			throw new Exception("Interner Fehler: City-Code darf nicht leer sein.");
		
		String pruefsumme = calcMD5Sum(cityCode);
		
		// *** Platzhalter im Format-String ausf�llen ***
		StringBuffer sb = new StringBuffer();
		Formatter formatter = new Formatter(sb);
		formatter.format(URL_TEMPLATE_STRING, cityCode, PROJEKT_NAME, pruefsumme);
		formatter.close();
		
		return sb.toString();
	}

}
