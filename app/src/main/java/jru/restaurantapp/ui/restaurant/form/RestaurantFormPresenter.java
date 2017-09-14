package jru.restaurantapp.ui.restaurant.form;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import io.realm.Realm;
import jru.restaurantapp.R;
import jru.restaurantapp.app.App;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.model.response.ResultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jansen on 7/5/2017.
 */

public class RestaurantFormPresenter extends MvpNullObjectBasePresenter<RestaurantFormView> {
    private Realm realm;
    private String TAG = RestaurantFormPresenter.class.getSimpleName();

    void onStart() {
        realm = Realm.getDefaultInstance();
    }

    Restaurant getRestaurant(int id){
        return realm.copyFromRealm( realm.where(Restaurant.class).equalTo(Constants.RESTAURANT_ID, id).findFirst());
    }

    void onStop() { realm.close();
    }

    void sendReservation(Integer restId, int userId, String date, String headCount) {
        getView().startLoading();
        if(!headCount.equals("") || !date.equals("")){
            App.getInstance().getApiInterface().sendReservation(restId+"", userId+"", date,headCount).enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                    getView().stopLoading();
                    if (response.isSuccessful()) {
                        switch (response.body().getResult()) {
                            case Constants.SUCCESS:
                                getView().onReservationSuccess();
                                break;
                            default:
                                getView().showAlert(String.valueOf(R.string.oops));
                                break;
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            getView().showAlert(errorBody);
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: Error parsing error body as string", e);
                            getView().showAlert(response.message() != null ?
                                    response.message() : "Unknown Exception");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {

                }
            });
        }else {
            getView().showAlert("Fill up all fields");
        }
    }
}
