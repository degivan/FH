package spbau.mit.divan.foodhunter.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.annimon.stream.Collectors;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

public class ShowMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean userWish;
    private String name;
    private EditText searchLine;
    private LatLng ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
        name = getIntent().getStringExtra("name");
        userWish = getIntent().getBooleanExtra("foodnotplace", false);
        searchLine = ((EditText) findViewById(R.id.search_line));
        searchLine.setText(name);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        ll = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(ll, (float) 15.5)));
        mMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f)
                .position(ll)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Client.request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!userWish) {
                    ShowPlacesOnMap(dataSnapshot);
                } else {
                    ShowDishesOnMap(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void onFindClick(View view) {
        Intent intent = new Intent(ShowMap.this, ShowMap.class);
        intent.putExtra("name", ((EditText) findViewById(R.id.search_line)).getText().toString());
        intent.putExtra("foodnotplace", userWish);
        startActivity(intent);
    }

    public void onSearchLineClick(View view) {
        if (searchLine.getText().toString().equals(getResources().getString(R.string.search_line))) {
            searchLine.setText("");
        }
    }

    private void ShowPlacesOnMap(DataSnapshot dataSnapshot) {
        List<Place> places = Client.getPlacesForNameNearby(dataSnapshot, ll, 0.005, name)
                .collect(Collectors.toList());
        List<Marker> markerList = new ArrayList<Marker>();
        for (Place place : places) {
            markerList.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.getLatitude(), place.getLongitude()))
                    .title(place.getName())));
            mMap.setOnInfoWindowClickListener(marker -> {
                if (markerList.indexOf(marker) != -1) {
                    Intent intent = new Intent(ShowMap.this, PlacePage.class);
                    intent.putExtra("place", places.get(markerList.indexOf(marker)));
                    startActivity(intent);
                }
            });
        }
    }

    private void ShowDishesOnMap(DataSnapshot dataSnapshot) {
        List<Dish> dishes = Client.getDishesForNameNearby(dataSnapshot, ll, 0.005, name)
                .collect(Collectors.toList());
        List<Marker> markerList = new ArrayList<Marker>();
        for (Dish dish : dishes) {
            markerList.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(dish.getLatitude(), dish.getLongitude()))
                    .title(dish.getPlaceName())));
        }
        mMap.setOnInfoWindowClickListener(marker -> {
            if (markerList.indexOf(marker) != -1) {
                Intent intent = new Intent(ShowMap.this, FoodPage.class);
                intent.putExtra("dish", dishes.get(markerList.indexOf(marker)));
                startActivity(intent);
            }
        });
    }
}
