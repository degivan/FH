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

    public Dish() {
    }

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

    public int getPrice() {
        return price;
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
    public String getName() {
        return name;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public void setRate(float mark) {
        rate = (rateIndex * rate + mark) / (rateIndex + 1);
        rateIndex++;
    }

}
