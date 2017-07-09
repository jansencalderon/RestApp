package jru.restaurantapp.ui.main;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import jru.restaurantapp.app.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class MainPresenter extends MvpNullObjectBasePresenter<MainView> {
    private Realm realm;
    private String TAG = MainPresenter.class.getSimpleName();
    private RealmResults<Restaurant> restaurants;

    void onStart(){
        realm = Realm.getDefaultInstance();
        getRestaurants();
        restaurants = realm.where(Restaurant.class).findAll();
        restaurants.addChangeListener(new RealmChangeListener<RealmResults<Restaurant>>() {
            @Override
            public void onChange(RealmResults<Restaurant> restaurants) {
                filterList();
            }
        });


    }

    void getRestaurants() {
        getView().startLoading();
        App.getInstance().getApiInterface().getRestaurants().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, final Response<List<Restaurant>> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Restaurant.class);
                            realm.copyToRealmOrUpdate(response.body());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            realm.close();
                            Log.e(TAG, "onError: Unable to save Restaurants", error);
                        }
                    });
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });

    }

    private void filterList() {
        if (restaurants.isLoaded() && restaurants.isValid()) {
            List<Restaurant> restaurantList;
            restaurantList = realm.copyFromRealm(restaurants);
            getView().setRestaurants(restaurantList);
          //  getView().showAlert(restaurantList.get(0).getRestName());
        }

    }




    public void onStop(){

        if(restaurants.isValid()){
            restaurants.removeAllChangeListeners();
        }
        realm.close();
    }


}
