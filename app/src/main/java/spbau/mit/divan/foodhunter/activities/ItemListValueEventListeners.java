package spbau.mit.divan.foodhunter.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.MapObject;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.Uses.DISH;
import static spbau.mit.divan.foodhunter.activities.Uses.PLACE;

public class ItemListValueEventListeners {
    public static ValueEventListener menuListener(Place place, Activity context) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Dish> menu = Client.getMenu(dataSnapshot, place);
                ListAdapter adapter = new ItemsAdapter(context, menu);
                setList(menu, context, adapter, FoodPage.class, DISH);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    public static ValueEventListener placesListListener(String placeName, Activity context) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Place> places = Client.getPlacesForName(dataSnapshot, placeName);
                ListAdapter adapter = new ItemsAdapter(places, context);
                setList(places, context, adapter, PlacePage.class, PLACE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    public static ValueEventListener dishesListListener(String dishName, Activity context) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Dish> dishes = Client.getDishesForName(dataSnapshot, dishName);
                ListAdapter adapter = new ItemsAdapter(context, dishes);
                setList(dishes, context, adapter, FoodPage.class, DISH);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    private static void setList(List<? extends MapObject> items, Activity context,
                                ListAdapter adapter, Class<?> nextActivity, String extra) {
        if (items.isEmpty()) {
            context.findViewById(R.id.no_result).setVisibility(View.VISIBLE);
        } else {
            ListView listView = (ListView) context.findViewById(R.id.tokens_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(((parent, view, position, id) -> {
                Intent intent = new Intent(context, nextActivity);
                intent.putExtra(extra, (Serializable) items.get(position));
                context.startActivity(intent);
            }));
        }
    }
}
