package meenakshinagarajan.example.com.icsaugmentationtool;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.round;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RelativeLayout mMapView;
    List<Address> address = null;
    private double yourLatitude = 39.7845;
    private double yourLongitude = -84.0580;
    private double incidentLatitude = 39.53386;
    private double incidentLongitude = -84.371423;
    private int PROXIMITY_RADIUS = 10000;
    ExpandableListAdapter listAdapter;
    ExpandableIncidentListAdapter incidentListAdapter;
    //ExpandableRiskAdapter riskListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    LatLng incidentLocation = new LatLng(incidentLatitude, incidentLongitude);
    LatLng yourLocation = new LatLng(yourLatitude, yourLongitude);
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
    List<String> riskUpdates = new ArrayList<String>();
    List<String> riskDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> riskDataChild = new HashMap<String, List<String>>();
    //private RecyclerView riskDetailsListView;
    private RecyclerView.Adapter riskListAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        Marker currentLocationMarker = mMap.addMarker(new MarkerOptions().position(yourLocation).title("You are here:" + address.get(0).getAddressLine(0)));

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
                //TabLayout.Tab tab=tabLayout.getTabAt(0);
                //tab.select();
                tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()){
                            case 0:
                                incidentDetailsListView.setVisibility(View.VISIBLE);
                                riskDetailsListView.setVisibility(View.INVISIBLE);
                            case 1:
                        }

                    }
                   @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        switch (tab.getPosition()){
                            case 0:
                                incidentDetailsListView.setVisibility(View.INVISIBLE);
                                riskDetailsListView.setVisibility(View.VISIBLE);
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
                final String[] data={"Vehicular accident at Middletown","Vehicles involved: Chemical truck, Car, Police vehicle","First responders: Middletown police department","Bystanders moved the truck driver and police van driver to the side of the road","EMT support requested","Symptoms found: cuts, bruises, red skin, blisters, breathing difficulty, edema","Sodium borohydride presence detected","Police van driver exhibits edema"};
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
            riskDataHeader.add("Vehicular Accident");

            // Adding child data
            riskUpdates.add("canCause");
            riskUpdates.add("Cut");
            riskUpdates.add("Break");
            riskUpdates.add("Bruise");
            riskUpdates.add("Unconsciousness");
            riskDataChild.put(riskDataHeader.get(0),(riskUpdates));// Header, Child data

        }


    }

    private void prepareIncidentListData(String data) {

        if(data.contains("Vehicular accident")){
            // Adding child data
            incidentListDataHeader.add("Hazards");
            // Adding child data
            hazardsList.add("Vehicular accident");
            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList); // Header, Child data
        }
        if(data.contains("First responders")){
            // Adding child data
            incidentListDataHeader.add("Responders");
            //Adding child data
            respondersList.add("Middletown police Department");
            incidentListDataChild.put(incidentListDataHeader.get(1), respondersList);
        }
        if(data.contains("Bystanders")){
            // Adding child data
            incidentListDataHeader.add("Bystanders");
            bystandersList.add("Connor, Will");
            bystandersList.add("Smith, Sam");
            bystandersList.add("Taylor, Jessica");
            bystandersList.add("Duber, Brandon");
            incidentListDataChild.put(incidentListDataHeader.get(2), bystandersList);
        }
        if(data.contains("EMT")){
            // Adding child data
            respondersList.add("EMT");
            incidentListDataChild.put(incidentListDataHeader.get(1), respondersList);
        }
        if(data.contains("Symptoms")){
            // Adding child data
            incidentListDataHeader.add("Symptoms");
            symptomsList.add("Cuts");
            symptomsList.add("Bruises");
            symptomsList.add("Red skin");
            symptomsList.add("Blisters");
            symptomsList.add("Breathing difficulty");
            symptomsList.add("Edema");
            incidentListDataChild.put(incidentListDataHeader.get(3), symptomsList);
        }
        if(data.contains("Sodium")){
            hazardsList.add("Sodium Borohydride");
            hazardsList.add("Unknown Hazard");
            incidentListDataChild.put(incidentListDataHeader.get(0), hazardsList);
        }

    }

}

