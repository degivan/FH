package spbau.mit.divan.foodhunter.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.annimon.stream.Collectors;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

public class ItemListValueEventListeners {
    private static void setList(List items, Activity context, ListAdapter adapter, Class<?> nextActivity, String extra) {
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

    public static ValueEventListener menuListener(Place place, Activity context) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Dish> menu = Client.getMenu(dataSnapshot, place)
                        .collect(Collectors.toList());
                ListAdapter adapter = new ItemsAdapter(context, menu);
                setList(menu, context, adapter, FoodPage.class, "dish");
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
                List<Place> places = Client.getPlacesForName(dataSnapshot, placeName)
                        .collect(Collectors.toList());
                ListAdapter adapter = new ItemsAdapter(places, context);
                setList(places, context, adapter, PlacePage.class, "place");
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
                List<Dish> dishes = Client.getDishesForName(dataSnapshot, dishName)
                        .collect(Collectors.toList());
                ListAdapter adapter = new ItemsAdapter(context, dishes);
                setList(dishes, context, adapter, FoodPage.class, "dish");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }
}
