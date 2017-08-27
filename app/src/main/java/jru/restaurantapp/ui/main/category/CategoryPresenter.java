package jru.restaurantapp.ui.main.category;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.model.data.Restaurant;

/**
 * Created by Jansen on 8/20/2017.
 */

public class CategoryPresenter extends MvpNullObjectBasePresenter<CategoryView> {


    private Realm realm;

    void onStart(){
        realm = Realm.getDefaultInstance();


    }

    public List<Restaurant> getRestaurants(String category){
        return realm.where(Restaurant.class)
                .equalTo(Constants.REALM.REST_CATEGORY, category.trim())
                .findAllSorted(Constants.REALM.REST_ID);
    }

    void onStop(){
        realm.close();
    }
}
