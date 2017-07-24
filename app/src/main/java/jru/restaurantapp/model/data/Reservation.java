package jru.restaurantapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Jansen on 7/24/2017.
 */

public class Reservation extends RealmObject {

    @SerializedName("trans_id")
    @Expose
    private Integer transId;
    @SerializedName("rest_id")
    @Expose
    private Integer restId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("trans_date")
    @Expose
    private Date transDate;
    @SerializedName("trans_head_count")
    @Expose
    private Integer transHeadCount;
    @SerializedName("trans_status")
    @Expose
    private String transStatus;
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    public Integer getRestId() {
        return restId;
    }

    public void setRestId(Integer restId) {
        this.restId = restId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Integer getTransHeadCount() {
        return transHeadCount;
    }

    public void setTransHeadCount(Integer transHeadCount) {
        this.transHeadCount = transHeadCount;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
