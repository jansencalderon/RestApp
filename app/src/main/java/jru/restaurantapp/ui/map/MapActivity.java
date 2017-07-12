package jru.restaurantapp.ui.map;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.ActivityMapsBinding;
import jru.restaurantapp.databinding.DialogMarkerClickBinding;
import jru.restaurantapp.databinding.DialogShowNearestBinding;
import jru.restaurantapp.model.data.NearestRestaurant;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.restaurant.RestaurantActivity;
import jru.restaurantapp.utils.BitmapUtils;
import jru.restaurantapp.utils.FusedLocation;

public class MapActivity extends MvpActivity<MapView, MapPresenter> implements MapView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    private ProgressDialog progressDialog;
    private Realm realm;
    private LatLngBounds bounds;
    private View markerRestIcon, markerUserIcon;
    private String TAG = MapActivity.class.getSimpleName();
    private PlaceAutocompleteFragment autocompleteFragment;
    private Marker myMarker = null;
    private ActivityMapsBinding binding;
    private MapListAdapter adapter;
    private FusedLocation fusedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initializeMap();


        fusedLocation = new FusedLocation(this, new FusedLocation.Callback() {
            @Override
            public void onLocationResult(Location location) {
                Log.e(TAG, "Location Triggered\n" + location.getLongitude() + "," + location.getLatitude());
                setMyMarker(new LatLng(location.getLatitude(), location.getLatitude()));
            }
        });
        ;
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fusedLocation.isGPSEnabled()) {
                    fusedLocation.showSettingsAlert();
                } else {
                    fusedLocation.getCurrentLocation(1);
                    Log.e(TAG, "Getting Location");
                }
            }
        });

        binding.buttonShowNearest.setVisibility(View.GONE);


    }

    private void initializeMap() {
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerRestIcon = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);
        markerUserIcon = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_user, null);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("PH")
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setHint("Where are you?");
        autocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(14.503863, 120.859556), new LatLng(14.767616, 121.088896)));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());//get place details here
                //my marker
                setMyMarker(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    private void setMyMarker(LatLng latLng) {
        if (myMarker != null) {
            myMarker.remove();
        }
        myMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.createDrawableFromView(MapActivity.this, markerUserIcon))));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 15));

        binding.buttonShowNearest.setVisibility(View.VISIBLE);

        presenter.getNearest(myMarker.getPosition().longitude, myMarker.getPosition().latitude);
    }

    @NonNull
    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(14.599512, 120.984222), 15));
        //create markers
    /*    List<Restaurant> restaurants = realm.where(Restaurant.class).findAll();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        MarkerOptions markerOptions = new MarkerOptions();
        if (!restaurants.isEmpty()) {
            for (Restaurant restaurant : restaurants) {
                markerOptions.position(new LatLng(restaurant.getRestLat(), restaurant.getRestLng()));
                markerOptions.title(restaurant.getRestName());
                markerOptions.snippet(restaurant.getRestId() + "");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.createDrawableFromView(this, markerRestIcon)));
                builder.include(markerOptions.getPosition());
                mMap.addMarker(markerOptions);

            }
            bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            mMap.animateCamera(cu);
        }*/


        presenter.onStart();

    }


    @Override
    public void showNearest() {
        final Realm realm = Realm.getDefaultInstance();
        //hide green button
        binding.buttonShowNearest.setVisibility(View.GONE);

        DialogShowNearestBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_show_nearest,
                null,
                false);
        final Dialog dialog = new Dialog(MapActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                realm.close();
                binding.buttonShowNearest.setVisibility(View.VISIBLE);
            }
        });

        //adapter
        dialogBinding.nearestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MapListAdapter(this);
        dialogBinding.nearestRecyclerView.setAdapter(adapter);


        final RealmResults<NearestRestaurant> nearestRestaurants = realm.where(NearestRestaurant.class).findAll().sort("distance", Sort.ASCENDING);
        setNearestRestaurants(nearestRestaurants);


        //spinner
        final List<NearestRestaurant> tempItems = realm.copyFromRealm(nearestRestaurants.where().distinct("restCategory"));
        final List<String> items = new ArrayList<>();
        if (!items.isEmpty()) {
            items.clear();
        }
        items.add("Show All");
        for (NearestRestaurant tempItem : tempItems) {
            items.add(tempItem.getRestCategory());
        }
        ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dialogBinding.dialogSpinner.setAdapter(stringAdapter);
        dialogBinding.dialogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(items.get(position).equals("Show All")){
                    setNearestRestaurants(tempItems);
                }else{
                    setNearestRestaurants(nearestRestaurants.where().equalTo("restCategory",items.get(position)).findAll());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.show();
    }


    @Override
    public void setNearestRestaurants(List<NearestRestaurant> restaurantList) {
        adapter.setList(restaurantList);
    }

    @Override
    public void OnItemClicked(NearestRestaurant nearestRestaurant) {
        Intent intent = new Intent(MapActivity.this, RestaurantActivity.class);
        intent.putExtra("id", nearestRestaurant.getRestId());
        startActivity(intent);
    }

    @Override
    public void startLoading(String s) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MapActivity.this);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setMessage(s);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateMap() {
        final Realm realm = Realm.getDefaultInstance();
        mMap.clear();
        List<Restaurant> restaurants = realm.where(Restaurant.class).findAll();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        MarkerOptions markerOptions = new MarkerOptions();
        if (!restaurants.isEmpty()) {
            for (Restaurant restaurant : restaurants) {
                markerOptions.position(new LatLng(restaurant.getRestLat(), restaurant.getRestLng()));
                markerOptions.title(restaurant.getRestName());
                markerOptions.snippet(restaurant.getRestId() + "");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.createDrawableFromView(this, markerRestIcon)));
                builder.include(markerOptions.getPosition());
                mMap.addMarker(markerOptions);

            }
            bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            mMap.animateCamera(cu);

        }

        realm.close();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Restaurant restaurant = realm.where(Restaurant.class).equalTo("restId", Integer.parseInt(marker.getSnippet())).findFirst();
        DialogMarkerClickBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_marker_click,
                null,
                false);
        final Dialog dialog = new Dialog(MapActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBinding.setRestaurant(restaurant);
        dialog.setCancelable(false);
        Glide.with(this).load(Constants.URL_IMAGE + restaurant.getRestImage().concat(".jpg")).into(dialogBinding.restImage);
        dialogBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, RestaurantActivity.class);
                intent.putExtra("id", restaurant.getRestId());
                startActivity(intent);
            }
        });
        dialog.show();

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {


            }
        }

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    //permishit
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permission")
                        .setMessage("Location Request")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        showAlert("Thanks");
                    }

                } else {
                    showAlert("Can't use this feature without location permission");
                    MapActivity.this.finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                mMap.clear();
                binding.buttonShowNearest.setVisibility(View.GONE);
                presenter.getRestaurants();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
