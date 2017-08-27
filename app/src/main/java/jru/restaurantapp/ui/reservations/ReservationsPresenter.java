package jru.restaurantapp.ui.reservations;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import jru.restaurantapp.app.App;
import jru.restaurantapp.model.data.Reservation;
import jru.restaurantapp.model.data.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jansen on 7/24/2017.
 */

class ReservationsPresenter extends MvpNullObjectBasePresenter<ReservationsView>{
    private Realm realm;
    private User user;
    private String TAG = ReservationsPresenter.class.getSimpleName();

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();
        getReservations();
    }

    public void getReservations() {
        getView().startLoading();
        App.getInstance().getApiInterface().getReservations(user.getUserId()+"").enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, final Response<List<Reservation>> response) {
                    getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Reservation.class);
                            realm.copyToRealmOrUpdate(response.body());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                            getView().setData();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            realm.close();
                            Log.e(TAG, "onError: Unable to save Data", error);
                        }
                    });
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
                }
            }


            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
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
