package app.pramodh;

/*
 * This class stores the weather details for the given location
 */
public class Weather {

    private static String city; // City is going to be the same for all objects of this class.
    private String condition;
    private String temp;
	private String date;
    private String high;
    private String low;
    public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		Weather.city = city;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
