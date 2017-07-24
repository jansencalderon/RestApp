package jru.restaurantapp.ui.restaurant.form;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import jru.restaurantapp.R;
import jru.restaurantapp.app.App;
import jru.restaurantapp.databinding.ActivityRestaurantFormBinding;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.register.RegisterActivity;
import jru.restaurantapp.utils.DateTimeUtils;


public class RestaurantFormActivity extends MvpActivity<RestaurantFormView, RestaurantFormPresenter> implements RestaurantFormView {

    ActivityRestaurantFormBinding binding;
    private String pickedDate;
    private String pickedTime;
    private boolean isAM = true;
    private Restaurant restaurant;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_form);
        binding.setView(getMvpView());
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent i = getIntent();
        restaurant = presenter.getRestaurant(i.getIntExtra("id", 0));
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


    @Override
    public void sendReservation(){
        presenter.sendReservation(restaurant.getRestId(), App.getUser().getUserId(),
                pickedDate+" "+pickedTime, binding.etHeadCount.getText().toString());
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(RestaurantFormActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Sending reservation...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onPM() {
        binding.tvPM.setAlpha(1);
        binding.tvAM.setAlpha(0.5f);
        isAM = false;

    }

    @Override
    public void onAM() {
        binding.tvAM.setAlpha(1);
        binding.tvPM.setAlpha(0.5f);
        isAM = true;
        /*String tempTime = binding.time.getText().toString();
        if (!tempTime.equals("")) {
            if (validateTime(binding.time.getText().toString())) {
                binding.tvAM.setAlpha(1);
                binding.tvPM.setAlpha(0.5f);
                isAM = true;
            }
        }*/
    }


    @Override
    public void onToday() {
        Calendar c = Calendar.getInstance();
        pickedDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
        binding.tvToday.setAlpha(1);
        binding.tvTomorrow.setAlpha(0.5f);

    }

    @Override
    public void onTomorrow() {
        binding.tvTomorrow.setAlpha(1);
        binding.tvToday.setAlpha(0.5f);
        Calendar c = Calendar.getInstance();
        pickedDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + (c.get(Calendar.DATE) + 1);

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

        final AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantFormActivity.this);
        builder.setItems(flowers, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String time;
                if (validateTime(flowers[which])) {
                    binding.time.setText(flowers[which]);
                }

            }
        });


        builder.setCancelable(false);
        Dialog myDialog = builder.create();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.show();

    }

    public boolean validateTime(String timePicked) {
        String time;
        if (isAM) {
            time = timePicked;
        } else {
            time = (timePicked + 12) + "";
        }
        pickedTime = time + ":00:00";

        Date picked = DateTimeUtils.String_To_Time(pickedTime);

        if (picked.before(DateTimeUtils.String_To_Time(restaurant.getRestHoursOpen())) || picked.after(DateTimeUtils.String_To_Time(restaurant.getRestHoursClose()))) {
            showAlert("Choose time between " + restaurant.getRestHours());
        } else {
            pickedTime = timePicked + ":00:00";
            return true;
        }
        return false;
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReservationSuccess() {
        showAlert("SUCCESS");
    }


}
