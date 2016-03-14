package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivity;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivityAndFinish;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.showToast;

public class AddDish extends AppCompatActivity {
    private static final String CORRECT_PRICE = "^-?\\d+$";
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        place = (Place) getIntent().getSerializableExtra(PLACE_EXTRA_NAME);
    }

    public void onAddNewDishClick(View view) {
            String name = ((TextView) findViewById(R.id.newDishName)).getText().toString();
            String description = ((TextView) findViewById(R.id.newDishDescription)).getText().toString();
            String price = ((TextView) findViewById(R.id.newDishPrice)).getText().toString();
            if (price.matches(CORRECT_PRICE)) {
                Client.pushDish(Integer.parseInt(price), name, description, place);
                openNetActivityAndFinish(this, placePageIntent());
            } else {
                showToast(getApplicationContext(), getResources().getString(R.string.m_incorrect_price));
            }
    }

    private Intent placePageIntent() {
        Intent intent = new Intent(AddDish.this, PlacePage.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        return intent;
    }

    @Override
    public void onBackPressed() {
        startActivity(placePageIntent());
        finish();
    }
}
