    package com.example.khach.parkingservice.Controller;

    import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khach.parkingservice.Entities.Account;
import com.example.khach.parkingservice.Entities.ParkingLot;
import com.example.khach.parkingservice.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.khach.parkingservice.R.id.btnOK;
import static com.example.khach.parkingservice.R.id.map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    final Context context = this;
    private GoogleMap mMap;
    private Button btn_Search;
    private Button btn_Location;
    private Button btn_Direction;
    private Polyline polyline;
    private List<ParkingLot> listParking = new ArrayList<ParkingLot>();
    private List<String> listKM = new ArrayList<String>();
    private GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermissionGranted;
    private  String place = null;
    //    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private EditText editText_search;
    String km;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LatLng mDefaultLocation = new LatLng(10.772278, 106.705806);
    //var to store Last Location
    private Location mLastLocation;
    private LatLng mParkingLocation;
    LatLng oldMarker;
    private double mLastLatitude;
    private double mLastLongitude;
    //search
    private Intent intent;
    //Marker
    private Marker markerCurrentLocation;
    private Marker markerSearchLocation;
    //LatLng
    private boolean check = false;
    private LatLng originCoordinate;
    private LatLng destinationCoordinate;
    private BottomNavigationView mButtomBar;
//Marker;
    private  ArrayList<Marker> listMarker = new ArrayList<Marker>();
    private DatabaseReference myData;
    Account a = new Account();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btn_Direction = (Button) findViewById(R.id.btn_Direction);
        btn_Direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = true;
                if (polyline != null)
                    polyline.remove();
                if (mParkingLocation == null) {
                    Toast.makeText(getApplicationContext(), "Please enter address where you want to come!", Toast.LENGTH_LONG).show();
                return;
                }
                else if (originCoordinate == null) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Location?")
                            .setMessage("Do you want to get your location?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getDeviceLocation();
                                    updateLocationUI();
                                    DrawPath(MainActivity.this.originCoordinate,MainActivity.this.mParkingLocation);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }else {
                    DrawPath(MainActivity.this.originCoordinate,MainActivity.this.mParkingLocation);
                }
            }
        });
        mButtomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        mButtomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bikeBottom) {
                   /* listParking.removeAll(listParking);
                    listKM.removeAll(listKM);
                    getListParking();*/

                } else if (item.getItemId() == R.id.carBottom) {
                }
                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_LOCATION, mLastLocation);
            outState.putParcelable(KEY_CAMERA_POSITION, mCameraPosition);
            super.onSaveInstanceState(outState);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getApplicationContext();
                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);
                return info;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
               try {
                    String text = marker.getSnippet();
                    String[] abc = text.split("\n");
                    String a = abc[3].substring(10, abc[3].length());
                    MainActivity.this.mParkingLocation = getLocationFromAddress(getApplicationContext(), a);
                }catch(Exception ex){}
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.detail_parking);
                dialog.setTitle("Detail Parking");
                TextView txtDetail = (TextView) dialog.findViewById(R.id.txtDetailParking);
                txtDetail.setText(marker.getSnippet());
                Button btnExit = (Button) dialog.findViewById(R.id.btnExitDetailParking);
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    private void addControls() {
        btn_Search = (Button) findViewById(R.id.btn_Search);
        btn_Location = (Button) findViewById(R.id.btn_location);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        ///Place Auto Complete (Google AIP)
        try {
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    private void addEvents() {
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                mMap.clear();
                //get Location Device
                getDeviceLocation();
                updateLocationUI();
                getListParking();

            }
        });
        btn_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDeviceLocation();
                updateLocationUI();
                getListParking();
            }
        });
    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }
    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        //Get Device Location in here
        if (mLocationPermissionGranted)
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 14));
            originCoordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            Toast.makeText(getApplicationContext(), "GetdeviceLocation14", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 15));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    private void updateLocationUI() {
        if (mMap == null)
            return;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            if (markerCurrentLocation != null)
                markerCurrentLocation.remove();
            markerCurrentLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLatitude, mLastLongitude))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Your Location")
            );
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastLocation = null;
        }
      /*  for(int i=0;i< listParking.size();i++){
            LatLng pointLocation;
            pointLocation = getLocationFromAddress(getApplicationContext(), listParking.get(i).getAddress());
            check = false;
            DrawPath(MainActivity.this.originCoordinate,pointLocation);
        }
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.chose_accountcode);
        String[] listChooseKM = new String[]{"1","2","3"};
        final  Spinner spinnerKM = (Spinner) dialog.findViewById(R.id.spnSoKm);
        ArrayAdapter<String> adapterRadius = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, listChooseKM);
        spinnerKM.setAdapter(adapterRadius);
        Button dialogButtonOK = (Button) dialog.findViewById(btnOK);
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkNullParking=  false;
                final float kmChose = Float.parseFloat(spinnerKM.getSelectedItem().toString());
                String textKm =spinnerKM.getSelectedItem().toString();
                for (int i = 0; i < listParking.size(); i++) {
                    String km =listKM.get(i).toString();
                    String resultKM =km.substring(0,km.length()-3);
                    if(Float.parseFloat(resultKM)<kmChose) {
                        try {
                            checkNullParking = true;
                            LatLng pointLocation;
                            pointLocation = getLocationFromAddress(getApplicationContext(), listParking.get(i).getAddress());
                            mMap.addMarker(new MarkerOptions().position(new LatLng(pointLocation.latitude, pointLocation.longitude))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .title("Thông tin").snippet("STT :" + (i + 1) +
                                            "\n" + "Tên Bãi : " + listParking.get(i).getPhoneNo() +
                                            "\n" + "Số điện thoại : " + listParking.get(i).getName() +
                                            "\n" + "Địa chỉ : " + listParking.get(i).getAddress() +
                                            "\n" + "Sức Chứa : " + listParking.get(i).size +
                                            "\nKhoảng cách đi bộ đến " + listKM.get(i).toString()));

                        } catch (Exception ex) {
                        }
                    }
                }
                if(checkNullParking ==false){
                    Toast.makeText(context, "Không tìm thấy bãi đỗ xe trong phạm vi "+textKm + "km", Toast.LENGTH_SHORT).show();
                    // dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        });
        Button btnCacel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();*/

    }
    private void DrawPath(LatLng latLngFrom,LatLng latLngTo ){
        String url = getDirectionsUrl(latLngFrom, latLngTo);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }
    private void getListParking(){
        listKM.removeAll(listKM);
        MainActivity.this.listParking.removeAll( MainActivity.this.listParking);
               /* DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();*/
        myData = FirebaseDatabase.getInstance().getReference();
        myData.child("ParkingLot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snap = dataSnapshot.getChildren();
                for (DataSnapshot child : snap) {
                    ParkingLot parkingLot = child.getValue(ParkingLot.class);
                    listParking.add(parkingLot);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                listParking = null;
            }
        });
    }
     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                this.place = place.getName().toString();
                if (markerSearchLocation != null)
                    markerSearchLocation.remove();
                markerSearchLocation = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13));
                //get Destination
                destinationCoordinate = place.getLatLng();
                LatLng pointLocation;
                for (int i = 0; i < listParking.size(); i++) {
                    try {
                        check = false;
                        pointLocation = getLocationFromAddress(getApplicationContext(), listParking.get(i).getAddress());
                        DrawPath(MainActivity.this.destinationCoordinate, pointLocation);
                    }catch (Exception e){

                    }
                }
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.chose_accountcode);
                String[] listChooseKM = new String[]{"1","2","3"};
               final  Spinner spinnerKM = (Spinner) dialog.findViewById(R.id.spnSoKm);
                ArrayAdapter<String> adapterRadius = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_checked, listChooseKM);
                spinnerKM.setAdapter(adapterRadius);
                Button dialogButtonOK = (Button) dialog.findViewById(btnOK);
                        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean checkNullParking=  false;
                        final float kmChose = Float.parseFloat(spinnerKM.getSelectedItem().toString());
                        String textKm =spinnerKM.getSelectedItem().toString();
                            for (int i = 0; i < listParking.size(); i++) {
                                String km =listKM.get(i).toString();
                                String resultKM =km.substring(0,km.length()-3);
                                if(Float.parseFloat(resultKM)<kmChose) {
                                    try {
                                        checkNullParking = true;
                                        LatLng pointLocation;
                                        pointLocation = getLocationFromAddress(getApplicationContext(), listParking.get(i).getAddress());
                                        mMap.addMarker(new MarkerOptions().position(new LatLng(pointLocation.latitude, pointLocation.longitude))
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                                .title("Thông tin").snippet("STT :" + (i + 1) +
                                                        "\n" + "Tên Bãi : " + listParking.get(i).getPhoneNo() +
                                                        "\n" + "Số điện thoại : " + listParking.get(i).getName() +
                                                        "\n" + "Địa chỉ : " + listParking.get(i).getAddress() +
                                                        "\n" + "Sức Chứa : " + listParking.get(i).size +
                                                        "\nKhoảng cách đi bộ đến " + listKM.get(i).toString()));

                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        if(checkNullParking ==false){
                            Toast.makeText(context, "Không tìm thấy bãi đỗ xe trong phạm vi "+textKm + "km", Toast.LENGTH_SHORT).show();
                           // dialog.dismiss();
                        }else{
                            dialog.dismiss();
                        }
                    }
                });
                Button btnCacel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCacel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLastLatitude = (mLastLocation.getLatitude());
            mLastLongitude = (mLastLocation.getLongitude());
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mLocationPermissionGranted = true;
        }
    }
    //On Location Changed
    @Override
    public void onLocationChanged(Location location) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_history) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.chose_accountcode);
            dialog.show();
        } else if (id == R.id.nav_like) {
            Intent aM = new Intent(this.getApplicationContext(), MapController.class);
            startActivity(aM);
        } else if (id == R.id.nav_quanlytaikhoan) {
            Intent aM = new Intent(this.getApplicationContext(), AccountManager.class);
            startActivity(aM);
        } else if (id == R.id.nav_Login) {
            Intent aM = new Intent(this.getApplicationContext(), login.class);
            startActivity(aM);
        } else if (id == R.id.nav_dkxe) {
            Intent aM = new Intent(this.getApplicationContext(), DangKyXe.class);
            startActivity(aM);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
//                    Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    public class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        public  String abcs = "";
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        MainActivity.this.listKM.add(distance);
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                lineOptions.color(Color.RED);
            }

         if(check ==true) {
             polyline = mMap.addPolyline(lineOptions);
             Toast.makeText(getApplicationContext(), "Distance:" + distance + ", Duration:" + duration, Toast.LENGTH_LONG).show();
         }
            abcs = distance;
            km = distance;
        }
        public  String A(){
            return this.abcs;
        }
    }
}
