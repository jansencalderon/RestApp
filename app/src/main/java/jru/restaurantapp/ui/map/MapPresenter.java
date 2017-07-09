package jru.restaurantapp.ui.map;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import jru.restaurantapp.app.App;
import jru.restaurantapp.model.data.NearestRestaurant;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.utils.MapUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Jansen on 6/29/2017.
 */

public class MapPresenter extends MvpNullObjectBasePresenter<MapView> {
    private Realm realm;

    public void onStart() {
        realm = Realm.getDefaultInstance();

        getRestaurants();
    }

    private void getRestaurants() {
        getView().startLoading("Getting data...");
        App.getInstance().getApiInterface().getRestaurants().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, final Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {
                    getView().stopLoading();
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
                            getView().updateMap();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            realm.close();
                            Log.e(TAG, "onError: Unable to save Restaurants", error);
                        }
                    });
                } else {
                    getView().stopLoading();
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


    public void onStop() {
        realm.close();
    }


    void getNearest(double latitude, double longitude) {
        getView().startLoading("calculating distance...");
        List<Restaurant> restaurants = realm.where(Restaurant.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(NearestRestaurant.class);
            }
        });

        for (Restaurant restaurant : restaurants) {
            Double distance = MapUtils.distance(latitude, longitude, restaurant.getRestLat(), restaurant.getRestLng());
            final NearestRestaurant nearest = new NearestRestaurant();
            nearest.setRestId(restaurant.getRestId());
            nearest.setRestName(restaurant.getRestName());
            nearest.setRestAdd(restaurant.getRestAdd());
            nearest.setRestContact(restaurant.getRestContact());
            nearest.setRestHours(restaurant.getRestHours());
            nearest.setRestLat(restaurant.getRestLat());
            nearest.setRestLng(restaurant.getRestLng());
            nearest.setRestImage(restaurant.getRestImage());
            nearest.setDistance(distance);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(nearest);
                }
            });
        }

        List<NearestRestaurant> nearestRestaurants = new ArrayList<>();
        nearestRestaurants = realm.where(NearestRestaurant.class).findAll().sort("distance", Sort.ASCENDING);
        getView().stopLoading();

    }
}
