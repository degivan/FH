package spbau.mit.divan.foodhunter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.Uses.CORRECT_PRICE;
import static spbau.mit.divan.foodhunter.activities.Uses.PLACE;
import static spbau.mit.divan.foodhunter.activities.Uses.showToast;

public class AddDish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
    }

    public void onAddNewDishClick(View view) {
        Place place = (Place) getIntent().getSerializableExtra(PLACE);
        String name = ((TextView) findViewById(R.id.newDishName)).getText().toString();
        String description = ((TextView) findViewById(R.id.newDishDescription)).getText().toString();
        String price = ((TextView) findViewById(R.id.newDishPrice)).getText().toString();
        if (price.matches(CORRECT_PRICE)) {
            Client.pushDish(Integer.parseInt(price), name, place.getAddress(), description,
                    place.getLatitude(), place.getLongitude(), place.getName(), place.getId());
            finish();
        } else {
            showToast(getApplicationContext(), "Incorrect price.");
        }
    }
}
