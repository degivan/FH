package spbau.mit.divan.foodhunter.dishes;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spbau.mit.divan.foodhunter.net.Client;

/**
 * Created by Ксения on 08.02.2016.
 */
public class Place implements Serializable {
    public final String name;
    public final String address;
    public final String openHours;
    public final List<Integer> menu;
    private double rate;
    private int rateIndex;
    private List<String> reviews;
    public final double latitude;
    public final double longitude;
    public final int id;


    public Place(String name, String address, String openHours, List<Integer> menu,
                 List<String> reviews, double rate, int rateIndex, double latitude, double longitude, int id) {
        this.name = name;
        this.address = address;
        this.openHours = openHours;
        this.menu = menu;
        this.reviews = reviews;
        this.rate = rate;
        this.rateIndex = rateIndex;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public void setRate(float mark) {
        rate = (rateIndex * rate + mark) / (rateIndex + 1);
        rateIndex++;
    }

    public double getRate() {
        return rate;
    }


    public List<String> getReviews() { return reviews; }

    public void sendReview(String review) {
        reviews.add(review);
    }

    public int getRateIndex() { return  rateIndex; }
}