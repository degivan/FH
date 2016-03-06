package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.R.id.foodRatingBar;

public class FoodPage extends AppCompatActivity {
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_page);
        dish = (Dish)getIntent().getExtras().get("dish");
        ((TextView)findViewById(R.id.foodNameText)).setText(dish.name);
        ((TextView)findViewById(R.id.foodPlaceNameText)).setText(dish.placeName);
        ((TextView)findViewById(R.id.foodAddressText)).setText(dish.address);
        ((TextView)findViewById(R.id.priceText)).setText(dish.price + " rub");
        ((TextView)findViewById(R.id.foodOpenHoursText)).setText("8.00-23.00");//add openHours to dish class
        ((TextView)findViewById(R.id.foodDescriptionText)).setText(dish.description);
        ((RatingBar)findViewById(R.id.foodRatingBar)).setRating((float)dish.getRate());
    }

    public void onFoodRateClick(View view) {
        Client.setDishRate(dish, ((RatingBar) findViewById(foodRatingBar)).getRating());
    }

    public void onPlacePageButtonClick(View view) {
        Client.request(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(FoodPage.this, PlacePage.class);
                intent.putExtra("place", Client.getPlace(dataSnapshot, dish.placeId));
                startActivity(intent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
