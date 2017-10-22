package meenakshinagarajan.example.com.icsaugmentationtool;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.design.widget.TabLayout;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.round;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RelativeLayout mMapView;
    List<Address> address = null;
    private double yourLatitude = 39.7845;
    private double yourLongitude = -84.0580;
    private double incidentLatitude = 39.533860;
    private double incidentLongitude = -84.371423;
    LatLng incidentLocation = new LatLng(incidentLatitude, incidentLongitude);
    LatLng yourLocation = new LatLng(yourLatitude, yourLongitude);
    ArrayList<LatLng> points;
    PolylineOptions lineOptions = null;
    float distance=0;
    Geocoder geocoder;
    List<Address> incidentAddress = getAddress(incidentLatitude,incidentLongitude);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorText));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_maps));



    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        address = getAddress(yourLatitude, yourLongitude);
        Marker currentLocationMarker=mMap.addMarker(new MarkerOptions().position(yourLocation).title("You are here:" + address.get(0).getAddressLine(0)));

        //distance between yourlocation and incident location
        Location orgLocation = new Location("point A");
        Location destLocation = new Location("point B");
        orgLocation.setLatitude(yourLatitude);
        orgLocation.setLongitude(yourLongitude);
        destLocation.setLatitude(incidentLatitude);
        destLocation.setLongitude(incidentLongitude);
        distance =orgLocation.distanceTo(destLocation);
        final float distanceInMiles = (float) round((distance*0.000621371192));
        Log.d("distance:",String.valueOf(distanceInMiles));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        currentLocationMarker.showInfoWindow();

        //declarations
        final CardView alertCard = (CardView) findViewById(R.id.alertCard);
        final TextView alertText = (TextView) findViewById(R.id.alertText);
        final Button takeActionButton = (Button) findViewById(R.id.takeActionButton);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabView);



        //alertbox after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertCard.setVisibility(View.VISIBLE);
                alertText.setText("Oh Snap! Vehicular accident at "+incidentAddress.get(0).getAddressLine(0)+" "+distanceInMiles+" "+"miles from your location");
                takeActionButton.setVisibility(View.VISIBLE);
            }
        }, 10000);


        //after click of takeaction button
        takeActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Marker incidentLocationMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Incident Location:" +incidentAddress.get(0).getAddressLine(0)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);
                incidentLocationMarker.showInfoWindow();
                Polyline line = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(yourLatitude, yourLongitude), new LatLng(incidentLatitude,   incidentLongitude))
                                .width(2).color(Color.RED).geodesic(true));
                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(incidentLatitude,incidentLongitude))
                        .radius(1000).strokeWidth(10)
                                .strokeColor(Color.GREEN)
                                .fillColor(Color.argb(128, 255, 0, 0))
                                .clickable(true);
                Circle circle = mMap.addCircle(circleOptions);
                alertCard.setVisibility(View.INVISIBLE);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                mMapView = (RelativeLayout) findViewById(R.id.rl);
                ResizeAnimation resizeAnimation = new ResizeAnimation(mMapView,650,mMapView.getWidth());
                resizeAnimation.setDuration(2000);
                mMapView.startAnimation(resizeAnimation);
                mMapView.requestLayout();

                //Tab Layout
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
                tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));

            }
        });

    }




    public List getAddress(double Lat, double Lng) {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            address = geocoder.getFromLocation(Lat, Lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
   return address;

    }


}

