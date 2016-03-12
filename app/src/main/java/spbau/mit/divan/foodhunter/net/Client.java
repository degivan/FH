package spbau.mit.divan.foodhunter.net;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.MapObject;
import spbau.mit.divan.foodhunter.dishes.Place;

public class Client {
    private static final Firebase APP_DATABASE = new Firebase("https://blistering-fire-6963.firebaseio.com/");

    public static void request(ValueEventListener listener) {
        APP_DATABASE.addListenerForSingleValueEvent(listener);
    }

    public static List<Dish> getMenu(DataSnapshot snapshot, Place place) {
        Set<Integer> menuSet = new HashSet<>(place.getMenu());
        return getDishes(snapshot)
                .filter(dish -> menuSet.contains(dish.getDishId()))
                .collect(Collectors.toList());
    }

    public static Place getPlace(DataSnapshot snapshot, int id) {
        DataSnapshot placeSnapshot = snapshot.child("places").child(Integer.toString(id));
        return getPlaceFromSnapshot(placeSnapshot);
    }

    public static List<Place> getPlacesForNameNearby(DataSnapshot snapshot, LatLng coordinates, double distance, String name) {
        return Stream.of(getPlacesForName(snapshot, name))
                .filter(p -> closeEnough(p, coordinates, distance))
                .collect(Collectors.toList());
    }

    public static List<Place> getPlacesForName(DataSnapshot snapshot, String name) {
        return getPlaces(snapshot)
                .filter(p -> MapObjectNameContainsString(p, name))
                .collect(Collectors.toList());
    }

    private static Stream<Place> getPlaces(DataSnapshot snapshot) {
        return Stream.of(snapshot.child("places").getChildren())
                .map(Client::getPlaceFromSnapshot);
    }

    public static List<Dish> getDishesForNameNearby(DataSnapshot snapshot, LatLng coordinates, double distance, String name) {
        return Stream.of(getDishesForName(snapshot, name))
                .filter(d -> closeEnough(d, coordinates, distance))
                .collect(Collectors.toList());
    }

    public static List<Dish> getDishesForName(DataSnapshot snapshot, String name) {
        return getDishes(snapshot)
                .filter(d -> MapObjectNameContainsString(d, name))
                .collect(Collectors.toList());
    }

    private static Stream<Dish> getDishes(DataSnapshot snapshot) {
        return Stream.of(snapshot.child("dishes").getChildren())
                .map(dishSnapshot -> dishSnapshot.getValue(Dish.class));
    }

    public static void changeDishRate(Dish dish, float rate) {
        dish.setRate(rate);
        Map<String, Object> rateChanges = new HashMap<>();
        rateChanges.put("rate", dish.getRate());
        rateChanges.put("rateIndex", dish.getRateIndex());
        APP_DATABASE.child("dishes").child(Integer.toString(dish.getDishId())).updateChildren(rateChanges);
    }

    public static void changePlaceRate(Place place, float rate) {
        place.setRate(rate);
        Map<String, Object> rateChanges = new HashMap<>();
        rateChanges.put("rate", place.getRate());
        rateChanges.put("rateIndex", place.getRateIndex());
        APP_DATABASE.child("places").child(Integer.toString(place.getId())).updateChildren(rateChanges);

    }

    public static void sendReview(Place place, String review) {
        place.sendReview(review);
        APP_DATABASE.child("places").child(Integer.toString(place.getId())).child("reviews").push().setValue(review);
    }

    public static void pushPlace(String name, String address, String openHours, double lat, double lng) {
        request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int id = (int) dataSnapshot.child("places").getChildrenCount() + 1;
                Place place = new Place(name, address, openHours, new ArrayList<>(), new ArrayList<>(), 0, 0, lat, lng, id);
                APP_DATABASE.child("places").child(Integer.toString(id)).setValue(place);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void pushDish(int price, String name, String address, String description, double lat, double lng, String placeName, int placeId) {
       request(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               int id = (int) dataSnapshot.child("places").getChildrenCount() + 1;
               Dish dish = new Dish(price, name, description, id, placeId, placeName, address, 0, 0, lat, lng);
               APP_DATABASE.child("dishes").child(Integer.toString(id)).setValue(dish);
               APP_DATABASE.child("places").child(Integer.toString(placeId)).child("menu").push().setValue(id);
           }

           @Override
           public void onCancelled(FirebaseError firebaseError) {

           }
       });
    }

    private static boolean closeEnough(MapObject mapObject, LatLng coordinates, double distance) {
        return Math.pow(mapObject.getLatitude() - coordinates.latitude, 2) + Math.pow(mapObject.getLongitude() - coordinates.longitude, 2) < distance;
    }

    private static Place getPlaceFromSnapshot(DataSnapshot placeSnapshot) {
        String name = placeSnapshot.child("name").getValue(String.class);
        String openHours = placeSnapshot.child("openHours").getValue(String.class);
        String address = placeSnapshot.child("address").getValue(String.class);
        List<Integer> menu = new ArrayList<>();
        for (DataSnapshot dishSnapshot : placeSnapshot.child("menu").getChildren())
            menu.add(dishSnapshot.getValue(Integer.class));
        List<String> reviews = new ArrayList<>();
        for (DataSnapshot reviewSnapshot : placeSnapshot.child("reviews").getChildren())
            reviews.add(reviewSnapshot.getValue(String.class));
        double latitude = placeSnapshot.child("latitude").getValue(Double.class);
        double longitude = placeSnapshot.child("longitude").getValue(Double.class);
        int id = placeSnapshot.child("id").getValue(Integer.class);
        double rate = placeSnapshot.child("rate").getValue(Double.class);
        int rateIndex = placeSnapshot.child("rateIndex").getValue(Integer.class);
        return new Place(name, address, openHours, menu, reviews, rate, rateIndex, latitude, longitude, id);
    }

    private static boolean MapObjectNameContainsString(MapObject mapObject, String name) {
        return mapObject.getName().toLowerCase().replaceAll(" ", "").contains(name.toLowerCase().replaceAll(" ", ""));
    }
}
