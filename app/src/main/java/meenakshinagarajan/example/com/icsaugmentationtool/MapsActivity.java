package meenakshinagarajan.example.com.icsaugmentationtool;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.round;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private RelativeLayout mMapView;
    List<Address> address = null;
    private double yourLatitude = 39.784450;
    private double yourLongitude = -84.057906;
    private double incidentLatitude = 39.5338;
    private double incidentLongitude = -84.3714;
    private double victim1Latitude = 39.5340;
    private double victim1Longitude= -84.3717;
    private double victim2Latitude = 39.533735;
    private double victim2Longitude=-84.372136;
    private double victim3Latitude=39.534062;
    private double victim3Longitude= -84.371002;
    private double bystander1Latitude = 39.534290;
    private double bystander1Longitude=-84.372346;
    private double bystander2Latitude = 39.534613;
    private double bystander2Longitude=-84.371514;
    private int PROXIMITY_RADIUS = 10000;
    ExpandableListAdapter listAdapter;
    ExpandableIncidentListAdapter incidentListAdapter;
    //ExpandableRiskAdapter riskListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    LatLng incidentLocation = new LatLng(incidentLatitude, incidentLongitude);
    LatLng yourLocation = new LatLng(yourLatitude, yourLongitude);
    LatLng victim1Location = new LatLng(victim1Latitude, victim1Longitude);
    LatLng victim2Location = new LatLng(victim2Latitude, victim2Longitude);
    LatLng victim3Location = new LatLng(victim3Latitude, victim3Longitude);
    LatLng bystander1Location = new LatLng(bystander1Latitude, bystander1Longitude);
    LatLng bystander2Location = new LatLng(bystander2Latitude, bystander2Longitude);
    float distance = 0;
    Geocoder geocoder;
    List<Address> incidentAddress = getAddress(incidentLatitude, incidentLongitude);
    List<String> oldUpdates=new ArrayList<String>();
    private ViewPager mViewPager;
    List<String> incidentListDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> incidentListDataChild = new HashMap<String,List<String>>();
    List<String> hazardsList = new ArrayList<String>();
    List<String> respondersList = new ArrayList<String>();
    List<String> bystandersList = new ArrayList<String>();
    List<String> symptomsList = new ArrayList<String>();
    List<String> victimsList = new ArrayList<String>();
    List<String> riskDataHeader;
    HashMap<String, List<String>> riskDataChild;
    //private RecyclerView riskDetailsListView;
    private RecyclerView.Adapter riskListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Marker> myMarkers = new ArrayList<Marker>();


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
        Log.d("address:",address.toString());
        Marker currentLocationMarker = mMap.addMarker(new MarkerOptions().position(yourLocation).title("You are here:" + address.get(0).getAddressLine(0)));
        myMarkers.add(currentLocationMarker);
        //Location enabled true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mMap.setMyLocationEnabled(true);


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
        final ToggleButton hospitalButton = (ToggleButton) findViewById(R.id.hospitalButton);
        final ToggleButton schoolButton = (ToggleButton) findViewById(R.id.schoolButton);
        final ToggleButton trafficButton = (ToggleButton) findViewById(R.id.trafficButton);
        final ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        final ExpandableListView incidentDetailsListView = (ExpandableListView) findViewById(R.id.incidentDetailsListView);
        final RecyclerView riskDetailsListView = (RecyclerView) findViewById(R.id.riskDetailsListView);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        TextView weatherIcon = (TextView) findViewById(R.id.weather_icon);
        TextView weatherIcon1 = (TextView) findViewById(R.id.weather_icon1);
        Typeface weatherFont=Typeface.createFromAsset(this.getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);
        weatherIcon1.setTypeface(weatherFont);

        riskDetailsListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        riskDetailsListView.setLayoutManager(layoutManager);



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

                final Marker incidentLocationMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Incident Location:" +incidentAddress.get(0).getAddressLine(0)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);
                incidentLocationMarker.showInfoWindow();
                myMarkers.add(incidentLocationMarker);
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

                //Resize map fragment
                mMapView = (RelativeLayout) findViewById(R.id.rl);
                ResizeAnimation resizeAnimation = new ResizeAnimation(mMapView,650,mMapView.getWidth());
                resizeAnimation.setDuration(500);
                mMapView.startAnimation(resizeAnimation);
                mMapView.requestLayout();



                //Tab Layout
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.addTab(tabLayout.newTab().setText("Incident Details"));
                tabLayout.addTab(tabLayout.newTab().setText("Predicted Risks"));
                TabLayout.Tab tab=tabLayout.getTabAt(0);
                tab.select();
                tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()){
                            case 0:
                                incidentDetailsListView.setVisibility(View.VISIBLE);
                                rl.setVisibility(View.INVISIBLE);
                            case 1:
                        }

                    }
                   @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        switch (tab.getPosition()){
                            case 0:
                                incidentDetailsListView.setVisibility(View.INVISIBLE);
                                rl.setVisibility(View.VISIBLE);
                            case 1:
                        }
                    }
                });


                //display nearby hospitals
                hospitalButton.setVisibility(View.VISIBLE);
                hospitalButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    String Hospital = "hospital";
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // The toggle is enabled
                            String url = getUrl(incidentLatitude, incidentLongitude, Hospital);
                            Object[] DataTransfer = new Object[2];
                            DataTransfer[0] = mMap;
                            DataTransfer[1] = url;
                            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                            getNearbyPlacesData.execute(DataTransfer);
                        } else {
                            // The toggle is disabled
                            mMap.clear();
                            Marker incidentLocationMarker = mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Incident Location:" +incidentAddress.get(0).getAddressLine(0)));
                            incidentLocationMarker.showInfoWindow();
                            mMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(yourLatitude, yourLongitude), new LatLng(incidentLatitude,   incidentLongitude))
                                    .width(2).color(Color.RED).geodesic(true));
                            CircleOptions circleOptions = new CircleOptions()
                                    .center(new LatLng(incidentLatitude,incidentLongitude))
                                    .radius(1000).strokeWidth(10)
                                    .strokeColor(Color.GREEN)
                                    .fillColor(Color.argb(128, 255, 0, 0))
                                    .clickable(true);
                            Circle circle = mMap.addCircle(circleOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

                        }
                    }
                });

                //display nearby schools
                schoolButton.setVisibility(View.VISIBLE);
                schoolButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    String School = "school";
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // The toggle is enabled
                            String url = getUrl(incidentLatitude, incidentLongitude, School);
                            Object[] DataTransfer = new Object[2];
                            DataTransfer[0] = mMap;
                            DataTransfer[1] = url;
                            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                            getNearbyPlacesData.execute(DataTransfer);
                        } else {
                            // The toggle is disabled
                            mMap.clear();
                            Marker incidentLocationMarker = mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Incident Location:" +incidentAddress.get(0).getAddressLine(0)));
                            incidentLocationMarker.showInfoWindow();
                            mMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(yourLatitude, yourLongitude), new LatLng(incidentLatitude,   incidentLongitude))
                                    .width(2).color(Color.RED).geodesic(true));
                            CircleOptions circleOptions = new CircleOptions()
                                    .center(new LatLng(incidentLatitude,incidentLongitude))
                                    .radius(1000).strokeWidth(10)
                                    .strokeColor(Color.GREEN)
                                    .fillColor(Color.argb(128, 255, 0, 0))
                                    .clickable(true);
                            Circle circle = mMap.addCircle(circleOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

                        }
                    }
                });

                //traffic enable/disable
                trafficButton.setVisibility(View.VISIBLE);
                trafficButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            mMap.setTrafficEnabled(true);
                        }else{
                            mMap.setTrafficEnabled(false);
                        }
                    }
                });

                //expandable list view
                expListView.setVisibility(View.VISIBLE);
                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

                        TextView childText = (TextView) view.findViewById(R.id.lblListItem);
                        String data = (String) childText.getText();
                        prepareRiskData(data);
                        riskListAdapter = new ExpandableRiskAdapter(MapsActivity.this, riskDataHeader, riskDataChild);
                        riskDetailsListView.setAdapter(riskListAdapter);

                        return false;
                    }
                });
                final String[] data={"Vehicular accident at Middletown","Vehicles involved: Chemical truck, Car, Police vehicle","First responders: Middletown police department","Bystanders moved the truck driver and police van driver to the side of the road","EMT support requested","Sodium borohydride presence detected","Symptoms found: cuts, bruises, red skin, blisters, breathing difficulty, edema","Bystanders exhibits mild scaling, red hands","Police van driver exhibits edema"};
                final ArrayList<String> myUpdates = new ArrayList<String>();
                        //"Bystanders moved the truck driver and police van driver to the side of the road","EMT support requested","Symptoms found: cuts, bruises, red skin, blisters, breathing difficulty, edema","Sodium borohydride presence detected","Police van driver found","Police van driver exhibits edema"};
                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    int i=0;
                    int j=-1;
                    @Override
                    public void run() {

                        if(i<data.length) {
                            prepareListData(data[i], data[j + 1]);
                            listAdapter = new ExpandableListAdapter(MapsActivity.this, listDataHeader, listDataChild);
                            expListView.setAdapter(listAdapter);
                            myUpdates.add(data[i]);
                            prepareIncidentListData(data[i]);
                            incidentListAdapter = new ExpandableIncidentListAdapter(MapsActivity.this, incidentListDataHeader, incidentListDataChild);
                            incidentDetailsListView.setAdapter(incidentListAdapter);
                            prepareRiskData(data[i]);
                            riskListAdapter = new ExpandableRiskAdapter(MapsActivity.this, riskDataHeader, riskDataChild);
                            riskDetailsListView.setAdapter(riskListAdapter);

                        }else{

                        }
                        i=i+1;
                        j=j+1;
                        handler.postDelayed(this, 5000);

                    }


                }, 1000);

                incidentDetailsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                        TextView childText = (TextView) view.findViewById(R.id.lblIncidentListItem);
                        String childData = (String) childText.getText();
                        if(childData.contains("Todd")){
                            float victim1distance=getDistance(victim1Latitude,victim1Longitude);
                            float distanceInFeet = (float) round((victim1distance*3.2808));
                            Marker victim1LocationMarker=mMap.addMarker(new MarkerOptions().position(victim1Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Break,Bruise,Cuts,Redness,Blistering"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim1Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            victim1LocationMarker.showInfoWindow();
                            myMarkers.add(victim1LocationMarker);
                            for(Marker m: myMarkers){
                                if(m==victim1LocationMarker){
                                    m.setVisible(true);
                                }else{
                                    m.setVisible(false);
                                }
                            }

                        }else if(childData.contains("Leicy")){
                            float victim2distance=getDistance(victim2Latitude,victim2Longitude);
                            float distanceInFeet = (float) round((victim2distance*3.2808));
                            Marker victim2LocationMarker=mMap.addMarker(new MarkerOptions().position(victim2Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Cut,Redness,Edema,Wheezing,Blistering,Unconsciousness"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim2Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            victim2LocationMarker.showInfoWindow();
                            myMarkers.add(victim2LocationMarker);
                            for(Marker m: myMarkers){
                                if(m==victim2LocationMarker){
                                    m.setVisible(true);
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Chris")){
                            float victim3distance=getDistance(victim3Latitude,victim3Longitude);
                            float distanceInFeet = (float) round((victim3distance*3.2808));
                            Marker victim3LocationMarker=mMap.addMarker(new MarkerOptions().position(victim3Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Bruise,Cut,Redness,Edema,Wheezing,Blistering"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim3Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            victim3LocationMarker.showInfoWindow();
                            myMarkers.add(victim3LocationMarker);
                            for(Marker m: myMarkers){
                                if(m==victim3LocationMarker){
                                    m.setVisible(true);
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Middletown")){
                            float victim3distance=getDistance(victim3Latitude,victim3Longitude);
                            float distanceInFeet = (float) round((victim3distance*3.2808));
                            Marker responder1LocationMarker=mMap.addMarker(new MarkerOptions().position(victim3Location).title("Responders:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("First Responders"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim3Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            responder1LocationMarker.showInfoWindow();
                            myMarkers.add(responder1LocationMarker);
                            for(Marker m: myMarkers){
                                if(m==responder1LocationMarker){
                                    m.setVisible(true);
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("EMT")){
                            float victim2distance=getDistance(victim2Latitude,victim2Longitude);
                            float distanceInFeet = (float) round((victim2distance*3.2808));
                            Marker responder2LocationMarker=mMap.addMarker(new MarkerOptions().position(victim2Location).title("Responders:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Emergency Medical Team"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim2Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            responder2LocationMarker.showInfoWindow();
                            myMarkers.add(responder2LocationMarker);
                            for(Marker m: myMarkers){
                                if(m==responder2LocationMarker){
                                    m.setVisible(true);
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Accident")||childData.contains("Sodium")){
                            for(Marker m: myMarkers){
                                if(m==incidentLocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Will") || childData.contains("Sam")){
                            if(myUpdates.toString().contains("Bystanders exhibits")){
                                float bystander1distance = getDistance(bystander1Latitude,bystander1Longitude);
                                float distanceInFeet = (float) round((bystander1distance*3.2808));
                                Marker bystander1LocationMarker=mMap.addMarker(new MarkerOptions().position(bystander1Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Scaling, Redness"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(bystander1Location));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                bystander1LocationMarker.showInfoWindow();
                                myMarkers.add(bystander1LocationMarker);
                                for(Marker m: myMarkers){
                                    if(m==bystander1LocationMarker){
                                        m.setVisible(true);
                                    }else{
                                        m.setVisible(false);
                                    }
                                }
                            }else{
                                float bystander1distance = getDistance(bystander1Latitude,bystander1Longitude);
                                float distanceInFeet = (float) round((bystander1distance*3.2808));
                                Marker bystander1LocationMarker=mMap.addMarker(new MarkerOptions().position(bystander1Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(bystander1Location));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                bystander1LocationMarker.showInfoWindow();
                                myMarkers.add(bystander1LocationMarker);
                                for(Marker m: myMarkers){
                                    if(m==bystander1LocationMarker){
                                        m.setVisible(true);
                                    }else{
                                        m.setVisible(false);
                                    }
                                }
                            }



                        }else if(childData.contains("Jessica") || childData.contains("Brandon")){
                            if(myUpdates.toString().contains("Sodium borohydride")){
                                float bystander2distance = getDistance(bystander2Latitude,bystander2Longitude);
                                float distanceInFeet = (float) round((bystander2distance*3.2808));
                                Marker bystander2LocationMarker=mMap.addMarker(new MarkerOptions().position(bystander2Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("ExposedTo: SodiumBorohydride"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(bystander2Location));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                bystander2LocationMarker.showInfoWindow();
                                myMarkers.add(bystander2LocationMarker);
                                for(Marker m: myMarkers){
                                    if(m==bystander2LocationMarker){
                                        m.setVisible(true);
                                    }else{
                                        m.setVisible(false);
                                    }
                                }
                            }else{
                                float bystander2distance = getDistance(bystander2Latitude,bystander2Longitude);
                                float distanceInFeet = (float) round((bystander2distance*3.2808));
                                Marker bystander2LocationMarker=mMap.addMarker(new MarkerOptions().position(bystander2Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(bystander2Location));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                bystander2LocationMarker.showInfoWindow();
                                myMarkers.add(bystander2LocationMarker);
                                for(Marker m: myMarkers){
                                    if(m==bystander2LocationMarker){
                                        m.setVisible(true);
                                    }else{
                                        m.setVisible(false);
                                    }
                                }
                            }



                        }
                        return false;
                    }

                });

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
    public float getDistance(double Lat,double Lng){
        Location orgLocation = new Location("point A");
        Location destLocation = new Location("point B");
        float distance=0;
        orgLocation.setLatitude(incidentLatitude);
        orgLocation.setLongitude(incidentLongitude);
        destLocation.setLatitude(Lat);
        destLocation.setLongitude(Lng);
        distance = orgLocation.distanceTo(destLocation);
        return distance;
    }
    public static JSONObject getJSON(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCboTl9o1tMK_8rKhcxL5ki20D0Ka8eEBM");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private void prepareListData(String data,String data1) {

        listDataHeader = new ArrayList<String>();

        listDataChild = new HashMap<String, List<String>>();

        //Adding date
        String s = Calendar.getInstance().getTime().toString();

        // Adding child data
        listDataHeader.add("Incident Updates:"+data);

        // Adding child data
        List<String> incidentUpdates = new ArrayList<String>();
        if (oldUpdates.isEmpty()) {

        } else {
            for(int i=0;i<oldUpdates.size();i++){
                incidentUpdates.add(0,oldUpdates.get(i));
            }
        }
        incidentUpdates.add(0,data1);
        listDataChild.put(listDataHeader.get(0),(incidentUpdates));// Header, Child data
        oldUpdates.add(data1);
    }

    private void prepareRiskData(String data) {

        if(data.contains("Vehicular accident")){
            // Adding child data
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("Vehicular Accident");

            List<String> riskUpdates = new ArrayList<String>();
            // Adding child data
            riskUpdates.add("type");
            riskUpdates.add("canCause");
            riskUpdates.add("Cut");
            riskUpdates.add("Break");
            riskUpdates.add("Bruise");
            riskUpdates.add("Unconsciousness");
            riskUpdates.add("mitigatedBy");
            riskUpdates.add("Seat Belt");
            riskUpdates.add("Air Bag");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));// Header, Child data

        }
        if(data.contains("Chemical truck")){
            // Adding child data
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("Chemical Truck");
            riskDataHeader.add("Chemicals");
            riskDataHeader.add("Chemical Hazard");
            // Adding child data
            List<String> riskUpdates = new ArrayList<String>();
            List<String> riskUpdates1 = new ArrayList<String>();
            List<String> riskUpdates2 = new ArrayList<String>();
            List<String> riskUpdates3 = new ArrayList<String>();
            riskUpdates.add("canCarry");
            riskUpdates1.add("canCause");
            riskUpdates2.add("canCause");
            riskUpdates2.add("Itching");
            riskUpdates2.add("Blindness");
            riskUpdates2.add("Blistering");
            riskUpdates2.add("Scaling");
            riskUpdates2.add("Redness");
            riskUpdates2.add("Corneal Damage");
            riskUpdates2.add("Coughing");
            riskUpdates2.add("Sneezing");
            riskUpdates2.add("mitigatedBy");
            riskUpdates2.add("Full suit");
            riskUpdates2.add("Gloves");
            riskUpdates2.add("Boots");
            riskUpdates2.add("Self contained breathing apparatus");
            riskUpdates2.add("Splash goggles");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));
            riskDataChild.put(riskDataHeader.get(1),(riskUpdates1));
            riskDataChild.put(riskDataHeader.get(2),(riskUpdates2));// Header, Child data

        }
        if(data.contains("First responders")){
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("First Responders");
            riskDataHeader.add("Vehicular Accident");
            riskDataHeader.add("Chemical Hazard");
            List<String> riskUpdates = new ArrayList<String>();
            List<String> riskUpdates1 = new ArrayList<String>();
            List<String> riskUpdates2 = new ArrayList<String>();
            riskUpdates.add("type");
            riskUpdates.add("are susceptibleTo");
            riskUpdates.add("Vehicular Accident");
            riskUpdates.add("Chemical Hazard");
            riskUpdates1.add("canCause");
            riskUpdates1.add("Cut");
            riskUpdates1.add("Break");
            riskUpdates1.add("Bruise");
            riskUpdates1.add("Unconsciousness");
            riskUpdates2.add("canCause");
            riskUpdates2.add("Itching");
            riskUpdates2.add("Blindness");
            riskUpdates2.add("Blistering");
            riskUpdates2.add("Scaling");
            riskUpdates2.add("Redness");
            riskUpdates2.add("Corneal Damage");
            riskUpdates2.add("Coughing");
            riskUpdates2.add("Sneezing");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));
            riskDataChild.put(riskDataHeader.get(1),(riskUpdates1));
            riskDataChild.put(riskDataHeader.get(2),(riskUpdates2));

        }
       if(data.contains("Bystanders")){
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("Bystanders");
            riskDataHeader.add("Vehicular Accident");
            riskDataHeader.add("Chemical Hazard");
            List<String> riskUpdates = new ArrayList<String>();
            List<String> riskUpdates1 = new ArrayList<String>();
            List<String> riskUpdates2 = new ArrayList<String>();
            riskUpdates.add("type");
            riskUpdates.add("are susceptibleTo");
            riskUpdates.add("Vehicular Accident");
            riskUpdates.add("Chemical Hazard");
            riskUpdates1.add("canCause");
            riskUpdates1.add("Cut");
            riskUpdates1.add("Break");
            riskUpdates1.add("Bruise");
            riskUpdates1.add("Unconsciousness");
            riskUpdates2.add("canCause");
            riskUpdates2.add("Itching");
            riskUpdates2.add("Blindness");
            riskUpdates2.add("Blistering");
            riskUpdates2.add("Scaling");
            riskUpdates2.add("Redness");
            riskUpdates2.add("Corneal Damage");
            riskUpdates2.add("Coughing");
            riskUpdates2.add("Sneezing");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));
            riskDataChild.put(riskDataHeader.get(1),(riskUpdates1));
            riskDataChild.put(riskDataHeader.get(2),(riskUpdates2));
        }
       if(data.contains("EMT")){
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("EMT Response Team");
            List<String> riskUpdates = new ArrayList<String>();
            riskUpdates.add("canTreat");
            riskUpdates.add("Cut");
            riskUpdates.add("Break");
            riskUpdates.add("Bruise");
            riskUpdates.add("Unconsciousness");
            riskUpdates.add("Itching");
            riskUpdates.add("Blindness");
            riskUpdates.add("Blistering");
            riskUpdates.add("Scaling");
            riskUpdates.add("Redness");
            riskUpdates.add("Corneal Damage");
            riskUpdates.add("Coughing");
            riskUpdates.add("Sneezing");
            riskUpdates.add("type");
            riskUpdates.add("are susceptibleTo");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));

        }if(data.contains("Symptoms")){
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("Victims");
            riskDataHeader.add("Sodium Borohydride");
            riskDataHeader.add("Chemical Hazard");
            riskDataHeader.add("Vehicular Accident");
            riskDataHeader.add("EMT Response Team");
            List<String> riskUpdates = new ArrayList<String>();
            List<String> riskUpdates1 = new ArrayList<String>();
            List<String> riskUpdates2 = new ArrayList<String>();
            List<String> riskUpdates3 = new ArrayList<String>();
            List<String> riskUpdates4 = new ArrayList<String>();
            riskUpdates.add("exhibits");
            riskUpdates.add("Cuts");
            riskUpdates.add("Bruises");
            riskUpdates.add("Redness");
            riskUpdates.add("Blisters");
            riskUpdates.add("Breathing difficulty");
            riskUpdates.add("Edema");
            riskUpdates1.add("canCause");
            riskUpdates1.add("Itching");
            riskUpdates1.add("Blindness");
            riskUpdates1.add("Blistering");
            riskUpdates1.add("Scaling");
            riskUpdates1.add("Redness");
            riskUpdates1.add("Corneal Damage");
            riskUpdates1.add("Coughing");
            riskUpdates1.add("Sneezing");
            riskUpdates2.add("canCause");
            riskUpdates2.add("Edema");
            riskUpdates3.add("canCause");
            riskUpdates3.add("Cut");
            riskUpdates3.add("Break");
            riskUpdates3.add("Bruise");
            riskUpdates3.add("Unconsciousness");
            riskUpdates4.add("canTreat");
            riskUpdates4.add("Cut");
            riskUpdates4.add("Break");
            riskUpdates4.add("Bruise");
            riskUpdates4.add("Unconsciousness");
            riskUpdates4.add("Itching");
            riskUpdates4.add("Blindness");
            riskUpdates4.add("Blistering");
            riskUpdates4.add("Scaling");
            riskUpdates4.add("Redness");
            riskUpdates4.add("Corneal Damage");
            riskUpdates4.add("Coughing");
            riskUpdates4.add("Sneezing");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));
            riskDataChild.put(riskDataHeader.get(1),(riskUpdates1));
            riskDataChild.put(riskDataHeader.get(2),(riskUpdates2));
            riskDataChild.put(riskDataHeader.get(3),(riskUpdates3));
            riskDataChild.put(riskDataHeader.get(4),(riskUpdates4));
        }
        if(data.contains("Bystanders")&&data.contains("exhibits")){
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("Bystanders");
            riskDataHeader.add("Vehicular Accident");
            riskDataHeader.add("Chemical Hazard");
            List<String> riskUpdates = new ArrayList<String>();
            List<String> riskUpdates1 = new ArrayList<String>();
            List<String> riskUpdates2 = new ArrayList<String>();
            riskUpdates.add("type");
            riskUpdates.add("are susceptibleTo");
            riskUpdates.add("Vehicular Accident");
            riskUpdates.add("Chemical Hazard");
            riskUpdates.add("exhibits");
            riskUpdates.add("Scaling");
            riskUpdates.add("Redness");
            riskUpdates1.add("canCause");
            riskUpdates1.add("Cut");
            riskUpdates1.add("Break");
            riskUpdates1.add("Bruise");
            riskUpdates1.add("Unconsciousness");
            riskUpdates2.add("canCause");
            riskUpdates2.add("Itching");
            riskUpdates2.add("Blindness");
            riskUpdates2.add("Blistering");
            riskUpdates2.add("Scaling");
            riskUpdates2.add("Redness");
            riskUpdates2.add("Corneal Damage");
            riskUpdates2.add("Coughing");
            riskUpdates2.add("Sneezing");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));
            riskDataChild.put(riskDataHeader.get(1),(riskUpdates1));
            riskDataChild.put(riskDataHeader.get(2),(riskUpdates2));
        }
        if(data.contains("Sodium borohydride")) {
            // Adding child data
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("Sodium Borohydride");
            List<String> riskUpdates = new ArrayList<String>();
            // Adding child data
            riskUpdates.add("type");
            riskUpdates.add("hasPhysicalDescription");
            riskUpdates.add("hasFormula");
            riskUpdates.add("hasSynonym");
            riskUpdates.add("canCause");
            riskUpdates.add("Itching");
            riskUpdates.add("Blindness");
            riskUpdates.add("Blistering");
            riskUpdates.add("Scaling");
            riskUpdates.add("Redness");
            riskUpdates.add("Corneal Damage");
            riskUpdates.add("Coughing");
            riskUpdates.add("Sneezing");
            riskUpdates.add("mitigatedBy");
            riskUpdates.add("Gloves");
            riskUpdates.add("FullSuit");
            riskUpdates.add("Boots");
            riskUpdates.add("SplashGoggles");
            riskUpdates.add("SelfBreathingApparatus");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0), (riskUpdates));// Header, Child data

        }if(data.contains("Police")){
            riskDataHeader = new ArrayList<String>();
            riskDataHeader.add("Victim");
            riskDataHeader.add("Chemical Hazard");
            List<String> riskUpdates = new ArrayList<String>();
            List<String> riskUpdates1 = new ArrayList<String>();
            riskUpdates.add("exhibits");
            riskUpdates.add("Edema");
            riskUpdates1.add("canCause");
            riskUpdates1.add("Edema");
            riskDataChild = new HashMap<String, List<String>>();
            riskDataChild.put(riskDataHeader.get(0), (riskUpdates));
            riskDataChild.put(riskDataHeader.get(1), (riskUpdates1));// Header, Child data
        }

    }

    private void prepareIncidentListData(String data) {

        if(data.contains("Vehicular accident")){
            // Adding child data
            incidentListDataHeader.add("Hazards");
            // Adding child data
            hazardsList.add("Vehicular Accident");
            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList); // Header, Child data
        }
        if(data.contains("Chemical truck")){
            // Adding child data
            incidentListDataHeader.add("Victims");
            // Adding child data
            victimsList.add("Winston, Todd");
            victimsList.add("Leicy, Mary");
            victimsList.add("Stevenson, Chris");
            incidentListDataChild.put(incidentListDataHeader.get(1), victimsList); // Header, Child data
        }
        if(data.contains("First responders")){
            // Adding child data
            incidentListDataHeader.add("Responders");
            //Adding child data
            respondersList.add("Middletown police Department");
            incidentListDataChild.put(incidentListDataHeader.get(2), respondersList);
        }
        if(data.contains("Bystanders")&&data.contains("truck")){
            // Adding child data
            incidentListDataHeader.add("Bystanders");
            bystandersList.add("Connor, Will");
            bystandersList.add("Smith, Sam");
            bystandersList.add("Taylor, Jessica");
            bystandersList.add("Duber, Brandon");
            incidentListDataChild.put(incidentListDataHeader.get(3), bystandersList);
        }
        if(data.contains("EMT")){
            // Adding child data
            respondersList.add("EMT");
            incidentListDataChild.put(incidentListDataHeader.get(2), respondersList);
        }
        if(data.contains("Symptoms")){
            // Adding child data
            incidentListDataHeader.add("Symptoms found on scene");
            symptomsList.add("Cuts");
            symptomsList.add("Bruises");
            symptomsList.add("Red skin");
            symptomsList.add("Blisters");
            symptomsList.add("Breathing difficulty");
            symptomsList.add("Edema");
            incidentListDataChild.put(incidentListDataHeader.get(4), symptomsList);
        }
        if(data.contains("Sodium")){
            hazardsList.add("Sodium Borohydride");
            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList);
        }

    }

}

