package jru.restaurantapp.ui.main.category;

import com.hannesdorfmann.mosby.mvp.MvpView;

import jru.restaurantapp.model.data.Restaurant;

/**
 * Created by Jansen on 8/20/2017.
 */

public interface CategoryView extends MvpView {
    void OnItemClicked (Restaurant restaurant);
}
