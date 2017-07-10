package jru.restaurantapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Jansen on 6/29/2017.
 */

public class NearestRestaurant extends RealmObject {

    @SerializedName("rest_id")
    @Expose
    private Integer restId;
    @SerializedName("rest_name")
    @Expose
    private String restName;
    @SerializedName("rest_category")
    @Expose
    private String restCategory;
    @SerializedName("rest_add")
    @Expose
    private String restAdd;
    @SerializedName("rest_contact")
    @Expose
    private String restContact;
    @SerializedName("rest_hours")
    @Expose
    private String restHours;
    @SerializedName("rest_lat")
    @Expose
    private Double restLat;
    @SerializedName("rest_lng")
    @Expose
    private Double restLng;
    @SerializedName("rest_slot_left")
    @Expose
    private Integer restSlotLeft;
    @SerializedName("rest_slot_max")
    @Expose
    private Integer restSlotMax;
    @SerializedName("rest_image")
    @Expose
    private String restImage;
    @SerializedName("rest_username")
    @Expose
    private String restUsername;
    @SerializedName("rest_password")
    @Expose
    private String restPassword;

    private double distance;

    public Integer getRestId() {
        return restId;
    }

    public void setRestId(Integer restId) {
        this.restId = restId;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestAdd() {
        return restAdd;
    }

    public void setRestAdd(String restAdd) {
        this.restAdd = restAdd;
    }

    public String getRestContact() {
        return restContact;
    }

    public void setRestContact(String restContact) {
        this.restContact = restContact;
    }

    public String getRestHours() {
        return restHours;
    }

    public void setRestHours(String restHours) {
        this.restHours = restHours;
    }

    public Double getRestLat() {
        return restLat;
    }

    public void setRestLat(Double restLat) {
        this.restLat = restLat;
    }

    public Double getRestLng() {
        return restLng;
    }

    public void setRestLng(Double restLng) {
        this.restLng = restLng;
    }

    public Integer getRestSlotLeft() {
        return restSlotLeft;
    }

    public void setRestSlotLeft(Integer restSlotLeft) {
        this.restSlotLeft = restSlotLeft;
    }

    public Integer getRestSlotMax() {
        return restSlotMax;
    }

    public void setRestSlotMax(Integer restSlotMax) {
        this.restSlotMax = restSlotMax;
    }

    public String getRestImage() {
        return restImage;
    }

    public void setRestImage(String restImage) {
        this.restImage = restImage;
    }

    public String getRestUsername() {
        return restUsername;
    }

    public void setRestUsername(String restUsername) {
        this.restUsername = restUsername;
    }

    public String getRestPassword() {
        return restPassword;
    }

    public void setRestPassword(String restPassword) {
        this.restPassword = restPassword;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRestCategory() {
        return restCategory;
    }

    public void setRestCategory(String restCategory) {
        this.restCategory = restCategory;
    }
}