package spbau.mit.divan.foodhunter.dishes;

import java.io.Serializable;
import java.util.List;

public class Place implements Serializable, MapObject {
    private String name;
    private String openHours;
    private double rate;
    private int rateIndex;
    private int id;
    private String address;
    private double latitude;
    private double longitude;
    private List<Integer> menu;
    private List<String> reviews;

    public Place(String name, String address, String openHours, List<Integer> menu,
                 List<String> reviews, double rate, int rateIndex, double latitude, double longitude, int id) {
        this.name = name;
        this.openHours = openHours;
        this.rate = rate;
        this.rateIndex = rateIndex;
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.menu = menu;
        this.reviews = reviews;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getOpenHours() {
        return openHours;
    }

    public double getRate() {
        return rate;
    }

    public int getRateIndex() {
        return rateIndex;
    }

    public int getId() {
        return id;
    }

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

    public List<Integer> getMenu() {
        return menu;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void sendReview(String review) {
        reviews.add(review);
    }

    public void setRate(float mark) {
        rate = (rateIndex * rate + mark) / (rateIndex + 1);
        rateIndex++;
    }
}