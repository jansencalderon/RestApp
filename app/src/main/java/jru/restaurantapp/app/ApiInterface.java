package jru.restaurantapp.app;


import java.util.List;

import jru.restaurantapp.model.data.Reservation;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.model.data.User;
import jru.restaurantapp.model.response.LoginResponse;
import jru.restaurantapp.model.response.ResultResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {

    @FormUrlEncoded
    @POST(Endpoints.LOGIN)
    Call<LoginResponse> login(@Field(Constants.EMAIL) String username,
                              @Field(Constants.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Endpoints.SAVE_USER_TOKEN)
    Call<ResultResponse> saveUserToken(@Field(Constants.USER_ID) String username,
                                       @Field("reg_token") String reg_token);

    @FormUrlEncoded
    @POST(Endpoints.DELETE_USER_TOKEN)
    Call<ResultResponse> deleteUserToken(@Field("reg_token") String reg_token);

    @FormUrlEncoded
    @POST(Endpoints.REGISTER)
    Call<ResultResponse> register(@Field(Constants.EMAIL) String username,
                                  @Field(Constants.PASSWORD) String password,
                                  @Field(Constants.FIRST_NAME) String firstName,
                                  @Field(Constants.LAST_NAME) String lastName,
                                  @Field(Constants.CONTACT) String contact,
                                  @Field(Constants.BIRTHDAY) String birthday,
                                  @Field(Constants.ADDRESS) String address,
                                  @Field(Constants.QUESTION) String question,
                                  @Field(Constants.ANSWER) String answer
    );

    @FormUrlEncoded
    @POST(Endpoints.VERIFY)
    Call<ResultResponse> verify(@Field(Constants.EMAIL) String email,
                                  @Field(Constants.VER_CODE) String code);


    @FormUrlEncoded
    @POST("changePassword")
    Call<User> changePassword(@Field(Constants.USER_ID) String user_id,
                                        @Field(Constants.PASSWORD) String password);

    @Multipart
    @POST("updateUserWithImage")
    Call<User> updateUserWithImage(@Part MultipartBody.Part image,
                                   @Part(Constants.USER_ID) RequestBody user_id,
                                   @Part(Constants.FIRST_NAME) RequestBody first_name,
                                   @Part(Constants.LAST_NAME) RequestBody last_name,
                                   @Part(Constants.CONTACT) RequestBody contact,
                                   @Part(Constants.BIRTHDAY) RequestBody birthday,
                                   @Part(Constants.ADDRESS) RequestBody address);


    @FormUrlEncoded
    @POST("updateUser")
    Call<User> updateUser(@Field(Constants.USER_ID) String user_id,
                          @Field(Constants.FIRST_NAME) String first_name,
                          @Field(Constants.LAST_NAME) String last_name,
                          @Field(Constants.CONTACT) String contact,
                          @Field(Constants.BIRTHDAY) String birthday,
                          @Field(Constants.ADDRESS) String address);

    @FormUrlEncoded
    @POST("passwordAlert")
    Call<ResultResponse> passwordAlert(@Field(Constants.EMAIL) String email);

    @FormUrlEncoded
    @POST("checkEmail")
    Call<ResultResponse> checkEmail(@Field(Constants.EMAIL) String email);


    @FormUrlEncoded
    @POST("checkAnswer")
    Call<ResultResponse> checkAnswer(@Field(Constants.EMAIL) String email,
                                     @Field(Constants.QUESTION) String question,
                                     @Field(Constants.ANSWER) String answer);

    @FormUrlEncoded
    @POST(Endpoints.VERIFY_RESEND_EMAIL)
    Call<ResultResponse> verifyResendEmail(@Field(Constants.USER_ID) String user_id);


    @Multipart
    @POST("upload.php")
    Call<ResultResponse> uploadImage(@Part MultipartBody.Part image);

    @POST("getRestaurants")
    Call<List<Restaurant>> getRestaurants();


    @FormUrlEncoded
    @POST("sendReservation")
    Call<ResultResponse> sendReservation(@Field("rest_id") String rest_id,@Field("user_id") String user_id,
                                         @Field("trans_date") String trans_date, @Field("trans_head_count") String trans_head_count);


    @FormUrlEncoded
    @POST("getReservations")
    Call<List<Reservation>> getReservations(@Field("user_id") String user_id);
}
