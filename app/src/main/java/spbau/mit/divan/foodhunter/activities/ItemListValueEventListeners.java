package spbau.mit.divan.foodhunter.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.activities.ItemsAdapter.Item;
import spbau.mit.divan.foodhunter.dishes.MapObject;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static java.lang.Math.round;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.DISH_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;

public class ItemListValueEventListeners {
    public static ValueEventListener menuListener(Place place, Activity context) {
        return itemsListListener(context, DISH_EXTRA_NAME, FoodPage.class,
                snapshot -> Client.getMenu(snapshot, place),
                d -> new Item(d.getName(), "", Integer.toString(d.getPrice())));
    }

    public static ValueEventListener placesListListener(String placeName, Activity context) {
        return itemsListListener(context, PLACE_EXTRA_NAME, PlacePage.class,
                snapshot -> Client.getPlacesForName(snapshot, placeName),
                p -> new Item(p.getName(), p.getAddress(), Double.toString(round(p.getRate())) + "/5"));
    }

    public static ValueEventListener dishesListListener(String dishName, Activity context) {
        return itemsListListener(context, DISH_EXTRA_NAME, FoodPage.class,
                snapshot -> Client.getDishesForName(snapshot, dishName),
                d -> new Item(d.getName(), d.getAddress() + " " + d.getPlaceName(), Integer.toString(d.getPrice())));
    }

    private static <T extends MapObject> ValueEventListener itemsListListener(Activity context, String extra, Class<?> nextActivity,
                                                                              Function<DataSnapshot, List<T>> infoGetter,
                                                                              Function<T, Item> converter) {
        return Client.createOnDataChangeListener(dataSnapshot -> {
            List<T> mapObjects = infoGetter.apply(dataSnapshot);
            List<Item> items = Stream.of(mapObjects)
                    .map(converter::apply)
                    .collect(Collectors.toList());
            ListAdapter adapter = new ItemsAdapter(items, context);
            setList(mapObjects, context, adapter, nextActivity, extra);
        });
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
