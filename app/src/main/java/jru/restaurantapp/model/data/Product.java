package jru.restaurantapp.model.data;

/**
 * Created by Jansen on 7/18/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject{

    @PrimaryKey
    @SerializedName("prod_id")
    @Expose
    private Integer prodId;
    @SerializedName("rest_id")
    @Expose
    private Integer restId;
    @SerializedName("prod_name")
    @Expose
    private String prodName;
    @SerializedName("prod_price")
    @Expose
    private Integer prodPrice;
    @SerializedName("prod_info")
    @Expose
    private String prodInfo;
    @SerializedName("prod_image")
    @Expose
    private String prodImage;

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getRestId() {
        return restId;
    }

    public void setRestId(Integer restId) {
        this.restId = restId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(Integer prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getProdInfo() {
        return prodInfo;
    }

    public void setProdInfo(String prodInfo) {
        this.prodInfo = prodInfo;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }

}
