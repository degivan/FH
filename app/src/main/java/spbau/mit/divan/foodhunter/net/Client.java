package spbau.mit.divan.foodhunter.net;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;

public class Client {
    public static final Firebase appDatabase = new Firebase("https://blistering-fire-6963.firebaseio.com/");

    public static void request(ValueEventListener listener) {
        appDatabase.addListenerForSingleValueEvent(listener);
    }

    public static void getMenu(List<Dish> menu, DataSnapshot snapshot, Place place) {
        Set<Integer> menuSet = Stream.of(place.menu).collect(Collectors.toSet());
        getDishes(menu, snapshot, dish -> menuSet.contains(dish.dishId));
    }

    public static Place getPlace(DataSnapshot snapshot, int id) {
        DataSnapshot placeSnapshot = snapshot.child("places").child(Integer.toString(id));
        return getPlaceFromSnapshot(placeSnapshot);

    }

    public static void getPlacesForNameNearby(List<Place> places, DataSnapshot snapshot, LatLng coordinates, double distance, String name){
        List<Place> newPlaces = new ArrayList<Place>();
        getPlacesForName(newPlaces, snapshot, name);
        Stream.of(newPlaces)
                .filter(p ->  Math.pow(p.latitude - coordinates.latitude, 2) + Math.pow(p.longitude - coordinates.longitude, 2) < distance)
                .forEach(places::add);
    }

    public static void getPlacesNearby(List<Place> places, DataSnapshot snapshot, LatLng coordinates, double distance) {
        getPlaces(places, snapshot, p -> Math.pow(p.latitude - coordinates.latitude, 2) + Math.pow(p.longitude - coordinates.longitude, 2) < distance);
    }

    public static void getPlacesForName(List<Place> places, DataSnapshot snapshot, String name) {
        getPlaces(places, snapshot, p -> p.name.toLowerCase().contains(name.toLowerCase()));
    }

    public static void getPlaces(List<Place> places, DataSnapshot snapshot, Predicate<Place> predicate) {
        Stream.of(snapshot.child("places").getChildren())
                .map(Client::getPlaceFromSnapshot)
                .filter(predicate)
                .forEach(places::add);
    }

    public static void  getDishesForNameNearby(List<Dish> dishes, DataSnapshot snapshot, LatLng coordinates, double distance, String name) {
        List<Dish> newDishes= new ArrayList<Dish>();
        getDishesForName(newDishes, snapshot, name);
        Stream.of(newDishes)
                .filter(p ->  Math.pow(p.latitude - coordinates.latitude, 2) + Math.pow(p.longitude - coordinates.longitude, 2) < distance)
                .forEach(dishes::add);
    }

    public static void getDishesNearby(List<Dish> dishes, DataSnapshot snapshot, LatLng coordinates, double distance) {
        getDishes(dishes, snapshot, d -> Math.pow(d.latitude - coordinates.latitude, 2) + Math.pow(d.longitude - coordinates.longitude, 2) < distance);
    }

    public static void getDishesForName(List<Dish> dishes, DataSnapshot snapshot, String name) {
        getDishes(dishes, snapshot, d -> d.name.toLowerCase().contains(name.toLowerCase()));
    }

    public static void getDishes(List<Dish> dishes, DataSnapshot snapshot, Predicate<Dish> predicate) {
         Stream.of(snapshot.child("dishes").getChildren())
                .map(dishSnapshot -> {
                    int price = Integer.parseInt(dishSnapshot.child("price").getValue().toString());
                    String name = dishSnapshot.child("name").getValue().toString();
                    String address = dishSnapshot.child("address").getValue().toString();
                    String description = dishSnapshot.child("description").getValue().toString();
                    Double rate = Double.parseDouble(dishSnapshot.child("rate").getValue().toString());
                    int rateIndex = Integer.parseInt(dishSnapshot.child("rateIndex").getValue().toString());
                    int dishId = Integer.parseInt(dishSnapshot.child("dishId").getValue().toString());
                    int placeId = Integer.parseInt(dishSnapshot.child("placeId").getValue().toString());
                    String placeName = dishSnapshot.child("placeName").getValue().toString();

                    Double latitude = Double.parseDouble(dishSnapshot.child("latitude").getValue().toString());
                    Double longitude = Double.parseDouble(dishSnapshot.child("longitude").getValue().toString());
                    return new Dish(price, name, description, dishId, placeId, placeName, address, rate, rateIndex, latitude, longitude);
                })
                .filter(predicate)
                .forEach(dishes::add);
    }

    public static void setDishRate(Dish dish, float rate) {
        dish.setRate(rate);
        Map<String, Object> rateChanges = new HashMap<String, Object>();
        rateChanges.put("rate", dish.getRate());
        rateChanges.put("rateIndex", dish.getRateIndex());
        appDatabase.child("dishes").child(Integer.toString(dish.dishId)).updateChildren(rateChanges);
    }

    public static void setPlaceRate(Place place, float rate) {
        place.setRate(rate);
        Map<String, Object> rateChanges = new HashMap<String, Object>();
        rateChanges.put("rate", place.getRate());
        rateChanges.put("rateIndex", place.getRateIndex());
        appDatabase.child("places").child(Integer.toString(place.id)).updateChildren(rateChanges);

    }

    public static void sendReview(Place place, String review) {
        place.sendReview(review);
        appDatabase.child("places").child(Integer.toString(place.id)).child("reviews").push().setValue(review);
    }

    private static Place getPlaceFromSnapshot(DataSnapshot placeSnapshot) {
        String name = placeSnapshot.child("name").getValue().toString();
        String openHours = placeSnapshot.child("openHours").getValue().toString();
        String address = placeSnapshot.child("address").getValue().toString();
        List<Integer> menu = new ArrayList<Integer>();
        for (DataSnapshot dishSnapshot : placeSnapshot.child("menu").getChildren())
            menu.add(Integer.valueOf(dishSnapshot.getValue().toString()));
        List<String> reviews = new ArrayList<String>();
        for (DataSnapshot reviewSnapshot : placeSnapshot.child("reviews").getChildren())
            reviews.add(reviewSnapshot.getValue().toString());
        double latitude = Double.valueOf(placeSnapshot.child("latitude").getValue().toString());
        double longitude = Double.valueOf(placeSnapshot.child("longitude").getValue().toString());
        int id = Integer.parseInt(placeSnapshot.child("id").getValue().toString());
        double rate = Double.parseDouble(placeSnapshot.child("rate").getValue().toString());
        int rateIndex = Integer.parseInt(placeSnapshot.child("rateIndex").getValue().toString());
        return new Place(name, address, openHours, menu, reviews, rate, rateIndex, latitude, longitude, id );
    }
}
