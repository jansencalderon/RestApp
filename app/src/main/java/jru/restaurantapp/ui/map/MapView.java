package jru.restaurantapp.ui.map;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import jru.restaurantapp.model.data.NearestRestaurant;

/**
 * Created by Jansen on 6/29/2017.
 */

public interface MapView extends MvpView {
    void onShowNearest();

    void setNearestRestaurants(List<NearestRestaurant> restaurantList);

    void OnItemClicked(NearestRestaurant nearestRestaurant);

    void startLoading(String s);

    void stopLoading();

    void showAlert(String s);

    void updateMap();
}
