package spbau.mit.divan.foodhunter.dishes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Place implements Serializable, MapObject {
    private String name;
    private String openHours;
    private double rate;
    private int rateIndex;
    private int id;
    private String address;
    private double latitude;
    private double longitude;
    private Collection<Integer> menu;
    private Collection<String> reviews;

    public Place() {
    }

    public Place(String name, String address, String openHours, Collection<Integer> menu,
                 Collection<String> reviews, double rate, int rateIndex, double latitude, double longitude, int id) {
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

    @Override
    public double getRate() {
        return rate;
    }

    @Override
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

    public Collection<Integer> getMenu() {
        if (menu == null) {
            menu = new ArrayList<>();
        }
        return menu;
    }

    public Collection<String> getReviews() {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    public static String getReview(String s) {
        return s.split("#")[1];
    }

    public static String getLogin(String s) {
        return s.split("#")[0];
    }

    public void sendReview(String review) {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }

    public void setRate(float mark) {
        rate = (rateIndex * rate + mark) / (rateIndex + 1);
        rateIndex++;
    }


    public void addDishToMenu(int id) {
        if (menu == null) {
            menu = new ArrayList<>();
        }
        menu.add(id);
    }
}