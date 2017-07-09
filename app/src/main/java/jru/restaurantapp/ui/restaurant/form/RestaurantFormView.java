package jru.restaurantapp.ui.restaurant.form;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Jansen on 7/5/2017.
 */

public interface RestaurantFormView extends MvpView{

    void onPM();

    void onAM();

    void pickTime();
}
