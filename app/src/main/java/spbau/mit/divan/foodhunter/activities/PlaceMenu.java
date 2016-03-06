package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

public class PlaceMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_menu);
        Client.request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Dish> menu = new ArrayList<Dish>();
                Client.getMenu(menu, dataSnapshot, (Place) getIntent().getSerializableExtra("place"));
                if(menu.size() == 0) {
                    findViewById(R.id.no_result).setVisibility(View.VISIBLE);
                } else {
                    List<Map<String, Object>> dishesPrint = new ArrayList<Map<String, Object>>();
                    for(Dish dish: menu) {
                        Map<String, Object> hashmap = new HashMap<String, Object>();
                        hashmap.put("name", dish.name);
                        hashmap.put("description", dish.description);
                        hashmap.put("price", dish.price);
                        dishesPrint.add(hashmap);
                    }
                    ListAdapter adapter = new SimpleAdapter(PlaceMenu.this, dishesPrint, R.layout.searchlistitem_row,
                            new String[] {"name", "description","price"}, new int[] {
                            R.id.text1, R.id.text2, R.id.text3});
                    ListView listView = (ListView) findViewById(R.id.menu_list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(((parent, view, position, id) -> {
                        Intent intent = new Intent(PlaceMenu.this, FoodPage.class);
                        intent.putExtra("dish", menu.get(position));
                        startActivity(intent);
                    }));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
