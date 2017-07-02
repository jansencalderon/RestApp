package jru.restaurantapp.ui.map;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import io.realm.Realm;
import jru.restaurantapp.R;
import jru.restaurantapp.app.App;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.model.data.User;
import jru.restaurantapp.model.response.LoginResponse;
import jru.restaurantapp.model.response.ResultResponse;
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


}
