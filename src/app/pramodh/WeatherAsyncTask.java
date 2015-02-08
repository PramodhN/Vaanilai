package app.pramodh;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
/*
 * This class reads a zip code, finds the weather forecast in that region and updates the 'Weather' class objects
 */
public class WeatherAsyncTask extends AsyncTask<String, Void, Void>{
	
	Context context;
	public WeatherAsyncTask(Context context) {
		super();
		this.context = context;
	}
	Weather[] weather;
	ProgressDialog pDialog;
	AlertDialog failureDialog;
	
	private static final String WEATHER_URL_ZIP = "http://weather.yahooapis.com/forecastrss?p=";
	private static final String WEATHER_URL_CITY = "http://weather.yahooapis.com/forecastrss?q=";
	private static final String NETWORK_ERROR_MESSAGE = "1. Check whether you device is connected to the internet.\n2. Check whether location services are enabled in your device.";
	private static final CharSequence INVALID_INPUT_ERROR_MESSAGE = "Entered location cannot be resolved. Please try again.";
    @SuppressWarnings("deprecation")
	@Override
    protected void onPreExecute(){
    	// Show a progress dialog while retrieving weather forecast
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Getting forecast...");
		pDialog.show();
		// Initializing an alert dialog that will be shown in case of any failure in retrieval of weather forecast
		failureDialog = new AlertDialog.Builder(context).create();
		failureDialog.setTitle("Oops...Something went wrong");
    }	
	
	@Override
	protected Void doInBackground(String... params) {
		try{
			URL url;
			// Set URL based on whether input is city name or zip code
			if(!isNumeric(params[0])){
				url = new URL(WEATHER_URL_CITY + params[0]);
			} else {
				url = new URL(WEATHER_URL_ZIP + params[0]);
			}
	        URLConnection conn = url.openConnection();
	        weather = new Weather[4];
	        setWeatherDetails(conn.getInputStream()); // Set the contents of weather[] objects
	        VaanilaiController.setWeather(weather);
    		pDialog.dismiss(); // Weather is retrieved, close the progress bar
    		((Activity) context).finish();
    		context.startActivity(new Intent(context, MainActivity.class)); // Open the main activity with weather updates
		} catch(UnknownHostException e){
			// In case network is not available, show error message and exit application.
			failureDialog.setMessage(NETWORK_ERROR_MESSAGE);
    		pDialog.dismiss();
    		((Activity) context).runOnUiThread(new Runnable() {
    	        @SuppressWarnings("deprecation")
				public void run() {
    	    		failureDialog.setButton("OK",  new DialogInterface.OnClickListener() {
    	    			@Override
    	    			public void onClick(DialogInterface arg0, int arg1) {
    	    				failureDialog.dismiss();
    	    	        	System.exit(0);
    	    			}});
    	        	failureDialog.show();
    	        }
    	    });
			e.printStackTrace();
		} catch(NullPointerException e){
			// In case location name or zip code is invalid, show an alert message 
			failureDialog.setMessage(INVALID_INPUT_ERROR_MESSAGE);
    		pDialog.dismiss();
    		((Activity) context).runOnUiThread(new Runnable() {
    	        @SuppressWarnings("deprecation")
				public void run() {
    	        	failureDialog.setButton("OK",  new DialogInterface.OnClickListener() {
    	    			@Override
    	    			public void onClick(DialogInterface arg0, int arg1) {
    	    				failureDialog.dismiss();
    	    			}});
    	        	failureDialog.show();
    	        }
    	    });
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(String params){
		
   }

	/*
	 * This fuction checks if the given input string has just numbers or not
	 */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    int d = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}	
	
	/*
	 * This method reads an XML file input stream and retrieves the weather forecast from it
	 */
	public void setWeatherDetails(InputStream inputStream) throws Exception {
    	Document doc = parseXML(inputStream);
    	float highF,highC,lowF,lowC;
    	
    	// weather[0] has current weather status
        weather[0]= new Weather();
        Node location = doc.getElementsByTagName("yweather:location").item(0);
        weather[0].setCity(location.getAttributes().item(0).getNodeValue());
        Node condition = doc.getElementsByTagName("yweather:condition").item(0);
        weather[0].setCondition(condition.getAttributes().item(0).getNodeValue());
        weather[0].setTemp(condition.getAttributes().item(2).getNodeValue());
        weather[0].setDate(condition.getAttributes().item(3).getNodeValue());
        
        // weather[1],weather[2] and weather[3] has the forecast for the next 3 days
        NodeList forecast = doc.getElementsByTagName("yweather:forecast");
        for(int i=1;i<4;i++){
        	weather[i] = new Weather();
        	highF = Float.parseFloat(forecast.item(i-1).getAttributes().item(3).getNodeValue().toString());
        	highC = (((highF)-32)*5)/9;
        	lowF = Float.parseFloat(forecast.item(i-1).getAttributes().item(2).getNodeValue().toString());
        	lowC = (((lowF)-32)*5)/9;
        	weather[i].setHigh(highF+""+(char) 0x00B0+"F("+String.format("%.2f", highC)+(char) 0x00B0+"C)");
        	weather[i].setLow(lowF+""+(char) 0x00B0+"F("+String.format("%.2f", lowC)+(char) 0x00B0+"C)");
        	weather[i].setCondition(forecast.item(i-1).getAttributes().item(4).getNodeValue());
        	weather[i].setDate(forecast.item(i-1).getAttributes().item(1).getNodeValue());
        }        
    }
	
	/*
	 * This method is used to parse the given XML input stream into a Document object
	 */
    private Document parseXML(InputStream stream) throws Exception {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        try{
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
            doc = objDocumentBuilder.parse(stream);
        }catch(Exception ex){
            throw ex;
        }       
        return doc;
    }  
}
