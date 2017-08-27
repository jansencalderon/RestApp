package jru.restaurantapp.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import jru.restaurantapp.model.data.RestCategory;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.model.data.User;


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public interface MainView extends MvpView {

    void stopLoading();

    void startLoading();

    void displayUserData(User user);

    void showAlert(String s);


    void refreshList();

    void setRestaurants(List<Restaurant> restaurantList);

    void internet(Boolean status);

    void OnItemClicked(Restaurant restaurant);

    void OnCategoryClicked (RestCategory restCategory);
}
