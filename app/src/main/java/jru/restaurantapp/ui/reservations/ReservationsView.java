package jru.restaurantapp.ui.reservations;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import jru.restaurantapp.model.data.Reservation;

/**
 * Created by Jansen on 7/24/2017.
 */

public interface ReservationsView extends MvpView{


    void startLoading();

    void stopLoading();

    void showAlert(String s);
}
