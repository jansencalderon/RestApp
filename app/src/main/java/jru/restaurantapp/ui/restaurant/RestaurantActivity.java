package jru.restaurantapp.ui.restaurant;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import io.realm.Realm;
import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.ActivityRestaurantBinding;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.map.MapActivity;
import jru.restaurantapp.ui.restaurant.form.RestaurantFormActivity;
import jru.restaurantapp.ui.restaurant.form.RestaurantFormView;

public class RestaurantActivity extends MvpActivity<RestaurantView,RestaurantPresenter> implements RestaurantView {

    ActivityRestaurantBinding binding;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant);
        binding.setView(getMvpView());



        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent i = getIntent();
        Restaurant restaurant = realm.where(Restaurant.class).equalTo("restId", i.getIntExtra("id",0)).findFirst();
        binding.setRestaurant(restaurant);


        Glide.with(this)
                .load(Constants.URL_IMAGE + restaurant.getRestImage().concat(".jpg"))
                .centerCrop()
                .into(binding.eventImage);

    }

    @Override
    public void OnReserve(Restaurant restaurant){
        Intent intent = new Intent(RestaurantActivity.this, RestaurantFormActivity.class);
        intent.putExtra("id",restaurant.getRestId());
        startActivity(intent);
    }

    @NonNull
    @Override
    public RestaurantPresenter createPresenter() {
        return new RestaurantPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
