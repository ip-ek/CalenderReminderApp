package com.ipk.reminderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap=googleMap;
        if(permissionBool){
            getDeviceLocation();
        }
        //burda tekrar izin almana gerek yok
        myMap.setMyLocationEnabled(true);   //currentLoc - mavi nokta olarak işaretlenir ve benim lokasyonuma dönme butonu
        myMap.getUiSettings().setMyLocationButtonEnabled(false);    //search kısmı olcak o yüzden buton kalksın
        init();
    }

    private EditText searchText;
    private ImageView currLoc;

    private  boolean permissionBool=false;
    private GoogleMap myMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchText=findViewById(R.id.address_search_input);
        currLoc=findViewById(R.id.address_current_btn);

        getLocPermission();

    }

    private void init(){
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH ||
                        actionId ==EditorInfo.IME_ACTION_DONE ||
                        event.getAction()==KeyEvent.ACTION_DOWN ||
                        event.getAction()==KeyEvent.KEYCODE_ENTER){
                    //search işlemi için çalışır.
                    geoLocate();
                }
                return false;
            }
        });

        currLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        //hideSoftKeyboard();
    }

    private void geoLocate(){
        String searchString= searchText.getText().toString();
        Geocoder geocoder= new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(list.size()>0){
            Address address=list.get(0);    //arama sonucu gelen adres burada

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()), 15f, address.getAddressLine(0 ));
        }
    }

    private void getDeviceLocation(){
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        try {
            if(permissionBool){
                final Task location =mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLoc=(Location) task.getResult();
                            moveCamera(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()), 15f, "Konumum");
                        }else{
                            }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void moveCamera(LatLng latLng, float zoom , String title){
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("Konumum")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            myMap.addMarker(options);   //ilgili yere marker konuldu
        }
        //hideSoftKeyboard();
    }

    private void initMap(){
        //bunun yarısı diğer kodda da tanımlanabilir ama orası yeterince kalabalık
        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    //permission codes
    private void getLocPermission(){
        String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                permissionBool=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions, 1234);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionBool=false;

        switch (requestCode){
            case 1234:{
                if(grantResults.length>0 ){
                    for(int i=0; i<grantResults.length; i++){
                        if(grantResults[i] !=PackageManager.PERMISSION_GRANTED){
                            permissionBool=false;
                            return;
                        }
                    }
                    permissionBool=true;
                    //map tanımlanabilir

                }
            }
        }
    }

    /*private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }*/

}
