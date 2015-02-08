package app.pramodh;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * This is the main screen for the application.
 * It reads the weather details from VaanilaiController class and displays them in an order.
 * It has options to change the current weather scale between Celsius and Farenheit.
 * It has options to check weather forecasts for another region (entry can be name of city or zip code).
 */
@SuppressLint({ "NewApi", "DefaultLocale" }) public class MainActivity extends ActionBarActivity {
	
	Weather[] weather;
	TextView cityText, conditionText, tempText, dateText, forecasts, forecast1Date, forecast1High, forecast1Low, forecast1Condition, 
			forecast2Date, forecast2High, forecast2Low, forecast2Condition, forecast3Date, forecast3High, forecast3Low, forecast3Condition;
	LinearLayout conditionImg; // This variable updates the background image based on current weather conditions
	Button changeCity, changeTempUnits; 
	
	@SuppressLint({ "NewApi", "DefaultLocale" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		initializeViews();
		weather = VaanilaiController.getWeather();
		
		setWeatherBackgroundImage();
		setTextViewFontSizes(convertFromDp());
		setTextViewValues();
		
		// Listener for changing location
		changeCity.setOnClickListener(new View.OnClickListener() {
		    @SuppressWarnings("deprecation")
			public void onClick(View view) {
		    	final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		        alertDialog.setTitle("Change city");
		        final EditText cityChangeText = new EditText(MainActivity.this);
		        cityChangeText.setHint("Enter city or zipcode");
		        alertDialog.setView(cityChangeText);
		        alertDialog.setCanceledOnTouchOutside(true);
		        alertDialog.setButton("Go", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
                		String changeCityInput = cityChangeText.getText().toString();
						AsyncTask<String, Void, Void> weatherAsync = new WeatherAsyncTask(MainActivity.this).execute(changeCityInput);
	                }	
	            });
		        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                		alertDialog.dismiss();
	                    }
	            });
		        alertDialog.show();
		    }
		});
		
		// Listener to change temperature units between celsius and farenheit
		changeTempUnits.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(changeTempUnits.getText().toString().equals("Change to "+(char) 0x00B0+"C") || changeTempUnits.getText().toString().equals("Change to Celsius")){
					changeTempUnits.setText("Change to "+(char) 0x00B0+"F");
					fToC();
				}else if(changeTempUnits.getText().toString().equals("Change to "+(char) 0x00B0+"F")){
					changeTempUnits.setText("Change to "+(char) 0x00B0+"C");
					cToF();
				}
			}
		});
				
	}
	/*
	 * This function gets the current weather value in farenheit and converts it to celsius
	 * It then updates the temperature field in the activity with new value
	 */
	protected void fToC() {
		String farenheit = tempText.getText().toString();
		farenheit = farenheit.substring(0,farenheit.length()-2);
		float celcius = (((Float.parseFloat(farenheit)-32)*5)/9);
		tempText.setText(String.format("%.2f", celcius)+""+(char) 0x00B0+"C");
	}
	/*
	 * This function gets the current weather value in celsius and converts it to farenheit
	 * It then updates the temperature field in the activity with new value
	 */
	protected void cToF() {
		String celcius = tempText.getText().toString();
		celcius = celcius.substring(0,celcius.length()-2);
		float farenheit = (Float.parseFloat(celcius)*9/5)+32;
		tempText.setText(String.format("%.0f", farenheit)+""+(char) 0x00B0+"F");
	}
	/*
	 * This function sets the values for the text views from the weather objects in VaanilaiController class
	 */
	private void setTextViewValues() {
		cityText.setText(weather[0].getCity());
		conditionText.setText(weather[0].getCondition());
		tempText.setText(weather[0].getTemp()+""+(char) 0x00B0+"F");
		dateText.setText("Last recorded : " + weather[0].getDate());
		
		forecast1Date.setText(weather[1].getDate());
		forecast1Condition.setText(weather[1].getCondition());
		forecast1High.setText(weather[1].getHigh());
		forecast1Low.setText(weather[1].getLow());
		forecast2Date.setText(weather[2].getDate());
		forecast2Condition.setText(weather[2].getCondition());
		forecast2High.setText(weather[2].getHigh());
		forecast2Low.setText(weather[2].getLow());
		forecast3Date.setText(weather[3].getDate());
		forecast3Condition.setText(weather[3].getCondition());
		forecast3High.setText(weather[3].getHigh());
		forecast3Low.setText(weather[3].getLow());
	}
	/*
	 * This function sets the sizes of text views in the activity based on screen size
	 */
	private void setTextViewFontSizes(float scaleFactor) {
		tempText.setTextSize((float) (scaleFactor*0.5));
		cityText.setTextSize((float) (scaleFactor*0.25));
		dateText.setTextSize((float) (scaleFactor*0.07));
		conditionText.setTextSize((float) (scaleFactor*0.2));
		forecasts.setTextSize((float) (scaleFactor*0.2));
		forecast1Date.setTextSize((float) (scaleFactor*0.1));
		forecast1Condition.setTextSize((float) (scaleFactor*0.05));
		forecast1High.setTextSize((float) (scaleFactor*0.07));
		forecast1Low.setTextSize((float) (scaleFactor*0.07));
		forecast1Condition.setTextSize((float) (scaleFactor*0.07));
		forecast2Date.setTextSize((float) (scaleFactor*0.1));
		forecast2Condition.setTextSize((float) (scaleFactor*0.05));
		forecast2High.setTextSize((float) (scaleFactor*0.07));
		forecast2Low.setTextSize((float) (scaleFactor*0.07));
		forecast2Condition.setTextSize((float) (scaleFactor*0.07));
		forecast3Date.setTextSize((float) (scaleFactor*0.1));
		forecast3Condition.setTextSize((float) (scaleFactor*0.05));
		forecast3High.setTextSize((float) (scaleFactor*0.07));
		forecast3Low.setTextSize((float) (scaleFactor*0.07));
		forecast3Condition.setTextSize((float) (scaleFactor*0.07));
	}
	/*
	 * This function updates the background image based on current weather conditions
	 */
	private void setWeatherBackgroundImage() {
		if(weather[0].getCondition().toLowerCase().contains("bluster"))
			conditionImg.setBackgroundResource(R.drawable.blustery);
		else if(weather[0].getCondition().toLowerCase().contains("clear"))
			conditionImg.setBackgroundResource(R.drawable.clear);
		else if(weather[0].getCondition().toLowerCase().contains("cloud"))
			conditionImg.setBackgroundResource(R.drawable.cloud);
		else if(weather[0].getCondition().toLowerCase().contains("cold"))
			conditionImg.setBackgroundResource(R.drawable.cold);
		else if(weather[0].getCondition().toLowerCase().contains("drizzle"))
			conditionImg.setBackgroundResource(R.drawable.drizzle);
		else if(weather[0].getCondition().toLowerCase().contains("dust"))
			conditionImg.setBackgroundResource(R.drawable.dust);
		else if(weather[0].getCondition().toLowerCase().contains("fair"))
			conditionImg.setBackgroundResource(R.drawable.fair);
		else if(weather[0].getCondition().toLowerCase().contains("fog"))
			conditionImg.setBackgroundResource(R.drawable.fog);
		else if(weather[0].getCondition().toLowerCase().contains("hail"))
			conditionImg.setBackgroundResource(R.drawable.hail);
		else if(weather[0].getCondition().toLowerCase().contains("haze"))
			conditionImg.setBackgroundResource(R.drawable.haze);
		else if(weather[0].getCondition().toLowerCase().contains("hot"))
			conditionImg.setBackgroundResource(R.drawable.hot);
		else if(weather[0].getCondition().toLowerCase().contains("hurric"))
			conditionImg.setBackgroundResource(R.drawable.hurricane);
		else if(weather[0].getCondition().toLowerCase().contains("rain"))
			conditionImg.setBackgroundResource(R.drawable.rain);
		else if(weather[0].getCondition().toLowerCase().contains("shower"))
			conditionImg.setBackgroundResource(R.drawable.shower);
		else if(weather[0].getCondition().toLowerCase().contains("sleet"))
			conditionImg.setBackgroundResource(R.drawable.sleet);
		else if(weather[0].getCondition().toLowerCase().contains("smok"))
			conditionImg.setBackgroundResource(R.drawable.smoky);
		else if(weather[0].getCondition().toLowerCase().contains("snow"))
			conditionImg.setBackgroundResource(R.drawable.snow);
		else if(weather[0].getCondition().toLowerCase().contains("storm"))
			conditionImg.setBackgroundResource(R.drawable.storm);
		else if(weather[0].getCondition().toLowerCase().contains("sunny"))
			conditionImg.setBackgroundResource(R.drawable.sunny);
		else if(weather[0].getCondition().toLowerCase().contains("tornado"))
			conditionImg.setBackgroundResource(R.drawable.tornado);
		else if(weather[0].getCondition().toLowerCase().contains("wind"))
			conditionImg.setBackgroundResource(R.drawable.windy);
		else
			conditionImg.setBackgroundResource(R.drawable.clear);		
	}
	/*
	 * This function initializes the views defined for this activity.
	 */
	private void initializeViews() {
		changeCity = (Button) findViewById(R.id.change_city);
		changeTempUnits = (Button) findViewById(R.id.change_temp_units);
		conditionImg = (LinearLayout)findViewById(R.id.condition_img);
		cityText = (TextView) findViewById(R.id.weather_city);
		conditionText = (TextView) findViewById(R.id.weather_condition);
		tempText = (TextView) findViewById(R.id.weather_temp);
		dateText = (TextView) findViewById(R.id.weather_date);
		forecasts = (TextView) findViewById(R.id.forecasts);
		forecast1Date = (TextView) findViewById(R.id.forecast1_date);
		forecast1High= (TextView) findViewById(R.id.forecast1_high);
		forecast1Low= (TextView) findViewById(R.id.forecast1_low);
		forecast1Condition = (TextView) findViewById(R.id.forecast1_condition);
		forecast2Date = (TextView) findViewById(R.id.forecast2_date);
		forecast2High= (TextView) findViewById(R.id.forecast2_high);
		forecast2Low= (TextView) findViewById(R.id.forecast2_low);
		forecast2Condition = (TextView) findViewById(R.id.forecast2_condition);
		forecast3Date = (TextView) findViewById(R.id.forecast3_date);
		forecast3High= (TextView) findViewById(R.id.forecast3_high);
		forecast3Low= (TextView) findViewById(R.id.forecast3_low);
		forecast3Condition = (TextView) findViewById(R.id.forecast3_condition);		
	}
	/*
	 * This function reads the screen size of the device and returns a scaled height value
	 */
	public float convertFromDp() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		return (height/10);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
