package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.net.Client;
import spbau.mit.divan.foodhunter.net.UserInfo;

import static spbau.mit.divan.foodhunter.R.id.ratingBar;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.DISH_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.displayMapObjectRate;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.onNetConnectedAction;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.showToast;

public class FoodPage extends AppCompatActivity {
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_page);
        dish = (Dish) getIntent().getExtras().get(DISH_EXTRA_NAME);
        ((TextView) findViewById(R.id.foodNameText)).setText(dish.getName());
        ((TextView) findViewById(R.id.foodPlaceNameText)).setText(dish.getPlaceName());
        ((TextView) findViewById(R.id.foodAddressText)).setText(dish.getAddress());
        ((TextView) findViewById(R.id.priceText)).setText(new StringBuilder()
                .append(String.valueOf(dish.getPrice()))
                .append(" rub").toString());
        ((TextView) findViewById(R.id.foodDescriptionText)).setText(dish.getDescription());
        displayRate();
        if (UserInfo.isDishRateChangedBefore(dish, this)) {
            ((RatingBar) findViewById(R.id.ratingBar)).setIsIndicator(true);
        }
    }

    public void onFoodRateClick(View view) {
        onNetConnectedAction(this, () -> {
            if (!UserInfo.isDishRateChangedBefore(dish, this)) {
                Client.changeDishRate(dish, ((RatingBar) findViewById(ratingBar)).getRating(), this);
                displayRate();
                ((RatingBar) findViewById(R.id.ratingBar)).setIsIndicator(true);
            } else {
                showToast(this, getResources().getString(R.string.already_rated));
            }
            return null;
        });
    }

    public void onPlacePageButtonClick(View view) {
        onNetConnectedAction(this, () -> {
            Client.request(snapshot -> {
                Intent intent = new Intent(FoodPage.this, PlacePage.class);
                intent.putExtra(PLACE_EXTRA_NAME, Client.getPlace(snapshot, dish.getPlaceId()));
                startActivity(intent);
            });
            return null;
        });
    }

    private void displayRate() {
        displayMapObjectRate(dish, this, getResources().getString(R.string.t_food_rate_index));
    }
}
