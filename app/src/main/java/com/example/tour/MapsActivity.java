package com.example.tour;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static Location location;
    private LocationManager locationManager;

    //stores "yes" or "no" based on whether its spot has a negative infront of it
    private static ArrayList negEndLat = new ArrayList();
    private static ArrayList negEndLong = new ArrayList();
    private static ArrayList negStartLat = new ArrayList();
    private static ArrayList negStartLong = new ArrayList();

    public Criteria criteria;
    public String bestProvider;

    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    // Construct a GeoDataClient.
    private GeoDataClient mGeoDataClient;

    // Current Location Marker stuff
    private Marker positionMarker;
    private Circle accuracyCircle;
    private BitmapDescriptor markerDescriptor;
    private boolean drawAccuracy = true;


    // Construct a PlaceDetectionClient.
    private PlaceDetectionClient mPlaceDetectionClient;

    // Stores the colour of the lines
    private static ArrayList colour = new ArrayList();

    //
    private static ArrayList splitEndLocationCoordinatesLat = new ArrayList();
    private static ArrayList splitEndLocationCoordinatesLong = new ArrayList();
    private static ArrayList splitStartingLocationCoordinatesLat = new ArrayList();
    private static ArrayList splitStartingLocationCoordinatesLong = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //locationManager.requestLocationUpdates(, 1000, 2);
        //https://www.youtube.com/watch?v=CCZPUeY94MU

        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        getLocation();
        System.out.println("GET DIRECTIONS STARTING");
        getDirections();


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

        // Locations
        LatLng mackHouse = new LatLng(43.655685, -79.378438);
        LatLng tourCN = new LatLng(43.6425662, -79.3870568);
        LatLng nathanPhillipsSquare = new LatLng(43.6527069, -79.3834137);

        // Locations Array
        ArrayList<LatLng> locations = new ArrayList();
        locations.add(mackHouse);
        locations.add(tourCN);
        locations.add(nathanPhillipsSquare);

        // Locations Names
        ArrayList<String> locationNames = new ArrayList<>();
        locationNames.add("Mackenzie House");
        locationNames.add("Le Tour CN");
        locationNames.add("Nathan Phillips Square");

        for (int i=0; i<locations.toArray().length; i++) {
            mMap.addMarker(new MarkerOptions().position(locations.get(i)).title(locationNames.get(i)));
        }

        // Center Camera
        double middle_lat = (mackHouse.latitude - tourCN.latitude) / 2 + tourCN.latitude;
        double middle_long = (tourCN.longitude - mackHouse.longitude) / 2 + mackHouse.longitude;
        LatLng middle = new LatLng(middle_lat, middle_long);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(middle,15.0f));



        // TODO THIS LOOP AGAIN. THE LENGTH IS EVALUATING TO 0
        System.out.println("START");
        int i=0;
        System.out.println(MapsActivity.negStartLat.toArray().length);
        while (i<MapsActivity.negStartLat.toArray().length) {
            String endLatStr = MapsActivity.splitEndLocationCoordinatesLat.get(i).toString();
            double endLat = Double.parseDouble(endLatStr);

            String endLongStr = MapsActivity.splitEndLocationCoordinatesLong.get(i).toString();
            double endLong = Double.parseDouble(endLongStr);

            String startLatStr = MapsActivity.splitStartingLocationCoordinatesLat.get(i).toString();
            double startLat = Double.parseDouble(startLatStr);

            String startLongStr = MapsActivity.splitStartingLocationCoordinatesLong.get(i).toString();
            double startLong = Double.parseDouble(startLongStr);

            System.out.println("LINES");
            System.out.println(endLat);
            System.out.println(endLong);
            System.out.println(startLat);
            System.out.println(startLong);



            LatLng startingLocation = new LatLng(startLat, -startLong);
            LatLng endLocation = new LatLng(endLat, -endLong);


            Polyline line = mMap.addPolyline(new PolylineOptions().add(startingLocation, endLocation).width(8).color((Integer) MapsActivity.colour.get(i)));

            i++;
        }
        System.out.println("END");


    }

    //Gets your current gps location
    @SuppressLint("MissingPermission")
    public Location getLocation() {
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        System.out.print("YAAAAAAY");
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println("NO");
                if (location != null) {
                    MapsActivity.location = location;
                    System.out.println("LOCATIONNNNNNNNNNNNNNNNN");
                    System.out.println(MapsActivity.location);

                    double latitude = MapsActivity.location.getLatitude();
                    double longitude = MapsActivity.location.getLongitude();
                    mMap.addCircle(new CircleOptions().center(new LatLng(latitude, longitude)).radius(50.0f).fillColor(Color.BLUE).strokeColor(Color.BLUE).strokeWidth(50));
                    //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("LEMME DIE"));

                }
            }
        });
        //} else {
        //    System.out.println("GDI");
        //}
        return null;
    }


    //Gets the directions from one location to another
    public void getDirections() {
        ArrayList<String> requestLinks = new ArrayList<>();
        requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.655685,-79.378438&destination=43.6425662,-79.3870568&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");
        requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.6425662,-79.3870568&destination=43.6527069,-79.3834137&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");




        for (int i = 0; i < requestLinks.toArray().length; i++) {
            RequestQueue queue = Volley.newRequestQueue(this);
            final int finalI = i;
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestLinks.get(i), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String finalResponse = response.toString();
                            System.out.println("INSIDE GET DIRECTIONS");
                            System.out.println(finalResponse);

                            JSONArray array;
                            JSONObject object;
                            //allDirections stores all of the Data related to directions
                            ArrayList allDirections = new ArrayList();
                            ArrayList directionsEndLocation = new ArrayList();
                            ArrayList directionsStartLocations = new ArrayList();


                            try {
                                array = response.getJSONArray("routes");
                                object = array.getJSONObject(0);
                                array = object.getJSONArray("legs");
                                object = array.getJSONObject(0);

                                //Each item in this list has the info I need
                                array = object.getJSONArray("steps");
                                System.out.println("JSON ARRAY");
                                System.out.println(array);


                                for (int k = 0; k < array.length(); k++) {
                                    JSONObject currentJSONObject = array.getJSONObject(k);
                                    allDirections.add(currentJSONObject);
                                    allDirections.add("\n");

                                    //Remove unnecessary characters
                                    String endLocation = currentJSONObject.getString("end_location").replace("{\"lat\":", "");
                                    endLocation = endLocation.replace("\"lng\":", "");
                                    endLocation = endLocation.replace("}", "");
                                    directionsEndLocation.add(endLocation);

                                    //Split the coordinates at the comma
                                    String[] splitEndCoordinates = endLocation.split(",", 2);

                                    //Make the string ending latitude and longitude values doubles and remove negative signs
                                    completeLatitude(splitEndCoordinates[0], "end");
                                    completeLongitude(splitEndCoordinates[1], "end");



                                    ArrayList colours = new ArrayList();
                                    colours.add(-65536);
                                    colours.add(-16776961);
                                    MapsActivity.colour.add(colours.get(finalI));




                                    //Remove unnecessary characters
                                    String startLocation = currentJSONObject.getString("start_location").replace("{\"lat\":", "");
                                    startLocation = startLocation.replace("\"lng\":", "");
                                    startLocation = startLocation.replace("}", "");
                                    directionsStartLocations.add(startLocation);

                                    //Split the coordinates at the comma
                                    String[] splitStartingCoordinates = startLocation.split(",", 2);

                                    //Make the string starting latitude and longitude values doubles and remove negative signs
                                    completeLatitude(splitStartingCoordinates[0], "Start");
                                    completeLongitude(splitStartingCoordinates[1], "Start");
                                }


                                System.out.println("END LOCATIONS");
                                System.out.println(directionsEndLocation);
                                System.out.println("START LOCATIONS");
                                System.out.println(directionsStartLocations);
                                System.out.println(directionsEndLocation.get(1));

                                // TODO USE THESE VALUES. THEY SHOULD BE DOUBLES OF THE COORDINATES AND YES/NO INDICATING WHETHER IT IS NEGATIVE OR NOT
                                System.out.println("SPLIT END COORDS LONG");
                                System.out.println(MapsActivity.splitEndLocationCoordinatesLong);
                                System.out.println(MapsActivity.negEndLong);

                                System.out.println("SPLIT END COORDS LAT");
                                System.out.println(MapsActivity.splitEndLocationCoordinatesLat);
                                System.out.println(MapsActivity.negEndLat);


                                System.out.println("SPLIT START COORDS LONG");
                                System.out.println(MapsActivity.splitStartingLocationCoordinatesLong);
                                System.out.println(MapsActivity.negStartLong);

                                System.out.println("SPLIT START COORDS Lat");
                                System.out.println(MapsActivity.splitStartingLocationCoordinatesLat);
                                System.out.println(MapsActivity.negStartLat);
                                System.out.println(MapsActivity.negStartLat.toArray().length);
                                System.out.println(MapsActivity.splitStartingLocationCoordinatesLat.get(0));

                                System.out.println("COLOURS");
                                System.out.println(MapsActivity.colour);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, null);

            // Add JsonObjectRequest to the RequestQueue
            queue.add(jsonRequest);


        }
    }

    public void completeLatitude(String lat, String origin) {
        if (lat.charAt(0) == '-') {
            if (origin == "end") {
                MapsActivity.negEndLat.add("yes");
                String splitEndLatCoordinateFinal = lat.replace("-", "");
                double doubleEndLat = Double.parseDouble(splitEndLatCoordinateFinal);
                MapsActivity.splitEndLocationCoordinatesLat.add(doubleEndLat);
            } else {
                MapsActivity.negStartLat.add("yes");
                String splitStartingLatCoordinateFinal = lat.replace("-", "");
                double doubleStartingLat = Double.parseDouble(splitStartingLatCoordinateFinal);
                MapsActivity.splitStartingLocationCoordinatesLat.add(doubleStartingLat);
            }
        } else {
            if (origin == "end") {
                MapsActivity.negEndLat.add("no");
                double doubleEndLat = Double.parseDouble(lat);
                MapsActivity.splitEndLocationCoordinatesLat.add(doubleEndLat);
            } else {
                MapsActivity.negStartLat.add("no");
                double doubleStartingLat = Double.parseDouble(lat);
                MapsActivity.splitStartingLocationCoordinatesLat.add(doubleStartingLat);
            }
        }
    }

    public void completeLongitude(String longitude, String origin) {
        if (longitude.charAt(0) == '-') {
            if (origin == "end") {
                MapsActivity.negEndLong.add("yes");
                String splitEndLongCoordinateFinal = longitude.replace("-", "");
                double doubleEndLong = Double.parseDouble(splitEndLongCoordinateFinal);
                MapsActivity.splitEndLocationCoordinatesLong.add(doubleEndLong);
            } else {
                MapsActivity.negStartLong.add("yes");
                String splitStartingLongCoordinateFinal = longitude.replace("-", "");
                double doubleStartingLong = Double.parseDouble(splitStartingLongCoordinateFinal);
                MapsActivity.splitStartingLocationCoordinatesLong.add(doubleStartingLong);
            }
        } else {
            if (origin == "end") {
                MapsActivity.negEndLong.add("no");
                double doubleEndLong = Double.parseDouble(longitude);
                MapsActivity.splitEndLocationCoordinatesLong.add(doubleEndLong);
            } else {
                MapsActivity.negStartLong.add("no");
                double doubleStartingLong = Double.parseDouble(longitude);
                MapsActivity.splitStartingLocationCoordinatesLong.add(doubleStartingLong);
            }
        }
    }



}
