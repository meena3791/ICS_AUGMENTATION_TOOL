package meenakshinagarajan.example.com.icsaugmentationtool;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by meenakshinagarajan on 10/23/17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    boolean flag;
    ArrayList<MarkerOptions> myHospitalMarkers = new ArrayList<MarkerOptions>();
    ArrayList<MarkerOptions> mySchoolMarkers = new ArrayList<MarkerOptions>();
    int hospitalflag;
    int schoolflag;



    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            Log.d("OnOffFlag", ((String.valueOf(flag)) ));
            if(url.contains("hospital")){
                hospitalflag=1;
                Log.d("FlagHospital", String.valueOf(hospitalflag));
            }else if(url.contains("school")){
                schoolflag=1;
                Log.d("FlagSchool", String.valueOf(schoolflag));
            }
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("Nearbyplaces",nearbyPlacesList.toString());
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }



    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

            if(Objects.equals(String.valueOf(hospitalflag), "1")){
                for (int i = 0; i < 5; i++) {
                    Log.d("onPostExecute", "Entered into showing locations");
                    MarkerOptions markerOptions = new MarkerOptions();
                    HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    myHospitalMarkers.add(markerOptions);
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.showInfoWindow();


                }
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));


            }else if(Objects.equals(String.valueOf(schoolflag), "1")) {
                for (int i = 0; i < 5; i++) {
                    Log.d("onPostExecute", "Entered into showing locations");
                    MarkerOptions markerOptions = new MarkerOptions();
                    HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    mySchoolMarkers.add(markerOptions);
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                }
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            }

            }


    }




