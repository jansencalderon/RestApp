package jru.restaurantapp.ui.map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.ActivityMapsBinding;
import jru.restaurantapp.databinding.DialogMapBinding;
import jru.restaurantapp.databinding.DialogNearestBinding;
import jru.restaurantapp.model.data.NearestRestaurant;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.restaurant.RestaurantActivity;
import jru.restaurantapp.utils.BitmapUtils;

public class MapActivity extends MvpActivity<MapView, MapPresenter> implements MapView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerRestIcon = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);
        markerUserIcon = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_user, null);

        presenter.onStart();


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
                if (myMarker != null) {
                    myMarker.remove();
                }
                myMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.createDrawableFromView(MapActivity.this, markerUserIcon))));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 15));
                presenter.getNearest(place.getLatLng().latitude, place.getLatLng().longitude);

                binding.buttonShowNearest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        binding.buttonShowNearest.setVisibility(View.GONE);

    }

    @NonNull
    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        //create markers
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


    }


    @Override
    public void onShowNearest(){
        //hide green button
        binding.buttonShowNearest.setVisibility(View.GONE);

        DialogNearestBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_nearest,
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
                binding.buttonShowNearest.setVisibility(View.VISIBLE);
            }
        });

        //adapter
        dialogBinding.nearestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MapListAdapter(this);
        dialogBinding.nearestRecyclerView.setAdapter(adapter);


        setNearestRestaurants(realm.where(NearestRestaurant.class).findAll().sort("distance", Sort.ASCENDING));
        dialog.show();
    }


    @Override
    public void setNearestRestaurants(List<NearestRestaurant> restaurantList) {
        adapter.setList(restaurantList);
    }

    @Override
    public void OnItemClicked(NearestRestaurant nearestRestaurant){
        Intent intent = new Intent(MapActivity.this, RestaurantActivity.class);
        intent.putExtra("id",nearestRestaurant.getRestId());
        startActivity(intent);
    }

    @Override
    public void startLoading(String s) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MapActivity.this);
            progressDialog.setCancelable(false);
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
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Restaurant restaurant = realm.where(Restaurant.class).equalTo("restId", Integer.parseInt(marker.getSnippet())).findFirst();
        DialogMapBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_map,
                null,
                false);
        final Dialog dialog = new Dialog(MapActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.setRestaurant(restaurant);
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
                intent.putExtra("id",restaurant.getRestId());
                startActivity(intent);
            }
        });
        dialog.show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}