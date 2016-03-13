package spbau.mit.divan.foodhunter.net;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
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
    private static final Firebase APP_DATABASE = new Firebase("https://food-hunter.firebaseio.com/");
    private static final String LOG_TAG = "ClientLogs";
    private static final String ON_CANCELLED_LOG = "Called onCancelled in Client.createOnDataChangeListener.";

    public static void request(ValueEventListener listener) {
        APP_DATABASE.addListenerForSingleValueEvent(listener);
    }

    public static void request(Consumer<DataSnapshot> consumer) {
        request(createOnDataChangeListener(consumer));
    }

    public static ValueEventListener createOnDataChangeListener(Consumer<DataSnapshot> consumer) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consumer.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, ON_CANCELLED_LOG);
            }
        };
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
        dish.changeRate(rate);
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
        APP_DATABASE.child("places").child(Integer.toString(place.getId())).child("reviews").setValue(place.getReviews());
    }

    public static void pushPlace(String name, String address, String openHours, double lat, double lng) {
        request(snapshot -> {
            int id = (int) snapshot.child("places").getChildrenCount() + 1;
            Place place = new Place(name, address, openHours, new ArrayList<>(), new ArrayList<>(), 0, 0, lat, lng, id);
            APP_DATABASE.child("places").child(Integer.toString(id)).setValue(place);
        });
    }

    public static void pushDish(int price, String name, String description, Place place) {
        request(snapshot -> {
            int id = (int) snapshot.child("dishes").getChildrenCount() + 1;
            Dish dish = new Dish(price, name, description, id, place.getId(), place.getName(),
                    place.getAddress(), 0, 0, place.getLatitude(), place.getLongitude());
            place.addDishToMenu(id);
            APP_DATABASE.child("dishes").child(Integer.toString(id)).setValue(dish);
            APP_DATABASE.child("places").child(Integer.toString(place.getId())).child("menu").setValue(place.getMenu());
        });
    }

    private static boolean closeEnough(MapObject mapObject, LatLng coordinates, double distance) {
        return Math.pow(mapObject.getLatitude() - coordinates.latitude, 2) + Math.pow(mapObject.getLongitude() - coordinates.longitude, 2) < distance;
    }

    private static Place getPlaceFromSnapshot(DataSnapshot placeSnapshot) {
        return placeSnapshot.getValue(Place.class);
    }

    private static boolean MapObjectNameContainsString(MapObject mapObject, String name) {
        return mapObject.getName().toLowerCase().replaceAll(" ", "").contains(name.toLowerCase().replaceAll(" ", ""));
    }

}
