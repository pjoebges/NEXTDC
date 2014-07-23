package util;

/**
 * In dieser Klasse sind u.a. Konstanten für die City-Codes enthalten. Diese
 * kann man abfragen, wenn man auf der normalen Endnutzer-Seite www.wetter.com
 * nach einer Stadt sucht und bei Anzeige der Wetterseite dann den Dateinamen
 * der HTML-Datei nimmt und das ".html" entfernt.
 * 
 * Beispiel:
 * http://www.wetter.com/wetter_aktuell/aktuelles_wetter/deutschland/bruchsal
 * /DE0001583.html -- City-Code für "Bruchsal" lautet also "DE0001583".
 */
public interface IGlobaleKonstanten {

	/**
	 * API-Key
	 */
	public static final String API_KEY = "b88cf41a7034b574565cb11d7eca1c44";

	/**
	 * Projektname
	 */
	public static final String PROJEKT_NAME = "aweatherg";

	public static final String CITY_CODE_KARLSRUHE = "DE0005309";

	/**
	 * Arrays mit den Städtenamen für die einzelnen Level
	 */

	public static final String[] firstLevelNames = { "Reykjavík", "Kairo",
			"München", "Stockholm" };
	public static final String[] secondLevelNames = { "Moskau", "Tokio",
			"Buenos Aires", "Vancouver" };
	public static final String[] thirdLevelNames = { "Sydney", "Peking",
			"Los Angeles", "Paris" };
	public static final String[] fourthLevelNames = { "London", "Madrid",
			"Helsinki", "Athen" };
	public static final String[] fifthLevelNames = { "Oslo", "Berlin", "Prag",
			"Amsterdam" };
	public static final String[] sixthLevelNames = { "Wien", "Maastricht",
			"Basel", "Dublin" };
	public static final String[] seventhLevelNames = { "Berlin", "Hamburg",
			"Karlsruhe", "Düsseldorf", };
	public static final String[] eighthLevelNames = { "Bremen", "Dresden",
			"Leipzig", "Heidelberg" };
	public static final String[] ninthLevelNames = { "Köln", "Essen",
			"Hannover", "Stuttgart" };

	/**
	 * Arrays mit den Citycodes für die einzelnen Level - wetter.com
	 */

	public static final String[] firstLevelCodes = { "ISXY00001", "EG0QH0001",
			"DE0006515", "SE0ST0012" };
	public static final String[] secondLevelCodes = { "RU0MC0002", "JP0TY0011",
			"AR0DF0001", "CA6173331" };
	public static final String[] thirdLevelCodes = { "AU0NS0062", "CN0BJ0003",
			"US5368361", "FR0IF0356" };
	public static final String[] fourthLevelCodes = { "GB0KI0101", "ES0MA0079",
			"FI0ES0005", "GR0AT0001" };
	public static final String[] fifthLevelCodes = { "NO0OS0002", "DE0001020",
			"CZ0PR0001", "NL0NH0013" };
	public static final String[] sixthLevelCodes = { "ATAT10678", "NL0LI0095",
			"CH0CH0260", "IE0DN0001" };
	public static final String[] seventhLevelCodes = { "DE0001020",
			"DE0004130", "DE0005309", "DE0001855" };
	public static final String[] eighthLevelCodes = { "DE0001516", "DE0002265",
			"DE0006194", "DE0004329" };
	public static final String[] ninthLevelCodes = { "DE0005156", "DE0002737",
			"DE0004160", "DE0010287" };

	/**
	 * Arrays mit den Citycodes für die einzelnen Level - forecast.io
	 */

	public static final String[] firstLevelCodesF[] = {
			{ "64.1474", "-21.9340" }, { "30.0500", "31.2486" },
			{ "48.1364", "11.5775" }, { "59.3323", "18.0629" } };
	public static final String[] secondLevelCodesF[] = {
			{ "55.7570", "37.6150" }, { "33.1817", "-102.5855" },
			{ "-34.6085", "-58.3735" }, { "45.6322", "-122.6716" } };
	public static final String[] thirdLevelCodesF[] = {
			{ "-33.8696", "151.2070" }, { "39.9060", "116.3879" },
			{ "34.0535", "-118.2453" }, { "48.8569", "2.3412" } };
	public static final String[] fourthLevelCodesF[] = {
			{ "51.5063", "-0.1271" }, { "40.4203", "-3.7058" },
			{ "60.1712", "24.9327" }, { "37.9761", "23.73642" } };
	public static final String[] fifthLevelCodesF[] = {
			{ "59.9123", "10.7500" }, { "52.5161", "13.3770" },
			{ "50.0791", "14.4332" }, { "52.3731", "4.8933" } };
	public static final String[] sixthLevelCodesF[] = {
			{ "48.2025", "16.3688" }, { "50.8499", "5.6883" },
			{ "47.5488", "7.5878" }, { "53.3481", "-6.2483" } };
	public static final String[] seventhLevelCodesF[] = {
			{ "52.5161", "13.3770" }, { "53.5533", "9.9925" },
			{ "49.0108", "8.4087" }, { "51.2156", "6.7760" } };
	public static final String[] eighthLevelCodesF[] = {
			{ "53.0751", "8.8047" }, { "51.0536", "13.7408" },
			{ "51.3452", "12.3859" }, { "49.4135", "8.7081" } };
	public static final String[] ninthLevelCodesF[] = {
			{ "50.9417", "6.9552" }, { "51.4518", "7.0106" },
			{ "52.3723", "9.7381" }, { "48.7677", "9.1719" } };
}
