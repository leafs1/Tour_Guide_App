package com.example.tour;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static Location location;
    private LocationManager locationManager;
    private double landmarkLat;
    private double landmarkLon;
    private ArrayList<Double> landmarkCoords;
    private boolean negLat;
    private boolean negLong;
    public double lemmedie;
    public static ArrayList<Double> workingCoords;
    public ArrayList<Double> testingArray = new ArrayList<>();


    //stores "yes" or "no" based on whether its spot has a negative infront of it
    private static ArrayList<String> negEndLat = new ArrayList<>();
    private static ArrayList<String> negEndLong = new ArrayList<>();
    private static ArrayList<String> negStartLat = new ArrayList<>();
    private static ArrayList<String> negStartLong = new ArrayList<String>();

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
    private static ArrayList<Integer> colour = new ArrayList<Integer>();

    //
    private static ArrayList<Double> splitEndLocationCoordinatesLat = new ArrayList<>();
    private static ArrayList<Double> splitEndLocationCoordinatesLong = new ArrayList<Double>();
    private static ArrayList<Double> splitStartingLocationCoordinatesLat = new ArrayList<Double>();
    private static ArrayList<Double> splitStartingLocationCoordinatesLong = new ArrayList<>();

    private static ArrayList<String> locations = new ArrayList<>();

    private static ArrayList<Double> fullCoords = new ArrayList();

    private static ArrayList<Double> endLat = new ArrayList();
    private static ArrayList<Double> endLong = new ArrayList<>();
    private static ArrayList<Double> startLat = new ArrayList<>();
    private static ArrayList<Double> startLong = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        landmarkCoords = new ArrayList<Double>();
        workingCoords = new ArrayList<Double>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //locationManager.requestLocationUpdates(, 1000, 2);
        //https://www.youtube.com/watch?v=CCZPUeY94MU

        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        System.out.println("GET DIRECTIONS STARTING");


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
    // todo GET A LIST OF ALL COORDINATE LOCATIONS BY CALLING THE NEW FUNCITON AND SAVING IT TO THE GLOBAL VAR. AUTOMATE
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("PLEASEEEE");
        int counter = 0;




        //getLandmarkLocations("Eaton Center");
        //getLandmarkLocations("Hockey Hall of Fame");


        System.out.println(MapsActivity.workingCoords);
        System.out.println("TESTING ARRRAYYYYYYYY");
        System.out.println(testingArray);
        paths();


    }

    public void paths() {
        ArrayList<String> requestLinks = new ArrayList<>();
        // CN TOWER = 43.6425662,-79.3870568
        // Hockey Hall of Fame = 43.6472722,-79.3776902
        // Hospotal for sick kids = 43.6570957,-79.3877344
        // Hilton Toronto = 43.6499459,-79.385479
        // Eaton Center = 43.6544382,-79.3806994

        // Ripley's Aquarium = 43.6424036,-79.3859716
        // Toronto City Hall = 43.6534399,-79.3840901
        // Hilton Toronto = 43.6499459,-79.385479
        // Young-Dundas Square = 43.6560811, -79.3801714
        // Hockey Hall of Fame = 43.6472722,-79.3776902
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.6424036,-79.3859716)).title("Riplay's Aquarium"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.6499459,-79.385479)).title("Hilton Toronto"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.6534399,-79.3840901)).title("Toronto City Hall"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.6560811, -79.3801714)).title("Young-Dundas Square"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.6472722,-79.3776902)).title("Hockey Hall of Fame"));


        LatLng mackHouse = new LatLng(43.655685, -79.378438);
        LatLng tourCN = new LatLng(43.6425662, -79.3870568);
        LatLng nathanPhillipsSquare = new LatLng(43.6527069, -79.3834137);

        double middle_lat = (mackHouse.latitude - tourCN.latitude) / 2 + tourCN.latitude;
        double middle_long = (tourCN.longitude - mackHouse.longitude) / 2 + mackHouse.longitude;
        LatLng middle = new LatLng(middle_lat, middle_long);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(middle,15.0f));

        requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.6424036,-79.3859716&destination=43.6499459,-79.385479&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");
        requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.6499459,-79.385479&destination=43.6534399,-79.3840901&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");
        requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.6534399,-79.3840901&destination=43.6560811, -79.3801714&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");
        requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.6560811, -79.3801714&destination=43.6472722,-79.3776902&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");



        for (int i = 0; i < requestLinks.toArray().length; i++) {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestLinks.get(i), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray array;
                            JSONObject object;

                            ArrayList allDirections = new ArrayList();
                            ArrayList<String> directionsEndLocation = new ArrayList<String>();
                            ArrayList<String> directionsStartLocations = new ArrayList<String>();


                            try {
                                array = response.getJSONArray("routes");
                                object = array.getJSONObject(0);
                                array = object.getJSONArray("legs");
                                object = array.getJSONObject(0);

                                //Each item in this list has the info I need
                                array = object.getJSONArray("steps");
                                System.out.println("JSON ARRAY");
                                System.out.println(array);
                                System.out.println(array.length());



                                int z;
                                for (z=0; z < array.length(); z++) {
                                    JSONObject currentJSONObject = array.getJSONObject(z);
                                    allDirections.add(currentJSONObject);
                                    allDirections.add("\n");
                                    System.out.println(allDirections);


                                    //Remove unnecessary characters
                                    String endLocation = currentJSONObject.getString("end_location").replace("{\"lat\":", "");
                                    endLocation = endLocation.replace("\"lng\":", "");
                                    endLocation = endLocation.replace("}", "");
                                    directionsEndLocation.add(endLocation);

                                    //Split the coordinates at the comma
                                    String[] splitEndCoordinates = endLocation.split(",", 2);
                                    System.out.println(splitEndCoordinates);
                                    System.out.println(splitEndCoordinates);


                                    //Make the string ending latitude and longitude values doubles and remove negative signs
                                    //completeLatitude(splitEndCoordinates[0], "end");
                                    //completeLongitude(splitEndCoordinates[1], "end");

                                    ArrayList<Integer> colours = new ArrayList<Integer>();
                                    //colours.add(-65536);
                                    //colours.add(-16776961);
                                    MapsActivity.colour.add(-16776961);


                                    //Remove unnecessary characters
                                    String startLocation = currentJSONObject.getString("start_location").replace("{\"lat\":", "");

                                    startLocation = startLocation.replace("\"lng\":", "");
                                    startLocation = startLocation.replace("}", "");
                                    directionsStartLocations.add(startLocation);


                                    //Split the coordinates at the comma
                                    String[] splitStartingCoordinates = startLocation.split(",", 2);


                                    MapsActivity.endLat.add(Double.parseDouble(splitEndCoordinates[0]));
                                    MapsActivity.endLong.add(Double.parseDouble(splitEndCoordinates[1]));
                                    MapsActivity.startLat.add(Double.parseDouble(splitStartingCoordinates[0]));
                                    MapsActivity.startLong.add(Double.parseDouble(splitStartingCoordinates[1]));
                                    System.out.println("WHYYYY RIOT");
                                    System.out.println(endLat.size());


                                }

                                for (int f = 0; f < endLat.size(); f++) {
                                    Polyline line = mMap.addPolyline(new PolylineOptions().add(new LatLng(MapsActivity.startLat.get(f), MapsActivity.startLong.get(f)), new LatLng(MapsActivity.endLat.get(f), MapsActivity.endLong.get(f))).width(8).color(MapsActivity.colour.get(0)));
                                    System.out.println("heeeelllleee");

                                }


                                //Make the string starting latitude and longitude values doubles and remove negative signs
                                //completeLatitude(splitStartingCoordinates[0], "Start");
                                //completeLongitude(splitStartingCoordinates[1], "Start");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }, null);

            // Add JsonObjectRequest to the RequestQueue
            queue.add(jsonRequest);


        }
    }
}

/*
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
                    */
// return the GPS location of a landmark
    /*
    public void getLandmarkLocations(String name) {
        System.out.println("INSIDE BUT NOT WORKING");
        ArrayList<String> requestLinks = new ArrayList<String>();

        String requestLink = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + name + "&inputtype=textquery&fields=photos,formatted_address,name,geometry&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU\n";
        String url = "http://my-json-feed";


        requestLinks.add(requestLink);


        for (int i = 0; i < requestLinks.toArray().length; i++) {
            RequestQueue queue = Volley.newRequestQueue(this);
            final int finalI = i;
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, requestLinks.get(i), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String finalResponse = response.toString();
                            System.out.println("SHOULD BE INFO AND STUFF");
                            System.out.println(finalResponse);

                            JSONArray array;
                            JSONObject object;
                            String location;

                            try {
                                array = response.getJSONArray("candidates");
                                object = array.getJSONObject(0);
                                object = object.getJSONObject("geometry");
                                location = object.getString("location");

                                location = location.replace("{\"lat\":", "");
                                location = location.replace("\"lng\":", "");
                                location = location.replace("}", "");

                                // Split at ","
                                String[] splitLongFromLat = location.split(",", 2);
                                String latitude = splitLongFromLat[0];
                                String longitude = splitLongFromLat[1];

                                // See if long and lat are neg or pos
                                negLat = false;
                                negLong = false;
                                if (latitude.charAt(0) == '-') {
                                    negLat = true;
                                }
                                if (longitude.charAt(0) == '-') {
                                    negLong = true;
                                }

                                landmarkLat = Double.parseDouble(latitude);
                                landmarkLon = Double.parseDouble(longitude);
                                landmarkLocationsReturner(landmarkLat, landmarkLon);

        /*
                                System.out.println("LONGITUDE AND LATITUDE TYPESSSSSSSSSSS");
                                System.out.println(landmarkLat);
                                System.out.println(landmarkLon);
                                System.out.println(negLat);
                                System.out.println(negEndLat);
                                System.out.println(landmarkCoords);



                                        // Split at "."
                                String[] splitLatitude = latitude.split(".", 2);
                                String[] splitLongitude = longitude.split(".", 2);
                                String firstLatitude = splitLatitude[0];
                                String secondLatitude = splitLatitude[1];
                                String firstLongitude = splitLongitude[0];
                                String secondLongitude = splitLongitude[1];


                                System.out.println("CANDIDATES");
                                System.out.println(array);
                                System.out.println(object);
                                System.out.println(location);
                                System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
                                System.out.println(location);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }, null);

            // Add JsonObjectRequest to the RequestQueue
            queue.add(jsonRequest);


        }
    }

    public void landmarkLocationsReturner(double latitude, double longitude) {
        System.out.println("INSIDE RETURNER METHOD");
        System.out.println(latitude);
        System.out.println(longitude);
        System.out.println("LOOK HEREE. THIS IS BEFORE NEW COORDS");
        System.out.println(landmarkCoords);

        landmarkCoords.add(latitude);
        landmarkCoords.add(longitude);
        workingCoords.add(latitude);
        workingCoords.add(longitude);

        System.out.println("LOOK HEREE. THIS IS AFTER NEW COORDS");
        System.out.println(landmarkCoords);
        landmarkLat = latitude;
        landmarkLon = longitude;
        lemmedie = latitude;
        System.out.println("lemme die");
        System.out.println(lemmedie);

        testingArray.add(latitude);
        testingArray.add(longitude);
        System.out.println("TESTING ARRAYYYYYYYYYYYYY");
        System.out.println(testingArray);

        System.out.println("PLEASE FUCKING WORK");
        System.out.println(doneFetch());

        ArrayList<Double> previousLandmarkCoords = landmarkCoords;
        if (landmarkCoords.size() == 2 * locations.size()) {
            System.out.println("DOING SHIT");
            getDirections();
        }


    }






    /*
                            System.out.println("YOLOOOOO");
                            System.out.println(MapsActivity.splitEndLocationCoordinatesLat.toArray().length);
                            for (int k = 0; k < MapsActivity.splitEndLocationCoordinatesLat.toArray().length; k++) {
                                System.out.println("k");
                                System.out.println(k);
                                System.out.println("condition");
                                System.out.println(MapsActivity.splitEndLocationCoordinatesLat.toArray().length);

                                String endLatStr = MapsActivity.splitEndLocationCoordinatesLat.get(k).toString();
                                double endLat = Double.parseDouble(endLatStr);

                                String endLongStr = MapsActivity.splitEndLocationCoordinatesLong.get(k).toString();
                                double endLong = Double.parseDouble(endLongStr);

                                String startLatStr = MapsActivity.splitStartingLocationCoordinatesLat.get(k).toString();
                                double startLat = Double.parseDouble(startLatStr);

                                String startLongStr = MapsActivity.splitStartingLocationCoordinatesLong.get(k).toString();
                                double startLong = Double.parseDouble(startLongStr);

                                System.out.println("LINES");
                                MapsActivity.fullCoords.add(startLat);
                                MapsActivity.fullCoords.add(startLong);
                                MapsActivity.fullCoords.add(endLat);
                                MapsActivity.fullCoords.add(endLong);
                                //System.out.println(MapsActivity.fullCoords);

                                //System.out.println(endLat);
                                //System.out.println(endLong);
                                //System.out.println(startLat);
                                //System.out.println(startLong);


                                LatLng startingLocation = new LatLng(startLat, -startLong);
                                LatLng endLocationn = new LatLng(endLat, -endLong);


                                Polyline line = mMap.addPolyline(new PolylineOptions().add(startingLocation, endLocationn).width(8).color(MapsActivity.colour.get(k)));

                                System.out.println("BEFORE WEIRD SHIT1");
                                //System.out.println(MapsActivity.fullCoords.size());


                            }

                            System.out.println("KILL ME NOW PLEASEE");
                            System.out.println(MapsActivity.endLat);
                            for (int o = 0; o < endLat.size(); o++) {
                                System.out.println("POPOPOPOPOPOPOPOPOPOPOP");
                                System.out.println(MapsActivity.startLat.get(o));
                                System.out.println(MapsActivity.startLong.get(o));
                                System.out.println(MapsActivity.endLat.get(o));
                                System.out.println(MapsActivity.endLong.get(o));

                                Polyline line = mMap.addPolyline(new PolylineOptions().add(new LatLng(MapsActivity.startLat.get(o), MapsActivity.startLong.get(o)), new LatLng(MapsActivity.endLat.get(o), MapsActivity.endLong.get(o))).width(8).color(MapsActivity.colour.get(0)));
                            }


                            System.out.println("BEFORE WEIRD SHIT");
                            //System.out.println(MapsActivity.fullCoords.size());

                            System.out.println("LIST OS STUFF");
*/





/*
    public ArrayList<Double> doneFetch() {
        System.out.println("FINAL");
        System.out.println(landmarkCoords);

        ArrayList<LatLng> locations = new ArrayList<LatLng>();

        System.out.println(landmarkCoords.size());
        System.out.println(landmarkCoords.get(0));
        System.out.println(landmarkCoords.get(1));
        for (int i=0; i < landmarkCoords.size()-1; i++) {
            LatLng newLocation = new LatLng(landmarkCoords.get(i), landmarkCoords.get(i+1));
            locations.add(newLocation);
        }
        System.out.println("LOCATIONS NEW");
        System.out.println(locations);



        LatLng mackHouse = new LatLng(43.655685, -79.378438);
        LatLng tourCN = new LatLng(43.6425662, -79.3870568);
        LatLng nathanPhillipsSquare = new LatLng(43.6527069, -79.3834137);

        // Locations Array
        //locations.add(mackHouse);
        //locations.add(tourCN);
        //locations.add(nathanPhillipsSquare);
        //locations.add(newLocation);


        // Locations Names
        ArrayList<String> locationNames = new ArrayList<>();
        locationNames.add("Eaton Center");
        locationNames.add("The Hospital for Sick Children");
        locationNames.add("Nathan Phillips Square");
        //locationNames.add("test");
        //System.out.println("LENGTH");
        //System.out.println(MapsActivity.locations.toArray().length);
        for (int j=0; j<locations.toArray().length; j++) {
            mMap.addMarker(new MarkerOptions().position(locations.get(j)).title(locationNames.get(0)));
        }

        // Center Camera
        double middle_lat = (mackHouse.latitude - tourCN.latitude) / 2 + tourCN.latitude;
        double middle_long = (tourCN.longitude - mackHouse.longitude) / 2 + mackHouse.longitude;
        LatLng middle = new LatLng(middle_lat, middle_long);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(middle,15.0f));
        //15f

        // TODO THIS LOOP AGAIN. THE LENGTH IS EVALUATING TO 0
        System.out.println("IMPORTANT LOCATIONS THING");
        System.out.println(MapsActivity.locations);
        System.out.println(landmarkCoords);

        *//*
        if (landmarkCoords.size() == 4) {
            System.out.println("ASDF");
            getDirections();
        }
        System.out.println("qwertyu");

        System.out.println("START");
        */
        /*
        int k=0;
        System.out.println(MapsActivity.splitEndLocationCoordinatesLat.toArray().length);
        while (k<MapsActivity.splitEndLocationCoordinatesLat.toArray().length) {
            String endLatStr = MapsActivity.splitEndLocationCoordinatesLat.get(k).toString();
            double endLat = Double.parseDouble(endLatStr);

            String endLongStr = MapsActivity.splitEndLocationCoordinatesLong.get(k).toString();
            double endLong = Double.parseDouble(endLongStr);

            String startLatStr = MapsActivity.splitStartingLocationCoordinatesLat.get(k).toString();
            double startLat = Double.parseDouble(startLatStr);

            String startLongStr = MapsActivity.splitStartingLocationCoordinatesLong.get(k).toString();
            double startLong = Double.parseDouble(startLongStr);

            System.out.println("LINES");
            System.out.println(endLat);
            System.out.println(endLong);
            System.out.println(startLat);
            System.out.println(startLong);

            LatLng startingLocation = new LatLng(startLat, -startLong);
            LatLng endLocation = new LatLng(endLat, -endLong);

            Polyline line = mMap.addPolyline(new PolylineOptions().add(startingLocation, endLocation).width(8).color(MapsActivity.colour.get(k)));


            k++;

        }
        *//*
        System.out.println("END");
        System.out.println(landmarkCoords);
        return landmarkCoords;

    }

    // TODO INPUT THE COORDINATES OF THE START AND NEXT LOCATION
    //Gets the directions from one location to another
    public void getDirections() {
        ArrayList<String> requestLinks = new ArrayList<>();
        System.out.println("LANDMARK COORDS");
        System.out.println(landmarkCoords);

        for (int i=0; i<landmarkCoords.size()/2; i++) {
            requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=" + landmarkCoords.get(0) + "," + landmarkCoords.get(1) + "&destination=" + landmarkCoords.get(2) + "," + landmarkCoords.get(3) + "&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");

        //System.out.println("DIRECTIONS REQUEST LINKS");
        //System.out.println(requestLinks);

        //requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.655685,-79.378438&destination=43.6425662,-79.3870568&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");
        //requestLinks.add("https://maps.googleapis.com/maps/api/directions/json?origin=43.6425662,-79.3870568&destination=43.6527069,-79.3834137&key=AIzaSyCQa21uzhxFbuQx0EGM4_-SW8J7flEWUgU");
*//*
        for (int u = 0; u < requestLinks.toArray().length; u++) {
            RequestQueue queue = Volley.newRequestQueue(this);
            final int finalI = u;
            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestLinks.get(u), null,
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
                            ArrayList<String> directionsEndLocation = new ArrayList<String>();
                            ArrayList<String> directionsStartLocations = new ArrayList<String>();


                            try {
                                array = response.getJSONArray("routes");
                                object = array.getJSONObject(0);
                                array = object.getJSONArray("legs");
                                object = array.getJSONObject(0);

                                //Each item in this list has the info I need
                                array = object.getJSONArray("steps");
                                System.out.println("JSON ARRAY");
                                System.out.println(array);
                                System.out.println(array.length());
*/
                                /*
                                int z;
                                for (z=0; z < array.length(); z++) {
                                    System.out.println("J UPDATER");
                                    System.out.println(z);
                                    JSONObject currentJSONObject = array.getJSONObject(z);
                                    allDirections.add(currentJSONObject);
                                    allDirections.add("\n");
                                    System.out.println("POIUY");
                                    System.out.println(allDirections);


                                    //Remove unnecessary characters
                                    String endLocation = currentJSONObject.getString("end_location").replace("{\"lat\":", "");
                                    endLocation = endLocation.replace("\"lng\":", "");
                                    endLocation = endLocation.replace("}", "");
                                    directionsEndLocation.add(endLocation);

                                    //Split the coordinates at the comma
                                    String[] splitEndCoordinates = endLocation.split(",", 2);
                                    System.out.println(splitEndCoordinates);
                                    System.out.println(splitEndCoordinates);


                                    //Make the string ending latitude and longitude values doubles and remove negative signs
                                    //completeLatitude(splitEndCoordinates[0], "end");
                                    //completeLongitude(splitEndCoordinates[1], "end");

                                    ArrayList<Integer> colours = new ArrayList<Integer>();
                                    //colours.add(-65536);
                                    //colours.add(-16776961);
                                    MapsActivity.colour.add(-16776961);


                                    //Remove unnecessary characters
                                    String startLocation = currentJSONObject.getString("start_location").replace("{\"lat\":", "");

                                    startLocation = startLocation.replace("\"lng\":", "");
                                    startLocation = startLocation.replace("}", "");
                                    directionsStartLocations.add(startLocation);


                                    //Split the coordinates at the comma
                                    String[] splitStartingCoordinates = startLocation.split(",", 2);



                                    MapsActivity.endLat.add(Double.parseDouble(splitEndCoordinates[0]));
                                    MapsActivity.endLong.add(Double.parseDouble(splitEndCoordinates[1]));
                                    MapsActivity.startLat.add(Double.parseDouble(splitStartingCoordinates[0]));
                                    MapsActivity.startLong.add(Double.parseDouble(splitStartingCoordinates[1]));
                                    System.out.println("WHYYYY RIOT");
                                    System.out.println(endLat.size());

                                    if (z == 5) {
                                        break;
                                    }
                                }

                                    for (int f = 0; f < endLat.size(); f++) {
                                        Polyline line = mMap.addPolyline(new PolylineOptions().add(new LatLng(MapsActivity.startLat.get(f), MapsActivity.startLong.get(f)), new LatLng(MapsActivity.endLat.get(f), MapsActivity.endLong.get(f))).width(8).color(MapsActivity.colour.get(0)));
                                        System.out.println("heeeelllleee");
                                        }
                    */



//Make the string starting latitude and longitude values doubles and remove negative signs
//completeLatitude(splitStartingCoordinates[0], "Start");
//completeLongitude(splitStartingCoordinates[1], "Start");
/*


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            System.out.println("YOLOOOOO");
                            System.out.println(MapsActivity.splitEndLocationCoordinatesLat.toArray().length);
                            for (int k = 0; k < MapsActivity.splitEndLocationCoordinatesLat.toArray().length; k++) {
                                System.out.println("k");
                                System.out.println(k);
                                System.out.println("condition");
                                System.out.println(MapsActivity.splitEndLocationCoordinatesLat.toArray().length);

                                String endLatStr = MapsActivity.splitEndLocationCoordinatesLat.get(k).toString();
                                double endLat = Double.parseDouble(endLatStr);

                                String endLongStr = MapsActivity.splitEndLocationCoordinatesLong.get(k).toString();
                                double endLong = Double.parseDouble(endLongStr);

                                String startLatStr = MapsActivity.splitStartingLocationCoordinatesLat.get(k).toString();
                                double startLat = Double.parseDouble(startLatStr);

                                String startLongStr = MapsActivity.splitStartingLocationCoordinatesLong.get(k).toString();
                                double startLong = Double.parseDouble(startLongStr);

                                System.out.println("LINES");
                                MapsActivity.fullCoords.add(startLat);
                                MapsActivity.fullCoords.add(startLong);
                                MapsActivity.fullCoords.add(endLat);
                                MapsActivity.fullCoords.add(endLong);
                                //System.out.println(MapsActivity.fullCoords);

                                //System.out.println(endLat);
                                //System.out.println(endLong);
                                //System.out.println(startLat);
                                //System.out.println(startLong);


                                LatLng startingLocation = new LatLng(startLat, -startLong);
                                LatLng endLocationn = new LatLng(endLat, -endLong);


                                Polyline line = mMap.addPolyline(new PolylineOptions().add(startingLocation, endLocationn).width(8).color(MapsActivity.colour.get(k)));

                                System.out.println("BEFORE WEIRD SHIT1");
                                //System.out.println(MapsActivity.fullCoords.size());


                            }

                            System.out.println("KILL ME NOW PLEASEE");
                            System.out.println(MapsActivity.endLat);

                            for (int o = 0; o < endLat.size(); o++) {
                                System.out.println("POPOPOPOPOPOPOPOPOPOPOP");
                                System.out.println(MapsActivity.startLat.get(o));
                                System.out.println(MapsActivity.startLong.get(o));
                                System.out.println(MapsActivity.endLat.get(o));
                                System.out.println(MapsActivity.endLong.get(o));

                                Polyline line = mMap.addPolyline(new PolylineOptions().add(new LatLng(MapsActivity.startLat.get(o), MapsActivity.startLong.get(o)), new LatLng(MapsActivity.endLat.get(o), MapsActivity.endLong.get(o))).width(8).color(MapsActivity.colour.get(0)));
                            }


                            System.out.println("BEFORE WEIRD SHIT");
                            //System.out.println(MapsActivity.fullCoords.size());

                            System.out.println("LIST OS STUFF");


                        }

                    }, null);

            // Add JsonObjectRequest to the RequestQueue
            queue.add(jsonRequest);
            System.out.println("LENGTH THING");
            System.out.println(MapsActivity.negStartLat.toArray().length);


        }
        }
    }

    public void completeLatitude(String lat, String origin) {
        if (lat.charAt(0) == '-') {
            if (origin == "end") {
                MapsActivity.negEndLat.add("yes");
                MapsActivity.endLat.add(Double.parseDouble(lat));
                String splitEndLatCoordinateFinal = lat.replace("-", "");
                double doubleEndLat = Double.parseDouble(splitEndLatCoordinateFinal);
                System.out.println("FUCK ME");
                System.out.println(doubleEndLat);
                MapsActivity.splitEndLocationCoordinatesLat.add(doubleEndLat);
                System.out.println(MapsActivity.splitEndLocationCoordinatesLat);

            } else {
                MapsActivity.negStartLat.add("yes");
                MapsActivity.startLat.add(Double.parseDouble(lat));
                String splitStartingLatCoordinateFinal = lat.replace("-", "");
                double doubleStartingLat = Double.parseDouble(splitStartingLatCoordinateFinal);
                System.out.println("FUCK ME");
                System.out.println(doubleStartingLat);
                MapsActivity.splitStartingLocationCoordinatesLat.add(doubleStartingLat);
                System.out.println(MapsActivity.splitStartingLocationCoordinatesLat);

            }
        } else {
            if (origin == "end") {
                MapsActivity.negEndLat.add("no");
                MapsActivity.endLat.add(Double.parseDouble(lat));
                double doubleEndLat = Double.parseDouble(lat);
                System.out.println("FUCK ME");
                System.out.println(doubleEndLat);
                MapsActivity.splitEndLocationCoordinatesLat.add(doubleEndLat);
                System.out.println(MapsActivity.splitEndLocationCoordinatesLat);

            } else {
                MapsActivity.negStartLat.add("no");
                MapsActivity.startLat.add(Double.parseDouble(lat));
                double doubleStartingLat = Double.parseDouble(lat);
                System.out.println("FUCK ME");
                System.out.println(doubleStartingLat);
                MapsActivity.splitStartingLocationCoordinatesLat.add(doubleStartingLat);
                System.out.println(MapsActivity.splitStartingLocationCoordinatesLat);

            }
        }
    }

    public void completeLongitude(String longitude, String origin) {
        if (longitude.charAt(0) == '-') {
            if (origin == "end") {
                MapsActivity.negEndLong.add("yes");
                MapsActivity.endLong.add(Double.parseDouble(longitude));
                String splitEndLongCoordinateFinal = longitude.replace("-", "");
                double doubleEndLong = Double.parseDouble(splitEndLongCoordinateFinal);
                System.out.println("FUCK ME");
                System.out.println(doubleEndLong);
                MapsActivity.splitEndLocationCoordinatesLong.add(doubleEndLong);
                System.out.println(MapsActivity.splitEndLocationCoordinatesLong);

            } else {
                MapsActivity.negStartLong.add("yes");
                MapsActivity.startLong.add(Double.parseDouble(longitude));
                String splitStartingLongCoordinateFinal = longitude.replace("-", "");
                double doubleStartingLong = Double.parseDouble(splitStartingLongCoordinateFinal);
                System.out.println("FUCK ME");
                System.out.println(doubleStartingLong);
                MapsActivity.splitStartingLocationCoordinatesLong.add(doubleStartingLong);
                System.out.println(MapsActivity.splitStartingLocationCoordinatesLong);

            }
        } else {
            if (origin == "end") {
                MapsActivity.negEndLong.add("no");
                MapsActivity.endLong.add(Double.parseDouble(longitude));
                double doubleEndLong = Double.parseDouble(longitude);
                System.out.println("FUCK ME");
                System.out.println(doubleEndLong);
                MapsActivity.splitEndLocationCoordinatesLong.add(doubleEndLong);
                System.out.println(MapsActivity.splitEndLocationCoordinatesLong);

            } else {
                MapsActivity.negStartLong.add("no");
                MapsActivity.startLong.add(Double.parseDouble(longitude));
                double doubleStartingLong = Double.parseDouble(longitude);
                System.out.println("FUCK ME");
                System.out.println(doubleStartingLong);
                MapsActivity.splitStartingLocationCoordinatesLong.add(doubleStartingLong);
                System.out.println(MapsActivity.splitStartingLocationCoordinatesLong);
            }
        }
    }



}
*/