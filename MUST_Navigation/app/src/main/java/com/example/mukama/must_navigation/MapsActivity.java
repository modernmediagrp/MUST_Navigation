package com.example.mukama.must_navigation;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {
    // GPS Location
    String mySearchList;
    GPSTracker gps=new GPSTracker(this);
    private Double lat[]={};
    private Double longt[]={};
    String myTitle[]={};
    StoreCoordinates myCordinateLocation;
    Boolean isConnection;
    ConnectionDetector connection;
    AlertManager alert=new AlertManager();
    GoogleMapOptions options = new GoogleMapOptions();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //getting search parameter from the previous class
        Bundle extras=getIntent().getExtras();
        mySearchList=extras.getString("List_Name");
        Toast.makeText(getApplicationContext(),"My Search List: "+mySearchList,Toast.LENGTH_LONG).show();
        //mapView=(MapView) findViewById(R.id.map);
        //mapView.setBuiltInZoomControls(true);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        connection=new ConnectionDetector(this);
        gps=new GPSTracker(this);
        isConnection=connection.isConnectingToInternet();
        if(!isConnection){
            alert.showAlertDialog(this,"Internet Setting","Check Internet Connection. Proceed to Settings", false);
            return;
        }
        else {
            if(gps.isGPSEnabled)
            {
                // the methods which are being applied getLatitude and getLongitude are being used to get the current location of the user
               // mMap.getCameraPosition().zoom(13);
                // the initial zoom size will be  18.0f
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),gps.getLongitude()),13.9f));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),gps.getLongitude()),18.0f));
                Marker marker=mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(gps.getLatitude(), gps.getLongitude()))
                        .title("You")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.you))
                        .snippet("Yourself"));
                marker.showInfoWindow();


            //showing faculties
                if(mySearchList.equals("Faculties")) {
                    //loop through cordinates for faculties
                    lat=myCordinateLocation.blockLat;
                    Toast.makeText(getApplicationContext(),"Showing: "+mySearchList,Toast.LENGTH_LONG).show();
                    for(int i=0;i<lat.length;i++){
                      mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.blockLat[i], myCordinateLocation.blockLong[i]))
                                .title(myCordinateLocation.blockNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.department_mapicon))
                                .snippet(myCordinateLocation.officeHolder[i]));
                        //marker.showInfoWindow();
//                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                            @Override
//                            public View getInfoWindow(Marker marker) {
//                                return null;
//                            }
//
//                            @Override
//                            public View getInfoContents(Marker arg0) {
//                                // Getting view from the layout file info_window_layout
//                                View v = getLayoutInflater().inflate(R.layout.windowlayout, null);
//
//                                // Getting the position from the marker
//                                LatLng latLng = arg0.getPosition();
//
//                                // Getting reference to the TextView to set latitude
//                                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
//
//                                // Getting reference to the TextView to set longitude
//                                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
//
//                                // Setting the latitude
//                                tvLat.setText("Latitude:" + myCordinateLocation.officeHolder[i]);
//
//                                // Setting the longitude
//                                tvLng.setText("Longitude:"+ latLng.longitude);
//                                return v;
//                            }
//                        });
//                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                            @Override
//                            public void onMapClick(LatLng arg0) {
//                                // Clears any existing markers from the GoogleMap
//
//
//                                // Creating an instance of MarkerOptions to set position
//                                MarkerOptions markerOptions = new MarkerOptions();
//
//                                // Setting position on the MarkerOptions
//                                markerOptions.position(arg0);
//
//                                // Animating to the currently touched position
//                                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
//
//                                // Adding marker on the GoogleMap
//                                Marker marker = mMap.addMarker(markerOptions);
//
//                                // Showing InfoWindow on the GoogleMap
//                                marker.showInfoWindow();
//                            }
//                        });

                    }

                }
                //showing offices
                else if (mySearchList.equals("Offices")){
                //loop through coordinates for offices
                    lat=myCordinateLocation.officeLat;
//                    longt=myCordinateLocation.officeLat;
//                    myTitle=myCordinateLocation.officeNames;
                    Toast.makeText(getApplicationContext(),"Showing: "+mySearchList,Toast.LENGTH_LONG).show();
                    for(int i=0;i<lat.length;i++){
//                        mMap.addMarker(new MarkerOptions().position(new LatLng(-0.616510, 30.656522)).title("Office One")
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.office)));
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.officeLat[i], myCordinateLocation.officeLong[i]))
                                .title(myCordinateLocation.officeNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.office_mapicon)));
                    }
                }
                //showing canteens
                else if (mySearchList.equals("MUST Canteens")) {
                    //loop through cordinates for offices
                    lat = myCordinateLocation.cateenLat;
                    Toast.makeText(getApplicationContext(), "Showing: " + mySearchList, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < lat.length; i++) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.cateenLat[i], myCordinateLocation.cateenLong[i]))
                                .title(myCordinateLocation.canteenNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.canteen_mapicon)));
                    }
                }
                //showing hostels
                    else if(mySearchList.equals("Hostels"))
                    {
                        lat = myCordinateLocation.hostelLat;
                        Toast.makeText(getApplicationContext(), "Showing: " + mySearchList, Toast.LENGTH_LONG).show();
                        for (int i = 0; i < lat.length; i++) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(myCordinateLocation.hostelLat[i], myCordinateLocation.hostelLong[i]))
                                    .title(myCordinateLocation.hostelNames[i].toString())
                                    .snippet(myCordinateLocation.hostelResidents[i])
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel2_mapicon)));
                        }
                    }
                //showing churches
                else if(mySearchList.equals("Churches"))
                {
                    lat = myCordinateLocation.churchLat;
                    Toast.makeText(getApplicationContext(), "Showing: " + mySearchList, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < lat.length; i++) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.churchLat[i], myCordinateLocation.churchLong[i]))
                                .title(myCordinateLocation.churchNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.church2)));
                    }
                }
//showing others on the map
                else if(mySearchList.equals("Others"))
                {
                    lat = myCordinateLocation.otherLat;
                    Toast.makeText(getApplicationContext(), "Showing: " + mySearchList, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < lat.length; i++) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.otherLat[i], myCordinateLocation.otherLong[i]))
                                .title(myCordinateLocation.otherNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.others_mapicon)));
                    }
                }
//lecture rooms
                else if(mySearchList.equals("Lectures"))
                {
                    lat = myCordinateLocation.lecLat;
                    Toast.makeText(getApplicationContext(), "Showing: " + mySearchList, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < lat.length; i++) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.lecLat[i], myCordinateLocation.lecLong[i]))
                                .title(myCordinateLocation.lecNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.lecture_rm_mapicon)));
                    }
                }

                //toilets
                else if(mySearchList.equals("Toilets"))
                {
                    lat = myCordinateLocation.toiletLat;
                    Toast.makeText(getApplicationContext(), "Showing: " + mySearchList, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < lat.length; i++) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.toiletLat[i], myCordinateLocation.toiletLong[i]))
                                .title(myCordinateLocation.toiletNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.toilets_mapicon)));
                    }
                }

                else if(mySearchList.equals("Rubbish"))
                {
                    lat = myCordinateLocation.dustLat;
                    Toast.makeText(getApplicationContext(), "Showing: " + mySearchList, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < lat.length; i++) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myCordinateLocation.dustLat[i], myCordinateLocation.dustLong[i]))
                                .title(myCordinateLocation.dustNames[i].toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.trash_mapicon)));
                    }
                }




                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().isZoomControlsEnabled();

               // CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(gps.getLatitude(), gps.getLongitude())).zoom(100).build();
            }
            else
            {
                //alert.showAlertDialog(this,"GPS Setting","Please Enable Your GPS from Setting Such That We Locate Where You Are",false);
                gps.showSettingsAlert();
                return;
            }
        }
    }
}
