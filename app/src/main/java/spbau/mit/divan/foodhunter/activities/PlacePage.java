package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.displayMapObjectRate;

public class PlacePage extends AppCompatActivity {
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_page);
        place = (Place) getIntent().getExtras().get(PLACE_EXTRA_NAME);
        ((TextView) findViewById(R.id.placeNameText)).setText(place.getName());
        ((TextView) findViewById(R.id.placeAddressText)).setText(place.getAddress());
        ((TextView) findViewById(R.id.placeOpenHoursText)).setText(place.getOpenHours());
        displayRate();
    }

    public void onViewReviewsClick(View view) {
        Intent intent = new Intent(PlacePage.this, ReviewsList.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        startActivity(intent);
    }

    public void onWriteReviewClick(View view) {
        Intent intent = new Intent(PlacePage.this, NewReview.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        startActivity(intent);
        finish();
    }

    public void onViewMenuClick(View view) {
        Intent intent = new Intent(PlacePage.this, PlaceMenu.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        startActivity(intent);
    }

    public void onRatePlaceClick(View view) {
        Client.changePlaceRate(place, ((RatingBar) findViewById(R.id.ratingBar)).getRating());
        displayRate();
    }

    public void onShowOnMapClick(View view) {
        Intent intent = new Intent(PlacePage.this, ShowOnMap.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        startActivity(intent);
    }

    public void onAddNewDishClick(View view) {
        Intent intent = new Intent(PlacePage.this, AddDish.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        startActivity(intent);
        finish();
    }

    private void displayRate() {
        displayMapObjectRate(place, this, getResources().getString(R.string.t_place_rate_index));
    }
}
