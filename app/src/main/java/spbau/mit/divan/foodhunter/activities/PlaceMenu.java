package spbau.mit.divan.foodhunter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.Uses.PLACE;

public class PlaceMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_menu);
        Place place = (Place) getIntent().getSerializableExtra(PLACE);
        Client.request(ItemListValueEventListeners.menuListener(place, this));
    }
}
