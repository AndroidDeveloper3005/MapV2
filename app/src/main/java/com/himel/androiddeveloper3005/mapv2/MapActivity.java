package com.himel.androiddeveloper3005.mapv2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "MapActivity";
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermission();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Map is ready");
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeveiceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

        }
    }

    private void initMap(){
        Log.d(TAG,"Map is initaizing");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(MapActivity.this);


    }
    private void getDeveiceLocation(){
        Log.d(TAG,"getting the Device Current  Location.");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted){
                 Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                 location.addOnCompleteListener(new OnCompleteListener<Location>() {
                     @Override
                     public void onComplete(@NonNull Task<Location> task) {

                         if (task.isSuccessful()){
                             Log.d(TAG,"Location found");
                             Location cuttentLocation = task.getResult();

                             moveCamera(new LatLng(cuttentLocation.getLatitude()
                                             ,cuttentLocation.getLongitude())
                                     ,DEFAULT_ZOOM);


                         }
                         else {
                             Log.d(TAG,"Current Location is null");
                             Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();

                         }

                     }
                 });



            }

        }catch (SecurityException e){
            Log.d(TAG,"Exception :"+e.getMessage());
            e.printStackTrace();
        }


    }

    private void moveCamera(LatLng latLng,float zoom) {
        Log.d(TAG,"Moviming the camera to :"+latLng.latitude +", lng : "+ latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    private void getLocationPermission(){
        Log.d(TAG,"Geting Location Permission");
        String []permission = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                //set boolean
                mLocationPermissionGranted = true;
                initMap();

            }
            else {
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);

            }

        }
        else {
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0){
                    for (int i =0 ;i<grantResults.length;i++){
                        if (grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted =false;
                            Log.d(TAG,"permission failed");
                            return;

                        }

                    }
                    Log.d(TAG,"permission granted");
                    mLocationPermissionGranted =true;

                    //initialize our map
                    initMap();

                }
            }
        }
    }


}
