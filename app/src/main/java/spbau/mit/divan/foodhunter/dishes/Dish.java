package spbau.mit.divan.foodhunter.dishes;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import spbau.mit.divan.foodhunter.net.Client;

/**
 * Created by Ксения on 08.02.2016.
 */
public class Dish implements Serializable {
    public final int price;
    public final String name;
    public final String description;
    private double rate;
    private int rateIndex;
    public final int dishId;
    public final int placeId;
    public final String placeName;
    public final String address;
    public final double latitude;
    public final double longitude;

    public Dish(int price, String name, String description,
                int dishId, int placeId, String placeName,
                String address, double rate, int rateIndex, double latitude, double longitude) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.dishId = dishId;
        this.placeId = placeId;
        this.placeName = placeName;
        this.address = address;
        this.rate = rate;
        this.rateIndex = rateIndex;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setRate(float mark) {
        rate = (rateIndex * rate + mark) / (rateIndex + 1);
        rateIndex++;
     }

    public double getRate() {
        return rate;
    }

    public int getRateIndex() {return rateIndex;}
}
