package jru.restaurantapp.ui.restaurant.form;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Jansen on 7/5/2017.
 */

public interface RestaurantFormView extends MvpView{

    void sendReservation();

    void startLoading();

    void stopLoading();

    void onPM();

    void onAM();

    void onToday();

    void onTomorrow();

    void pickTime();

    void showAlert(String s);

    void onReservationSuccess();
}
