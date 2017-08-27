package jru.restaurantapp.ui.profile;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;


/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public interface ProfileView extends MvpView{


    void showAlert(String message);

    void onChangePassword();

    void onPasswordChanged();

    void onEdit();

    void startLoading();

    void stopLoading();

    void finishAct();

    void onBirthdayClicked();

    void onPhotoClicked();

    void finish();
}
