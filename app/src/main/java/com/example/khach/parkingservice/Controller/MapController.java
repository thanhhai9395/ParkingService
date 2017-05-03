package com.example.khach.parkingservice.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MapController extends ActionBarActivity
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks ,
        LocationListener {



    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Button btn_Search;
    private Button btn_Location;
    private Button btn_Test;
    private GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermissionGranted;
    //    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private EditText editText_search;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private LatLng mDefaultLocation = new LatLng(10.772278, 106.705806);
    //var to store Last Location
    private Location mLastLocation;
    private double mLastLatitude;
    private double mLastLongitude;

    //search
    private Intent intent;
    //Marker
    private Marker markerCurrentLocation;
    private Marker markerSearchLocation;
    //LatLng
    private LatLng originCoordinate;
    private LatLng destinationCoordinate;

    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_map);
        addControls();
        addEvents();

//        getDeviceLocation();
        if (mLastLocation!=null)
            originCoordinate=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        else
            Toast.makeText(getApplicationContext(), "dm", Toast.LENGTH_SHORT).show();
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

//            Toast.makeText(getApplicationContext(), "onSaveInstance", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        LatLng bitexco = new LatLng(10.772278, 106.705806);
//        mMap.addMarker(new MarkerOptions().position(bitexco).title("Bitexco, Bến Nghé, TpHCM"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bitexco, 16));
//        Toast.makeText(getApplicationContext(), "onMapready", Toast.LENGTH_LONG).show();

    }

    private void addControls() {
        btn_Search  = (Button) findViewById(R.id.btn_Search);
        btn_Location = (Button) findViewById(R.id.btn_location);
        btn_Test=(Button)findViewById(R.id.btn_Test);

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
                startActivityForResult(intent,PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        btn_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDeviceLocation();
                updateLocationUI();
            }
        });

        btn_Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove Last marker
                //markerSearchLocation.remove();
              /*  if(originCoordinate!=null&&destinationCoordinate!=null)
                Toast.makeText(getApplicationContext(), originCoordinate.latitude+""+originCoordinate.longitude+"/n"+destinationCoordinate.latitude+""+destinationCoordinate.longitude,Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Don't have destination Location", Toast.LENGTH_SHORT).show();*/
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng( mLastLocation.getLatitude(),mLastLocation.getLongitude()),15));
            }
        });

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
            originCoordinate=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//            Toast.makeText(getApplicationContext(), "GetdeviceLocation14", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 15));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

//            Toast.makeText(getApplicationContext(), "GetdeviceLocation15", Toast.LENGTH_LONG).show();
        }

    }


    private void updateLocationUI()
    {
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

            if(markerCurrentLocation!=null)
                markerCurrentLocation.remove();
//            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker1);
//            Bitmap b = bitmapdraw.getBitmap();
//            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);
            //set marker at current location
            markerCurrentLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLatitude, mLastLongitude))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Your Location")
            );
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastLocation = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());

                if(markerSearchLocation!=null)
                    markerSearchLocation.remove();
                markerSearchLocation= mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13));
                //get Destination
                destinationCoordinate=place.getLatLng();

                Toast.makeText(getApplicationContext(),place.getName(),Toast.LENGTH_LONG).show();
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

//        Toast.makeText(getApplicationContext(), "connettionFail", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
//        Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
//        Toast.makeText(getApplicationContext(), "Suspended", Toast.LENGTH_LONG).show();
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
        //updateLocationUI();
//        Toast.makeText(getApplicationContext(), "OnRequestPermissionsResult", Toast.LENGTH_LONG).show();
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
}
