package com.jeycorp.impressletters.weather;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;


public class FusedLocationHelper {
	
	public interface FusedLocationListener {
		public void onNotSupportedService();
		public void onConnected(Location location);
		
		public void onLocationChanged(Location location);
		public void onAddressChanged(MyAddress myAddress);
	}
		
	// 충청북도 영동군 황간면
	public static final double DEFAULT_LATITUDE = 36.220799898229316; // 임의의 위도 설정
	public static final double DEFAULT_LOINGITUDE = 127.89840001612902; // 임의의 경도 설정
	
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final double LOCATION_ERROR_RANGE = 0.00001; 
	public static final String DEFAULT_PROVIDER = "default";
	private static final int INTERVAL = 5000;
	private static final int FASTEST_INTERVAL = 3000;

	
	private static final String CLASS_NAME = "LockScreenLucky";
	private static final String KEY_MY_ADDRESS_JSON = "myAddressJson";
	
	private LocationRequest locationRequest;
	private Activity activity;
	private Context context;
//    private LocationClient locationClient;
    private GoogleApiClient googleApiClient;
    private FusedLocationListener fusedLocationListener;
    private Location cacheLocation;
	
	public FusedLocationHelper(Activity activity, FusedLocationListener fusedLocationListener) {
		this.activity = activity;
		this.context = activity;
		this.fusedLocationListener = fusedLocationListener;
		
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);        
//        locationClient = new LocationClient(context, connectionCallbacks, onConnectionFailedListener);
        googleApiClient = new GoogleApiClient.Builder(context)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(connectionCallbacks)
        .addOnConnectionFailedListener(onConnectionFailedListener)
        .build();
	}

	public static boolean isNetworkProviderAvailable(Context context) {
	    LocationManager localLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    return (localLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) && (localLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
	}

	public static boolean isGpsProviderAvailable(Context context) {
		LocationManager localLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    return (localLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) && (localLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
	}

	public static boolean isLocationAvailable(Context context) {
		boolean enabled = false;
		enabled = isNetworkProviderAvailable(context);
		if (enabled == false) {
			enabled = isGpsProviderAvailable(context);
		}

		return enabled;
	}
	
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status

            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
        	if(fusedLocationListener != null) {
        		fusedLocationListener.onNotSupportedService();
        	}
            return false;
        }
    }
	
    public void start() {
//        locationClient.connect();
        googleApiClient.connect();
    }
	
    public void stop() {
    	stopPeriodicUpdates();
    	
//    	if(locationClient != null && locationClient.isConnected()) {    		
//        	locationClient.disconnect();
//    	}
    	
    	if(googleApiClient != null && googleApiClient.isConnected()) {    		
    		googleApiClient.disconnect();
    	}
    }

    private void startPeriodicUpdates() {
//        locationClient.requestLocationUpdates(locationRequest, locationListener);    	
    	LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
    }

    private void stopPeriodicUpdates() {        
//    	if(locationClient != null && locationClient.isConnected()) {
//    		locationClient.removeLocationUpdates(locationListener);
//    	}
    	
    	if(googleApiClient != null && googleApiClient.isConnected()) {
    		LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
    	}    	
    }
    
    public Location getLastLocation() {

    	Location lastLocation = null;
        if (servicesConnected()) {
//        	if(locationClient.isConnected()) {
//        		lastLocation = locationClient.getLastLocation();        		
//        	}
        	
        	if(googleApiClient.isConnected()) {
        		lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);        		
        	}
        }
        
        if(lastLocation == null) {
        	lastLocation = getLegacyLastLocation();
        }
        
        if(lastLocation == null) {
        	lastLocation = new Location(DEFAULT_PROVIDER);
        	lastLocation.setLatitude(DEFAULT_LATITUDE);
        	lastLocation.setLongitude(DEFAULT_LOINGITUDE);
        }
        
        if(lastLocation != null) {
        	Log.i(getClass().getName(), "getLastLocation : " + lastLocation.getLatitude() + "&" + lastLocation.getLongitude());
        }
        
        return lastLocation;
    }
    
    private Location getLegacyLastLocation() {
        LocationManager localLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = null;
        if (localLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	lastLocation = localLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        	Log.i(getClass().getName(), "getLegacyLastLocation : NETWORK_PROVIDER - " + (lastLocation != null ? "TRUE" : "FALSE"));
        }
        
//        if(lastLocation == null) {
//        	if(localLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
//        		lastLocation = localLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//        		Log.i(getClass().getName(), "getLegacyLastLocation : PASSIVE_PROVIDER - " + (lastLocation != null ? "TRUE" : "FALSE"));
//        	}
//        }
        
//        if(lastLocation == null) {
//        	if(localLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//        		lastLocation = localLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        		Log.i(getClass().getName(), "getLegacyLastLocation : GPS_PROVIDER - " + (lastLocation != null ? "TRUE" : "FALSE"));
//        	}
//        }        
        
        return lastLocation;
        
    }
    
    private LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(fusedLocationListener != null) {
				fusedLocationListener.onLocationChanged(location);
			}					
			requestGetAddress(location);
		}
	};
	
	private void requestGetAddress(Location location) {
		
		if(location != null && DEFAULT_PROVIDER.equals(location.getProvider()) == false) {
			// cache instead of GetAddressTask.
			if(cacheGetAddress(location) == false) 
			{
//				VolleyToast.show(context, "requestGetAddress");
				new GetAddressTask(context).execute(location);
				Log.i(getClass().getName(), "onLocationChanged location(false)");	
			}			
		}		
	}
	
	private boolean cacheGetAddress(Location location) {
		if(cacheLocation != null && location != null) {			
			Log.d(getClass().getName(), "cacheLocation : " + cacheLocation.getLatitude() + "&" + cacheLocation.getLongitude());
			Log.d(getClass().getName(), "location : " + location.getLatitude() + "&" + location.getLongitude());
			
			double latitudeAbs = Math.abs(cacheLocation.getLatitude() - location.getLatitude());
			double longitudeAbs = Math.abs(cacheLocation.getLongitude() - location.getLongitude());
			
			Log.d(getClass().getName(), String.format("latitudeAbs : %f", latitudeAbs));
			Log.d(getClass().getName(), String.format("longitudeAbs : %f", longitudeAbs));
			
			if(latitudeAbs < LOCATION_ERROR_RANGE && longitudeAbs < LOCATION_ERROR_RANGE) {
				MyAddress myAddress = getMyAddress(context);			
				if(myAddress != null) {
					
					Log.d(getClass().getName(), "myAddress.getCountry() : " + myAddress.getCountry());
					
					if(TextUtils.isEmpty(myAddress.getCountry()) == false) {
			    		if(fusedLocationListener != null) {
			    			fusedLocationListener.onAddressChanged(myAddress);
			    		}
						Log.i(getClass().getName(), "onLocationChanged location(true)");
						return true;
					}
				}
			}
		}
		
		cacheLocation = location;
		return false;
	}
		
	private ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {
		
		@Override
		public void onConnected(Bundle bundle) {
			// TODO Auto-generated method stub
	        startPeriodicUpdates();
	        
	        Location lastLocation = getLastLocation();
	        
	        if(fusedLocationListener != null) {	        	
	        	fusedLocationListener.onConnected(lastLocation);	        	
	        }
	        requestGetAddress(lastLocation);
	        
			Log.i(getClass().getName(), "onConnected");
		}

		@Override
		public void onConnectionSuspended(int cause) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private OnConnectionFailedListener onConnectionFailedListener = new OnConnectionFailedListener() {

		@Override
		public void onConnectionFailed(ConnectionResult connectionResult) {
			// TODO Auto-generated method stub
			connectionFailed(activity, connectionResult);
		}		
	};
	
	public void connectionFailed(Activity activity, ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
            	
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
        	int errorCode = connectionResult.getErrorCode();
        	
        	switch(errorCode) {
        	case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
        		//VolleyDialog.showMessageDialog(activity, context.getString(R.string.service_version_update_required));
        		Location legacyLastLocatio = getLegacyLastLocation();
        		requestGetAddress(legacyLastLocatio);
        		break;
    		default:
    		//	VolleyDialog.showMessageDialog(activity, "connectionFailed errorCode : " + connectionResult.getErrorCode());
    			break;
        	}            
        }
	}	
	
   protected class GetAddressTask extends AsyncTask<Location, Void, Integer> {

	   private static final int STATE_NOMAL = 0;
	   private static final int STATE_EXCEPTION = 1;
	   
        private Context localContext;
        private MyAddress myAddress;
        private int state;
        private Location location;
        
        public GetAddressTask(Context context) {
            super();
            localContext = context;
        }

        @Override
        protected Integer doInBackground(Location... params) {

        	state = STATE_NOMAL;
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());
            location = params[0];
            
//            VolleyToast.show(localContext, location != null ? location.getProvider() : "null");
            
            List <Address> addresses = null;
            Address address = null;

			if(location != null) {
				try {
	                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
	                } catch (IOException ioException) {
	                	ioException.printStackTrace();
	                	state = STATE_EXCEPTION;
	                } catch (IllegalArgumentException illegalArgumentException) {
	                    illegalArgumentException.printStackTrace();
	                    state = STATE_EXCEPTION;
	                }
	                if (addresses != null && addresses.size() > 0) {
	                    address = addresses.get(0);	                    
	                }  
	                
	            	myAddress = new MyAddress();
	            	
	        		if(address != null) {
	            		String country = address.getCountryName();
	            		String sido = address.getAdminArea();
	            		String gugun = address.getLocality();
	            		String dongLee = address.getThoroughfare();
	            		double latitude = address.getLatitude();
	            		double longitude = address.getLongitude();
	            		
	            		Log.i(getClass().getName(), "country : " + country);
	            		Log.i(getClass().getName(), "sido : " + sido);
	            		Log.i(getClass().getName(), "gugun : " + gugun);
	            		Log.i(getClass().getName(), "dongLee : " + dongLee);
	            		Log.i(getClass().getName(), "address : " + address.toString());
	            		
	            		// google api bug : 창원시 is null
						if(TextUtils.isEmpty(sido) || TextUtils.isEmpty(gugun) || TextUtils.isEmpty(dongLee)) {
							String addressLine = address.getAddressLine(0);
							
							if(TextUtils.isEmpty(addressLine) == false) {
								String[] addr = addressLine.split(" ");	
								
								myAddress = new MyAddress();
								if(addr.length > 0) {
									myAddress.setCountry(addr[0]);	
								}					
								if(addr.length > 1) {
									myAddress.setSido(addr[1]);	
								}
								if(addr.length > 2) {
									myAddress.setGugun(addr[2]);	
								}
								if(addr.length > 3) {
									myAddress.setDongLee(addr[3]);	
								}								
							}				
						} else {
							myAddress.setCountry(country);
		            		myAddress.setSido(sido);
		            		myAddress.setGugun(gugun);
		            		myAddress.setDongLee(dongLee);	
						}	            		
	            		myAddress.setLatitude(latitude);
	            		myAddress.setLongitude(longitude);
	        		}	                                
			}
			return state;      
			

        }

        @Override
        protected void onPostExecute(Integer state) {
        	if(state == STATE_NOMAL) {
        		postExecuteGetAddress(localContext, myAddress);	
        	} else {
        		new GetAddress2Task(context).execute(location);
        	}
        	
        }
    }
   
   private void postExecuteGetAddress(Context context, MyAddress myAddress) {
		putMyAddress(context, myAddress);
		
		if(fusedLocationListener != null) {
			fusedLocationListener.onAddressChanged(myAddress);
		}
   }
   
	class GetAddress2Task extends AsyncTask<Location, Void, Void> {
		
		private MyAddress myAddress;
		private Context localContext;
		
        public GetAddress2Task(Context context) {
            super();
            localContext = context;
        }
        
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);			
			postExecuteGetAddress(localContext, myAddress);
		}

		@Override
		protected Void doInBackground(Location... params) {

			Location location = params[0];
            
			if(location != null) {
				try {
					String url = "http://maps.googleapis.com/maps/api/geocode/xml?latlng=" + location.getLatitude() + "," + location.getLongitude() + "&sensor=true&language=KO";
					URL httpUrl = new URL(url);
					
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
					XmlPullParser parser = factory.newPullParser();
					parser.setInput(httpUrl.openStream(), null);	
					int parserEvent = parser.getEventType();
					String address = null;
					
					do{					
						if(parserEvent == XmlPullParser.START_TAG) {
							
							if(parser.getName().equals("error_message")) {
								break;
							}
							
							if(parser.getName().equals("formatted_address")) {
								address = parser.nextText();
								break;
							}
						}
						
						parserEvent = parser.next();
					}while(parserEvent != XmlPullParser.END_DOCUMENT);
					
					if(TextUtils.isEmpty(address) == false) {					
						String[] addr = address.split(" ");	
						
						myAddress = new MyAddress();
						myAddress.setCountry(addr[0]);
						myAddress.setSido(addr[1]);
						myAddress.setGugun(addr[2]);
						myAddress.setDongLee(addr[3]);					
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return null;
		}
	}
	
   
	private static boolean putMyAddress(Context context, MyAddress myAddress) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		String myAddressJson = new Gson().toJson(myAddress);
		editor.putString(KEY_MY_ADDRESS_JSON, myAddressJson);
		return editor.commit();
	}
	
	public static MyAddress getMyAddress(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
		String myAddressJson = sharedPreferences.getString(KEY_MY_ADDRESS_JSON, null); 
		return new Gson().fromJson(myAddressJson, MyAddress.class);
	}
	
	public MyAddress getLastMyAddress() {
		return getMyAddress(context);
	}
}
