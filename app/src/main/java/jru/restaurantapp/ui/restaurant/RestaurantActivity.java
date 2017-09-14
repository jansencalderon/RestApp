package jru.restaurantapp.ui.restaurant;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.ActivityRestaurantBinding;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.restaurant.form.RestaurantFormActivity;

public class RestaurantActivity extends MvpActivity<RestaurantView, RestaurantPresenter> implements RestaurantView {

    ActivityRestaurantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant);
        binding.setView(getMvpView());


        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent i = getIntent();

        presenter.onStart();
        presenter.getData(i.getIntExtra("id", 0));

    }

    @Override
    public void OnReserve(Restaurant restaurant) {
        Intent intent = new Intent(RestaurantActivity.this, RestaurantFormActivity.class);
        intent.putExtra(Constants.ID, restaurant.getRestId());
        startActivity(intent);
    }

    @Override
    public void setRestaurant(Restaurant restaurant) {
        binding.setRestaurant(restaurant);
        Glide.with(this)
                .load(Constants.URL_IMAGE + restaurant.getRestImage().concat(".jpg"))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(binding.eventImage);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        ProductListAdapter adapter = new ProductListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        if (!restaurant.getProducts().isEmpty()) {
            adapter.setList(restaurant.getProducts());
        }
    }

    @NonNull
    @Override
    public RestaurantPresenter createPresenter() {
        return new RestaurantPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
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
