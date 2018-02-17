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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    List<Address> incidentAddress = null;
    private double yourLatitude = 39.784450;
    private double yourLongitude = -84.057906;
    private double incidentLatitude = 39.5338;
    private double incidentLongitude = -84.3714;
    private double sodiumBorohydrideLatitude = 39.384766;
    private double sodiumBorohydrideLongitude= -84.374207;
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
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> incidentListDataHeader;
    HashMap<String, List<String>> incidentListDataChild;
    LatLng incidentLocation = new LatLng(incidentLatitude, incidentLongitude);
    LatLng chemicalLocation = new LatLng(sodiumBorohydrideLatitude, sodiumBorohydrideLongitude);
    LatLng yourLocation = new LatLng(yourLatitude, yourLongitude);
    LatLng victim1Location = new LatLng(victim1Latitude, victim1Longitude);
    LatLng victim2Location = new LatLng(victim2Latitude, victim2Longitude);
    LatLng victim3Location = new LatLng(victim3Latitude, victim3Longitude);
    LatLng bystander1Location = new LatLng(bystander1Latitude, bystander1Longitude);
    LatLng bystander2Location = new LatLng(bystander2Latitude, bystander2Longitude);
    float distance = 0;
    Geocoder geocoder;
    List<String> oldUpdates=new ArrayList<String>();
    private ViewPager mViewPager;
    List<String> hazardsList;
    List<String> respondersList;
    List<String> bystandersList;
    List<String> symptomsList;
    List<String> victimsList;
    List<String> riskDataHeader;
    HashMap<String, List<String>> riskDataChild;
    //private RecyclerView riskDetailsListView;
    private RecyclerView.Adapter riskListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Marker> myMarkers = new ArrayList<Marker>();
    boolean hospitalFlag,schoolFlag;
    View listView = null;
    int targetHeight;


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
        mMap.getUiSettings().setZoomControlsEnabled(true);
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
       // final ToggleButton incidentUpdatesToggleButton = (ToggleButton) findViewById(R.id.incidentUpdatesToggleButton);
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
                incidentAddress = getAddress(incidentLatitude, incidentLongitude);
                alertText.setText("Oh Snap! Vehicular accident at "+incidentAddress.get(0).getAddressLine(0)+" "+distanceInMiles+" "+"miles from your location");
                takeActionButton.setVisibility(View.VISIBLE);
            }
        }, 5000);



        


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
                ResizeAnimation resizeAnimation = new ResizeAnimation(mMapView,650,mMapView.getWidth(),mMapView.getHeight(),mMapView.getHeight());
                resizeAnimation.setDuration(500);
                mMapView.startAnimation(resizeAnimation);
                mMapView.requestLayout();



                incidentDetailsListView.setVisibility(View.VISIBLE);




                //display nearby hospitals
                hospitalButton.setVisibility(View.VISIBLE);
                hospitalButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String Hospital = "hospital";
                        if (isChecked) {
                            // The toggle is enabled
                            hospitalFlag=true;
                            String url = getUrl(incidentLatitude, incidentLongitude, Hospital);
                            Object[] DataTransfer = new Object[2];
                            DataTransfer[0] = mMap;
                            DataTransfer[1] = url;
                            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                            getNearbyPlacesData.execute(DataTransfer);
                            for(Marker m: myMarkers){
                                if(m==incidentLocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        } else {
                            // The toggle is disabled
                            hospitalFlag=false;
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

                            if(schoolFlag){
                                String url = getUrl(incidentLatitude, incidentLongitude, "school");
                                Object[] DataTransfer = new Object[2];
                                DataTransfer[0] = mMap;
                                DataTransfer[1] = url;
                                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                                getNearbyPlacesData.execute(DataTransfer);
                            }


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
                            schoolFlag=true;
                            String url = getUrl(incidentLatitude, incidentLongitude, School);
                            Object[] DataTransfer = new Object[2];
                            DataTransfer[0] = mMap;
                            DataTransfer[1] = url;
                            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                            getNearbyPlacesData.execute(DataTransfer);

                            for(Marker m: myMarkers){
                                if(m==incidentLocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }

                        } else {
                            schoolFlag=false;
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
                            if(hospitalFlag){
                                String url = getUrl(incidentLatitude, incidentLongitude, "hospital");
                                Object[] DataTransfer = new Object[2];
                                DataTransfer[0] = mMap;
                                DataTransfer[1] = url;
                                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                                getNearbyPlacesData.execute(DataTransfer);
                            }



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
                            for(Marker m: myMarkers){
                                if(m==incidentLocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }

                        }else{
                            mMap.setTrafficEnabled(false);

                        }
                    }
                });




                //expandable list view
                expListView.setVisibility(View.VISIBLE);
                targetHeight=expListView.getHeight();

                expListView.setOnTouchListener(new OnSwipeTouchListener(MapsActivity.this){
                    @Override
                    public void onSwipeLeft() {
                        super.onSwipeLeft();
                        ResizeAnimation resizeAnimation = new ResizeAnimation(expListView,70,expListView.getWidth(),70,expListView.getHeight());
                        resizeAnimation.setDuration(500);
                        expListView.startAnimation(resizeAnimation);

                        LatLng newPosition = mMap.getCameraPosition().target;
                        double distance = getDistance(newPosition.latitude, newPosition.longitude);
                        Log.d("DistanceMovedSwipeLeft", ((String.valueOf(distance)) ));
                        if(distance > 10){
                            //Log.d("Camera movement change","camera moved");

                            Toast.makeText(MapsActivity.this, "Camera moved", Toast.LENGTH_SHORT).show();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                            for(Marker m:myMarkers){
                                if(m.isVisible()){
                                    m.showInfoWindow();
                                }else{
                                    incidentLocationMarker.setVisible(true);
                                    incidentLocationMarker.showInfoWindow();
                                }
                            }
                        }

                    }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                    ResizeAnimation resizeAnimation = new ResizeAnimation(expListView,650,expListView.getWidth(),targetHeight,70);
                    resizeAnimation.setDuration(200);
                    expListView.startAnimation(resizeAnimation);

                    LatLng newPosition = mMap.getCameraPosition().target;
                    double distance = getDistance(newPosition.latitude, newPosition.longitude);
                    Log.d("DistanceMovedSwipeRight", ((String.valueOf(distance)) ));
                    if(distance > 10){
                        Log.d("Camera movement change","camera moved");
                        Toast.makeText(MapsActivity.this, "Camera moved", Toast.LENGTH_SHORT).show();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                        for(Marker m:myMarkers){
                            if(m.isVisible()){
                                m.showInfoWindow();
                            }else{
                                incidentLocationMarker.setVisible(true);
                                incidentLocationMarker.showInfoWindow();
                            }
                        }

                    }


                }
                });



                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

                        expListView.collapseGroup(groupPosition);

                        TextView childText = (TextView) view.findViewById(R.id.lblListItem);
                        String data = (String) childText.getText();


                        listDataHeader = new ArrayList<String>();
                        // Adding child data
                        listDataHeader.add("Incident Updates:"+data);
                        listDataChild.put(listDataHeader.get(0),(oldUpdates));
                        listAdapter = new ExpandableListAdapter(MapsActivity.this, listDataHeader, listDataChild);
                        expListView.setAdapter(listAdapter);
                        prepareIncidentListData(data);
                        incidentListAdapter = new ExpandableIncidentListAdapter(MapsActivity.this, incidentListDataHeader, incidentListDataChild);
                        incidentDetailsListView.setAdapter(incidentListAdapter);

                        incidentDetailsListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                return true;
                            }
                        });

                        if(data.contains("edema")){
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    incidentDetailsListView.requestFocusFromTouch();
                                    incidentDetailsListView.setSelection(3);
                                    incidentDetailsListView.requestFocus();

                                    Marker chemicalLocationMarker = mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Ricin Toxin").snippet("can cause: Itching, Blindness, Redness, Scaling, Blistering, Coughing, Sneezing" + "\n" + "\n" + "Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"));
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {

                                            LinearLayout info = new LinearLayout(MapsActivity.this);
                                            info.setOrientation(LinearLayout.VERTICAL);

                                            TextView title = new TextView(MapsActivity.this);
                                            title.setTextColor(Color.BLACK);
                                            title.setTypeface(null, Typeface.BOLD);
                                            title.setText(marker.getTitle());

                                            TextView snippet = new TextView(MapsActivity.this);
                                            snippet.setTextColor(Color.DKGRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);

                                            return info;
                                        }
                                    });
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                    chemicalLocationMarker.showInfoWindow();
                                    myMarkers.add(chemicalLocationMarker);
                                    for (Marker m : myMarkers) {
                                        if (m == chemicalLocationMarker) {
                                            m.setVisible(true);
                                        } else {
                                            m.setVisible(false);
                                        }

                                    }
                                }
                            });
                        }
                        if(data.contains("Middletown police")) {
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    incidentDetailsListView.requestFocusFromTouch();
                                    incidentDetailsListView.smoothScrollToPosition(12);
                                    incidentDetailsListView.requestFocus();
                                    Marker responder1LocationMarker = mMap.addMarker(new MarkerOptions().position(victim3Location).title("First Responders: Middletown Police Department 147.0 feet from incident location").snippet("Susceptible to nearby hazards: Sodium Borohydride" + "\n" + "\n" + "Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {

                                            LinearLayout info = new LinearLayout(MapsActivity.this);
                                            info.setOrientation(LinearLayout.VERTICAL);

                                            TextView title = new TextView(MapsActivity.this);
                                            title.setTextColor(Color.BLACK);
                                            title.setTypeface(null, Typeface.BOLD);
                                            title.setText(marker.getTitle());

                                            TextView snippet = new TextView(MapsActivity.this);
                                            snippet.setTextColor(Color.DKGRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);

                                            return info;
                                        }
                                    });
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                    responder1LocationMarker.showInfoWindow();
                                    myMarkers.add(responder1LocationMarker);
                                    for (Marker m : myMarkers) {
                                        if (m == responder1LocationMarker) {
                                            m.setVisible(true);
                                        } else {
                                            m.setVisible(false);
                                        }

                                    }
                                }
                            });
                        }
                        if(data.contains("EMS")){
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    incidentDetailsListView.requestFocusFromTouch();
                                    incidentDetailsListView.smoothScrollToPosition(11);
                                    incidentDetailsListView.requestFocus();
                                    Marker responder2LocationMarker=mMap.addMarker(new MarkerOptions().position(victim2Location).title("First Responders:EMS. 209.0 feet from incident location").snippet("Susceptible to nearby hazards: Sodium Borohydride"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {

                                            LinearLayout info = new LinearLayout(MapsActivity.this);
                                            info.setOrientation(LinearLayout.VERTICAL);

                                            TextView title = new TextView(MapsActivity.this);
                                            title.setTextColor(Color.BLACK);
                                            title.setTypeface(null, Typeface.BOLD);
                                            title.setText(marker.getTitle());

                                            TextView snippet = new TextView(MapsActivity.this);
                                            snippet.setTextColor(Color.DKGRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);

                                            return info;
                                        }
                                    });
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                    responder2LocationMarker.showInfoWindow();
                                    myMarkers.add(responder2LocationMarker);
                                    for(Marker m: myMarkers) {
                                        if (m == responder2LocationMarker) {
                                            m.setVisible(true);
                                        } else {
                                            m.setVisible(false);
                                        }

                                    }
                                }
                            });
                        }
                        if(data.contains("Sodium")){
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    incidentDetailsListView.requestFocusFromTouch();
                                    incidentDetailsListView.setSelection(2);
                                    incidentDetailsListView.requestFocus();
                                    Marker sodiumBorohydrideLocationMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Sodium Borohydride").snippet("Physical description: Solid White Grayish powder. Easily soluble in cold water"+"\n"+"\n"+"Formula: NaBH4"+"\n"+"\n"+"Synonym: Sodium Tetrahydrodorate"+"\n"+"\n"+"can cause: Itching, Blindness, Redness, Scaling, Blistering, Coughing, Sneezing"+"\n"+"\n"+"Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"));
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {

                                            LinearLayout info = new LinearLayout(MapsActivity.this);
                                            info.setOrientation(LinearLayout.VERTICAL);

                                            TextView title = new TextView(MapsActivity.this);
                                            title.setTextColor(Color.BLACK);
                                            title.setTypeface(null, Typeface.BOLD);
                                            title.setText(marker.getTitle());

                                            TextView snippet = new TextView(MapsActivity.this);
                                            snippet.setTextColor(Color.DKGRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);

                                            return info;
                                        }
                                    });
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                    sodiumBorohydrideLocationMarker.showInfoWindow();
                                    myMarkers.add(sodiumBorohydrideLocationMarker);

                                    for(Marker m: myMarkers){
                                        if(m==sodiumBorohydrideLocationMarker){
                                            m.setVisible(true);
                                        }else{
                                            m.setVisible(false);
                                        }
                                    }


                                }
                            });
                        }if(data.contains("accident")) {
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    Marker incidentMarker = mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Vehicular Accident").snippet("can cause: Cuts, Bruises, Break, Unconsciousness" + "\n" + "\n" + "Suggested mitigations: Seat Belt, Air bag"));
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {

                                            LinearLayout info = new LinearLayout(MapsActivity.this);
                                            info.setOrientation(LinearLayout.VERTICAL);

                                            TextView title = new TextView(MapsActivity.this);
                                            title.setTextColor(Color.BLACK);
                                            title.setTypeface(null, Typeface.BOLD);
                                            title.setText(marker.getTitle());

                                            TextView snippet = new TextView(MapsActivity.this);
                                            snippet.setTextColor(Color.DKGRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);

                                            return info;
                                        }
                                    });
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                    incidentMarker.showInfoWindow();
                                    for (Marker m : myMarkers) {
                                        if (m == incidentMarker) {
                                            m.setVisible(true);
                                            m.showInfoWindow();
                                        } else {
                                            m.setVisible(false);
                                        }


                                    }
                                }
                            });
                        }

                        return false;
                    }
                });

                final String[] data={"Vehicular accident at Middletown and symptoms found broken leg, red skin, blisters, scaling, cuts, bruises","First responders arrived: Middletown police department","EMS support requested","Sodium borohydride presence detected","Driver and Car passenger experience breathing difficulty and their lips and eyes are swollen with edema"};
                final ArrayList<String> myUpdates = new ArrayList<String>();
                        //"Bystanders moved the truck driver and police van driver to the side of the road","EMS support requested","Symptoms found: cuts, bruises, red skin, blisters, breathing difficulty, edema","Sodium borohydride presence detected","Police van driver found","Police van driver exhibits edema"};
                final Handler handler = new Handler();



                handler.postDelayed(new Runnable() {
                    int i=0;
                    int j=-1;

                    final Handler h = new Handler();
                    final int delay = 100;







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

                            incidentDetailsListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                @Override
                                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                    return true;
                                }
                            });

                            if(data[i].contains("edema")){
                                incidentDetailsListView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        incidentDetailsListView.requestFocusFromTouch();
                                        incidentDetailsListView.setSelection(3);
                                        incidentDetailsListView.requestFocus();
                                        Marker chemicalLocationMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Ricin Toxin").snippet("can cause: Fever, Cough, Pulmonary edema"+"\n"+"\n"+"Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"+"\n"+"\n"+"First Aid: Remove contaminated clothes, Never give anything to drink for an unconscious victim"));
                                        if(myMarkers.contains(chemicalLocationMarker)){

                                        }else{
                                            myMarkers.add(chemicalLocationMarker);
                                        }

                                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                            @Override
                                            public View getInfoWindow(Marker arg0) {
                                                return null;
                                            }

                                            @Override
                                            public View getInfoContents(Marker marker) {

                                                LinearLayout info = new LinearLayout(MapsActivity.this);
                                                info.setOrientation(LinearLayout.VERTICAL);

                                                TextView title = new TextView(MapsActivity.this);
                                                title.setTextColor(Color.BLACK);
                                                title.setTypeface(null, Typeface.BOLD);
                                                title.setText(marker.getTitle());

                                                TextView snippet = new TextView(MapsActivity.this);
                                                snippet.setTextColor(Color.DKGRAY);
                                                snippet.setText(marker.getSnippet());

                                                info.addView(title);
                                                info.addView(snippet);

                                                return info;
                                            }
                                        });
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                        //chemicalLocationMarker.showInfoWindow();
                                        myMarkers.add(chemicalLocationMarker);
                                        for(Marker m: myMarkers) {
                                            if (m == chemicalLocationMarker) {
                                                m.setVisible(true);
                                                m.showInfoWindow();
                                            } else {
                                                m.setVisible(false);
                                            }

                                        }
                                    }
                                });
                            }
                            if(data[i].contains("EMS")){
                                incidentDetailsListView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        incidentDetailsListView.requestFocusFromTouch();
                                        incidentDetailsListView.smoothScrollToPosition(13);
                                        incidentDetailsListView.setSelection(14);
                                        incidentDetailsListView.requestFocus();
                                        Marker responder2LocationMarker=mMap.addMarker(new MarkerOptions().position(victim2Location).title("First Responders:EMS. 209.0 feet from incident location").snippet("Susceptible to nearby hazards: Sodium Borohydride"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                                        if(myMarkers.contains(responder2LocationMarker)){

                                        }else{
                                            myMarkers.add(responder2LocationMarker);
                                        }

                                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                            @Override
                                            public View getInfoWindow(Marker arg0) {
                                                return null;
                                            }

                                            @Override
                                            public View getInfoContents(Marker marker) {

                                                LinearLayout info = new LinearLayout(MapsActivity.this);
                                                info.setOrientation(LinearLayout.VERTICAL);

                                                TextView title = new TextView(MapsActivity.this);
                                                title.setTextColor(Color.BLACK);
                                                title.setTypeface(null, Typeface.BOLD);
                                                title.setText(marker.getTitle());

                                                TextView snippet = new TextView(MapsActivity.this);
                                                snippet.setTextColor(Color.DKGRAY);
                                                snippet.setText(marker.getSnippet());

                                                info.addView(title);
                                                info.addView(snippet);

                                                return info;
                                            }
                                        });
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                        //responder2LocationMarker.showInfoWindow();
                                        myMarkers.add(responder2LocationMarker);
                                        for(Marker m: myMarkers) {
                                            if (m == responder2LocationMarker) {
                                                m.setVisible(true);
                                                m.showInfoWindow();
                                            } else {
                                                m.setVisible(false);
                                            }

                                        }
                                    }
                                });
                            }
                            if(data[i].contains("Middletown police")){
                                incidentDetailsListView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        incidentDetailsListView.requestFocusFromTouch();
                                        incidentDetailsListView.smoothScrollToPosition(13);
                                        incidentDetailsListView.setSelection(12);
                                        incidentDetailsListView.requestFocus();
                                        Marker responder1LocationMarker=mMap.addMarker(new MarkerOptions().position(victim3Location).title("First Responders: Middletown Police Department 147.0 feet from incident location").snippet("Susceptible to nearby hazards: Sodium Borohydride"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                                        if(myMarkers.contains(responder1LocationMarker)){

                                        }else{
                                            myMarkers.add(responder1LocationMarker);
                                        }
                                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                            @Override
                                            public View getInfoWindow(Marker arg0) {
                                                return null;
                                            }

                                            @Override
                                            public View getInfoContents(Marker marker) {

                                                LinearLayout info = new LinearLayout(MapsActivity.this);
                                                info.setOrientation(LinearLayout.VERTICAL);

                                                TextView title = new TextView(MapsActivity.this);
                                                title.setTextColor(Color.BLACK);
                                                title.setTypeface(null, Typeface.BOLD);
                                                title.setText(marker.getTitle());

                                                TextView snippet = new TextView(MapsActivity.this);
                                                snippet.setTextColor(Color.DKGRAY);
                                                snippet.setText(marker.getSnippet());

                                                info.addView(title);
                                                info.addView(snippet);

                                                return info;
                                            }
                                        });
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                        //responder1LocationMarker.showInfoWindow();
                                        myMarkers.add(responder1LocationMarker);
                                        for(Marker m: myMarkers) {
                                            if (m == responder1LocationMarker) {
                                                m.setVisible(true);
                                                m.showInfoWindow();
                                            } else {
                                                m.setVisible(false);
                                            }

                                        }
                                    }
                                });
                            }
                            if(data[i].contains("Sodium")){
                                incidentDetailsListView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        incidentDetailsListView.requestFocusFromTouch();
                                        incidentDetailsListView.setSelection(2);
                                        incidentDetailsListView.requestFocus();
                                        Marker sodiumBorohydrideLocationMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Sodium Borohydride").snippet("Physical description: Solid White Grayish powder. Easily soluble in cold water"+"\n"+"\n"+"can cause: Itching, Blindness, Redness, Scaling, Blistering, Coughing, Sneezing"+"\n"+"\n"+"Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"+"\n"+"\n"+"First Aid: Remove contaminated clothes, Wash with a disinfectant, Allow the victim to rest in a well ventillated area"));
                                        if(myMarkers.contains(sodiumBorohydrideLocationMarker)){

                                        }else{
                                            myMarkers.add(sodiumBorohydrideLocationMarker);
                                        }
                                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                            @Override
                                            public View getInfoWindow(Marker arg0) {
                                                return null;
                                            }

                                            @Override
                                            public View getInfoContents(Marker marker) {

                                                LinearLayout info = new LinearLayout(MapsActivity.this);
                                                info.setOrientation(LinearLayout.VERTICAL);

                                                TextView title = new TextView(MapsActivity.this);
                                                title.setTextColor(Color.BLACK);
                                                title.setTypeface(null, Typeface.BOLD);
                                                title.setText(marker.getTitle());

                                                String s=marker.getSnippet();
                                                Log.d("marker:","snippet:"+s);


                                                TextView snippet = new TextView(MapsActivity.this);
                                                snippet.setTextColor(Color.DKGRAY);
                                                snippet.setText(marker.getSnippet());

                                                info.addView(title);
                                                info.addView(snippet);

                                                return info;
                                            }
                                        });
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                        //sodiumBorohydrideLocationMarker.showInfoWindow();
                                        myMarkers.add(sodiumBorohydrideLocationMarker);

                                        for(Marker m: myMarkers){
                                            if(m==sodiumBorohydrideLocationMarker){
                                                m.setVisible(true);
                                                m.showInfoWindow();
                                            }else{
                                                m.setVisible(false);
                                            }
                                        }

                                    }
                                });
                            }if(data[i].contains("accident")) {
                                incidentDetailsListView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Marker incidentMarker = mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Vehicular Accident").snippet("can cause: Cuts, Bruises, Break, Unconsciousness" + "\n" + "\n" + "Suggested mitigations: Seat Belt, Air bag"));
                                        if(myMarkers.contains(incidentMarker)){

                                        }else{
                                            myMarkers.add(incidentMarker);
                                        }
                                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                            @Override
                                            public View getInfoWindow(Marker arg0) {
                                                return null;
                                            }

                                            @Override
                                            public View getInfoContents(Marker marker) {

                                                LinearLayout info = new LinearLayout(MapsActivity.this);
                                                info.setOrientation(LinearLayout.VERTICAL);

                                                TextView title = new TextView(MapsActivity.this);
                                                title.setTextColor(Color.BLACK);
                                                title.setTypeface(null, Typeface.BOLD);
                                                title.setText(marker.getTitle());

                                                TextView snippet = new TextView(MapsActivity.this);
                                                snippet.setTextColor(Color.DKGRAY);
                                                snippet.setText(marker.getSnippet());

                                                info.addView(title);
                                                info.addView(snippet);

                                                return info;
                                            }
                                        });
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                        //incidentMarker.showInfoWindow();
                                        for (Marker m : myMarkers) {
                                            if (m == incidentMarker) {
                                                m.setVisible(true);
                                                m.showInfoWindow();
                                            } else {
                                                m.setVisible(false);
                                            }


                                        }
                                    }
                                });
                            }

                        }else{

                        }

                        i=i+1;
                        j=j+1;
                        LatLng newPosition = mMap.getCameraPosition().target;
                        double distance = getDistance(newPosition.latitude, newPosition.longitude);
                        Log.d("Distance moved", ((String.valueOf(distance)) ));
                        if(distance > 10){
                            Log.d("Camera movement change","camera moved");
                            Toast.makeText(MapsActivity.this, "Camera moved", Toast.LENGTH_SHORT).show();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                            for(Marker m:myMarkers){
                                if(m.isVisible()){
                                    String markertitle=m.getTitle();
                                    Log.d("MARKER TITLE",markertitle);
//                                    m.showInfoWindow();
                                }else{
                                   //m.setVisible(false);
                                    incidentLocationMarker.setVisible(true);
                                    //incidentLocationMarker.showInfoWindow();
                                }
                            }
                        }
                        handler.postDelayed(this, 20000);

                    }




                }, 1000);


                incidentDetailsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                        expListView.collapseGroup(0);

                        incidentDetailsListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                return true;
                            }
                        });
                        TextView childText = (TextView) view.findViewById(R.id.lblIncidentListItem);
                        String childData = (String) childText.getText();
                        if(childData.contains("Todd")){
                            float victim1distance=getDistance(victim1Latitude,victim1Longitude);
                            float distanceInFeet = (float) round((victim1distance*3.2808));
                            Marker victim1LocationMarker=mMap.addMarker(new MarkerOptions().position(victim1Location).title("Victim:"+"\t"+childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Break,Bruise,Cuts,Redness,Blistering"+"\n"+"\n"+"Exposed to: Vehicular Accident and Sodium Borohydride"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));

                            InfoWindowData infom = new InfoWindowData();

                            infom.setDetails("Vehicular accident"+"\t"+"\t"+"Sodium Borohydride");

                            infom.setExhibits("Break,"+"\t"+"Bruise,"+"\t"+"Cuts"+"\t"+"\t"+"Redness,"+"\t"+"Blistering");

                            infom.setMitigations("Remove contaminated clothes"+"\n"+"Wash with a disinfectant"+"\n"+"Allow the victim to rest in a well ventillated area");


                            CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(MapsActivity.this);

                            mMap.setInfoWindowAdapter(customInfoWindow);
                            victim1LocationMarker.setTag(infom);
                            //victim1LocationMarker.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim1Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            if(myMarkers.contains(victim1LocationMarker)){

                            }else{
                                myMarkers.add(victim1LocationMarker);
                            }

                            for(Marker m: myMarkers){
                                if(m==victim1LocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }

                        }else if(childData.contains("Leicy")){
                            float victim2distance=getDistance(victim2Latitude,victim2Longitude);
                            float distanceInFeet = (float) round((victim2distance*3.2808));
                            Marker victim2LocationMarker=mMap.addMarker(new MarkerOptions().position(victim2Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Cut,Redness,Edema,Wheezing,Blistering,Unconsciousness"+"\n"+"\n"+"Exposed to: Vehicular Accident and Sodium Borohydride"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                            InfoWindowData infom = new InfoWindowData();

                            infom.setDetails("Vehicular accident"+"\t"+"\t"+"Sodium Borohydride"+"\t"+"\t"+"Ricin Toxin");

                            infom.setExhibits("Cuts, Unconsciousness"+"\t"+"Redness, Wheezing, Blistering"+"\t"+"Pulmonary Edema");

                            infom.setMitigations("Remove contaminated clothes"+"\n"+"Wash with a disinfectant"+"\n"+"Allow the victim to rest in a well ventillated area"+"\n"+"Never give anything to drink for an unconscious victim");


                            CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(MapsActivity.this);

                            mMap.setInfoWindowAdapter(customInfoWindow);
                            victim2LocationMarker.setTag(infom);
                            victim2LocationMarker.showInfoWindow();

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim2Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            //victim2LocationMarker.showInfoWindow();
                            if(myMarkers.contains(victim2LocationMarker)){

                            }else{
                                myMarkers.add(victim2LocationMarker);
                            }
                            for(Marker m: myMarkers){
                                if(m==victim2LocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Chris")){
                            float victim3distance=getDistance(victim3Latitude,victim3Longitude);
                            float distanceInFeet = (float) round((victim3distance*3.2808));
                            Marker victim3LocationMarker=mMap.addMarker(new MarkerOptions().position(victim3Location).title("Victim:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Bruise,Cut,Redness,Edema,Wheezing,Blistering"+"\n"+"\n"+"Exposed to: Vehicular Accident, Sodium Borohydride and Ricin Toxin"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                            InfoWindowData infom = new InfoWindowData();

                            infom.setDetails("Vehicular accident"+"\t"+"\t"+"Sodium Borohydride"+"\t"+"\t"+"Ricin Toxin ");


                            infom.setExhibits("Cuts, Unconsciousness"+"\t"+"Redness, Wheezing, Blistering"+"\t"+"Pulmonary Edema");

                            infom.setMitigations("Remove contaminated clothes"+"\n"+"Wash with a disinfectant"+"\n"+"Allow the victim to rest in a well ventillated area"+"\n"+"Never give anything to drink for an unconscious victim");

                            CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(MapsActivity.this);

                            mMap.setInfoWindowAdapter(customInfoWindow);
                            victim3LocationMarker.setTag(infom);
                            victim3LocationMarker.showInfoWindow();

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim3Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            //victim3LocationMarker.showInfoWindow();
                            if(myMarkers.contains(victim3LocationMarker)){

                            }else{
                                myMarkers.add(victim3LocationMarker);
                            }
                            for(Marker m: myMarkers){
                                if(m==victim3LocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Middletown")){
                            float victim3distance=getDistance(victim3Latitude,victim3Longitude);
                            float distanceInFeet = (float) round((victim3distance*3.2808));
                            Marker responder1LocationMarker=mMap.addMarker(new MarkerOptions().position(victim3Location).title("First Responders:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Susceptible to nearby hazards: Sodium Borohydride"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(MapsActivity.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(MapsActivity.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(MapsActivity.this);
                                    snippet.setTextColor(Color.DKGRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim3Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            //responder1LocationMarker.showInfoWindow();
                            if(myMarkers.contains(responder1LocationMarker)){

                            }else{
                                myMarkers.add(responder1LocationMarker);
                            }
                            for(Marker m: myMarkers){
                                if(m==responder1LocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("EMS")){
                            float victim2distance=getDistance(victim2Latitude,victim2Longitude);
                            float distanceInFeet = (float) round((victim2distance*3.2808));
                            Marker responder2LocationMarker=mMap.addMarker(new MarkerOptions().position(victim2Location).title("First Responders:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Susceptible to nearby hazards: Sodium Borohydride, Ricin Toxin"+"\n"+"\n"+"Suggested Mitigations: Air bag, Seat belt,Gloves, Boots, Self contained breathing apparatus, Splash goggles"));
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(MapsActivity.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(MapsActivity.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(MapsActivity.this);
                                    snippet.setTextColor(Color.DKGRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(victim2Location));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            //responder2LocationMarker.showInfoWindow();
                            if(myMarkers.contains(responder2LocationMarker)){

                            }else{
                                myMarkers.add(responder2LocationMarker);
                            }
                            for(Marker m: myMarkers){
                                if(m==responder2LocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Accident")){
                            Marker incidentMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Vehicular Accident").snippet("can cause: Cuts, Bruises, Break, Unconsciousness"+"\n"+"\n"+"Suggested mitigations: Seat Belt, Air bag"));
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(MapsActivity.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(MapsActivity.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(MapsActivity.this);
                                    snippet.setTextColor(Color.DKGRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                            //incidentMarker.showInfoWindow();
                            if(myMarkers.contains(incidentMarker)){

                            }else{
                                myMarkers.add(incidentMarker);
                            }
                            for(Marker m: myMarkers){
                                if(m==incidentMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }


                        }else if(childData.contains("Sodium")){
                            Marker sodiumBorohydrideLocationMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Sodium Borohydride").snippet("Physical description: Solid White Grayish powder"+"\n"+"\n"+"causes: Itching, Blindness, Redness, Scaling, Blistering, Coughing, Wheezing"+"\n"+"\n"+"Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"+"\n"+"\n"+"First Aid: Remove contaminated clothes, Wash with a disinfectant, Allow the victim to rest in a well ventillated area"));
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(MapsActivity.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(MapsActivity.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(MapsActivity.this);
                                    snippet.setTextColor(Color.DKGRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                            //sodiumBorohydrideLocationMarker.showInfoWindow();
                            if(myMarkers.contains(sodiumBorohydrideLocationMarker)){

                            }else{
                                myMarkers.add(sodiumBorohydrideLocationMarker);
                            }
                            for(Marker m: myMarkers){
                                if(m==sodiumBorohydrideLocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }
                        }else if(childData.contains("Ricin")){
                            Marker chemicalLocationMarker=mMap.addMarker(new MarkerOptions().position(incidentLocation).title("Ricin Toxin").snippet("causes: Fever, Cough, Pulmonary edema"+"\n"+"\n"+"Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"+"\n"+"\n"+"First Aid: Remove contaminated clothes, Never give anything to drink for an unconscious victim"));
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(MapsActivity.this);
                                    info.setOrientation(LinearLayout.VERTICAL);

                                    TextView title = new TextView(MapsActivity.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(MapsActivity.this);
                                    snippet.setTextColor(Color.DKGRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    return info;
                                }
                            });
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                            //chemicalLocationMarker.showInfoWindow();
                            if(myMarkers.contains(chemicalLocationMarker)){

                            }else{
                                myMarkers.add(chemicalLocationMarker);
                            }
                            for(Marker m: myMarkers){
                                if(m==chemicalLocationMarker){
                                    m.setVisible(true);
                                    m.showInfoWindow();
                                }else{
                                    m.setVisible(false);
                                }
                            }
                        }
                        else if(childData.contains("Will") || childData.contains("Sam")){

                                float bystander1distance = getDistance(bystander1Latitude,bystander1Longitude);
                                float distanceInFeet = (float) round((bystander1distance*3.2808));
                                Marker bystander1LocationMarker=mMap.addMarker(new MarkerOptions().position(bystander1Location).title("Bystander:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Susceptible to: Sodium Borohydride Exposure "+"\n"+"\n"+"Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"));
                                InfoWindowData infom = new InfoWindowData();

                                infom.setDetails("Sodium Borohydride");

                                infom.setExhibits("Redness,"+"\t"+"Scaling");

                                infom.setMitigations("Remove contaminated clothes"+"\n"+"Wash with a disinfectant"+"\n"+"Allow the victim to rest in a well ventillated area");


                                CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(MapsActivity.this);

                                mMap.setInfoWindowAdapter(customInfoWindow);
                                bystander1LocationMarker.setTag(infom);
                                bystander1LocationMarker.showInfoWindow();

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(bystander1Location));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                //bystander1LocationMarker.showInfoWindow();
                                if(myMarkers.contains(bystander1LocationMarker)){

                                }else{
                                    myMarkers.add(bystander1LocationMarker);
                                }
                                for(Marker m: myMarkers){
                                    if(m==bystander1LocationMarker){
                                        m.setVisible(true);
                                        m.showInfoWindow();
                                    }else{
                                        m.setVisible(false);
                                    }
                                }

                        }else if(childData.contains("Jessica") || childData.contains("Brandon")){
                            if(myUpdates.toString().contains("edema")){
                                float bystander2distance = getDistance(bystander2Latitude,bystander2Longitude);
                                float distanceInFeet = (float) round((bystander2distance*3.2808));
                                Marker bystander2LocationMarker=mMap.addMarker(new MarkerOptions().position(bystander2Location).title("Bystander:" +childData+". "+distanceInFeet+" "+"feet from incident location").snippet("Exhibits: Scaling, Redness"+"\n"+"\n"+"Exposed To: Sodium Borohydride Exposure"+"Suggested mitigations: Gloves, Self Breathing apparatus, Boots, Splash goggles"));
                                InfoWindowData infom = new InfoWindowData();

                                infom.setDetails("Sodium Borohydride"+"\t"+"\t"+"Ricin Toxin");

                                infom.setExhibits("");

                                infom.setMitigations("");


                                CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(MapsActivity.this);

                                mMap.setInfoWindowAdapter(customInfoWindow);
                                bystander2LocationMarker.setTag(infom);
                                bystander2LocationMarker.showInfoWindow();

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(bystander2Location));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                //bystander2LocationMarker.showInfoWindow();
                                if(myMarkers.contains(bystander2LocationMarker)){

                                }else{
                                    myMarkers.add(bystander2LocationMarker);
                                }
                                for(Marker m: myMarkers){
                                    if(m==bystander2LocationMarker){
                                        m.setVisible(true);
                                        m.showInfoWindow();

                                    }else{
                                        m.setVisible(false);
                                    }
                                }
                            }else{
                                float bystander2distance = getDistance(bystander2Latitude,bystander2Longitude);
                                float distanceInFeet = (float) round((bystander2distance*3.2808));
                                Marker bystander2LocationMarker=mMap.addMarker(new MarkerOptions().position(bystander2Location).title("Bystander:" +childData+". "+distanceInFeet+" "+"feet from incident location"));
                                InfoWindowData infom = new InfoWindowData();

                                infom.setDetails("Sodium Borohydride");

                                infom.setExhibits("");

                                infom.setMitigations("");


                                CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(MapsActivity.this);

                                mMap.setInfoWindowAdapter(customInfoWindow);
                                bystander2LocationMarker.setTag(infom);
                                bystander2LocationMarker.showInfoWindow();
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(bystander2Location));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                //bystander2LocationMarker.showInfoWindow();
                                if(myMarkers.contains(bystander2LocationMarker)){

                                }else{
                                    myMarkers.add(bystander2LocationMarker);
                                }
                                for(Marker m: myMarkers){
                                    if(m==bystander2LocationMarker){
                                        m.setVisible(true);
                                        m.showInfoWindow();
                                    }else{
                                        m.setVisible(false);
                                    }
                                }
                            }



                        }else if(childData.contains("Cuts")||childData.contains("Bruises")){
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    incidentDetailsListView.requestFocusFromTouch();
                                    incidentDetailsListView.smoothScrollToPosition(1);
                                    incidentDetailsListView.setSelection(1);
                                    incidentDetailsListView.requestFocus();

                                }
                            });

                        }else if(childData.contains("Red skin")||childData.contains("Blisters")||childData.contains("Breathing Difficulty")){
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    incidentDetailsListView.requestFocusFromTouch();
                                    incidentDetailsListView.smoothScrollToPosition(2);
                                    incidentDetailsListView.setSelection(2);
                                    incidentDetailsListView.requestFocus();

                                }
                            });

                        }else if(childData.contains("edema")){
                            incidentDetailsListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    incidentDetailsListView.requestFocusFromTouch();
                                    incidentDetailsListView.smoothScrollToPosition(3);
                                    incidentDetailsListView.setSelection(3);
                                    incidentDetailsListView.requestFocus();

                                }
                            });}
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
    private int getCount() {
        int count = 0;
        for (int i = 0; i < incidentListAdapter.getGroupCount(); i++) {
            for (int j = 0; j < incidentListAdapter.getChildrenCount(i); j++) {
                count++;
            }
            count++;
        }
        return count;
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
                incidentUpdates.add(oldUpdates.get(i));
            }
        }
        incidentUpdates.add(0,data1);
        listDataChild.put(listDataHeader.get(0),(incidentUpdates));// Header, Child data


        oldUpdates.add(0,data1);
    }


    private void prepareIncidentListData(String data) {

        if(data.contains("Vehicular accident")){
            incidentListDataHeader = new ArrayList<String>();
            incidentListDataChild = new HashMap<String,List<String>>();
            // Adding child data
            incidentListDataHeader.add("Hazards");
            incidentListDataHeader.add("Victims");
            incidentListDataHeader.add("Bystanders");
            incidentListDataHeader.add("Responders");
            incidentListDataHeader.add("Symptoms found on scene");

            // Adding child data
            hazardsList = new ArrayList<String>();
            hazardsList.add("Vehicular Accident");
            hazardsList.add("Sodium Borohydride");
            victimsList = new ArrayList<String>();
            victimsList.add("Winston, Todd");
            victimsList.add("Leicy, Mary");
            victimsList.add("Stevenson, Chris");
            symptomsList = new ArrayList<String>();
            symptomsList.add("Cuts, Bruises, Broken leg");
            symptomsList.add("Red skin, Blisters, Scaling");
            bystandersList = new ArrayList<String>();
            bystandersList.add("Connor, Will");
            bystandersList.add("Smith, Sam");
            bystandersList.add("Taylor, Jessica");
            bystandersList.add("Duber, Brandon");

            respondersList = new ArrayList<String>();
            respondersList.add("No Responders available on scene");
            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList);
            incidentListDataChild.put(incidentListDataHeader.get(1), victimsList);
            incidentListDataChild.put(incidentListDataHeader.get(2), bystandersList);
            incidentListDataChild.put(incidentListDataHeader.get(3), respondersList);
            incidentListDataChild.put(incidentListDataHeader.get(4), symptomsList);
              // Header, Child data
        }
        if(data.contains("Middletown police")){
            incidentListDataHeader = new ArrayList<String>();
            incidentListDataChild = new HashMap<String,List<String>>();
            // Adding child data
            incidentListDataHeader.add("Hazards");
            incidentListDataHeader.add("Victims");
            incidentListDataHeader.add("Bystanders");
            incidentListDataHeader.add("Responders");
            incidentListDataHeader.add("Symptoms found on scene");
            // Adding child data
            hazardsList = new ArrayList<String>();
            hazardsList.add("Vehicular Accident");
            hazardsList.add("Sodium Borohydride");
            victimsList = new ArrayList<String>();
            victimsList.add("Winston, Todd");
            victimsList.add("Leicy, Mary");
            victimsList.add("Stevenson, Chris");

            respondersList = new ArrayList<String>();
            respondersList.add("Middletown police Department");

            symptomsList = new ArrayList<String>();
            symptomsList.add("Cuts, Bruises, Broken leg");
            symptomsList.add("Red skin, Blisters, Scaling");
            bystandersList = new ArrayList<String>();
            bystandersList.add("Connor, Will");
            bystandersList.add("Smith, Sam");
            bystandersList.add("Taylor, Jessica");
            bystandersList.add("Duber, Brandon");
            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList);
            incidentListDataChild.put(incidentListDataHeader.get(1), victimsList);
            incidentListDataChild.put(incidentListDataHeader.get(2), bystandersList);
            incidentListDataChild.put(incidentListDataHeader.get(3), respondersList);
            incidentListDataChild.put(incidentListDataHeader.get(4), symptomsList);
            // Header, Child data
        }
        if(data.contains("EMS")){
            incidentListDataHeader = new ArrayList<String>();
            incidentListDataChild = new HashMap<String,List<String>>();
            incidentListDataHeader.add("Hazards");
            incidentListDataHeader.add("Victims");
            incidentListDataHeader.add("Bystanders");
            incidentListDataHeader.add("Responders");
            incidentListDataHeader.add("Symptoms found on scene");

            // Adding child data
            hazardsList = new ArrayList<String>();
            hazardsList.add("Vehicular Accident");
            hazardsList.add("Sodium Borohydride");
            victimsList = new ArrayList<String>();
            victimsList.add("Winston, Todd");
            victimsList.add("Leicy, Mary");
            victimsList.add("Stevenson, Chris");
            respondersList = new ArrayList<String>();
            respondersList.add("EMS");
            respondersList.add("Middletown police Department");
            symptomsList = new ArrayList<String>();
            symptomsList.add("Cuts, Bruises, Broken leg");
            symptomsList.add("Red skin, Blisters, Scaling");
            bystandersList = new ArrayList<String>();
            bystandersList.add("Connor, Will");
            bystandersList.add("Smith, Sam");
            bystandersList.add("Taylor, Jessica");
            bystandersList.add("Duber, Brandon");

            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList);
            incidentListDataChild.put(incidentListDataHeader.get(1), victimsList);
            incidentListDataChild.put(incidentListDataHeader.get(2), bystandersList);
            incidentListDataChild.put(incidentListDataHeader.get(3), respondersList);
            incidentListDataChild.put(incidentListDataHeader.get(4), symptomsList);;// Header, Child data
        }

        if(data.contains("edema")){
            incidentListDataHeader = new ArrayList<String>();
            incidentListDataChild = new HashMap<String,List<String>>();
            // Adding child data
            incidentListDataHeader.add("Hazards");
            incidentListDataHeader.add("Victims");
            incidentListDataHeader.add("Bystanders");
            incidentListDataHeader.add("Responders");
            incidentListDataHeader.add("Symptoms found on scene");
            hazardsList = new ArrayList<String>();
            hazardsList.add("Vehicular Accident");
            hazardsList.add("Sodium Borohydride");
            hazardsList.add("Ricin Toxin");
            victimsList = new ArrayList<String>();
            victimsList.add("Winston, Todd");
            victimsList.add("Leicy, Mary");
            victimsList.add("Stevenson, Chris");
            respondersList = new ArrayList<String>();
            respondersList.add("Middletown police Department");
            respondersList.add("EMS");
            bystandersList = new ArrayList<String>();
            bystandersList.add("Connor, Will");
            bystandersList.add("Smith, Sam");
            bystandersList.add("Taylor, Jessica");
            bystandersList.add("Duber, Brandon");
            symptomsList = new ArrayList<String>();
            symptomsList.add("Pulmonary edema");
            symptomsList.add("Cuts, Bruises, Broken leg");
            symptomsList.add("Red skin, Blisters, Scaling");

            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList);
            incidentListDataChild.put(incidentListDataHeader.get(1), victimsList);
            incidentListDataChild.put(incidentListDataHeader.get(3), respondersList);
            incidentListDataChild.put(incidentListDataHeader.get(2), bystandersList);
            incidentListDataChild.put(incidentListDataHeader.get(4), symptomsList);
        }
        if(data.contains("Sodium")){
            incidentListDataHeader = new ArrayList<String>();
            incidentListDataChild = new HashMap<String,List<String>>();
            incidentListDataHeader.add("Hazards");
            incidentListDataHeader.add("Victims");
            incidentListDataHeader.add("Bystanders");
            incidentListDataHeader.add("Responders");
            incidentListDataHeader.add("Symptoms found on scene");
            hazardsList = new ArrayList<String>();
            hazardsList.add("Vehicular Accident");
            hazardsList.add("Sodium Borohydride");
            victimsList = new ArrayList<String>();
            victimsList.add("Winston, Todd");
            victimsList.add("Leicy, Mary");
            victimsList.add("Stevenson, Chris");
            respondersList = new ArrayList<String>();
            respondersList.add("Middletown police Department");
            respondersList.add("EMS");
            bystandersList = new ArrayList<String>();
            bystandersList.add("Connor, Will");
            bystandersList.add("Smith, Sam");
            bystandersList.add("Taylor, Jessica");
            bystandersList.add("Duber, Brandon");
            symptomsList = new ArrayList<String>();
            symptomsList.add("Cuts, Bruises, Broken leg");
            symptomsList.add("Red skin, Blisters, Scaling");
            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList);
            incidentListDataChild.put(incidentListDataHeader.get(1), victimsList);
            incidentListDataChild.put(incidentListDataHeader.get(3), respondersList);
            incidentListDataChild.put(incidentListDataHeader.get(2), bystandersList);
            incidentListDataChild.put(incidentListDataHeader.get(4), symptomsList);


        }

    }

}

