package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.R.id.foodRatingBar;
import static spbau.mit.divan.foodhunter.activities.Uses.DISH;
import static spbau.mit.divan.foodhunter.activities.Uses.PLACE;

public class FoodPage extends AppCompatActivity {
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_page);
        dish = (Dish) getIntent().getExtras().get(DISH);
        ((TextView) findViewById(R.id.foodNameText)).setText(dish.getName());
        ((TextView) findViewById(R.id.foodPlaceNameText)).setText(dish.getPlaceName());
        ((TextView) findViewById(R.id.foodAddressText)).setText(dish.getAddress());
        ((TextView) findViewById(R.id.priceText)).setText(new StringBuilder()
                .append(String.valueOf(dish.getPrice()))
                .append(" rub").toString());
        ((TextView) findViewById(R.id.foodDescriptionText)).setText(dish.getDescription());
        ((RatingBar) findViewById(R.id.foodRatingBar)).setRating((float) dish.getRate());
    }

    public void onFoodRateClick(View view) {
        Client.changeDishRate(dish, ((RatingBar) findViewById(foodRatingBar)).getRating());
    }

    public void onPlacePageButtonClick(View view) {
        Client.request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(FoodPage.this, PlacePage.class);
                intent.putExtra(PLACE, Client.getPlace(dataSnapshot, dish.getPlaceId()));
                startActivity(intent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
