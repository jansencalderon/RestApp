package jru.restaurantapp.ui.map;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Jansen on 6/29/2017.
 */

public interface MapView extends MvpView {
    void startLoading(String s);

    void stopLoading();

    void showAlert(String s);

    void updateMap();
}
