package app.pramodh;
// This class stores 4 objects for Weather that has current weather and forecast for next 3 days
public class VaanilaiController {

	private static Weather[] weather;
	
	VaanilaiController(){
		setWeather(new Weather[4]);
	}

	public static Weather[] getWeather() {
		return weather;
	}

	public static void setWeather(Weather[] weather) {
		VaanilaiController.weather = weather;
	}
	
}
