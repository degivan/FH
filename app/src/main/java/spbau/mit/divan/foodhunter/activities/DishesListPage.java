package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.net.Client;

public class DishesListPage extends AppCompatActivity {
    private final List<Dish> dishes = new ArrayList<Dish>();
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_list_page);
        String dishName = getIntent().getStringExtra("name");
        searchLine = (EditText)findViewById(R.id.dishesSearchLine);
        searchLine.setText(dishName);
        Client.request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Client.getDishesForName(dishes, dataSnapshot, dishName);
                if(dishes.size() == 0) {
                    findViewById(R.id.no_result).setVisibility(View.VISIBLE);
                } else {
                    List<Map<String, Object>> dishesPrint = new ArrayList<Map<String, Object>>();
                    for(Dish dish: dishes) {
                        Map<String, Object> hashmap = new HashMap<String, Object>();
                        hashmap.put("name", dish.name);
                        hashmap.put("addressname", dish.address + " " + dish.placeName);
                        hashmap.put("price", dish.price);
                        dishesPrint.add(hashmap);
                    }
                    ListAdapter adapter = new SimpleAdapter(DishesListPage.this, dishesPrint, R.layout.searchlistitem_row,
                            new String[] {"name", "addressname","price"}, new int[] {
                            R.id.text1, R.id.text2, R.id.text3});
                    ListView listView = (ListView) findViewById(R.id.tokens_list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(((parent, view, position, id) -> {
                        Intent intent = new Intent(DishesListPage.this, FoodPage.class);
                        intent.putExtra("dish", dishes.get(position));
                        startActivity(intent);
                    }));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void onFindDishesClick(View view) {
        Intent intent = new Intent(DishesListPage.this, DishesListPage.class);
        intent.putExtra("name", ((EditText)findViewById(R.id.dishesSearchLine)).getText().toString());
        startActivity(intent);
    }

    public void onSearchLineClick(View view) {
        if(searchLine.getText().toString().equals(getResources().getString(R.string.search_line))) {
            searchLine.setText("");
        }
    }
}
