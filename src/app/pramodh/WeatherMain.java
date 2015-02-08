package app.pramodh;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/*
 * Starting activity for weather app.
 * Shows a splash screen until current location and the weather forcast for that location is found
 * If location services is switched off, it chooses 'Las Vegas' as default location
 */
@SuppressLint("NewApi") public class WeatherMain extends ActionBarActivity {

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
		
        // Hide the top action bar
        ActionBar actionBar = getActionBar();
		actionBar.hide();        
		
		try {
			
			// Get location (latitude and longitude) from network provider
			LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();

			Geocoder geocoder = new Geocoder(getBaseContext());
			boolean isLocationFound = false;
			
			// Get list of possible addresses in the retrieved location (10 addresses are obtained here)
	        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
	        for (Address address : addresses) {
	        	if(address.getLocality()!=null && address.getPostalCode()!=null){
	        		// Check if postal code is available in any of the addresses retrieved. If yes, execute the thread below with the postal code. 
	        		@SuppressWarnings("unused")
					AsyncTask<String, Void, Void> weatherAsync = new WeatherAsyncTask(WeatherMain.this).execute(address.getPostalCode());
	        		isLocationFound = true;
	        	    break;
	        	}
	        }
	        
	        // In case postal code was not found, go for default location - Las Vegas
	        if(!isLocationFound){
	        	@SuppressWarnings("unused")
				AsyncTask<String, Void, Void> weatherAsync = new WeatherAsyncTask(WeatherMain.this).execute("89101");
	        }
    	}
    	catch(Exception e){
        	@SuppressWarnings("unused")
        	// In case of any exceptions while finding location, choose the default location - Tempe
			AsyncTask<String, Void, Void> weatherAsync = new WeatherAsyncTask(WeatherMain.this).execute("89101");
    		e.printStackTrace();
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

  

}
