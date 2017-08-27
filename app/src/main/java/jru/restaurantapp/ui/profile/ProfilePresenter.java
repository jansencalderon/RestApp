package jru.restaurantapp.ui.profile;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import jru.restaurantapp.R;
import jru.restaurantapp.app.App;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.model.data.User;
import jru.restaurantapp.model.response.ResultResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class ProfilePresenter extends MvpNullObjectBasePresenter<ProfileView> {

    private static final String TAG = ProfilePresenter.class.getSimpleName();
    private Realm realm;
    private User user;
    // private RealmResults<Event> eventRealmResults;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();
    }

    void changePassword(String currPass, String newPass, String confirmNewPass) {
        final User user = App.getUser();
        if (currPass.equals(user.getPassword())) {

            if (confirmNewPass.equals(confirmNewPass.toLowerCase())) {
                getView().showAlert("Password must have Uppercase!");
            } else if (confirmNewPass.length() < 8) {
                getView().showAlert("Password must be atleast 8 characters");
            } else if (confirmNewPass.matches("[A-Za-z0-9 ]*")) {
                getView().showAlert("Password must have at least 1 numeric and special character");
            } else if (newPass.equals(confirmNewPass)) {
                getView().startLoading();
                App.getInstance().getApiInterface().changePassword(user.getUserId() + "", newPass).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, final Response<User> response) {
                        getView().stopLoading();
                        if (response.isSuccessful() && response.body().getUserId() == user.getUserId()) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(response.body());
                                    getView().onPasswordChanged();
                                }
                            });
                        } else {
                            getView().showAlert(response.message() != null ? response.message()
                                    : "Unknown Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        getView().stopLoading();
                        Log.e(TAG, "onFailure: Error calling login api", t);
                        getView().stopLoading();
                        getView().showAlert("Error Connecting to Server");
                    }
                });
            } else {
                getView().showAlert("New Password Mismatch");
            }
        } else {
            getView().showAlert("Wrong Current Password!");
        }
    }

    /* public void updateUserWithImage(File image, String userId, String firstName, String lastName, String contact, String birthday, String address) {
         if (firstName.equals("") || lastName.equals("") || birthday.equals("") || contact.equals("") || address.equals("")) {
             getView().showAlert("Fill-up all fields");
         } else {
             getView().startLoading();
             RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
             MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", image.getName(), requestFile);
             App.getInstance().getApiInterface().updateUserWithImage(body,
                     createPartFromString(userId), createPartFromString(firstName), createPartFromString(lastName), createPartFromString(contact)
                     , createPartFromString(birthday), createPartFromString(address))
                     .enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, final Response<User> response) {
                             getView().stopLoading();
                             if (response.isSuccessful() && response.body().getUserId() == user.getUserId()) {
                                 realm.executeTransaction(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
                                         realm.copyToRealmOrUpdate(response.body());
                                         getView().finishAct();
                                     }
                                 });
                             } else {
                                 getView().showAlert("Oops something went wrong");
                             }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             Log.e(TAG, "onFailure: Error calling login api", t);
                             getView().stopLoading();
                             getView().showAlert("Error Connecting to Server");
                         }
                     });
         }
     }
 */
    public void updateUser(String userId, String firstName, String lastName, String contact, String birthday, String address) {
        if (firstName.equals("") || lastName.equals("") || birthday.equals("") || contact.equals("") || address.equals("")) {
            getView().showAlert("Fill-up all fields");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().updateUser(userId, firstName, lastName, contact, birthday, address)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, final Response<User> response) {
                            getView().stopLoading();
                            if (response.isSuccessful() && response.body().getUserId() == user.getUserId()) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealmOrUpdate(response.body());
                                        getView().finishAct();
                                    }
                                });
                            } else {
                                getView().showAlert("Oops something went wrong");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling login api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse("multipart/form-data"), descriptionString);
    }

    public void onStop() {
        realm.close();
    }
}
