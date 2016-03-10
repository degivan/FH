package spbau.mit.divan.foodhunter.dishes;

import java.io.Serializable;

/**
 * Created by Ксения on 08.02.2016.
 */
public class Dish implements Serializable, MapObject {
    private int price;
    private String name;
    private String description;
    private double rate;
    private int rateIndex;
    private int dishId;
    private int placeId;
    private String placeName;
    private String address;
    private double latitude;
    private double longitude;

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDishId() {
        return dishId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getRate() {
        return rate;
    }

    public int getRateIndex() {
        return rateIndex;
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

    public Dish() {}

    public void setRate(float mark) {
        rate = (rateIndex * rate + mark) / (rateIndex + 1);
        rateIndex++;
    }

}
