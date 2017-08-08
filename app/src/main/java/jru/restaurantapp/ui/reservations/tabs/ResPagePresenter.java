package jru.restaurantapp.ui.reservations.tabs;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import jru.restaurantapp.model.data.Reservation;
import jru.restaurantapp.utils.DateTimeUtils;

/**
 * Created by Sen on 2/28/2017.
 */

public class ResPagePresenter extends MvpNullObjectBasePresenter<ResPageView> {
    private Realm realm;
    private RealmResults<Reservation> reservationRealmResults;
    private String type;

    public void onStart(String type) {
        realm = Realm.getDefaultInstance();
        this.type = type;
        reservationRealmResults = realm.where(Reservation.class).findAll();
        filterList();
    }

    private void filterList() {
        if (reservationRealmResults.isLoaded() && reservationRealmResults.isValid()) {
            List<Reservation> list;
            if (type.equals("Upcoming")) {
                list = realm.copyFromRealm(reservationRealmResults.where()
                        .greaterThan("transDate", DateTimeUtils.getDateToday())
                        .findAll());
               // getView().showAlert(DateTimeUtils.dateTodayToast());
            } else {
                list = realm.copyFromRealm(reservationRealmResults.where()
                        .lessThan("transDate",DateTimeUtils.getDateToday())
                        .findAll());
            }
            getView().setEvents(list);
        }

    }

    public void onStop() {
        realm.close();
    }

}
