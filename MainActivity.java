package com.mycompany.myapp10;

//Location app
//MainActivity.java

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.location.*;
import android.os.*;
import android.provider.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import android.content.Intent;  
import android.os.PowerManager;  
import android.net.Uri;  
import android.os.Build;  
import android.provider.Settings;  


public class MainActivity extends Activity {
	private static final int REQUEST_LOCATION = 1;
	Button btnGetLocation;
	TextView showLocation;
	LocationManager locationManager;
	String latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ActivityCompat.requestPermissions( this,
										  new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
		showLocation = findViewById(R.id.showLocation);
		btnGetLocation = findViewById(R.id.btnGetLocation);
		btnGetLocation.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						OnGPS();
					} else {
						getLocation();
					}
				}
			});
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  
			Intent intent = new Intent();  
			String packageName = getPackageName();  
			PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);  
			if (!pm.isIgnoringBatteryOptimizations(packageName)) {  
				intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);  
				intent.setData(Uri.parse("package:" + packageName));  
				startActivity(intent);  
			}  
		}  

	}




	private void OnGPS() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	private void getLocation() {
		if (ActivityCompat.checkSelfPermission(
				MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
				MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
		} else {
			Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (locationGPS != null) {
				double lat = locationGPS.getLatitude();
				double longi = locationGPS.getLongitude();
				latitude = String.valueOf(lat);
				longitude = String.valueOf(longi);
				showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
			} else {
				Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
