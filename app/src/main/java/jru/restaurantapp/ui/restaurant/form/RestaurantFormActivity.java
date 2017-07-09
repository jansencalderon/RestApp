package jru.restaurantapp.ui.restaurant.form;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import io.realm.Realm;
import jru.restaurantapp.R;
import jru.restaurantapp.databinding.ActivityRestaurantFormBinding;
import jru.restaurantapp.model.data.Restaurant;


public class RestaurantFormActivity extends MvpActivity<RestaurantFormView, RestaurantFormPresenter> implements RestaurantFormView {

    ActivityRestaurantFormBinding binding;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_form);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent i = getIntent();
        Restaurant restaurant = realm.where(Restaurant.class).equalTo("restId", i.getIntExtra("id", 0)).findFirst();
        binding.setRestaurant(restaurant);

    }

    @NonNull
    @Override
    public RestaurantFormPresenter createPresenter() {
        return new RestaurantFormPresenter();
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

    @Override
    public void onPM() {
        binding.tvPM.setAlpha(1);
        binding.tvAM.setAlpha(0.5f);
    }

    @Override
    public void onAM() {
        binding.tvAM.setAlpha(1);
        binding.tvPM.setAlpha(0.5f);

    }

    @Override
    public void pickTime() {

        final String[] flowers = new String[]{
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantFormActivity.this);
        builder.setItems(flowers, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.time.setText(flowers[which]);
            }
        });


        builder.setCancelable(false);
        Dialog myDialog = builder.create();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.show();

    }
}
