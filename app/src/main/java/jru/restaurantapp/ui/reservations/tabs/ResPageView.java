package jru.restaurantapp.ui.reservations.tabs;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import jru.restaurantapp.model.data.Reservation;
import jru.restaurantapp.model.data.Restaurant;

/**
 * Created by Sen on 2/28/2017.
 */

public interface ResPageView extends MvpView {
    void setEvents(List<Reservation> reservations);

    void internet(Boolean status);

    void checkResult(int count);

    void OnItemClicked(Reservation reservation);

    void showAlert(String s);
}
