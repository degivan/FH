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
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

public class PlacesListPage extends AppCompatActivity {

    private final List<Place> places = new ArrayList<Place>();
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list_page);
        String placeName = getIntent().getStringExtra("name");
        searchLine = (EditText)findViewById(R.id.placesSearchLine);
        searchLine.setText(placeName);
        Client.request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Client.getPlacesForName(places, dataSnapshot, placeName);
                if (places.size() == 0) {
                    findViewById(R.id.no_result).setVisibility(View.VISIBLE);
                } else {
                    List<Map<String, Object>> placesPrint = new ArrayList<>();
                    for(Place place: places) {
                        Map<String, Object> hashmap = new HashMap<>();
                        hashmap.put("name", place.name);
                        hashmap.put("address", place.address);
                        hashmap.put("price", Double.toString(Math.round(place.getRate())) + "/5");
                        placesPrint.add(hashmap);
                    }
                    ListAdapter adapter = new SimpleAdapter(PlacesListPage.this, placesPrint, R.layout.searchlistitem_row,
                            new String[] {"name", "address","price"}, new int[] {
                            R.id.text1, R.id.text2, R.id.text3});
                    ListView listView = (ListView) findViewById(R.id.tokens_list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(((parent, view, position, id) -> {
                        Intent intent = new Intent(PlacesListPage.this, PlacePage.class);
                        intent.putExtra("place", places.get(position));
                        startActivity(intent);
                    }));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void onFindPlacesClick(View view) {
        Intent intent = new Intent(PlacesListPage.this, PlacesListPage.class);
        intent.putExtra("name", ((EditText)findViewById(R.id.placesSearchLine)).getText().toString());
        startActivity(intent);
    }

    public void onSearchLineClick(View view) {
        if(searchLine.getText().toString().equals(getResources().getString(R.string.search_line))) {
            searchLine.setText("");
        }
    }
}
