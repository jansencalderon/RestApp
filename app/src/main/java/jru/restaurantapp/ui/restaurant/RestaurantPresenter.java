package jru.restaurantapp.ui.restaurant;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.model.data.Restaurant;

/**
 * Created by Jansen on 7/4/2017.
 */

public class RestaurantPresenter extends MvpNullObjectBasePresenter<RestaurantView> {

    private Realm realm;

    void onStart(){
        realm = Realm.getDefaultInstance();

    }

    void getData(int id){

        Restaurant restaurant = realm.where(Restaurant.class).equalTo("restId",id).findFirst();
        getView().setRestaurant(restaurant);

    }

    void onStop(){
        realm.close();
    }
}
