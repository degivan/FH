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
import com.annimon.stream.Stream;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.DISH_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.SEARCH_CHOICE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.SEARCH_TEXT_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.clearEditText;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.showToast;

public class ShowMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public enum SearchChoice {SEARCH_FOOD, SEARCH_PLACE}

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private SearchChoice searchChoice;
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
        name = getIntent().getStringExtra(SEARCH_TEXT_EXTRA_NAME);
        searchChoice = (SearchChoice) getIntent().getSerializableExtra(SEARCH_CHOICE_EXTRA_NAME);
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
                showToast(getApplicationContext(), getResources().getString(R.string.m_need_access_location));
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
                .title(getResources().getString(R.string.map_user_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Client.request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (searchChoice == SearchChoice.SEARCH_PLACE) {
                    showPlacesOnMap(dataSnapshot);
                } else {
                    showDishesOnMap(dataSnapshot);
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
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, ((EditText) findViewById(R.id.search_line)).getText().toString());
        intent.putExtra(SEARCH_CHOICE_EXTRA_NAME, searchChoice);
        startActivity(intent);
    }

    private void showPlacesOnMap(DataSnapshot dataSnapshot) {
        List<Place> places = Client.getPlacesForNameNearby(dataSnapshot, ll, 0.005, name);
        List<Marker> markerList = Stream.of(places)
                .map(p -> mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(p.getLatitude(), p.getLongitude()))
                        .title(p.getName())))
                .collect(Collectors.toList());
        mMap.setOnInfoWindowClickListener(marker -> {
            if (markerList.indexOf(marker) != -1) {
                Intent intent = new Intent(ShowMap.this, PlacePage.class);
                intent.putExtra(PLACE_EXTRA_NAME, places.get(markerList.indexOf(marker)));
                startActivity(intent);
            }
        });

        zoomToFit(markerList);

    }

    private void showDishesOnMap(DataSnapshot dataSnapshot) {
        List<Dish> dishes = Client.getDishesForNameNearby(dataSnapshot, ll, 0.005, name);
        List<Marker> markerList = Stream.of(dishes)
                .map(d -> mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(d.getLatitude(), d.getLongitude()))
                        .title(d.getPlaceName())))
                .collect(Collectors.toList());
        mMap.setOnInfoWindowClickListener(marker -> {
            if (markerList.indexOf(marker) != -1) {
                Intent intent = new Intent(ShowMap.this, FoodPage.class);
                intent.putExtra(DISH_EXTRA_NAME, dishes.get(markerList.indexOf(marker)));
                startActivity(intent);
            }
        });

        zoomToFit(markerList);
    }

    private void zoomToFit(List<Marker> markerList) {
        if (markerList.size() == 0) return;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(ll);
        for (Marker marker : markerList)
            builder.include(marker.getPosition());
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    public void onSearchLineClick(View view) {
        clearEditText(searchLine);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowMap.this, MainMenu.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
        startActivity(intent);
        finish();
    }
}
