package spbau.mit.divan.foodhunter.dishes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ксения on 08.02.2016.
 */
public class Place implements Serializable, MapObject {
    private String name;
    private String openHours;
    private List<Integer> menu;
    private double rate;
    private int rateIndex;
    private List<String> reviews;
    private int id;
    private String address;
    private double latitude;
    private double longitude;

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getOpenHours() {
        return openHours;
    }

    public List<Integer> getMenu() {
        return menu;
    }

    public int getId() {
        return id;
    }

    public int getRateIndex() {
        return rateIndex;
    }

    public double getRate() {
        return rate;
    }


    public List<String> getReviews() {
        return reviews;
    }

    public Place(String name, String address, String openHours, List<Integer> menu,
                 List<String> reviews, double rate, int rateIndex, double latitude, double longitude, int id) {
        this.name = name;
        this.openHours = openHours;
        this.menu = menu;
        this.reviews = reviews;
        this.rate = rate;
        this.rateIndex = rateIndex;
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setRate(float mark) {
        rate = (rateIndex * rate + mark) / (rateIndex + 1);
        rateIndex++;
    }

    public void sendReview(String review) {
        reviews.add(review);
    }

}