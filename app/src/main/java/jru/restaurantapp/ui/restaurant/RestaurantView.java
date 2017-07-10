package jru.restaurantapp.ui.restaurant;

import com.hannesdorfmann.mosby.mvp.MvpView;

import jru.restaurantapp.model.data.Restaurant;

/**
 * Created by Jansen on 7/4/2017.
 */

public interface RestaurantView extends MvpView{

    void OnReserve(Restaurant restaurant);
}
